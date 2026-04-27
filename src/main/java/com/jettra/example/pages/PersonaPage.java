package com.jettra.example.pages;

import com.jettra.example.dashboard.DashboardBasePage;
import com.jettra.example.model.PersonaModel;
import com.jettra.example.repository.PersonaRepository;
import io.jettra.wui.complex.Center;
import io.jettra.wui.complex.Modal;
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

    private Modal crudModal;
    private Header modalTitle;
    private TextBox modalAction;
    private TextBox modalId;
    private Button modalSubmitBtn;
    private FormGroup groupNombre;
    private FormGroup groupDireccion;
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
            .modal-box { 
                background-color: #161b22; 
                padding: 25px; 
                border: 1px solid var(--jettra-border); 
                width: 95%; 
                max-width: 500px; 
                border-radius: 12px; 
                box-shadow: 0 10px 50px rgba(0,0,0,0.6); 
                color: #fff;
                transition: all 0.3s ease;
            }
            .form-group { margin-bottom: 15px; }
            .form-group label { display: block; margin-bottom: 5px; color: var(--jettra-accent); font-size: 14px; }
            .modal-actions { display: grid; grid-template-columns: repeat(auto-fit, minmax(120px, 1fr)); gap: 10px; margin-top: 20px; }
            
            /* 100% Responsive DataTable */
            .j-datatable-container { 
                width: 100%; 
                max-width: 100vw;
                border-radius: 8px; 
                overflow: hidden; 
                background: #0d1117;
                box-shadow: 0 4px 15px rgba(0,0,0,0.3);
            }
            .j-datatable { 
                width: 100%; 
                border-collapse: collapse; 
                table-layout: auto;
            }
            
            @media (max-width: 768px) {
                .modal-box { width: 98%; padding: 20px; }
                .j-datatable { min-width: 550px; }
            }
            
            @media (max-width: 600px) {
                .modal-box { padding: 15px; border-radius: 8px; }
                .j-datatable { min-width: 500px; }
                .j-datatable-container { margin: 0; width: 100%; border-radius: 0; }
                .form-group { margin-bottom: 12px; }
            }
            
            @media (max-width: 480px) {
                .modal-box { 
                    width: 100%; 
                    height: 100%; 
                    max-width: 100vw; 
                    max-height: 100vh; 
                    border-radius: 0;
                    top: 0 !important;
                    left: 0 !important;
                    transform: none !important;
                    position: fixed;
                }
                .modal-actions { grid-template-columns: 1fr; }
                .j-input { font-size: 16px !important; }
                .j-datatable { min-width: 450px; }
                .j-datatable td, .j-datatable th { padding: 10px 8px; font-size: 13px; }
                .j-btn { padding: 10px; width: 100%; justify-content: center; }
            }
            """);
        center.add(customStyles);

        Card mainCard = new Card()
            .setTitle(msg.getProperty("subtitle.persona"))
            .setWidth("100%");

        setupModal();

        Button addBtn = new Button("➕ " + msg.getProperty("btn.add"))
            .setId("addBtn")
            .addClass("j-btn")
            .setStyle("background-color", "#238636")
            .addClickListener(() -> {
                this.persona.setId("");
                this.persona.setNombre("");
                this.persona.setDireccion("");
                showModal(msg.getProperty("modal.add.title"), "save");
            });

        // Table
        io.jettra.wui.complex.Datatable table = new io.jettra.wui.complex.Datatable();
        
        Row headerRow = new Row();
        headerRow.add(new TD(msg.getProperty("th.name")));
        headerRow.add(new TD(msg.getProperty("th.address")));
        
        TD actionsTdHeader = new TD()
            .setStyle("display", "flex")
            .setStyle("justify-content", "space-between")
            .setStyle("align-items", "center");
        actionsTdHeader.add(new Span(msg.getProperty("th.actions")));
        actionsTdHeader.add(addBtn.setStyle("margin", "0").setStyle("padding", "4px 8px").setStyle("font-size", "12px"));
        
        headerRow.add(actionsTdHeader);
        table.addHeaderRow(headerRow);

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
                .setStyle("border-color", "rgba(255,0,0,0.3)")
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

        mainCard.add(table);

        // Simple Pager
        if (totalPages > 1) {
            Div pager = new Div()
                .setStyle("margin-top", "20px")
                .setStyle("display", "flex")
                .setStyle("justify-content", "center")
                .setStyle("gap", "15px")
                .setStyle("align-items", "center");
            
            if (pageNumber > 1) {
                pager.add(new Link("?lang=" + lang + "&page=" + (pageNumber - 1), "« " + msg.getProperty("pager.prev"))
                    .addClass("j-btn"));
            }
            pager.add(new Span(msg.getProperty("pager.page", "Página") + " " + pageNumber + " / " + totalPages));
            if (pageNumber < totalPages) {
                pager.add(new Link("?lang=" + lang + "&page=" + (pageNumber + 1), msg.getProperty("pager.next") + " »")
                    .addClass("j-btn"));
            }
            mainCard.add(pager);
        }

        center.add(new Div().setStyle("padding", "20px").add(mainCard));
        center.add(this.crudModal);
    }

    private void showModal(String title, String action) {
        this.crudModal.setStyle("display", "block");
        this.modalTitle.setContent(title);
        this.modalAction.setProperty("value", action);
        this.modalId.setProperty("value", this.persona.getId());

        if ("delete".equals(action)) {
            this.groupNombre.setStyle("display", "none");
            this.groupDireccion.setStyle("display", "none");
            this.deleteMsg.setStyle("display", "block");
            this.modalSubmitBtn.setContent(msg.getProperty("btn.confirm.delete"));
            this.modalSubmitBtn.setStyle("background-color", "#da3633");
        } else {
            this.groupNombre.setStyle("display", "block");
            this.groupDireccion.setStyle("display", "block");
            this.deleteMsg.setStyle("display", "none");
            this.modalSubmitBtn.setContent(msg.getProperty("btn.save"));
            this.modalSubmitBtn.setStyle("background-color", "#238636");
        }
    }

    private void setupModal() {
        this.crudModal = new Modal("crudModal");
        this.crudModal.addClass("modal-box")
            .setStyle("z-index", "9999")
            .setStyle("background-color", "#161b22");

        this.modalTitle = new Header(3, "Operación");
        
        Form form = new Form("personaForm", JettraServer.resolvePath("/persona"));
        this.modalAction = new TextBox("hidden", "action");
        this.modalId = new TextBox("hidden", "personaId");
        
        this.groupNombre = new FormGroup();
        groupNombre.add(new Label("nombre", msg.getProperty("lbl.name")));
        TextBox inputNombre = new TextBox("text", "nombre");
        inputNombre.setId("personaNombre").addClass("j-input");
        JettraValidations.apply(inputNombre, PersonaModel.class, "nombre");
        groupNombre.add(inputNombre);

        this.groupDireccion = new FormGroup();
        groupDireccion.add(new Label("direccion", msg.getProperty("lbl.address")));
        TextBox inputDireccion = new TextBox("text", "direccion");
        inputDireccion.setId("personaDireccion").addClass("j-input");
        JettraValidations.apply(inputDireccion, PersonaModel.class, "direccion");
        groupDireccion.add(inputDireccion);

        this.deleteMsg = new Paragraph(msg.getProperty("msg.confirm.delete"));
        this.deleteMsg.setStyle("color", "#f85149").setStyle("display", "none");

        Div actions = new Div().addClass("modal-actions");
        
        Button cancelBtn = new Button(msg.getProperty("btn.cancel"));
        cancelBtn.setProperty("type", "button");
        cancelBtn.addClass("j-btn").setStyle("background", "#30363d");
        cancelBtn.setProperty("onclick", "document.getElementById('crudModal').style.display='none'; return false;");

        this.modalSubmitBtn = new Button(msg.getProperty("btn.save"));
        this.modalSubmitBtn.addClass("j-btn");
        this.modalSubmitBtn.setProperty("type", "submit");

        actions.add(cancelBtn).add(this.modalSubmitBtn);
        form.add(this.modalAction).add(this.modalId).add(groupNombre).add(groupDireccion).add(this.deleteMsg).add(actions);
        
        this.crudModal.add(this.modalTitle).add(form);
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
