package com.zzy.demo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.zzy.demo.dto.HouseRentDto;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("house_rent")
public class HouseRent implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String title;
    private String content;
    private BigDecimal price;
    private String province;
    private String city;
    private String town;
    private BigDecimal area;
    private Integer author;
    private String tips;
    private Integer flag;

    public HouseRent(Integer id, String title, String content, BigDecimal price, String province, String city, String town, BigDecimal area, Integer author, String tips, Integer flag) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.price = price;
        this.province = province;
        this.city = city;
        this.town = town;
        this.area = area;
        this.author = author;
        this.tips = tips;
        this.flag = flag;
    }

    public HouseRent(HouseRentDto dto){
        this.id=dto.getId();
        this.title=dto.getTitle();
        this.content=dto.getContent();
        this.price=dto.getPrice();
        this.province=dto.getProvince();
        this.city=dto.getCity();
        this.town=dto.getTown();
        this.area=dto.getArea();
        this.author=dto.getAuthor();
        if (dto.getTips() != null && !dto.getTips().isEmpty()) {
            this.tips = String.join("``", dto.getTips());
        } else {
            this.tips = "";
        }
    }
}
