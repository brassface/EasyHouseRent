package com.zzy.demo.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzy.demo.dto.HouseRentDto;
import com.zzy.demo.entity.HouseRent;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface HouseRentService extends IService<HouseRent> {
    Page<HouseRentDto> pageWithDto(Page<HouseRent> page, Wrapper<HouseRent> wrapper);
    boolean saveDto(HouseRentDto dto);
    boolean updateDtoById(HouseRentDto dto);
    boolean deleteDtoById(Long id);
    HouseRentDto getDtoById(Long id);
    long countByFlag(int i);
    List<Map<String, Object>> getCityStatistics(int i);
    Map<String, Integer> getTipsStatistics(int i);
}
