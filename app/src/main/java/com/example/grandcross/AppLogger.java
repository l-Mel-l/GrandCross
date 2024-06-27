package com.example.grandcross;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppLogger {
    private static final Logger logger = LoggerFactory.getLogger(AppLogger.class);

    // Метод для вывода информационных сообщений
    public static void info(String message) {
        if (BuildConfig.DEBUG) {
            logger.info(message);
        }
    }

    // Метод для вывода сообщений об ошибках
    public static void error(String message) {
        if (BuildConfig.DEBUG) {
            logger.error(message);
        }
    }

    // Метод для вывода отладочных сообщений
    public static void debug(String message) {
        if (BuildConfig.DEBUG) {
            logger.debug(message);
        }
    }

    // Метод для вывода предупреждений
    public static void warn(String message) {
        if (BuildConfig.DEBUG) {
            logger.warn(message);
        }
    }

    // Метод для проверки утверждений
    public static void assertCondition(boolean condition, String message) {
        if (BuildConfig.DEBUG) {
            if (!condition) {
                error("ASSERT FAILED: " + message);
                throw new IllegalStateException(message);
            }
        }
    }
}