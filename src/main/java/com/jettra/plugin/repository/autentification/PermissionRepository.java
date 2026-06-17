package com.jettra.plugin.repository.autentification;

import com.jettra.plugin.entity.autentification.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PermissionRepository {
    private static final List<Permission> db = new ArrayList<>();

    static {
        db.add(new Permission(UUID.fromString("11111111-1111-1111-1111-111111111111"), "USER_CREATE", "Create users", "/api/autentification/users"));
        db.add(new Permission(UUID.fromString("22222222-2222-2222-2222-222222222222"), "USER_DELETE", "Delete users", "/api/autentification/users"));
        db.add(new Permission(UUID.fromString("33333333-3333-3333-3333-333333333333"), "ROLE_VIEW", "View roles", "/api/autentification/roles"));
    }

    public static List<Permission> findAll() {
        return new ArrayList<>(db);
    }

    public static void save(Permission record) {
        if (record.id() == null) {
            record = new Permission(UUID.randomUUID(), record.name(), record.description(), record.resourcePath());
        }
        delete(record.id().toString());
        db.add(record);
    }

    public static void delete(String id) {
        db.removeIf(r -> r.id().toString().equals(id));
    }

    public static Optional<Permission> findById(String id) {
        return db.stream().filter(r -> r.id().toString().equals(id)).findFirst();
    }
}
