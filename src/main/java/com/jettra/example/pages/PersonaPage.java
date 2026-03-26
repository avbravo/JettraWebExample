package com.jettra.example.pages;

import com.jettra.example.model.PersonaModel;
import com.jettra.example.repository.PersonaRepository;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import io.jettra.wui.complex.Center;
import io.jettra.wui.complex.Table;
import io.jettra.wui.components.Button;
import io.jettra.wui.components.Div;
import io.jettra.wui.components.Form;
import io.jettra.wui.components.Header;
import io.jettra.wui.components.Label;
import io.jettra.wui.components.Link;
import io.jettra.wui.components.Paragraph;
import io.jettra.wui.components.Script;
import io.jettra.wui.components.Span;
import io.jettra.wui.components.Style;
import io.jettra.wui.components.TextBox;
import io.jettra.wui.validations.JettraValidations;
import io.jettra.wui.core.UIComponent;
import com.jettra.server.JettraServer;

import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class PersonaPage extends DashboardBasePage {

    private final Properties msg = new Properties();
    private String lang = "es";
    private int pageNumber = 1;

    public PersonaPage() {
        super("Persona CRUD");
    }

    private void loadMessages(String language) {
        msg.clear();
        String file = "messages_" + language + ".properties";
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(file)) {
            if (input == null) {
                System.out.println("Sorry, unable to find " + file);
                return;
            }
            msg.load(new InputStreamReader(input, StandardCharsets.UTF_8));
        } catch (Exception ex) {
            System.err.println("Error loading properties: " + ex.getMessage());
        }
    }

    @Override
    protected void initCenter(Center center, String username) {
        loadMessages(this.lang);

        Style customStyles = new Style(
            ".crud-table { width: 100%; border-collapse: collapse; margin-top: 20px; color: #fff; } " +
            ".crud-table th, .crud-table td { padding: 12px; border: 1px solid rgba(0,255,255,0.3); text-align: left; } " +
            ".crud-table th { background: rgba(0,255,255,0.1); color: #0ff; } " +
            ".action-btn { padding: 5px 10px; margin-right: 5px; cursor: pointer; border: none; background: rgba(0,255,255,0.2); color: #fff; border-radius:3px; } " +
            ".action-btn:hover { background: rgba(0,255,255,0.4); } " +
            ".modal { display: none; position: fixed; z-index: 1000; left: 0; top: 0; width: 100%; height: 100%; background-color: rgba(0,0,0,0.7); display:flex; justify-content:center; align-items:center;} " +
            ".modal-content { background-color: #1a202c; padding: 20px; border: 1px solid #0ff; width: 400px; max-width: 90%; border-radius: 8px; box-shadow: 0 0 20px rgba(0,255,255,0.2); color: #fff;} " +
            ".form-group { margin-bottom: 15px; } " +
            ".form-group label { display: block; margin-bottom: 5px; color: #0ff; } " +
            ".form-group input { width: 100%; padding: 8px; background: rgba(0,0,0,0.5); border: 1px solid rgba(0,255,255,0.3); color: #fff; border-radius: 4px; } " +
            ".modal-actions { display: flex; justify-content: flex-end; gap: 10px; margin-top: 20px; } " +
            "@media print { " +
            "   #app > *:not(#center-content) { display: none !important; } " +
            "   #center-content { padding: 0 !important; width: 100% !important; margin: 0 !important; }" +
            "   .no-print { display: none !important; }" +
            "}"
        );

        Div mainContent = new Div();
        mainContent.setProperty("id", "center-content");
        mainContent.setStyle("padding", "20px");


        Div actionContainer = new Div();
        actionContainer.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("flex-wrap", "wrap").setStyle("gap", "10px").setStyle("margin-bottom", "20px");
        actionContainer.addClass("no-print");

        Button addBtn = new Button(msg.getProperty("btn.add"));
        addBtn.setStyle("background-color", "#0f5132");
        addBtn.setProperty("onclick", "addPersona()");
        
        Button printBtn = new Button(msg.getProperty("btn.print"));
        printBtn.setProperty("onclick", "window.print()");

        actionContainer.add(addBtn).add(printBtn);

        // Pagination setup
        List<PersonaModel> all = PersonaRepository.findAll();
        int pageSize = 5;
        int totalPages = (int) Math.ceil((double)all.size() / pageSize);
        if (totalPages == 0) totalPages = 1;
        if (this.pageNumber > totalPages) this.pageNumber = totalPages;
        if (this.pageNumber < 1) this.pageNumber = 1;

        int fromIndex = (this.pageNumber - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, all.size());
        List<PersonaModel> paginated = all.subList(fromIndex, toIndex);

        Table table = new Table();
        table.addClass("crud-table");

        String hName = msg.getProperty("th.name");
        String hAddress = msg.getProperty("th.address");
        String hActions = msg.getProperty("th.actions");
        
        UIComponent tr = new UIComponent("tr"){};
        tr.setStyle("border-bottom", "2px solid var(--jettra-accent)");
        
        UIComponent th1 = new UIComponent("th"){}; th1.setContent(hName); th1.setStyle("padding", "12px").setStyle("text-align", "left").setStyle("color", "var(--jettra-accent)");
        UIComponent th2 = new UIComponent("th"){}; th2.setContent(hAddress); th2.setStyle("padding", "12px").setStyle("text-align", "left").setStyle("color", "var(--jettra-accent)");
        UIComponent th3 = new UIComponent("th"){}; th3.setContent(hActions); th3.setStyle("padding", "12px").setStyle("text-align", "left").setStyle("color", "var(--jettra-accent)");
        th3.addClass("no-print");
        
        tr.add(th1).add(th2).add(th3);
        table.add(tr);

        for (PersonaModel p : paginated) {
            UIComponent trRow = new UIComponent("tr"){};
            trRow.setStyle("border-bottom", "1px solid var(--jettra-border)");
            
            UIComponent td1 = new UIComponent("td"){}; td1.setContent(escapeHtml(p.getNombre())); td1.setStyle("padding", "10px");
            UIComponent td2 = new UIComponent("td"){}; td2.setContent(escapeHtml(p.getDireccion())); td2.setStyle("padding", "10px");
            
            UIComponent td3 = new UIComponent("td"){}; td3.setStyle("padding", "10px");
            td3.addClass("no-print");
            
            Button editBtn = new Button(msg.getProperty("btn.edit"));
            editBtn.addClass("action-btn");
            editBtn.setProperty("onclick", "editPersona('" + p.getId() + "', '" + escapeHtml(p.getNombre()) + "', '" + escapeHtml(p.getDireccion()) + "')");
            
            Button deleteBtn = new Button(msg.getProperty("btn.delete"));
            deleteBtn.addClass("action-btn");
            deleteBtn.setStyle("background", "rgba(255,0,0,0.3)");
            deleteBtn.setProperty("onclick", "deletePersona('" + p.getId() + "')");

            td3.add(editBtn).add(deleteBtn);
            trRow.add(td1).add(td2).add(td3);
            table.add(trRow);
        }

        Div pager = new Div();
        pager.setStyle("margin-top", "20px").setStyle("display", "flex").setStyle("justify-content", "center").setStyle("gap", "10px");
        pager.addClass("no-print");
        
        if (this.pageNumber > 1) {
            Link prev = new Link("?lang=" + lang + "&page=" + (this.pageNumber - 1), msg.getProperty("pager.prev"));
            prev.addClass("j-btn");
            pager.add(prev);
        }
        Span pageInfo = new Span("Page " + this.pageNumber + " of " + totalPages);
        pageInfo.setStyle("padding", "10px");
        pager.add(pageInfo);
        
        if (this.pageNumber < totalPages) {
            Link next = new Link("?lang=" + lang + "&page=" + (this.pageNumber + 1), msg.getProperty("pager.next"));
            next.addClass("j-btn");
            pager.add(next);
        }

        // Modal Form 
        Div modalContainer = new Div();
        modalContainer.setProperty("id", "crudModal");
        modalContainer.setStyle("display", "none").addClass("modal");

        Div modalContent = new Div();
        modalContent.addClass("modal-content");
        
        Header modalTitle = new Header(3, "");
        modalTitle.setProperty("id", "modalTitle");
        modalTitle.setStyle("margin-bottom", "15px").setStyle("color", "#0ff");

        Form form = new Form("personaForm", JettraServer.resolvePath("/persona"));
        
        TextBox actionInput = new TextBox("hidden", "action");
        actionInput.setProperty("id", "modalAction").setProperty("value", "save");
        
        TextBox idInput = new TextBox("hidden", "personaId");
        idInput.setProperty("id", "personaId").setProperty("value", "");
        
        TextBox langInput = new TextBox("hidden", "lang");
        langInput.setProperty("value", lang);

        Div groupNombre = new Div();
        groupNombre.addClass("form-group").setProperty("id", "formGroupNombre");
        Label lblNombre = new Label("personaNombre", msg.getProperty("lbl.name"));
        TextBox inputNombre = new TextBox("text", "nombre");
        inputNombre.setProperty("id", "personaNombre");
        JettraValidations.apply(inputNombre, PersonaModel.class, "nombre");
        groupNombre.add(lblNombre).add(inputNombre);

        Div groupDireccion = new Div();
        groupDireccion.addClass("form-group").setProperty("id", "formGroupDireccion");
        Label lblDireccion = new Label("personaDireccion", msg.getProperty("lbl.address"));
        TextBox inputDireccion = new TextBox("text", "direccion");
        inputDireccion.setProperty("id", "personaDireccion");
        JettraValidations.apply(inputDireccion, PersonaModel.class, "direccion");
        groupDireccion.add(lblDireccion).add(inputDireccion);

        Paragraph deleteMsg = new Paragraph(msg.getProperty("msg.confirm.delete"));
        deleteMsg.setProperty("id", "deleteMsg");
        deleteMsg.setStyle("display", "none").setStyle("color", "#f55");

        Div formActions = new Div();
        formActions.addClass("modal-actions");
        
        Button cancelBtn = new Button(msg.getProperty("btn.cancel"));
        cancelBtn.setProperty("type", "button");
        cancelBtn.setStyle("background", "#555");
        cancelBtn.setProperty("onclick", "closeModal()");
        
        Button submitBtn = new Button(msg.getProperty("btn.save"));
        submitBtn.setProperty("type", "submit");
        submitBtn.setProperty("id", "modalSubmitBtn");

        formActions.add(cancelBtn).add(submitBtn);

        form.add(actionInput).add(idInput).add(langInput).add(groupNombre).add(groupDireccion).add(deleteMsg).add(formActions);
        modalContent.add(modalTitle).add(form);
        modalContainer.add(modalContent);

        // Script block
        Script pageScript = new Script("""
            function openModal() { document.getElementById('crudModal').style.display = 'flex'; }
            function closeModal() { document.getElementById('crudModal').style.display = 'none'; }
            function addPersona() {
              document.getElementById('modalTitle').innerText = '%s';
              document.getElementById('modalAction').value = 'save';
              document.getElementById('personaId').value = '';
              document.getElementById('personaNombre').value = '';
              document.getElementById('personaDireccion').value = '';
              document.getElementById('formGroupNombre').style.display = 'block';
              document.getElementById('formGroupDireccion').style.display = 'block';
              document.getElementById('personaNombre').required = true;
              document.getElementById('personaDireccion').required = true;
              document.getElementById('deleteMsg').style.display = 'none';
              document.getElementById('modalSubmitBtn').innerText = '%s';
              document.getElementById('modalSubmitBtn').style.background = '';
              openModal();
            }
            function editPersona(id, name, address) {
              document.getElementById('modalTitle').innerText = '%s';
              document.getElementById('modalAction').value = 'save';
              document.getElementById('personaId').value = id;
              document.getElementById('personaNombre').value = name;
              document.getElementById('personaDireccion').value = address;
              document.getElementById('formGroupNombre').style.display = 'block';
              document.getElementById('formGroupDireccion').style.display = 'block';
              document.getElementById('personaNombre').required = true;
              document.getElementById('personaDireccion').required = true;
              document.getElementById('deleteMsg').style.display = 'none';
              document.getElementById('modalSubmitBtn').innerText = '%s';
              document.getElementById('modalSubmitBtn').style.background = '';
              openModal();
            }
            function deletePersona(id) {
              document.getElementById('modalTitle').innerText = '%s';
              document.getElementById('modalAction').value = 'delete';
              document.getElementById('personaId').value = id;
              document.getElementById('formGroupNombre').style.display = 'none';
              document.getElementById('formGroupDireccion').style.display = 'none';
              document.getElementById('personaNombre').required = false;
              document.getElementById('personaDireccion').required = false;
              document.getElementById('deleteMsg').style.display = 'block';
              document.getElementById('modalSubmitBtn').innerText = '%s';
              document.getElementById('modalSubmitBtn').style.background = 'rgba(255,0,0,0.5)';
              openModal();
            }""".formatted(
                msg.getProperty("modal.add.title"),
                msg.getProperty("btn.save"),
                msg.getProperty("modal.edit.title"),
                msg.getProperty("btn.save"),
                msg.getProperty("modal.delete.title"),
                msg.getProperty("btn.confirm.delete")
        ));

        Div tableWrapper = new Div();
        tableWrapper.addClass("j-table-container");
        tableWrapper.add(table);

        mainContent.add(actionContainer).add(tableWrapper).add(pager).add(modalContainer);
        center.add(customStyles).add(mainContent).add(pageScript);
    }

    private String escapeHtml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#39;");
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String loggedUser = getLoggedUser(exchange);
        if (!checkAuth(exchange, loggedUser)) return;

        if (!"admin".equals(loggedUser) && !"avbravo".equals(loggedUser)) {
            exchange.getResponseHeaders().set("Location", JettraServer.resolvePath("/dashboard"));
            exchange.sendResponseHeaders(302, -1);
            exchange.getResponseBody().close();
            return;
        }

        String method = exchange.getRequestMethod();
        String query = exchange.getRequestURI().getQuery();
        Map<String, String> queryParams = new HashMap<>();
        
        if (query != null) {
            String[] pairs = query.split("&");
            for (String pairStr : pairs) {
                String[] pair = pairStr.split("=");
                if (pair.length == 2) {
                    queryParams.put(pair[0], pair[1]);
                    if (pair[0].equals("lang")) this.lang = pair[1];
                    if (pair[0].equals("page")) {
                        try {
                            this.pageNumber = Integer.parseInt(pair[pair.length - 1]);
                        } catch(NumberFormatException e) {
                        }
                    }
                } else if (pair.length == 1) {
                    queryParams.put(pair[0], "");
                }
            }
        }

        if ("POST".equalsIgnoreCase(method)) {
            InputStream is = exchange.getRequestBody();
            StringBuilder body = new StringBuilder();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) != -1) {
                body.append(new String(buffer, 0, length, StandardCharsets.UTF_8));
            }
            Map<String, String> formParams = parseFormData(body.toString());
            String action = formParams.get("action");
            String personaId = formParams.get("personaId");
            String submittedLang = formParams.get("lang");
            if (submittedLang != null) this.lang = submittedLang;
            
            if ("save".equals(action)) {
                String nombre = formParams.get("nombre");
                String direccion = formParams.get("direccion");
                if (nombre != null && direccion != null) {
                    PersonaRepository.save(new PersonaModel(personaId, nombre, direccion));
                }
            } else if ("delete".equals(action)) {
                if (personaId != null && !personaId.isEmpty()) {
                    PersonaRepository.delete(personaId);
                }
            }
            
            exchange.getResponseHeaders().set("Location", JettraServer.resolvePath("/persona?lang=" + this.lang + "&page=" + this.pageNumber));
            exchange.sendResponseHeaders(302, -1);
            exchange.getResponseBody().close();
            return;
        }

        this.children.clear();
        initLayout(loggedUser, queryParams);
        
        String html = this.render();
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(200, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.getResponseBody().close();
    }

    private Map<String, String> parseFormData(String formData) {
        Map<String, String> map = new HashMap<>();
        if (formData == null || formData.isEmpty()) return map;
        String[] pairs = formData.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                try {
                    String key = URLDecoder.decode(keyValue[0], "UTF-8");
                    String value = URLDecoder.decode(keyValue[1], "UTF-8");
                    map.put(key, value);
                } catch (IllegalArgumentException | java.io.UnsupportedEncodingException e) {
                }
            } else if (keyValue.length == 1) {
                try {
                    String key = URLDecoder.decode(keyValue[0], "UTF-8");
                    map.put(key, "");
                } catch (IllegalArgumentException | java.io.UnsupportedEncodingException e) {
                }
            }
        }
        return map;
    }
}
