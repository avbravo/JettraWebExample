
package com.jettra.example.model;

import io.jettra.wui.core.annotations.JettraViewModel;
import io.jettra.wui.validations.NotNull;
import io.jettra.wui.validations.Size;

@JettraViewModel
public class SubGrupoModel {
    @NotNull
    @Size(min = 2, max = 5)
    private String id;
    
    @NotNull
    @Size(min = 3, max = 100)
    private String name;
    
    @NotNull
    private GrupoModel grupoModel;
    
 

    public SubGrupoModel() {}

    public SubGrupoModel(String id, String name, GrupoModel grupoModel) {
        this.id = id;
        this.name = name;
        this.grupoModel = grupoModel;
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

    

   
}
