package com.jettra.example.pages;

import com.jettra.example.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.components.*;
import io.jettra.wui.complex.Datatable;
import io.jettra.wui.core.UIComponent;

/**
 * Showcase page for Editable Datatable
 */
public class DatatableEditablePage extends DashboardBasePage {

    public DatatableEditablePage() {
        super("DataTable Editable");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        
        Header h1 = new Header(1, "Datatable Editable");
        h1.setStyle("margin", "0");
        headerRow.add(h1);
        
        Button codeBtn = new Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('datatable-editable-code-modal').style.display = 'block'");
        headerRow.add(codeBtn);
        
        container.add(headerRow);
        
        // --- Modal Dialog for Java Code ---
        io.jettra.wui.complex.Modal codeModal = new io.jettra.wui.complex.Modal("datatable-editable-code-modal");
        codeModal.setStyle("display", "none").setStyle("background", "var(--jettra-glass)")
                 .setStyle("backdrop-filter", "blur(10px)")
                 .setStyle("padding", "20px").setStyle("border-radius", "8px")
                 .setStyle("width", "90%").setStyle("max-width", "800px")
                 .setStyle("border", "1px solid var(--jettra-border)");
        
        codeModal.add(new Header(3, "Java Code Examples").setStyle("margin-top", "0").setStyle("color", "var(--jettra-accent)"));
        
        Div codeContainer = new Div();
        codeContainer.setStyle("background", "rgba(0,0,0,0.4)").setStyle("padding", "15px")
                     .setStyle("border-radius", "4px").setStyle("overflow-x", "auto")
                     .setStyle("margin-bottom", "20px").setStyle("border", "1px solid rgba(255,255,255,0.1)");
        
        String javaCode = "// Creating an Editable Datatable\n" +
                          "Datatable table = new Datatable();\n" +
                          "table.addHeaderRow(\"Artículo\", \"Categoría\", \"Precio\", \"Cantidad\", \"Total\");\n\n" +
                          "for(int i = 0; i < 3; i++) {\n" +
                          "    Row row = new Row();\n" +
                          "    row.setProperty(\"id\", \"row_\" + i);\n" +
                          "    \n" +
                          "    // Read-only Article\n" +
                          "    row.add(new TD(\"Artículo \" + i));\n" +
                          "    \n" +
                          "    // Editable Category\n" +
                          "    SelectOne catBox = new SelectOne(\"cat_\" + i);\n" +
                          "    catBox.addOption(\"tech\", \"Tecnología\");\n" +
                          "    catBox.addOption(\"home\", \"Hogar\");\n" +
                          "    row.add(new TD().add(catBox));\n" +
                          "    \n" +
                          "    // Editable Price\n" +
                          "    TextBox priceBox = new TextBox(\"number\", \"price_\" + i);\n" +
                          "    priceBox.setId(\"price_\" + i);\n" +
                          "    priceBox.setProperty(\"value\", \"10.00\");\n" +
                          "    priceBox.setProperty(\"onchange\", \"updateRow(\" + i + \")\");\n" +
                          "    row.add(new TD().add(priceBox));\n" +
                          "    \n" +
                          "    // Editable Quantity\n" +
                          "    TextBox qtyBox = new TextBox(\"number\", \"qty_\" + i);\n" +
                          "    qtyBox.setId(\"qty_\" + i);\n" +
                          "    qtyBox.setProperty(\"value\", \"1\");\n" +
                          "    qtyBox.setProperty(\"onchange\", \"updateRow(\" + i + \")\");\n" +
                          "    row.add(new TD().add(qtyBox));\n" +
                          "    \n" +
                          "    // Calculated Total\n" +
                          "    TD totalTd = new TD(\"10.00\");\n" +
                          "    totalTd.setId(\"total_\" + i);\n" +
                          "    row.add(totalTd);\n" +
                          "    \n" +
                          "    table.addRow(row);\n" +
                          "}\n";
                          
        UIComponent pre = new UIComponent("pre") {};
        pre.setStyle("margin", "0");
        UIComponent codeTag = new UIComponent("code") {};
        codeTag.setProperty("id", "dt-editable-java-code");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;"));
        
        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        
        Button copyBtn = new Button("Copiar código");
        copyBtn.addClass("j-btn");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('dt-editable-java-code').innerText).then(() => { this.innerText='Copiado!'; setTimeout(() => this.innerText='Copiar código', 2000); })");
        
        Button closeBtn = new Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('datatable-editable-code-modal').style.display = 'none'");
        
        modalActions.add(closeBtn).add(copyBtn);
        codeModal.add(modalActions);
        
        container.add(codeModal);
        // --- End Modal Dialog ---

        Paragraph p = new Paragraph("En JettraWUI, DataTable permite que las columnas sean editables integrando componentes de entrada como TextBox, DatePicker y SelectOne directamente en sus celdas. Se pueden capturar eventos como 'onchange' y 'oninput' para actualizar el estado de filas completas en tiempo real.");
        container.add(p);

        // -- Editable Table Implementation --
        Datatable table = new Datatable();
        table.addHeaderRow("Artículo", "Categoría", "Precio Unit. ($)", "Cantidad", "Fecha de Entrega", "Total por Fila ($)");

        String[] articles = {"Laptop XYZ", "Monitor 4K", "Teclado Mecánico"};
        String[] categories = {"electronic", "accessory", "office"};
        double[] prices = {10000.0, 456.0, 231.0};
        
        for (int i = 0; i < articles.length; i++) {
            Row row = new Row();
            row.setProperty("id", "row_" + i);
            
            // Article Column
            row.add(new TD(articles[i]));
            
            // Category Column (SelectOne)
            SelectOne catBox = new SelectOne("cat_" + i);
            catBox.setId("cat_" + i);
            catBox.addOption("electronic", "Electrónica");
            catBox.addOption("accessory", "Accesorios");
            catBox.addOption("office", "Oficina");
            catBox.setProperty("value", categories[i]);
            catBox.setStyle("width", "120px").setStyle("padding", "5px").setStyle("border-radius", "4px").setStyle("border", "1px solid var(--jettra-border)").setStyle("background", "var(--jettra-bg)");
            catBox.setProperty("onchange", "updateCategory(" + i + ")");
            row.add(new TD().add(catBox));

            // Editable Price Column (TextBox)
            TextBox priceBox = new TextBox("number", "price_" + i);
            priceBox.setId("price_" + i);
            priceBox.setProperty("value", String.valueOf(prices[i]));
            priceBox.setStyle("width", "100px").setStyle("padding", "5px").setStyle("border-radius", "4px").setStyle("border", "1px solid var(--jettra-border)").setStyle("background", "var(--jettra-bg)");
            priceBox.setProperty("oninput", "updateRow(" + i + ")");
            priceBox.setProperty("step", "0.01");
            row.add(new TD().add(priceBox));
            
            // Editable Quantity Column (TextBox)
            TextBox qtyBox = new TextBox("number", "qty_" + i);
            qtyBox.setId("qty_" + i);
            qtyBox.setProperty("value", "1");
            qtyBox.setStyle("width", "80px").setStyle("padding", "5px").setStyle("border-radius", "4px").setStyle("border", "1px solid var(--jettra-border)").setStyle("background", "var(--jettra-bg)");
            qtyBox.setProperty("oninput", "updateRow(" + i + ")");
            qtyBox.setProperty("min", "1");
            row.add(new TD().add(qtyBox));

            // Editable Date Column (DatePicker)
            DatePicker dateBox = new DatePicker("date_" + i, "");
            dateBox.setId("date_" + i);
            dateBox.setStyle("width", "140px").setStyle("padding", "5px").setStyle("border-radius", "4px").setStyle("border", "1px solid var(--jettra-border)").setStyle("background", "var(--jettra-bg)");
            row.add(new TD().add(dateBox));

            // Calculated Total Column
            TD totalTd = new TD(String.valueOf(prices[i]));
            totalTd.setId("total_" + i);
            totalTd.setStyle("font-weight", "bold").setStyle("color", "var(--jettra-accent)");
            row.add(totalTd);
            
            table.addRow(row);
        }

        container.add(table);

        // Grand Total Section
        Div sumContainer = new Div().setStyle("margin-top", "20px").setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("padding-right", "20px");
        Header sumHeader = new Header(3, "Gran Total: $<span id='gran_total'>0.00</span>");
        sumHeader.setStyle("color", "#238636");
        sumContainer.add(sumHeader);
        container.add(sumContainer);

        // Javascript for dynamic interactions
        String scriptContent = "<script>\n" +
            "function updateCategory(index) {\n" +
            "    const catEl = document.getElementById('cat_' + index);\n" +
            "    const priceEl = document.getElementById('price_' + index);\n" +
            "    if (catEl && priceEl) {\n" +
            "        const cat = catEl.value;\n" +
            "        let newPrice = 0;\n" +
            "        if (cat === 'electronic') newPrice = 10000.00;\n" +
            "        else if (cat === 'accessory') newPrice = 456.00;\n" +
            "        else if (cat === 'office') newPrice = 231.00;\n" +
            "        priceEl.value = newPrice.toFixed(2);\n" +
            "        updateRow(index);\n" +
            "    }\n" +
            "}\n" +
            "function updateRow(index) {\n" +
            "    const priceEl = document.getElementById('price_' + index);\n" +
            "    const qtyEl = document.getElementById('qty_' + index);\n" +
            "    const totalEl = document.getElementById('total_' + index);\n" +
            "    if (priceEl && qtyEl && totalEl) {\n" +
            "        const price = parseFloat(priceEl.value) || 0;\n" +
            "        const qty = parseInt(qtyEl.value) || 0;\n" +
            "        const total = price * qty;\n" +
            "        totalEl.innerText = total.toFixed(2);\n" +
            "        \n" +
            "        // Opcional: Resaltar la fila que se actualizó\n" +
            "        const rowEl = document.getElementById('row_' + index);\n" +
            "        if(rowEl) {\n" +
            "            rowEl.style.backgroundColor = 'rgba(0, 255, 255, 0.1)';\n" +
            "            setTimeout(() => rowEl.style.backgroundColor = '', 500);\n" +
            "        }\n" +
            "        \n" +
            "        calculateGrandTotal();\n" +
            "    }\n" +
            "}\n" +
            "function calculateGrandTotal() {\n" +
            "    let grandTotal = 0;\n" +
            "    for (let i = 0; i < " + articles.length + "; i++) {\n" +
            "        const totalEl = document.getElementById('total_' + i);\n" +
            "        if (totalEl) {\n" +
            "            grandTotal += parseFloat(totalEl.innerText) || 0;\n" +
            "        }\n" +
            "    }\n" +
            "    const gtEl = document.getElementById('gran_total');\n" +
            "    if (gtEl) gtEl.innerText = grandTotal.toFixed(2);\n" +
            "}\n" +
            "// Run once on load to initialize Grand Total\n" +
            "document.addEventListener('DOMContentLoaded', calculateGrandTotal);\n" +
            "</script>";
            
        io.jettra.wui.components.Script js = new io.jettra.wui.components.Script(scriptContent);
        container.add(js);

        center.add(container);
    }
}
