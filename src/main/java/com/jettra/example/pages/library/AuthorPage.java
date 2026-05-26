package com.jettra.example.pages.library;

import com.jettra.example.model.library.AuthorModel;
import com.jettra.example.services.library.AuthorService;
import io.jettra.wui.core.Page;
import io.jettra.wui.core.annotations.CrudView;

@CrudView(model = AuthorModel.class, controller = AuthorService.class, editable = true, report = true)
public class AuthorPage extends Page {
    public AuthorPage() {
        super("Gestión de Autores");
    }
}
