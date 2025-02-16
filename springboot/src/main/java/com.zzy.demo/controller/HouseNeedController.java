package com.zzy.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzy.demo.common.Result;
import com.zzy.demo.dto.HouseNeedDto;
import com.zzy.demo.entity.HouseNeed;
import com.zzy.demo.entity.HouseNeed;
import com.zzy.demo.entity.HouseRent;
import com.zzy.demo.service.HouseNeedService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/house_need")
public class HouseNeedController {
    @Resource
    HouseNeedService houseNeedService;

    //添加
    @PostMapping
    public Result<?> save(@RequestBody HouseNeedDto entity) {
        if (houseNeedService.saveDto(entity)) {
            return Result.success();
        } else {
            return Result.error("-1", "租房需求上传失败");
        }
    }

    //修改
    @PutMapping
    public Result<?> update(@RequestBody HouseNeedDto entity) {
        if (houseNeedService.updateDtoById(entity)) {
            return Result.success();
        } else {
            return Result.error("-1", "租房需求修改失败");
        }
    }

    //删除
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        if (houseNeedService.deleteDtoById(id)) {
            return Result.success();
        } else {
            return Result.error("-1", "租房需求删除失败");
        }
    }

    //批量删除
    @Transactional
    @DeleteMapping("/batch")
    public Result<?> batchDelete(@RequestBody List<Long> ids){
        try {
            for(Long id:ids){
                houseNeedService.deleteDtoById(id);
            }
            return Result.success();
        }catch (Exception e){
            e.printStackTrace();
            return Result.error("-1", "批量删除失败，请稍后重试");
        }
    }

    //根据id查询
    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        HouseNeedDto entity = houseNeedService.getDtoById(id);
        if (entity != null) {
            return Result.success(entity);
        } else {
            return Result.error("-1", "租房需求查询失败");
        }
    }

    //分页查询
    @GetMapping
    public Result<?> findPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") Integer author,
            @RequestParam(defaultValue = "") String province,
            @RequestParam(defaultValue = "") String city,
            @RequestParam(defaultValue = "") String town) {
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
        Page<HouseNeedDto> entityPage = houseNeedService.pageWithDto(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(entityPage);
    }

    @GetMapping("/statistics")
    public Result getStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("total", houseNeedService.countByFlag(0));
        statistics.put("city_count", houseNeedService.getCityStatistics(0));
        statistics.put("tips_count", houseNeedService.getTipsStatistics(0));
        return Result.success(statistics);
    }
}
