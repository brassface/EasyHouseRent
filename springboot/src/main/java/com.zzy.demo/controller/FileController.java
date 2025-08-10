package com.zzy.demo.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.zzy.demo.common.Result;
import com.zzy.demo.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/files")
public class FileController {
    @Value("${server.port}")
    private String port;
    private static final String ip = "http://localhost";
    private static final int MAX_FILE_SIZE = 10 * 1024 * 1024;
    private static final String HOUSE_NEED_CACHE_PREFIX = "file:";
    @Resource
    private RedisUtil redisUtil;

    /**
     * 上传接口
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public Result<?> upload(MultipartFile file) {
        if (file == null) {
            return Result.error("-1", "文件传输失败，请重新上传");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            return Result.error("-1", "文件超出10MB，请重新选择");
        }
        // 获取原文件名
        String originalFilename = file.getOriginalFilename();
        // 获取文件后缀名（包含点）
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 生成唯一文件名 + 后缀
        String flag = IdUtil.fastSimpleUUID() + suffix;
        String rootFilePath = System.getProperty("user.dir") + "/springboot/src/main/resources/files/" + flag;
        try {
            FileUtil.writeBytes(file.getBytes(), rootFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("-1", "文件上传失败");
        }
        String url = ip + ":" + port + "/files/" + flag;
        return Result.success(url);
    }


    /**
     * 下载接口
     *
     * @param flag
     * @param response
     */
    @GetMapping("/{flag}")
    public void getFiles(@PathVariable String flag, HttpServletResponse response) {
        OutputStream os;//新建一个输出对象
        String basePath = System.getProperty("user.dir") + "/springboot/src/main/resources/files/";
        List<String> fileNames = FileUtil.listFileNames((basePath));
        String fileName = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse("");
        String cacheKey = HOUSE_NEED_CACHE_PREFIX + flag;
        byte[] bytes = redisUtil.getCache(cacheKey, byte[].class);
        try {
            if (bytes != null) {
                System.out.println("从缓存读取");
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                response.setContentType("application/octet-stream");
                os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
            } else {
                if (StrUtil.isNotEmpty(fileName)) {
                    response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                    response.setContentType("application/octet-stream");
                    bytes = FileUtil.readBytes(basePath + fileName);
                    redisUtil.setCache(cacheKey, bytes, 10, TimeUnit.MINUTES);
                    System.out.println("从文件读取并写入缓存");
                    os = response.getOutputStream();
                    os.write(bytes);
                    os.flush();
                    os.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 富文本上传接口
     *
     * @param file
     * @return
     */
    @PostMapping("editor/upload")
    public JSON editorUpload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String flag = IdUtil.fastSimpleUUID();
        String rootFilePath = System.getProperty("user.dir") + "/springboot/src/main/resources/files/" + flag + "_" + originalFilename;
        try {
            FileUtil.writeBytes(file.getBytes(), rootFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = ip + ":" + port + "/files/" + flag;
        JSONObject jsonObject = new JSONObject();
        jsonObject.set("errno", 0);
        JSONArray arr = new JSONArray();
        JSONObject data = new JSONObject();
        arr.add(data);
        data.set("url", url);
        jsonObject.set("data", arr);
        return jsonObject;
    }
}