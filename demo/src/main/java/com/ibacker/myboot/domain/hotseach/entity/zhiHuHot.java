package com.ibacker.myboot.domain.hotseach.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class zhiHuHot {
    private String title;

    private String url;

    private String desc;

    private String hotTag;

    private String hotScore;
}
