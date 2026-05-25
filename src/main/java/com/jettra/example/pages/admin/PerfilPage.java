package com.jettra.example.pages.admin;

import com.jettra.example.dashboard.DashboardBasePage;
import io.jettra.wui.core.annotations.CrudView;
import io.jettra.wui.core.annotations.InjectProperties;
import io.jettra.wui.sync.JettraPageSincronized;
import io.jettra.wui.sync.SyncType;
import java.util.Properties;
import io.jettra.wui.complex.Center;

@JettraPageSincronized(SyncType.ALL)
@CrudView(model = com.jettra.example.model.admin.PerfilModel.class, repository = com.jettra.example.repository.admin.PerfilRepository.class,
       report = true,
       reportOrientation = "LANDSCAPE",
       reportTitle = "REPORTE DE PERFILES",
       reportHeaderColor = "#007BFF")
public class PerfilPage extends DashboardBasePage {
    @InjectProperties(name = "messages")
    private Properties msg;

    public PerfilPage() {
        super("Mantenimiento de Perfiles");
    }

    @Override
    protected void initCenter(Center center, String username) {
    }
}
