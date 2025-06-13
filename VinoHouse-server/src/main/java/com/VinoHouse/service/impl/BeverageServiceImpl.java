package com.VinoHouse.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.VinoHouse.constant.MessageConstant;
import com.VinoHouse.constant.StatusConstant;
import com.VinoHouse.dto.BeverageDTO;
import com.VinoHouse.dto.BeveragePageQueryDTO;
import com.VinoHouse.entity.Beverage;
import com.VinoHouse.entity.BeverageFlavor;
import com.VinoHouse.entity.Setmeal;
import com.VinoHouse.exception.DeletionNotAllowedException;
import com.VinoHouse.mapper.BeverageFlavorMapper;
import com.VinoHouse.mapper.BeverageMapper;
import com.VinoHouse.mapper.SetmealBeverageMapper;
import com.VinoHouse.mapper.SetmealMapper;
import com.VinoHouse.result.PageResult;
import com.VinoHouse.service.BeverageService;
import com.VinoHouse.vo.BeverageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class BeverageServiceImpl implements BeverageService {

    @Autowired
    private BeverageMapper beverageMapper;
    @Autowired
    private BeverageFlavorMapper beverageFlavorMapper;
    @Autowired
    private SetmealBeverageMapper setmealBeverageMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 新增酒水和对应的口味
     */
    @Transactional  // 原子性
    public void saveWithFlavor(BeverageDTO beverageDTO) {

        Beverage beverage = new Beverage();

        // 拷贝
        BeanUtils.copyProperties(beverageDTO, beverage);

        // 向酒水表插入 1 条数据
        beverageMapper.insert(beverage);

        // 获取 insert 语句生成的主键值
        Long beverageId = beverage.getId();

        List<BeverageFlavor> flavors = beverageDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(beverageFlavor -> {
                beverageFlavor.setBeverageId(beverageId);
            });
            // 向口味表插入 n 条数据
            beverageFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 酒水分页查询
     */
    public PageResult pageQuery(BeveragePageQueryDTO beveragePageQueryDTO) {
        PageHelper.startPage(beveragePageQueryDTO.getPage(), beveragePageQueryDTO.getPageSize());
        Page<BeverageVO> page = beverageMapper.pageQuery(beveragePageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 酒水批量删除
     */
    @Transactional
    public void deleteBatch(List<Long> ids) {
        // 判断当前酒水是否能够删除：是否存在起售中的酒水
        for (Long id : ids) {
            Beverage beverage = beverageMapper.getById(id);
            if (beverage.getStatus() == StatusConstant.ENABLE) {
                // 当前酒水处于起售中，不能删除
                throw new DeletionNotAllowedException(MessageConstant.BEVERAGE_ON_SALE);
            }
        }

        // 判断当前酒水是否能够删除：是否被套餐关联
        List<Long> setmealIds = setmealBeverageMapper.getSetmealIdsByBeverageIds(ids);
        if (setmealIds != null && setmealIds.size() > 0) {
            // 当前酒水被套餐关联，不能删除
            throw new DeletionNotAllowedException(MessageConstant.BEVERAGE_BE_RELATED_BY_SETMEAL);
        }

        // 1. 多条 SQL 语句
        // 删除酒水表中的酒水数据
//        for (Long id : ids) {
//            beverageMapper.deleteById(id);
//            // 删除酒水关联的口味数据
//            beverageFlavorMapper.deleteByBeverageId(id);
//        }

        // 2. 一条 SQL 语句
        // 根据酒水 id 集合批量删除酒水数据：delete from beverage where id in(?,?,?)
        beverageMapper.deleteByIds(ids);
        // 根据酒水 id 集合批量删除关联的口味数据：delete from beverage_flavor where beverage_id in(?,?,?)
        beverageFlavorMapper.deleteByBeverageIds(ids);
    }

    /**
     * 根据 id 查询酒水和对应的口味数据
     */
    public BeverageVO getByIdWithFlavor(Long id) {
        // 根据 id 查询酒水数据
        Beverage beverage = beverageMapper.getById(id);

        // 根据酒水 id 查询口味数据
        List<BeverageFlavor> beverageFlavors = beverageFlavorMapper.getByBeverageId(id);

        // 将查询到的数据封装到 VO
        BeverageVO beverageVO = new BeverageVO();
        BeanUtils.copyProperties(beverage, beverageVO);
        beverageVO.setFlavors(beverageFlavors);

        return beverageVO;
    }

    /**
     * 根据 id 修改酒水基本信息和对应的口味信息
     */
    public void updateWithFlavor(BeverageDTO beverageDTO) {
        Beverage beverage = new Beverage();
        BeanUtils.copyProperties(beverageDTO, beverage);

        // 修改酒水表基本信息
        beverageMapper.update(beverage);

        // 删除原有的口味数据
        beverageFlavorMapper.deleteByBeverageId(beverageDTO.getId());

        // 重新插入口味数据
        List<BeverageFlavor> flavors = beverageDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(beverageFlavor -> {
                beverageFlavor.setBeverageId(beverageDTO.getId());
            });
            // 向口味表插入 n 条数据
            beverageFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 酒水起售停售
     */
    @Transactional
    public void startOrStop(Integer status, Long id) {
        Beverage beverage = Beverage.builder()
                .id(id)
                .status(status)
                .build();
        beverageMapper.update(beverage);

        if (status == StatusConstant.DISABLE) {
            // 如果是停售操作，还需要将包含当前酒水的套餐也停售
            List<Long> beverageIds = new ArrayList<>();
            beverageIds.add(id);
            // select setmeal_id from setmeal_beverage where beverage_id in (?,?,?)
            List<Long> setmealIds = setmealBeverageMapper.getSetmealIdsByBeverageIds(beverageIds);
            if (setmealIds != null && setmealIds.size() > 0) {
                for (Long setmealId : setmealIds) {
                    Setmeal setmeal = Setmeal.builder()
                            .id(setmealId)
                            .status(StatusConstant.DISABLE)
                            .build();
                    setmealMapper.update(setmeal);
                }
            }
        }
    }

    /**
     * 根据分类 id 查询酒水
     */
    public List<Beverage> list(Long categoryId) {
        Beverage beverage = Beverage.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return beverageMapper.list(beverage);
    }

    /**
     * 条件查询酒水和口味
     */
    public List<BeverageVO> listWithFlavor(Beverage beverage) {
        List<Beverage> beverageList = beverageMapper.list(beverage);

        List<BeverageVO> beverageVOList = new ArrayList<>();

        for (Beverage d : beverageList) {
            BeverageVO beverageVO = new BeverageVO();
            BeanUtils.copyProperties(d, beverageVO);

            // 根据酒水 id 查询对应的口味
            List<BeverageFlavor> flavors = beverageFlavorMapper.getByBeverageId(d.getId());

            beverageVO.setFlavors(flavors);
            beverageVOList.add(beverageVO);
        }

        return beverageVOList;
    }
}
