package com.springbite.authorization_server.mixin;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.springbite.authorization_server.deserializer.SecurityUserDeserializer;

@JsonDeserialize(using = SecurityUserDeserializer.class)
public abstract class SecurityUserMixin {
}
