package com.jettra.plugin.example.cruview.pages;

import com.jettra.main.dashboard.DashboardBasePage;
import io.jettra.wui.core.annotations.CrudView;
import com.jettra.core.inject.annotation.InjectProperties;
import io.jettra.wui.sync.JettraPageSincronized;
import io.jettra.wui.sync.SyncType;
import java.util.Properties;
import io.jettra.wui.complex.Center;

@JettraPageSincronized(SyncType.ALL)
@CrudView(extendsClass = DashboardBasePage.class, model = com.jettra.plugin.example.rules.model.ReglasModel.class, 
          repository = com.jettra.plugin.example.crud.repository.ReglasRepository.class,
          report = true,
          reportOrientation = "PORTRAIT",
          reportTitle = "REPORTE DE REGLAS Y COMPUTOS",
          reportHeaderColor = "#28a745")
public interface ReglasViewCrudPageDef {
}
