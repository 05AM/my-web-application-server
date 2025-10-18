package org.web.app.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.web.app.domain.User;

public class InMemoryUserRepository implements UserRepository {

    private final List<User> store = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void save(User user) {
        store.add(user);
    }

    @Override
    public List<User> findAll() {
        synchronized (store) {
            return List.copyOf(store);
        }
    }
}
