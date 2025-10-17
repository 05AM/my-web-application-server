package org.webserver.valves;

import org.webserver.*;
import org.webserver.connector.Request;
import org.webserver.connector.Response;
import org.webserver.core.StandardHost;

public class StandardHostValve extends ValveBase {
    private final StandardHost host;

    public StandardHostValve(StandardHost host) {
        this.host = host;
    }

    @Override
    public void invoke(Request req, Response res) throws Exception {
        Container container = host.getContainer();

        if (container == null) { res.getWriter().write("404 Context"); return; }
        container.getPipeline().getFirst().invoke(req, res);
    }
}
