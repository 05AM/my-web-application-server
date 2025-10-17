package org.webserver;

public interface Pipeline {
    Valve getFirst();
    void setBasic(Valve valve);
    void addValve(Valve valve);
}
