package com.jettra.example.pages.admin;

import com.jettra.example.dashboard.DashboardBasePage;
import io.jettra.wui.core.annotations.CrudView;
import io.jettra.wui.sync.JettraPageSincronized;
import io.jettra.wui.sync.SyncType;

@JettraPageSincronized(SyncType.ALL)
@CrudView(extendsClass = DashboardBasePage.class, model = com.jettra.example.model.admin.UsuarioModel.class, repository = com.jettra.example.repository.admin.UsuarioRepository.class,
       report = true,
       reportOrientation = "LANDSCAPE",
       reportTitle = "REPORTE DE USUARIOS",
       reportHeaderColor = "#007BFF")
public interface UsuarioPageDef {
}
