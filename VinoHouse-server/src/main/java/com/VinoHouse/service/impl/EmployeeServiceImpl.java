package com.VinoHouse.service.impl;

import com.VinoHouse.constant.MessageConstant;
import com.VinoHouse.constant.PasswordConstant;
import com.VinoHouse.constant.StatusConstant;
import com.VinoHouse.context.BaseContext;
import com.VinoHouse.dto.EmployeeDTO;
import com.VinoHouse.dto.EmployeeLoginDTO;
import com.VinoHouse.entity.Employee;
import com.VinoHouse.exception.AccountLockedException;
import com.VinoHouse.exception.AccountNotFoundException;
import com.VinoHouse.exception.PasswordErrorException;
import com.VinoHouse.mapper.EmployeeMapper;
import com.VinoHouse.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        // 1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        // 2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            // 账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // MD5 加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        // 密码比对
        if (!password.equals(employee.getPassword())) {
            // 密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            // 账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        // 3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     */
    public void save(EmployeeDTO employeeDTO) {
        System.out.println("当前线程 id：" + Thread.currentThread().getId());
        Employee employee = new Employee();

        // 对象属性拷贝：employeeDTO → employee
        BeanUtils.copyProperties(employeeDTO, employee);

        // 封装 employeeDTO 不存在的属性
        // 1.设置账号的状态，默认正常状态：1表示正常 0表示锁定
        employee.setStatus(StatusConstant.ENABLE);

        // 2.设置密码，默认密码 123456
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        // 3.设置当前记录的创建时间和修改时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        // 4.设置当前记录创建人id和修改人id
        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.insert(employee);
    }
}
