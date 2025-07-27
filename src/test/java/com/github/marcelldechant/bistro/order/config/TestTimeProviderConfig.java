package com.github.marcelldechant.bistro.order.config;

import com.github.marcelldechant.bistro.order.util.TimeProvider;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestTimeProviderConfig {
    @Bean
    @Primary
    public TimeProvider timeProvider() {
        return Mockito.mock(TimeProvider.class);
    }
}
