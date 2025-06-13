package com.VinoHouse.service;

import com.VinoHouse.dto.CategoryDTO;
import com.VinoHouse.dto.CategoryPageQueryDTO;
import com.VinoHouse.entity.Category;
import com.VinoHouse.result.PageResult;

import java.util.List;

public interface CategoryService {

    /**
     * 新增分类
     */
    void save(CategoryDTO categoryDTO);

    /**
     * 分页查询
     */
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 根据 id 删除分类
     */
    void deleteById(Long id);

    /**
     * 修改分类
     */
    void update(CategoryDTO categoryDTO);

    /**
     * 启用、禁用分类
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据类型查询分类
     */
    List<Category> list(Integer type);
}
