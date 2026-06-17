package com.jettra.plugin.pages.autentification;

import com.jettra.example.dashboard.DashboardBasePage;
import com.jettra.plugin.model.autentification.RoleModel;
import com.jettra.plugin.services.autentification.RoleService;
import io.jettra.wui.core.annotations.CrudView;
import io.jettra.wui.sync.JettraPageSincronized;
import io.jettra.wui.sync.SyncType;

@JettraPageSincronized(SyncType.ALL)
@CrudView(extendsClass = DashboardBasePage.class, model = RoleModel.class,
        controller = RoleService.class, editable = true,
        report = true,
        reportOrientation = "LANDSCAPE",
        reportTitle = "REPORTE DE ROLES",
        reportHeaderColor = "#007BFF")
public interface RolePageDef {
}
