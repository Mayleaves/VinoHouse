package com.VinoHouse.service;

import com.VinoHouse.dto.EmployeeDTO;
import com.VinoHouse.dto.EmployeeLoginDTO;
import com.VinoHouse.dto.EmployeePageQueryDTO;
import com.VinoHouse.entity.Employee;
import com.VinoHouse.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * 分页查询
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用、禁用员工账号
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据 id 查询员工信息
     */
    Employee getById(Long id);

    /**
     * 编辑员工信息
     */
    void update(EmployeeDTO employeeDTO);
}
