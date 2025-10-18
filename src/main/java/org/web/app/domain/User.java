package org.web.app.domain;

import java.util.concurrent.atomic.AtomicLong;

public class User {
    private static final AtomicLong SEQ = new AtomicLong(1);

    private final long id;
    private final String name;
    private final String email;

    public User(String name, String email) {
        this.id = SEQ.getAndIncrement();
        this.name = name;
        this.email = email;
    }
    public long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return "Id: " + id + ", name: " + name + ", email: " + email;
    }
}
