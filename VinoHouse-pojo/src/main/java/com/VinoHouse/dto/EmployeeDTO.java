package com.VinoHouse.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 封装前端传递的参数列表
 */
@Data
public class EmployeeDTO implements Serializable {

    private Long id;

    private String username;

    private String name;

    private String phone;

    private String sex;

    private String idNumber;

}
