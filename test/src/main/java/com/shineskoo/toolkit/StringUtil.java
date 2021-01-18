package com.shineskoo.toolkit;

/*
 *   @Author: Cosmos
 *   @Date: 2021/1/15 4:04 下午
 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * String的工具类
 */
public class StringUtil {
    /**
     * 将含有json的String信息转换为map
     * @param target
     * @return
     */
    public static Map<String, String> stringToMap(String target) {
        Map<String, String> info = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            info = mapper.readValue(target, new TypeReference<HashMap<String, String>>(){});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return info;
    }
}