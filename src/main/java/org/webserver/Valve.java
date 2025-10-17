package org.webserver;

import org.webserver.connector.Request;
import org.webserver.connector.Response;

public interface Valve {
    void invoke(Request req, Response res) throws Exception;
}
