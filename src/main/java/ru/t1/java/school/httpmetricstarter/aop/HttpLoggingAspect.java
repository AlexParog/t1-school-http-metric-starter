package ru.t1.java.school.httpmetricstarter.aop;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.t1.java.school.httpmetricstarter.util.LogLevelEnum;

import java.util.Arrays;

/**
 * Аспект для логирования HTTP-запросов и ответов в приложении.
 * Логирует информацию о запросах, ответах и ошибках, используя настройки из {@link HttpLoggingProperties}.
 */
@Component
@Aspect
public class HttpLoggingAspect {
    private static final String REQUEST = "REQUEST";
    private static final String RESPONSE = "RESPONSE";
    private static final String ERROR = "ERROR";
    private static final String PREFIX_T1 = "T1 Java School:";
    private final Logger logger = LoggerFactory.getLogger(HttpLoggingAspect.class);
    private final HttpLoggingProperties properties;

    /**
     * Конструктор аспекта.
     *
     * @param properties Настройки логирования.
     */
    public HttpLoggingAspect(HttpLoggingProperties properties) {
        this.properties = properties;
    }

    /**
     * Pointcut для методов контроллеров.
     * Определяет методы, которые должны быть перехвачены аспектом.
     */
    @Pointcut("within(@org.springframework.stereotype.Controller *) || within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerMethods() {
    }

    /**
     * Логирует HTTP-запросы и ответы.
     *
     * @param joinPoint Точка соединения, представляющая метод контроллера.
     * @return Результат выполнения метода контроллера.
     * @throws Throwable Если возникает ошибка при выполнении метода.
     */
    @Around("controllerMethods()")
    public Object logHttpRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("Aspect is working!");
        if (!properties.isEnabled()) {
            return joinPoint.proceed();
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringType().getSimpleName() + "." + signature.getName();
        Object[] args = joinPoint.getArgs();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getRequest();
        String httpMethod = request.getMethod();
        String requestURI = request.getRequestURI();

        // Логирование запроса
        log(REQUEST, methodName, httpMethod, requestURI, args, null);

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable t) {
            // Логирование ошибки, если выброшено исключение
            log(ERROR, methodName, httpMethod, requestURI, null, t);
            throw t;
        }

        // Логирование ответа
        log(RESPONSE, methodName, httpMethod, requestURI, null, null);
        return result;
    }

    /**
     * Логирует сообщение в зависимости от типа (запрос, ответ, ошибка) и уровня логирования.
     *
     * @param type       Тип сообщения (REQUEST, RESPONSE, ERROR).
     * @param methodName Имя метода, в котором произошло событие.
     * @param httpMethod HTTP-метод (GET, POST и т.д.).
     * @param requestURI URI запроса.
     * @param details    Детали запроса (аргументы метода).
     * @param error      Исключение, если оно возникло.
     */
    private void log(String type, String methodName, String httpMethod, String requestURI, Object[] details,
                     Throwable error) {
        LogLevelEnum level = properties.getLevel();
        String message;

        if (REQUEST.equals(type)) {
            message = String.format(PREFIX_T1 + " HTTP %s Request to %s %s with arguments: %s",
                    httpMethod, requestURI, methodName, Arrays.toString(details));
        } else if (RESPONSE.equals(type)) {
            message = String.format(PREFIX_T1 + " HTTP %s Response from %s %s", httpMethod, requestURI, methodName);
        } else if (ERROR.equals(type)) {
            message = String.format(PREFIX_T1 + " Exception in %s %s: %s", httpMethod, methodName, error.getMessage());
        } else {
            message = PREFIX_T1 + " " + type + " " + methodName;
        }

        if (level != null) {
            switch (level) {
                case DEBUG -> logger.debug(message, error);
                case WARN -> logger.warn(message, error);
                case ERROR -> logger.error(message, error);
                default -> logger.info(message, error);
            }
        } else {
            logger.info(message, error);
        }
    }
}
