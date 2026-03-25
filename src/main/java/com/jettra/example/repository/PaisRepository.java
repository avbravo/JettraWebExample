package com.jettra.example.repository;

import com.jettra.example.model.Pais;
import java.util.ArrayList;
import java.util.List;

public class PaisRepository {
    private static final List<Pais> paises = new ArrayList<>();

    static {
        paises.add(new Pais("PA", "Panamá"));
        paises.add(new Pais("CR", "Costa Rica"));
        paises.add(new Pais("US", "Estados Unidos"));
    }

    public static List<Pais> findAll() {
        return new ArrayList<>(paises);
    }

    public static Pais findByCode(String code) {
        return paises.stream().filter(p -> p.getCode().equals(code)).findFirst().orElse(null);
    }

    public static void save(Pais pais) {
        Pais existing = findByCode(pais.getCode());
        if (existing != null) {
            existing.setName(pais.getName());
        } else {
            paises.add(pais);
        }
    }

    public static void delete(String code) {
        paises.removeIf(p -> p.getCode().equals(code));
    }
}
