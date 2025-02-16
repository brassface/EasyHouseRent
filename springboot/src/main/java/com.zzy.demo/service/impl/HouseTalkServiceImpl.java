package com.zzy.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzy.demo.dto.HouseTalkDto;
import com.zzy.demo.entity.HouseTalk;
import com.zzy.demo.entity.HouseTalk;
import com.zzy.demo.entity.Picture;
import com.zzy.demo.entity.User;
import com.zzy.demo.mapper.HouseTalkMapper;
import com.zzy.demo.mapper.PictureMapper;
import com.zzy.demo.mapper.UserMapper;
import com.zzy.demo.service.HouseTalkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HouseTalkServiceImpl extends ServiceImpl<HouseTalkMapper, HouseTalk> implements HouseTalkService {
    @Resource
    PictureMapper pictureMapper;
    @Resource
    UserMapper userMapper;
    private int type=3;
    @Override
    public Page<HouseTalkDto> pageWithDto(Page<HouseTalk> page, Wrapper<HouseTalk> wrapper) {
        Page<HouseTalk> houseTalkPage = this.page(page, wrapper);

        List<HouseTalkDto> houseTalkDtoList = houseTalkPage.getRecords().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        Page<HouseTalkDto> houseTalkDtoPage = new Page<>();
        houseTalkDtoPage.setRecords(houseTalkDtoList);
        houseTalkDtoPage.setTotal(houseTalkPage.getTotal());
        houseTalkDtoPage.setCurrent(houseTalkPage.getCurrent());
        houseTalkDtoPage.setSize(houseTalkPage.getSize());
        return houseTalkDtoPage;
    }

    private HouseTalkDto convertToDto(HouseTalk houseTalk) {
        HouseTalkDto dto = new HouseTalkDto();
        dto.setId(houseTalk.getId());
        dto.setAuthor(houseTalk.getAuthor());
        dto.setTitle(houseTalk.getTitle());
        dto.setContent(houseTalk.getContent());
        if (houseTalk.getTips() != null) {
            dto.setTips(Arrays.asList(houseTalk.getTips().split("``")));
        }
        dto.setDate(houseTalk.getDate());
        dto.setPictures(pictureMapper.selectList(new LambdaQueryWrapper<Picture>()
                        .eq(Picture::getBelongId, dto.getId())
                        .eq(Picture::getType, type)
                        .eq(Picture::getFlag, 0)
                        .orderByAsc(Picture::getNo)).stream()
                .map(Picture::getImage)
                .collect(Collectors.toList()));
        User author = userMapper.selectById(dto.getAuthor());
        dto.setName(author.getName());
        dto.setImage(author.getImage());
        return dto;
    }

    @Transactional
    @Override
    public boolean saveDto(HouseTalkDto dto){
        HouseTalk houseTalk=new HouseTalk(dto);
        this.getBaseMapper().insert(houseTalk);
        List<String> pictureList=dto.getPictures();
        Picture picture=new Picture();
        for(int i=0;i<pictureList.size();i++){
            picture.setId(null);
            picture.setImage(pictureList.get(i));
            picture.setType(type);
            picture.setBelongId(houseTalk.getId());
            picture.setNo(i);
            pictureMapper.insert(picture);
        }
        return true;
    }

    @Transactional
    @Override
    public boolean updateDtoById(HouseTalkDto dto){
        HouseTalk houseTalk=new HouseTalk(dto);
        this.getBaseMapper().updateById(houseTalk);
        List<Picture> pictureList0=pictureMapper.selectList(new LambdaQueryWrapper<Picture>()
                .eq(Picture::getBelongId, dto.getId())
                .eq(Picture::getType, type)
                .eq(Picture::getFlag, 0));
        for(Picture pic:pictureList0){
            pic.setFlag(1);
            pictureMapper.updateById(pic);
        }
        pictureList0.clear();
        List<String> pictureList=dto.getPictures();
        Picture picture=new Picture();
        for(int i=0;i<pictureList.size();i++){
            picture.setId(null);
            picture.setImage(pictureList.get(i));
            picture.setType(type);
            picture.setBelongId(dto.getId());
            picture.setFlag(0);
            picture.setNo(i);
            pictureMapper.insert(picture);
        }
        return true;
    }

    @Transactional
    @Override
    public boolean deleteDtoById(Long id){
        this.getBaseMapper().deleteById(id);
        List<Picture> pictureList0=pictureMapper.selectList(new LambdaQueryWrapper<Picture>()
                .eq(Picture::getBelongId, id)
                .eq(Picture::getType, type)
                .eq(Picture::getFlag, 0));
        for(Picture pic:pictureList0){
            pic.setFlag(1);
            pictureMapper.updateById(pic);
        }
        return true;
    }

    public HouseTalkDto getDtoById(Long id){
        HouseTalk houseTalk = this.getBaseMapper().selectById(id);
        if (houseTalk!=null) {
            return this.convertToDto(houseTalk);
        }else {
            return null;
        }
    }
}
