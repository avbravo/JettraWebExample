package com.jettra.example.pages;

import com.jettra.example.model.Pais;
import com.jettra.example.repository.PaisRepository;
import io.jettra.wui.components.*;
import io.jettra.wui.complex.Center;
import io.jettra.wui.core.annotations.InjectViewModel;
import com.jettra.server.JettraServer;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PaisPage extends DashboardBasePage {

    @InjectViewModel
    Pais pais;

    private Div crudModal;
    private Header modalTitle;
    private TextBox modalAction;
    private Button modalSubmitBtn;

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

        center.add(customStyles);
        
        Div mainContent = new Div();
        mainContent.setStyle("padding", "20px");

        Header title = new Header(2, "Catálogo de Países (Pure Java Event-Driven)");
        title.setStyle("color", "var(--jettra-accent)").setStyle("margin-bottom", "20px");
        mainContent.add(title);

        setupModal(); // Initialize components before using them in listeners

        Button addBtn = new Button("➕ Añadir País");
        addBtn.setId("addBtn").addClass("j-btn").setStyle("background-color", "#0f5132");
        addBtn.addClickListener(() -> {
            this.pais.setCode("");
            this.pais.setName("");
            showModal("Nuevo País", "save");
        });
        mainContent.add(addBtn);

        // Table
        io.jettra.wui.complex.Datatable table = new io.jettra.wui.complex.Datatable();
        table.addHeaderRow(new io.jettra.wui.components.Row(
            new io.jettra.wui.components.TD("Código"),
            new io.jettra.wui.components.TD("Nombre"),
            new io.jettra.wui.components.TD("Acciones")
        ));

        List<Pais> all = PaisRepository.findAll();
        for (Pais p : all) {
            io.jettra.wui.components.TD actionsTd = new io.jettra.wui.components.TD();
            actionsTd.setStyle("display", "flex").setStyle("gap", "10px");
            
            Button editBtn = new Button("✏️");
            editBtn.addClass("j-btn").setId("edit-" + p.getCode());
            editBtn.addClickListener(() -> {
                this.pais.setCode(p.getCode());
                this.pais.setName(p.getName());
                showModal("Editar País", "save");
            });
            
            Button deleteBtn = new Button("🗑️");
            deleteBtn.addClass("j-btn").setId("del-" + p.getCode());
            deleteBtn.setStyle("color", "#ff5555").setStyle("border-color", "#ff5555").setStyle("background", "rgba(255,0,0,0.1)");
            deleteBtn.addClickListener(() -> {
                this.pais.setCode(p.getCode());
                this.pais.setName(p.getName());
                showModal("¿Eliminar País?", "delete");
            });
            
            actionsTd.add(editBtn).add(deleteBtn);
            table.addRow(new io.jettra.wui.components.Row(
                new io.jettra.wui.components.TD(p.getCode()),
                new io.jettra.wui.components.TD(p.getName()),
                actionsTd
            ));
        }
        mainContent.add(table);
        center.add(mainContent);
    }

    private void showModal(String title, String action) {
        this.crudModal.setStyle("display", "flex");
        this.modalTitle.setContent(title);
        this.modalAction.setProperty("value", action);
        
        if ("delete".equals(action)) {
            this.modalSubmitBtn.setContent("¡Confirmar!");
            this.modalSubmitBtn.setStyle("background-color", "rgba(255,0,0,0.6)");
        } else {
            this.modalSubmitBtn.setContent("Guardar");
            this.modalSubmitBtn.setStyle("background-color", "");
        }
    }

    private void setupModal() {
        this.crudModal = new Div();
        this.crudModal.setId("crudModal").addClass("modal");

        Div modalBody = new Div();
        modalBody.addClass("modal-content");
        
        this.modalTitle = new Header(3, "Operación");
        this.modalTitle.setId("modalTitle");

        Form form = new Form("paisForm", JettraServer.resolvePath("/pais"));
        this.modalAction = new TextBox("hidden", "modalAction");
        this.modalAction.setId("modalAction");
        
        Div g1 = new Div(); g1.addClass("form-group"); g1.add(new Label("code", "Código"));
        TextBox inputCode = new TextBox("text", "code");
        inputCode.setId("paisCode").setProperty("required", "true").addClass("j-input");
        g1.add(inputCode);

        Div g2 = new Div(); g2.addClass("form-group"); g2.add(new Label("name", "Nombre"));
        TextBox inputName = new TextBox("text", "name");
        inputName.setId("paisName").setProperty("required", "true").addClass("j-input");
        g2.add(inputName);

        Div groupActions = new Div();
        groupActions.addClass("modal-actions");
        
        Button cancelBtn = new Button("Cancelar");
        cancelBtn.addClass("j-btn").setStyle("background", "#555").setStyle("border", "none");
        cancelBtn.addClickListener(() -> {
            this.crudModal.setStyle("display", "none");
        });

        this.modalSubmitBtn = new Button("Guardar");
        this.modalSubmitBtn.setId("modalSubmitBtn").addClass("j-btn");
        this.modalSubmitBtn.setProperty("type", "submit");

        groupActions.add(cancelBtn).add(this.modalSubmitBtn);
        form.add(this.modalAction).add(g1).add(g2).add(groupActions);
        modalBody.add(this.modalTitle).add(form);
        this.crudModal.add(modalBody);
        this.add(this.crudModal);
    }

    @Override
    protected void onPost(Map<String, String> params) {
        // Logging for diagnostics
        String action = params.get("modalAction");
        String code = params.get("code");
        String name = params.get("name");
        
        System.out.println("[PaisPage] POST Action: " + action + " | Code: " + code + " | Name: " + name);
        
        boolean changed = false;
        if ("save".equals(action)) {
            if (code != null && !code.trim().isEmpty()) {
                PaisRepository.save(new Pais(code, name));
                changed = true;
            }
        } else if ("delete".equals(action)) {
            if (code != null && !code.trim().isEmpty()) {
                PaisRepository.delete(code);
                changed = true;
            }
        }
        
        if (changed) {
            try {
                redirect(currentExchange, JettraServer.resolvePath("/pais"));
            } catch (IOException e) { 
                System.err.println("Error during redirect: " + e.getMessage());
            }
        }
    }
}
