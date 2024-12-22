package com.ibacker.myboot.interfafce.controller;

import com.ibacker.myboot.application.FileTransService;
import com.ibacker.myboot.infrastructure.bean.ResultObject;
import com.ibacker.myboot.interfafce.dto.filetrans.FileInfoRequest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("services/file")
public class FileTransController {

    @Resource
    FileTransService fileTransService;

    @GetMapping("/getTxtTemplateFile")
    public ResponseEntity<byte[]> downloadFileTemplate(HttpServletRequest request) throws IOException {
        return fileTransService.downloadTxtFile(request);
    }

    /**
     * 指定返回的contentType 为 js
     */
    @GetMapping("/getJsFile")
    public ResponseEntity<byte[]> downloadJsFile() throws IOException {
        return fileTransService.downloadJsFile();
    }

    /**
     * 将图片转换为base64字符串返回
     * 将图片通过二进制返回
     */
    @GetMapping("/getImgFile")
    public ResponseEntity<Object> downloadImgFile(@RequestParam(name = "base64", required = false) String base64) throws IOException {
        return fileTransService.downloadImgFile("Y".equalsIgnoreCase(base64));
    }

    /**
     * 指定返回的contentType 为 pdf
     */
    @GetMapping("/getPdfFile")
    public ResponseEntity<byte[]> downloadPdfFile() throws IOException {
        return fileTransService.downloadPdfFile();
    }

    /**
     * 通过produces指定返回类型
     * 但不能传递header
     */
    @GetMapping(value = "/getPdfFile2", produces = MediaType.APPLICATION_PDF_VALUE, headers = {"myTest"})
    public byte[] downloadPdfFile2() throws IOException {
        return fileTransService.downloadPdfFile2();
    }

    /**
     * 指定返回的contentType 为 Xls
     * ResponseEntity<byte[]>
     */
    @GetMapping("/getXlsFile")
    public ResponseEntity<byte[]> downloadXlsFile() throws IOException {
        return fileTransService.downloadXlsFile();
    }


    /**
     * 指定返回的contentType 为 Xls
     * getXlsFileInputStreamResource
     */
    @GetMapping("/getXlsFileInputStreamResource")
    public ResponseEntity<InputStreamResource> downloadXlsFileInputStreamResource() throws IOException {
        return fileTransService.downloadXlsFileInputStreamResource();
    }

    /**
     * 指定返回的contentType 为 Xls
     * ByteArrayResource
     */
    @GetMapping("/getXlsFileByteArrayResource")
    public ResponseEntity<ByteArrayResource> downloadXlsFileByteArrayResource() throws IOException {
        return fileTransService.downloadXlsFileByteArrayResource();
    }

    /**
     * 指定返回的contentType 为 Xls
     * HttpServletResponse
     */
    @GetMapping("/getXlsFileServletResponse")
    public void downloadXlsFileServletResponse(HttpServletResponse response) throws IOException {
        fileTransService.downloadXlsFileServletResponse(response);
    }

    /**
     * multipart
     * ByteArrayResource
     */
    @GetMapping("/getFileMultipart")
    public void downloadFileMultipart(HttpServletResponse response) throws IOException {
        fileTransService.downloadFileMultipart(response);
    }


    /**
     * 上传文件
     * 需要使用 {@link RequestPart} 传递json格式入参
     */
    @RequestMapping("/upload")
    public ResultObject httpUpload(@RequestParam("files") MultipartFile[] files,
                                   @RequestPart(value = "info", required = false) FileInfoRequest request) {
        return fileTransService.uploadFiles(files, request);
    }



}
