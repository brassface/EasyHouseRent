package com.zzy.demo.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzy.demo.dto.HouseNeedDto;
import com.zzy.demo.entity.*;
import com.zzy.demo.mapper.HouseNeedMapper;
import com.zzy.demo.mapper.PictureMapper;
import com.zzy.demo.mapper.UserMapper;
import com.zzy.demo.service.HouseNeedService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import com.zzy.demo.utils.RedisUtil;

@Service
public class HouseNeedServiceImpl extends ServiceImpl<HouseNeedMapper, HouseNeed> implements HouseNeedService {

    @Resource
    private PictureMapper pictureMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedisUtil redisUtil;

    private final int type = 2;
    private static final String HOUSE_NEED_CACHE_PREFIX = "house_need:";

    // 根据ID查询
    @Override
    public HouseNeedDto getDtoById(Long id) {
        String cacheKey = HOUSE_NEED_CACHE_PREFIX + id;

        // 先尝试从缓存中获取
        HouseNeedDto cachedDto = redisUtil.getCache(cacheKey, HouseNeedDto.class);
        if (cachedDto != null) {
            return cachedDto; // 如果缓存中有数据，直接返回
        }

        HouseNeed houseNeed = this.getBaseMapper().selectById(id);
        if (houseNeed != null) {
            HouseNeedDto dto = this.convertToDto(houseNeed);
            redisUtil.setCache(cacheKey, dto, 10, TimeUnit.MINUTES);
            return dto;
        } else {
            return null;
        }
    }

    // 分页查询
    @Override
    public Page<HouseNeedDto> pageWithDto(Integer pageNum, Integer pageSize, String search, Integer author, String province, String city, String town) {
        String cacheKey = HOUSE_NEED_CACHE_PREFIX + "page:"+ pageNum + "_" + pageSize + "_" + search + "_" + author + "_" + province + "_" + city + "_" + town;
        Page<HouseNeedDto> cachedPage = redisUtil.getCache(cacheKey, Page.class);
        if (cachedPage != null) {
            return cachedPage;
        }
        LambdaQueryWrapper<HouseNeed> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(HouseNeed::getFlag, 0);
        if (author > 0) {
            wrapper.eq(HouseNeed::getAuthor, author);
        }
        if (StrUtil.isNotBlank(province)) {
            wrapper.eq(HouseNeed::getProvince, province);
            if (StrUtil.isNotBlank(city)) {
                wrapper.eq(HouseNeed::getCity, city);
                if (StrUtil.isNotBlank(town)) {
                    wrapper.eq(HouseNeed::getTown, town);
                }
            }
        }
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(HouseNeed::getTitle, search)
                    .or()
                    .like(HouseNeed::getTips, search)
                    .or()
                    .like(HouseNeed::getProvince, search)
                    .or()
                    .like(HouseNeed::getCity, search)
                    .or()
                    .like(HouseNeed::getTown, search);
        }
        wrapper.orderByDesc(HouseNeed::getId);
        Page<HouseNeed> houseNeedPage = this.page(new Page<>(pageNum, pageSize), wrapper);
        List<HouseNeedDto> houseNeedDtoList = houseNeedPage.getRecords().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        Page<HouseNeedDto> houseNeedDtoPage = new Page<>();
        houseNeedDtoPage.setRecords(houseNeedDtoList);
        houseNeedDtoPage.setTotal(houseNeedPage.getTotal());
        houseNeedDtoPage.setCurrent(houseNeedPage.getCurrent());
        houseNeedDtoPage.setSize(houseNeedPage.getSize());
        redisUtil.setCache(cacheKey, houseNeedDtoPage, 10, TimeUnit.MINUTES);

        return houseNeedDtoPage;
    }

    // 将 HouseNeed 转换为 HouseNeedDto
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

    // 添加 HouseNeedDto
    @Transactional
    @Override
    public boolean saveDto(HouseNeedDto dto) {
        HouseNeed houseNeed = new HouseNeed(dto);
        this.getBaseMapper().insert(houseNeed);
        List<String> pictureList = dto.getPictures();
        Picture picture = new Picture();
        for (int i = 0; i < pictureList.size(); i++) {
            picture.setId(null);
            picture.setImage(pictureList.get(i));
            picture.setType(type);
            picture.setBelongId(houseNeed.getId());
            picture.setNo(i);
            pictureMapper.insert(picture);
        }
        redisUtil.updateCacheAfterChange(HOUSE_NEED_CACHE_PREFIX, houseNeed.getId().longValue());

        return true;
    }

    // 更新 HouseNeedDto
    @Transactional
    @Override
    public boolean updateDtoById(HouseNeedDto dto) {
        HouseNeed houseNeed = new HouseNeed(dto);
        this.getBaseMapper().updateById(houseNeed);
        List<Picture> pictureList0 = pictureMapper.selectList(new LambdaQueryWrapper<Picture>()
                .eq(Picture::getBelongId, dto.getId())
                .eq(Picture::getType, type)
                .eq(Picture::getFlag, 0));
        for (Picture pic : pictureList0) {
            pic.setFlag(1);
            pictureMapper.updateById(pic);
        }
        pictureList0.clear();
        List<String> pictureList = dto.getPictures();
        Picture picture = new Picture();
        for (int i = 0; i < pictureList.size(); i++) {
            picture.setId(null);
            picture.setImage(pictureList.get(i));
            picture.setType(type);
            picture.setBelongId(dto.getId());
            picture.setFlag(0);
            picture.setNo(i);
            pictureMapper.insert(picture);
        }
        redisUtil.updateCacheAfterChange(HOUSE_NEED_CACHE_PREFIX, dto.getId().longValue());

        return true;
    }

    // 删除 HouseNeedDto
    @Transactional
    @Override
    public boolean deleteDtoById(Long id) {
        this.getBaseMapper().deleteById(id);
        List<Picture> pictureList0 = pictureMapper.selectList(new LambdaQueryWrapper<Picture>()
                .eq(Picture::getBelongId, id)
                .eq(Picture::getType, type)
                .eq(Picture::getFlag, 0));
        for (Picture pic : pictureList0) {
            pic.setFlag(1);
            pictureMapper.updateById(pic);
        }

        // 删除数据时更新缓存
        redisUtil.updateCacheAfterChange(HOUSE_NEED_CACHE_PREFIX, id);

        return true;
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
