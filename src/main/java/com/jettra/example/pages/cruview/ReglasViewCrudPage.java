package com.jettra.example.pages.cruview;

import com.jettra.example.dashboard.DashboardBasePage;
import io.jettra.wui.core.annotations.CrudView;
import io.jettra.wui.core.annotations.InjectProperties;
import io.jettra.wui.sync.JettraPageSincronized;
import io.jettra.wui.sync.SyncType;
import java.util.Properties;
import io.jettra.wui.complex.Center;

@JettraPageSincronized(SyncType.ALL)
@CrudView(model = com.jettra.example.model.ReglasModel.class, 
          repository = com.jettra.example.repository.ReglasRepository.class,
          report = true,
          reportOrientation = "PORTRAIT",
          reportTitle = "REPORTE DE REGLAS Y COMPUTOS",
          reportHeaderColor = "#28a745")
public class ReglasViewCrudPage extends DashboardBasePage {

    @InjectProperties(name = "messages")
    private Properties msg;

    public ReglasViewCrudPage() {
        super("CRUD con Reglas y Cómputos (@CrudView)");
    }

    @Override
    protected void initCenter(Center center, String username) {
        // CrudView is added automatically
    }
}
