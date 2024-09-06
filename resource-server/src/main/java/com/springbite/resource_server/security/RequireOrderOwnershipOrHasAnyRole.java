package com.springbite.resource_server.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@authorizationService.hasOrderOwnershipOrHasAnyRole(authentication, #orderId, {roles})")
public @interface RequireOrderOwnershipOrHasAnyRole {
    String[] roles();
}
