package com.ibacker.session.application;

import com.ibacker.session.adapter.dto.SessionScopedBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Service
public class SessionApplicationService {

    @Resource
    SessionScopedBean sessionScopedBean;

    @Resource
    HttpSession session;

    public String getSession() {
        if (sessionScopedBean.getSessionId() == null){
            sessionScopedBean.setSessionId(session.getId());
        }
        return sessionScopedBean.getSessionId();
    }

}
