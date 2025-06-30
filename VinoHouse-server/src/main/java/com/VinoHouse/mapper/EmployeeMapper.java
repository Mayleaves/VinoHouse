package com.VinoHouse.mapper;

import com.VinoHouse.annotation.AutoFill;
import com.VinoHouse.dto.EmployeePageQueryDTO;
import com.VinoHouse.entity.Employee;
import com.VinoHouse.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.github.pagehelper.Page;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工：注解实现 SQL
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 插入员工数据：单表操作
     * 注意：id_number（下划线） 对应 idNumber（驼峰）
     */
    @Insert("insert into employee (name, username, password, phone, sex, id_number, create_time, update_time, create_user, update_user,status) " +
            "values " +
            "(#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{createTime},#{updateTime},#{createUser},#{updateUser},#{status})")
    @AutoFill(value = OperationType.INSERT)
    void insert(Employee employee);

    /**
     * 分页查询：动态 SQL
     */
    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 根据主键动态修改属性
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Employee employee);

    /**
     * 根据 id 查询员工信息
     */
    @Select("select * from employee where id = #{id}")
    Employee getById(Long id);

    /**
     * 根据 id 删除员工
     */
    @Delete("delete from employee where id = #{id}")
    void deleteById(Long employeeId);
}
