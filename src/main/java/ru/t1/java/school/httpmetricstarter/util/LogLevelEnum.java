package ru.t1.java.school.httpmetricstarter.util;

import lombok.Getter;

import java.util.Arrays;

/**
 * Перечисление для определения уровня логирования.
 */
@Getter
public enum LogLevelEnum {
    INFO("INFO"),
    DEBUG("DEBUG"),
    WARN("WARN"),
    ERROR("ERROR");

    private final String value;

    /**
     * Конструктор перечисления.
     *
     * @param value Значение уровня логирования.
     */
    LogLevelEnum(String value) {
        this.value = value;
    }

    /**
     * Возвращает значение перечисления по строковому представлению.
     *
     * @param value Строковое значение уровня логирования.
     * @return Соответствующий уровень логирования.
     */
    public static LogLevelEnum fromString(String value) {
        return Arrays.stream(values())
                .filter(level -> level.value.equalsIgnoreCase(value))
                .findFirst()
                .orElse(INFO);
    }

    /**
     * Возвращает строковое представление уровня логирования.
     *
     * @return Строковое значение уровня логирования.
     */
    @Override
    public String toString() {
        return value;
    }
}
