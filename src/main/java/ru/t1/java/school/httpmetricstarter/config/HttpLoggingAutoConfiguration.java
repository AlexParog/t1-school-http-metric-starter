package ru.t1.java.school.httpmetricstarter.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.t1.java.school.httpmetricstarter.aop.HttpLoggingAspect;
import ru.t1.java.school.httpmetricstarter.aop.HttpLoggingProperties;

/**
 * Конфигурация для автоматического создания и настройки аспекта логирования HTTP-запросов.
 */
@Configuration
@EnableConfigurationProperties(HttpLoggingProperties.class)
@ConditionalOnProperty(prefix = "http.logging", name = "enabled", havingValue = "true", matchIfMissing = true)
public class HttpLoggingAutoConfiguration {

    /**
     * Создает бин аспекта для логирования HTTP-запросов.
     *
     * @param properties Настройки логирования.
     * @return Аспект для логирования HTTP-запросов.
     */
    @Bean
    @ConditionalOnMissingBean
    public HttpLoggingAspect httpLoggingAspect(HttpLoggingProperties properties) {
        return new HttpLoggingAspect(properties);
    }
}
