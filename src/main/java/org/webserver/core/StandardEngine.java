package org.webserver.core;

import org.webserver.Container;
import org.webserver.Engine;
import org.webserver.Pipeline;
import org.webserver.valves.LoggingValve;
import org.webserver.valves.StandardEngineValve;

public class StandardEngine implements Engine {

    private final Pipeline pipeline = new StandardPipeline();
    private Container container;

    public StandardEngine() {
        pipeline.addValve(new LoggingValve());
        pipeline.setBasic(new StandardEngineValve(this));
    }

    @Override
    public Container getContainer() {
        return this.container;
    }

    @Override
    public void setContainer(Container container) {
        this.container = container;
    }

    @Override
    public Pipeline getPipeline() {
        return pipeline;
    }
}
