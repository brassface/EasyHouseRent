package com.zzy.demo.service;

import com.zzy.demo.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<User> {
    int saveUser(User entity);
    boolean deleteUserById(Long id);

    int EditPw(Long id, String oldPw, String newPw);
}
