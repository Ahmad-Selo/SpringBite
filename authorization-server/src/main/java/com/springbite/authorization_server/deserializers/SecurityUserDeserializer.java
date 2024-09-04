package com.springbite.authorization_server.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springbite.authorization_server.models.SecurityUser;
import com.springbite.authorization_server.models.User;

import java.io.IOException;

public class SecurityUserDeserializer extends JsonDeserializer<SecurityUser> {

    @Override
    public SecurityUser deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = p.getCodec().readTree(p);
        JsonNode userNode = jsonNode.get("user");

        User user = objectMapper.treeToValue(userNode, User.class);

        return new SecurityUser(user);
    }
}
