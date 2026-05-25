package com.jettra.example.pages.admin;

import com.jettra.example.dashboard.DashboardBasePage;
import io.jettra.wui.core.annotations.CrudView;
import io.jettra.wui.core.annotations.InjectProperties;
import io.jettra.wui.sync.JettraPageSincronized;
import io.jettra.wui.sync.SyncType;
import java.util.Properties;
import io.jettra.wui.complex.Center;

@JettraPageSincronized(SyncType.ALL)
@CrudView(model = com.jettra.example.model.admin.RolModel.class, repository = com.jettra.example.repository.admin.RolRepository.class,
       report = true,
       reportOrientation = "LANDSCAPE",
       reportTitle = "REPORTE DE ROLES",
       reportHeaderColor = "#007BFF")
public class RolPage extends DashboardBasePage {
    @InjectProperties(name = "messages")
    private Properties msg;

    public RolPage() {
        super("Mantenimiento de Roles");
    }

    @Override
    protected void initCenter(Center center, String username) {
    }
}
