package org.web.registry;

import java.lang.reflect.Method;

public class HandlerMethod {
    private final Object controller;
    private final Method method;

    public HandlerMethod(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public Object invoke() throws Exception {
        return method.invoke(controller);
    }
}