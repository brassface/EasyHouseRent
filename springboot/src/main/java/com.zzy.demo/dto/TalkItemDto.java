package com.zzy.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class TalkItemDto {
    private Integer id;
    private String content;
    private Integer belongItem;
    private Integer author;
    private String image;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date date;
    private boolean showFull = false;
    private Integer flag;
}
