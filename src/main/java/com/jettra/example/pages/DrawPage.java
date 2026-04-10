package com.jettra.example.pages;

import com.jettra.example.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.components.Button;
import io.jettra.wui.components.Div;
import io.jettra.wui.components.Header;
import io.jettra.wui.components.Draw;
import io.jettra.wui.components.TextBox;
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
        mainWrapper.setProperty("id", "draw-main-wrapper");
        mainWrapper.setStyle("display", "flex").setStyle("height", "calc(100vh - 100px)").setStyle("overflow", "hidden").setStyle("gap", "15px");

        // --- Left Palette ---
        Div palette = new Div();
        palette.setProperty("id", "tool-palette");
        palette.addClass("j-3d-effect")
               .setStyle("width", "280px")
               .setStyle("background", "var(--jettra-glass)")
               .setStyle("backdrop-filter", "blur(20px)")
               .setStyle("border", "1px solid var(--jettra-border)")
               .setStyle("border-radius", "15px")
               .setStyle("padding", "20px")
               .setStyle("display", "flex")
               .setStyle("flex-direction", "column")
               .setStyle("gap", "15px")
               .setStyle("overflow-y", "auto");

        Div palHeader = new Div();
        palHeader.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "10px");
        
        Header palTitle = new Header(4, "Designer Palette");
        palTitle.setStyle("color", "var(--jettra-accent)").setStyle("margin", "0").setStyle("font-size", "14px").setStyle("text-transform", "uppercase").setStyle("letter-spacing", "1px");
        
        Button configBtn = new Button("⚙️");
        configBtn.setStyle("padding", "5px").setStyle("background", "rgba(0,255,255,0.1)").setStyle("border", "1px solid var(--jettra-border)").setStyle("border-radius", "8px").setStyle("cursor", "pointer");
        configBtn.setProperty("onclick", "toggleToolSettings()");
        
        palHeader.add(palTitle).add(configBtn);
        palette.add(palHeader);

        // Tool groups and items
        String[][] allTools = {
            {"Selection", "🖱️", "selection"},
            {"Rectangle", "⬜", "rectangle"},
            {"Diamond", "💎", "diamond"},
            {"Ellipse", "⭕", "ellipse"},
            {"Arrow", "➡️", "arrow"},
            {"Line", "➖", "line"},
            {"Draw", "✏️", "draw"},
            {"Text", "A", "text"}
        };

        Div toolsList = new Div();
        toolsList.setProperty("id", "active-tools-list");
        toolsList.setStyle("display", "flex").setStyle("flex-direction", "column").setStyle("gap", "12px");
        
        for (String[] tool : allTools) {
            Div toolItem = new Div();
            toolItem.addClass("j-3d-effect")
                    .addClass("palette-tool")
                    .setProperty("id", "tool-" + tool[2])
                    .setProperty("draggable", "true")
                    .setProperty("ondragstart", "event.dataTransfer.setData('tool', '" + tool[2] + "')")
                    .setStyle("padding", "12px")
                    .setStyle("background", "rgba(255,255,255,0.03)")
                    .setStyle("border-radius", "10px")
                    .setStyle("cursor", "move")
                    .setStyle("display", "flex")
                    .setStyle("align-items", "center")
                    .setStyle("gap", "15px")
                    .setStyle("border", "1px solid rgba(255,255,255,0.05)")
                    .setStyle("transition", "all 0.3s");
            
            UIComponent icon = new UIComponent("span") {};
            icon.setContent(tool[1]);
            icon.setStyle("font-size", "18px").setStyle("filter", "drop-shadow(0 0 5px var(--jettra-glow))");
            
            UIComponent name = new UIComponent("span") {};
            name.setContent(tool[0]);
            name.setStyle("font-size", "14px").setStyle("font-weight", "500").setStyle("color", "#fff");
            
            toolItem.add(icon).add(name);
            toolsList.add(toolItem);
        }
        palette.add(toolsList);

        // --- Action Area (Save/Open) ---
        Div fileActions = new Div();
        fileActions.setStyle("margin-top", "auto").setStyle("padding-top", "25px").setStyle("display", "flex").setStyle("flex-direction", "column").setStyle("gap", "12px").setStyle("border-top", "1px solid var(--jettra-border)");
        
        Button saveBtn = new Button("💾 Guardar Diagrama");
        saveBtn.addClass("j-btn-primary").setStyle("width", "100%");
        saveBtn.setProperty("onclick", "saveJDraw()");
        
        Button openBtn = new Button("📂 Importar .jdraw");
        openBtn.addClass("j-btn-secondary").setStyle("width", "100%");
        openBtn.setProperty("onclick", "document.getElementById('jdraw-loader').click()");
        
        UIComponent fileInput = new UIComponent("input") {};
        fileInput.setProperty("type", "file");
        fileInput.setProperty("id", "jdraw-loader");
        fileInput.setProperty("accept", ".jdraw");
        fileInput.setStyle("display", "none");
        fileInput.setProperty("onchange", "loadJDraw(this)");
        
        fileActions.add(saveBtn).add(openBtn).add(fileInput);
        palette.add(fileActions);

        // --- Add Dynamic Tool Section ---
        Div addToolSection = new Div();
        addToolSection.setStyle("margin-top", "20px").setStyle("border-top", "1px dashed rgba(255,255,255,0.1)").setStyle("padding-top", "15px");
        
        Header addToolH = new Header(5, "NEW CUSTOM TOOL");
        addToolH.setStyle("font-size", "11px").setStyle("color", "#aaa").setStyle("margin-bottom", "12px").setStyle("letter-spacing", "0.5px");
        
        TextBox newToolInput = new TextBox("text", "");
        newToolInput.setId("new-tool-name").addClass("j-input").setStyle("font-size", "13px").setStyle("margin-bottom", "12px");
        newToolInput.setProperty("placeholder", "Tool Name...");
        
        Button addToolBtn = new Button("➕ ADD TOOL");
        addToolBtn.addClass("j-btn").setStyle("width", "100%").setStyle("font-size", "11px").setStyle("background", "rgba(0,255,255,0.05)");
        addToolBtn.setProperty("onclick", "addNewToolToPalette()");
        
        addToolSection.add(addToolH).add(newToolInput).add(addToolBtn);
        palette.add(addToolSection);

        // --- Main Canvas Area ---
        Div canvasArea = new Div();
        canvasArea.setStyle("flex", "1").setStyle("display", "flex").setStyle("flex-direction", "column").setStyle("position", "relative");
        
        // Handle Drop script target
        Div dropTarget = new Div();
        dropTarget.setStyle("flex", "1").setStyle("position", "relative").setStyle("overflow", "hidden").setStyle("border-radius", "15px").setStyle("border", "1px solid var(--jettra-border)");
        dropTarget.setProperty("ondragover", "event.preventDefault()");
        dropTarget.setProperty("ondrop", "handleToolDrop(event)");
        
        Draw sketchpad = new Draw("main-sketch", 1600, 1000);
        sketchpad.setStyle("width", "100%").setStyle("height", "100%");
        
        dropTarget.add(sketchpad);
        canvasArea.add(dropTarget);

        mainWrapper.add(palette).add(canvasArea);

        // --- Tool Settings Modal ---
        Div settingsModal = createSettingsModal(allTools);
        
        // --- Custom Logic Script ---
        UIComponent mainScript = new UIComponent("script") {};
        mainScript.setContent(
            "function handleToolDrop(event) {" +
            "  event.preventDefault();" +
            "  const toolType = event.dataTransfer.getData('tool');" +
            "  const api = window['excalidrawAPI_main-sketch'];" +
            "  if(api) {" +
            "    const rect = document.getElementById('main-sketch').getBoundingClientRect();" +
            "    const x = event.clientX - rect.left;" +
            "    const y = event.clientY - rect.top;" +
            "    const newElement = {" +
            "      type: (['rectangle','ellipse','diamond','arrow','line'].includes(toolType) ? toolType : 'rectangle')," +
            "      x: x - 50, y: y - 50, width: 100, height: 100," +
            "      strokeColor: '#00ffff', backgroundColor: 'rgba(0,255,255,0.1)'," +
            "      fillStyle: 'solid', strokeWidth: 2, roughness: 1, opacity: 100," +
            "      seed: Math.floor(Math.random() * 100000)," +
            "      version: 1, versionNonce: Math.floor(Math.random() * 100000)," +
            "      isDeleted: false, groupIds: [], boundElements: []," +
            "      id: 'dt_' + Date.now()" +
            "    };" +
            "    api.updateScene({ elements: [...api.getSceneElements(), newElement] });" +
            "    api.updateScene({ appState: { activeTool: { type: toolType } } });" +
            "  } else {" +
            "    console.warn('Excalidraw API not ready');" +
            "  }" +
            "}" +
            "function saveJDraw() {" +
            "  const api = window['excalidrawAPI_main-sketch'];" +
            "  if(!api) return;" +
            "  const elements = api.getSceneElements();" +
            "  const appState = api.getAppState();" +
            "  const data = JSON.stringify({ elements, appState });" +
            "  const blob = new Blob([data], { type: 'application/json' });" +
            "  const url = URL.createObjectURL(blob);" +
            "  const link = document.createElement('a');" +
            "  link.href = url;" +
            "  link.download = 'diagrama-' + new Date().getTime() + '.jdraw';" +
            "  link.click();" +
            "}" +
            "function loadJDraw(input) {" +
            "  const file = input.files[0]; if(!file) return;" +
            "  const reader = new FileReader();" +
            "  reader.onload = (e) => {" +
            "    const api = window['excalidrawAPI_main-sketch'];" +
            "    if(api) {" +
            "      const data = JSON.parse(e.target.result);" +
            "      api.updateScene({ elements: data.elements, appState: data.appState });" +
            "      window.show3DMessage('Archivo Cargado', 'Diagrama restaurado correctamente.');" +
            "    }" +
            "  };" +
            "  reader.readAsText(file);" +
            "}" +
            "function toggleToolSettings() { document.getElementById('tool-settings-modal').style.display='flex'; }" +
            "function toggleToolVisibility(toolId, checked) {" +
            "  const el = document.getElementById('tool-' + toolId);" +
            "  if(el) el.style.display = checked ? 'flex' : 'none';" +
            "}" +
            "function addNewToolToPalette() {" +
            "  const name = document.getElementById('new-tool-name').value;" +
            "  if(!name) return;" +
            "  const id = name.toLowerCase().replace(/\\\\s/g, '-');" +
            "  const list = document.getElementById('active-tools-list');" +
            "  const div = document.createElement('div');" +
            "  div.className = 'j-3d-effect palette-tool';" +
            "  div.id = 'tool-' + id;" +
            "  div.draggable = true;" +
            "  div.style = 'padding:12px; background:rgba(255,255,255,0.03); border-radius:10px; cursor:move; display:flex; align-items:center; gap:15px; border:1px solid rgba(255,255,255,0.05); margin-bottom:12px; position:relative; transition: all 0.3s;';" +
            "  div.ondragstart = (e) => e.dataTransfer.setData('tool', id);" +
            "  div.innerHTML = `<span style=\"font-size:18px; filter:drop-shadow(0 0 5px var(--jettra-glow));\">🛠️</span><span style=\"font-size:14px; font-weight:500; color:#fff;\">${name}</span><span onclick=\"this.parentElement.remove()\" style=\"margin-left:auto; cursor:pointer; opacity:0.5; hover:{opacity:1}\">🗑️</span>`;" +
            "  list.appendChild(div);" +
            "  document.getElementById('new-tool-name').value = '';" +
            "  window.show3DMessage('Paleta Actualizada', 'Herramienta añadida: ' + name);" +
            "}"
        );

        center.add(mainWrapper).add(settingsModal).add(mainScript);
    }

    private Div createSettingsModal(String[][] tools) {
        Div modal = new Div();
        modal.setProperty("id", "tool-settings-modal");
        modal.addClass("j-modal");
        modal.setStyle("display", "none").setStyle("position", "fixed").setStyle("top", "0").setStyle("left", "0").setStyle("width", "100vw").setStyle("height", "100vh")
             .setStyle("background", "rgba(0,0,0,0.85)").setStyle("backdrop-filter", "blur(10px)").setStyle("justify-content", "center").setStyle("align-items", "center").setStyle("z-index", "3000");
        
        Div content = new Div();
        content.addClass("j-card").setStyle("width", "400px").setStyle("padding", "35px").setStyle("border-radius", "20px").setStyle("border", "1px solid var(--jettra-accent)");
        
        Header h = new Header(3, "Palette Configuration");
        h.setStyle("margin-top", "0").setStyle("color", "var(--jettra-accent)").setStyle("margin-bottom", "25px").setStyle("text-align", "center");
        content.add(h);
        
        for(String[] tool : tools) {
            Div row = new Div();
            row.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px").setStyle("padding", "10px").setStyle("border-radius", "8px").setStyle("background", "rgba(255,255,255,0.02)");
            
            UIComponent label = new UIComponent("span") {}; 
            label.setContent(tool[0]);
            label.setStyle("color", "#eee").setStyle("font-weight", "500");
            
            UIComponent cb = new UIComponent("input") {};
            cb.setProperty("type", "checkbox");
            cb.setProperty("checked", "true");
            cb.setProperty("onchange", "toggleToolVisibility('" + tool[2] + "', this.checked)");
            cb.setStyle("width", "20px").setStyle("height", "20px").setStyle("accent-color", "var(--jettra-accent)");
            
            row.add(label).add(cb);
            content.add(row);
        }
        
        Button close = new Button("SAVE & CLOSE");
        close.addClass("j-btn-primary").setStyle("margin-top", "30px").setStyle("width", "100%");
        close.setProperty("onclick", "document.getElementById('tool-settings-modal').style.display='none'");
        content.add(close);
        
        modal.add(content);
        return modal;
    }
}
