package com.zzy.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzy.demo.common.Result;
import com.zzy.demo.dto.HouseRentDto;
import com.zzy.demo.entity.HouseNeed;
import com.zzy.demo.entity.HouseRent;
import com.zzy.demo.service.HouseRentService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/house_rent")
public class HouseRentController {
    @Resource
    HouseRentService houseRentService;

    //添加
    @PostMapping
    public Result<?> save(@RequestBody HouseRentDto entity) {
        System.out.println(entity);
        if (houseRentService.saveDto(entity)) {
            return Result.success();
        } else {
            return Result.error("-1", "房源信息上传失败");
        }
    }

    //修改
    @PutMapping
    public Result<?> update(@RequestBody HouseRentDto entity) {
        if (houseRentService.updateDtoById(entity)) {
            return Result.success();
        } else {
            return Result.error("-1", "房源信息修改失败");
        }
    }

    //删除
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        if (houseRentService.deleteDtoById(id)) {
            return Result.success();
        } else {
            return Result.error("-1", "房源信息删除失败");
        }
    }

    //批量删除
    @Transactional
    @DeleteMapping("/batch")
    public Result<?> batchDelete(@RequestBody List<Long> ids) {
        try {
            for (Long id : ids) {
                houseRentService.deleteDtoById(id);
            }
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("-1", "批量删除失败，请稍后重试");
        }
    }

    //根据id查询
    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        HouseRentDto entity = houseRentService.getDtoById(id);
        if (entity != null) {
            return Result.success(entity);
        } else {
            return Result.error("-1", "房源信息查询失败");
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
        LambdaQueryWrapper<HouseRent> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(HouseRent::getFlag, 0);
        if (author > 0) {
            wrapper.eq(HouseRent::getAuthor, author);
        }
        if (StrUtil.isNotBlank(province)) {
            wrapper.eq(HouseRent::getProvince, province);
            if (StrUtil.isNotBlank(city)) {
                wrapper.eq(HouseRent::getCity, city);
                if (StrUtil.isNotBlank(town)) {
                    wrapper.eq(HouseRent::getTown, town);
                }
            }
        }
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(HouseRent::getTitle, search)
                    .or()
                    .like(HouseRent::getTips, search);
        }
        Page<HouseRentDto> entityPage = houseRentService.pageWithDto(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(entityPage);
    }

    @GetMapping("/statistics")
    public Result getStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("total", houseRentService.countByFlag(0));
        statistics.put("city_count", houseRentService.getCityStatistics(0));
        statistics.put("tips_count", houseRentService.getTipsStatistics(0));
        return Result.success(statistics);
    }
}
