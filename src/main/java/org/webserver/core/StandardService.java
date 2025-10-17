package org.webserver.core;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.webserver.Engine;
import org.webserver.LifeCycle;
import org.webserver.Service;
import org.webserver.connector.Connector;
import org.webserver.mapper.Mapper;

public class StandardService implements Service, LifeCycle {

    private Connector connector;
    private Engine engine;
    private ExecutorService sharedExecutor;
    private Mapper mapper;

    @Override public void init()    { sharedExecutor = Executors.newVirtualThreadPerTaskExecutor(); }
    @Override public void start() throws Exception { connector.start(); }
    @Override public void stop()    { if (sharedExecutor != null) sharedExecutor.shutdown(); }
    @Override public void destroy() {}

    @Override
    public void addConnector(Connector connector) {
        this.connector = connector;
    }

    @Override
    public Executor getExecutor() {
        return sharedExecutor;
    }

    @Override
    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    public Engine getEngine() {
        return engine;
    }

    @Override
    public Mapper getMapper() {
        return null;
    }
}
