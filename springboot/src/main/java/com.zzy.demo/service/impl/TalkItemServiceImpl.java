package com.zzy.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzy.demo.dto.TalkItemDto;
import com.zzy.demo.dto.TalkItemDto;
import com.zzy.demo.entity.*;
import com.zzy.demo.entity.TalkItem;
import com.zzy.demo.mapper.TalkItemMapper;
import com.zzy.demo.mapper.TalkItemMapper;
import com.zzy.demo.mapper.UserMapper;
import com.zzy.demo.service.TalkItemService;
import com.zzy.demo.service.TalkItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TalkItemServiceImpl extends ServiceImpl<TalkItemMapper, TalkItem> implements TalkItemService {
    @Resource
    UserMapper userMapper;

    @Override
    public Page<TalkItemDto> pageWithDto(Page<TalkItem> page, Wrapper<TalkItem> wrapper) {
        Page<TalkItem> talkItemPage = this.page(page, wrapper);

        List<TalkItemDto> talkItemDtoList = talkItemPage.getRecords().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        Page<TalkItemDto> talkItemDtoPage = new Page<>();
        talkItemDtoPage.setRecords(talkItemDtoList);
        talkItemDtoPage.setTotal(talkItemPage.getTotal());
        talkItemDtoPage.setCurrent(talkItemPage.getCurrent());
        talkItemDtoPage.setSize(talkItemPage.getSize());
        return talkItemDtoPage;
    }

    private TalkItemDto convertToDto(TalkItem talkItem) {
        TalkItemDto dto = new TalkItemDto();
        dto.setId(talkItem.getId());
        dto.setAuthor(talkItem.getAuthor());
        dto.setContent(talkItem.getContent());
        dto.setDate(talkItem.getDate());

        User author = userMapper.selectById(dto.getAuthor());
        dto.setName(author.getName());
        dto.setImage(author.getImage());
        return dto;
    }
}
