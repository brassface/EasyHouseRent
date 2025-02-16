package com.zzy.demo.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzy.demo.entity.HouseNeed;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class RedisUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private ValueOperations<String, Object> operations;

    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.operations = redisTemplate.opsForValue();
    }

    // 获取缓存（将 Object 转换成具体类型）
    public <T> T getCache(String key, Class<T> valueType) {
        Object cachedValue = operations.get(key);
        if (cachedValue == null) {
            return null;
        }

        try {
            return objectMapper.readValue((String) cachedValue, valueType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 设置缓存（将对象序列化成 JSON 字符串）
    public void setCache(String key, Object value, long timeout, TimeUnit unit) {
        try {
            String valueAsString = objectMapper.writeValueAsString(value);
            operations.set(key, valueAsString, timeout, unit);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    // 删除缓存
    public void deleteCache(String key) {
        redisTemplate.delete(key);
    }

    // 判断缓存是否存在
    public boolean hasCache(String key) {
        return redisTemplate.hasKey(key);
    }
}


