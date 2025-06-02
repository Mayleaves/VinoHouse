package com.VinoHouse.service;

import com.VinoHouse.dto.EmployeeLoginDTO;
import com.VinoHouse.entity.Employee;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

}
