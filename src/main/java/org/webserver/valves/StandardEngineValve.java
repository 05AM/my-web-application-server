package org.webserver.valves;

import org.webserver.Container;
import org.webserver.Engine;
import org.webserver.connector.Request;
import org.webserver.connector.Response;
import org.webserver.core.StandardEngine;

public class StandardEngineValve extends ValveBase {
    private final Engine engine;

    public StandardEngineValve(StandardEngine engine) {
        this.engine = engine;
    }

    @Override
    public void invoke(Request req, Response res) throws Exception {
        Container container = engine.getContainer();
        if (container == null) {
            res.getWriter().write("404 Host");
            return;
        }
        container.getPipeline().getFirst().invoke(req, res);
    }
}
