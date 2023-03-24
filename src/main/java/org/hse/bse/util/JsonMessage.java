package org.hse.bse.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jade.lang.acl.ACLMessage;

public class JsonMessage extends ACLMessage {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public JsonMessage(int cfp) {
        super(cfp);
    }

    public <T> T getContent(Class<T> clazz) throws JsonProcessingException {
        return objectMapper.readValue(super.getContent(), clazz);
    }

    public <T> void setContent(T content) throws JsonProcessingException {
        super.setContent(objectMapper.writeValueAsString(content));
    }
}
