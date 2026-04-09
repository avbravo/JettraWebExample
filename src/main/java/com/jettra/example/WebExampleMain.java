package com.jettra.example;

import com.jettra.server.JettraServer;
import com.jettra.server.config.JettraConfigProperty;
import com.jettra.server.config.ConfigInjector;

public class WebExampleMain {

    @JettraConfigProperty(name = "app.title")
    private String appTitle;

    public void initUI() {
        ConfigInjector.inject(this);
        System.out.println("Iniciando aplicación Web: " + appTitle);
    }

    public static void main(String[] args) {
        WebExampleMain app = new WebExampleMain();
        app.initUI();

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
        server.addHandler("/", com.jettra.example.pages.LoginPage.class);
        server.addHandler("/alert", com.jettra.example.pages.AlertPage.class);
        server.addHandler("/avatar", com.jettra.example.pages.AvatarPage.class);
        server.addHandler("/avatargroup", com.jettra.example.pages.AvatarGroupPage.class);
        server.addHandler("/spinner", com.jettra.example.pages.SpinnerPage.class);
        server.addHandler("/icons", com.jettra.example.pages.IconsPage.class);
        server.addHandler("/kanban", com.jettra.example.pages.KanbanPage.class);

        server.addHandler("/button", com.jettra.example.pages.ButtonPage.class);
        server.addHandler("/calendar", com.jettra.example.pages.CalendarPage.class);
        server.addHandler("/card", com.jettra.example.pages.CardPage.class);
        server.addHandler("/checkbox", com.jettra.example.pages.CheckBoxPage.class);
        server.addHandler("/checkboxgroup", com.jettra.example.pages.CheckBoxGroupPage.class);
        server.addHandler("/clock", com.jettra.example.pages.ClockPage.class);
        server.addHandler("/dashboard", com.jettra.example.dashboard.DashboardPage.class);
        server.addHandler("/header", com.jettra.example.pages.HeaderPage.class);
        server.addHandler("/icon", com.jettra.example.pages.IconPage.class);
        server.addHandler("/image", com.jettra.example.pages.ImagePage.class);
        server.addHandler("/layoutdisplay", com.jettra.example.dashboard.LayoutDisplayPage.class);
        server.addHandler("/login", com.jettra.example.pages.LoginPage.class);
        server.addHandler("/logout", com.jettra.example.pages.LoginPage.class);
        server.addHandler("/loading", com.jettra.example.pages.LoadingPage.class);
        server.addHandler("/persona", com.jettra.example.pages.PersonaPage.class);

        server.addHandler("/datepicker", com.jettra.example.pages.DatePickerPage.class);
        server.addHandler("/label", com.jettra.example.pages.LabelPage.class);
        server.addHandler("/link", com.jettra.example.pages.LinkPage.class);
        server.addHandler("/organigram", com.jettra.example.pages.OrganigramPage.class);
        server.addHandler("/paragraph", com.jettra.example.pages.ParagraphPage.class);
        server.addHandler("/radiobutton", com.jettra.example.pages.RadioButtonPage.class);
        server.addHandler("/selectone", com.jettra.example.pages.SelectOnePage.class);
        server.addHandler("/selectmany", com.jettra.example.pages.SelectManyPage.class);
        server.addHandler("/schedule", com.jettra.example.pages.SchedulePage.class);
        server.addHandler("/table", com.jettra.example.pages.TablePage.class);
        server.addHandler("/textbox", com.jettra.example.pages.TextBoxPage.class);
        server.addHandler("/time", com.jettra.example.pages.TimePage.class);
        server.addHandler("/timeline", com.jettra.example.pages.TimelinePage.class);

        server.addHandler("/toggleswitch", com.jettra.example.pages.ToggleSwitchPage.class);
        server.addHandler("/carousel", com.jettra.example.pages.CarouselPage.class);
        server.addHandler("/modal", com.jettra.example.pages.ModalPage.class);
        server.addHandler("/notification", com.jettra.example.pages.NotificationPage.class);
        server.addHandler("/menubar", com.jettra.example.pages.MenuBarPage.class);

        server.addHandler("/selectoneicon", com.jettra.example.pages.SelectOneIconPage.class);
        server.addHandler("/grid", com.jettra.example.pages.GridPage.class);
        server.addHandler("/sessiontimeout", com.jettra.example.pages.SessionTimeoutDialogPage.class);
        server.addHandler("/loginadvanced", com.jettra.example.pages.LoginAdvancedPage.class);
        server.addHandler("/div", com.jettra.example.pages.DivPage.class);
        server.addHandler("/span", com.jettra.example.pages.SpanPage.class);
        server.addHandler("/form", com.jettra.example.pages.FormPage.class);
        server.addHandler("/menu", com.jettra.example.pages.MenuPage.class);
        server.addHandler("/separator", com.jettra.example.pages.SeparatorPage.class);
        server.addHandler("/tabview", com.jettra.example.pages.TabViewPage.class);
        server.addHandler("/spinner", com.jettra.example.pages.SpinnerPage.class);
        server.addHandler("/pais", com.jettra.example.pages.PaisPage.class);
        server.addHandler("/webdesigner", com.jettra.example.pages.WebDesignerPage.class);
        server.addHandler("/map", com.jettra.example.pages.MapPage.class);
        server.addHandler("/panel", com.jettra.example.pages.PanelPage.class);
        server.addHandler("/tree", com.jettra.example.pages.TreePage.class);
        server.addHandler("/divide", com.jettra.example.pages.DividePage.class);
        server.addHandler("/fileupload", com.jettra.example.pages.FileUploadPage.class);
        server.addHandler("/folderselector", com.jettra.example.pages.FolderSelectorPage.class);
        server.addHandler("/forms", com.jettra.example.pages.FormsPage.class);
        server.addHandler("/progressbar", com.jettra.example.pages.ProgressBarPage.class);
        server.addHandler("/textarea", com.jettra.example.pages.TextAreaPage.class);
        server.addHandler("/navigation", com.jettra.example.pages.NavigationPage.class);
        server.addHandler("/feedback", com.jettra.example.pages.FeedbackPage.class);
        server.addHandler("/icon", com.jettra.example.pages.IconPage.class);
        server.addHandler("/typography", com.jettra.example.pages.TypographyPage.class);
        server.addHandler("/downloader", com.jettra.example.pages.DownloaderPage.class);
        server.addHandler("/pdfviewer", com.jettra.example.pages.PDFViewerPage.class);
        server.addHandler("/viewmedia", com.jettra.example.pages.ViewMediaPage.class);
        server.addHandler("/chartsdoughnut", com.jettra.example.pages.ChartsDoughnutPage.class);
        server.addHandler("/chartsbar", com.jettra.example.pages.ChartsBarPage.class);
        server.addHandler("/chartspie", com.jettra.example.pages.ChartsPiePage.class);
        server.addHandler("/chartsradar", com.jettra.example.pages.ChartsRadarPage.class);
        server.addHandler("/chartsline", com.jettra.example.pages.ChartsLinePage.class);
        server.addHandler("/schedulecontrol", com.jettra.example.pages.ScheduleControlPage.class);
        server.addHandler("/qr", com.jettra.example.pages.QRPage.class);
        server.addHandler("/barcode", com.jettra.example.pages.BarCodePage.class);
        server.addHandler("/otpvalidator", com.jettra.example.pages.OTPValidatorPage.class);
        server.addHandler("/catcha", com.jettra.example.pages.CatchaPage.class);
        server.addHandler("/creditcard", com.jettra.example.pages.CreditCardPage.class);

        server.start();
    }
}
