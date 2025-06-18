package com.VinoHouse.service.impl;

import com.VinoHouse.dto.GoodsSalesDTO;
import com.VinoHouse.entity.Orders;
import com.VinoHouse.mapper.OrderMapper;
import com.VinoHouse.mapper.UserMapper;
import com.VinoHouse.service.ReportService;
import com.VinoHouse.service.WorkspaceService;
import com.VinoHouse.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WorkspaceService workspaceService;

    /**
     * 统计指定时间区间内的营业额数据
     * 营业额：指状态为“5已完成”的订单金额合计
     */
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        // 当前集合用于存放从 begin 到 end 范围内的每天的日期
        List<LocalDate> dateList = new ArrayList<>();

        dateList.add(begin);  // 加入第一天
        while (!begin.equals(end)) {
            // 日期计算，计算指定日期的后一天对应的日期
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        // 存放每天的营业额
        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            // 查询 date 日期对应的营业额数据
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);  // 00:00:00
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);  // 23:59:59

            // 查询“已完成”的订单金额：select sum(amount) from orders where order_time > beginTime and order_time < endTime and status = 5
            Double turnover = getTurnoverCount(beginTime, endTime);
            // turnover = turnover == null ? 0.0 : turnover;  // 已合并在 sql 语法
            turnoverList.add(turnover);
        }

        // 封装返回结果
        return TurnoverReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ","))  // dateList 的值以 , 分割
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

    /**
     * 根据时间区间统计营业额数据
     */
    private Double getTurnoverCount(LocalDateTime beginTime, LocalDateTime endTime) {
        Map map = new HashMap();
        map.put("begin", beginTime);  // key 对应 mapper 中 test
        map.put("end", endTime);
        map.put("status", Orders.COMPLETED);
        return orderMapper.sumByMap(map);
    }

    /**
     * 统计指定时间区间内的用户数据
     */
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        // 存放从 begin 到 end 之间的每天对应的日期
        List<LocalDate> dateList = new ArrayList<>();

        dateList.add(begin);

        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        List<Integer> newUserList = new ArrayList<>();  // 新增用户数
        List<Integer> totalUserList = new ArrayList<>();  // 总用户数

        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            // 新增用户数量：select count(id) from user where create_time > beginTime and create_time < endTime
            Integer newUser = getUserCount(beginTime, endTime);
            // 总用户数量：select count(id) from user where  create_time < endTime
            Integer totalUser = getUserCount(null, endTime);

            newUserList.add(newUser);
            totalUserList.add(totalUser);
        }

        // 封装结果数据
        return UserReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .build();
    }

    /**
     * 根据时间区间统计用户数量
     */
    private Integer getUserCount(LocalDateTime beginTime, LocalDateTime endTime) {
        Map map = new HashMap();
        map.put("begin", beginTime);
        map.put("end", endTime);
        return userMapper.countByMap(map);
    }

    /**
     * 统计指定时间区间内的订单数据
     */
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
        // 存放从 begin 到 end 之间的每天对应的日期
        List<LocalDate> dateList = new ArrayList<>();

        dateList.add(begin);

        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        // 存放每天的订单总数
        List<Integer> orderCountList = new ArrayList<>();
        // 存放每天的有效订单数
        List<Integer> validOrderCountList = new ArrayList<>();

        // 遍历 dateList 集合，查询每天的有效订单数和订单总数
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            // 查询每天的订单总数：select count(id) from orders where order_time > beginTime and order_time < endTime
            Integer orderCount = getOrderCount(beginTime, endTime, null);
            // 查询每天的有效订单数：select count(id) from orders where order_time > beginTime and order_time < endTime and status = 5
            Integer validOrderCount = getOrderCount(beginTime, endTime, Orders.COMPLETED);

            orderCountList.add(orderCount);
            validOrderCountList.add(validOrderCount);
        }

        // 计算时间区间内的订单总数量
        Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();

        // 计算时间区间内的有效订单数量
        Integer validOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();

        Double orderCompletionRate = 0.0;
        if (totalOrderCount != 0) {  // 防止除以 0
            // 计算订单完成率
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
        }

        // 封装返回结果数据
        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(orderCountList, ","))
                .validOrderCountList(StringUtils.join(validOrderCountList, ","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    /**
     * 根据条件统计订单数量
     */
    private Integer getOrderCount(LocalDateTime begin, LocalDateTime end, Integer status) {
        Map map = new HashMap();
        map.put("begin", begin);
        map.put("end", end);
        map.put("status", status);

        return orderMapper.countByMap(map);
    }

    /**
     * 统计指定时间区间内的销量排名前 10
     */
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        // select od.name, sum(od.number) number from order_detail od, orders o where od.order_id = o.id and o.status = 5 and o.order_time > beginTime and o.order_time < endTime
        // group by od.name order by number limit 0, 10
        List<GoodsSalesDTO> salesTop10 = orderMapper.getSalesTop10(beginTime, endTime);
        // 转换格式
        List<String> names = salesTop10.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        List<Integer> numbers = salesTop10.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());

        // 封装返回结果数据
        return SalesTop10ReportVO
                .builder()
                .nameList(StringUtils.join(names, ","))
                .numberList(StringUtils.join(numbers, ","))
                .build();
    }

    /**
     * 导出运营数据报表
     */
    public void exportBusinessData(HttpServletResponse response) {
        // 1. 查询数据库，获取营业数据 —— 查询最近 30 天的运营数据
        LocalDate dateBegin = LocalDate.now().minusDays(30);  // 减去 30 天
        LocalDate dateEnd = LocalDate.now().minusDays(1);

        // 查询概览数据
        BusinessDataVO businessDataVO = workspaceService.getBusinessData(LocalDateTime.of(dateBegin, LocalTime.MIN), LocalDateTime.of(dateEnd, LocalTime.MAX));

        // 2. 通过 POI 将数据写入到 Excel 文件中
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");

        try {
            // 基于模板文件创建一个新的 Excel 文件
            XSSFWorkbook excel = new XSSFWorkbook(in);

            // 获取表格文件的 Sheet 页
            XSSFSheet sheet = excel.getSheet("Sheet1");

            // 填充时间（运营数据报表下）
            sheet.getRow(1).getCell(1).setCellValue("时间：" + dateBegin + " 至 " + dateEnd);

            // 概览数据
            // 获得第 4 行
            XSSFRow row = sheet.getRow(3);
            row.getCell(2).setCellValue(businessDataVO.getTurnover());  // 营业额
            row.getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());  // 订单完成率
            row.getCell(6).setCellValue(businessDataVO.getNewUsers());  // 新增用户数
            // 获得第 5 行
            row = sheet.getRow(4);
            row.getCell(2).setCellValue(businessDataVO.getValidOrderCount());  // 有效订单
            row.getCell(4).setCellValue(businessDataVO.getUnitPrice());  // 平均客单价

            // 填充明细数据：遍历 30 天
            for (int i = 0; i < 30; i++) {
                LocalDate date = dateBegin.plusDays(i);
                // 查询某一天的营业数据
                BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));

                // 获得某一行
                row = sheet.getRow(7 + i);  // 第 7 行开始
                row.getCell(1).setCellValue(date.toString());  // 日期
                row.getCell(2).setCellValue(businessData.getTurnover());  // 营业额
                row.getCell(3).setCellValue(businessData.getValidOrderCount());  // 有效订单
                row.getCell(4).setCellValue(businessData.getOrderCompletionRate());  // 订单完成率
                row.getCell(5).setCellValue(businessData.getUnitPrice());  // 平均客单价
                row.getCell(6).setCellValue(businessData.getNewUsers());  // 新增用户数
            }

            // 3. 通过输出流将 Excel 文件下载到客户端浏览器
            ServletOutputStream out = response.getOutputStream();
            excel.write(out);

            // 关闭资源
            out.close();
            excel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
