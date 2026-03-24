package com.jettra.example;

import com.jettra.server.JettraServer;
import com.jettra.server.config.JettraConfigProperty;
import com.jettra.server.config.ConfigInjector;
import com.jettra.example.pages.LoginPage;
import com.jettra.example.pages.DashboardPage;
import com.jettra.example.pages.PersonaPage;

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

        // Los eventos y el enrutamiento son manejados internamente por cada Page
        LoginPage loginPage = new LoginPage();
        server.addHandler("/", loginPage);
        server.addHandler("/login", loginPage);
        server.addHandler("/logout", loginPage);

        server.addHandler("/dashboard", new DashboardPage());
        server.addHandler("/persona", new PersonaPage());
        server.addHandler("/avatar", new com.jettra.example.pages.AvatarPage());
        server.addHandler("/alert", new com.jettra.example.pages.AlertPage());
        server.addHandler("/button", new com.jettra.example.pages.ButtonPage());
        server.addHandler("/checkbox", new com.jettra.example.pages.CheckBoxPage());
        server.addHandler("/clock", new com.jettra.example.pages.ClockPage());
        server.addHandler("/header", new com.jettra.example.pages.HeaderPage());
        server.addHandler("/icon", new com.jettra.example.pages.IconPage());
        server.addHandler("/image", new com.jettra.example.pages.ImagePage());
        server.addHandler("/label", new com.jettra.example.pages.LabelPage());
        server.addHandler("/link", new com.jettra.example.pages.LinkPage());
        server.addHandler("/paragraph", new com.jettra.example.pages.ParagraphPage());
        server.addHandler("/radiobutton", new com.jettra.example.pages.RadioButtonPage());
        server.addHandler("/selectone", new com.jettra.example.pages.SelectOnePage());
        server.addHandler("/table", new com.jettra.example.pages.TablePage());
        server.addHandler("/textbox", new com.jettra.example.pages.TextBoxPage());
        server.addHandler("/toggleswitch", new com.jettra.example.pages.ToggleSwitchPage());
        server.addHandler("/carousel", new com.jettra.example.pages.CarouselPage());
        server.addHandler("/modal", new com.jettra.example.pages.ModalPage());
        server.addHandler("/notification", new com.jettra.example.pages.NotificationPage());
        server.addHandler("/menubar", new com.jettra.example.pages.MenuBarPage());
        server.addHandler("/avatargroup", new com.jettra.example.pages.AvatarGroupPage());
        server.addHandler("/selectoneicon", new com.jettra.example.pages.SelectOneIconPage());
        server.addHandler("/grid", new com.jettra.example.pages.GridPage());
        server.addHandler("/sessiontimeout", new com.jettra.example.pages.SessionTimeoutDialogPage());
        server.addHandler("/loginadvanced", new com.jettra.example.pages.LoginAdvancedPage());
        server.addHandler("/div", new com.jettra.example.pages.DivPage());
        server.addHandler("/span", new com.jettra.example.pages.SpanPage());
        server.addHandler("/form", new com.jettra.example.pages.FormPage());
        server.addHandler("/menu", new com.jettra.example.pages.MenuPage());
        server.addHandler("/separator", new com.jettra.example.pages.SeparatorPage());

        server.start();
    }
}
