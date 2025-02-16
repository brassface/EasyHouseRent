package com.zzy.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zzy.demo.entity.*;
import com.zzy.demo.mapper.*;
import com.zzy.demo.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    HouseRentMapper houseRentMapper;
    @Resource
    HouseNeedMapper houseNeedMapper;
    @Resource
    HouseTalkMapper houseTalkMapper;
    @Resource
    TalkItemMapper talkItemMapper;
    @Resource
    PictureMapper pictureMapper;

    @Override
    public int saveUser(User entity) {
        if(this.getBaseMapper().selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, entity.getUsername()))==null){
            try {
                this.getBaseMapper().insert(entity);
                return 0;
            }catch (Exception e){
                return -1;
            }
        }else {
            return 1;
        }
    }

    @Override
    @Transactional
    public boolean deleteUserById(Long id) {
        User user = this.getBaseMapper().selectById(id);
        if (user == null) {
            return false; // 用户不存在
        }

        Long userId = Long.valueOf(user.getId());

        // 批量逻辑删除房源信息
        houseRentMapper.update(null,
                new LambdaUpdateWrapper<HouseRent>()
                        .set(HouseRent::getFlag, 1)
                        .eq(HouseRent::getAuthor, userId)
        );

        // 批量逻辑删除求租信息
        houseNeedMapper.update(null,
                new LambdaUpdateWrapper<HouseNeed>()
                        .set(HouseNeed::getFlag, 1)
                        .eq(HouseNeed::getAuthor, userId)
        );

        // 批量获取帖子 ID
        List<Integer> postIds = houseTalkMapper.selectList(
                new LambdaQueryWrapper<HouseTalk>()
                        .eq(HouseTalk::getAuthor, userId)
                        .select(HouseTalk::getId)
        ).stream().map(HouseTalk::getId).collect(Collectors.toList());

        if (!postIds.isEmpty()) {
            // 批量逻辑删除帖子
            houseTalkMapper.update(null,
                    new LambdaUpdateWrapper<HouseTalk>()
                            .set(HouseTalk::getFlag, 1)
                            .in(HouseTalk::getId, postIds)
            );

            // 批量逻辑删除评论区内容
            talkItemMapper.update(null,
                    new LambdaUpdateWrapper<TalkItem>()
                            .set(TalkItem::getFlag, 1)
                            .in(TalkItem::getBelongItem, postIds)
            );
        }

        // 批量逻辑删除图片信息
        pictureMapper.update(null,
                new LambdaUpdateWrapper<Picture>()
                        .set(Picture::getFlag, 1)
                        .eq(Picture::getBelongId, userId)
        );

        // 批量逻辑删除用户评论
        talkItemMapper.update(null,
                new LambdaUpdateWrapper<TalkItem>()
                        .set(TalkItem::getFlag, 1)
                        .eq(TalkItem::getAuthor, userId)
        );
        return true;
    }

    @Override
    public int EditPw(Long id, String oldPw, String newPw) {
        User user = this.getBaseMapper().selectById(id);
        if (user.getPassword().equals(oldPw)){
            User temp=new User();
            temp.setId(user.getId());
            temp.setPassword(newPw);
            try {
                this.getBaseMapper().updateById(temp);
                return 0;
            }catch (Exception e){
                e.printStackTrace();
                return -1;
            }
        }else {
            return 1;
        }
    }
}
