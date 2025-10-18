package org.web.app.repository;

import java.util.List;

import org.web.app.domain.User;

public interface UserRepository {

    void save(User user);

    List<User> findAll();
}
