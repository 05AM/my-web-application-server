package org.webserver.valves;

import org.webserver.Valve;
import org.webserver.connector.Request;
import org.webserver.connector.Response;

public abstract class ValveBase implements Valve {
    protected Valve next;

    public void setNext(Valve next) { this.next = next; }
    public Valve getNext() { return next; }

    protected void invokeNext(Request req, Response res) throws Exception {
        if (next != null) next.invoke(req, res);
    }
}
