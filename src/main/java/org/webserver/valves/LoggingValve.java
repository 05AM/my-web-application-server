package org.webserver.valves;

import org.webserver.connector.Request;
import org.webserver.connector.Response;
import org.webserver.util.logging.Log;
import org.webserver.util.logging.Logger;

public class LoggingValve extends ValveBase {

    private static final Log logger = Logger.getInstance(LoggingValve.class);

    @Override
    public void invoke(Request req, Response res) throws Exception {
        logger.info(req.toString());
        invokeNext(req, res);
    }
}