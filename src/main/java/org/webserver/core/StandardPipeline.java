package org.webserver.core;

import org.webserver.*;
import org.webserver.valves.ValveBase;

public class StandardPipeline implements Pipeline {
    private Valve first;
    private Valve basic;

    @Override public Valve getFirst() { return first; }

    @Override
    public void setBasic(Valve valve) {
        this.basic = valve;
        attach(valve);
    }

    @Override
    public void addValve(Valve valve) {
        attach(valve);
        if (basic != null && valve instanceof ValveBase vb) vb.setNext(basic);
    }

    private void attach(Valve valve) {
        if (first == null) { first = valve; return; }
        Valve cur = first;
        while (cur instanceof ValveBase vb && vb.getNext() != null) cur = vb.getNext();
        if (cur instanceof ValveBase vb) vb.setNext(valve);
    }
}

