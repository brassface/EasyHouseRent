package com.zzy.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzy.demo.common.Result;
import com.zzy.demo.dto.TalkItemDto;
import com.zzy.demo.entity.TalkItem;
import com.zzy.demo.service.TalkItemService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/talk_item")
public class TalkItemController {
    @Resource
    TalkItemService talkItemService;

    //添加
    @PostMapping
    public Result<?> save(@RequestBody TalkItem entity) {
        if (talkItemService.save(entity)) {
            return Result.success();
        } else {
            return Result.error("-1", "评论上传失败");
        }
    }

    //修改
    @PutMapping
    public Result<?> update(@RequestBody TalkItem entity) {
        if (talkItemService.updateById(entity)) {
            return Result.success();
        } else {
            return Result.error("-1", "评论修改失败");
        }
    }

    //删除
    @DeleteMapping()
    public Result<?> delete(@RequestParam Long id) {
        if (talkItemService.removeById(id)) {
            return Result.success();
        } else {
            return Result.error("-1", "评论删除失败");
        }
    }

    //根据id查询
    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        TalkItem entity = talkItemService.getById(id);
        if (entity != null) {
            return Result.success(entity);
        } else {
            return Result.error("-1", "评论查询失败");
        }
    }

    //分页查询
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "0") Integer belongItem) {
        LambdaQueryWrapper<TalkItem> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TalkItem::getBelongItem, belongItem);
        wrapper.eq(TalkItem::getFlag, 0);
        Page<TalkItemDto> entityPage = talkItemService.pageWithDto(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(entityPage);
    }
}

