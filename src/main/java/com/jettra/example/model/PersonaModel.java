package com.jettra.example.model;

import io.jettra.wui.validations.NotNull;
import io.jettra.wui.validations.Size;

public class PersonaModel {
    private String id;
    
    @NotNull
    @Size(min = 3, max = 100)
    private String nombre;
    
    @NotNull
    @Size(min = 5, max = 200)
    private String direccion;

    public PersonaModel() {}

    public PersonaModel(String id, String nombre, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
}
