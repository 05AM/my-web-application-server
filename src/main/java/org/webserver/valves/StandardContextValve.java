package org.webserver.valves;

import org.webserver.*;
import org.webserver.connector.Request;
import org.webserver.connector.Response;
import org.webserver.core.StandardContext;

public class StandardContextValve extends ValveBase {

    private final Context context;
    public StandardContextValve(StandardContext context) { this.context = context; }

    @Override
    public void invoke(Request req, Response res) throws Exception {
        Container container = context.getContainer();

        if (container == null) { res.getWriter().write("404 Wrapper"); return; }
        container.getPipeline().getFirst().invoke(req, res);
    }
}
