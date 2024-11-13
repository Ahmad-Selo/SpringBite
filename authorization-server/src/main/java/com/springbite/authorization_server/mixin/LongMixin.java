package com.springbite.authorization_server.mixin;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;

@JsonDeserialize(using = NumberDeserializers.LongDeserializer.class)
public abstract class LongMixin {
}
