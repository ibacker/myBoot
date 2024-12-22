package com.ibacker.session.adapter;

import com.ibacker.session.application.SessionApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/session")
public class SessionController {

    @Resource
    SessionApplicationService sessionApplicationService;


    // 保存数据到 Session
    @GetMapping("/save/{name}")
    public String saveSession(@PathVariable("name") String data, HttpSession session) {
        session.setAttribute("key", data); // 保存数据到 Session

        log.info("请求的 session 是： {}", sessionApplicationService.getSession());

        return "Session saved. sessionId: " + session.getId();
    }

    // 从 Session 中读取数据
    @GetMapping("/get")
    public String getSession(HttpSession session) {
        Object data = session.getAttribute("key");
        return data != null ? "Session data: " + data : "No session data found.";
    }
}
