package com.zzy.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzy.demo.dto.HouseNeedDto;
import com.zzy.demo.entity.*;
import com.zzy.demo.entity.HouseNeed;
import com.zzy.demo.mapper.HouseNeedMapper;
import com.zzy.demo.mapper.PictureMapper;
import com.zzy.demo.mapper.UserMapper;
import com.zzy.demo.service.HouseNeedService;
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
public class HouseNeedServiceImpl extends ServiceImpl<HouseNeedMapper, HouseNeed> implements HouseNeedService {
    @Resource
    PictureMapper pictureMapper;
    @Resource
    UserMapper userMapper;
    private final int type=2;

    @Override
    public Page<HouseNeedDto> pageWithDto(Page<HouseNeed> page, Wrapper<HouseNeed> wrapper) {
        Page<HouseNeed> houseNeedPage = this.page(page, wrapper);

        List<HouseNeedDto> houseNeedDtoList = houseNeedPage.getRecords().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        Page<HouseNeedDto> houseNeedDtoPage = new Page<>();
        houseNeedDtoPage.setRecords(houseNeedDtoList);
        houseNeedDtoPage.setTotal(houseNeedPage.getTotal());
        houseNeedDtoPage.setCurrent(houseNeedPage.getCurrent());
        houseNeedDtoPage.setSize(houseNeedPage.getSize());
        return houseNeedDtoPage;
    }

    private HouseNeedDto convertToDto(HouseNeed houseNeed) {
        HouseNeedDto dto = new HouseNeedDto();
        dto.setId(houseNeed.getId());
        dto.setAuthor(houseNeed.getAuthor());
        dto.setTitle(houseNeed.getTitle());
        dto.setContent(houseNeed.getContent());
        dto.setPrice(houseNeed.getPrice());
        dto.setProvince(houseNeed.getProvince());
        dto.setCity(houseNeed.getCity());
        dto.setTown(houseNeed.getTown());
        dto.setArea(houseNeed.getArea());
        if (houseNeed.getTips() != null) {
            dto.setTips(Arrays.asList(houseNeed.getTips().split("``")));
        }
        System.out.println(dto);
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
    public boolean saveDto(HouseNeedDto dto){
        HouseNeed houseNeed=new HouseNeed(dto);
        this.getBaseMapper().insert(houseNeed);
        List<String> pictureList=dto.getPictures();
        Picture picture=new Picture();
        for(int i=0;i<pictureList.size();i++){
            picture.setId(null);
            picture.setImage(pictureList.get(i));
            picture.setType(type);
            picture.setBelongId(houseNeed.getId());
            picture.setNo(i);
            pictureMapper.insert(picture);
        }
        return true;
    }

    @Transactional
    @Override
    public boolean updateDtoById(HouseNeedDto dto){
        HouseNeed houseNeed=new HouseNeed(dto);
        this.getBaseMapper().updateById(houseNeed);
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

    public HouseNeedDto getDtoById(Long id){
        HouseNeed houseNeed = this.getBaseMapper().selectById(id);
        if (houseNeed!=null) {
            return this.convertToDto(houseNeed);
        }else {
            return null;
        }
    }
    @Override
    public long countByFlag(int flag) {
        QueryWrapper<HouseNeed> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("flag", flag);
        return this.count(queryWrapper);
    }
    @Override
    public List<Map<String, Object>> getCityStatistics(int flag) {
        QueryWrapper<HouseNeed> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("flag", flag); // 添加 flag 条件
        queryWrapper.select("city", "COUNT(*) AS count") // 选择需要统计的字段
                .groupBy("city"); // 按城市分组
        return this.listMaps(queryWrapper); // 使用 listMaps 返回统计结果
    }
    @Override
    public Map<String, Integer> getTipsStatistics(int flag) {
        QueryWrapper<HouseNeed> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("flag", flag); // 添加 flag 条件
        queryWrapper.isNotNull("tips"); // 确保 tips 字段不为空
        List<String> tipsList = this.list(queryWrapper).stream()
                .map(HouseNeed::getTips) // 获取每个记录的 tips 字段
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
