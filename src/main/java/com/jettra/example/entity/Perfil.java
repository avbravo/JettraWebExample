package com.jettra.example.entity;

public record Perfil(String nombreCompleto, String avatarUrl, String zonaHoraria) {
    public Perfil(String nombreCompleto) {
        this(nombreCompleto, null, "UTC");
    }
}
