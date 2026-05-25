package com.jettra.example.entity.admin;

public record Perfil(String nombreCompleto, String avatarUrl, String zonaHoraria) {
    public Perfil(String nombreCompleto) {
        this(nombreCompleto, null, "UTC");
    }
}
