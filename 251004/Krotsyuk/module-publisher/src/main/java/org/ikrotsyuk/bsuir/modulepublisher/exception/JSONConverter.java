package org.ikrotsyuk.bsuir.modulepublisher.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JSONConverter {
    private final ObjectMapper objectMapper;

    @Autowired
    public JSONConverter(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    public String convertObjectToJSON(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }
}
