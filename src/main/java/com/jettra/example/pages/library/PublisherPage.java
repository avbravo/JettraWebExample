package com.jettra.example.pages.library;

import com.jettra.example.dashboard.DashboardBasePage;
import com.jettra.example.model.library.PublisherModel;
import com.jettra.example.services.library.PublisherService;
import io.jettra.wui.complex.Center;
import io.jettra.wui.core.Page;
import io.jettra.wui.core.annotations.CrudView;

@CrudView(model = PublisherModel.class, controller = PublisherService.class, editable = true, report = true)
public class PublisherPage extends DashboardBasePage {
    public PublisherPage() {
        super("Gestión de Editoriales");
    }

    @Override
    protected void initCenter(Center center, String username) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
