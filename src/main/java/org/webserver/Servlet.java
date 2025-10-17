package org.webserver;

import org.webserver.connector.Request;
import org.webserver.connector.Response;

public interface Servlet {
    void service(Request req, Response res) throws Exception;
}