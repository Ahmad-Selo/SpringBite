package com.springbite.authorization_server.model.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class JsonResponse {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String json(Object body) throws JsonProcessingException {
        return objectMapper.writeValueAsString(body);
    }
}
