package org.webserver.startup;

public class BootStrap {
    public static void main(String[] args) throws Exception {
        ServerInitializer initializer = new ServerInitializer();
        initializer.start();
    }
}