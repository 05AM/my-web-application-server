package org.webserver.startup;

import org.webserver.Context;
import org.webserver.Engine;
import org.webserver.Host;
import org.webserver.Wrapper;
import org.webserver.connector.Connector;
import org.webserver.core.StandardContext;
import org.webserver.core.StandardEngine;
import org.webserver.core.StandardHost;
import org.webserver.core.StandardService;
import org.webserver.core.StandardWrapper;
import org.webserver.util.logging.Log;
import org.webserver.util.logging.Logger;
import org.web.registry.HandlerRegistry;
import org.web.servlet.StaticResourceHandler;
import org.web.servlet.DispatcherServlet;

public class ServerInitializer {
    private final static Log logger = Logger.getInstance(ServerInitializer.class);

    public void start() throws Exception {
        ServerProperties properties = new ServerProperties();

        StandardService service = new StandardService();
        service.init();

        Connector connector = new Connector(properties.port, service);
        connector.init();

        // scanning controller
        HandlerRegistry registry = new HandlerRegistry().scan(properties.basePackage);
        StaticResourceHandler staticResourceHandler = new StaticResourceHandler(properties.classpathBase);
        DispatcherServlet dispatcherServlet = new DispatcherServlet(registry, staticResourceHandler);

        // initialize container
        Engine engine = new StandardEngine();
        Host host = new StandardHost(properties.hostname);
        Context context = new StandardContext();
        Wrapper wrapper = new StandardWrapper(dispatcherServlet);

        engine.setContainer(host);
        host.setContainer(context);
        context.setContainer(wrapper);

        service.setEngine(engine);
        service.addConnector(connector);
        service.start();

        logger.info("🚀 Server started on " + properties.address);
        Thread.currentThread().join();
    }
}
