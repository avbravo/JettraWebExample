package com.jettra.example.pages;

import com.jettra.example.dashboard.DashboardBasePage;
import com.jettra.example.model.PersonaModel;
import com.jettra.example.repository.PersonaRepository;
import io.jettra.wui.complex.Center;
import io.jettra.wui.components.*;
import io.jettra.wui.core.annotations.InjectProperties;
import io.jettra.wui.core.annotations.InjectViewModel;
import io.jettra.wui.sync.JettraPageSincronized;
import io.jettra.wui.sync.SyncType;
import io.jettra.wui.sync.JettraSyncManager;
import io.jettra.wui.validations.JettraValidations;
import com.jettra.server.JettraServer;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@JettraPageSincronized(SyncType.ALL)
public class PersonaPage extends DashboardBasePage {

    @InjectViewModel
    PersonaModel persona;

    @InjectProperties(name = "messages")
    private Properties msg;
    private String lang = "es";
    private int pageNumber = 1;

    private Div crudModal;
    private Header modalTitle;
    private TextBox modalAction;
    private TextBox modalId;
    private Button modalSubmitBtn;
    private Div groupNombre;
    private Div groupDireccion;
    private Paragraph deleteMsg;

    public PersonaPage() {
        super("Mantenimiento de Personas (MVC)");
    }

   
    @Override
    protected void onInit(Map<String, String> params) {
        String pStr = params.get("page");
        if (pStr != null) {
            try { this.pageNumber = Integer.parseInt(pStr); } catch (Exception e) {}
        }
        String lStr = params.get("lang");
        if (lStr != null) this.lang = lStr;
        
        super.onInit(params);
    }

    @Override
    protected void initCenter(Center center, String username) {
  

        Style customStyles = new Style("""
            .modal { display: none; position: fixed; z-index: 9999; left: 0; top: 0; width: 100vw; height: 100vh; background: rgba(0,0,0,0.85); backdrop-filter: blur(10px); justify-content: center; align-items: center;}
            .modal-content { background-color: #0d1117; padding: 30px; border: 2px solid #0ff; width: 450px; border-radius: 12px; box-shadow: 0 0 50px rgba(0,255,255,0.4); color: #fff;}
            .form-group { margin-bottom: 20px; }
            .form-group label { display: block; margin-bottom: 8px; color: #0ff; }
            .modal-actions { display: flex; justify-content: flex-end; gap: 15px; margin-top: 25px; }
            """);
        center.add(customStyles);

        Div mainContent = new Div();
        mainContent.setStyle("padding", "20px");

        Header title = new Header(2, msg.getProperty("subtitle.persona"))
            .setStyle("color", "var(--jettra-accent)")
            .setStyle("margin-bottom", "20px");
        mainContent.add(title);

        setupModal();

        Div actionContainer = new Div()
            .setStyle("display", "flex")
            .setStyle("justify-content", "space-between")
            .setStyle("margin-bottom", "20px");

        Button addBtn = new Button("➕ " + msg.getProperty("btn.add"))
            .setId("addBtn")
            .addClass("j-btn")
            .setStyle("background-color", "#0f5132")
            .addClickListener(() -> {
                this.persona.setId("");
                this.persona.setNombre("");
                this.persona.setDireccion("");
                showModal(msg.getProperty("modal.add.title"), "save");
            });

        Button printBtn = new Button("🖨️ " + msg.getProperty("btn.print"))
            .addClass("j-btn")
            .setProperty("onclick", "window.print()");

        actionContainer.add(addBtn).add(printBtn);
        mainContent.add(actionContainer);

        // Table
        io.jettra.wui.complex.Datatable table = new io.jettra.wui.complex.Datatable()
            .addHeaderRow(msg.getProperty("th.name"),
                msg.getProperty("th.address"),
                msg.getProperty("th.actions")
            );

        List<PersonaModel> all = PersonaRepository.findAll();
        int pageSize = 10;
        int totalPages = (int) Math.ceil((double)all.size() / pageSize);
        if (totalPages == 0) totalPages = 1;
        int start = (pageNumber - 1) * pageSize;
        int end = Math.min(start + pageSize, all.size());
        
        List<PersonaModel> paginated = (start >= all.size()) ? List.of() : all.subList(Math.max(0, start), Math.max(0, end));

        for (PersonaModel p : paginated) {
            TD actionsTd = new TD()
                .setStyle("display", "flex")
                .setStyle("gap", "10px");
            
            Button editBtn = new Button("✏️")
                .setId("edit-" + p.getId())
                .addClass("j-btn")
                .addClickListener(() -> {
                    this.persona.setId(p.getId());
                    this.persona.setNombre(p.getNombre());
                    this.persona.setDireccion(p.getDireccion());
                    showModal(msg.getProperty("modal.edit.title"), "save");
                });
            
            Button deleteBtn = new Button("🗑️")
                .setId("del-" + p.getId())
                .addClass("j-btn")
                .setStyle("color", "#ff5555")
                .setStyle("border-color", "#ff5555")
                .setStyle("background", "rgba(255,0,0,0.1)")
                .addClickListener(() -> {
                    this.persona.setId(p.getId());
                    this.persona.setNombre(p.getNombre());
                    this.persona.setDireccion(p.getDireccion());
                    showModal(msg.getProperty("modal.delete.title"), "delete");
                });
            
            actionsTd.add(editBtn).add(deleteBtn);
            table.addRow(new Row(
                new TD(p.getNombre()),
                new TD(p.getDireccion()),
                actionsTd
            ));
        }

        mainContent.add(table);

        // Simple Pager
        Div pager = new Div()
            .setStyle("margin-top", "20px")
            .setStyle("display", "flex")
            .setStyle("justify-content", "center")
            .setStyle("gap", "15px");
        if (pageNumber > 1) {
            Link prev = new Link("?lang=" + lang + "&page=" + (pageNumber - 1), "« " + msg.getProperty("pager.prev"))
                .addClass("j-btn");
            pager.add(prev);
        }
        pager.add(new Span("Page " + pageNumber + " of " + totalPages));
        if (pageNumber < totalPages) {
            Link next = new Link("?lang=" + lang + "&page=" + (pageNumber + 1), msg.getProperty("pager.next") + " »")
                .addClass("j-btn");
            pager.add(next);
        }
        mainContent.add(pager);

        center.add(mainContent);
    }

    private void showModal(String title, String action) {
        this.crudModal.setStyle("display", "flex");
        this.modalTitle.setContent(title);
        this.modalAction.setProperty("value", action);
        this.modalId.setProperty("value", this.persona.getId());

        if ("delete".equals(action)) {
            this.groupNombre.setStyle("display", "none");
            this.groupDireccion.setStyle("display", "none");
            this.deleteMsg.setStyle("display", "block");
            this.modalSubmitBtn.setContent(msg.getProperty("btn.confirm.delete"));
            this.modalSubmitBtn.setStyle("background-color", "rgba(255,0,0,0.6)");
        } else {
            this.groupNombre.setStyle("display", "block");
            this.groupDireccion.setStyle("display", "block");
            this.deleteMsg.setStyle("display", "none");
            this.modalSubmitBtn.setContent(msg.getProperty("btn.save"));
            this.modalSubmitBtn.setStyle("background-color", "");
        }
    }

    private void setupModal() {
        this.crudModal = new Div();
        this.crudModal.setId("crudModal").addClass("modal");

        Div modalContent = new Div();
        modalContent.addClass("modal-content");
        
        this.modalTitle = new Header(3, "Operación");
        
        Form form = new Form("personaForm", JettraServer.resolvePath("/persona"));
        this.modalAction = new TextBox("hidden", "action");
        this.modalId = new TextBox("hidden", "personaId");
        
        this.groupNombre = new Div();
        groupNombre.addClass("form-group");
        groupNombre.add(new Label("nombre", msg.getProperty("lbl.name")));
        TextBox inputNombre = new TextBox("text", "nombre");
        inputNombre.setId("personaNombre").addClass("j-input");
        JettraValidations.apply(inputNombre, PersonaModel.class, "nombre");
        groupNombre.add(inputNombre);

        this.groupDireccion = new Div();
        groupDireccion.addClass("form-group");
        groupDireccion.add(new Label("direccion", msg.getProperty("lbl.address")));
        TextBox inputDireccion = new TextBox("text", "direccion");
        inputDireccion.setId("personaDireccion").addClass("j-input");
        JettraValidations.apply(inputDireccion, PersonaModel.class, "direccion");
        groupDireccion.add(inputDireccion);

        this.deleteMsg = new Paragraph(msg.getProperty("msg.confirm.delete"));
        this.deleteMsg.setStyle("color", "#ff5555").setStyle("display", "none");

        Div actions = new Div();
        actions.addClass("modal-actions");
        
        Button cancelBtn = new Button(msg.getProperty("btn.cancel"));
        cancelBtn.setProperty("type", "button");
        cancelBtn.addClass("j-btn").setStyle("background", "#555");
        cancelBtn.setProperty("onclick", "document.getElementById('crudModal').style.display='none'; return false;");

        this.modalSubmitBtn = new Button(msg.getProperty("btn.save"));
        this.modalSubmitBtn.addClass("j-btn");
        this.modalSubmitBtn.setProperty("type", "submit");

        actions.add(cancelBtn).add(this.modalSubmitBtn);
        form.add(this.modalAction).add(this.modalId).add(groupNombre).add(groupDireccion).add(this.deleteMsg).add(actions);
        
        modalContent.add(this.modalTitle).add(form);
        this.crudModal.add(modalContent);
        this.add(this.crudModal);
    }

    @Override
    protected void onPost(Map<String, String> params) {
        String action = params.get("action");
        String id = params.get("personaId");
        String nombre = params.get("nombre");
        String direccion = params.get("direccion");
        
        boolean changed = false;
        if ("save".equals(action)) {
            PersonaRepository.save(new PersonaModel(id, nombre, direccion));
            JettraSyncManager.notifyChange("PersonaModel", SyncType.UPDATE, getLoggedUser(currentExchange));
            changed = true;
        } else if ("delete".equals(action)) {
            if (id != null && !id.isEmpty()) {
                PersonaRepository.delete(id);
                JettraSyncManager.notifyChange("PersonaModel", SyncType.DELETE, getLoggedUser(currentExchange));
                changed = true;
            }
        }

        if (changed) {
            try {
                redirect(currentExchange, JettraServer.resolvePath("/persona?lang=" + lang + "&page=" + pageNumber));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
