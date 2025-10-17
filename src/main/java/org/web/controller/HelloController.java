package org.web.controller;

import org.web.annotation.Controller;
import org.web.annotation.RequestMapping;
import org.web.common.HttpMethod;

@Controller(basePath = "/api")
public class HelloController {

    @RequestMapping(path = "/hello", method = HttpMethod.GET)
    public String hello() {
        return "Hello, world!";
    }

    @RequestMapping(path = "/ping", method = HttpMethod.GET)
    public String ping() {
        return "pong";
    }
}
