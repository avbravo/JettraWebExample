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
import io.jettra.wui.core.Page;
import io.jettra.wui.core.UIComponent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public abstract class DashboardBasePage extends Page {

    protected Dashboard dashboard;
    protected Top top;
    protected Left left;
    protected Footer footer;
    protected Center center;

    public DashboardBasePage(String title) {
        super(title);
    }

    @Override
    protected void onInit(Map<String, String> params) {
        String loggedUser = getLoggedUser(currentExchange);
        // Authentication check
        if ("Guest".equals(loggedUser) || loggedUser.isEmpty()) {
            try {
                redirect(currentExchange, JettraServer.resolvePath("/"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        this.children.clear();
        initLayout(loggedUser, params);
    }

    protected HttpExchange currentExchange;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        this.currentExchange = exchange;
        super.handle(exchange);
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
        // langSelect se inicializa aquí
        
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
        rightSection.add(langSelect);
        
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
        
       
        // CheckBox para animaciones dentro del menú
        Div animItem = new Div();
        animItem.addClass("j-avatar-dropdown-item")
                .setStyle("display", "flex")
                .setStyle("align-items", "center")
                .setStyle("gap", "10px")
                .setStyle("padding", "10px 15px")
                .setStyle("cursor", "default")
                .setProperty("onclick", "event.stopPropagation()"); // Evita que el menú se cierre al hacer clic
        
        io.jettra.wui.components.Span animLabel = new io.jettra.wui.components.Span("Animations");
        animLabel.setStyle("font-size", "0.9rem").setStyle("color", "var(--jettra-text)").setStyle("margin-right", "auto");
        
        io.jettra.wui.components.ToggleSwitch animCB = new io.jettra.wui.components.ToggleSwitch("anim-toggle", "animated", "true");
        animCB.setLabels("ON", "OFF");
        animCB.setProperty("onchange", "toggleJettraAnimation(this.checked)");
        
        String animatedValue = com.jettra.server.config.JettraConfig.getProperty("app.animated");
        boolean isAnimatedConfig = animatedValue == null || animatedValue.equalsIgnoreCase("true");
        if (isAnimatedConfig) {
            animCB.setProperty("checked", "checked");
        }
        
        animItem.add(animLabel).add(animCB);
        dropdown.add(animItem);
        
        
         // Items del menú (Logout)
        UIComponent logoutLink = new UIComponent("a") {};
        logoutLink.setProperty("href", JettraServer.resolvePath("/logout"));
        logoutLink.setContent("<svg width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='currentColor' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'><path d='M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4'></path><polyline points='16 17 21 12 16 7'></polyline><line x1='21' y1='12' x2='9' y2='12'></line></svg> <span>Logout</span>");
        dropdown.add(logoutLink);

        
        
        userWrapper.add(dropdown);
        rightSection.add(userWrapper);

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
        menuHtml.append("<div style='padding:10px; font-family:sans-serif;'>");
        
        String menuClass = "display:flex; align-items:center; padding:8px 12px; margin-bottom:4px; background:rgba(20,30,50,0.5); border-left:3px solid transparent; border-radius:4px; color:#fff; cursor:pointer; text-decoration:none; font-size:13px; transition:all 0.3s;";
        String hoverEvents = "onmouseover=\"this.style.background='rgba(0,255,255,0.1)'; this.style.borderLeftColor='#0ff'\" onmouseout=\"this.style.background='rgba(20,30,50,0.5)'; this.style.borderLeftColor='transparent'\"";
        String categoryStyle = "color:#0ff; margin:15px 0 8px 0; font-weight:600; text-transform:uppercase; font-size:12px; letter-spacing:1px; cursor:pointer; display:flex; justify-content:space-between; align-items:center; opacity:0.8; transition:opacity 0.2s;";
        
        // --- Navigation ---
        menuHtml.append("<div style='").append(categoryStyle).append("' onclick='toggleCategory(this)'><span>Navigation</span> <span>▼</span></div>");
        menuHtml.append("<div class='menu-category-content'>");
        appendMenuItem(menuHtml, "Main Dashboard", "/dashboard", "<svg width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'><polyline points='4 17 10 11 4 5'></polyline><line x1='12' y1='19' x2='20' y2='19'></line></svg>", menuClass, hoverEvents);
        menuHtml.append("</div>");

        // --- Administration ---
        if ("admin".equals(username) || "demo".equals(username) || "avbravo".equals(username)) {
            menuHtml.append("<div style='").append(categoryStyle).append("' onclick='toggleCategory(this)'><span>Administration</span> <span>▼</span></div>");
            menuHtml.append("<div class='menu-category-content'>");
            appendMenuItem(menuHtml, "Persona CRUD", "/persona", "<svg width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'><path d='M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2'></path><circle cx='9' cy='7' r='4'></circle></svg>", menuClass, hoverEvents);
            appendMenuItem(menuHtml, "Pais CRUD (MVC)", "/pais", "<svg width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'><circle cx='12' cy='12' r='10'></circle><line x1='2' y1='12' x2='22' y2='12'></line></svg>", menuClass, hoverEvents);
            appendMenuItem(menuHtml, "Web Designer", "/webdesigner", "<svg width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'><rect x='3' y='3' width='18' height='18' rx='2' ry='2'></rect><line x1='3' y1='9' x2='21' y2='9'></line><line x1='9' y1='21' x2='9' y2='9'></line></svg>", menuClass, hoverEvents);
            appendMenuItem(menuHtml, "Kanban Board", "/kanban", "<svg width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'><rect x='3' y='3' width='18' height='18' rx='2' ry='2'></rect><line x1='3' y1='9' x2='21' y2='9'></line><line x1='9' y1='21' x2='9' y2='9'></line></svg>", menuClass, hoverEvents);
            menuHtml.append("</div>");
        }

        // --- Categories and Components ---
        String compIcon = "<svg width='14' height='14' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round' style='opacity:0.7;'><path d='M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z'></path></svg>";
        
        addCategory(menuHtml, "Typography", new String[]{"Header", "Paragraph", "Span", "Label", "Separator", "Icon", "Typography"}, menuClass, hoverEvents, categoryStyle, compIcon);
        addCategory(menuHtml, "Forms", new String[]{"Button", "CheckBox", "Form", "Forms", "RadioButton", "SelectOne", "SelectOneIcon", "Spinner", "TextBox", "TextArea", "ToggleSwitch"}, menuClass, hoverEvents, categoryStyle, compIcon);
        addCategory(menuHtml, "Navigation", new String[]{"Link", "Menu", "MenuBar", "Navigation"}, menuClass, hoverEvents, categoryStyle, compIcon);
        addCategory(menuHtml, "Feedback", new String[]{"Alert", "Modal", "Notification", "SessionTimeout", "Feedback"}, menuClass, hoverEvents, categoryStyle, compIcon);
        addCategory(menuHtml, "Layout", new String[]{"Avatar", "AvatarGroup", "Board", "Carousel", "Clock", "Div", "Divide", "FileUpload", "FolderSelector", "Grid", "Image", "LayoutDisplay", "LoginAdvanced", "Panel", "ProgressBar", "Table", "TabView", "Tree"}, menuClass, hoverEvents, categoryStyle, compIcon);

        // Logout
        menuHtml.append("<div style='margin-top:20px;'></div>");
        appendMenuItem(menuHtml, "Logout", "/logout", "<svg width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='#0ff' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'><path d='M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4'></path><polyline points='16 17 21 12 16 7'></polyline></svg>", menuClass, hoverEvents);

        // Toggle Script
        menuHtml.append("<script>\n")
                .append("  function toggleCategory(el) {\n")
                .append("    const content = el.nextElementSibling;\n")
                .append("    const arrow = el.querySelector('span:last-child');\n")
                .append("    if (content.style.display === 'none') {\n")
                .append("      content.style.display = 'block';\n")
                .append("      arrow.innerHTML = '▼';\n")
                .append("      el.style.opacity = '1';\n")
                .append("    } else {\n")
                .append("      content.style.display = 'none';\n")
                .append("      arrow.innerHTML = '▶';\n")
                .append("      el.style.opacity = '0.6';\n")
                .append("    }\n")
                .append("  }\n")
                .append("  window.jtUpdate = function(ids) {\n")
                .append("    console.log('[JettraWUI] Updating components:', ids);\n")
                .append("    const idList = ids.split(/[\\s,]+/);\n")
                .append("    idList.forEach(id => {\n")
                .append("      const el = document.getElementById(id);\n")
                .append("      if(el) {\n")
                .append("        el.classList.add('j-refresh-effect');\n")
                .append("        setTimeout(() => el.classList.remove('j-refresh-effect'), 500);\n")
                .append("        // Note: Real partial re-rendering would happen here via fetch\n")
                .append("      }\n")
                .append("    });\n")
                .append("  };\n")
                .append("  // Global trigger for data-update\n")
                .append("  document.addEventListener('click', e => {\n")
                .append("    const target = e.target.closest('[data-update]');\n")
                .append("    if(target) window.jtUpdate(target.getAttribute('data-update'));\n")
                .append("  });\n")
                .append("</script>");
        
        menuHtml.append("</div>");
        left.setContent(menuHtml.toString());
    }

    private void addCategory(StringBuilder html, String name, String[] comps, String menuClass, String hoverEvents, String categoryStyle, String icon) {
        html.append("<div style='").append(categoryStyle).append("' onclick='toggleCategory(this)'><span>").append(name).append("</span> <span>▼</span></div>");
        html.append("<div class='menu-category-content' style='display:none;'>");
        for (String comp : comps) {
            appendMenuItem(html, comp, "/" + comp.toLowerCase(), icon, menuClass, hoverEvents);
        }
        html.append("</div>");
    }

    private void appendMenuItem(StringBuilder html, String text, String path, String icon, String menuClass, String hoverEvents) {
        html.append("<div style='").append(menuClass).append("' ").append(hoverEvents).append(" onclick='window.location.href=\"" + JettraServer.resolvePath(path) + "\"'>")
            .append("<div style='margin-right:10px;'>").append(icon).append("</div>")
            .append("<span>").append(text).append("</span></div>");
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
        System.out.println("[DashboardBasePage] Raw Cookies: " + cookies);
        if (cookies != null) {
            for (String cookie : cookies.split(";")) {
                cookie = cookie.trim();
                System.out.println("[DashboardBasePage] Checking cookie: " + cookie);
                if (cookie.startsWith("username=") && cookie.length() > "username=".length()) {
                    String user = cookie.substring("username=".length());
                    System.out.println("[DashboardBasePage] Found user: " + user);
                    return user;
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

}
