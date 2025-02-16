package com.zzy.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzy.demo.entity.Picture;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PictureMapper extends BaseMapper<Picture> {
}
