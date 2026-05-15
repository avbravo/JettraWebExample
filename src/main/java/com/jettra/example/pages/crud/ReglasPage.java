package com.jettra.example.pages.crud;

import com.jettra.example.dashboard.DashboardBasePage;
import com.jettra.example.model.ReglasModel;
import com.jettra.example.repository.ReglasRepository;
import io.jettra.wui.complex.*;
import io.jettra.wui.components.*;
import io.jettra.wui.core.annotations.InjectProperties;
import io.jettra.wui.sync.*;
import com.jettra.server.JettraServer;
import java.util.*;

@JettraPageSincronized(SyncType.ALL)
public class ReglasPage extends DashboardBasePage {
    @InjectProperties(name = "messages")
    private Properties msg;
    
    private Modal crudModal;
    private TextBox modalAction, modalId, inputId, inputSaldo, inputDescuento, inputSaldoNeto;
    private FormGroup groupId, groupSaldo, groupDescuento, groupSaldoNeto;
    private Paragraph deleteMsg;
    private Button modalSubmitBtn;

    public ReglasPage() {
        super("Gestión Manual de Reglas y Cómputos");
    }

    @Override
    protected void initCenter(Center center, String username) {
        setupModal();
        Card card = new Card().setTitle("Listado de Reglas").setWidth("100%");
        Datatable table = new Datatable();

        Button addBtn = new Button("➕ " + msg.getProperty("btn.add", "Añadir")).setId("btnAdd")
                .setBackgroundColor("#238636").setStyle("font-size", "12px")
                .addClickListener(() -> openModal("save", new ReglasModel("", 0.0, 0.0, 0.0)));

        table.addHeaderRow(new Row(
            new TD("ID"), 
            new TD("Saldo"), 
            new TD("Descuento"), 
            new TD("Saldo Neto"),
            new TD().add(addBtn)
        ));

        List<ReglasModel> all = ReglasRepository.findAll();
        all.forEach(r -> {
            Button editBtn = new Button("✏️").setId("btnEdit-" + r.getId())
                    .addClickListener(() -> openModal("save", r));
            Button delBtn = new Button("🗑️").setId("btnDel-" + r.getId()).setStyle("color", "red")
                    .addClickListener(() -> openModal("delete", r));
            table.addRow(new Row(
                new TD(r.getId()),
                new TD(r.getSaldo().toString()),
                new TD(r.getDescuento().toString()),
                new TD(r.getSaldoNeto().toString()),
                new TD().add(editBtn).add(delBtn)
            ));
        });

        center.add(new Div().setStyle("padding", "20px").add(card.add(table))).add(crudModal);
        
        // Manual JS for @Compute simulation
        String js = "function updateSaldoNeto() {\n" +
                    "  const saldo = parseFloat(document.getElementById('inputSaldo').value) || 0;\n" +
                    "  const descuento = parseFloat(document.getElementById('inputDescuento').value) || 0;\n" +
                    "  const neto = saldo - descuento;\n" +
                    "  const target = document.getElementById('inputSaldoNeto');\n" +
                    "  if(target) target.value = neto.toFixed(2);\n" +
                    "}\n" +
                    "// We need to wait for the DOM to be ready or ensure elements exist\n" +
                    "setTimeout(() => {\n" +
                    "  const s = document.getElementById('inputSaldo');\n" +
                    "  const d = document.getElementById('inputDescuento');\n" +
                    "  if(s) s.addEventListener('input', updateSaldoNeto);\n" +
                    "  if(d) d.addEventListener('input', updateSaldoNeto);\n" +
                    "}, 500);\n";
        center.add(new Script(js));
    }

    private void openModal(String action, ReglasModel r) {
        crudModal.setDisplay("block");
        modalAction.setValue(action);
        modalId.setValue(r.getId());
        inputId.setValue(r.getId());
        inputSaldo.setValue(r.getSaldo().toString());
        inputDescuento.setValue(r.getDescuento().toString());
        inputSaldoNeto.setValue(r.getSaldoNeto().toString());
        
        boolean isDel = "delete".equals(action);
        boolean isNew = (r.getId() == null || r.getId().isEmpty());
        
        inputId.setReadonly(!isNew);
        inputSaldo.setReadonly(isDel);
        inputDescuento.setReadonly(isDel);
        
        deleteMsg.setStyle("display", isDel ? "block" : "none");
        modalSubmitBtn.setContent(isDel ? "Confirmar Eliminar" : "Guardar");
        modalSubmitBtn.setBackgroundColor(isDel ? "#da3633" : "#238636");
    }

    private void setupModal() {
        crudModal = new Modal("crudModal").setMaxWidth("500px").setZIndex("9999");
        Form form = new Form("reglasForm", JettraServer.resolvePath("/reglas"));
        modalAction = new TextBox("hidden", "action");
        modalId = new TextBox("hidden", "reglasId");
        form.add(modalAction).add(modalId);

        form.add(groupId = new FormGroup()).add(new Label("id", "ID")).add(inputId = new TextBox("text", "id").setId("inputId"));
        form.add(groupSaldo = new FormGroup()).add(new Label("s", "Saldo")).add(inputSaldo = new TextBox("text", "saldo").setId("inputSaldo"));
        form.add(groupDescuento = new FormGroup()).add(new Label("d", "Descuento")).add(inputDescuento = new TextBox("text", "descuento").setId("inputDescuento"));
        form.add(groupSaldoNeto = new FormGroup()).add(new Label("sn", "Saldo Neto")).add(inputSaldoNeto = new TextBox("text", "saldoNeto").setId("inputSaldoNeto"));
        
        // Saldo Neto is always readonly because it's computed
        inputSaldoNeto.setReadonly(true).setStyle("background-color", "#f0f0f0").setStyle("cursor", "not-allowed");

        deleteMsg = new Paragraph("¿Está seguro de eliminar este registro?").setStyle("color", "#f85149").setStyle("display", "none");
        form.add(deleteMsg);

        Div actions = new Div().setStyle("display", "flex").setStyle("gap", "10px").setStyle("margin-top", "20px");
        actions.add(new Button("Cancelar").setType("button").setOnclick("document.getElementById('crudModal').style.display='none'; return false;"));
        actions.add(modalSubmitBtn = new Button("Guardar").setType("submit").setBackgroundColor("#238636"));
        crudModal.add(new Header(3, "Operación")).add(form.add(actions));
    }

    @Override
    protected void onPost(java.util.Map<String, String> params) {
        String action = params.get("action");
        String idValue = params.get("id");
        if (idValue == null || idValue.isEmpty()) idValue = params.get("reglasId");

        if ("save".equals(action)) {
            try {
                Double saldo = Double.parseDouble(params.get("saldo"));
                Double descuento = Double.parseDouble(params.get("descuento"));
                Double neto = Double.parseDouble(params.get("saldoNeto"));
                ReglasRepository.save(new ReglasModel(idValue, saldo, descuento, neto));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("delete".equals(action)) {
            ReglasRepository.delete(idValue);
        }
        try { redirect(currentExchange, JettraServer.resolvePath("/reglas")); } catch (Exception e) {}
    }
}
