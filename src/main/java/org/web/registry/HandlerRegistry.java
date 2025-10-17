package org.web.registry;

import org.webserver.util.logging.Log;
import org.webserver.util.logging.Logger;
import org.web.annotation.Controller;
import org.web.annotation.RequestMapping;
import org.web.common.HttpMethod;
import org.web.util.ClasspathScanner;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HandlerRegistry {
    private final static Log logger = Logger.getInstance(HandlerRegistry.class);

    private final Map<String, HandlerMethod> mappings = new HashMap<>();

    public HandlerRegistry scan(String basePackage) {
        for (Class<?> clazz : ClasspathScanner.findClasses(basePackage)) {
            if (!clazz.isAnnotationPresent(Controller.class)) continue;

            String basePath = clazz.getAnnotation(Controller.class).basePath();
            Object controller = newInstance(clazz);

            for (Method m : clazz.getDeclaredMethods()) {
                var rm = m.getAnnotation(RequestMapping.class);
                if (rm == null) continue;

                String fullPath = normalizePath(basePath, rm.path());
                String key = rm.method().name().toUpperCase() + " " + fullPath;
                mappings.put(key, new HandlerMethod(controller, m));

                logger.info("[MAPPING] " + key + " -> " + clazz.getSimpleName() + "." + m.getName());
            }
        }
        return this;
    }

    public HandlerMethod getHandler(HttpMethod httpMethod, String path) {
        return mappings.get(httpMethod.name() + " " + path);
    }

    private static Object newInstance(Class<?> clazz) {
        try {
            var ctor = clazz.getDeclaredConstructor();
            ctor.setAccessible(true);
            return ctor.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Failure on instancing controller: " + clazz.getName(), e);
        }
    }

    private static String normalizePath(String base, String sub) {
        String b = (base == null) ? "" : base.trim();
        String s = (sub == null) ? "" : sub.trim();
        if (!b.startsWith("/")) b = "/" + b;
        if (b.endsWith("/") && s.startsWith("/")) s = s.substring(1);
        String path = (b + "/" + s).replaceAll("//+", "/");
        return path.equals("/") ? "/" : path;
    }
}