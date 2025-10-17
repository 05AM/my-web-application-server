package org.web.util;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class ClasspathScanner {
    private ClasspathScanner() {}

    public static List<Class<?>> findClasses(String basePackage) {
        String path = basePackage.replace('.', '/');
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        List<Class<?>> result = new ArrayList<>();
        try {
            Enumeration<URL> resources = cl.getResources(path);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                if ("file".equals(url.getProtocol())) {
                    result.addAll(scanDirectory(new File(URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8)), basePackage));
                } else if ("jar".equals(url.getProtocol())) {
                    result.addAll(scanJar(url, path));
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("scan fail: " + basePackage, e);
        }
        return result;
    }

    private static List<Class<?>> scanDirectory(File dir, String basePackage) {
        List<Class<?>> classes = new ArrayList<>();
        if (!dir.exists()) return classes;

        for (File f : Objects.requireNonNull(dir.listFiles())) {
            if (f.isDirectory()) {
                classes.addAll(scanDirectory(f, basePackage + "." + f.getName()));
            } else if (f.getName().endsWith(".class")) {
                String className = basePackage + '.' + f.getName().substring(0, f.getName().length() - 6);
                loadSafely(classes, className);
            }
        }
        return classes;
    }

    private static List<Class<?>> scanJar(URL url, String path) {
        List<Class<?>> classes = new ArrayList<>();
        try {
            String jarPath = url.getPath();
            if (jarPath.startsWith("file:")) jarPath = jarPath.substring(5);
            if (jarPath.contains("!")) jarPath = jarPath.substring(0, jarPath.indexOf('!'));
            try (JarFile jar = new JarFile(URLDecoder.decode(jarPath, StandardCharsets.UTF_8))) {
                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    JarEntry e = entries.nextElement();
                    if (!e.getName().startsWith(path) || !e.getName().endsWith(".class")) continue;
                    String className = e.getName().replace('/', '.').substring(0, e.getName().length() - 6);
                    loadSafely(classes, className);
                }
            }
        } catch (IOException ignored) {}
        return classes;
    }

    private static void loadSafely(List<Class<?>> out, String className) {
        try {
            out.add(Class.forName(className));
        } catch (Throwable ignored) {}
    }
}
