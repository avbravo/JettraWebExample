package com.jettra.example.pages;

import com.jettra.example.dashboard.DashboardBasePage;
import io.jettra.wui.core.annotations.CrudView;
import io.jettra.wui.core.annotations.InjectProperties;
import io.jettra.wui.sync.JettraPageSincronized;
import io.jettra.wui.sync.SyncType;
import java.util.Properties;
import io.jettra.wui.complex.Center;

@JettraPageSincronized(SyncType.ALL)
@CrudView(model = "PlanetaModel")
public class PlanetaPage extends DashboardBasePage {

    @InjectProperties(name = "messages")
    private Properties msg;

    public PlanetaPage() {
        super("Mantenimiento de Planetas");
    }

    @Override
    protected void initCenter(Center center, String username) {
        // center is initialized but @CrudView will add its content automatically via JettraMVC.processCrudView
        // If we want to add extra content we can do it here.
    }
}
