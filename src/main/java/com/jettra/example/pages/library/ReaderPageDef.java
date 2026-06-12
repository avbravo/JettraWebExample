package com.jettra.example.pages.library;

import com.jettra.example.dashboard.DashboardBasePage;
import com.jettra.example.model.library.ReaderModel;
import com.jettra.example.services.library.ReaderService;
import io.jettra.wui.complex.Center;
import io.jettra.wui.core.annotations.CrudView;

@CrudView(extendsClass = DashboardBasePage.class, model = ReaderModel.class, controller = ReaderService.class, editable = true, report = true)
public interface ReaderPageDef {
}
