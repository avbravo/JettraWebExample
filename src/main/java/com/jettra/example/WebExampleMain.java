package com.jettra.example;

import com.jettra.server.JettraServer;
import com.jettra.server.config.JettraConfigProperty;
import com.jettra.server.config.ConfigInjector;
import com.jettra.example.pages.LoginPage;
import com.jettra.example.pages.DashboardPage;

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

        server.start();
    }
}
