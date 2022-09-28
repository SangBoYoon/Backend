package com.sangboyoon.accounter.configuration.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Documented
@AuthenticationPrincipal(expression = "#this == 'anonymousUser ? null : user")
public @interface CurrentUser {
}
