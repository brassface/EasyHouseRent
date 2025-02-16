package com.zzy.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class HouseTalkDto {
    private Integer id;
    private String title;
    private String content;
    private Integer author;
    private List<String> tips;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date date;
    private List<String> pictures;
    private String name;
    private String image;
}
