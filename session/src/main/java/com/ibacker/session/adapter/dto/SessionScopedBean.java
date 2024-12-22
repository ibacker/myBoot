package com.ibacker.session.adapter.dto;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;


@Data
@Component
@SessionScope
@JsonDeserialize(as = SessionScopedBean.class)
public class SessionScopedBean implements Serializable {
    private static final long serialVersionUID = 1L; // 添加序列化 ID
    private String sessionId;


}
