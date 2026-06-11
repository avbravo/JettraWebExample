package com.jettra.example.pages.admin;

import com.jettra.example.dashboard.DashboardBasePage;
import io.jettra.wui.core.annotations.CrudView;
import io.jettra.wui.core.annotations.InjectProperties;
import io.jettra.wui.sync.JettraPageSincronized;
import io.jettra.wui.sync.SyncType;
import java.util.Properties;
import io.jettra.wui.complex.Center;

@JettraPageSincronized(SyncType.ALL)
@CrudView(model = com.jettra.example.model.admin.PermisoModel.class, repository = com.jettra.example.repository.admin.PermisoRepository.class,
       report = true,
       reportOrientation = "LANDSCAPE",
       reportTitle = "REPORTE DE PERMISOS",
       reportHeaderColor = "#007BFF", public interface PermisoPageDef {
}
