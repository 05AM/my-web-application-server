package org.webserver.connector;

import org.web.common.HttpStatus;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class Response {
    private HttpStatus status = HttpStatus.OK;
    private final Map<String, String> headers = new LinkedHashMap<>();
    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    private boolean keepAlive = true;
    private boolean isCommitted = false;

    private PrintWriter writer;

    public HttpStatus status() { return status; }
    public void setStatus(HttpStatus status) { this.status = status; }

    public void setHeader(String key, String value) { headers.put(key, value); }
    public String getHeader(String key) { return headers.get(key); }

    public boolean keepAlive() { return keepAlive; }
    public void setKeepAlive(boolean keepAlive) { this.keepAlive = keepAlive; }

    public boolean isCommitted() { return isCommitted; }
    public void setIsCommitted(boolean isCommited) { this.isCommitted = isCommited; }

    public PrintWriter getWriter() {
        if (writer == null) {
            writer = new PrintWriter(new OutputStreamWriter(buffer, StandardCharsets.UTF_8), true);
        }
        return writer;
    }

    public OutputStream getOutputStream() {
        if (writer != null) writer.flush(); // writer로 쓴 내용 반영
        return buffer;
    }

    public void writeBody(String content) {
        getWriter().write(content + "\n");
        isCommitted = true;
    }

    public byte[] body() {
        if (writer != null) writer.flush();
        return buffer.toByteArray();
    }

    public void sendNotFound() {
        this.status = HttpStatus.NOT_FOUND;
        setHeader("Content-Type", "text/plain; charset=UTF-8");
        writeBody("404 Not Found");
    }

    public void sendRedirection(HttpStatus status) {
        this.status = status;
        setHeader("Location", "/api/users");
        setHeader("Content-Length", "0");
        writeBody("");
    }

    public byte[] toHttpBytes() {
        if (writer != null) writer.flush();

        headers.putIfAbsent("Content-Length", String.valueOf(buffer.size()));
        headers.putIfAbsent("Connection", keepAlive ? "keep-alive" : "close");

        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 ")
            .append(status.code()).append(" ").append(status.reason()).append("\r\n");
        for (var e : headers.entrySet()) {
            sb.append(e.getKey()).append(": ").append(e.getValue()).append("\r\n");
        }
        sb.append("\r\n");

        byte[] head = sb.toString().getBytes(StandardCharsets.UTF_8);
        byte[] body = buffer.toByteArray();
        byte[] all = new byte[head.length + body.length];

        System.arraycopy(head, 0, all, 0, head.length);
        System.arraycopy(body, 0, all, head.length, body.length);

        return all;
    }
}
