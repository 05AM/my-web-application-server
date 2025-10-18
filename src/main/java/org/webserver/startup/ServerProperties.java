package org.webserver.startup;

public class ServerProperties {
    public String hostname = "localhost";
    public int port = 8080;
    public String address;
    public String basePackage = "org.web.app.controller";
    public String classpathBase = "static";

    public ServerProperties() {
        this.address = hostname + ":" + port;
    }
}
