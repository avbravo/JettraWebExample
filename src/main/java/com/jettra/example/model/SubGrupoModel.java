
package com.jettra.example.model;

import io.jettra.wui.core.annotations.JettraViewModel;
import io.jettra.wui.core.annotations.PropertiesLabel;
import io.jettra.wui.core.annotations.ViewSelectMany;
import io.jettra.wui.core.annotations.ViewSelectOne;
import io.jettra.wui.validations.NotNull;
import io.jettra.wui.validations.Size;

@JettraViewModel
public class SubGrupoModel {
    @NotNull
    @Size(min = 2, max = 5)
    @PropertiesLabel(value = "lbl.id", label = "ID")
    private String id;
    
    @NotNull
    @Size(min = 3, max = 100)
    @PropertiesLabel(value = "lbl.name", label = "SubGrupo")
    private String name;
    
    @NotNull
    @ViewSelectOne(label = "name", source = "GrupoRepository", method = "findAll")
    @PropertiesLabel(value = "lbl.grupo", label = "Grupo")
    private GrupoModel grupoModel;
    
    @ViewSelectMany(label = "deporte", source = "DeporteRepository", method = "findAll")
    @PropertiesLabel(value = "lbl.deportes", label = "Deportes")
    private String deportes;
    

    public SubGrupoModel() {}

    public SubGrupoModel(String id, String name, GrupoModel grupoModel) {
        this.id = id;
        this.name = name;
        this.grupoModel = grupoModel;
    }

    public SubGrupoModel(String id, String name, GrupoModel grupoModel, String deportes) {
        this.id = id;
        this.name = name;
        this.grupoModel = grupoModel;
        this.deportes = deportes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GrupoModel getGrupoModel() {
        return grupoModel;
    }

    public void setGrupoModel(GrupoModel grupoModel) {
        this.grupoModel = grupoModel;
    }

    public String getDeportes() {
        return deportes;
    }

    public void setDeportes(String deportes) {
        this.deportes = deportes;
    }

    @Override
    public String toString() {
        return "SubGrupoModel{" + "id=" + id + ", name=" + name + ", grupoModel=" + (grupoModel != null ? grupoModel.getName() : "null") + ", deportes=" + deportes + '}';
    }
}
