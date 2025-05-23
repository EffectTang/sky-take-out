package com.sky.service;

import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    Integer insert(Employee employee);

    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    int changeEmployeeStatus(Integer status, Long id);

    Employee getById(Long id);

    int updateEmployee(Employee employee);
}
