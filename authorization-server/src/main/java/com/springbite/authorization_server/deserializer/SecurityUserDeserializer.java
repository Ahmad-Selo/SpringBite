package com.springbite.authorization_server.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.springbite.authorization_server.entity.User;
import com.springbite.authorization_server.model.SecurityUser;

import java.io.IOException;

public class SecurityUserDeserializer extends StdDeserializer<SecurityUser> {

    protected SecurityUserDeserializer() {
        super(SecurityUser.class);
    }

    @Override
    public SecurityUser deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = p.getCodec().readTree(p);
        JsonNode userNode = jsonNode.get("user");

        User user = objectMapper.treeToValue(userNode, User.class);

        return new SecurityUser(user);
    }
}
