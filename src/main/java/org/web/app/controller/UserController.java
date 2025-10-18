package org.web.app.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.web.annotation.Controller;
import org.web.annotation.RequestMapping;
import org.web.app.domain.User;
import org.web.app.repository.InMemoryUserRepository;
import org.web.app.repository.UserRepository;
import org.web.common.HttpMethod;
import org.web.common.HttpStatus;
import org.webserver.connector.Request;
import org.webserver.connector.Response;

@Controller(basePath = "/api")
public class UserController {

    private final UserRepository userRepository = new InMemoryUserRepository();

    @RequestMapping(path = "/users/signup", method = HttpMethod.POST)
    public void signup(Request request, Response response) {
        String name = request.getForm("name");
        String email = request.getForm("email");

        User user = new User(name, email);
        userRepository.save(user);

        response.sendRedirection(HttpStatus.MOVED_PERMANENTLY);
    }

    @RequestMapping(path = "/users", method = HttpMethod.GET)
    public String getUsers(Request request, Response response) {
        List<User> users = userRepository.findAll();

        return users.stream()
            .map(User::toString)
            .collect(Collectors.joining("\n"));
    }
}
