package com.VinoHouse.controller.admin;

import com.VinoHouse.constant.JwtClaimsConstant;
import com.VinoHouse.dto.EmployeeDTO;
import com.VinoHouse.dto.EmployeeLoginDTO;
import com.VinoHouse.dto.EmployeePageQueryDTO;
import com.VinoHouse.entity.Employee;
import com.VinoHouse.properties.JwtProperties;
import com.VinoHouse.result.PageResult;
import com.VinoHouse.result.Result;
import com.VinoHouse.service.EmployeeService;
import com.VinoHouse.utils.JwtUtil;
import com.VinoHouse.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     */
    @PostMapping("/login")
    @ApiOperation(value = "员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),  // 过期时间
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     */
    @PostMapping("/logout")
    @ApiOperation("员工退出")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 新增员工：json
     * 非查询操作可以不写泛型 Result<T>，因为写了返回值也是空。
     */
    @PostMapping
    @ApiOperation("新增员工")
    public Result save(@RequestBody EmployeeDTO employeeDTO) {
        log.info("新增员工：{}", employeeDTO);
        log.info("当前线程 id：" + Thread.currentThread().getId());
        employeeService.save(employeeDTO);
        return Result.success();
    }

    /**
     * 员工分页查询：Query
     */
    @GetMapping("/page")
    @ApiOperation("员工分页查询")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("员工分页查询，参数为：{}", employeePageQueryDTO);
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启用、禁用员工账号
     * 路径参数需要 @PathVariable 修饰。
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用员工账号")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        log.info("启用、禁用员工账号：{}，{}", status, id);
        employeeService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * 根据 id 查询员工信息
     */
    @GetMapping("/{id}")
    @ApiOperation("根据 id 查询员工信息")
    public Result<Employee> getById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    /**
     * 编辑员工信息
     */
    @PutMapping
    @ApiOperation("编辑员工信息")
    public Result update(@RequestBody EmployeeDTO employeeDTO){
        log.info("编辑员工信息：{}", employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }
}
