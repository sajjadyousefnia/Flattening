package com.sajjady.flattening.feature.serialization.model;

import java.io.Serial;
import java.io.Serializable;

public class JavaSerializableUser implements Serializable {
    @Serial
    private static final long serialVersionUID = 42L;

    private final long id;
    private final String username;
    private final transient String password;

    public JavaSerializableUser(long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "JavaSerializableUser{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            '}';
    }
}
