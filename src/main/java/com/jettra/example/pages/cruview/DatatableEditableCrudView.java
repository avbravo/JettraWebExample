package com.jettra.example.pages.cruview;

import com.jettra.example.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.core.annotations.CrudView;
import io.jettra.wui.core.annotations.InjectProperties;
import io.jettra.wui.sync.JettraPageSincronized;
import io.jettra.wui.sync.SyncType;
import java.util.Properties;

/**
 * Implementation of Editable Datatable using @CrudView annotation.
 */
@JettraPageSincronized(SyncType.ALL)
@CrudView(model = com.jettra.example.model.ReglasModel.class, 
          repository = com.jettra.example.repository.ReglasRepository.class,
          editable = true,
          report = true,
          reportTitle = "GESTIÓN DE REGLAS (DATATABLE EDITABLE)",
          reportHeaderColor = "#da3633")
public class DatatableEditableCrudView extends DashboardBasePage {

    @InjectProperties(name = "messages")
    private Properties msg;

    public DatatableEditableCrudView() {
        super("Datatable Editable con @CrudView");
    }

    @Override
    protected void initCenter(Center center, String username) {
        // The @CrudView annotation will cause JettraMVC to inject the CrudView component automatically
    }
}
