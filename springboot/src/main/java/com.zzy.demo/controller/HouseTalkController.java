package com.zzy.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzy.demo.common.Result;
import com.zzy.demo.dto.HouseTalkDto;
import com.zzy.demo.entity.HouseRent;
import com.zzy.demo.entity.HouseTalk;
import com.zzy.demo.entity.HouseTalk;
import com.zzy.demo.service.HouseTalkService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/house_talk")
public class HouseTalkController {
    @Resource
    HouseTalkService houseTalkService;

    //添加
    @PostMapping
    public Result<?> save(@RequestBody HouseTalkDto entity) {
        if(houseTalkService.saveDto(entity)) {
            return Result.success();
        } else {
            return Result.error("-1", "帖子上传失败");
        }
    }

    //修改
    @PutMapping
    public Result<?> update(@RequestBody HouseTalkDto entity) {
        if(houseTalkService.updateDtoById(entity)) {
            return Result.success();
        } else {
            return Result.error("-1", "帖子修改失败");
        }
    }

    //删除
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        if(houseTalkService.deleteDtoById(id)) {
            return Result.success();
        } else {
            return Result.error("-1", "帖子删除失败");
        }
    }

    //批量删除
    @Transactional
    @DeleteMapping("/batch")
    public Result<?> batchDelete(@RequestBody List<Long> ids){
        try {
            for(Long id:ids){
                houseTalkService.deleteDtoById(id);
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
        HouseTalkDto entity = houseTalkService.getDtoById(id);
        if (entity != null) {
            return Result.success(entity);
        } else {
            return Result.error("-1", "帖子查询失败");
        }
    }

    //分页查询
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "") String search, @RequestParam(defaultValue = "") String author) {
        LambdaQueryWrapper<HouseTalk> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(HouseTalk::getFlag, 0);
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(HouseTalk::getTitle, search)
                    .or()
                    .like(HouseTalk::getTips, search);
        } else if (StrUtil.isNotBlank(author)) {
            try {
                int authorId = Integer.parseInt(author);
                if (authorId > 0) {
                    wrapper.eq(HouseTalk::getAuthor, authorId);
                } else {
                    return Result.error("-1", "查询失败");
                }
            } catch (NumberFormatException e) {
                return Result.error("-1", "查询失败");
            }
        }
        Page<HouseTalkDto> entityPage = houseTalkService.pageWithDto(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(entityPage);
    }
}
