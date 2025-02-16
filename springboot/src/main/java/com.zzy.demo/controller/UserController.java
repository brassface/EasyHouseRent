package com.zzy.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzy.demo.common.Result;
import com.zzy.demo.entity.User;
import com.zzy.demo.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService userservice;

    //添加
    @PostMapping
    public Result<?> save(@RequestBody User entity) {
        if (entity.getPassword() == null) {
            entity.setPassword("123456");
        }
        int back = userservice.saveUser(entity);
        if (back == 0) {
            return Result.success(entity);
        } else if (back == 1) {
            return Result.error(String.valueOf(back), "用户名已存在");
        } else {
            return Result.error(String.valueOf(back), "注册失败");
        }
    }

    //修改
    @PutMapping
    public Result<?> update(@RequestBody User entity) {
        userservice.updateById(entity);
        return Result.success();
    }

    //删除
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        userservice.deleteUserById(id);
        return Result.success();
    }

    //根据id查询
    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        User entity = userservice.getById(id);
        return Result.success(entity);
    }

    //分页查询
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "") String search) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getFlag, 0);
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(User::getName, search);
        }
        Page<User> entityPage = userservice.page(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(entityPage);
    }

    //登录
    @PostMapping("/login")
    public Result<?> login(@RequestBody User entity) {
        User res = userservice.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, entity.getUsername()).eq(User::getPassword, entity.getPassword()));
        if (res == null) {
            return Result.error("-1", "用户名或密码错误");
        }
        return Result.success(res);
    }

    //注册
    @PostMapping("/register")
    public Result<?> register(@RequestBody User entity) {
        int back = userservice.saveUser(entity);
        if (back == 0) {
            return Result.success(entity);
        } else if (back == 1) {
            return Result.error(String.valueOf(back), "用户名已存在");
        } else {
            return Result.error(String.valueOf(back), "注册失败");
        }
    }

    //修改密码
    @PostMapping("/password")
    public Result<?> EditPw(@RequestBody Map<String, Object> request) {
        System.out.println(request.keySet());
        Integer id = (Integer) request.get("id");
        String oldPw = (String) request.get("oldPw");
        String newPw = (String) request.get("newPw");
        int back = userservice.EditPw(new Long(id), oldPw, newPw);
        if (back == 0) {
            return Result.success();
        } else if (back == 1) {
            return Result.error(String.valueOf(back), "原密码错误，请重新输入或联系管理员重置");
        } else {
            return Result.error(String.valueOf(back), "修改失败");
        }
    }
}
