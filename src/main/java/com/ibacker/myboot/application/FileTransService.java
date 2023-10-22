package com.ibacker.myboot.application;

import com.ibacker.myboot.domain.filetrans.CreateFileTemplateService;
import com.ibacker.myboot.domain.filetrans.entity.FileInfo;
import com.ibacker.myboot.infrastructure.bean.ResultObject;
import com.ibacker.myboot.infrastructure.util.JsonUtil;
import com.ibacker.myboot.interfafce.dto.filetrans.FileInfoRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;

import static org.springframework.util.StringUtils.getFilename;

@Slf4j
@Service
public class FileTransService {
    @Resource
    CreateFileTemplateService createFileTemplateService;

    public ResultObject uploadFiles(MultipartFile[] files, FileInfoRequest request) {
        ResultObject resultObject = new ResultObject();
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();  // 文件名
            File dest = new File("/Users/ibacker/codes/IdeaProjects/" + fileName);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            try {
                file.transferTo(dest);
                resultObject.setResult(request);
            } catch (Exception e) {
                resultObject = ResultObject.error(e.getMessage());
            }
        }
        return resultObject;
    }


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

    public ResponseEntity<byte[]> downloadJsFile() throws IOException {

        File file = createFileTemplateService.getJsFile();
        //设置响应头
        HttpHeaders headers = new HttpHeaders();
        //通知浏览器以下载的方式打开文件
        headers.setContentDispositionFormData("attachment", getFilename(file.getPath()));
        //定义以流的形式下载返回文件数据
        headers.setContentType(MediaType.valueOf("text/javascript"));
        //使用springmvc框架的ResponseEntity对象封装返回数据
        return new ResponseEntity<>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
    }

    public ResponseEntity<Object> downloadImgFile(boolean base64) throws IOException {

        File file = createFileTemplateService.getJpeg();
        //设置响应头
        HttpHeaders headers = new HttpHeaders();
        //定义以JSON的形式下载返回文件数据
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 读取图像文件内容
        byte[] imageBytes = FileUtils.readFileToByteArray(file);

        if (!base64) {
            //通知浏览器以下载的方式打开文件
            headers.setContentDispositionFormData("attachment", getFilename(file.getPath()));
            //定义以流的形式下载返回文件数据
            headers.setContentType(MediaType.IMAGE_JPEG);
            //使用springmvc框架的ResponseEntity对象封装返回数据
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        }

        // 将图像字节数组编码为Base64字符串
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        ResultObject resultObject = ResultObject.success();
        resultObject.setResult(base64Image);
        //使用springmvc框架的ResponseEntity对象封装返回数据
        return new ResponseEntity<>(resultObject, headers, HttpStatus.OK);
    }

    public ResponseEntity<byte[]> downloadPdfFile() throws IOException {

        File file = createFileTemplateService.getPdfFile();
        //设置响应头
        HttpHeaders headers = new HttpHeaders();
        //通知浏览器以下载的方式打开文件
//        headers.setContentDispositionFormData("attachment", getFilename(file.getPath()));
        headers.setContentDisposition(ContentDisposition.attachment().filename(getFilename(file.getPath())).build());
        //定义以流的形式下载返回文件数据
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        //使用springmvc框架的ResponseEntity对象封装返回数据
        return ResponseEntity.ok().headers(headers).body(FileUtils.readFileToByteArray(file));
    }

    public byte[] downloadPdfFile2() throws IOException {
        File file = createFileTemplateService.getPdfFile();
        //使用springmvc框架的ResponseEntity对象封装返回数据
        return FileUtils.readFileToByteArray(file);
    }


    public ResponseEntity<byte[]> downloadXlsFile() throws IOException {

        File file = createFileTemplateService.getXlsxFile();
        //设置响应头
        HttpHeaders headers = new HttpHeaders();
        //通知浏览器以下载的方式打开文件
        headers.setContentDisposition(ContentDisposition.attachment().filename(getFilename(file.getPath())).build());
        //定义以流的形式下载返回文件数据
        //对照表 https://tool.oschina.net/commons
        headers.setContentType(MediaType.valueOf("application/x-xls"));
        //使用springmvc框架的ResponseEntity对象封装返回数据
        return ResponseEntity.ok().headers(headers).body(FileUtils.readFileToByteArray(file));
    }

    public ResponseEntity<InputStreamResource> downloadXlsFileInputStreamResource() throws IOException {

        File file = createFileTemplateService.getXlsxFile();
        //设置响应头
        HttpHeaders headers = new HttpHeaders();
        //通知浏览器以下载的方式打开文件
        headers.setContentDisposition(ContentDisposition.attachment().filename(getFilename(file.getPath())).build());
        //定义以流的形式下载返回文件数据
        //对照表 https://tool.oschina.net/commons
        headers.setContentType(MediaType.valueOf("application/x-xls"));
        //使用springmvc框架的ResponseEntity对象封装返回数据
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(Files.newInputStream(file.toPath())));
    }

    public ResponseEntity<ByteArrayResource> downloadXlsFileByteArrayResource() throws IOException {

        File file = createFileTemplateService.getXlsxFile();
        //设置响应头
        HttpHeaders headers = new HttpHeaders();
        //通知浏览器以下载的方式打开文件
        headers.setContentDisposition(ContentDisposition.attachment().filename(getFilename(file.getPath())).build());
        //定义以流的形式下载返回文件数据
        //对照表 https://tool.oschina.net/commons
        headers.setContentType(MediaType.valueOf("application/x-xls"));
        //使用springmvc框架的ResponseEntity对象封装返回数据
        return ResponseEntity.ok().headers(headers).body(new ByteArrayResource(FileUtils.readFileToByteArray(file)));
    }

    public void downloadXlsFileServletResponse(HttpServletResponse response) throws IOException {
        File file = createFileTemplateService.getXlsxFile();

        // Content-Type
        response.setContentType("application/x-xls");

        // Content-Disposition
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName());

        // Content-Length
        response.setContentLength((int) file.length());

        BufferedInputStream inStream = new BufferedInputStream(Files.newInputStream(file.toPath()));
        BufferedOutputStream outStream = new BufferedOutputStream(response.getOutputStream());

        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        outStream.flush();
        inStream.close();

    }

    public void downloadFileMultipart(HttpServletResponse response) throws IOException {
        File file = createFileTemplateService.getXlsxFile();
        HttpEntity httpEntity = MultipartEntityBuilder.create()
                // 表单 => （部件名称，数据，类型），要注意uri编码
                .addPart("name",
                        new StringBody(UriUtils.encode("nameStr", StandardCharsets.UTF_8), ContentType.APPLICATION_FORM_URLENCODED))
                // JSON => （部件名称，JSON，类型）
                .addPart("info",
                        new StringBody(JsonUtil.objectToJson(new FileInfo()), ContentType.APPLICATION_JSON))
                // 文件 => （ 部件名称，文件，类型，文件名称）
                .addPart("file",
                        new FileBody(createFileTemplateService.getPdfFile(), ContentType.APPLICATION_OCTET_STREAM, "file.pdf"))
                // 二进制
                .addBinaryBody("file",
                        (new InputStreamResource(Files.newInputStream(file.toPath())).getInputStream()),
                        ContentType.APPLICATION_OCTET_STREAM, "file.xlsx")
                .build();

        // 设置ContentType
        response.setContentType(httpEntity.getContentType().getValue());

        // 响应客户端
        httpEntity.writeTo(response.getOutputStream());
    }
}
