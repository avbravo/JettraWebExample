package com.jettra.example;

import com.jettra.server.JettraServer;
import com.jettra.server.config.JettraConfigProperty;
import com.jettra.server.config.ConfigInjector;
import com.jettra.example.pages.*;
import com.jettra.example.dashboard.*;

public class WebExampleMain {

    @JettraConfigProperty(name = "app.title")
    private String appTitle;
    @JettraConfigProperty(name = "server.port")
    private String port;
    @JettraConfigProperty(name = "server.contextpath")
    private String contextpath;
   

    public void initUI() {
        ConfigInjector.inject(this);
        System.out.println("Iniciando aplicación Web: " + appTitle);
    }

    public static void main(String[] args) {
        WebExampleMain app = new WebExampleMain();
        app.initUI();

        // Configurar la ruta de redirección en ErrorPage, usando contextpath (y el puerto implícitamente por el host)
        io.jettra.wui.complex.ErrorPage.path = "http://localhost:" + app.port + app.contextpath;
        
        System.out.println("Levantando servidor de enrutamiento JettraServer empotrado...");
        JettraServer server = new JettraServer();
        server.setErrorPage("/error");

        // IMPORTANTE / DOCUMENTACIÓN:
        // Todas las páginas creadas deben registrarse obligatoriamente en este método
        // main
        // usando server.addHandler(). Esto permite que JettraServer reconozca las rutas
        // HTTP
        // y establezca el contexto correcto. Si se omite, se generarán errores de "404
        // Not Found"
        // o "No context found" al intentar cargar la vista o manejar peticiones de
        // Formularios.

        // Register pages as classes to ensure a fresh, isolated instance per request
        server.addHandler("/error", io.jettra.wui.complex.ErrorPage.class);
        server.addHandler("/", LoginPage.class);
        server.addHandler("/alert", AlertPage.class);
        server.addHandler("/avatar", AvatarPage.class);
        server.addHandler("/avatargroup", AvatarGroupPage.class);
        server.addHandler("/barcode", BarCodePage.class);
        server.addHandler("/board", BoardPage.class);
        server.addHandler("/button", ButtonPage.class);
        server.addHandler("/calendar", CalendarPage.class);
        server.addHandler("/card", CardPage.class);
        server.addHandler("/carousel", CarouselPage.class);
        server.addHandler("/checkbox", CheckBoxPage.class);
        server.addHandler("/checkboxgroup", CheckBoxGroupPage.class);
        server.addHandler("/clock", ClockPage.class);
        server.addHandler("/catcha", CatchaPage.class);
        server.addHandler("/creditcard", CreditCardPage.class);
        server.addHandler("/formgroup", FormGroupPage.class);
        server.addHandler("/dashboard", DashboardPage.class);
        server.addHandler("/header", HeaderPage.class);
        server.addHandler("/icon", IconPage.class);
        server.addHandler("/icons", IconsPage.class);
        server.addHandler("/draw", DrawPage.class);
        server.addHandler("/deporte", DeportePage.class);
        server.addHandler("/kanban", KanbanPage.class);
        server.addHandler("/image", ImagePage.class);
        server.addHandler("/layoutdisplay", LayoutDisplayPage.class);
        server.addHandler("/login", LoginPage.class);
        server.addHandler("/logout", LoginPage.class);
        server.addHandler("/loading", LoadingPage.class);
        server.addHandler("/datepicker", DatePickerPage.class);
        server.addHandler("/datatable", DataTablePage.class);
        server.addHandler("/label", LabelPage.class);
        server.addHandler("/link", LinkPage.class);
        server.addHandler("/organigram", OrganigramPage.class);
        server.addHandler("/paragraph", ParagraphPage.class);
        server.addHandler("/radiobutton", RadioButtonPage.class);
        server.addHandler("/selectone", SelectOnePage.class);
        server.addHandler("/selectmany", SelectManyPage.class);
        server.addHandler("/schedule", SchedulePage.class);
        server.addHandler("/textbox", TextBoxPage.class);
        server.addHandler("/time", TimePage.class);
        server.addHandler("/timeline", TimelinePage.class);
        server.addHandler("/toggleswitch", ToggleSwitchPage.class);
        server.addHandler("/modal", ModalPage.class);
        server.addHandler("/notification", NotificationPage.class);
        server.addHandler("/menubar", MenuBarPage.class);
        server.addHandler("/persona", PersonaPage.class);
        server.addHandler("/selectoneicon", SelectOneIconPage.class);
        server.addHandler("/grid", GridPage.class);
        server.addHandler("/sessiontimeout", SessionTimeoutDialogPage.class);
        server.addHandler("/loginadvanced", LoginAdvancedPage.class);
        server.addHandler("/div", DivPage.class);
        server.addHandler("/span", SpanPage.class);
        server.addHandler("/form", FormPage.class);
        server.addHandler("/menu", MenuPage.class);
        server.addHandler("/separator", SeparatorPage.class);
        server.addHandler("/tabview", TabViewPage.class);
        server.addHandler("/spinner", SpinnerPage.class);
        server.addHandler("/pais", PaisPage.class);
        server.addHandler("/webdesigner", WebDesignerPage.class);
        server.addHandler("/map", MapPage.class);
        server.addHandler("/panel", PanelPage.class);
        server.addHandler("/tree", TreePage.class);
        server.addHandler("/divide", DividePage.class);
        server.addHandler("/fileupload", FileUploadPage.class);
        server.addHandler("/folderselector", FolderSelectorPage.class);
        server.addHandler("/forms", FormsPage.class);
        server.addHandler("/progressbar", ProgressBarPage.class);
        server.addHandler("/textarea", TextAreaPage.class);
        server.addHandler("/navigation", NavigationPage.class);
        server.addHandler("/feedback", FeedbackPage.class);
        server.addHandler("/typography", TypographyPage.class);
        server.addHandler("/downloader", DownloaderPage.class);
        server.addHandler("/pdfviewer", PDFViewerPage.class);
        server.addHandler("/viewmedia", ViewMediaPage.class);
        server.addHandler("/chartsdoughnut", ChartsDoughnutPage.class);
        server.addHandler("/chartsbar", ChartsBarPage.class);
        server.addHandler("/chartspie", ChartsPiePage.class);
        server.addHandler("/chartsradar", ChartsRadarPage.class);
        server.addHandler("/chartsline", ChartsLinePage.class);
        server.addHandler("/schedulecontrol", ScheduleControlPage.class);
        server.addHandler("/qr", QRPage.class);
        server.addHandler("/otpvalidator", OTPValidatorPage.class);
        server.addHandler("/trafficlight", TrafficLightPage.class);
        server.start();
    }
}
