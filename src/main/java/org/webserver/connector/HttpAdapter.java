package org.webserver.connector;

import org.webserver.*;

public class HttpAdapter implements Adapter {
    private final Connector connector;

    public HttpAdapter(Connector connector) {
        this.connector = connector;
    }

    @Override
    public void service(Request req, Response res) throws Exception {
        Engine engine = connector.getService().getEngine();
        engine.getPipeline().getFirst().invoke(req, res);
    }
}
