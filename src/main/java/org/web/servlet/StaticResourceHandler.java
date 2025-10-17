package org.web.servlet;

import org.webserver.connector.Request;
import org.webserver.connector.Response;
import org.web.common.HttpMethod;
import org.web.common.HttpStatus;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class StaticResourceHandler {
    private final String classpathBase;
    private final String indexName;
    private final int cacheSeconds;

    private static final DateTimeFormatter RFC_1123 = DateTimeFormatter
        .ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US)
        .withZone(ZoneId.of("GMT"));

    public StaticResourceHandler(String classpathBase) {
        this(classpathBase, "index.html", 0);
    }

    public StaticResourceHandler(String classpathBase, String indexName, int cacheSeconds) {
        if (classpathBase == null || classpathBase.isBlank()) throw new IllegalArgumentException("classpathBase required");
        this.classpathBase = trimSlashes(classpathBase);
        this.indexName = (indexName == null || indexName.isBlank()) ? "index.html" : indexName;
        this.cacheSeconds = Math.max(0, cacheSeconds);
    }

    public boolean handle(Request req, Response res) throws IOException {
        if (!HttpMethod.GET.equals(req.getMethod()) && !HttpMethod.HEAD.equals(req.getMethod())) {
            return false;
        }

        String rawPath = req.getPath();
        if (rawPath == null || rawPath.isBlank()) rawPath = "/";

        int q = rawPath.indexOf('?');
        if (q >= 0) rawPath = rawPath.substring(0, q);

        String normalized = normalizeForClasspath(rawPath);
        if (normalized == null) {
            res.setStatus(HttpStatus.FORBIDDEN);
            res.getWriter().write("403 Forbidden");
            return true;
        }

        String candidate = this.classpathBase + (normalized.startsWith("/") ? normalized : "/" + normalized);

        if (candidate.endsWith("/")) {
            candidate = candidate + indexName;
        }

        URL url = getResource(candidate);
        if (url == null && !hasExtension(candidate)) {
            url = getResource(candidate + "/" + indexName);
            if (url != null) candidate = candidate + "/" + indexName;
        }
        if (url == null) {
            return false;
        }

        long lastModifiedMillis = 0L;
        try {
            URLConnection conn = url.openConnection();
            lastModifiedMillis = conn.getLastModified();
        } catch (Exception ignore) { /* some classloaders don't support */ }

        if (lastModifiedMillis > 0) {
            String lastModified = RFC_1123.format(Instant.ofEpochMilli(lastModifiedMillis));
            res.setHeader("Last-Modified", lastModified);

            String ifModifiedSince = req.getHeader("If-Modified-Since");
            if (ifModifiedSince != null && !ifModifiedSince.isBlank()) {
                try {
                    long since = Instant.from(RFC_1123.parse(ifModifiedSince)).toEpochMilli();
                    if (lastModifiedMillis / 1000 <= since / 1000) {
                        res.setStatus(HttpStatus.NOT_MODIFIED);
                        return true;
                    }
                } catch (Exception ignore) { /* ignore bad header */ }
            }
        }

        // Content-Type
        String mime = guessContentType(candidate);
        res.setHeader("Content-Type", mime);

        // Cache-Control
        if (cacheSeconds > 0) {
            res.setHeader("Cache-Control", "public, max-age=" + cacheSeconds);
            res.setHeader("Expires", RFC_1123.format(Instant.now().plusSeconds(cacheSeconds)));
        }

        res.setStatus(HttpStatus.OK);
        if (HttpMethod.HEAD.equals(req.getMethod())) {
            return true;
        }

        try (InputStream in = url.openStream()) {
            in.transferTo(res.getOutputStream());
        }
        return true;
    }

    // ---- helpers ----

    private static URL getResource(String path) {
        String p = path.startsWith("/") ? path.substring(1) : path;
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL url = (cl != null) ? cl.getResource(p) : null;
        if (url == null) url = StaticResourceHandler.class.getClassLoader().getResource(p);
        return url;
    }

    private static String trimSlashes(String s) {
        String r = s.trim();
        while (r.startsWith("/")) r = r.substring(1);
        while (r.endsWith("/")) r = r.substring(0, r.length() - 1);
        return r;
    }

    private static String normalizeForClasspath(String raw) {
        String p = raw.replace('\\', '/');
        if (!p.startsWith("/")) p = "/" + p;
        p = p.replaceAll("/{2,}", "/");

        String[] parts = p.split("/");
        StringBuilder sb = new StringBuilder();
        int depth = 0;
        for (String part : parts) {
            if (part.isEmpty() || ".".equals(part)) continue;
            if ("..".equals(part)) {
                if (depth == 0) return null;
                int last = sb.lastIndexOf("/");
                sb.delete(last, sb.length());
                depth--;
            } else {
                sb.append('/').append(part);
                depth++;
            }
        }
        return sb.length() == 0 ? "/" : sb.toString();
    }

    private static boolean hasExtension(String path) {
        int lastSlash = path.lastIndexOf('/');
        int lastDot = path.lastIndexOf('.');
        return lastDot > lastSlash;
    }

    private static String guessContentType(String resourcePath) {
        String mime = java.net.URLConnection.guessContentTypeFromName(resourcePath);
        return (mime != null) ? mime : "application/octet-stream";
    }
}
