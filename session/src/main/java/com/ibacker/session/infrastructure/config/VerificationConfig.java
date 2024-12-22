package com.ibacker.session.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "verification")
public class VerificationConfig {
    private static final String VERIFICATION_CODE_KEY = "verificationCode";
    private List<String> checkList = new ArrayList<>();

}
