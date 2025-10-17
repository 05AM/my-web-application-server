package org.webserver.connector;

import org.webserver.*;
import org.webserver.util.http.HttpProtocolHandler;

public class Connector implements LifeCycle {
    private final int port;
    private final Service service;
    private HttpProtocolHandler protocolHandler;
    private HttpAdapter adapter;

    public Connector(int port, Service service) {
        this.port = port;
        this.service = service;
    }

    @Override
    public void init() throws Exception {
        this.adapter = new HttpAdapter(this);
        this.protocolHandler = new HttpProtocolHandler(port, adapter);
        protocolHandler.getEndpoint().setExecutor(service.getExecutor());
        protocolHandler.init();
    }

    @Override public void start() throws Exception { protocolHandler.start(); }
    @Override public void stop() throws Exception  { protocolHandler.stop(); }
    @Override public void destroy() throws Exception {}

    public Service getService() { return service; }
}
