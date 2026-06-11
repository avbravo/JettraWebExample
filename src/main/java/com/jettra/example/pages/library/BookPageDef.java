package com.jettra.example.pages.library;

import com.jettra.example.dashboard.DashboardBasePage;
import com.jettra.example.model.library.BookModel;
import com.jettra.example.services.library.BookService;
import io.jettra.wui.complex.Center;
import io.jettra.wui.core.Page;
import io.jettra.wui.core.annotations.CrudView;

@CrudView(model = BookModel.class, controller = BookService.class, editable = true, report = true, public interface BookPageDef {
}
