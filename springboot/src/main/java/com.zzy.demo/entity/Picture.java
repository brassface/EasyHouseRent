package com.zzy.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("picture")
public class Picture implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer belongId;
    private Integer no;
    private String image;
    //0 user
    //1 rent
    //2 need
    //3 talk
    private Integer type;
    private Integer flag;
}
