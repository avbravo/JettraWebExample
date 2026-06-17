package com.jettra.plugin.pages.autentification;

import com.jettra.example.dashboard.DashboardBasePage;
import com.jettra.plugin.model.autentification.DepartmentModel;
import com.jettra.plugin.services.autentification.DepartmentService;
import io.jettra.wui.core.annotations.CrudView;
import io.jettra.wui.sync.JettraPageSincronized;
import io.jettra.wui.sync.SyncType;

@JettraPageSincronized(SyncType.ALL)
@CrudView(extendsClass = DashboardBasePage.class, model = DepartmentModel.class,
        controller = DepartmentService.class, editable = true,
        report = true,
        reportOrientation = "LANDSCAPE",
        reportTitle = "REPORTE DE DEPARTAMENTOS",
        reportHeaderColor = "#007BFF")
public interface DepartmentPageDef {
}
