package com.zzy.demo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzy.demo.dto.HouseTalkDto;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("house_talk")
public class HouseTalk implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private Integer author;
    private String content;
    private String tips;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date date;
    private Integer flag;

    public HouseTalk(Integer id, String title, Integer author, String content, String tips, Date date, Integer flag) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.content = content;
        this.tips = tips;
        this.date = date;
        this.flag = flag;
    }
    public HouseTalk(HouseTalkDto dto){
        this.id=dto.getId();
        this.title=dto.getTitle();
        this.author=dto.getAuthor();
        this.content=dto.getContent();
        if (dto.getTips() != null && !dto.getTips().isEmpty()) {
            this.tips = String.join("``", dto.getTips());
        } else {
            this.tips = "";
        }
    }
}
