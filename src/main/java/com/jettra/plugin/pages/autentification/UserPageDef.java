package com.jettra.plugin.pages.autentification;

import com.jettra.example.dashboard.DashboardBasePage;
import com.jettra.plugin.model.autentification.UserModel;
import com.jettra.plugin.services.autentification.UserService;
import io.jettra.wui.core.annotations.CrudView;
import io.jettra.wui.sync.JettraPageSincronized;
import io.jettra.wui.sync.SyncType;

@JettraPageSincronized(SyncType.ALL)
@CrudView(extendsClass = DashboardBasePage.class, model = UserModel.class,
        controller = UserService.class, editable = true,
        report = true,
        reportOrientation = "LANDSCAPE",
        reportTitle = "REPORTE DE USUARIOS",
        reportHeaderColor = "#007BFF")
public interface UserPageDef {
}
