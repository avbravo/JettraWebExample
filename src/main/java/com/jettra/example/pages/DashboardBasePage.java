package com.jettra.example.pages;

import com.jettra.server.JettraServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.jettra.wui.complex.Center;
import io.jettra.wui.complex.Dashboard;
import io.jettra.wui.complex.Footer;
import io.jettra.wui.complex.Left;
import io.jettra.wui.complex.Top;
import io.jettra.wui.core.Page;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class DashboardBasePage extends Page implements HttpHandler {

    protected Dashboard dashboard;
    protected Top top;
    protected Left left;
    protected Footer footer;
    protected Center center;

    public DashboardBasePage(String title) {
        super(title);
    }

    public void initLayout(String username) {
        dashboard = new Dashboard();

        // 1. Setup Top
        top = new Top();
        setupTop(top, username);
        dashboard.setTop(top);

        // 2. Setup Left (Menu)
        left = new Left();
        setupLeft(left, username);
        dashboard.setLeft(left);

        // 3. Setup Footer
        footer = new Footer();
        setupFooter(footer);
        dashboard.setFooter(footer);

        // 4. Setup Center (to be filled by subclasses)
        center = new Center();
        initCenter(center, username);
        dashboard.setCenter(center);

        add(dashboard);
        
        // Additional common components
        setupAdditionalComponents();
    }

    protected void setupTop(Top top, String username) {
        top.setContent("<h2>Jettra Global Dashboard</h2><div style='display:flex; align-items:center; gap:20px;'><span>Welcome, " + username + "</span><button class='j-btn' style='width:auto; padding:8px 15px; font-size:12px;' onclick='window.location.href=\"" + JettraServer.resolvePath("/logout") + "\"'>Cerrar Sesión</button></div>");
    }

    protected void setupLeft(Left left, String username) {
        StringBuilder menuHtml = new StringBuilder();
        menuHtml.append("<div style='padding:15px; font-family:sans-serif;'>");
        menuHtml.append("<h3 style='color:#0ff; margin-bottom:10px; font-weight:600; text-transform:uppercase; font-size:14px; letter-spacing:1px;'>Navigation</h3>");
        menuHtml.append("<hr style='border:1px solid rgba(0, 255, 255, 0.2); margin-bottom:20px;'>");
        
        String menuClass = "display:flex; align-items:center; padding:10px 15px; margin-bottom:8px; background:rgba(20,30,50,0.5); border-left:3px solid transparent; border-radius:4px; color:#fff; cursor:pointer; text-decoration:none; font-size:14px; transition:all 0.3s; box-shadow:0 2px 5px rgba(0,0,0,0.2);";
        String hoverEvents = "onmouseover=\"this.style.background='rgba(0,255,255,0.1)'; this.style.borderLeftColor='#0ff'\" onmouseout=\"this.style.background='rgba(20,30,50,0.5)'; this.style.borderLeftColor='transparent'\"";
        
        // Terminal Access (Link example)
        menuHtml.append("<div style='").append(menuClass).append("' ").append(hoverEvents).append(" onclick='window.location.href=\"" + JettraServer.resolvePath("/dashboard") + "\"'>")
                .append("<svg width='18' height='18' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round' style='margin-right:12px;'><polyline points='4 17 10 11 4 5'></polyline><line x1='12' y1='19' x2='20' y2='19'></line></svg>")
                .append("<span>Main Dashboard</span></div>");

        if ("admin".equals(username) || "avbravo".equals(username)) {
            menuHtml.append("<div style='margin-top:25px;'><h3 style='color:#0ff; margin-bottom:10px; font-weight:600; text-transform:uppercase; font-size:14px; letter-spacing:1px;'>Administration</h3></div>");
            menuHtml.append("<div style='").append(menuClass).append("' ").append(hoverEvents).append(" onclick='window.location.href=\"" + JettraServer.resolvePath("/persona") + "\"'>")
                    .append("<svg width='18' height='18' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round' style='margin-right:12px;'><path d='M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2'></path><circle cx='9' cy='7' r='4'></circle><path d='M23 21v-2a4 4 0 0 0-3-3.87'></path><path d='M16 3.13a4 4 0 0 1 0 7.75'></path></svg>")
                    .append("<span>Persona CRUD</span></div>");
        }

        menuHtml.append("<div style='").append(menuClass).append("' ").append(hoverEvents).append(" onclick='window.location.href=\"" + JettraServer.resolvePath("/logout") + "\"'>")
                .append("<svg width='18' height='18' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round' style='margin-right:12px;'><path d='M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4'></path><polyline points='16 17 21 12 16 7'></polyline><line x1='21' y1='12' x2='9' y2='12'></line></svg>")
                .append("<span>Logout</span></div>");

        menuHtml.append("</div>");
        left.setContent(menuHtml.toString());
    }

    protected void setupFooter(Footer footer) {
        footer.setContent("<p>JettraStack © 2026. Data connection secure.</p>");
    }

    protected void setupAdditionalComponents() {
        String timeoutValue = com.jettra.server.config.JettraConfig.getProperty("server.session.timeout");
        int timeoutMinutes = (timeoutValue != null && !timeoutValue.isBlank()) ? Integer.parseInt(timeoutValue.trim()) : 0;
        add(new io.jettra.wui.components.SessionTimeoutDialog(timeoutMinutes, 60));
    }

    protected abstract void initCenter(Center center, String username);

    protected String getLoggedUser(HttpExchange exchange) {
        String cookies = exchange.getRequestHeaders().getFirst("Cookie");
        if (cookies != null) {
            for (String cookie : cookies.split(";")) {
                cookie = cookie.trim();
                if (cookie.startsWith("username=") && cookie.length() > "username=".length()) {
                    return cookie.substring("username=".length());
                }
            }
        }
        return "Guest";
    }

    protected boolean checkAuth(HttpExchange exchange, String loggedUser) throws IOException {
        if ("Guest".equals(loggedUser) || loggedUser.isEmpty()) {
            exchange.getResponseHeaders().set("Location", JettraServer.resolvePath("/"));
            exchange.sendResponseHeaders(302, -1);
            exchange.getResponseBody().close();
            return false;
        }
        return true;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String loggedUser = getLoggedUser(exchange);
        if (!checkAuth(exchange, loggedUser)) return;

        this.children.clear();
        initLayout(loggedUser);
        
        String html = this.render();
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(200, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.getResponseBody().close();
    }
}
