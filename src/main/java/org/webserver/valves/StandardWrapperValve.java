package org.webserver.valves;

import org.webserver.connector.Request;
import org.webserver.connector.Response;
import org.webserver.core.StandardWrapper;

public class StandardWrapperValve extends ValveBase {

    private final StandardWrapper wrapper;
    public StandardWrapperValve(StandardWrapper wrapper) { this.wrapper = wrapper; }

    @Override
    public void invoke(Request req, Response res) throws Exception {
        wrapper.getServlet().service(req, res);
    }
}
