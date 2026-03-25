package com.jettra.example.pages;

import com.jettra.example.model.Pais;
import com.jettra.example.repository.PaisRepository;
import com.sun.net.httpserver.HttpExchange;
import com.jettra.server.JettraServer;
import io.jettra.wui.complex.Center;
import io.jettra.wui.complex.Table;
import io.jettra.wui.components.*;
import io.jettra.wui.core.UIComponent;
import io.jettra.wui.core.annotations.InjectViewModel;
import io.jettra.wui.mvc.JettraMVC;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaisPage extends DashboardBasePage {

    @InjectViewModel
    Pais pais;

    public PaisPage() {
        super("Mantenimiento de Países (MVC)");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div mainContent = new Div();
        mainContent.setStyle("padding", "20px");

        Header title = new Header(2, "Catálogo de Países");
        title.setStyle("color", "var(--jettra-accent)");
        mainContent.add(title);

        // Form using Binding
        Form form = new Form("paisForm", JettraServer.resolvePath("/pais"));
        form.setStyle("background", "rgba(0,0,0,0.3)");
        form.setStyle("padding", "20px");
        form.setStyle("border-radius", "8px");
        form.setStyle("margin-bottom", "30px");
        
        Div g1 = new Div();
        g1.setStyle("margin-bottom", "15px");
        Label lblCode = new Label("code", "Código del País: ");
        lblCode.setStyle("display", "block");
        lblCode.setStyle("color", "#0ff");
        g1.add(lblCode);
        TextBox txtCode = new TextBox("text", "code"); // This name "code" matches Pais.code
        g1.add(txtCode);
        
        Div g2 = new Div();
        g2.setStyle("margin-bottom", "15px");
        Label lblName = new Label("name", "Nombre del País: ");
        lblName.setStyle("display", "block");
        lblName.setStyle("color", "#0ff");
        g2.add(lblName);
        TextBox txtName = new TextBox("text", "name"); // This name "name" matches Pais.name
        g2.add(txtName);

        Button saveBtn = new Button("Guardar con MVC Binding");
        saveBtn.setProperty("type", "submit");
        
        form.add(g1).add(g2).add(saveBtn);
        mainContent.add(form);

        // Table
        Table table = new Table();
        table.addClass("j-table");
        
        UIComponent headerRow = new UIComponent("tr"){};
        UIComponent th1 = new UIComponent("th"){};
        th1.setContent("Código");
        UIComponent th2 = new UIComponent("th"){};
        th2.setContent("Nombre");
        UIComponent th3 = new UIComponent("th"){};
        th3.setContent("Acciones");
        headerRow.add(th1).add(th2).add(th3);
        table.add(headerRow);

        List<Pais> all = PaisRepository.findAll();
        for (Pais p : all) {
            UIComponent row = new UIComponent("tr"){};
            UIComponent td1 = new UIComponent("td"){};
            td1.setContent(p.getCode());
            UIComponent td2 = new UIComponent("td"){};
            td2.setContent(p.getName());
            
            UIComponent actions = new UIComponent("td"){};
            Link editLink = new Link("?action=edit&code=" + p.getCode(), "Editar");
            editLink.setStyle("margin-right", "10px").setStyle("color", "#0ff");
            
            Link deleteLink = new Link("?action=delete&code=" + p.getCode(), "Eliminar");
            deleteLink.setStyle("color", "#f55");
            
            actions.add(editLink).add(deleteLink);
            row.add(td1).add(td2).add(actions);
            table.add(row);
        }

        mainContent.add(table);
        center.add(mainContent);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String loggedUser = getLoggedUser(exchange);
        if (!checkAuth(exchange, loggedUser)) return;

        Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI().getQuery());
        
        // Handle Actions (GET)
        String action = queryParams.get("action");
        if ("edit".equals(action)) {
            String code = queryParams.get("code");
            Pais p = PaisRepository.findByCode(code);
            if (p != null) {
                // Populate the injected viewModel
                this.pais.setCode(p.getCode());
                this.pais.setName(p.getName());
            }
        } else if ("delete".equals(action)) {
            String code = queryParams.get("code");
            PaisRepository.delete(code);
            redirect(exchange, "/pais");
            return;
        }

        // Handle Form Submission (POST)
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            Map<String, String> formParams = parseRequestBody(exchange);
            
            // AUTOMATIC BINDING: Update 'pais' object from form fields!
            JettraMVC.updateModelFromRequest(this, formParams);
            
            // Now 'pais' has the updated data, we save it.
            if (this.pais.getCode() != null && !this.pais.getCode().isEmpty()) {
                PaisRepository.save(new Pais(this.pais.getCode(), this.pais.getName()));
            }
            
            redirect(exchange, "/pais");
            return;
        }

        this.children.clear();
        initLayout(loggedUser, queryParams);
        
        renderResponse(exchange, this.render());
    }

    private Map<String, String> parseQueryParams(String query) {
        Map<String, String> map = new HashMap<>();
        if (query == null) return map;
        for (String pair : query.split("&")) {
            String[] kv = pair.split("=");
            if (kv.length == 2) map.put(kv[0], kv[1]);
        }
        return map;
    }

    private Map<String, String> parseRequestBody(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        StringBuilder sb = new StringBuilder();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) != -1) {
            sb.append(new String(buffer, 0, len, StandardCharsets.UTF_8));
        }
        return parseFormData(sb.toString());
    }

    private Map<String, String> parseFormData(String formData) {
        Map<String, String> map = new HashMap<>();
        if (formData == null || formData.isEmpty()) return map;
        for (String pair : formData.split("&")) {
            String[] kv = pair.split("=");
            try {
                if (kv.length == 2) map.put(URLDecoder.decode(kv[0], "UTF-8"), URLDecoder.decode(kv[1], "UTF-8"));
            } catch (Exception e) {}
        }
        return map;
    }

    private void redirect(HttpExchange exchange, String path) throws IOException {
        exchange.getResponseHeaders().set("Location", JettraServer.resolvePath(path));
        exchange.sendResponseHeaders(302, -1);
        exchange.getResponseBody().close();
    }

    private void renderResponse(HttpExchange exchange, String html) throws IOException {
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(200, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.getResponseBody().close();
    }
}
