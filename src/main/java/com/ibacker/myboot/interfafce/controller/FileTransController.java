package com.ibacker.myboot.interfafce.controller;

import com.ibacker.myboot.application.FileTransService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("services/file")
public class FileTransController {

    @Resource
    FileTransService fileTransService;
    @GetMapping("/getTemplateFile")
    public ResponseEntity<byte[]> downloadFileTemplate(HttpServletRequest request) throws IOException {
        return fileTransService.downloadTxtFile(request);
    }
}
