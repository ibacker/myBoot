package com.ibacker.myboot.domain.filetrans;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

@Slf4j
@Service
public class CreateFileTemplateService {
    @javax.annotation.Resource
    private ResourceLoader resourceLoader;

    private String createTxtFile() {
        try {
            // 获取资源目录的位置
            Resource resource = resourceLoader.getResource("classpath:");
            // 默认是项目的resources目录

            // 构建文件的相对路径
            String relativePath = "myfiles/myfile.txt";

            // 获取文件的绝对路径
            Path absolutePath = resource.getFile().toPath().resolve(relativePath);

            // 检查目标文件的父目录是否存在，如果不存在，则创建它们
            Path parentDir = absolutePath.getParent();
            if (!parentDir.toFile().exists()) {
                parentDir.toFile().mkdirs();
            }

            // 创建文件并写入内容
            String content = "This is the content of the txt file.";
            FileCopyUtils.copy(content.getBytes(), absolutePath.toFile());

            return "Txt file created successfully!";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error creating the txt file: " + e.getMessage();
        }
    }

    public byte[] createBytes() {
        // 创建文本内容
        String textContent = "This is the content of the text file.";

        // 将文本内容转换为字节数组
        byte[] textBytes = textContent.getBytes(StandardCharsets.UTF_8);

        return textBytes;
    }

    private String getContentFromMyFile() {
        String content = "";
        try {
            // 使用ClassPathResource获取txt文件的资源
            Resource resource = new ClassPathResource("myfiles/myfile.txt"); // 替换为你的txt文件路径

            // 读取文件内容
            InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
            content = FileCopyUtils.copyToString(reader);
            reader.close();

        } catch (Exception e) {
            log.error("createBytesFromTextFile error: {}", e.getMessage(), e);
        }
        return content;
    }

    public byte[] createBytesFromTxtFile() {
        if (StringUtils.isEmpty(getContentFromMyFile())) {
            createTxtFile();
        }
        return getContentFromMyFile().getBytes();
    }

    public File getTxtFile() {
        try {
            Resource resource = new ClassPathResource("myfiles/myfile.txt");
            if (resource.exists()) {
                return resource.getFile();
            }

            createTxtFile();
            return new ClassPathResource("myfiles/myfile.txt").getFile();
        } catch (Exception e) {
            log.error("getTxtFile error: {}", e.getMessage(), e);
        }
        return new File("C/");
    }

}
