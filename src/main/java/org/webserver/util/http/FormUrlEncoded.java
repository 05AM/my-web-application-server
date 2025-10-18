package org.webserver.util.http;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

final class FormUrlEncoded {
    private FormUrlEncoded() {}

    static Map<String, String> parse(String body) {
        if (body == null || body.isEmpty()) return Map.of();
        Map<String, String> map = new LinkedHashMap<>();
        int i = 0, n = body.length();
        while (i <= n) {
            int amp = body.indexOf('&', i);
            if (amp == -1) amp = n;
            String pair = body.substring(i, amp);
            int eq = pair.indexOf('=');
            String k = eq >= 0 ? pair.substring(0, eq) : pair;
            String v = eq >= 0 ? pair.substring(eq + 1) : "";

            map.put(decode(k), decode(v)); // 같은 키면 마지막 값으로 덮어씀
            i = amp + 1;
        }
        return map;
    }

    private static String decode(String s) {
        if (s == null) return "";
        s = s.replace('+', ' ');
        byte[] buf = new byte[s.length()];
        int bi = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '%' && i + 2 < s.length()) {
                int hi = hex(s.charAt(i + 1));
                int lo = hex(s.charAt(i + 2));
                if (hi >= 0 && lo >= 0) {
                    buf[bi++] = (byte)((hi << 4) + lo);
                    i += 2;
                    continue;
                }
            }
            buf[bi++] = (byte)c;
        }
        return new String(buf, 0, bi, StandardCharsets.UTF_8);
    }

    private static int hex(char c) {
        if ('0' <= c && c <= '9') return c - '0';
        if ('a' <= c && c <= 'f') return c - 'a' + 10;
        if ('A' <= c && c <= 'F') return c - 'A' + 10;
        return -1;
    }
}
