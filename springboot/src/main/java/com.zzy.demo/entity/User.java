package com.zzy.demo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("user")
public class User implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String address;
    private Integer role;
    private String image;
    private Integer flag;
}
