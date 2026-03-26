package com.jettra.example.model;

import io.jettra.wui.core.annotations.JettraViewModel;
import io.jettra.wui.validations.NotNull;
import io.jettra.wui.validations.Size;

@JettraViewModel
public class PaisModel {
    @NotNull
    @Size(min = 2, max = 5)
    private String code;
    
    @NotNull
    @Size(min = 3, max = 100)
    private String name;

    public PaisModel() {}

    public PaisModel(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
