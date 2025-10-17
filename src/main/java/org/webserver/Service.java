package org.webserver;

import java.util.concurrent.Executor;

import org.webserver.connector.Connector;
import org.webserver.mapper.Mapper;

/**
 * 컨테이너를 묶어주는 구성 단위
 */
public interface Service {
    void addConnector(Connector connector);

    Executor getExecutor();

    void setEngine(Engine engine);

    Engine getEngine();

    Mapper getMapper();
}
