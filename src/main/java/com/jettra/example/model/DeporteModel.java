/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jettra.example.model;

import io.jettra.wui.core.annotations.JettraViewModel;
import io.jettra.wui.validations.NotNull;
import io.jettra.wui.validations.Size;

/**
 *
 * @author avbravo
 */
@JettraViewModel
public class DeporteModel {
     @NotNull
    @Size(min = 2, max = 5)
    private String code;
    
    @NotNull
    @Size(min = 3, max = 100)
    private String deporte;

    public DeporteModel() {
    }

    public DeporteModel(String code, String deporte) {
        this.code = code;
        this.deporte = deporte;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDeporte() {
        return deporte;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }
    
    
    
    
    
}
