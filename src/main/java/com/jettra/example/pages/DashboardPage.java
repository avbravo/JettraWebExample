package com.jettra.example.pages;

import io.jettra.wui.complex.Center;
import io.jettra.wui.complex.Dashboard;
import io.jettra.wui.complex.Footer;
import io.jettra.wui.complex.Left;
import io.jettra.wui.core.Page;
import io.jettra.wui.complex.Top;
import io.jettra.wui.components.Icon;
import io.jettra.wui.components.Menu;
import io.jettra.wui.components.MenuBar;
import io.jettra.wui.components.MenuItem;
import io.jettra.wui.components.Separator;
import io.jettra.wui.core.annotations.Init;

public class DashboardPage extends Page {

    private Dashboard dashboard;

    public DashboardPage() {
        super("Dashboard");
    }

    @Init
    public void initUI() {
        dashboard = new Dashboard();

        Top top = new Top();
        top.setContent("<h2>Jettra Global Dashboard</h2><div style='display:flex; align-items:center; gap:20px;'><span>Welcome, Admin</span><button class='j-btn' style='width:auto; padding:8px 15px; font-size:12px;' onclick='window.location.reload()'>Cerrar Sesión</button></div>");
        dashboard.setTop(top);

        Left left = new Left();
        Menu menu = new Menu();
        MenuBar fileMenu = new MenuBar("System Control");
        fileMenu.add(new MenuItem("Access Matrix", Icon.SETTINGS));
        fileMenu.add(new Separator());
        fileMenu.add(new MenuItem("Logout", Icon.LOGOUT));
        menu.add(fileMenu);
        left.setContent("<h3>Navigation</h3><hr><p>Terminal Access</p><p>Core Systems</p><p>Network Traffic</p><p>Agent Status</p>");

        dashboard.setLeft(left);

        Center center = new Center();
        center.setContent("<h1>Main Workspace</h1><p>Status: All systems online. Jettra native servers operating at normal capacity. Welcome to the future of immersive dashboard control systems.</p>");
        dashboard.setCenter(center);
        
        Footer footer = new Footer();
        footer.setContent("<p>JettraStack © 2026. Data connection secure.</p>");
        dashboard.setFooter(footer);
        
        add(dashboard);
    }
}
