package com.jettra.example.pages;

import io.jettra.wui.complex.Center;
import io.jettra.wui.complex.Modal;
import io.jettra.wui.components.*;

public class ChartsRadarPage extends DashboardBasePage {
    public ChartsRadarPage() {
        super("Charts Radar");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Header h1 = new Header(1, "ChartsRadar Component");
        Paragraph subtitle = new Paragraph("A fully responsive Radar Chart powered by Chart.js.");
        center.add(h1).add(subtitle).add(new Divide());

        ChartsRadar chart = new ChartsRadar("chartR");
        chart.setLabels("Speed", "Power", "Defense", "Agility");
        chart.addDataset("Player 1", new Number[]{80, 90, 70, 85}, 
             new String[]{"rgba(255, 99, 132, 0.5)"}, 
             new String[]{"rgba(255, 99, 132, 1)"});

        Button codeBtn = new Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('code-modal').style.display='block'");

        center.add(chart).add(codeBtn);

        // Code Modal
        Modal codeModal = new Modal("code-modal");
        codeModal.add(new Header(3, "ChartsRadar Code"));
        String codeStr = "ChartsRadar chart = new ChartsRadar(\"chartR\");\\n" +
                         "chart.setLabels(\"Speed\", \"Power\", \"Defense\", \"Agility\");\\n" +
                         "chart.addDataset(\"Player 1\", new Number[]{80, 90, 70, 85}, \\n" +
                         "     new String[]{\"rgba(255, 99, 132, 0.5)\"}, \\n" +
                         "     new String[]{\"rgba(255, 99, 132, 1)\"});";
        
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
