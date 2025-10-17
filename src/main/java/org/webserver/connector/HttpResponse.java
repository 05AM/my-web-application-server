package org.webserver.connector;

public class HttpResponse {
    private byte[] body = new byte[0];
    private boolean keepAlive = true;

    public byte[] body() { return body; }
    public void setBody(byte[] body) { this.body = body; }

    public boolean keepAlive() { return keepAlive; }
    public void setKeepAlive(boolean keepAlive) { this.keepAlive = keepAlive; }
}
