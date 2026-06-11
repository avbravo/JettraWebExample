package com.jettra.example.pages.library;

import com.jettra.example.dashboard.DashboardBasePage;
import com.jettra.example.model.library.PublisherModel;
import com.jettra.example.services.library.PublisherService;
import io.jettra.wui.complex.Center;
import io.jettra.wui.core.Page;
import io.jettra.wui.core.annotations.CrudView;

@CrudView(model = PublisherModel.class, controller = PublisherService.class, editable = true, report = true, public interface PublisherPageDef {
}
