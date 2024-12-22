package com.ibacker.myboot.infrastructure.util;

import org.apache.commons.lang.StringUtils;

public class StringTool {

    public static String[] getSubstringBetweenFF(String str, String begin, String end) {
        if (null == str || 0 == str.length()) {
            return null;
        }
        return StringUtils.substringsBetween(str, begin, end);
    }
}
