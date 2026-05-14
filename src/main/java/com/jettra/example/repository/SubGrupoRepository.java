package com.jettra.example.repository;

import com.jettra.example.model.SubGrupoModel;
import com.jettra.example.model.GrupoModel;
import java.util.ArrayList;
import java.util.List;

public class SubGrupoRepository {
    private static final List<SubGrupoModel> subgrupos = new ArrayList<>();

    static {
        List<GrupoModel> grupos = GrupoRepository.findAll();
        subgrupos.add(new SubGrupoModel("SG1", "Java Backend", grupos.get(0), "FUT,TEN"));
        subgrupos.add(new SubGrupoModel("SG2", "Vue Frontend", grupos.get(0), "NAT"));
        subgrupos.add(new SubGrupoModel("SG3", "Help Desk", grupos.get(1), "FUT"));
    }

    public static List<SubGrupoModel> findAll() {
        return new ArrayList<>(subgrupos);
    }

    public static SubGrupoModel findById(String id) {
        return subgrupos.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
    }

    public static void save(SubGrupoModel subgrupo) {
        SubGrupoModel existing = findById(subgrupo.getId());
        if (existing != null) {
            existing.setName(subgrupo.getName());
            existing.setGrupoModel(subgrupo.getGrupoModel());
            existing.setDeportes(subgrupo.getDeportes());
        } else {
            subgrupos.add(subgrupo);
        }
    }

    public static void delete(String id) {
        subgrupos.removeIf(s -> s.getId().equals(id));
    }
}
