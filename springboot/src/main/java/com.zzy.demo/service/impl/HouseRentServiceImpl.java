package com.zzy.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzy.demo.dto.HouseRentDto;
import com.zzy.demo.entity.HouseRent;
import com.zzy.demo.entity.Picture;
import com.zzy.demo.entity.User;
import com.zzy.demo.mapper.HouseRentMapper;
import com.zzy.demo.mapper.PictureMapper;
import com.zzy.demo.mapper.UserMapper;
import com.zzy.demo.service.HouseRentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HouseRentServiceImpl extends ServiceImpl<HouseRentMapper, HouseRent> implements HouseRentService {
    @Resource
    PictureMapper pictureMapper;
    @Resource
    UserMapper userMapper;
    private final int type=1;
    @Override
    public Page<HouseRentDto> pageWithDto(Page<HouseRent> page, Wrapper<HouseRent> wrapper) {
        Page<HouseRent> houseRentPage = this.page(page, wrapper);
        List<HouseRentDto> houseRentDtoList = houseRentPage.getRecords().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        Page<HouseRentDto> houseRentDtoPage = new Page<>();
        houseRentDtoPage.setRecords(houseRentDtoList);
        houseRentDtoPage.setTotal(houseRentPage.getTotal());
        houseRentDtoPage.setCurrent(houseRentPage.getCurrent());
        houseRentDtoPage.setSize(houseRentPage.getSize());
        return houseRentDtoPage;
    }

    private HouseRentDto convertToDto(HouseRent houseRent) {
        HouseRentDto dto = new HouseRentDto();
        dto.setId(houseRent.getId());
        dto.setAuthor(houseRent.getAuthor());
        dto.setTitle(houseRent.getTitle());
        dto.setContent(houseRent.getContent());
        dto.setPrice(houseRent.getPrice());
        dto.setProvince(houseRent.getProvince());
        dto.setCity(houseRent.getCity());
        dto.setTown(houseRent.getTown());
        dto.setArea(houseRent.getArea());
        if (houseRent.getTips() != null) {
            dto.setTips(Arrays.asList(houseRent.getTips().split("``")));
        }
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
        dto.setPhone(author.getPhone());
        return dto;
    }

    @Transactional
    @Override
    public boolean saveDto(HouseRentDto dto){
        HouseRent houseRent=new HouseRent(dto);
        this.getBaseMapper().insert(houseRent);
        List<String> pictureList=dto.getPictures();
        Picture picture=new Picture();
        for(int i=0;i<pictureList.size();i++){
            picture.setId(null);
            picture.setBelongId(houseRent.getId());
            picture.setNo(i);
            picture.setImage(pictureList.get(i));
            picture.setType(type);
            pictureMapper.insert(picture);
        }
        return true;
    }

    @Transactional
    @Override
    public boolean updateDtoById(HouseRentDto dto){
        HouseRent houseRent=new HouseRent(dto);
        this.getBaseMapper().updateById(houseRent);
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

    public HouseRentDto getDtoById(Long id){
        HouseRent houseRent = this.getBaseMapper().selectById(id);
        if (houseRent!=null) {
            return this.convertToDto(houseRent);
        }else {
            return null;
        }
    }

    @Override
    public long countByFlag(int flag) {
        QueryWrapper<HouseRent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("flag", flag);
        return this.count(queryWrapper);
    }
    @Override
    public List<Map<String, Object>> getCityStatistics(int flag) {
        QueryWrapper<HouseRent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("flag", flag); // 添加 flag 条件
        queryWrapper.select("city", "COUNT(*) AS count") // 选择需要统计的字段
                .groupBy("city"); // 按城市分组
        return this.listMaps(queryWrapper); // 使用 listMaps 返回统计结果
    }
    @Override
    public Map<String, Integer> getTipsStatistics(int flag) {
        QueryWrapper<HouseRent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("flag", flag); // 添加 flag 条件
        queryWrapper.isNotNull("tips"); // 确保 tips 字段不为空
        List<String> tipsList = this.list(queryWrapper).stream()
                .map(HouseRent::getTips) // 获取每个记录的 tips 字段
                .filter(tips -> tips != null && !tips.isEmpty()) // 过滤掉空的 tips
                .collect(Collectors.toList());

        Map<String, Integer> tipsCount = new HashMap<>();
        for (String tips : tipsList) {
            String[] splitTips = tips.split("``");
            for (String tip : splitTips) {
                tipsCount.put(tip, tipsCount.getOrDefault(tip, 0) + 1);
            }
        }
        return tipsCount;
    }
}
