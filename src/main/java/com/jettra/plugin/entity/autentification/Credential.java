package com.jettra.plugin.entity.autentification;

import java.time.Instant;
import java.util.UUID;

public record Credential(
    UUID id,
    User user,
    String username,
    String passwordHash,
    Boolean active,
    Instant lastLogin
) {}
