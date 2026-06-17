package com.jettra.plugin.repository.autentification;

import com.jettra.plugin.entity.autentification.User;
import com.jettra.plugin.entity.autentification.Department;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Set;

public class UserRepository {
    private static final List<User> db = new ArrayList<>();

    static {
        db.add(new User(UUID.fromString("88888888-8888-8888-8888-888888888888"), "John", "Doe", "john.doe@example.com", "123456789", true, null, Set.of()));
    }

    public static List<User> findAll() {
        return new ArrayList<>(db);
    }

    public static void save(User record) {
        if (record.id() == null) {
            record = new User(UUID.randomUUID(), record.firstName(), record.lastName(), record.email(), record.phone(), record.active(), record.department(), record.roles());
        }
        delete(record.id().toString());
        db.add(record);
    }

    public static void delete(String id) {
        db.removeIf(r -> r.id().toString().equals(id));
    }

    public static Optional<User> findById(String id) {
        return db.stream().filter(r -> r.id().toString().equals(id)).findFirst();
    }
}
