package org.webserver.util.http;

import org.webserver.Adapter;
import org.webserver.LifeCycle;
import org.webserver.util.net.Nio2Endpoint;

public class HttpProtocolHandler implements LifeCycle {
    private final Nio2Endpoint endpoint;

    public HttpProtocolHandler(int port, Adapter adapter) {
        this.endpoint = new Nio2Endpoint(port, adapter);
    }

    public Nio2Endpoint getEndpoint() { return endpoint; }

    @Override public void init() throws Exception { endpoint.init(); }
    @Override public void start() throws Exception { endpoint.start(); }
    @Override public void stop() throws Exception { endpoint.stop(); }
    @Override public void destroy() throws Exception {}
}
