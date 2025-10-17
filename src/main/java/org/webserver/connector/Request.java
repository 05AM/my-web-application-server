package org.webserver.connector;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import org.web.common.HttpMethod;

public class Request {

    private final HttpMethod method;
    private final String path;
    private final String protocol;
    private final Map<String, String> headers;
    private final String body;

    public Request(String method, String path, String protocol, Map<String, String> headers, String body) {
        this.method = HttpMethod.valueOf(method.trim().toUpperCase());
        this.path = path;
        this.protocol = protocol;
        this.headers = headers != null ? headers : Map.of();
        this.body = body != null ? body : "";
    }

    public HttpMethod getMethod() { return method; }
    public String getPath() { return path; }
    public String getProtocol() { return protocol; }
    public Map<String, String> getHeaders() { return Collections.unmodifiableMap(headers); }
    public String getHeader(String key) { return headers.getOrDefault(key, ""); }
    public String getBody() { return body; }

    @Override
    public String toString() {
        String headersStr = headers.entrySet().stream()
            .map(e -> "> " + e.getKey() + ": " + e.getValue())
            .collect(Collectors.joining("\n"));

        return String.format(
            "[REQUEST] %s %s (%s)\n\n=== Headers ===\n%s\n\n=== Body ===\n%s\n",
            method, path, protocol, headersStr,
            body.isBlank() ? "(empty)" : body
        );
    }
}
