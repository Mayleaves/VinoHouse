package com.VinoHouse.service;

import com.VinoHouse.dto.EmployeeDTO;
import com.VinoHouse.dto.EmployeeLoginDTO;
import com.VinoHouse.entity.Employee;

public interface EmployeeService {

    /**
     * 员工登录
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     */
    void save(EmployeeDTO employeeDTO);
}
