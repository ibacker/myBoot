package com.ibacker.myboot.infrastructure.zookeeper;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class CacheMessage {
    private String name;
    private String value;
    private String uid;

    public CacheMessage(String name, String value) {
        this.name = name;
        this.value = value;
        this.uid = UUID.randomUUID().toString();
    }
}
