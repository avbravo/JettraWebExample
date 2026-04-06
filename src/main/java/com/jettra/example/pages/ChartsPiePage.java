package com.jettra.example.pages;

import io.jettra.wui.complex.Center;
import io.jettra.wui.complex.Modal;
import io.jettra.wui.components.*;

public class ChartsPiePage extends DashboardBasePage {
    public ChartsPiePage() {
        super("Charts Pie");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Header h1 = new Header(1, "ChartsPie Component");
        Paragraph subtitle = new Paragraph("A fully responsive Pie Chart powered by Chart.js.");
        center.add(h1).add(subtitle).add(new Divide());

        ChartsPie chart = new ChartsPie("chartP");
        chart.setLabels("Apple", "Banana", "Cherry");
        chart.addDataset("Fruits", new Number[]{50, 20, 30}, 
             new String[]{"#ff0000", "#ffff00", "#ff00ff"}, 
             new String[]{"#fff", "#fff", "#fff"});

        Button codeBtn = new Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('code-modal').style.display='block'");

        center.add(chart).add(codeBtn);

        // Code Modal
        Modal codeModal = new Modal("code-modal");
        codeModal.add(new Header(3, "ChartsPie Code"));
        String codeStr = "ChartsPie chart = new ChartsPie(\"chartP\");\\n" +
                         "chart.setLabels(\"Apple\", \"Banana\", \"Cherry\");\\n" +
                         "chart.addDataset(\"Fruits\", new Number[]{50, 20, 30}, \\n" +
                         "     new String[]{\"#ff0000\", \"#ffff00\", \"#ff00ff\"}, \\n" +
                         "     new String[]{\"#fff\", \"#fff\", \"#fff\"});";
        
        TextArea codeArea = new TextArea("codeArea", "");
        codeArea.setStyle("width", "100%").setStyle("height", "150px").setStyle("font-family", "monospace");
        codeArea.setValue(codeStr.replace("\\n", "\n"));
        
        Button copyBtn = new Button("Copiar codigo");
        copyBtn.addClass("j-btn");
        copyBtn.setStyle("background", "var(--jettra-accent)").setStyle("color", "#fff");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(`" + codeStr + "`).then(() => alert('Code copied!'))");
        
        Button closeBtn = new Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('code-modal').style.display='none'");
        
        Div actions = new Div();
        actions.setStyle("margin-top", "15px").setStyle("display", "flex").setStyle("gap", "10px");
        actions.add(copyBtn).add(closeBtn);
        
        codeModal.add(codeArea).add(actions);
        center.add(codeModal);
    }
}
