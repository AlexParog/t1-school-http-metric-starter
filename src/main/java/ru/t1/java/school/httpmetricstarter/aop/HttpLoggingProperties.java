package ru.t1.java.school.httpmetricstarter.aop;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import ru.t1.java.school.httpmetricstarter.util.LogLevelEnum;

/**
 * Класс для настройки параметров логирования HTTP-запросов и ответов.
 * Настройки могут быть заданы через application.properties или application.yml.
 */
@Validated
@Getter
@Setter
@ConfigurationProperties(prefix = "http.logging")
public class HttpLoggingProperties {

    /**
     * Флаг, включающий или отключающий логирование.
     */
    private boolean enabled = true;

    /**
     * Уровень логирования: INFO, DEBUG, WARN, ERROR.
     */
    @NotNull(message = "Level must not be null")
    private LogLevelEnum level = LogLevelEnum.INFO;

}
