package org.web.registry;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.webserver.connector.Request;
import org.webserver.connector.Response;

public class HandlerMethod {
    private final Object controller;
    private final Method method;

    public HandlerMethod(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public Object invoke(Request req, Response res) throws Exception {
        Object[] args = new Object[] {req, res};

        try {
            return method.invoke(controller, args);
        } catch (InvocationTargetException e) {
            throw (e.getCause() instanceof Exception ex) ? ex : e;
        }
    }
}