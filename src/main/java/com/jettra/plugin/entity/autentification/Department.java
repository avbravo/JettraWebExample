package com.jettra.plugin.entity.autentification;

import java.util.UUID;

public record Department(
    UUID id,
    String name,
    UUID parentDepartmentId
) {}
