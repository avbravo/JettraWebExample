package com.jettra.example.pages.cruview;

import com.jettra.example.dashboard.DashboardBasePage;
import io.jettra.wui.core.annotations.CrudView;
import io.jettra.wui.core.annotations.InjectProperties;
import io.jettra.wui.sync.JettraPageSincronized;
import io.jettra.wui.sync.SyncType;
import java.util.Properties;
import io.jettra.wui.complex.Center;

@JettraPageSincronized(SyncType.ALL)
@CrudView(model = com.jettra.example.model.PlanetaModel.class, repository = com.jettra.example.repository.PlanetaRepository.class,
       report = true,
       reportOrientation = "LANDSCAPE",
       reportTitle = "INFORME GLOBAL DE PLANETAS",
       reportHeaderColor = "#007BFF", public interface PlanetaPageDef {
}
