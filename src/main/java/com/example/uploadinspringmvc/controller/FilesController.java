package com.example.uploadinspringmvc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

/**
 * @author 钟金锋Jean
 * @date 2021/5/19 1:58
 * @description
 */
@Slf4j
@RestController
public class FilesController {

    /**
     *  文件上传
     * @param file
     * @param request
     * @return
     */
    @PostMapping("/upload")
    public String upload(MultipartFile file, HttpServletRequest request){
        if (file.isEmpty()){
            return "请选择至少一个文件";
        }

        String filename = file.getOriginalFilename();
        String path = null;
        try {
            // 获取项目路径 + 文件夹 + 文件名
            path = ResourceUtils.getURL("classpath:").getPath() + "/upload" + File.separator + filename;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        log.info(path);
        assert path != null;
        File fileTrans = new File(path);

        try {
            file.transferTo(fileTrans);
            log.info("上传成功");
            return "上传成功";
        } catch (IOException e) {
            e.printStackTrace();
            log.info("上传失败");
        }

        return "上传失败";
    }


    /**
     * 文件下载
     * @param filename
     * @return
     */
    @GetMapping("/download/{filename}")
    public ResponseEntity<byte[]> download(@PathVariable String filename){
        String path = null;
        InputStream inputStream = null;
        byte[] bytes = null;

        try {
            // 获取项目路径 + 文件夹 + 文件名
            path = ResourceUtils.getURL("classpath:").getPath() + "/upload" + File.separator + filename;
            inputStream = new FileInputStream(path);
            bytes = new byte[inputStream.available()];
            inputStream.read(bytes);

        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        // 设置headers
        headers.add("Content-Disposition","attachment;filename=" + filename);

        ResponseEntity<byte[]> responseEntity = ResponseEntity.ok()
                .headers(headers)
                .body(bytes);
        return responseEntity;
    }
}
