package org.webserver.util.http;

import java.util.HashMap;
import java.util.Map;

import org.webserver.connector.Request;

public class HttpParser {

    public static Request parse(String raw) {
        if (raw == null || raw.isBlank())
            return new Request("GET", "/", "HTTP/1.1", Map.of(), "");

        String[] parts = raw.split("\r?\n\r?\n", 2);
        String headerPart = parts[0];
        String bodyPart = parts.length > 1 ? parts[1] : "";

        String[] lines = headerPart.split("\r?\n");
        String[] requestLine = lines[0].split(" ");
        String method = requestLine.length > 0 ? requestLine[0] : "GET";
        String path = requestLine.length > 1 ? requestLine[1] : "/";
        String protocol = requestLine.length > 2 ? requestLine[2] : "HTTP/1.1";

        Map<String, String> headers = new HashMap<>();
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) break;
            int colon = line.indexOf(':');
            if (colon > 0) {
                String key = line.substring(0, colon).trim();
                String value = line.substring(colon + 1).trim();
                headers.put(key, value);
            }
        }

        Map<String, String> form = Map.of();
        String contentType = headers.entrySet().stream()
            .filter(e -> e.getKey().equalsIgnoreCase("Content-Type"))
            .map(Map.Entry::getValue)
            .findFirst().orElse(null);

        if (contentType != null && contentType.toLowerCase().startsWith("application/x-www-form-urlencoded")) {
            form = FormUrlEncoded.parse(bodyPart);
        }

        return new Request(method, path, protocol, headers, bodyPart, form);
    }
}
