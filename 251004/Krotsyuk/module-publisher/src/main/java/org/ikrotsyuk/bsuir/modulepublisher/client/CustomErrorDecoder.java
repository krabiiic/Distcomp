package org.ikrotsyuk.bsuir.modulepublisher.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.ikrotsyuk.bsuir.modulepublisher.exception.exceptions.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class CustomErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder.Default defaultError = new ErrorDecoder.Default();

    @Override
    public Exception decode(String s, Response response) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String exceptionText;
        if (response.status() >= HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            return defaultError.decode(s, response);
        }
        try {
            exceptionText = objectMapper.readValue(response.body().asInputStream(), String.class);
        } catch (Exception e) {
            throw new FeignException("feign deserialization exception");
        }
        throw new FeignException(exceptionText);
    }
}