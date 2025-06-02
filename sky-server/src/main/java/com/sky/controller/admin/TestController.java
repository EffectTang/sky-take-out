package com.sky.controller.admin;/**
 * @description
 * @author starlord
 * @create 2025/5/17
 */

import com.sky.dto.EmployeeLoginDTO;
import com.sky.result.Result;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author starlord
 * @description
 * @create 2025/5/17
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @PostMapping("/json")
    public Result<?> test(@RequestBody EmployeeLoginDTO employeeLoginDTO){
        System.out.println("username:"+employeeLoginDTO.getUsername());
        System.out.println("password:"+employeeLoginDTO.getPassword());
        Result<String> result = Result.success("username:"+employeeLoginDTO.getUsername());
        result.setMsg("password:"+employeeLoginDTO.getPassword());
        return result;
    }

    @PostMapping("/single")
    public Result<?> single( String  per){
        System.out.println("password:"+per);
        Result<String> result = Result.success("username:"+per);
        return result;
    }

    @PostMapping("/source")
    public Result<?> source(@ModelAttribute EmployeeLoginDTO employeeLoginDTO){
        System.out.println("username:"+employeeLoginDTO.getUsername());
        System.out.println("password:"+employeeLoginDTO.getPassword());
        Result<String> result = Result.success("username:"+employeeLoginDTO.getUsername());
        result.setMsg("password:"+employeeLoginDTO.getPassword());
        return result;
    }

    @PostMapping("/hello")
    public Result<?> hello( @RequestParam String username, @RequestParam String password){
        System.out.println("username:"+username);
        System.out.println("password:"+password);
        Result<String> result = Result.success("username:"+username);
        result.setMsg("password:"+password);
        return result;
    }
}
