package org.webserver;

import org.webserver.connector.Request;
import org.webserver.connector.Response;

public interface Adapter {
    void service(Request req, Response res) throws Exception;
}