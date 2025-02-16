package com.zzy.demo.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzy.demo.dto.HouseTalkDto;
import com.zzy.demo.dto.TalkItemDto;
import com.zzy.demo.entity.HouseTalk;
import com.zzy.demo.entity.TalkItem;

public interface TalkItemService extends IService<TalkItem> {
    Page<TalkItemDto> pageWithDto(Page<TalkItem> page, Wrapper<TalkItem> wrapper);
}
