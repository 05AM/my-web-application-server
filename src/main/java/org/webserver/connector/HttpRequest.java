package org.webserver.connector;

public class HttpRequest {
    private final String raw;
    public HttpRequest(String raw) { this.raw = raw; }

    public String method() {
        String[] parts = raw.split(" ");
        return parts.length > 0 ? parts[0] : "GET";
    }

    public String path() {
        String[] parts = raw.split(" ");
        return parts.length > 1 ? parts[1] : "/";
    }
}
