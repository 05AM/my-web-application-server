package org.webserver;

/**
 * 서비스의 모음
 */
public interface Server extends LifeCycle {

    void addService(Service service);

    void await();

    Service[] findServices();
}
