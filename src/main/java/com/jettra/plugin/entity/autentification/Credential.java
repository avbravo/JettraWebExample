package com.jettra.plugin.entity.autentification;

import java.time.Instant;
import java.util.UUID;

public record Credential(
    UUID id,
    UUID userId,
    String username,
    String passwordHash,
    Boolean active,
    Instant lastLogin
) {}
