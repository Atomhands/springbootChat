package com.niehao.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.niehao.model.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ClassName: JsonUtil
 * Package: com.niehao.util
 * Description:
 *
 * @Author NieHao
 * @Create 2023/12/7 22:11
 * @Version 1.0
 */
public class JsonUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    //Json字符串 转 java对象
    public static <T> T parseJsonToObj(String json, Class<T> c) {
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            return JSON.toJavaObject(jsonObject, c);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    //java对象 转 Json字符串
    public static String parseObjToJson(Object object) {
        String string = null;
        try {
            string = JSONObject.toJSONString(object);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return string;
    }
}
