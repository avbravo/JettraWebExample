package com.jettra.plugin.pages.autentification;

import com.jettra.example.dashboard.DashboardBasePage;
import com.jettra.plugin.model.autentification.PermissionModel;
import com.jettra.plugin.services.autentification.PermissionService;
import io.jettra.wui.core.annotations.CrudView;
import io.jettra.wui.sync.JettraPageSincronized;
import io.jettra.wui.sync.SyncType;

@JettraPageSincronized(SyncType.ALL)
@CrudView(extendsClass = DashboardBasePage.class, model = PermissionModel.class,
        controller = PermissionService.class, editable = true,
        report = true,
        reportOrientation = "LANDSCAPE",
        reportTitle = "REPORTE DE PERMISOS",
        reportHeaderColor = "#007BFF")
public interface PermissionPageDef {
}
