package org.webserver.util.net;

import org.webserver.Adapter;
import org.webserver.LifeCycle;
import org.webserver.util.http.HttpProcessor;
import org.webserver.util.logging.Log;
import org.webserver.util.logging.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.concurrent.Executor;

public class Nio2Endpoint implements LifeCycle {
    private final static Log logger = Logger.getInstance(Nio2Endpoint.class);

    private final int port;
    private final Adapter adapter;
    private Executor executor;
    private AsynchronousChannelGroup group;
    private AsynchronousServerSocketChannel server;

    public Nio2Endpoint(int port, Adapter adapter) {
        this.port = port;
        this.adapter = adapter;
    }

    public void setExecutor(Executor executor) { this.executor = executor; }

    @Override
    public void init() throws Exception {
        if (executor == null)
            throw new IllegalStateException("Executor must be injected before init()");

        group = AsynchronousChannelGroup.withFixedThreadPool(
            Runtime.getRuntime().availableProcessors(),
            Thread::new
        );
        server = AsynchronousServerSocketChannel.open(group)
            .bind(new InetSocketAddress(port));

        logger.info("[SOCKET] Listening on " + port);
    }

    @Override
    public void start() {
        server.accept(null, new CompletionHandler<>() {
            @Override
            public void completed(AsynchronousSocketChannel socket, Object attachment) {
                server.accept(null, this);
                executor.execute(new HttpProcessor(socket, adapter));
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                logger.error("[ACCEPT FAIL] " + exc);
                server.accept(null, this);
            }
        });
    }

    @Override public void stop() throws Exception {
        try { if (server != null) server.close(); } catch (IOException ignore) {}
        if (group != null) group.shutdownNow();
    }

    @Override public void destroy() throws Exception {}
}
