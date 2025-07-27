package com.github.marcelldechant.bistro.order.util;

import java.time.LocalTime;

public interface TimeProvider {
    LocalTime now();
}
