package com.github.marcelldechant.bistro.order.config;

import com.github.marcelldechant.bistro.order.service.OrderService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class OrderServiceTestConfig {

    @Bean
    public OrderService orderService() {
        return Mockito.mock(OrderService.class);
    }

}
