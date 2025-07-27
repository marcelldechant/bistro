package com.github.marcelldechant.bistro.order.util;

import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class SystemTimeProvider implements TimeProvider {
    @Override
    public LocalTime now() {
        return LocalTime.now();
    }
}
