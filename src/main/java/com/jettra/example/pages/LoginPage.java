package com.jettra.example.pages;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import io.jettra.wui.components.Login;
import io.jettra.wui.core.Page;
import io.jettra.wui.core.annotations.Init;

public class LoginPage extends Page implements HttpHandler {

    private Login loginForm;

    public LoginPage() {
        super("Login");
    }

    @Init
    public void initUI() {
        loginForm = new Login("/login");

        // Styling the login form generically with 3D futuristic aesthetics
        loginForm.setProperty("style", "margin: 10% auto; padding: 30px; width: 350px; " +
            "background: rgba(20, 30, 50, 0.85); box-shadow: 0 0 20px cyan; " +
            "border-radius: 15px; backdrop-filter: blur(10px); " +
            "color: white; font-family: 'Courier New', monospace; text-align: center;");
        
        loginForm.getUsername().setProperty("style", "width: 100%; padding: 10px; margin-bottom: 15px; " +
            "background: #0f172a; border: 1px solid cyan; color: white; border-radius: 5px;");
            
        loginForm.getPassword().setProperty("style", "width: 100%; padding: 10px; margin-bottom: 15px; " +
            "background: #0f172a; border: 1px solid cyan; color: white; border-radius: 5px;");
            
        loginForm.getLoginButton().setProperty("style", "width: 100%; padding: 12px; margin-bottom: 10px; " +
            "background: cyan; color: #0f172a; border: none; border-radius: 5px; font-weight: bold; cursor: pointer; " +
            "box-shadow: 0 0 10px cyan;");
            
        loginForm.getForgotPasswordButton().setProperty("style", "background: transparent; border: none; " +
            "color: cyan; cursor: pointer; text-decoration: underline; font-size: 0.9em;");

        add(loginForm);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if ("/logout".equals(path)) {
            exchange.getResponseHeaders().set("Set-Cookie", "username=; Path=/; Max-Age=0");
            exchange.getResponseHeaders().set("Location", "/");
            exchange.sendResponseHeaders(302, -1);
            exchange.getResponseBody().close();
            return;
        }

        if ("POST".equals(exchange.getRequestMethod()) && "/login".equals(path)) {
            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Map<String, String> params = parseQuery(body);

            String user = params.get("username");
            String pass = params.get("password");

            if ("admin".equals(user) && "admin".equals(pass)) {
                exchange.getResponseHeaders().set("Set-Cookie", "username=" + user + "; Path=/");
                exchange.getResponseHeaders().set("Location", "/dashboard");
                exchange.sendResponseHeaders(302, -1);
            } else {
                              

                 if ("demo".equals(user) && "demo".equals(pass)) {
                     exchange.getResponseHeaders().set("Set-Cookie", "username=" + user + "; Path=/");
                     exchange.getResponseHeaders().set("Location", "/dashboard");
                      exchange.sendResponseHeaders(302, -1);
                 }else{
                 exchange.getResponseHeaders().set("Location", "/?error=invalid_credentials");
                exchange.sendResponseHeaders(302, -1);
            }
               
               
               
            }
        } else if ("GET".equals(exchange.getRequestMethod())) {
            this.children.clear(); // Reset before re-init
            exchange.getResponseHeaders().set("Set-Cookie", "username=; Path=/; Max-Age=0");
            initUI();
            
            String query = exchange.getRequestURI().getQuery();
            if (query != null && query.contains("error=invalid_credentials")) {
                io.jettra.wui.components.Notification notif = new io.jettra.wui.components.Notification();
                notif.setType("error");
                notif.showMessage("Error: Username y/o password no válidos");
                this.add(notif);
            }
            
            String html = this.render();
            byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, bytes.length);
            exchange.getResponseBody().write(bytes);
        } else {
            exchange.sendResponseHeaders(405, -1);
        }
        exchange.getResponseBody().close();
    }

    private Map<String, String> parseQuery(String query) {
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
