package com.jettra.example.model.library;

import com.jettra.example.entity.library.Author;
import io.jettra.wui.core.annotations.ModelToRecordConversor;
import io.jettra.wui.core.annotations.PropertiesInRecord;
import io.jettra.wui.core.annotations.JettraViewModel;

@JettraViewModel
@ModelToRecordConversor(goal = Author.class)
public class AuthorModel {

    @PropertiesInRecord
    private String id;

    @PropertiesInRecord
    private String name;

    @PropertiesInRecord
    private String country;

    public AuthorModel() {
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
