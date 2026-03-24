package com.jettra.example.pages;

import com.jettra.server.JettraServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.jettra.wui.complex.Center;
import io.jettra.wui.complex.Dashboard;
import io.jettra.wui.complex.Footer;
import io.jettra.wui.complex.Left;
import io.jettra.wui.complex.Top;
import io.jettra.wui.complex.Avatar;
import io.jettra.wui.components.Div;
import io.jettra.wui.components.SelectOneIcon;
import io.jettra.wui.components.CheckBox;
import io.jettra.wui.core.Page;
import io.jettra.wui.core.UIComponent;
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

    public void initLayout(String username, java.util.Map<String, String> params) {
        dashboard = new Dashboard();

        // 1. Setup Top
        top = new Top();
        setupTop(top, username, params);
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

    protected void setupTop(Top top, String username, java.util.Map<String, String> params) {
        top.setStyle("display", "flex")
           .setStyle("justify-content", "space-between")
           .setStyle("align-items", "center")
           .setStyle("width", "100%")
           .setStyle("padding", "2px 10px")
           .setStyle("box-sizing", "border-box")
           .setStyle("overflow", "visible");

        String fullTitle = com.jettra.server.config.JettraConfig.getProperty("app.title");
        String shortTitle = com.jettra.server.config.JettraConfig.getProperty("app.shorttitle");
        if (fullTitle == null) fullTitle = "Jettra Global Dashboard";
        if (shortTitle == null) shortTitle = fullTitle.substring(0, 1);

        io.jettra.wui.components.Header title = new io.jettra.wui.components.Header(2, "");
        title.setContent("<span class='hide-on-low-res'>" + fullTitle + "</span><span class='show-on-low-res'>" + shortTitle + "</span>");
        title.setStyle("margin", "0").setStyle("font-size", "1.1rem");
        top.add(title);
        
        io.jettra.wui.components.Div rightSection = new io.jettra.wui.components.Div();
        rightSection.addClass("j-top-right");
        rightSection.setStyle("display", "flex").setStyle("align-items", "center").setStyle("gap", "6px").setStyle("flex-wrap", "nowrap").setStyle("overflow", "visible");
        
        // Obtener valores por defecto de la configuración
        String defaultConfigLang = com.jettra.server.config.JettraConfig.getProperty("app.language");
        if (defaultConfigLang == null) defaultConfigLang = "en";
        
        String defaultConfigTheme = com.jettra.server.config.JettraConfig.getProperty("app.theme");
        if (defaultConfigTheme == null) defaultConfigTheme = "3d";

        // Selector de Idioma (sin texto en trigger)
        SelectOneIcon langSelect = new SelectOneIcon("lang","");
        langSelect.setShowLabelInTrigger(false);
        langSelect.addOption("en", "", "🇺🇸");
        langSelect.addOption("es", "", "🇪🇸");
        
        // Establecer selección inicial basada en config (o URL si existe)
        String currentLang = params.getOrDefault("lang", defaultConfigLang);
        if ("es".equals(currentLang)) {
            langSelect.setSelectedValue("es", "", "🇪🇸");
        } else {
            langSelect.setSelectedValue("en", "", "🇺🇸");
        }
        rightSection.add(langSelect);
        
        // Selector de Tema (sin texto en trigger)
        SelectOneIcon themeSelect = new SelectOneIcon("theme","");
        themeSelect.setShowLabelInTrigger(false);
        themeSelect.addOption("3d", "", "🚀");
        themeSelect.addOption("dark", "", "🌙");
        themeSelect.addOption("white", "", "☀️");
        
        // Establecer selección inicial basada en config
        // Nota: En el cliente el JS priorizará localStorage para personlización por usuario
        String themeVal = defaultConfigTheme.toLowerCase();
        themeSelect.setSelectedValue(themeVal, "", 
            themeVal.equals("3d") ? "🚀" : (themeVal.equals("dark") ? "🌙" : "☀️"));
            
        rightSection.add(themeSelect);
        
        // Avatar Menu en lugar de Welcome
        Div userWrapper = new Div();
        userWrapper.addClass("j-avatar-wrapper");
        userWrapper.setProperty("onclick", "toggleAvatarMenu()");
        
        String initials = getInitials(username);
        Avatar userAvatar = Avatar.label(initials, "var(--jettra-accent)")
                .setShape(Avatar.Shape.CIRCLE)
                .setSize(Avatar.Size.MD);
        userWrapper.add(userAvatar);
        
        Div dropdown = new Div();
        dropdown.setProperty("id", "user-avatar-dropdown");
        dropdown.addClass("j-avatar-dropdown");
        
        UIComponent logoutLink = new UIComponent("a") {};
        logoutLink.setProperty("href", JettraServer.resolvePath("/logout"));
        logoutLink.setContent("<svg width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='currentColor' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'><path d='M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4'></path><polyline points='16 17 21 12 16 7'></polyline><line x1='21' y1='12' x2='9' y2='12'></line></svg> <span>Logout</span>");
        dropdown.add(logoutLink);
        
        userWrapper.add(dropdown);
        rightSection.add(userWrapper);
        
        // CheckBox para animaciones
        io.jettra.wui.components.Div animDiv = new io.jettra.wui.components.Div();
        animDiv.addClass("hide-mobile");
        animDiv.setStyle("display", "flex").setStyle("align-items", "center").setStyle("gap", "3px");
        io.jettra.wui.components.Span animLabel = new io.jettra.wui.components.Span("Anim");
        animLabel.setStyle("font-size", "0.8rem");
        CheckBox animCB = new CheckBox("anim-toggle", "animated", "true");
        animCB.setProperty("onchange", "toggleJettraAnimation(this.checked)");
        // Eliminado onclick redundante, JettraTheme maneja el evento change
        
        // Estado inicial desde config (si localStorage está vacío, JettraTheme usará este default o true)
        String animatedValue = com.jettra.server.config.JettraConfig.getProperty("app.animated");
        boolean isAnimatedConfig = animatedValue == null || animatedValue.equalsIgnoreCase("true");
        if (isAnimatedConfig) {
            animCB.setProperty("checked", "checked");
        }
        
        // JettraTheme handles animation and theme sync via localStorage internally on DOMContentLoaded

        
        animDiv.add(animLabel).add(animCB);
        rightSection.add(animDiv);

        top.add(rightSection);
    }

    private String getInitials(String name) {
        if (name == null || name.isEmpty()) return "U";
        String[] parts = name.split("[^a-zA-Z0-9]+");
        StringBuilder init = new StringBuilder();
        for (int i = 0; i < Math.min(parts.length, 2); i++) {
            if (parts[i].length() > 0) {
                init.append(parts[i].substring(0, 1).toUpperCase());
            }
        }
        return init.length() > 0 ? init.toString() : "U";
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
            
            menuHtml.append("<div style='").append(menuClass).append("' ").append(hoverEvents).append(" onclick='window.location.href=\"" + com.jettra.server.JettraServer.resolvePath("/avatar") + "\"'>")
                    .append("<svg width='18' height='18' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round' style='margin-right:12px;'><circle cx='12' cy='12' r='3'></circle><path d='M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 0 1 0 2.83 2 2 0 0 1-2.83 0l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-2 2 2 2 0 0 1-2-2v-.09A1.65 1.65 0 0 0 9 19.4a1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 0 1-2.83 0 2 2 0 0 1 0-2.83l.06-.06a1.65 1.65 0 0 0 .33-1.82 1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1-2-2 2 2 0 0 1 2-2h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 0 1 0-2.83 2 2 0 0 1 2.83 0l.06.06a1.65 1.65 0 0 0 1.82.33H9a1.65 1.65 0 0 0 1-1.51V3a2 2 0 0 1 2-2 2 2 0 0 1 2 2v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 0 1 2.83 0 2 2 0 0 1 0 2.83l-.06.06a1.65 1.65 0 0 0-.33 1.82V9a1.65 1.65 0 0 0 1.51 1H21a2 2 0 0 1 2 2 2 2 0 0 1-2 2h-.09a1.65 1.65 0 0 0-1.51 1Z'></path></svg>")
                    .append("<span>Avatar Showcase</span></div>");
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
        
        // Parse query params
        java.util.Map<String, String> queryParams = new java.util.LinkedHashMap<>();
        String query = exchange.getRequestURI().getQuery();
        if (query != null) {
            for (String param : query.split("&")) {
                String[] pair = param.split("=");
                if (pair.length > 1) {
                    queryParams.put(pair[0], pair[1]);
                } else {
                    queryParams.put(pair[0], "");
                }
            }
        }
        
        initLayout(loggedUser, queryParams);
        
        String html = this.render();
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(200, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.getResponseBody().close();
    }
}
