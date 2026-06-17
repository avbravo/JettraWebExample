package com.jettra.plugin.entity.autentification;

import java.util.UUID;
import java.util.Set;

public record Role(
    UUID id,
    String name,
    Set<Permission> permissions
) {}
