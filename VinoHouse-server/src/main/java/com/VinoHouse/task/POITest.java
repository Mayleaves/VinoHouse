package com.VinoHouse.task;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class POITest {

    /**
     * 基于 POI 向 Excel 文件写入数据
     */
    public static void write() throws Exception{
        // 在内存中创建一个 Excel 文件对象
        XSSFWorkbook excel = new XSSFWorkbook();
        // 创建 Sheet 页
        XSSFSheet sheet = excel.createSheet("VinoHouse");

        // 在 Sheet 页中创建行，0 表示第 1 行
        XSSFRow row1 = sheet.createRow(0);
        // 创建单元格并在单元格中设置值，单元格编号也是从 0 开始，1 表示第 2 个单元格
        row1.createCell(0).setCellValue("姓名");
        row1.createCell(1).setCellValue("城市");

        XSSFRow row2 = sheet.createRow(1);
        row2.createCell(0).setCellValue("张木生");
        row2.createCell(1).setCellValue("北京");

        XSSFRow row3 = sheet.createRow(2);
        row3.createCell(0).setCellValue("阿权");
        row3.createCell(1).setCellValue("上海");

        // 通过输出流将内存中的 Excel 文件写入到磁盘上
        FileOutputStream out = new FileOutputStream(new File("D:\\VinoHouse.xlsx"));
        excel.write(out);

        // 关闭资源
        out.flush();
        out.close();
        excel.close();
    }

    /**
     * 基于 POI 读取 Excel 文件
     */
    public static void read() throws Exception{
        // 通过输入流读取指定的 Excel 文件
        FileInputStream in = new FileInputStream(new File("D:\\VinoHouse.xlsx"));
        // 从输入流中加载 Excel 文件内容到内存工作簿对象
        XSSFWorkbook excel = new XSSFWorkbook(in);

        // 获取 Excel 文件的第 1 个 Sheet 页
        XSSFSheet sheet = excel.getSheetAt(0);
        // 获取 Sheet 页中的最后一行的行号
        int lastRowNum = sheet.getLastRowNum();

        for (int i = 0; i <= lastRowNum; i++) {
            // 获取 Sheet 页中的行
            XSSFRow titleRow = sheet.getRow(i);
            // 获取行的第 1 个单元格
            XSSFCell cell1 = titleRow.getCell(0);
            // 获取单元格中的文本内容
            String cellValue1 = cell1.getStringCellValue();
            // 获取行的第 2 个单元格
            XSSFCell cell2 = titleRow.getCell(1);
            // 获取单元格中的文本内容
            String cellValue2 = cell2.getStringCellValue();

            System.out.println(cellValue1 + " " +cellValue2);
        }

        // 关闭资源
        in.close();
        excel.close();
    }

    public static void main(String[] args) throws Exception {
        write();
        read();
        System.out.println("success");
    }
}
