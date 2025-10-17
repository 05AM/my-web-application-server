package org.webserver.core;

import org.webserver.Container;
import org.webserver.Pipeline;
import org.webserver.Servlet;
import org.webserver.Wrapper;
import org.webserver.valves.StandardWrapperValve;

public class StandardWrapper implements Wrapper {

    private final Pipeline pipeline = new StandardPipeline();
    private final Servlet servlet;

    public StandardWrapper(Servlet servlet) {
        this.servlet = servlet;
        pipeline.setBasic(new StandardWrapperValve(this));
    }

    @Override
    public Servlet getServlet() {
        return this.servlet;
    }

    @Override
    public Container getContainer() {
        return null;
    }

    @Override
    public void setContainer(Container container) {
        return;
    }

    @Override
    public Pipeline getPipeline() {
        return this.pipeline;
    }
}
