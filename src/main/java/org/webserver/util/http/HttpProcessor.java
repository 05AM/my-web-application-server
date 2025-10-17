package org.webserver.util.http;

import org.webserver.Adapter;
import org.webserver.connector.Request;
import org.webserver.connector.Response;
import org.web.common.HttpStatus;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

public class HttpProcessor implements Runnable {
    private final AsynchronousSocketChannel socket;
    private final Adapter adapter;
    private final ByteBuffer readBuf = ByteBuffer.allocateDirect(8192);
    private final ByteBuffer writeBuf = ByteBuffer.allocateDirect(8192);

    public HttpProcessor(AsynchronousSocketChannel socket, Adapter adapter) {
        this.socket = socket;
        this.adapter = adapter;
    }

    @Override
    public void run() { doRead(); }

    private void doRead() {
        readBuf.clear();
        socket.read(readBuf, null, new CompletionHandler<Integer, Void>() {
            @Override
            public void completed(Integer n, Void att) {
                if (n == null || n <= 0) { close(); return; }
                readBuf.flip();

                String raw = StandardCharsets.UTF_8.decode(readBuf).toString();
                Request req = HttpParser.parse(raw);
                Response res = new Response();

                try {
                    adapter.service(req, res);
                    if (res.getHeader("Content-Type") == null) {
                        res.setHeader("Content-Type", "text/plain; charset=utf-8");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                    res.setHeader("Content-Type", "text/plain; charset=utf-8");
                    res.getWriter().write("Internal Server Error: " + e.getMessage());
                    res.setKeepAlive(false);
                }

                doWrite(res);
            }

            @Override
            public void failed(Throwable exc, Void att) { close(); }
        });
    }

    private void doWrite(Response res) {
        byte[] out = res.toHttpBytes();
        ByteBuffer buf = ByteBuffer.wrap(out);

        socket.write(buf, null, new CompletionHandler<Integer, Void>() {
            @Override
            public void completed(Integer n, Void att) {
                if (buf.hasRemaining()) {
                    socket.write(buf, null, this);
                    return;
                }
                if (res.keepAlive()) doRead();
                else close();
            }

            @Override
            public void failed(Throwable exc, Void att) { close(); }
        });
    }

    private void close() {
        try { socket.close(); } catch (Exception ignore) {}
    }
}
