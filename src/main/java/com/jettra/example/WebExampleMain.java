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

        // Register pages as classes to ensure a fresh, isolated instance per request
        server.addHandler("/", com.jettra.example.pages.LoginPage.class);
        server.addHandler("/login", com.jettra.example.pages.LoginPage.class);
        server.addHandler("/logout", com.jettra.example.pages.LoginPage.class);

        server.addHandler("/dashboard", com.jettra.example.pages.DashboardPage.class);
        server.addHandler("/persona", com.jettra.example.pages.PersonaPage.class);
        server.addHandler("/avatar", com.jettra.example.pages.AvatarPage.class);
        server.addHandler("/alert", com.jettra.example.pages.AlertPage.class);
        server.addHandler("/button", com.jettra.example.pages.ButtonPage.class);
        server.addHandler("/checkbox", com.jettra.example.pages.CheckBoxPage.class);
        server.addHandler("/clock", com.jettra.example.pages.ClockPage.class);
        server.addHandler("/header", com.jettra.example.pages.HeaderPage.class);
        server.addHandler("/icon", com.jettra.example.pages.IconPage.class);
        server.addHandler("/image", com.jettra.example.pages.ImagePage.class);
        server.addHandler("/label", com.jettra.example.pages.LabelPage.class);
        server.addHandler("/link", com.jettra.example.pages.LinkPage.class);
        server.addHandler("/paragraph", com.jettra.example.pages.ParagraphPage.class);
        server.addHandler("/radiobutton", com.jettra.example.pages.RadioButtonPage.class);
        server.addHandler("/selectone", com.jettra.example.pages.SelectOnePage.class);
        server.addHandler("/table", com.jettra.example.pages.TablePage.class);
        server.addHandler("/textbox", com.jettra.example.pages.TextBoxPage.class);
        server.addHandler("/toggleswitch", com.jettra.example.pages.ToggleSwitchPage.class);
        server.addHandler("/carousel", com.jettra.example.pages.CarouselPage.class);
        server.addHandler("/modal", com.jettra.example.pages.ModalPage.class);
        server.addHandler("/notification", com.jettra.example.pages.NotificationPage.class);
        server.addHandler("/menubar", com.jettra.example.pages.MenuBarPage.class);
        server.addHandler("/avatargroup", com.jettra.example.pages.AvatarGroupPage.class);
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

        server.start();
    }
}
