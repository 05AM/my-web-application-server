package org.web.app.controller;

import org.web.annotation.Controller;
import org.web.annotation.RequestMapping;
import org.web.common.HttpMethod;
import org.webserver.connector.Request;
import org.webserver.connector.Response;

@Controller(basePath = "/api")
public class HelloController {

    @RequestMapping(path = "/hello", method = HttpMethod.GET)
    public String hello(Request request, Response response) {
        return "Hello, world!";
    }
}
