package com.VinoHouse.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.VinoHouse.constant.MessageConstant;
import com.VinoHouse.constant.StatusConstant;
import com.VinoHouse.context.BaseContext;
import com.VinoHouse.dto.CategoryDTO;
import com.VinoHouse.dto.CategoryPageQueryDTO;
import com.VinoHouse.entity.Category;
import com.VinoHouse.exception.DeletionNotAllowedException;
import com.VinoHouse.mapper.CategoryMapper;
import com.VinoHouse.mapper.BeverageMapper;
import com.VinoHouse.mapper.SetmealMapper;
import com.VinoHouse.result.PageResult;
import com.VinoHouse.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 分类业务层
 */
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private BeverageMapper beverageMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 新增分类
     */
    public void save(CategoryDTO categoryDTO) {
        Category category = new Category();
        // 属性拷贝
        BeanUtils.copyProperties(categoryDTO, category);

        // 分类状态默认为禁用状态0
        category.setStatus(StatusConstant.DISABLE);

        // 已经通过 AutoFill 赋值
        // 设置创建时间、修改时间、创建人、修改人
//        category.setCreateTime(LocalDateTime.now());
//        category.setUpdateTime(LocalDateTime.now());
//        category.setCreateUser(BaseContext.getCurrentId());
//        category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.insert(category);
    }

    /**
     * 分页查询
     */
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        // 下一条 sql 进行分页，自动加入 limit 关键字分页
        Page<Category> page = categoryMapper.pageQuery(categoryPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 根据 id 删除分类
     */
    public void deleteById(Long id) {
        // 查询当前分类是否关联了酒水，如果关联了就抛出业务异常
        Integer count = beverageMapper.countByCategoryId(id);
        if (count > 0) {
            // 当前分类下有酒水，不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_BEVERAGE);
        }

        // 查询当前分类是否关联了套餐，如果关联了就抛出业务异常
        count = setmealMapper.countByCategoryId(id);
        if (count > 0) {
            // 当前分类下有酒水，不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }

        // 删除分类数据
        categoryMapper.deleteById(id);
    }

    /**
     * 修改分类
     */
    public void update(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);

        // 已经通过 AutoFill 赋值
        // 设置修改时间、修改人
//        category.setUpdateTime(LocalDateTime.now());
//        category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.update(category);
    }

    /**
     * 启用、禁用分类
     */
    public void startOrStop(Integer status, Long id) {
        Category category = Category.builder()
                .id(id)
                .status(status)
                // 已经通过 AutoFill 赋值
//                .updateTime(LocalDateTime.now())
//                .updateUser(BaseContext.getCurrentId())
                .build();
        categoryMapper.update(category);
    }

    /**
     * 根据类型查询分类
     */
    public List<Category> list(Integer type) {
        return categoryMapper.list(type);
    }
}
