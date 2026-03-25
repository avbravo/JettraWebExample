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
        super("Mantenimiento de Países (Pure MVC)");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Style customStyles = new Style("""
            .crud-table { width: 100%; border-collapse: collapse; margin-top: 20px; color: #fff; }
            .crud-table th, .crud-table td { padding: 12px; border: 1px solid rgba(0,255,255,0.3); text-align: left; }
            .crud-table th { background: rgba(0,255,255,0.1); color: #0ff; }
            
            .modal { display: none; position: fixed; z-index: 9999; left: 0; top: 0; width: 100vw; height: 100vh; background: rgba(0,0,0,0.85); backdrop-filter: blur(10px); justify-content: center; align-items: center;}
            .modal-content { background-color: #0d1117; padding: 30px; border: 2px solid #0ff; width: 450px; border-radius: 12px; box-shadow: 0 0 50px rgba(0,255,255,0.4); color: #fff;}
            .form-group { margin-bottom: 20px; }
            .form-group label { display: block; margin-bottom: 8px; color: #0ff; }
            .modal-actions { display: flex; justify-content: flex-end; gap: 15px; margin-top: 25px; }
            """);

        Div mainContent = new Div();
        mainContent.setStyle("padding", "20px");

        Header title = new Header(2, "Catálogo de Países (Full MVC)");
        title.setStyle("color", "var(--jettra-accent)").setStyle("margin-bottom", "20px");
        mainContent.add(title);

        // Simple link or button to trigger a reload with ?action=add
        Link addLink = new Link(JettraServer.resolvePath("/pais") + "?action=add", "Añadir País");
        addLink.setStyle("background-color", "#0f5132").setStyle("padding", "10px 20px").setStyle("border-radius", "8px").setStyle("text-decoration", "none").setStyle("color", "#fff");
        mainContent.add(addLink);

        // Table
        Table table = new Table();
        table.addClass("crud-table");
        
        UIComponent headerRow = new UIComponent("tr"){};
        UIComponent th1 = new UIComponent("th"){}; th1.setContent("Código");
        UIComponent th2 = new UIComponent("th"){}; th2.setContent("Nombre");
        UIComponent th3 = new UIComponent("th"){}; th3.setContent("Acciones");
        headerRow.add(th1).add(th2).add(th3);
        table.add(headerRow);

        List<Pais> all = PaisRepository.findAll();
        for (Pais p : all) {
            UIComponent row = new UIComponent("tr"){};
            UIComponent td1 = new UIComponent("td"){}; td1.setContent(p.getCode());
            UIComponent td2 = new UIComponent("td"){}; td2.setContent(p.getName());
            
            UIComponent actionsTd = new UIComponent("td"){};
            Link editLnk = new Link(JettraServer.resolvePath("/pais") + "?action=edit&code=" + p.getCode(), "Editar");
            editLnk.setStyle("margin-right", "10px").setStyle("color", "#0ff");
            
            Link deleteLnk = new Link(JettraServer.resolvePath("/pais") + "?action=delete&code=" + p.getCode(), "Eliminar");
            deleteLnk.setStyle("color", "#f55");
            
            actionsTd.add(editLnk).add(deleteLnk);
            row.add(td1).add(td2).add(actionsTd);
            table.add(row);
        }
        mainContent.add(table);

        // --- MODAL (Purely Reactive via ViewModel) ---
        Div modal = new Div();
        modal.setProperty("id", "crudModal").addClass("modal");

        Div content = new Div();
        content.addClass("modal-content");
        
        Header modalTitle = new Header(3, "Operación de País");
        modalTitle.setProperty("id", "modalTitle");

        Form form = new Form("paisForm", JettraServer.resolvePath("/pais"));
        
        // Action to save or delete
        TextBox modalAction = new TextBox("hidden", "modalAction");
        modalAction.setProperty("id", "modalAction").setProperty("value", "save");
        
        Div g1 = new Div(); g1.addClass("form-group"); g1.add(new Label("code", "Código del País"));
        TextBox inputCode = new TextBox("text", "code"); // BINDING: matches pais.code
        inputCode.setProperty("id", "paisCode").setProperty("required", "true").addClass("j-input");
        g1.add(inputCode);

        Div g2 = new Div(); g2.addClass("form-group"); g2.add(new Label("name", "Nombre del País"));
        TextBox inputName = new TextBox("text", "name"); // BINDING: matches pais.name
        inputName.setProperty("id", "paisName").setProperty("required", "true").addClass("j-input");
        g2.add(inputName);

        Div groupActions = new Div();
        groupActions.addClass("modal-actions");
        
        Link cancelBtn = new Link(JettraServer.resolvePath("/pais"), "Cerrar"); // Simple redirect closes the modal (as it's not open by default)
        cancelBtn.setStyle("background", "#555").setStyle("padding", "10px").setStyle("text-decoration", "none").setStyle("color", "#fff").setStyle("border-radius", "6px");
        
        Button submitBtn = new Button("Guardar");
        submitBtn.setProperty("type", "submit").setProperty("id", "modalSubmitBtn");

        groupActions.add(cancelBtn).add(submitBtn);

        form.add(modalAction).add(g1).add(g2).add(groupActions);
        content.add(modalTitle).add(form);
        modal.add(content);
        
        center.add(customStyles).add(mainContent);
        this.add(modal); // Add directly to Page root for better centering
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String loggedUser = getLoggedUser(exchange);
        if (!checkAuth(exchange, loggedUser)) return;

        Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI().getQuery());
        String action = queryParams.get("action");
        String codeParam = queryParams.get("code");

        // --- MVC SYNC: Set data into viewmodel ---
        if ("edit".equals(action) || "delete".equals(action)) {
            Pais p = PaisRepository.findByCode(codeParam);
            if (p != null) {
                this.pais.setCode(p.getCode());
                this.pais.setName(p.getName());
            }
        }

        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            Map<String, String> formParams = parseRequestBody(exchange);
            String modalAction = formParams.get("modalAction");
            
            if ("save".equals(modalAction)) {
                JettraMVC.updateModelFromRequest(this, formParams);
                if (this.pais.getCode() != null && !this.pais.getCode().isEmpty()) {
                    PaisRepository.save(new Pais(this.pais.getCode(), this.pais.getName()));
                }
            } else if ("delete".equals(modalAction)) {
                PaisRepository.delete(formParams.get("code"));
            }
            redirect(exchange, "/pais");
            return;
        }

        this.children.clear();
        initLayout(loggedUser, queryParams);
        
        // --- CLIENT-SIDE REACTION: Minimal JS to control visibility ---
        if (action != null) {
            String js = "document.getElementById('crudModal').style.display='flex';";
            if ("edit".equals(action)) js += "document.getElementById('modalTitle').innerText='Editar País';";
            if ("delete".equals(action)) {
                js += "document.getElementById('modalTitle').innerText='Eliminar País';";
                js += "document.getElementById('modalAction').value='delete';";
                js += "document.getElementById('modalSubmitBtn').innerText='Eliminar!';";
                js += "document.getElementById('modalSubmitBtn').style.background='rgba(255,0,0,0.6)';";
            }
            this.add(new Script(js));
        }

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
