package org.webserver.util.logging;

import org.slf4j.LoggerFactory;

public class Logger implements Log {

    private final org.slf4j.Logger basicLogger;

    private <T> Logger(Class<T> clazz) {
        this.basicLogger = LoggerFactory.getLogger(clazz);
    }

    public static <T> Logger getInstance(Class<T> clazz) {
        return new Logger(clazz);
    }

    @Override
    public void info(String message) {
        basicLogger.info(message);
    }

    @Override
    public void warn(String message) {
        basicLogger.warn(message);
    }

    @Override
    public void warn(String message, Throwable throwable) {
        basicLogger.warn(message, throwable);
    }

    @Override
    public void error(String message) {
        basicLogger.error(message);
    }

    @Override
    public void error(String message, Throwable throwable) {
        basicLogger.error(message, throwable);
    }

    @Override
    public void debug(String message) {
        basicLogger.debug(message);
    }

    @Override
    public void debug(String message, Throwable throwable) {
        basicLogger.debug(message, throwable);
    }
}
