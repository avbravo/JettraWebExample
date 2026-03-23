package com.jettra.example.pages;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import io.jettra.wui.complex.Center;
import io.jettra.wui.complex.Dashboard;
import io.jettra.wui.complex.Footer;
import io.jettra.wui.complex.Left;
import io.jettra.wui.core.Page;
import io.jettra.wui.complex.Top;
import io.jettra.wui.core.annotations.Init;
import com.jettra.server.JettraServer;

public class DashboardPage extends Page implements HttpHandler {

    private Dashboard dashboard;

    public DashboardPage() {
        super("Dashboard");
    }

    @Init
    public void initUI() {
        initUI("Admin"); // Default fallback
    }

    public void initUI(String username) {
        dashboard = new Dashboard();

        Top top = new Top();
        top.setContent("<h2>Jettra Global Dashboard</h2><div style='display:flex; align-items:center; gap:20px;'><span>Welcome, " + username + "</span><button class='j-btn' style='width:auto; padding:8px 15px; font-size:12px;' onclick='window.location.href=\"" + JettraServer.resolvePath("/logout") + "\"'>Cerrar Sesión</button></div>");
        dashboard.setTop(top);

        Left left = new Left();
        
        StringBuilder menuHtml = new StringBuilder();
        menuHtml.append("<div style='padding:15px; font-family:sans-serif;'>");
        menuHtml.append("<h3 style='color:#0ff; margin-bottom:10px; font-weight:600; text-transform:uppercase; font-size:14px; letter-spacing:1px;'>Navigation</h3>");
        menuHtml.append("<hr style='border:1px solid rgba(0, 255, 255, 0.2); margin-bottom:20px;'>");
        
        String menuClass = "display:flex; align-items:center; padding:10px 15px; margin-bottom:8px; background:rgba(20,30,50,0.5); border-left:3px solid transparent; border-radius:4px; color:#fff; cursor:pointer; text-decoration:none; font-size:14px; transition:all 0.3s; box-shadow:0 2px 5px rgba(0,0,0,0.2);";
        String hoverEvents = "onmouseover=\"this.style.background='rgba(0,255,255,0.1)'; this.style.borderLeftColor='#0ff'\" onmouseout=\"this.style.background='rgba(20,30,50,0.5)'; this.style.borderLeftColor='transparent'\"";
        
        menuHtml.append("<div style='").append(menuClass).append("' ").append(hoverEvents).append(">")
                .append("<svg width='18' height='18' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round' style='margin-right:12px;'><polyline points='4 17 10 11 4 5'></polyline><line x1='12' y1='19' x2='20' y2='19'></line></svg>")
                .append("<span>Terminal Access</span></div>");

        menuHtml.append("<div style='").append(menuClass).append("' ").append(hoverEvents).append(">")
                .append("<svg width='18' height='18' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round' style='margin-right:12px;'><rect x='2' y='2' width='20' height='8' rx='2' ry='2'></rect><rect x='2' y='14' width='20' height='8' rx='2' ry='2'></rect><line x1='6' y1='6' x2='6.01' y2='6'></line><line x1='6' y1='18' x2='6.01' y2='18'></line></svg>")
                .append("<span>Core Systems</span></div>");

        menuHtml.append("<div style='").append(menuClass).append("' ").append(hoverEvents).append(">")
                .append("<svg width='18' height='18' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round' style='margin-right:12px;'><polyline points='22 12 18 12 15 21 9 3 6 12 2 12'></polyline></svg>")
                .append("<span>Network Traffic</span></div>");

        menuHtml.append("<div style='").append(menuClass).append("' ").append(hoverEvents).append(">")
                .append("<svg width='18' height='18' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round' style='margin-right:12px;'><path d='M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2'></path><circle cx='9' cy='7' r='4'></circle><path d='M23 21v-2a4 4 0 0 0-3-3.87'></path><path d='M16 3.13a4 4 0 0 1 0 7.75'></path></svg>")
                .append("<span>Agent Status</span></div>");

        menuHtml.append("<div style='margin-top:25px;'><h3 style='color:#0ff; margin-bottom:10px; font-weight:600; text-transform:uppercase; font-size:14px; letter-spacing:1px;'>System Control</h3></div>");

        menuHtml.append("<div style='").append(menuClass).append("' ").append(hoverEvents).append(">")
                .append("<svg width='18' height='18' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round' style='margin-right:12px;'><circle cx='12' cy='12' r='3'></circle><path d='M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 0 1 0 2.83 2 2 0 0 1-2.83 0l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-2 2 2 2 0 0 1-2-2v-.09A1.65 1.65 0 0 0 9 19.4a1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 0 1-2.83 0 2 2 0 0 1 0-2.83l.06-.06a1.65 1.65 0 0 0 .33-1.82 1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1-2-2 2 2 0 0 1 2-2h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 0 1 0-2.83 2 2 0 0 1 2.83 0l.06.06a1.65 1.65 0 0 0 1.82.33H9a1.65 1.65 0 0 0 1-1.51V3a2 2 0 0 1 2-2 2 2 0 0 1 2 2v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 0 1 2.83 0 2 2 0 0 1 0 2.83l-.06.06a1.65 1.65 0 0 0-.33 1.82V9a1.65 1.65 0 0 0 1.51 1H21a2 2 0 0 1 2 2 2 2 0 0 1-2 2h-.09a1.65 1.65 0 0 0-1.51 1z'></path></svg>")
                .append("<span>Access Matrix</span></div>");

        if ("admin".equals(username) || "avbravo".equals(username)) {
            menuHtml.append("<div style='").append(menuClass).append("' ").append(hoverEvents).append(" onclick='window.location.href=\"" + JettraServer.resolvePath("/persona") + "\"'>")
                    .append("<svg width='18' height='18' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round' style='margin-right:12px;'><path d='M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2'></path><circle cx='9' cy='7' r='4'></circle><path d='M23 21v-2a4 4 0 0 0-3-3.87'></path><path d='M16 3.13a4 4 0 0 1 0 7.75'></path></svg>")
                    .append("<span>Persona</span></div>");
        }


        menuHtml.append("<div style='").append(menuClass).append("' ").append(hoverEvents).append(" onclick='window.location.href=\"" + JettraServer.resolvePath("/logout") + "\"'>")
                .append("<svg width='18' height='18' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round' style='margin-right:12px;'><path d='M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4'></path><polyline points='16 17 21 12 16 7'></polyline><line x1='21' y1='12' x2='9' y2='12'></line></svg>")
                .append("<span>Logout</span></div>");

        menuHtml.append("</div>");
        left.setContent(menuHtml.toString());

        dashboard.setLeft(left);

        Center center = new Center();
        center.setContent("<h1>Main Workspace</h1><p>Status: All systems online. Jettra native servers operating at normal capacity. Welcome to the future of immersive dashboard control systems.</p>");
        dashboard.setCenter(center);
        
        Footer footer = new Footer();
        footer.setContent("<p>JettraStack © 2026. Data connection secure.</p>");
        dashboard.setFooter(footer);
        
        String timeoutValue = com.jettra.server.config.JettraConfig.getProperty("server.session.timeout");
        int timeoutMinutes = (timeoutValue != null && !timeoutValue.isBlank()) ? Integer.parseInt(timeoutValue.trim()) : 0;
        io.jettra.wui.components.SessionTimeoutDialog sessionDialog = new io.jettra.wui.components.SessionTimeoutDialog(timeoutMinutes, 60); // Muestra advertencia 60 segs antes

        add(dashboard);
        add(sessionDialog);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String loggedUser = "Guest";
        String cookies = exchange.getRequestHeaders().getFirst("Cookie");
        if (cookies != null) {
            for (String cookie : cookies.split(";")) {
                cookie = cookie.trim();
                if (cookie.startsWith("username=") && cookie.length() > "username=".length()) {
                    loggedUser = cookie.substring("username=".length());
                }
            }
        }

        if ("Guest".equals(loggedUser) || loggedUser.isEmpty()) {
            exchange.getResponseHeaders().set("Location", JettraServer.resolvePath("/"));
            exchange.sendResponseHeaders(302, -1);
            exchange.getResponseBody().close();
            return;
        }

        this.children.clear(); // Safe full reset
        initUI(loggedUser);
        
        String html = this.render();
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(200, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.getResponseBody().close();
    }
}
