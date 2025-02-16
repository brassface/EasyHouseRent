package com.zzy.demo.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class HouseNeedDto {
    private Integer id;
    private String title;
    private String content;
    private BigDecimal price;
    private String province;
    private String city;
    private String town;
    private BigDecimal area;
    private Integer author;
    private List<String> tips;
    private List<String> pictures;
    private String name;
    private String image;
    private String phone;
}
