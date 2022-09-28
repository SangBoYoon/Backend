package com.sangboyoon.accounter.web.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class ErrorResponse {
    private final String reason;
    private final String message;
}
