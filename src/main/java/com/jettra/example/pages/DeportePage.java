package com.jettra.example.pages;

import com.jettra.example.dashboard.DashboardBasePage;
import com.jettra.example.model.DeporteModel;
import com.jettra.example.repository.DeporteRepository;
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
public class DeportePage extends DashboardBasePage {

    @InjectViewModel
    DeporteModel deporte;

    @InjectProperties(name = "messages")
    private Properties msg;
    private String lang = "es";
    private int pageNumber = 1;

    private Modal crudModal;
    private Header modalTitle;
    private TextBox modalAction;
    private TextBox modalCode;
    private TextBox inputDeporte;
    private Button modalSubmitBtn;
    private FormGroup groupCode;
    private FormGroup groupDeporte;
    private Paragraph deleteMsg;

    public DeportePage() {
        super("Mantenimiento de Deportes");
    }

    @Override
    protected void onInit(Map<String, String> params) {
        String pStr = params.get("page");
        if (pStr != null) {
            try {
                this.pageNumber = Integer.parseInt(pStr);
            } catch (Exception e) {
            }
        }
        String lStr = params.get("lang");
        if (lStr != null)
            this.lang = lStr;

        super.onInit(params);
    }

    @Override
    protected void initCenter(Center center, String username) {
        Card mainCard = new Card()
                .setTitle(msg.getProperty("title.deporte"))
                .setSubtitle(msg.getProperty("subtitle.deporte"))
                .setWidth("100%");

        setupModal();

        Button addBtn = new Button("➕ " + msg.getProperty("btn.add.deporte"))
                .setId("addDeporteBtn")
                .addClass("j-btn")
                .setStyle("background-color", "#238636")
                .addClickListener(() -> {
                    this.deporte.setCode("");
                    this.deporte.setDeporte("");
                    showModal(msg.getProperty("modal.add.deporte.title"), "save", false);
                });

        // Table
        io.jettra.wui.complex.Datatable table = new io.jettra.wui.complex.Datatable();
        // io.jettra.wui.complex.Datatable table = new io.jettra.wui.complex.Datatable()
        // .addHeaderRow(msg.getProperty("th.code", "Código"),
        // msg.getProperty("th.name", "Nombre"),
        // msg.getProperty("th.actions", "Acciones")
        // );
        Row headerRow = new Row();
        headerRow.add(new TD(msg.getProperty("th.code")));
        headerRow.add(new TD(msg.getProperty("th.deporte")));

        TD actionsTdHeader = new TD();
        actionsTdHeader.add(addBtn);

        headerRow.add(actionsTdHeader);
        table.addHeaderRow(headerRow);

        List<DeporteModel> all = DeporteRepository.findAll();
        int pageSize = 10;
        int totalPages = (int) Math.ceil((double) all.size() / pageSize);
        if (totalPages == 0)
            totalPages = 1;
        int start = (pageNumber - 1) * pageSize;
        int end = Math.min(start + pageSize, all.size());

        List<DeporteModel> paginated = (start >= all.size()) ? List.of()
                : all.subList(Math.max(0, start), Math.max(0, end));

        for (DeporteModel d : paginated) {
            TD actionsTd = new TD();

            Button editBtn = new Button("✏️")
                    .setId("editBtn_" + d.getCode())

                    .addClickListener(() -> {
                        this.deporte.setCode(d.getCode());
                        this.deporte.setDeporte(d.getDeporte());
                        showModal(msg.getProperty("modal.edit.deporte.title"), "save", true);
                    });

            Button deleteBtn = new Button("🗑️")
                    .setId("deleteBtn_" + d.getCode())
                    .addClickListener(() -> {
                        this.deporte.setCode(d.getCode());
                        this.deporte.setDeporte(d.getDeporte());
                        showModal(msg.getProperty("modal.delete.deporte.title"), "delete", true);
                    });

            actionsTd.add(editBtn).add(deleteBtn);
            table.addRow(new Row(
                    new TD(d.getCode()),
                    new TD(d.getDeporte()),
                    actionsTd));
        }

        mainCard.add(table);

        // Pager
        if (totalPages > 1) {
            Div pager = new Div();

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

        center.add(mainCard);
        center.add(this.crudModal);
    }

    private void showModal(String title, String action, boolean isEdit) {
        this.crudModal.setStyle("display", "block");
        this.modalTitle.setContent(title);
        this.modalAction.setProperty("value", action);
        this.modalCode.setProperty("value", this.deporte.getCode());
        this.inputDeporte.setProperty("value", this.deporte.getDeporte());

        if (isEdit) {
            this.modalCode.setProperty("readonly", "true");
        } else {
            this.modalCode.setProperty("readonly", "false");
        }

        if ("delete".equals(action)) {
            this.groupCode.setStyle("display", "none");
            this.groupDeporte.setStyle("display", "none");
            this.modalSubmitBtn.setContent(msg.getProperty("btn.confirm.delete"));
        } else {
            this.groupCode.setStyle("display", "block");
            this.groupDeporte.setStyle("display", "block");
            this.modalSubmitBtn.setContent(msg.getProperty("btn.save"));
        }
    }

    private void setupModal() {
        this.crudModal = new Modal("crudModal");

        this.modalTitle = new Header(3, "Operación");

        Form form = new Form("deporteForm", JettraServer.resolvePath("/deporte"));
        this.modalAction = new TextBox("hidden", "action");

        this.groupCode = new FormGroup();
        groupCode.add(new Label("code", msg.getProperty("lbl.code")));
        this.modalCode = new TextBox("text", "code");
        this.modalCode.setId("deporteCode").addClass("j-input");
        JettraValidations.apply(this.modalCode, DeporteModel.class, "code");
        groupCode.add(this.modalCode);

        this.groupDeporte = new FormGroup();
        groupDeporte.add(new Label("deporte", msg.getProperty("lbl.deporte")));
        this.inputDeporte = new TextBox("text", "deporte");
        this.inputDeporte.setId("deporteName").addClass("j-input");
        JettraValidations.apply(this.inputDeporte, DeporteModel.class, "deporte");
        groupDeporte.add(this.inputDeporte);

        this.deleteMsg = new Paragraph(msg.getProperty("msg.confirm.delete.deporte"));
        this.deleteMsg.setStyle("color", "#f85149").setStyle("display", "none");

        Div actions = new Div().addClass("modal-actions");

        Button cancelBtn = new Button(msg.getProperty("btn.cancel"));
        cancelBtn.setProperty("type", "button");
        cancelBtn.addClass("j-btn");
        cancelBtn.setProperty("onclick", "document.getElementById('crudModal').style.display='none'; return false;");

        this.modalSubmitBtn = new Button(msg.getProperty("btn.save")).addClass("j-btn").setProperty("type", "submit");

        actions.add(cancelBtn).add(this.modalSubmitBtn);
        form.add(this.modalAction).add(groupCode).add(groupDeporte).add(this.deleteMsg).add(actions);

        this.crudModal.add(this.modalTitle).add(form);
    }

    @Override
    protected void onPost(Map<String, String> params) {
        String action = params.get("action");
        String code = params.get("code");
        String name = params.get("deporte");

        boolean changed = false;
        if ("save".equals(action)) {
            DeporteRepository.save(new DeporteModel(code, name));
            JettraSyncManager.notifyChange("DeporteModel", SyncType.UPDATE, getLoggedUser(currentExchange));
            changed = true;
        } else if ("delete".equals(action)) {
            if (code != null && !code.isEmpty()) {
                DeporteRepository.delete(code);
                JettraSyncManager.notifyChange("DeporteModel", SyncType.DELETE, getLoggedUser(currentExchange));
                changed = true;
            }
        }

        if (changed) {
            try {
                redirect(currentExchange, JettraServer.resolvePath("/deporte?lang=" + lang + "&page=" + pageNumber));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
