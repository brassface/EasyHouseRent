package com.zzy.demo.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzy.demo.dto.HouseRentDto;
import com.zzy.demo.dto.HouseTalkDto;
import com.zzy.demo.entity.HouseRent;
import com.zzy.demo.entity.HouseTalk;
import com.baomidou.mybatisplus.extension.service.IService;

public interface HouseTalkService extends IService<HouseTalk> {
    Page<HouseTalkDto> pageWithDto(Page<HouseTalk> page, Wrapper<HouseTalk> wrapper);
    boolean saveDto(HouseTalkDto dto);
    boolean updateDtoById(HouseTalkDto dto);
    boolean deleteDtoById(Long id);
    HouseTalkDto getDtoById(Long id);
}
