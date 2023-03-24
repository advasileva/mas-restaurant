package org.hse.bse.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jade.lang.acl.ACLMessage;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ACLMessageUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T getContent(ACLMessage message, Class<T> clazz) throws JsonProcessingException {
        return objectMapper.readValue(message.getContent(), clazz);

    }
}