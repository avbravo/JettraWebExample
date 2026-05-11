package com.jettra.example.pages;

import com.jettra.example.dashboard.DashboardBasePage;
import com.jettra.example.model.DeporteModel;
import com.jettra.example.repository.DeporteRepository;
import io.jettra.wui.complex.*;
import io.jettra.wui.components.*;
import io.jettra.wui.core.annotations.InjectProperties;
import io.jettra.wui.sync.*;
import com.jettra.server.JettraServer;
import java.util.*;

@JettraPageSincronized(SyncType.ALL)
public class DeportePage extends DashboardBasePage {
    @InjectProperties(name = "messages")
    private Properties msg;
    
    private int pageNumber = 1;
    private Modal crudModal;
    private TextBox modalAction, modalId, inputIdVisible, inputDeporte;
    private FormGroup groupId, groupDeporte;
    private Paragraph deleteMsg;
    private Button modalSubmitBtn;

    public DeportePage() {
        super("Gestión de Deportes (Optimizado)");
    }

    @Override
    protected void onInit(java.util.Map<String, String> params) {
        try {
            pageNumber = Integer.parseInt(params.getOrDefault("page", "1"));
        } catch (Exception e) {
        }
        super.onInit(params);
    }

    @Override
    protected void initCenter(Center center, String username) {
        setupModal();
        Card card = new Card().setTitle(msg.getProperty("subtitle.deporte")).setWidth("100%");
        Datatable table = new Datatable();

        Button addBtn = new Button("➕ " + msg.getProperty("btn.add")).setId("btnAdd")
                .setBackgroundColor("#238636").setStyle("font-size", "12px")
                .addClickListener(() -> openModal("save", new DeporteModel("", "")));

        table.addHeaderRow(new Row(new TD(msg.getProperty("th.code", "Código")), new TD(msg.getProperty("th.deporte")),
                new TD().add(addBtn)));

        List<DeporteModel> all = DeporteRepository.findAll();
        all.stream().skip((pageNumber - 1) * 10L).limit(10).forEach(d -> {
            Button editBtn = new Button("✏️").setId("btnEdit-" + d.getCode())
                    .addClickListener(() -> openModal("save", d));
            Button delBtn = new Button("🗑️").setId("btnDel-" + d.getCode()).setStyle("color", "red")
                    .addClickListener(() -> openModal("delete", d));
            table.addRow(new Row(new TD(d.getCode()).setStyle("font-family", "monospace").setStyle("font-size", "11px"),
                    new TD(d.getDeporte()), new TD().add(editBtn).add(delBtn)));
        });

        center.add(new Div().setStyle("padding", "20px").add(card.add(table))).add(crudModal);
    }

    private void openModal(String action, DeporteModel d) {
        crudModal.setDisplay("block");
        modalAction.setValue(action);
        modalId.setValue(d.getCode());
        inputIdVisible.setValue(d.getCode());
        inputDeporte.setValue(d.getDeporte());
        boolean isDel = "delete".equals(action);
        boolean isNew = (d.getCode() == null || d.getCode().isEmpty());
        
        inputIdVisible.setReadonly(!isNew);
        inputIdVisible.setStyle("opacity", isNew ? "1" : "0.7");
        
        groupId.setStyle("display", "block");
        groupDeporte.setStyle("display", isDel ? "none" : "block");
        deleteMsg.setStyle("display", isDel ? "block" : "none");
        modalSubmitBtn.setContent(isDel ? msg.getProperty("btn.confirm.delete") : msg.getProperty("btn.save"));
        modalSubmitBtn.setBackgroundColor(isDel ? "#da3633" : "#238636");
    }

    private void setupModal() {
        crudModal = new Modal("crudModal").setMaxWidth("500px").setZIndex("9999");
        Form form = new Form("deporteForm", JettraServer.resolvePath("/deporte"));
        modalAction = new TextBox("hidden", "action");
        modalId = new TextBox("hidden", "deporteId");
        form.add(modalAction).add(modalId);

        groupId = new FormGroup();
        groupId.add(new Label("idV", msg.getProperty("lbl.code", "Código")))
                .add(inputIdVisible = new TextBox("text", "idV"));
        form.add(groupId);

        form.add(groupDeporte = new FormGroup()).add(new Label("d", msg.getProperty("lbl.deporte")))
                .add(inputDeporte = new TextBox("text", "deporte"));

        deleteMsg = new Paragraph(msg.getProperty("msg.confirm.delete")).setStyle("color", "#f85149")
                .setStyle("display", "none");
        form.add(deleteMsg);

        Div actions = new Div().setStyle("display", "flex").setStyle("gap", "10px").setStyle("margin-top", "20px");
        actions.add(new Button(msg.getProperty("btn.cancel")).setType("button")
                .setOnclick("document.getElementById('crudModal').style.display='none'; return false;"));
        actions.add(modalSubmitBtn = new Button(msg.getProperty("btn.save")).setType("submit")
                .setBackgroundColor("#238636"));
        crudModal.add(new Header(3, "Operación")).add(form.add(actions));
    }

    @Override
    protected void onPost(java.util.Map<String, String> params) {
        String action = params.get("action"), id = params.get("deporteId");
        String code = (id == null || id.isEmpty()) ? params.get("idV") : id;
        
        if ("save".equals(action)) {
            DeporteRepository.save(new DeporteModel(code, params.get("deporte")));
            JettraSyncManager.notifyChange("DeporteModel",
                    (id == null || id.isEmpty()) ? SyncType.CREATE : SyncType.UPDATE, "user");
        } else if ("delete".equals(action)) {
            DeporteRepository.delete(id);
            JettraSyncManager.notifyChange("DeporteModel", SyncType.DELETE, "user");
        }
        try {
            redirect(currentExchange, JettraServer.resolvePath("/deporte?page=" + pageNumber));
        } catch (Exception e) {
        }
    }
}
