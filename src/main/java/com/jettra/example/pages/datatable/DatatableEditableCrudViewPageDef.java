package com.jettra.example.pages.datatable;

import com.jettra.example.dashboard.DashboardBasePage;
import io.jettra.wui.core.annotations.CrudView;
import io.jettra.wui.core.annotations.InjectProperties;
import io.jettra.wui.sync.JettraPageSincronized;
import io.jettra.wui.sync.SyncType;
import java.util.Properties;
import io.jettra.wui.complex.Center;

@JettraPageSincronized(SyncType.ALL)
@CrudView(extendsClass = DashboardBasePage.class, model = com.jettra.example.model.ArticuloModel.class, 
          repository = com.jettra.example.repository.ArticuloRepository.class,
          editable = true,
          report = true,
          reportOrientation = "PORTRAIT",
          reportTitle = "REPORTE DATATABLE EDITABLE @CrudView",
          reportHeaderColor = "#28a745")
public interface DatatableEditableCrudViewPageDef {
}
