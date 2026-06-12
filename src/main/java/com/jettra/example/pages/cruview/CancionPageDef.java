/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jettra.example.pages.cruview;

import com.jettra.example.dashboard.DashboardBasePage;
import io.jettra.wui.core.annotations.CrudView;
import io.jettra.wui.sync.JettraPageSincronized;
import io.jettra.wui.sync.SyncType;

/**
 *
 * @author avbravo
 */
@JettraPageSincronized(SyncType.ALL)
@CrudView(extendsClass = DashboardBasePage.class, model=com.jettra.example.model.crudview.CancionModel.class,
repository = com.jettra.example.repository.crud.CancionRepository.class,
report = true,
reportOrientation = "LANDSCAPE",
reportTitle = "INFORME GLOBAL DE CANCIONES",
reportHeaderColor = "#007BFF"
)
public interface CancionPageDef {
    
}
