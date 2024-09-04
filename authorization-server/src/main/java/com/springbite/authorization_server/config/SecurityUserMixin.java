package com.springbite.authorization_server.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.springbite.authorization_server.deserializers.SecurityUserDeserializer;

@JsonDeserialize(using = SecurityUserDeserializer.class)
public abstract class SecurityUserMixin {
}
