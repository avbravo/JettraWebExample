package com.jettra.example.repository;

import com.jettra.example.model.PaisModel;
import java.util.ArrayList;
import java.util.List;

public class PaisRepository {
    private static final List<PaisModel> paises = new ArrayList<>();

    static {
        paises.add(new PaisModel("PA", "Panamá"));
        paises.add(new PaisModel("CR", "Costa Rica"));
        paises.add(new PaisModel("US", "Estados Unidos"));
    }

    public static List<PaisModel> findAll() {
        return new ArrayList<>(paises);
    }

    public static PaisModel findByCode(String code) {
        return paises.stream().filter(p -> p.getCode().equals(code)).findFirst().orElse(null);
    }

    public static void save(PaisModel pais) {
        PaisModel existing = findByCode(pais.getCode());
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
