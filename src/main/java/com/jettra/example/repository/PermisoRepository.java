package com.jettra.example.repository;

import com.jettra.example.model.PermisoModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PermisoRepository {
    private static final List<PermisoModel> list = new ArrayList<>();

    public static List<PermisoModel> findAll() {
        return list;
    }

    public static Optional<PermisoModel> findById(String nombre) {
        return list.stream().filter(x -> x.getNombre() != null && x.getNombre().equals(nombre)).findFirst();
    }

    public static void save(PermisoModel model) {
        if (model.getNombre() != null) {
            delete(model.getNombre());
            list.add(model);
        }
    }

    public static void delete(String nombre) {
        list.removeIf(x -> x.getNombre() != null && x.getNombre().equals(nombre));
    }
}
