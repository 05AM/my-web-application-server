package org.web.servlet;

import org.webserver.Servlet;
import org.webserver.connector.Request;
import org.webserver.connector.Response;
import org.web.common.HttpStatus;
import org.web.registry.HandlerRegistry;
import org.web.registry.HandlerMethod;

public class DispatcherServlet implements Servlet {
    private final HandlerRegistry registry;
    private final StaticResourceHandler staticResourceHandler;

    public DispatcherServlet(HandlerRegistry registry, StaticResourceHandler staticResourceHandler) {
        this.registry = registry;
        this.staticResourceHandler = staticResourceHandler;
    }

    @Override
    public void service(Request req, Response res) throws Exception {
        // dynamic
        HandlerMethod hm = registry.getHandler(req.getMethod(), req.getPath());

        if (hm != null) {
            Object result = hm.invoke(req, res);
            if (!res.isCommitted()) {
                res.setStatus(HttpStatus.OK);
                res.getWriter().write(result != null ? result.toString() : "");
            }
            return;
        }

        // static resources
        if (staticResourceHandler != null && staticResourceHandler.handle(req, res)) {
            return;
        }

        // not found
        res.sendNotFound();
    }
}
