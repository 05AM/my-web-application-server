package org.webserver.util.logging;

public interface Log {

    void info(String message);

    void warn(String message);
    void warn(String message, Throwable throwable);

    void error(String message);
    void error(String message, Throwable throwable);

    void debug(String message);
    void debug(String message, Throwable throwable);
}
