package com.VinoHouse.service.impl;

import com.VinoHouse.constant.MessageConstant;
import com.VinoHouse.constant.PasswordConstant;
import com.VinoHouse.constant.StatusConstant;
import com.VinoHouse.context.BaseContext;
import com.VinoHouse.dto.EmployeeDTO;
import com.VinoHouse.dto.EmployeeLoginDTO;
import com.VinoHouse.dto.EmployeePageQueryDTO;
import com.VinoHouse.dto.PasswordEditDTO;
import com.VinoHouse.entity.Employee;
import com.VinoHouse.entity.Setmeal;
import com.VinoHouse.exception.AccountLockedException;
import com.VinoHouse.exception.AccountNotFoundException;
import com.VinoHouse.exception.DeletionNotAllowedException;
import com.VinoHouse.exception.PasswordErrorException;
import com.VinoHouse.mapper.EmployeeMapper;
import com.VinoHouse.result.PageResult;
import com.VinoHouse.service.EmployeeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
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
        log.info("当前线程 id：" + Thread.currentThread().getId());
        Employee employee = new Employee();

        // 对象属性拷贝：employeeDTO → employee
        BeanUtils.copyProperties(employeeDTO, employee);

        // 封装 employeeDTO 不存在的属性
        // 1.设置账号的状态，默认正常状态：1表示正常 0表示锁定
        employee.setStatus(StatusConstant.ENABLE);

        // 2.设置密码，默认密码 123456
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        // 已经通过 AutoFill 赋值
        // 3.设置当前记录的创建时间和修改时间
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//
//        // 4.设置当前记录创建人 id 和修改人 id
//        employee.setCreateUser(BaseContext.getCurrentId());
//        employee.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.insert(employee);
    }

    /**
     * 分页查询：底层是基于 MySQL 的 limit 关键字实现分页查询
     */
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        // select * from employee limit 0,10
        //开始分页查询
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());

        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);

        long total = page.getTotal();
        List<Employee> records = page.getResult();

        return new PageResult(total, records);
    }

    /**
     * 启用、禁用员工账号
     */
    public void startOrStop(Integer status, Long id) {
        // update employee set status = ? where id = ?

        // 动态修改
        // 1.写法一
//        Employee employee = new Employee();
//        employee.setStatus(status);
//        employee.setId(id);
        // 2.写法二
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();

        employeeMapper.update(employee);
    }

    /**
     * 根据 id 查询员工信息
     */
    public Employee getById(Long id) {
        Employee employee = employeeMapper.getById(id);
        employee.setPassword("****");  // 回显到前端只会显示“****”，其目的是为了加强系统安全性。
        return employee;
    }

    /**
     * 编辑员工信息
     */
    public void update(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        // 已经通过 AutoFill 赋值
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.update(employee);
    }

    /**
     * 编辑员工密码
     */
    public void editPassword(PasswordEditDTO passwordEditDTO) {
        String oldPassword = passwordEditDTO.getOldPassword();  // 旧密码
        String newPassword = passwordEditDTO.getNewPassword();  // 新密码


        // 获取员工 id
        Long empId = BaseContext.getCurrentId();

        // 1. 根据 id 查询员工信息
        Employee employee = employeeMapper.getById(empId);

        // 2. 密码比对
        String encryptedOldPassword = DigestUtils.md5DigestAsHex(oldPassword.getBytes());
        if (!encryptedOldPassword.equals(employee.getPassword())) {
            // 密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_EDIT_FAILED);
        }

        // 3. 更新密码
        String encryptedNewPassword = DigestUtils.md5DigestAsHex(newPassword.getBytes());
        employee.setPassword(encryptedNewPassword);
        // AutoFill 赋值
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);
        employeeMapper.update(employee);

    }

    /**
     * 批量删除员工
     */
    @Transactional
    public void delete(Long id) {
        Employee employee = employeeMapper.getById(id);
        if (employee.getStatus() == StatusConstant.ENABLE) {
            // 启用中的员工账号不能删除
            throw new DeletionNotAllowedException(MessageConstant.ACCOUNT_ACTIVATION);
        }
        // 删除员工表中的数据
        employeeMapper.deleteById(id);
    }
}
