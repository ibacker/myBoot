package com.ibacker.myboot.application;

import com.ibacker.myboot.domain.filetrans.CreateFileTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

import static org.springframework.util.StringUtils.getFilename;

@Slf4j
@Service
public class FileTransService {
    @Resource
    CreateFileTemplateService createFileTemplateService;


    public ResponseEntity<byte[]> downloadTxtFile(HttpServletRequest request) throws IOException {
        File file = createFileTemplateService.getTxtFile();
        //设置响应头
        HttpHeaders headers = new HttpHeaders();
        //通知浏览器以下载的方式打开文件
        headers.setContentDispositionFormData("attachment", getFilename(file.getPath()));
        //定义以流的形式下载返回文件数据
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        //使用springmvc框架的ResponseEntity对象封装返回数据
        return new ResponseEntity<>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
    }
}
