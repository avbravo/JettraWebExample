package com.jettra.plugin.repository.autentification;

import com.jettra.plugin.entity.autentification.Role;
import com.jettra.plugin.entity.autentification.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Set;

public class RoleRepository {
    private static final List<Role> db = new ArrayList<>();

    static {
        db.add(new Role(UUID.fromString("44444444-4444-4444-4444-444444444444"), "ADMIN", Set.of()));
        db.add(new Role(UUID.fromString("55555555-5555-5555-5555-555555555555"), "MANAGER", Set.of()));
    }

    public static List<Role> findAll() {
        return new ArrayList<>(db);
    }

    public static void save(Role record) {
        if (record.id() == null) {
            record = new Role(UUID.randomUUID(), record.name(), record.permissions());
        }
        delete(record.id().toString());
        db.add(record);
    }

    public static void delete(String id) {
        db.removeIf(r -> r.id().toString().equals(id));
    }

    public static Optional<Role> findById(String id) {
        return db.stream().filter(r -> r.id().toString().equals(id)).findFirst();
    }
}
