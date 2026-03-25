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
        super("Mantenimiento de Países (MVC + Modals)");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Style customStyles = new Style("""
            .crud-table { width: 100%; border-collapse: collapse; margin-top: 20px; color: #fff; }
            .crud-table th, .crud-table td { padding: 12px; border: 1px solid rgba(0,255,255,0.3); text-align: left; }
            .crud-table th { background: rgba(0,255,255,0.1); color: #0ff; }
            .action-btn { padding: 5px 10px; margin-right: 5px; cursor: pointer; border: none; background: rgba(0,255,255,0.2); color: #fff; border-radius:3px; transition: all 0.3s; }
            .action-btn:hover { background: rgba(0,255,255,0.4); box-shadow: 0 0 10px var(--jettra-accent); }
            
            .modal { 
                display: none; 
                position: fixed; 
                z-index: 9999; 
                left: 0; 
                top: 0; 
                width: 100vw; 
                height: 100vh; 
                background-color: rgba(0,0,0,0.85); 
                backdrop-filter: blur(10px); 
                justify-content: center; 
                align-items: center;
                animation: fadeIn 0.3s ease-out;
            }
            @keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }

            .modal-content { 
                background-color: #0d1117; 
                padding: 30px; 
                border: 2px solid #0ff; 
                width: 450px; 
                max-width: 90%; 
                border-radius: 12px; 
                box-shadow: 0 0 50px rgba(0,255,255,0.4); 
                color: #fff;
                transform: scale(0.9);
                animation: scaleUp 0.3s forwards;
            }
            @keyframes scaleUp { from { transform: scale(0.9); } to { transform: scale(1); } }
            
            .form-group { margin-bottom: 20px; }
            .form-group label { display: block; margin-bottom: 8px; color: #0ff; font-weight: 500; }
            .form-group input { width: 100%; padding: 10px; background: rgba(0,0,0,0.5); border: 1px solid rgba(0,255,255,0.3); color: #fff; border-radius: 6px; outline: none; }
            .form-group input:focus { border-color: #0ff; box-shadow: 0 0 10px rgba(0,255,255,0.5); }
            .modal-actions { display: flex; justify-content: flex-end; gap: 15px; margin-top: 25px; }
            """);

        Div mainContent = new Div();
        mainContent.setStyle("padding", "20px");

        Header title = new Header(2, "Catálogo de Países (MVC)");
        title.setStyle("color", "var(--jettra-accent)").setStyle("margin-bottom", "20px");
        mainContent.add(title);

        Button addBtn = new Button("Añadir País");
        addBtn.setStyle("background-color", "#0f5132").setStyle("margin-bottom", "20px");
        addBtn.setProperty("onclick", "addPais()");
        mainContent.add(addBtn);

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
            
            UIComponent actions = new UIComponent("td"){};
            
            Button editBtn = new Button("Editar");
            editBtn.addClass("action-btn");
            editBtn.setProperty("onclick", "editPais('%s', '%s')".formatted(p.getCode(), p.getName()));
            
            Button deleteBtn = new Button("Eliminar");
            deleteBtn.addClass("action-btn");
            deleteBtn.setStyle("background", "rgba(255,0,0,0.3)");
            deleteBtn.setProperty("onclick", "deletePais('%s')".formatted(p.getCode()));
            
            actions.add(editBtn).add(deleteBtn);
            row.add(td1).add(td2).add(actions);
            table.add(row);
        }
        mainContent.add(table);

        // --- Modal Container ---
        Div modalContainer = new Div();
        modalContainer.setProperty("id", "crudModal").addClass("modal");

        Div modalContent = new Div();
        modalContent.addClass("modal-content");
        
        Header modalTitle = new Header(3, "");
        modalTitle.setProperty("id", "modalTitle");

        Form form = new Form("paisForm", JettraServer.resolvePath("/pais"));
        
        TextBox modalActionInput = new TextBox("hidden", "modalAction");
        modalActionInput.setProperty("id", "modalAction");
        
        Div groupCode = new Div();
        groupCode.addClass("form-group").setProperty("id", "groupCode");
        groupCode.add(new Label("code", "Código del País"));
        TextBox inputCode = new TextBox("text", "code");
        inputCode.setProperty("id", "paisCode").setProperty("required", "true");
        groupCode.add(inputCode);

        Div groupName = new Div();
        groupName.addClass("form-group").setProperty("id", "groupName");
        groupName.add(new Label("name", "Nombre del País"));
        TextBox inputName = new TextBox("text", "name");
        inputName.setProperty("id", "paisName").setProperty("required", "true");
        groupName.add(inputName);

        Paragraph deleteMsg = new Paragraph("¿Está seguro de que desea eliminar este país?");
        deleteMsg.setProperty("id", "deleteMsg").setStyle("display", "none").setStyle("color", "#f55").setStyle("margin-bottom", "20px");

        Div formActions = new Div();
        formActions.addClass("modal-actions");
        
        Button cancelBtn = new Button("Cancelar");
        cancelBtn.setProperty("type", "button").setStyle("background", "#555").setProperty("onclick", "closeModal()");
        
        Button submitBtn = new Button("Guardar");
        submitBtn.setProperty("type", "submit").setProperty("id", "modalSubmitBtn");

        formActions.add(cancelBtn).add(submitBtn);

        form.add(modalActionInput).add(groupCode).add(groupName).add(deleteMsg).add(formActions);
        modalContent.add(modalTitle).add(form);
        modalContainer.add(modalContent);
        
        // Scripts
        Script pageScript = new Script("""
            function openModal() { document.getElementById('crudModal').style.display = 'flex'; }
            function closeModal() { document.getElementById('crudModal').style.display = 'none'; }
            
            function addPais() {
                document.getElementById('modalTitle').innerText = 'Añadir Nuevo País';
                document.getElementById('modalAction').value = 'save';
                document.getElementById('paisCode').value = '';
                document.getElementById('paisCode').readOnly = false;
                document.getElementById('paisName').value = '';
                document.getElementById('groupCode').style.display = 'block';
                document.getElementById('groupName').style.display = 'block';
                document.getElementById('deleteMsg').style.display = 'none';
                document.getElementById('modalSubmitBtn').innerText = 'Guardar';
                document.getElementById('modalSubmitBtn').style.background = '';
                openModal();
            }
            
            function editPais(code, name) {
                document.getElementById('modalTitle').innerText = 'Editar País';
                document.getElementById('modalAction').value = 'save';
                document.getElementById('paisCode').value = code;
                document.getElementById('paisCode').readOnly = true;
                document.getElementById('paisName').value = name;
                document.getElementById('groupCode').style.display = 'block';
                document.getElementById('groupName').style.display = 'block';
                document.getElementById('deleteMsg').style.display = 'none';
                document.getElementById('modalSubmitBtn').innerText = 'Actualizar';
                document.getElementById('modalSubmitBtn').style.background = '';
                openModal();
            }
            
            function deletePais(code) {
                document.getElementById('modalTitle').innerText = 'Confirmar Eliminación';
                document.getElementById('modalAction').value = 'delete';
                document.getElementById('paisCode').value = code;
                document.getElementById('groupCode').style.display = 'none';
                document.getElementById('groupName').style.display = 'none';
                document.getElementById('deleteMsg').style.display = 'block';
                document.getElementById('modalSubmitBtn').innerText = 'Eliminar';
                document.getElementById('modalSubmitBtn').style.background = 'rgba(255,0,0,0.6)';
                openModal();
            }
            """);

        center.add(customStyles).add(mainContent);
        this.add(modalContainer).add(pageScript); // Added to page root
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String loggedUser = getLoggedUser(exchange);
        if (!checkAuth(exchange, loggedUser)) return;

        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            Map<String, String> formParams = parseRequestBody(exchange);
            String modalAction = formParams.get("modalAction");
            
            if ("save".equals(modalAction)) {
                // MVC BINDING: Llenar el objeto 'pais' automáticamente desde el formulario
                JettraMVC.updateModelFromRequest(this, formParams);
                
                if (this.pais.getCode() != null && !this.pais.getCode().isEmpty()) {
                    PaisRepository.save(new Pais(this.pais.getCode(), this.pais.getName()));
                }
            } else if ("delete".equals(modalAction)) {
                String code = formParams.get("code");
                if (code != null && !code.isEmpty()) {
                    PaisRepository.delete(code);
                }
            }
            
            redirect(exchange, "/pais");
            return;
        }

        this.children.clear();
        initLayout(loggedUser, new HashMap<>());
        
        renderResponse(exchange, this.render());
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
                else if (kv.length == 1) map.put(URLDecoder.decode(kv[0], "UTF-8"), "");
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
