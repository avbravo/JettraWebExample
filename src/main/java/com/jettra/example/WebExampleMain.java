package com.jettra.example;

import com.jettra.server.JettraServer;
import com.jettra.server.config.JettraConfigProperty;
import com.jettra.server.config.ConfigInjector;
import com.jettra.example.pages.LoginPage;
import com.jettra.example.pages.DashboardPage;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

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

        // 1. Ruta / : LoginPage
        server.addHandler("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if ("GET".equals(exchange.getRequestMethod())) {
                    LoginPage loginPage = new LoginPage();
                    loginPage.initUI();
                    String html = loginPage.render();
                    byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
                    exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                    exchange.sendResponseHeaders(200, bytes.length);
                    exchange.getResponseBody().write(bytes);
                } else {
                    exchange.sendResponseHeaders(405, -1);
                }
                exchange.getResponseBody().close();
            }
        });

        // 2. Ruta /login : validación POST
        server.addHandler("/login", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if ("POST".equals(exchange.getRequestMethod())) {
                    InputStream is = exchange.getRequestBody();
                    String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                    Map<String, String> params = parseQuery(body);

                    String user = params.get("username");
                    String pass = params.get("password");

                    if ("admin".equals(user) && "admin".equals(pass)) {
                        exchange.getResponseHeaders().set("Location", "/dashboard");
                        exchange.sendResponseHeaders(302, -1);
                    } else {
                        // Vuelve a /
                        exchange.getResponseHeaders().set("Location", "/");
                        exchange.sendResponseHeaders(302, -1);
                    }
                } else {
                    exchange.sendResponseHeaders(405, -1);
                }
                exchange.getResponseBody().close();
            }
        });

        // 3. Ruta /dashboard : DashboardPage
        server.addHandler("/dashboard", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                DashboardPage dashboardPage = new DashboardPage();
                dashboardPage.initUI();
                String html = dashboardPage.render();
                byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                exchange.sendResponseHeaders(200, bytes.length);
                exchange.getResponseBody().write(bytes);
                exchange.getResponseBody().close();
            }
        });

        server.start();
    }

    private static Map<String, String> parseQuery(String query) {
        Map<String, String> query_pairs = new HashMap<>();
        if (query == null || query.isBlank()) return query_pairs;
        String[] pairs = query.split("&");
        try {
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                if(idx > 0) {
                   query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
                }
            }
        } catch(Exception e) {}
        return query_pairs;
    }
}
