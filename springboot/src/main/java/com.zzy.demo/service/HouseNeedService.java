package com.zzy.demo.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzy.demo.dto.HouseNeedDto;
import com.zzy.demo.dto.HouseRentDto;
import com.zzy.demo.entity.HouseNeed;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzy.demo.entity.HouseRent;

import java.util.List;
import java.util.Map;

public interface HouseNeedService extends IService<HouseNeed> {
    Page<HouseNeedDto> pageWithDto(Integer pageNum, Integer pageSize, String search, Integer author, String province, String city, String town);
    boolean saveDto(HouseNeedDto dto);
    boolean updateDtoById(HouseNeedDto dto);
    boolean deleteDtoById(Long id);
    HouseNeedDto getDtoById(Long id);
    long countByFlag(int i);
    List<Map<String, Object>> getCityStatistics(int i);
    Map<String, Integer> getTipsStatistics(int i);
}
