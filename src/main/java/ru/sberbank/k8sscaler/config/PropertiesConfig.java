package ru.sberbank.k8sscaler.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import ru.sberbank.k8sscaler.property.ScaleProperties;

@Configuration
public class PropertiesConfig {

    @Bean
    @Validated
    @ConfigurationProperties(prefix = "scale", ignoreUnknownFields = false)
    public ScaleProperties scaleProperties() {
        return new ScaleProperties();
    }
}
