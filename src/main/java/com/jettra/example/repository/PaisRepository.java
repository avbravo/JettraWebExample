package com.jettra.example.repository;

import com.jettra.example.model.PaisModel;
import java.util.List;

public interface PaisRepository {
    List<PaisModel> findAll();
    PaisModel findByCode(String code);
    void save(PaisModel pais);
    void delete(String code);
}
