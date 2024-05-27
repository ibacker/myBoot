package com.ibacker.myboot.infrastructure.remote.post;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class EmptyStringDeserializer<T> extends StdDeserializer<T> {

    public EmptyStringDeserializer() {
        this(null);
    }

    public EmptyStringDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getValueAsString();
        if (value != null && !value.isEmpty()) {
            // 非空字符串情况下进行正常反序列化
            return (T) value;
        } else {
            // 空字符串情况下返回null或其他默认值
            return null;
        }
    }
}
