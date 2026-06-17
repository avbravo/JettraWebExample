package com.jettra.plugin.repository.autentification;

import com.jettra.plugin.entity.autentification.Credential;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.time.Instant;

public class CredentialRepository {
    private static final List<Credential> db = new ArrayList<>();

    static {
        db.add(new Credential(UUID.fromString("99999999-9999-9999-9999-999999999999"), UUID.fromString("88888888-8888-8888-8888-888888888888"), "johndoe", "hashedpassword", true, Instant.now()));
    }

    public static List<Credential> findAll() {
        return new ArrayList<>(db);
    }

    public static void save(Credential record) {
        if (record.id() == null) {
            record = new Credential(UUID.randomUUID(), record.userId(), record.username(), record.passwordHash(), record.active(), record.lastLogin());
        }
        delete(record.id().toString());
        db.add(record);
    }

    public static void delete(String id) {
        db.removeIf(r -> r.id().toString().equals(id));
    }

    public static Optional<Credential> findById(String id) {
        return db.stream().filter(r -> r.id().toString().equals(id)).findFirst();
    }
}
