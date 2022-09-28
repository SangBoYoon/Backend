package com.sangboyoon.accounter.web.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public final class ApiResponse<T> {
    private final T data;
}
