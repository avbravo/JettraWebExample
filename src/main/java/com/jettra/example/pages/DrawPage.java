package com.jettra.example.pages;

import com.jettra.example.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.components.Button;
import io.jettra.wui.components.Div;
import io.jettra.wui.components.Header;
import io.jettra.wui.components.Paragraph;
import io.jettra.wui.components.Draw;
import io.jettra.wui.core.UIComponent;

/**
 * Showcase page for the Draw component.
 */
public class DrawPage extends DashboardBasePage {

    public DrawPage() {
        super("Draw Showcase");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div mainWrapper = new Div();
        mainWrapper.setStyle("display", "flex").setStyle("height", "calc(100vh - 80px)").setStyle("overflow", "hidden");

        // --- Left Palette ---
        Div palette = new Div();
        palette.setStyle("width", "260px")
               .setStyle("background", "rgba(13, 17, 23, 0.8)")
               .setStyle("backdrop-filter", "blur(15px)")
               .setStyle("border-right", "1px solid var(--jettra-border)")
               .setStyle("padding", "20px")
               .setStyle("display", "flex")
               .setStyle("flex-direction", "column")
               .setStyle("gap", "20px")
               .setStyle("overflow-y", "auto");

        Header palTitle = new Header(4, "Herramientas de Dibujo");
        palTitle.setStyle("color", "var(--jettra-accent)").setStyle("margin-top", "0").setStyle("font-size", "14px").setStyle("text-transform", "uppercase").setStyle("letter-spacing", "1px");
        palette.add(palTitle);

        String[][] tools = {
            {"Selection", "🖱️", "Select items"},
            {"Rectangle", "⬜", "Draw rectangles"},
            {"Diamond", "💎", "Draw diamonds"},
            {"Ellipse", "⭕", "Draw circles/ellipses"},
            {"Arrow", "➡️", "Draw arrows"},
            {"Line", "➖", "Draw lines"},
            {"Draw", "✏️", "Freehand drawing"},
            {"Text", "A", "Add text"},
            {"Image", "🖼️", "Insert image"},
            {"Eraser", "🧼", "Erase elements"}
        };

        for (String[] tool : tools) {
            Div toolItem = new Div();
            toolItem.addClass("j-3d-effect")
                    .setStyle("padding", "12px")
                    .setStyle("background", "rgba(255,255,255,0.05)")
                    .setStyle("border-radius", "8px")
                    .setStyle("cursor", "pointer")
                    .setStyle("display", "flex")
                    .setStyle("align-items", "center")
                    .setStyle("gap", "12px")
                    .setStyle("border", "1px solid rgba(255,255,255,0.1)")
                    .setStyle("transition", "all 0.2s ease");
            
            toolItem.setProperty("onmouseover", "this.style.borderColor='var(--jettra-accent)'; this.style.background='rgba(0,255,255,0.05)'");
            toolItem.setProperty("onmouseout", "this.style.borderColor='rgba(255,255,255,0.1)'; this.style.background='rgba(255,255,255,0.05)'");

            UIComponent icon = new UIComponent("span") {};
            icon.setContent(tool[1]);
            icon.setStyle("font-size", "20px");
            
            Div labelCol = new Div();
            UIComponent name = new UIComponent("span") {};
            name.setContent(tool[0]);
            name.setStyle("display", "block").setStyle("font-weight", "bold").setStyle("font-size", "13px").setStyle("color", "#fff");
            UIComponent desc = new UIComponent("span") {};
            desc.setContent(tool[2]);
            desc.setStyle("font-size", "11px").setStyle("color", "#888");
            labelCol.add(name).add(desc);

            toolItem.add(icon).add(labelCol);
            palette.add(toolItem);
        }

        // Action Buttons at bottom of palette
        Div palActions = new Div();
        palActions.setStyle("margin-top", "auto").setStyle("padding-top", "20px");
        
        Button exportBtn = new Button("Exportar Imagen");
        exportBtn.addClass("j-btn-primary").setStyle("width", "100%").setStyle("margin-bottom", "10px");
        
        Button clearBtn = new Button("Limpiar Lienzo");
        clearBtn.addClass("j-btn").setStyle("width", "100%").setStyle("color", "#ff5555").setStyle("border-color", "#ff5555");
        
        palActions.add(exportBtn).add(clearBtn);
        palette.add(palActions);

        // --- Main Canvas Area ---
        Div canvasArea = new Div();
        canvasArea.setStyle("flex", "1")
                  .setStyle("display", "flex")
                  .setStyle("flex-direction", "column")
                  .setStyle("background", "#121212")
                  .setStyle("position", "relative");

        // Toolbar
        Div topToolbar = new Div();
        topToolbar.setStyle("padding", "15px 30px")
                  .setStyle("background", "rgba(0,0,0,0.3)")
                  .setStyle("border-bottom", "1px solid rgba(255,255,255,0.05)")
                  .setStyle("display", "flex")
                  .setStyle("justify-content", "space-between")
                  .setStyle("align-items", "center");

        Header canvasTitle = new Header(3, "Jettra Designer - Pizarra Infinita");
        canvasTitle.setStyle("margin", "0").setStyle("color", "#eee").setStyle("font-size", "1.1rem");
        
        Button codeBtn = new Button("</> Ver Código");
        codeBtn.addClass("j-btn").setStyle("font-size", "12px");
        codeBtn.setProperty("onclick", "document.getElementById('draw-code-modal').style.display = 'block'");
        
        topToolbar.add(canvasTitle).add(codeBtn);

        // Actual Draw Component
        Div drawContainer = new Div();
        drawContainer.setStyle("flex", "1").setStyle("padding", "20px");
        
        Draw sketchpad = new Draw("main-sketch", 1300, 800);
        sketchpad.setStyle("width", "100%").setStyle("height", "100%").setStyle("border-radius", "12px").setStyle("overflow", "hidden").setStyle("box-shadow", "0 10px 40px rgba(0,0,0,0.5)");
        
        drawContainer.add(sketchpad);
        canvasArea.add(topToolbar).add(drawContainer);

        mainWrapper.add(palette).add(canvasArea);

        // --- Code Modal ---
        io.jettra.wui.complex.Modal codeModal = generateCodeModal();
        center.add(mainWrapper).add(codeModal);
    }

    private io.jettra.wui.complex.Modal generateCodeModal() {
        io.jettra.wui.complex.Modal codeModal = new io.jettra.wui.complex.Modal("draw-code-modal");
        codeModal.setStyle("display", "none").setStyle("background", "var(--jettra-glass)").setStyle("backdrop-filter", "blur(10px)")
                 .setStyle("padding", "20px").setStyle("border-radius", "8px").setStyle("width", "90%").setStyle("max-width", "800px")
                 .setStyle("border", "1px solid var(--jettra-border)").setStyle("position", "fixed").setStyle("top", "50%").setStyle("left", "50%")
                 .setStyle("transform", "translate(-50%, -50%)").setStyle("z-index", "1000");

        Header modalH3 = new Header(3, "Implementación del Designer");
        modalH3.setStyle("margin-top", "0").setStyle("color", "var(--jettra-accent)");
        codeModal.add(modalH3);

        String javaCode = "Div wrapper = new Div();\\n" +
                          "wrapper.setStyle(\"display\", \"flex\");\\n\\n" +
                          "// 1. Sidebar Palette\\n" +
                          "Div palette = new Div();\\n" +
                          "palette.add(new Header(4, \"Tools\"));\\n" +
                          "palette.add(new Button(\"Rectangle\"));\\n\\n" +
                          "// 2. Center Draw Component\\n" +
                          "Draw draw = new Draw(\"designer-canvas\", \"100%\", \"100%\");\\n\\n" +
                          "wrapper.add(palette).add(draw);";

        UIComponent pre = new UIComponent("pre") {};
        UIComponent code = new UIComponent("code") {};
        code.setStyle("color", "#a5d6ff");
        code.setContent(javaCode.replace("\\n", "\n"));
        pre.add(code);
        codeModal.add(pre);

        Button closeBtn = new Button("Cerrar");
        closeBtn.addClass("j-btn");
        closeBtn.setProperty("onclick", "document.getElementById('draw-code-modal').style.display='none'");
        codeModal.add(closeBtn);

        return codeModal;
    }
}
