package com.jettra.example.dashboard;

import io.jettra.wui.complex.Left;
import io.jettra.wui.core.JettraDashboardPage;

/**
 * Simplified DashboardBasePage leveraging JettraWUI integration.
 * Most logic was moved to JettraDashboardPage in io.jettra.wui.core.
 */
public abstract class DashboardBasePage extends JettraDashboardPage {

    public DashboardBasePage(String title) {
        super(title);
    }

    @Override
    protected void setupLeft(Left left, String username) {
        initMenuBuilder();
        
        String compIcon = "<svg width='14' height='14' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round' style='opacity:0.7;'><path d='M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z'></path></svg>";
        
        String lang = com.jettra.server.config.JettraConfig.getProperty("app.language");
        boolean es = "es".equals(lang != null ? lang.trim() : "en");
        
        // --- Navigation ---
        addCategory(es ? "Navegación" : "Navigation", new String[]{}, ""); // Empty to hold custom elements
        appendMenuItem(es ? "Panel Principal" : "Main Dashboard", "/dashboard", "<svg width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'><polyline points='4 17 10 11 4 5'></polyline><line x1='12' y1='19' x2='20' y2='19'></line></svg>");

        // --- Administration ---
        if ("admin".equals(username) || "demo".equals(username) || "avbravo".equals(username)) {
            addCategory(es ? "Administración" : "Administration", new String[]{}, "");
            appendMenuItem("Persona CRUD", "/persona", "<svg width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'><path d='M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2'></path><circle cx='9' cy='7' r='4'></circle></svg>");
            appendMenuItem("Pais CRUD (MVC)", "/pais", "<svg width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'><circle cx='12' cy='12' r='10'></circle><line x1='2' y1='12' x2='22' y2='12'></line></svg>");
            appendMenuItem("Web Designer", "/webdesigner", "<svg width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'><rect x='3' y='3' width='18' height='18' rx='2' ry='2'></rect><line x1='3' y1='9' x2='21' y2='9'></line><line x1='9' y1='21' x2='9' y2='9'></line></svg>");
            appendMenuItem("Kanban Board", "/kanban", "<svg width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'><rect x='3' y='3' width='18' height='18' rx='2' ry='2'></rect><line x1='3' y1='9' x2='21' y2='9'></line><line x1='9' y1='21' x2='9' y2='9'></line></svg>");
        }

        // --- Categories and Components ---
        addCategory(es ? "Tipografía" : "Typography", new String[]{"Header", "Paragraph", "Span", "Label", "Separator", "Icon", "Typography"}, compIcon);
        addCategory(es ? "Formularios" : "Forms", new String[]{"Button", "Catcha", "CheckBox", "CheckBoxGroup", "CreditCard", "Form", "Forms", "OTPValidator", "RadioButton", "RadioGroupButton", "ScheduleControl", "SelectOne", "SelectMany", "SelectOneIcon", "Spinner", "TextBox", "TextArea", "ToggleSwitch"}, compIcon);
        addCategory(es ? "Fecha y Tiempo" : "Date", new String[]{"Calendar", "DatePicker", "Organigram", "Schedule", "Time", "Timeline"}, compIcon);
        addCategory(es ? "Navegación" : "Navigation", new String[]{"Link", "Menu", "MenuBar", "Navigation"}, compIcon);
        addCategory(es ? "Retroalimentación" : "Feedback", new String[]{"Alert", "Modal", "Notification", "SessionTimeout", "Feedback"}, compIcon);
        addCategory(es ? "Diseño de Página" : "Layout", new String[]{"Avatar", "AvatarGroup", "Board", "Card", "Carousel", "Clock", "Div", "Divide", "FileUpload", "FolderSelector", "Grid", "Icon", "Image", "LayoutDisplay", "Loading", "LoginAdvanced", "Map", "Panel", "ProgressBar", "Spinner", "Table", "TabView", "Tree"}, compIcon);
        addCategory(es ? "Multimedia" : "Media", new String[]{"BarCode", "Downloader", "PDFViewer", "QR", "ViewMedia"}, compIcon);
        addCategory("Charts", new String[]{"ChartsBar", "ChartsDoughnut", "ChartsLine", "ChartsPie", "ChartsRadar"}, compIcon);

        menuHtmlBuilder.append("<div style='margin-top:20px;'></div>");
        appendMenuItem(es ? "Cerrar Sesión" : "Logout", "/logout", "<svg width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'><path d='M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4'></path><polyline points='16 17 21 12 16 7'></polyline></svg>");

        finishMenuBuilder(left);
    }
}
