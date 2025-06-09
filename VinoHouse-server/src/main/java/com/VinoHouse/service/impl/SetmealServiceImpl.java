package com.VinoHouse.service.impl;

import com.VinoHouse.constant.MessageConstant;
import com.VinoHouse.constant.StatusConstant;
import com.VinoHouse.dto.SetmealDTO;
import com.VinoHouse.dto.SetmealPageQueryDTO;
import com.VinoHouse.entity.Setmeal;
import com.VinoHouse.entity.SetmealBeverage;
import com.VinoHouse.exception.DeletionNotAllowedException;
import com.VinoHouse.exception.SetmealEnableFailedException;
import com.VinoHouse.mapper.BeverageMapper;
import com.VinoHouse.mapper.SetmealBeverageMapper;
import com.VinoHouse.mapper.SetmealMapper;
import com.VinoHouse.result.PageResult;
import com.VinoHouse.service.SetmealService;
import com.VinoHouse.vo.BeverageItemVO;
import com.VinoHouse.vo.SetmealVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.VinoHouse.entity.Beverage;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 套餐业务实现
 */
@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealBeverageMapper setmealBeverageMapper;
    @Autowired
    private BeverageMapper beverageMapper;

    /**
     * 新增套餐，同时需要保存套餐和酒水的关联关系
     */
    @Transactional
    public void saveWithBeverage(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        // 向套餐表插入数据
        setmealMapper.insert(setmeal);

        // 获取生成的套餐 id
        Long setmealId = setmeal.getId();

        List<SetmealBeverage> setmealBeverages = setmealDTO.getSetmealDishes();  // 这里的 getSetmealDishes 不能修改，要和前端保持一致 json

        setmealBeverages.forEach(setmealBeverage -> {
            // 1. 设置酒水 ID：通过酒水 name 在 beverage 查找酒水 ID 赋值给 setmeal_beverage
            Beverage query = new Beverage();
            query.setName(setmealBeverage.getName());
            List<Beverage> beverages = beverageMapper.list(query); // 逐条查询
            setmealBeverage.setBeverageId(beverages.get(0).getId());  // 名称唯一，取第一个结果

            // 设置套餐 ID
            setmealBeverage.setSetmealId(setmealId);
        });

        // 保存套餐和酒水的关联关系
        setmealBeverageMapper.insertBatch(setmealBeverages);
    }

    /**
     * 分页查询
     */
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        int pageNum = setmealPageQueryDTO.getPage();
        int pageSize = setmealPageQueryDTO.getPageSize();

        PageHelper.startPage(pageNum, pageSize);
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 批量删除套餐
     */
    @Transactional
    public void deleteBatch(List<Long> ids) {
        ids.forEach(id -> {
            Setmeal setmeal = setmealMapper.getById(id);
            if (StatusConstant.ENABLE == setmeal.getStatus()) {
                // 起售中的套餐不能删除
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });

        ids.forEach(setmealId -> {
            // 删除套餐表中的数据
            setmealMapper.deleteById(setmealId);
            // 删除套餐酒水关系表中的数据
            setmealBeverageMapper.deleteBySetmealId(setmealId);
        });
    }

    /**
     * 根据 id 查询套餐和套餐酒水关系
     */
    public SetmealVO getByIdWithBeverage(Long id) {
        SetmealVO setmealVO = setmealMapper.getByIdWithBeverage(id);
        return setmealVO;
    }

    /**
     * 修改套餐
     */
    @Transactional
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        // 1、修改套餐表，执行 update
        setmealMapper.update(setmeal);

        // 套餐 id
        Long setmealId = setmealDTO.getId();

        // 2、删除套餐和酒水的关联关系，操作 setmeal_beverage 表，执行 delete
        setmealBeverageMapper.deleteBySetmealId(setmealId);

        List<SetmealBeverage> setmealBeverages = setmealDTO.getSetmealDishes();
        setmealBeverages.forEach(setmealBeverage -> {
            setmealBeverage.setSetmealId(setmealId);
        });
        // 3、重新插入套餐和酒水的关联关系，操作 setmeal_beverage 表，执行 insert
        setmealBeverageMapper.insertBatch(setmealBeverages);
    }

    /**
     * 套餐起售、停售
     */
    public void startOrStop(Integer status, Long id) {
        // 起售套餐时，判断套餐内是否有停售酒水，有停售酒水提示"套餐内包含未启售酒水，无法启售"
        if (status == StatusConstant.ENABLE) {
            // select a.* from beverage a left join setmeal_beverage b on a.id = b.beverage_id where b.setmeal_id = ?
            List<Beverage> beverageList = beverageMapper.getBySetmealId(id);
            if (beverageList != null && beverageList.size() > 0) {
                beverageList.forEach(beverage -> {
                    if (StatusConstant.DISABLE == beverage.getStatus()) {
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }
        }

        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.update(setmeal);
    }
//
//    /**
//     * 条件查询
//     */
//    public List<Setmeal> list(Setmeal setmeal) {
//        List<Setmeal> list = setmealMapper.list(setmeal);
//        return list;
//    }
//
//    /**
//     * 根据 id 查询酒水选项
//     */
//    public List<BeverageItemVO> getBeverageItemById(Long id) {
//        return setmealMapper.getBeverageItemBySetmealId(id);
//    }
}
