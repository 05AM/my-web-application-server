package org.webserver.core;

import org.webserver.*;
import org.webserver.valves.StandardHostValve;

public class StandardHost implements Host {

    private final String hostName;
    private final Pipeline pipeline = new StandardPipeline();
    private Container container;

    public StandardHost(String hostName) {
        this.hostName = hostName;
        pipeline.setBasic(new StandardHostValve(this));
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
    public Pipeline getPipeline() { return pipeline; }
}
