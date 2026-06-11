package com.jettra.example.pages.library;

import com.jettra.example.dashboard.DashboardBasePage;
import com.jettra.example.model.library.AuthorModel;
import com.jettra.example.repository.library.AuthorRepository;
import com.jettra.example.services.library.AuthorService;
import io.jettra.wui.complex.Center;
import io.jettra.wui.core.annotations.CrudView;
import io.jettra.wui.core.annotations.InjectProperties;
import io.jettra.wui.sync.JettraPageSincronized;
import io.jettra.wui.sync.SyncType;
import java.util.Properties;

@JettraPageSincronized(SyncType.ALL)
//@CrudView(model = AuthorModel.class, 
//        repository = AuthorRepository.class, editable = true,
//        report = true,
//        reportOrientation = "LANDSCAPE",
//        reportTitle = "INFORME GLOBAL DE PLANETAS",
//        reportHeaderColor = "#007BFF")
@CrudView(model = AuthorModel.class, 
        controller = AuthorService.class, editable = true,
        report = true,
        reportOrientation = "LANDSCAPE",
        reportTitle = "INFORME GLOBAL DE PLANETAS",
        reportHeaderColor = "#007BFF", expublic interface AuthorPageDef {

    default void afterInitCenter(io.jettra.wui.complex.Center center, String username) {
        io.jettra.wui.components.Console console = new io.jettra.wui.components.Console("miConsola");
        center.add(console);
    }
}
