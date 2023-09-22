package com.yagieottae_back_end.Util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.yagieottae_back_end.Exception.CustomBadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Slf4j
public class JsonUtil
{
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ObjectNode ObjectToJsonObject(String key, Object value)
    {
        try
        {
            ObjectNode returnValue = JsonNodeFactory.instance.objectNode();
            returnValue.putPOJO(key, value);

            return returnValue;
        } catch (Exception e)
        {
            throw new CustomBadRequestException("Object를 Json으로 변환하는 과정에서 오류가 발생하였습니다.");
        }
    }

    public static ObjectNode ObjectToJsonObject(Map<String, Object> map)
    {
        try
        {
            ObjectNode returnValue = JsonNodeFactory.instance.objectNode();
            returnValue = objectMapper.valueToTree(map);

            return returnValue;
        } catch (Exception e)
        {
            log.error(e.getMessage());
            throw new CustomBadRequestException("Map을 Json으로 변환하는 과정에서 오류가 발생하였습니다.");
        }
    }
}
