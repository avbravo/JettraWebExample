package com.jettra.plugin.entity.autentification;

import java.util.UUID;

public record Permission(
    UUID id,
    String name,
    String description,
    String resourcePath
) {}
