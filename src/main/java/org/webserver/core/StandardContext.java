package org.webserver.core;

import org.webserver.Container;
import org.webserver.Context;
import org.webserver.Pipeline;
import org.webserver.valves.StandardContextValve;

public class StandardContext implements Context {

    private final Pipeline pipeline = new StandardPipeline();
    private Container container;

    public StandardContext() {
        pipeline.setBasic(new StandardContextValve(this));
    }

    @Override
    public Container getContainer() {
        return container;
    }

    @Override
    public void setContainer(Container container) {
        this.container = container;
    }

    @Override
    public Pipeline getPipeline() {
        return this.pipeline;
    }
}
