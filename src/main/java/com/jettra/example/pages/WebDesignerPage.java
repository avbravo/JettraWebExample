package com.jettra.example.pages;

import com.jettra.example.dashboard.DashboardBasePage;
import io.jettra.wui.complex.Center;
import io.jettra.wui.complex.Modal;
import io.jettra.wui.components.*;

/**
 * JettraWebDesigner: A visual designer for JettraWUI interfaces.
 */
public class WebDesignerPage extends DashboardBasePage {

    // Properties field removed as it was unused

    public WebDesignerPage() {
        super("Jettra Web Designer");
    }
    
    @Override
    protected void onPost(java.util.Map<String, String> params) {
        String code = params.get("generated-code-hidden");
        if (code != null && !code.isEmpty()) {
            System.out.println("======= SAVING GENERATED CODE =======");
            System.out.println(code);
            System.out.println("=====================================");
            // In a real production environment, this would write to a .java file
        }
        super.onPost(params);
    }

    @Override
    protected void initCenter(Center center, String username) {
        // Layout: 3 Columns
        // [ Palette (25%) ] | [ Canvas (50%) ] | [ Code View (25%) ]
        
        Form saveForm = new Form("save-form", "/webdesigner");

        Div container = new Div();
        container.setStyle("display", "flex").setStyle("height", "calc(100vh - 120px)").setStyle("gap", "15px");

        // 1. Sidebar (Palette + Project Explorer)
        Div sidebar = new Div();
        sidebar.setStyle("flex", "0 0 280px").setStyle("display", "flex").setStyle("flex-direction", "column").setStyle("gap", "15px");

        Div palette = createCategorizedPalette();
        palette.setStyle("flex", "1").setStyle("background", "rgba(20,30,50,0.8)").setStyle("border", "1px solid var(--jettra-accent)").setStyle("padding", "15px").setStyle("border-radius", "8px").setStyle("overflow-y", "auto");

        Div projectExplorer = createProjectExplorer();
        projectExplorer.setStyle("flex", "0 0 250px").setStyle("background", "rgba(10,20,30,0.9)").setStyle("border", "1px solid rgba(0,255,255,0.4)").setStyle("padding", "15px").setStyle("border-radius", "8px").setStyle("overflow-y", "auto");

        sidebar.add(palette).add(projectExplorer);

        // 2. Canvas
        Div canvasArea = new Div();
        canvasArea.setProperty("id", "canvas-area");
        canvasArea.setStyle("flex", "1").setStyle("background", "rgba(0,0,0,0.3)").setStyle("border", "2px dashed var(--jettra-accent)").setStyle("border-radius", "8px").setStyle("padding", "20px").setStyle("position", "relative").setStyle("overflow-y", "auto").setStyle("display", "flex").setStyle("flex-direction", "column");
        
        Div canvasHeader = new Div();
        canvasHeader.setProperty("id", "canvas-header");
        canvasHeader.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px").setStyle("border-bottom", "1px solid rgba(0,255,255,0.1)").setStyle("padding-bottom", "10px");
        
        Header canvasTitle = new Header(4, "View");
        canvasTitle.setProperty("id", "canvas-title-text");
        canvasTitle.setStyle("color", "var(--jettra-accent)").setStyle("margin", "0");
        
        Div canvasHeaderActions = new Div();
        canvasHeaderActions.setStyle("display", "flex").setStyle("gap", "10px");
        
        Button previewBtn = new Button("Preview 👁️");
        previewBtn.addClass("j-btn-secondary");
        previewBtn.setStyle("font-size", "11px").setStyle("padding", "5px 12px");
        previewBtn.setProperty("type", "button");
        previewBtn.setProperty("onclick", "window.previewInterface()");

        Button crudBtn = new Button("CRUD ⚡");
        crudBtn.addClass("j-btn-primary");
        crudBtn.setStyle("font-size", "11px").setStyle("padding", "5px 12px");
        crudBtn.setProperty("type", "button");
        crudBtn.setProperty("onclick", "generateCRUD()");
        
        Button toggleCodeBtn = new Button("Code </>");
        toggleCodeBtn.addClass("j-btn");
        toggleCodeBtn.setStyle("font-size", "11px").setStyle("padding", "5px 12px");
        toggleCodeBtn.setProperty("type", "button");
        toggleCodeBtn.setProperty("onclick", "window.toggleCodeView()");
        
        canvasHeaderActions.add(previewBtn).add(crudBtn).add(toggleCodeBtn);
        canvasHeader.add(canvasTitle).add(canvasHeaderActions);
        canvasArea.add(canvasHeader);

        Div canvasDropArea = new Div();
        canvasDropArea.setProperty("id", "canvas-drop-area");
        canvasDropArea.setStyle("flex", "1").setStyle("display", "flex").setStyle("flex-direction", "column");

        Div canvasPlaceholder = new Div();
        canvasPlaceholder.addClass("canvas-placeholder");
        canvasPlaceholder.setContent("Start dragging components here to design your page");
        canvasPlaceholder.setStyle("color", "rgba(0,255,255,0.2)").setStyle("text-align", "center").setStyle("margin-top", "100px");
        canvasDropArea.add(canvasPlaceholder);

        canvasArea.add(canvasDropArea);

        // --- NEW: Modal Tools Area ---
        Div modalToolsArea = new Div();
        modalToolsArea.setProperty("id", "modal-tools-area");
        modalToolsArea.setStyle("margin-top", "15px").setStyle("border-top", "1px dashed rgba(0,255,255,0.3)").setStyle("padding-top", "15px").setStyle("display", "flex").setStyle("flex-direction", "column");
        
        Header modalToolsTitle = new Header(5, "Modal Tools");
        modalToolsTitle.setStyle("color", "var(--jettra-accent)").setStyle("margin", "0 0 10px 0").setStyle("font-size", "12px").setStyle("text-transform", "uppercase");
        
        Div modalListContainer = new Div();
        modalListContainer.setProperty("id", "modal-list-container");
        modalListContainer.setStyle("display", "flex").setStyle("flex-wrap", "wrap").setStyle("gap", "15px").setStyle("min-height", "60px").setStyle("background", "rgba(0,0,0,0.2)").setStyle("border-radius", "8px").setStyle("padding", "10px").setStyle("border", "1px solid rgba(255,255,255,0.05)");
        
        modalToolsArea.add(modalToolsTitle).add(modalListContainer);
        canvasArea.add(modalToolsArea);
        // -----------------------------

        // 3. Code View
        Div codeView = new Div();
        codeView.setProperty("id", "code-view-container");
        codeView.setStyle("flex", "0 0 350px").setStyle("background", "rgba(10,20,30,0.9)").setStyle("border", "1px solid var(--jettra-accent)").setStyle("padding", "15px").setStyle("border-radius", "8px").setStyle("display", "flex").setStyle("flex-direction", "column");
        
        Header codeHeader = new Header(4, "Java Source Code");
        codeHeader.setStyle("color", "var(--jettra-accent)").setStyle("margin-bottom", "10px");
        
        Div codeContainerWrapper = new Div();
        codeContainerWrapper.setStyle("flex", "1").setStyle("display", "flex").setStyle("flex-direction", "column");

        TextArea codeContainer = new TextArea("generated-code-display", "// Drag components to start generating code...");
        codeContainer.setProperty("id", "generated-code-display");
        codeContainer.setStyle("flex", "1").setStyle("background", "#050a10").setStyle("color", "#a9b7c6").setStyle("padding", "10px").setStyle("font-family", "monospace").setStyle("font-size", "11px").setStyle("white-space", "pre").setStyle("border-radius", "4px").setStyle("border", "1px solid #333").setStyle("overflow", "auto").setStyle("resize", "none").setStyle("outline", "none");
        
        codeContainerWrapper.add(codeContainer);

        // Hidden input to send code to server
        TextBox hiddenCode = new TextBox("generated-code-hidden", "");
        hiddenCode.setProperty("id", "generated-code-hidden");
        hiddenCode.setStyle("display", "none");

        Div actions = new Div();
        actions.setStyle("margin-top", "10px").setStyle("display", "flex").setStyle("gap", "10px");
        
        Button syncBtn = new Button("SYNC CANVAS");
        syncBtn.addClass("j-btn-secondary");
        syncBtn.setProperty("type", "button");
        syncBtn.setProperty("onclick", "window.syncCodeToCanvas()");

        Button saveBtn = new Button("GENERATE CLASS");
        saveBtn.addClass("j-btn-primary");
        saveBtn.setProperty("type", "submit");
        
        Button clearBtn = new Button("CLEAR ALL");
        clearBtn.addClass("j-btn-danger");
        clearBtn.setProperty("type", "button");
        clearBtn.setProperty("onclick", "window.clearDesigner()");
        
        actions.add(syncBtn).add(saveBtn).add(clearBtn);
        codeView.add(codeHeader).add(codeContainerWrapper).add(hiddenCode).add(actions);

        // 4. Property Inspector (Floating/Right Sidebar)
        Div inspector = new Div();
        inspector.setProperty("id", "property-inspector");
        inspector.setStyle("flex", "0 0 250px").setStyle("background", "rgba(20,35,55,0.9)").setStyle("border", "1px solid var(--jettra-accent)").setStyle("padding", "15px").setStyle("border-radius", "8px").setStyle("display", "none").setStyle("flex-direction", "column");
        
        Div inspectorHeaderWrapper = new Div();
        inspectorHeaderWrapper.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "10px");
        
        Header inspectorHeader = new Header(4, "Properties");
        inspectorHeader.setStyle("color", "var(--jettra-accent)").setStyle("margin", "0");
        
        Span closeInspector = new Span("×");
        closeInspector.setStyle("color", "#ff4444").setStyle("font-size", "24px").setStyle("cursor", "pointer").setStyle("line-height", "1").setStyle("font-weight", "bold");
        closeInspector.setProperty("onclick", "document.getElementById('property-inspector').style.display='none'");
        
        inspectorHeaderWrapper.add(inspectorHeader).add(closeInspector);
        
        Div propList = new Div();
        propList.setProperty("id", "inspector-properties");
        propList.setStyle("flex", "1");

        inspector.add(inspectorHeaderWrapper).add(propList);

        // --- Add Tool Form (Restored to Palette) ---
        Div addToolPanel = new Div();
        addToolPanel.setStyle("padding", "15px").setStyle("background", "rgba(0,0,0,0.3)").setStyle("border-top", "1px solid var(--jettra-border)").setStyle("margin-top", "10px");
        Header addH = new Header(5, "Añadir Herramienta");
        addH.setStyle("font-size", "11px").setStyle("margin-bottom", "10px");
        
        TextBox toolNameInput = new TextBox("text", "Nueva...");
        toolNameInput.setId("new-tool-name").addClass("j-input").setStyle("margin-bottom", "10px").setStyle("font-size", "12px");
        
        Button addToolBtn = new Button("Añadir a Paleta");
        addToolBtn.addClass("j-btn-primary").setStyle("width", "100%").setStyle("font-size", "11px");
        addToolBtn.setProperty("onclick", "addNewToolToPalette()");
        
        addToolPanel.add(addH).add(toolNameInput).add(addToolBtn);
        palette.add(addToolPanel);

        container.add(sidebar).add(canvasArea).add(codeView).add(inspector);
        
        // 5. Event Editor Modal
        Modal eventModal = new Modal("event-editor-modal");
        eventModal.addClass("modal-3d-effect");
        eventModal.setStyle("display", "none").setStyle("background", "linear-gradient(145deg, rgba(30, 50, 80, 0.98), rgba(15, 25, 45, 1))")
                 .setStyle("backdrop-filter", "blur(25px)").setStyle("padding", "40px").setStyle("border-radius", "30px")
                 .setStyle("width", "750px").setStyle("border", "1px solid rgba(0, 255, 255, 0.5)")
                 .setStyle("position", "fixed").setStyle("top", "50%").setStyle("left", "50%").setStyle("transform", "translate(-50%, -50%)").setStyle("z-index", "1000")
                 .setStyle("box-shadow", "0 40px 100px -20px rgba(0, 0, 0, 0.8), inset 0 2px 2px 0 rgba(255, 255, 255, 0.2), 0 0 30px rgba(0, 255, 255, 0.3)");
        
        Header modalHeader = new Header(3, "⚡ Pro Event Handler Editor");
        modalHeader.setStyle("color", "var(--jettra-accent)").setStyle("margin-top", "0").setStyle("text-shadow", "0 0 15px rgba(0,255,255,0.6)").setStyle("font-weight", "800").setStyle("letter-spacing", "1px");
        
        TextArea codeInput = new TextArea("event-code-input", "e -> { \n    // Write your Java code here\n}");
        codeInput.setStyle("width", "100%").setStyle("height", "350px").setStyle("background", "rgba(0,0,0,0.7)").setStyle("color", "#0ff").setStyle("font-family", "'Fira Code', monospace").setStyle("padding", "20px").setStyle("border", "1px solid rgba(0,255,255,0.3)").setStyle("border-radius", "15px").setStyle("box-shadow", "inset 0 4px 20px rgba(0,0,0,0.7)");
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "15px").setStyle("margin-top", "25px");
        
        Button cancelBtn = new Button("Cancel");
        cancelBtn.addClass("j-btn");
        cancelBtn.setStyle("border-radius", "8px").setStyle("background", "rgba(255,255,255,0.05)").setStyle("box-shadow", "0 4px 6px rgba(0,0,0,0.2)");
        cancelBtn.setProperty("type", "button");
        cancelBtn.setProperty("onclick", "document.getElementById('event-editor-modal').style.display = 'none'");
        
        Button saveEventBtn = new Button("Save Handler");
        saveEventBtn.addClass("j-btn-primary");
        saveEventBtn.setStyle("border-radius", "8px").setStyle("box-shadow", "0 4px 15px rgba(0,255,255,0.3)");
        saveEventBtn.setProperty("onclick", "saveEventHandler()");
        
        modalActions.add(cancelBtn).add(saveEventBtn);
        eventModal.add(modalHeader).add(codeInput).add(modalActions);
        
        // 6. 3D Confirmation Modal
        Modal confirmModal = new Modal("confirm-3d-modal");
        confirmModal.addClass("modal-3d-effect");
        confirmModal.setStyle("display", "none").setStyle("background", "linear-gradient(135deg, #1e293b, #0f172a)")
                   .setStyle("border", "2px solid rgba(0, 255, 255, 0.6)").setStyle("border-radius", "30px")
                   .setStyle("padding", "50px").setStyle("width", "500px")
                   .setStyle("position", "fixed").setStyle("top", "50%").setStyle("left", "50%").setStyle("transform", "translate(-50%, -50%)").setStyle("z-index", "1000")
                   .setStyle("box-shadow", "0 50px 100px -20px rgba(0, 0, 0, 0.8), inset 0 2px 2px rgba(255, 255, 255, 0.1), 0 0 40px rgba(0, 255, 255, 0.2)");
        
        Header confirmTitle = new Header(3, "Confirm Action");
        confirmTitle.setProperty("id", "confirm-title");
        confirmTitle.setStyle("color", "var(--jettra-accent)").setStyle("margin-top", "0").setStyle("text-align", "center");
        
        Paragraph confirmBody = new Paragraph("");
        confirmBody.setProperty("id", "confirm-body");
        confirmBody.setStyle("color", "#cbd5e1").setStyle("text-align", "center").setStyle("font-size", "16px").setStyle("margin", "20px 0");
        
        Div confirmActions = new Div();
        confirmActions.setStyle("display", "flex").setStyle("justify-content", "center").setStyle("gap", "20px").setStyle("margin-top", "30px");
        
        Button confirmNo = new Button("No, Cancel");
        confirmNo.addClass("j-btn");
        confirmNo.setStyle("width", "140px");
        confirmNo.setProperty("onclick", "window.close3DConfirm()");
        
        Button confirmYes = new Button("Yes, Open");
        confirmYes.addClass("j-btn-primary");
        confirmYes.setStyle("width", "140px");
        confirmYes.setProperty("id", "confirm-yes-btn");
        
        confirmActions.add(confirmNo).add(confirmYes);
        confirmModal.add(confirmTitle).add(confirmBody).add(confirmActions);

        // 7. Model Selection Row
        Div modelSelectionRow = new Div();
        modelSelectionRow.setStyle("display", "flex").setStyle("align-items", "center").setStyle("gap", "10px").setStyle("margin-bottom", "15px").setStyle("background", "rgba(0,255,255,0.05)").setStyle("padding", "10px").setStyle("border-radius", "10px").setStyle("border", "1px solid rgba(0,255,255,0.1)");
        
        Label modelLabel = new Label("Selected Model:");
        modelLabel.setStyle("font-size", "12px").setStyle("color", "var(--jettra-accent)");
        
        Div modelSelectContainer = new Div();
        modelSelectContainer.setProperty("id", "model-select-container");
        modelSelectContainer.setStyle("flex", "1");
        
        modelSelectionRow.add(modelLabel).add(modelSelectContainer);
        
        // Wrap everything into the main center
        center.add(eventModal);
        center.add(confirmModal);

        // 8. Preview Modal
        Modal previewModal = new Modal("preview-modal");
        previewModal.setStyle("display", "none").setStyle("background", "linear-gradient(135deg, #0f172a, #1e293b)").setStyle("position", "fixed").setStyle("top", "0").setStyle("left", "0").setStyle("width", "100vw").setStyle("height", "100vh").setStyle("z-index", "9999").setStyle("overflow-y", "auto");
        
        Div previewHeader = new Div();
        previewHeader.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("padding", "15px 30px").setStyle("background", "rgba(0,0,0,0.5)").setStyle("border-bottom", "1px solid rgba(0, 255, 255, 0.2)");
        Header prevTitle = new Header(3, "Interface Preview");
        prevTitle.setStyle("margin", "0").setStyle("color", "var(--jettra-accent)");
        Button closePreviewBtn = new Button("Close Preview ×");
        closePreviewBtn.addClass("j-btn-danger");
        closePreviewBtn.setStyle("font-weight", "bold");
        closePreviewBtn.setProperty("type", "button");
        closePreviewBtn.setProperty("onclick", "document.getElementById('preview-modal').style.display = 'none'");
        previewHeader.add(prevTitle).add(closePreviewBtn);

        Div previewContent = new Div();
        previewContent.setProperty("id", "preview-content-area");
        previewContent.setStyle("padding", "40px").setStyle("max-width", "1200px").setStyle("margin", "0 auto");

        previewModal.add(previewHeader).add(previewContent);
        center.add(previewModal);
        
        saveForm.add(modelSelectionRow).add(container);
        center.add(saveForm);

        // Add Designer Scripts and Styles
        setupDesignerAssets(center);
    }

    private Div createProjectExplorer() {
        Div explorer = new Div();
        
        Div explorerHeaderWrap = new Div();
        explorerHeaderWrap.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "10px");
        
        Header h = new Header(5, "Project Explorer");
        h.setStyle("color", "var(--jettra-accent)").setStyle("margin", "0");
        
        Span clearExplorerBtn = new Span("🗑️");
        clearExplorerBtn.setStyle("cursor", "pointer").setStyle("font-size", "14px").setStyle("transition", "transform 0.2s").setStyle("opacity", "0.8");
        clearExplorerBtn.setProperty("onclick", "window.clearProjectCache()");
        clearExplorerBtn.setProperty("title", "Limpiar selección de proyecto");
        
        explorerHeaderWrap.add(h).add(clearExplorerBtn);
        explorer.add(explorerHeaderWrap);

        FolderSelector folderSel = new FolderSelector("fs-explorer");
        folderSel.setReferenceLocation("/").setReferenceContent("Root");
        folderSel.excludeTarget(true).style3D().setConfirmUpload(true, "Explorador de Proyecto", "¿Desea cargar los archivos de esta carpeta?");
        folderSel.setStyle("width", "100%").setStyle("margin-top", "10px").setStyle("margin-bottom", "10px");
        
        // Custom ID for JS hook
        folderSel.setProperty("onchange", "loadFiles(this)");

        Div treeContainer = new Div();
        treeContainer.setProperty("id", "explorer-tree-view");
        treeContainer.setStyle("min-height", "100px");

        explorer.add(folderSel).add(treeContainer);
        
        // Link FolderSelector to project explorer logic
        folderSel.getFolderInput().setProperty("onchange", "window.loadFiles(this)");
        
        return explorer;
    }

    private Div createCategorizedPalette() {
        Div palette = new Div();
        Header h = new Header(4, "Visual Palette");
        h.setStyle("color", "var(--jettra-accent)").setStyle("margin-bottom", "20px").setStyle("text-align", "center");
        palette.add(h);

        // Typography
        addPaletteCategory(palette, "Typography", new String[]{"Header", "Paragraph", "Span", "Label", "Separator", "Divide"});
        // Forms
        addPaletteCategory(palette, "Forms", new String[]{"Button", "CheckBox", "CheckBoxGroup", "CreditCard", "RadioButton", "RadioGroupButton", "ScheduleControl", "SelectOne", "SelectMany", "SelectOneIcon", "TextBox", "TextArea", "ToggleSwitch", "FileUpload", "FolderSelector", "OTPValidator", "Catcha"});
        // Date
        addPaletteCategory(palette, "Date", new String[]{"DatePicker", "Time", "Calendar", "Schedule", "Organigram", "Timeline"});
        // Navigation
        addPaletteCategory(palette, "Navigation", new String[]{"Link", "Menu", "MenuBar", "MenuItem"});
        // Feedback
        addPaletteCategory(palette, "Feedback", new String[]{"ProgressBar", "Spinner", "Loading", "Alert", "Notification", "Clock"});
        // Media & Files
        addPaletteCategory(palette, "Media", new String[]{"Downloader", "PDFViewer", "ViewMedia", "BarCode", "Draw"});
        // Charts
        addPaletteCategory(palette, "Charts", new String[]{"ChartsBar", "ChartsDoughnut", "ChartsLine", "ChartsPie", "ChartsRadar"});
        // Layout & Display
        addPaletteCategory(palette, "Layout", new String[]{"Grid", "Panel", "Board", "Card", "Avatar", "Carousel", "Table", "TabView", "Tab", "Modal", "Tree", "TreeItem", "Div", "LayoutDisplay", "Map"});

        return palette;
    }

    private void addPaletteCategory(Div palette, String name, String[] components) {
        Div cat = new Div();
        cat.addClass("palette-category");
        cat.setStyle("margin-bottom", "15px");
        
        Header head = new Header(5, name);
        head.setStyle("color", "#0ff").setStyle("font-size", "12px").setStyle("text-transform", "uppercase").setStyle("border-bottom", "1px solid rgba(0,255,255,0.2)").setStyle("padding-bottom", "5px").setStyle("margin-bottom", "10px");
        cat.add(head);

        Div grid = new Div();
        grid.setStyle("display", "grid").setStyle("grid-template-columns", "1fr 1fr").setStyle("gap", "8px");

        for (String comp : components) {
            Div item = new Div();
            item.addClass("palette-item");
            item.setProperty("draggable", "true");
            item.setProperty("ondragstart", "drag(event)");
            item.setProperty("data-type", comp);
            item.setContent(comp);
            item.setStyle("padding", "6px").setStyle("background", "rgba(0,255,255,0.05)").setStyle("border", "1px solid rgba(0,255,255,0.2)").setStyle("border-radius", "4px").setStyle("cursor", "move").setStyle("color", "#eee").setStyle("font-size", "11px").setStyle("text-align", "center").setStyle("transition", "all 0.2s");
            grid.add(item);
        }
        cat.add(grid);
        palette.add(cat);
    }
    private void setupDesignerAssets(Center center) {
        Style style = new Style("""
            .palette-item:hover { background: rgba(0,255,255,0.2) !important; border-color: #0ff !important; color: #fff !important; transform: scale(1.05); }
            .canvas-item { position: relative; margin-bottom: 20px; border: 1px transparent dashed; transition: border 0.2s; min-height: 20px; padding: 10px; border-radius: 4px; cursor: pointer; }
            .canvas-item:hover { border-color: rgba(0,255,255,0.4); background: rgba(0,255,255,0.02); }
            .canvas-item.selected { border-color: var(--jettra-accent) !important; background: rgba(0,255,255,0.05) !important; box-shadow: 0 0 10px var(--jettra-glow); }
            .canvas-item .delete-tool { position: absolute; top: -8px; right: -8px; background: #ff4444; color: white; width: 18px; height: 18px; border-radius: 50%; display: none; justify-content: center; align-items: center; cursor: pointer; font-size: 10px; z-index: 100; box-shadow: 0 2px 5px rgba(0,0,0,0.5); }
            .canvas-item:hover .delete-tool { display: flex; }
            .inspector-row { margin-bottom: 15px; background: rgba(255,255,255,0.02); padding: 8px; border-radius: 6px; border-left: 3px solid transparent; transition: all 0.3s; }
            .inspector-row:hover { border-left-color: var(--jettra-accent); background: rgba(255,255,255,0.05); }
            .inspector-label { display: block; font-size: 11px; color: #9aa; margin-bottom: 6px; text-transform: uppercase; letter-spacing: 0.5px; }
            .inspector-input { width: 100%; background: #0a0f15; border: 1px solid #334155; color: #fff; padding: 8px; border-radius: 6px; font-size: 13px; outline: none; transition: border-color 0.2s; }
            .inspector-input:focus { border-color: var(--jettra-accent); box-shadow: 0 0 0 1px rgba(0,255,255,0.2); }
            .project-file { padding: 6px 10px; font-size: 12px; color: #94a3b8; cursor: pointer; border-radius: 6px; display: flex; align-items: center; gap: 10px; transition: all 0.2s; }
            .project-file:hover { background: rgba(0,255,255,0.1); color: #fff; transform: translateX(5px); }
            .file-page { color: #facc15 !important; font-weight: 600; text-shadow: 0 0 5px rgba(250,204,21,0.2); }
            .file-model { color: #4ade80 !important; font-weight: 600; text-shadow: 0 0 5px rgba(74,222,128,0.2); }
            .canvas-container { border: 2px dashed rgba(255,255,255,0.1); padding: 15px; border-radius: 12px; position: relative; min-height: 80px; transition: all 0.3s; }
            .canvas-container:hover { border-color: var(--jettra-accent); background: rgba(0,255,255,0.02); }
            .view-model-row { margin-bottom: 20px; padding: 12px; background: rgba(0,255,255,0.05); border-radius: 8px; border: 1px solid rgba(0,255,255,0.1); }
            .btn-event { background: linear-gradient(135deg, var(--jettra-accent), #0891b2); color: #000; border: none; padding: 6px 12px; border-radius: 6px; font-size: 11px; weight: 600; cursor: pointer; transition: all 0.2s; display: flex; align-items: center; gap: 5px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.3); }
            .btn-event:hover { transform: translateY(-2px); box-shadow: 0 10px 15px -3px rgba(0,255,255,0.4); }
            .btn-event:active { transform: translateY(0); }
            
            /* Enhanced 3D Styles for Modals */
            .modal-3d-effect {
                transform-style: preserve-3d;
                perspective: 2000px;
                transition: transform 0.1s ease-out, opacity 0.3s ease-out;
                animation: modalDeepAppear 0.5s cubic-bezier(0.175, 0.885, 0.32, 1.275);
            }
            @keyframes modalDeepAppear {
                from { 
                    opacity: 0; 
                    transform: translate(-50%, -30%) scale(0.8) rotateX(-25deg); 
                }
                to { 
                    opacity: 1; 
                    transform: translate(-50%, -50%) scale(1) rotateX(0); 
                }
            }
            
            .canvas-container.modal-container-mock {
                background: rgba(30, 50, 80, 0.3);
                border: 2px solid var(--jettra-accent);
                box-shadow: 0 10px 30px rgba(0,0,0,0.5);
                min-height: 120px;
            }
            
            /* 3D Effects for Modals */
            #confirm-3d-modal, #event-editor-modal {
                transform-style: preserve-3d;
                perspective: 1000px;
                transition: transform 0.1s ease-out, opacity 0.3s ease-out;
                animation: modalAppear 0.4s ease-out;
            }
            .modal-content-3d {
                transform: translateZ(50px);
                transform-style: preserve-3d;
            }
            .btn-3d {
                background: linear-gradient(135deg, var(--jettra-accent), #0891b2);
                border: none;
                box-shadow: 0 4px 0 #044e5e, 0 8px 15px rgba(0,0,0,0.4);
                transition: all 0.1s;
                transform: translateZ(20px);
            }
            .btn-3d:active {
                box-shadow: 0 2px 0 #044e5e, 0 4px 8px rgba(0,0,0,0.4);
                transform: translateY(2px) translateZ(10px);
            }
            @keyframes modalAppear {
                from { opacity: 0; transform: translate(-50%, -60%) scale(0.9) rotateX(-15deg); }
                to { opacity: 1; transform: translate(-50%, -50%) scale(1) rotateX(0); }
            }
            .glass-panel {
                background: linear-gradient(145deg, rgba(30, 41, 59, 0.8), rgba(15, 23, 42, 0.9));
                backdrop-filter: blur(16px);
                border: 1px solid rgba(255, 255, 255, 0.1);
            }
            @keyframes spin { 100% { transform: rotate(360deg); } }
        """);
        
        String scriptPart1 = """
            // Designer GLOBALS
            var selectedItem = null;
            var currentModel = null;
            var modelFields = [];
            var availableModels = [];
            var viewModelName = "BaseViewModel";
            var activeEventProperty = null;
            var projectFilesMap = {};
            window.isSyncing = false;

            // EXPOSE FUNCTIONS TO WINDOW EXPLICITLY
            window.drag = function(ev) {
                ev.dataTransfer.setData("type", ev.currentTarget.getAttribute("data-type"));
                ev.dataTransfer.effectAllowed = "move";
            };

            setInterval(() => {
                document.querySelectorAll('.live-clock').forEach(el => {
                    el.innerText = new Date().toLocaleTimeString();
                });
            }, 1000);

            window.toggleCodeView = function() {
                const cv = document.getElementById('code-view-container');
                if(cv.style.display === 'none') {
                    cv.style.display = 'flex';
                } else {
                    cv.style.display = 'none';
                }
            };

            function setupCanvasHandlers() {
                const canvas = document.getElementById('canvas-drop-area');
                if (!canvas) return;
                
                // Ensure event listeners are attached correctly for capturing drop inside ANY container
                document.body.ondragover = function(ev) {
                    if (ev.target.closest('#canvas-drop-area')) {
                        ev.preventDefault();
                        ev.dataTransfer.dropEffect = "move";
                    }
                };

                document.body.ondrop = function(ev) {
                    const canvasSection = ev.target.closest('#canvas-drop-area');
                    if (!canvasSection) return;
                    
                    ev.preventDefault();
                    ev.stopPropagation();
                    
                    const type = ev.dataTransfer.getData("type");
                    const moveId = ev.dataTransfer.getData("move-id");
                    const target = ev.target.closest('.canvas-container') || canvasSection;
                    
                    if (target) {
                        const targetWrapper = target.closest('.canvas-item');
                        if (targetWrapper && targetWrapper.getAttribute('data-type') === 'RadioGroupButton') {
                            let drpType = type;
                            if (moveId) {
                                const el = document.getElementById(moveId);
                                if (el) drpType = el.getAttribute('data-type');
                            }
                            if (drpType && drpType !== 'RadioButton') {
                                window.show3DMessage("Invalid Action", "Only RadioButton components can be nested inside a RadioGroupButton.");
                                return;
                            }
                        }
                        if (targetWrapper && targetWrapper.getAttribute('data-type') === 'CheckBoxGroup') {
                            let drpType = type;
                            if (moveId) {
                                const el = document.getElementById(moveId);
                                if (el) drpType = el.getAttribute('data-type');
                            }
                            if (drpType && drpType !== 'CheckBox') {
                                window.show3DMessage("Invalid Action", "Only CheckBox components can be nested inside a CheckBoxGroup.");
                                return;
                            }
                        }
                    }

                    if (moveId) {
                        const el = document.getElementById(moveId);
                        if (el && target !== el && !el.contains(target)) {
                            target.appendChild(el);
                            window.updateGeneratedCode();
                        }
                    } else if (type) {
                        window.addComponentToCanvas(type, target);
                    }
                };
            }

            if (document.readyState === 'loading') {
                document.addEventListener('DOMContentLoaded', () => {
                    setupCanvasHandlers();
                    window.restoreFilesFromCache();
                });
            } else {
                setupCanvasHandlers();
                window.restoreFilesFromCache();
            }

            window.addComponentToCanvas = function(type, parent, externalProps) {
                const canvas = document.getElementById('canvas-drop-area');
                const targetParent = parent || canvas;
                if (!targetParent) return;

                const placeholder = document.querySelector('.canvas-placeholder');
                if (placeholder) placeholder.style.display = 'none';

                if (type === 'Modal' && targetParent.id !== 'modal-list-container' && !targetParent.closest('#modal-list-container')) {
                    const modalIdGen = 'modal_' + Math.floor(Math.random()*10000);
                    window.addComponentToCanvas('Button', targetParent, { text: "Open Modal", columns: 2, events: {onClick: `e -> { \\n    document.getElementById("${modalIdGen}").style.display = "block"; \\n}`}, binding: "" });
                    const mt = document.getElementById('modal-list-container');
                    if(mt) window.addComponentToCanvas('Modal', mt, Object.assign({idGen: modalIdGen}, externalProps || {}));
                    return;
                }

                const wrapper = document.createElement('div');
                wrapper.className = 'canvas-item';
                wrapper.id = 'ci_' + Date.now() + Math.floor(Math.random()*1000); // Important for inner dnd
                wrapper.setAttribute('data-type', type);
                
                let defaultProps = externalProps || {text: type, columns: 2, events: {}, binding: ""};
                wrapper.setAttribute('data-props', JSON.stringify(defaultProps));
                wrapper.setAttribute('draggable', 'true');
                
                wrapper.ondragstart = function(ev) {
                    ev.dataTransfer.setData("move-id", ev.target.id);
                    ev.dataTransfer.effectAllowed = "move";
                    ev.stopPropagation(); // Stop parent from reacting
                };
                
                wrapper.onclick = (e) => { e.stopPropagation(); selectElement(wrapper); };
                wrapper.oncontextmenu = (e) => { e.preventDefault(); e.stopPropagation(); selectElement(wrapper); showInspector(); };
                
                let content = '';
                switch(type) {
                    case 'Clock': content = '<div class="j-component live-clock" style="font-size: 20px; font-weight: bold; color: #0f0; background: #000; padding: 10px; border-radius: 8px; border: 2px solid #333; display:inline-block; font-family: monospace;">' + new Date().toLocaleTimeString() + '</div>'; break;
                    case 'Header': content = '<h2 style="margin:0; color:var(--jettra-text);">New Header</h2>'; break;
                    case 'Paragraph': content = '<p style="margin:0; color:var(--jettra-text);">Sample text...</p>'; break;
                    case 'Span': content = '<span style="color:var(--jettra-text);">Span Text</span>'; break;
                    case 'Label': content = '<label style="color:var(--jettra-text); font-weight:bold;">Label Text</label>'; break;
                    case 'Divide': content = '<div style="border-top:1px solid var(--jettra-border); margin:15px 0; width:100%"></div>'; break;
                    case 'Separator': content = '<hr style="border: 0; border-top: 1px dashed var(--jettra-border); margin:15px 0; width:100%;">'; break;
                    case 'Button': content = '<button class="j-btn j-btn-primary" type="button">Interactive Button</button>'; break;
                    case 'TextBox': content = '<input type="text" class="j-input" placeholder="TextBox..." onfocus="this.blur()">'; break;
                    case 'TextArea': content = '<textarea class="j-input" placeholder="TextArea..." rows="3" onfocus="this.blur()"></textarea>'; break;
                    case 'CheckBox': content = '<div style="display:flex; align-items:center; gap:8px;"><input type="checkbox" checked onfocus="this.blur()"/><label>CheckBox</label></div>'; break;
                    case 'RadioButton': content = '<div style="display:flex; align-items:center; gap:8px;"><input type="radio" checked onfocus="this.blur()"/><label>RadioButton</label></div>'; break;
                    case 'RadioGroupButton': content = '<div class="canvas-container" style="padding:10px; border:1px dashed var(--jettra-accent); min-height:60px;"><label style="font-weight:bold; color:var(--jettra-accent); display:block; margin-bottom:10px;">Radio Group</label></div>'; break;
                    case 'CheckBoxGroup': content = '<div class="canvas-container" style="padding:10px; border:1px dashed var(--jettra-accent); min-height:60px;"><label style="font-weight:bold; color:var(--jettra-accent); display:block; margin-bottom:10px;">CheckBox Group</label></div>'; break;
                    case 'ScheduleControl': content = '<input type="datetime-local" class="j-input" onfocus="this.blur()"/>'; break;
                    case 'SelectOne': content = '<select class="j-input" onfocus="this.blur()"><option>Option 1...</option></select><span style="display:none">SelectOne</span>'; break;
                    case 'SelectOneIcon': content = '<select class="j-input" onfocus="this.blur()"><option>⭐ Option 1...</option></select><span style="display:none">SelectOneIcon</span>'; break;
                    case 'Spinner': content = '<div class="j-spinner-wrapper" style="display:inline-flex; align-items:center; border:1px solid var(--jettra-border); border-radius:8px; background:rgba(0,0,0,0.3); overflow:hidden;"><button type="button" class="j-spinner-btn j-spinner-minus" style="width:40px;height:40px;background:rgba(255,255,255,0.05);border:none;color:var(--jettra-accent);font-size:1.2rem;font-weight:bold;">-</button><div class="j-spinner-display" style="min-width:60px;text-align:center;font-family:monospace;font-size:1.1rem;color:var(--jettra-text);">0</div><button type="button" class="j-spinner-btn j-spinner-plus" style="width:40px;height:40px;background:rgba(255,255,255,0.05);border:none;color:var(--jettra-accent);font-size:1.2rem;font-weight:bold;">+</button></div>'; break;
                    case 'ToggleSwitch': content = '<div style="display:flex; align-items:center; gap:8px;"><div style="width:40px;height:20px;background:var(--jettra-accent);border-radius:10px;position:relative;"><div style="width:16px;height:16px;background:#fff;border-radius:50%;position:absolute;top:2px;right:2px;"></div></div><label>ToggleSwitch</label></div>'; break;
                    case 'FileUpload': content = '<div style="border:1px dashed var(--jettra-accent); padding:20px; text-align:center; border-radius:8px; color:var(--jettra-text);"><div style="font-size:24px; margin-bottom:10px;">☁️</div><span>Click or drag files here to upload</span></div>'; break;
                    case 'FolderSelector': content = '<div style="border:1px dashed var(--jettra-accent); padding:20px; text-align:center; border-radius:8px; color:var(--jettra-text);"><div style="font-size:24px; margin-bottom:10px;">📁</div><span>Select Directory</span></div>'; break;
                    case 'OTPValidator': content = '<div class="j-component" style="display:flex; justify-content:center; gap:5px; padding:10px;"><input disabled style="width:30px; height:40px; text-align:center;" value="*"/><input disabled style="width:30px; height:40px; text-align:center;" value="*"/><input disabled style="width:30px; height:40px; text-align:center;" value="*"/><input disabled style="width:30px; height:40px; text-align:center;" value="*"/></div>'; break;
                    case 'Catcha': content = '<div class="j-component" style="padding:10px; border:1px solid #aaa; border-radius:4px; display:inline-flex; align-items:center; gap:10px; background:#f9f9f9;"><input type="checkbox" disabled/> <span style="color:#333; font-family:sans-serif">I\\'m not a robot</span></div>'; break;
                    case 'CreditCard': content = '<div class="j-component" style="padding:15px; border:1px solid rgba(0,255,255,0.2); border-radius:12px; background:linear-gradient(145deg, #1e293b, #0f172a); min-height:100px; display:flex; flex-direction:column; gap:10px; width:280px;"><div style="font-family:monospace; font-size:16px; color:#fff; letter-spacing:2px; margin-top:20px;">•••• •••• •••• ••••</div><div style="display:flex; justify-content:space-between; color:#94a3b8; font-size:10px;"><span class="cc-name-mock">NAME SURNAME</span><span>MM/YY</span></div><button class="j-btn j-btn-primary" style="margin-top:10px; width:100%; border-radius:8px; padding:10px;" disabled>Pay Now</button><div class="canvas-container" style="min-height:30px; border:1px dashed rgba(255,255,255,0.1); margin-top:5px; padding:5px;"></div></div>'; break;
                    case 'Link': content = '<a href="javascript:void(0)" style="color:var(--jettra-accent); text-decoration:underline;"><span>Link Text</span></a>'; break;
                    case 'Menu': content = '<div style="background:rgba(0,0,0,0.4); padding:10px; border-radius:4px; display:inline-block;"><div style="padding:8px 15px; cursor:pointer;"><span>Menu Item</span></div></div>'; break;
                    case 'MenuBar': content = '<div class="canvas-container" style="background:rgba(0,0,0,0.4); padding:10px; border-radius:4px; display:flex; gap:15px; min-height:45px; border:1px dashed rgba(255,255,255,0.2);"></div>'; break;
                    case 'MenuItem': content = '<div class="j-component" style="padding:10px; cursor:pointer; border-bottom:1px solid rgba(255,255,255,0.1); background:rgba(0,0,0,0.2);"><span>Menu Item</span></div>'; break;
                    case 'Spinner': content = '<div class="..." ... ></div>'; break; // removed to fix parsing problem
                    case 'Loading': content = '<div class="j-loading" style="display:inline-flex; align-items:center; justify-content:center; padding:10px;"><svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="var(--jettra-accent)" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 2v4M12 18v4M4.93 4.93l2.83 2.83M16.24 16.24l2.83 2.83M2 12h4M18 12h4M4.93 19.07l2.83-2.83M16.24 7.76l2.83-2.83" /></svg></div>'; break;
                    case 'Alert': content = '<div style="background:rgba(255,0,0,0.1); border-left:4px solid #ff4444; padding:15px; border-radius:4px; color:#ff4444; display:flex; align-items:center; gap:10px;"><b>⚠️</b><span>Alert Message</span></div>'; break;
                    case 'Notification': content = '<div style="background:rgba(0,255,255,0.1); border:1px solid var(--jettra-accent); padding:15px; border-radius:8px; color:var(--jettra-text); max-width:300px; box-shadow:0 4px 12px rgba(0,0,0,0.3);"><span>Notification Message</span></div>'; break;
                    case 'Downloader': content = '<div style="display:inline-flex; align-items:center; gap:8px; padding:6px 12px; background:rgba(255,255,255,0.1); border-radius:4px; cursor:pointer;"><b>💾</b><span>Download File</span></div>'; break;
                    case 'PDFViewer': content = '<div style="border:1px solid #aaa; background:#eee; color:#333; height:200px; display:flex; align-items:center; justify-content:center; border-radius:4px;"><span>PDF Document Preview</span></div>'; break;
                    case 'ViewMedia': content = '<div style="background:#000; color:#fff; height:200px; display:flex; align-items:center; justify-content:center; border-radius:4px;"><span>▶ Media Player</span></div>'; break;
                    case 'BarCode': content = '<div class="j-component" style="padding:10px; text-align:center; border:1px dashed #fff;"><span style="font-family:monospace; font-size:24px; letter-spacing:2px">||| | || |||</span><br><span>BarCode</span></div>'; break;
                    case 'Carousel': content = '<div style="display:flex; gap:10px; overflow:hidden; padding:10px; background:rgba(0,0,0,0.2); border-radius:8px;"><div style="width:150px; height:80px; background:rgba(0,255,255,0.1); border:1px solid rgba(0,255,255,0.3); border-radius:8px; display:flex; align-items:center; justify-content:center;"><span>Slide 1</span></div></div>'; break;
                    case 'TabView': content = '<div style="border:1px solid rgba(0,255,255,0.2); border-radius:8px; overflow:hidden;"><div style="display:flex; background:rgba(0,0,0,0.3); border-bottom:1px solid rgba(0,255,255,0.2);" class="tab-headers"><span style="padding:10px; color:#aaa; font-size:11px;">[Tab Headers]</span></div><div class="canvas-container" style="padding:10px; min-height:100px; background:rgba(0,255,255,0.02);"></div></div>'; break;
                    case 'Tab': content = '<div class="canvas-container" style="border:1px dashed var(--jettra-accent); min-height:80px; padding:10px; position:relative; background:rgba(0,0,0,0.4); margin-bottom:10px;"><span style="position:absolute; top:-12px; left:10px; background:var(--jettra-accent); color:#000; padding:2px 8px; border-radius:4px; font-size:10px; font-weight:bold;">Tab Title</span></div>'; break;
                    case 'Tree': content = '<div class="canvas-container" style="padding:10px; border:1px solid rgba(255,255,255,0.1); border-radius:4px; background:rgba(0,0,0,0.2); min-height:50px;"></div>'; break;
                    case 'TreeItem': content = '<div class="canvas-container" style="padding-left:15px; border-left:1px dashed rgba(0,255,255,0.2); min-height:30px; margin-top:5px; position:relative;"><span style="position:absolute; left:-10px; top:5px; font-size:10px; color:var(--jettra-accent);">▶</span><span style="display:inline-block; margin-bottom:5px; color:#fff;">Tree Node</span></div>'; break;
                    case 'Div': content = '<div class="canvas-container" style="border:1px dashed rgba(255,255,255,0.2); min-height:50px; padding:10px; border-radius:4px; position:relative;"><span style="position:absolute;top:2px;left:5px;font-size:9px;color:rgba(255,255,255,0.3)">Div Container</span></div>'; break;
                    case 'LayoutDisplay': content = '<div class="canvas-container" style="border:2px solid rgba(0,255,255,0.2); min-height:100px; padding:15px; border-radius:8px; position:relative;"><span style="position:absolute;top:5px;left:10px;font-size:10px;color:rgba(0,255,255,0.5);font-weight:bold;">LayoutDisplay</span></div>'; break;
                    case 'DatePicker': content = '<div style="display:flex; flex-direction:column; gap:5px;"><label style="font-weight:500; font-size:0.9rem;">Date Selection</label><input type="datetime-local" class="j-input" onfocus="this.blur()"/></div>'; break;
                    case 'Time': content = '<div style="display:flex; flex-direction:column; gap:5px;"><label style="font-weight:500; font-size:0.9rem;">Time Selection</label><input type="time" class="j-input" onfocus="this.blur()"/></div>'; break;
                    case 'Map': content = '<div style="width:100%; height:200px; background:rgba(0,255,0,0.1); border:1px dashed #0f0; border-radius:8px; display:flex; align-items:center; justify-content:center; color:#0f0;"><span>🗺️ Leaflet Map Area</span></div>'; break;
                    case 'Calendar': content = '<div style="width:100%; min-height:150px; background:rgba(0,0,0,0.3); border:1px solid var(--jettra-border); border-radius:8px; padding:10px;"><div style="text-align:center; color:var(--jettra-accent); margin-bottom:5px;">[Month Calendar Placeholder]</div><div style="display:grid; grid-template-columns:repeat(7, 1fr); gap:2px; opacity:0.5;"><div style="background:#222;height:20px;"></div><div style="background:#222;height:20px;"></div><div style="background:#222;height:20px;"></div><div style="background:#222;height:20px;"></div><div style="background:#222;height:20px;"></div><div style="background:#222;height:20px;"></div><div style="background:#222;height:20px;"></div></div></div>'; break;
                    case 'Schedule': content = '<div style="width:100%; min-height:100px; background:rgba(0,0,0,0.3); border:1px solid var(--jettra-border); border-radius:8px; padding:10px;"><div style="text-align:center; color:var(--jettra-accent); margin-bottom:5px;">[Weekly Schedule Placeholder]</div><div style="border-left:2px solid var(--jettra-border); padding-left:10px;"><div style="background:var(--jettra-accent); color:#000; font-size:10px; width:80px; padding:2px; margin-bottom:5px;">Event</div></div></div>'; break;
                    case 'Timeline': content = '<div style="border-left:2px solid var(--jettra-border); padding-left:20px; position:relative; min-height:100px;"><div style="position:absolute; left:-6px; top:10px; width:10px; height:10px; border-radius:50%; background:var(--jettra-accent);"></div><div style="background:rgba(255,255,255,0.05); padding:10px; border-radius:4px; margin-top:5px;">Timeline Node</div></div>'; break;
                    case 'Organigram': content = '<div style="text-align:center; padding:10px;"><div style="display:inline-block; padding:10px; border:1px solid var(--jettra-accent); border-radius:6px; background:rgba(0,255,255,0.05);">Root Node</div><div style="border-left:1px dashed var(--jettra-border); height:20px; margin:0 auto; width:1px;"></div><div style="display:inline-block; padding:10px; border:1px solid var(--jettra-border); border-radius:6px; opacity:0.8;">Child Node</div></div>'; break;
                    case 'Panel': 
                        content = '<div class="j-component j-panel" style="padding:0; overflow:hidden;"><div class="j-panel-header" style="background:rgba(0,255,255,0.05); padding:10px 15px; border-bottom:1px solid var(--jettra-border); font-weight:bold; color:var(--jettra-accent)">Panel Title</div><div class="canvas-container j-panel-body" style="padding:15px; display:flex; flex-direction:column; gap:10px; min-height:50px;"></div></div>'; 
                        break;
                    case 'Grid':
                        content = '<div class="canvas-container" style="display:grid; grid-template-columns:1fr 1fr; gap:15px; min-height:50px;"></div>'; 
                        break;
                    case 'Modal':
                        content = '<div class="canvas-container modal-container-mock j-component" style="padding:20px; border-radius:12px; min-height:100px; transform:translateZ(10px); box-shadow:0 15px 40px rgba(0,0,0,0.5);"><h3 style="margin-top:0; color:var(--jettra-accent); border-bottom:1px solid rgba(0,255,255,0.2); padding-bottom:10px;">Modal Component</h3></div>';
                        break;
                    case 'Board':
                        content = '<div class="canvas-container board-container-mock j-component" style="padding:20px; background:rgba(15,23,42,0.4);"><h2 style="margin-top:0; color:var(--jettra-accent); font-size:18px; margin-bottom:15px;">New Board</h2><div style="display:grid; grid-template-columns:1fr 1fr 1fr; gap:15px; min-height:100px;"><div class="canvas-container j-component j-panel" style="padding:10px; min-height:80px; box-shadow:none;"></div><div class="canvas-container j-component j-panel" style="padding:10px; min-height:80px; box-shadow:none;"></div><div class="canvas-container j-component j-panel" style="padding:10px; min-height:80px; box-shadow:none;"></div></div></div>';
                        break;
                    case 'Card':
                        content = '<div class="j-component j-card" style="padding:0; border:1px solid var(--jettra-accent); border-radius:8px; background:rgba(0,0,0,0.4); display:flex; flex-direction:column; gap:10px;"><div style="padding:15px; border-bottom:1px solid rgba(255,255,255,0.1);"><h3 style="margin:0;color:var(--jettra-accent);" class="card-title-mock">Card Title</h3><p style="margin:0;font-size:12px;color:#aaa" class="card-subtitle-mock"></p><p style="margin:5px 0 0;font-size:13px;color:rgba(255,255,255,0.8)" class="card-content-mock"></p></div><div class="canvas-container" style="flex:1; border:0; min-height:50px; padding:15px; background:transparent;"></div></div>';
                        break;
                    case 'Table':
                        content = '<div class="j-table-container j-component" style="padding:0; border-radius:12px; overflow:hidden;"><table style="width:100%; border-collapse:collapse; text-align:left; font-size:0.9rem;"><thead style="background:rgba(0,255,255,0.05); border-bottom:1px solid var(--jettra-accent);"><tr><th style="padding:12px; color:var(--jettra-text);">Col 1</th><th style="padding:12px; color:var(--jettra-text);">Col 2</th></tr></thead><tbody><tr style="border-bottom:1px solid var(--jettra-border);"><td style="padding:10px; color:var(--jettra-text);">Data 1</td><td style="padding:10px; color:var(--jettra-text);">Data 2</td></tr></tbody></table></div>';
                        break;
                    case 'Image':
                        content = '<div class="j-component" style="padding:0; overflow:hidden; display:flex; align-items:center; justify-content:center; background:rgba(0,0,0,0.2); min-height:100px; border-radius:8px;"><span style="color:#666;">[Image Placeholder]</span></div>';
                        break;
                    case 'Avatar':
                        content = '<div class="j-avatar j-avatar-circle j-avatar-md" style="background:var(--jettra-accent); color:#000; display:flex; align-items:center; justify-content:center; font-weight:bold">AV</div>';
                        break;
                    case 'ProgressBar':
                        content = '<div class="j-progressbar-container" style="width:100%; height:12px; background:rgba(255,255,255,0.05); border-radius:6px; overflow:hidden; border:1px solid var(--jettra-border)">' +
                                  '<div class="j-progressbar-fill" style="width:60%; height:100%; background:var(--jettra-accent); box-shadow:0 0 10px var(--jettra-accent)"></div>' +
                                  '</div>';
                        break;
                    case 'ChartsBar': content = '<div class="j-component" style="display:flex;align-items:center;justify-content:center;background:rgba(0,0,0,0.2);min-height:100px;border-radius:8px;border:1px solid #36a2eb"><span style="color:#36a2eb;font-size:2rem;">📊 Bar Chart</span></div>'; break;
                    case 'ChartsDoughnut': content = '<div class="j-component" style="display:flex;align-items:center;justify-content:center;background:rgba(0,0,0,0.2);min-height:100px;border-radius:50%;border:2px dashed #ff6384;width:100px;margin:auto;"><span style="color:#ff6384;font-size:2rem;">🍩</span></div>'; break;
                    case 'ChartsLine': content = '<div class="j-component" style="display:flex;align-items:center;justify-content:center;background:rgba(0,0,0,0.2);min-height:100px;border-radius:8px;border:1px solid #4bc0c0"><span style="color:#4bc0c0;font-size:2rem;">📈 Line Chart</span></div>'; break;
                    case 'ChartsPie': content = '<div class="j-component" style="display:flex;align-items:center;justify-content:center;background:rgba(0,0,0,0.2);min-height:100px;border-radius:50%;border:1px solid #ffcd56;width:100px;margin:auto;"><span style="color:#ffcd56;font-size:2rem;">🥧</span></div>'; break;
                    case 'ChartsRadar': content = '<div class="j-component" style="display:flex;align-items:center;justify-content:center;background:rgba(0,0,0,0.2);min-height:100px;border-radius:8px;border:1px solid #9966ff"><span style="color:#9966ff;font-size:2rem;">🕸️ Radar Chart</span></div>'; break;
                    default: content = `<div class="canvas-container j-component" style="padding:10px; text-align:center; color:#888;">${type} Component</div>`;
                }

                wrapper.innerHTML = content + '<div class="delete-tool" onclick="this.parentElement.remove(); window.updateGeneratedCode();">×</div>';
                targetParent.appendChild(wrapper);
                window.updateGeneratedCode();
            };

            window.selectElement = function(el) {
                if (selectedItem) selectedItem.classList.remove('selected');
                selectedItem = el;
                selectedItem.classList.add('selected');
                window.updateInspector();
            };

            window.showInspector = function() { 
                const inspector = document.getElementById('property-inspector');
                if (inspector) inspector.style.display = 'flex'; 
            };

            window.updateInspector = function() {
                if (!selectedItem) return;
                const type = selectedItem.getAttribute('data-type');
                const props = JSON.parse(selectedItem.getAttribute('data-props'));
                const inspector = document.getElementById('inspector-properties');
                if (!inspector) return;
                
                let html = `
                    <div class="view-model-row">
                        <span class="inspector-label">Active View Model</span>
                        <select class="inspector-input" onchange="selectModel(this.value)">
                            <option value="">None</option>
                            ${availableModels.map(m => `<option value="${m.name}" ${viewModelName === m.name ? 'selected' : ''}>${m.name}</option>`).join('')}
                        </select>
                    </div>
                `;

                html += `
                    <div class="inspector-row">
                        <span class="inspector-label">Component Type</span>
                        <div style="color:var(--jettra-accent); font-weight:bold">${type}</div>
                    </div>
                    <div class="inspector-row">
                        <span class="inspector-label">ID (Variables)</span>
                        <input type="text" class="inspector-input" value="${props.id || ""}" placeholder="Comp ID" onchange="updateProp('id', this.value)">
                    </div>
                    <div class="inspector-row">
                        <span class="inspector-label">Text Content</span>
                        <input type="text" class="inspector-input" value="${props.text || ""}" onchange="updateProp('text', this.value)">
                    </div>
                `;

                if (type === 'SelectOne') {
                    html += `
                        <div class="inspector-row">
                            <span class="inspector-label">Options (comma separated)</span>
                            <input type="text" class="inspector-input" value="${props.options || ""}" placeholder="Opt1, Opt2, Opt3..." onchange="updateProp('options', this.value)">
                        </div>
                    `;
                } else if (type === 'SelectOneIcon') {
                    html += `
                        <div class="inspector-row">
                            <span class="inspector-label">Options (val|label|icon, ...)</span>
                            <input type="text" class="inspector-input" value="${props.options || ""}" placeholder="en|English|🇺🇸, es|Spanish|🇪🇸..." onchange="updateProp('options', this.value)">
                        </div>
                    `;
                }

                // Global icon picker
                const iconComponents = ['Button', 'SelectOneIcon', 'Menu', 'MenuItem', 'Avatar', 'Alert', 'Notification', 'Link', 'Tab', 'TreeItem'];
                if (iconComponents.includes(type)) {
                    html += `
                        <div class="inspector-row">
                            <span class="inspector-label">Icon</span>
                            <div style="display:grid; grid-template-columns: repeat(5, 1fr); gap:5px; margin-top:5px;">
                                ${['👤','👥','🏠','⚙️','🔔','📧','📁','📊','🚀','🌙','☀️','➕','✏️','🗑️','✅'].map(icon => 
                                    `<div class="icon-preset" style="cursor:pointer; padding:5px; text-align:center; border:1px solid ${props.icon === icon ? 'var(--jettra-accent)' : 'rgba(255,255,255,0.1)'}" onclick="updateProp('icon', '${icon}')">${icon}</div>`
                                ).join('')}
                            </div>
                            <input type="text" class="inspector-input" style="margin-top:5px" placeholder="Custom icon/unicode" value="${props.icon || ""}" onchange="updateProp('icon', this.value)">
                        </div>
                    `;
                }

                if (type === 'Button') {
                    html += `
                        <div class="inspector-row">
                            <span class="inspector-label">Button Style</span>
                            <select class="inspector-input" onchange="updateProp('btnStyle', this.value)">
                                <option value="PRIMARY" ${props.btnStyle === 'PRIMARY' ? 'selected' : ''}>PRIMARY</option>
                                <option value="SECONDARY" ${props.btnStyle === 'SECONDARY' ? 'selected' : ''}>SECONDARY</option>
                                <option value="HELP" ${props.btnStyle === 'HELP' ? 'selected' : ''}>HELP</option>
                                <option value="DANGER" ${props.btnStyle === 'DANGER' ? 'selected' : ''}>DANGER</option>
                                <option value="INFO" ${props.btnStyle === 'INFO' ? 'selected' : ''}>INFO</option>
                            </select>
                        </div>
                    `;
                }

                if (type === 'TextBox' || type === 'Label') {
                    html += `
                        <div class="inspector-row">
                            <span class="inspector-label">Bind to Model Field</span>
                            <select class="inspector-input" onchange="updateProp('binding', this.value)">
                                <option value="">No Binding</option>
                                ${modelFields.map(f => `<option value="${f}" ${props.binding === f ? 'selected' : ''}>${f}</option>`).join('')}
                            </select>
                        </div>
                    `;
                }

                if (type === 'Panel' || type === 'Grid') {
                    html += `
                        <div class="inspector-row">
                            <span class="inspector-label">Columns</span>
                            <input type="number" class="inspector-input" value="${props.columns || 2}" min="1" max="12" onchange="updateProp('columns', this.value)">
                        </div>
                    `;
                }

                if (type === 'Map') {
                    html += `
                        <div class="inspector-row">
                            <span class="inspector-label">Initial Latitude (lat)</span>
                            <input type="number" step="0.0001" class="inspector-input" value="${props.lat || 40.7128}" placeholder="e.g. 40.7128" onchange="updateProp('lat', this.value)">
                        </div>
                        <div class="inspector-row">
                            <span class="inspector-label">Initial Longitude (lng)</span>
                            <input type="number" step="0.0001" class="inspector-input" value="${props.lng || -74.0060}" placeholder="e.g. -74.0060" onchange="updateProp('lng', this.value)">
                        </div>
                        <div class="inspector-row">
                            <span class="inspector-label">Zoom Level</span>
                            <input type="number" class="inspector-input" value="${props.zoom || 13}" min="1" max="25" onchange="updateProp('zoom', this.value)">
                        </div>
                        <div class="inspector-row">
                            <span class="inspector-label">Marker Title</span>
                            <input type="text" class="inspector-input" value="${props.marker || ''}" placeholder="Main Location" onchange="updateProp('marker', this.value)">
                        </div>
                        <div class="inspector-row">
                            <span class="inspector-label">Enable Search</span>
                            <select class="inspector-input" onchange="updateProp('enableSearch', this.value === 'true')">
                                <option value="false" ${props.enableSearch ? '' : 'selected'}>False</option>
                                <option value="true" ${props.enableSearch ? 'selected' : ''}>True</option>
                            </select>
                        </div>
                        <div class="inspector-row">
                            <span class="inspector-label">Enable Relief</span>
                            <select class="inspector-input" onchange="updateProp('enableRelief', this.value === 'true')">
                                <option value="false" ${props.enableRelief ? '' : 'selected'}>False</option>
                                <option value="true" ${props.enableRelief ? 'selected' : ''}>True</option>
                            </select>
                        </div>
                    `;
                }

                if (type === 'Spinner') {
                    html += `
                        <div class="inspector-row">
                            <span class="inspector-label">Value</span>
                            <input type="number" step="0.1" class="inspector-input" value="${props.value || 0}" onchange="updateProp('value', this.value)">
                        </div>
                        <div class="inspector-row">
                            <span class="inspector-label">Min</span>
                            <input type="number" step="0.1" class="inspector-input" value="${props.min || 0}" onchange="updateProp('min', this.value)">
                        </div>
                        <div class="inspector-row">
                            <span class="inspector-label">Max</span>
                            <input type="number" step="0.1" class="inspector-input" value="${props.max || 100}" onchange="updateProp('max', this.value)">
                        </div>
                        <div class="inspector-row">
                            <span class="inspector-label">Step</span>
                            <input type="number" step="0.1" class="inspector-input" value="${props.step || 1}" onchange="updateProp('step', this.value)">
                        </div>
                    `;
                }

                if (type === 'RadioGroupButton' || type === 'CheckBoxGroup') {
                    html += `
                        <div class="inspector-row">
                            <span class="inspector-label">Group Name</span>
                            <input type="text" class="inspector-input" value="${props.name || (type === "CheckBoxGroup" ? "checkboxGroup1" : "radioGroup1")}" onchange="updateProp('name', this.value)">
                        </div>
                    `;
                }

                if (type.startsWith('Chart')) {
                    html += `
                        <div class="inspector-row">
                            <span class="inspector-label">Labels (comma sep.)</span>
                            <input type="text" class="inspector-input" value="${props.labels || "Label 1, Label 2"}" onchange="updateProp('labels', this.value)">
                        </div>
                        <div class="inspector-row">
                            <span class="inspector-label">Dataset Label</span>
                            <input type="text" class="inspector-input" value="${props.datasetLabel || "Data"}" onchange="updateProp('datasetLabel', this.value)">
                        </div>
                        <div class="inspector-row">
                            <span class="inspector-label">Data (comma sep. numbers)</span>
                            <input type="text" class="inspector-input" value="${props.data || "10, 20"}" onchange="updateProp('data', this.value)">
                        </div>
                        <div class="inspector-row">
                            <span class="inspector-label">Bg Color</span>
                            <input type="color" class="inspector-input" value="${props.bgColor || "#00ffff"}" onchange="updateProp('bgColor', this.value)">
                        </div>
                    `;
                }

                if (type === 'ScheduleControl') {
                    html += `
                        <div class="inspector-row">
                            <span class="inspector-label">Initial View</span>
                            <select class="inspector-input" onchange="updateProp('view', this.value)">
                                <option value="day" ${props.view === 'day' ? 'selected' : ''}>Day</option>
                                <option value="week" ${props.view === 'week' ? 'selected' : ''}>Week</option>
                                <option value="month" ${props.view === 'month' ? 'selected' : ''}>Month</option>
                            </select>
                        </div>
                    `;
                }

                if (type === 'Card') {
                    html += `
                        <div class="inspector-row"><span class="inspector-label">Title</span><input type="text" class="inspector-input" value="${props.title || ''}" onchange="updateProp('title', this.value)"></div>
                        <div class="inspector-row"><span class="inspector-label">Subtitle</span><input type="text" class="inspector-input" value="${props.subtitle || ''}" onchange="updateProp('subtitle', this.value)"></div>
                        <div class="inspector-row"><span class="inspector-label">Content</span><textarea class="inspector-input" onchange="updateProp('content', this.value)">${props.content || ''}</textarea></div>
                        <div class="inspector-row"><span class="inspector-label">Image URL</span><input type="text" class="inspector-input" value="${props.imageUrl || ''}" onchange="updateProp('imageUrl', this.value)"></div>
                    `;
                }

                if (type === 'QR') {
                    html += `
                        <div class="inspector-row"><span class="inspector-label">Text</span><input type="text" class="inspector-input" value="${props.text || 'https://jettra.io'}" onchange="updateProp('text', this.value)"></div>
                        <div class="inspector-row"><span class="inspector-label">Width</span><input type="number" class="inspector-input" value="${props.width || 128}" onchange="updateProp('width', this.value)"></div>
                        <div class="inspector-row"><span class="inspector-label">Height</span><input type="number" class="inspector-input" value="${props.height || 128}" onchange="updateProp('height', this.value)"></div>
                        <div class="inspector-row"><span class="inspector-label">Dark Color</span><input type="color" class="inspector-input" value="${props.colorDark || '#000000'}" onchange="updateProp('colorDark', this.value)"></div>
                        <div class="inspector-row"><span class="inspector-label">Light Color</span><input type="color" class="inspector-input" value="${props.colorLight || '#ffffff'}" onchange="updateProp('colorLight', this.value)"></div>
                    `;
                }

                if (type === 'BarCode') {
                    html += `
                        <div class="inspector-row"><span class="inspector-label">Text</span><input type="text" class="inspector-input" value="${props.text || '123456789012'}" onchange="updateProp('text', this.value)"></div>
                        <div class="inspector-row"><span class="inspector-label">Format</span><input type="text" class="inspector-input" value="${props.format || 'CODE128'}" onchange="updateProp('format', this.value)"></div>
                        <div class="inspector-row"><span class="inspector-label">Line Color</span><input type="color" class="inspector-input" value="${props.lineColor || '#000000'}" onchange="updateProp('lineColor', this.value)"></div>
                    `;
                }

                if (type === 'OTPValidator') {
                    html += `
                        <div class="inspector-row"><span class="inspector-label">Digits</span><input type="number" class="inspector-input" value="${props.amountOfDigits || 6}" onchange="updateProp('amountOfDigits', this.value)"></div>
                    `;
                }

                if (type === 'Catcha') {
                    html += `
                        <div class="inspector-row"><span class="inspector-label">Images To Validate</span><input type="number" class="inspector-input" value="${props.amountOfImagesToValidate || 3}" onchange="updateProp('amountOfImagesToValidate', this.value)"></div>
                    `;
                }

                if (type === 'CreditCard') {
                    html += `
                        <div class="inspector-row"><span class="inspector-label">Form Action</span><input type="text" class="inspector-input" value="${props.formAction || ''}" onchange="updateProp('formAction', this.value)"></div>
                        <div class="inspector-row"><span class="inspector-label">Submit Text</span><input type="text" class="inspector-input" value="${props.submitText || 'Pay Now'}" onchange="updateProp('submitText', this.value)"></div>
                    `;
                }

                if (type === 'Clock') {
                    html += `
                        <div class="inspector-row" style="display:flex; justify-content:space-between; align-items:center;">
                            <span class="inspector-label">Show Remaining Time</span>
                            <input type="checkbox" ${props.showTimeRemaining ? 'checked' : ''} onchange="updateProp('showTimeRemaining', this.checked)">
                        </div>
                        <div class="inspector-row">
                            <span class="inspector-label">On Time Reached (JS)</span>
                            <input type="text" class="inspector-input" value="${props.onTimeReached || ""}" placeholder="alert('Time is up!');" onchange="updateProp('onTimeReached', this.value)">
                        </div>
                    `;
                }

                // Common properties
                html += `
                    <div style="margin-top:15px; border-top:1px solid rgba(255,255,255,0.1); padding-top:10px">
                        <div class="inspector-row">
                            <span class="inspector-label">Update (IDs)</span>
                            <input type="text" class="inspector-input" value="${props.update || ""}" onchange="updateProp('update', this.value)">
                        </div>
                    </div>
                `;

                if (type === 'Avatar') {
                    html += `
                        <div class="inspector-row">
                            <span class="inspector-label">Avatar Text</span>
                            <input type="text" class="inspector-input" value="${props.text || ""}" onchange="updateProp('text', this.value)">
                        </div>
                    `;
                }

                if (type === 'ProgressBar') {
                    html += `
                        <div class="inspector-row">
                            <span class="inspector-label">Value</span>
                            <input type="number" class="inspector-input" value="${props.value || 60}" onchange="updateProp('value', this.value)">
                        </div>
                        <div class="inspector-row">
                            <span class="inspector-label">Max</span>
                            <input type="number" class="inspector-input" value="${props.max || 100}" onchange="updateProp('max', this.value)">
                        </div>
                        <div class="inspector-row">
                            <span class="inspector-label">Color</span>
                            <input type="color" class="inspector-input" value="${props.color || '#00ffff'}" onchange="updateProp('color', this.value)">
                        </div>
                        <div class="inspector-row" style="display:flex; justify-content:space-between; align-items:center;">
                            <span class="inspector-label">Indeterminate</span>
                            <input type="checkbox" ${props.indeterminate ? 'checked' : ''} onchange="updateProp('indeterminate', this.checked)">
                        </div>
                        <div class="inspector-row" style="display:flex; justify-content:space-between; align-items:center;">
                            <span class="inspector-label">Show %</span>
                            <input type="checkbox" ${props.showPercent !== false ? 'checked' : ''} onchange="updateProp('showPercent', this.checked)">
                        </div>
                    `;
                }

                const supportedEvents = ['onClick', 'onChange', 'onAction'];
                html += `
                    <div style="margin-top:20px; border-top:1px solid rgba(0,255,255,0.1); padding-top:15px">
                        <span class="inspector-label" style="color:var(--jettra-accent); font-weight:bold">⚡ Event Handlers</span>
                        <div style="display:flex; flex-direction:column; gap:8px; margin-top:10px">
                            ${supportedEvents.map(ev => `
                                <div class="inspector-row" style="cursor:pointer; display:flex; justify-content:space-between; align-items:center" onclick="window.openEventEditor('${ev}')">
                                    <span style="font-size:11px; color:#ccc">${ev}</span>
                                    <span style="font-size:10px; color:var(--jettra-accent)">Configure ⇢</span>
                                </div>
                            `).join('')}
                        </div>
                    </div>
                `;

                inspector.innerHTML = html;
            };

            window.openEventEditor = function(evName) {
                if (!selectedItem) return;
                activeEventProperty = evName;
                const props = JSON.parse(selectedItem.getAttribute('data-props'));
                const currentCode = props.events[evName] || "e -> { \\n    // Enter code here \\n}";
                
                document.getElementById('event-code-input').value = currentCode.replace(/\\\\n/g, '\\n');
                const modal = document.getElementById('event-editor-modal');
                modal.style.display = 'block';
                window.apply3DTracking(modal);
            };

            window.saveEventHandler = function() {
                if (!selectedItem || !activeEventProperty) return;
                const props = JSON.parse(selectedItem.getAttribute('data-props'));
                const newCode = document.getElementById('event-code-input').value;
                props.events[activeEventProperty] = newCode.replace(/\\n/g, '\\\\n');
                selectedItem.setAttribute('data-props', JSON.stringify(props));
                
                document.getElementById('event-editor-modal').style.display = 'none';
                window.updateGeneratedCode();
                window.updateInspector();
            };

            window.parseModelFieldsContent = function(content) {
                const fieldRegex = /private\\s+([\\w<>]+)\\s+(\\w+);/g;
                let match;
                modelFields = [];
                while ((match = fieldRegex.exec(content)) !== null) {
                    modelFields.push({ type: match[1], name: match[2] });
                }
                window.updateInspector();
                window.updateGeneratedCode();
            };

            window.selectModel = function(name) {
                viewModelName = name;
                const model = availableModels.find(m => m.name === name);
                if (model) {
                    currentModel = model;
                    window.parseModelFieldsContent(model.content);
                    window.show3DMessage("Model Attached", "Se ha vinculado el modelo " + name + " a la vista.");
                } else {
                    modelFields = [];
                    window.updateInspector();
                    window.updateGeneratedCode();
                }
            };

            window.updateProp = function(key, value) {
                if (!selectedItem) return;
                const props = JSON.parse(selectedItem.getAttribute('data-props'));
                props[key] = value;
                selectedItem.setAttribute('data-props', JSON.stringify(props));
                
                const type = selectedItem.getAttribute('data-type');
                if (key === 'text') {
                    if (type === 'TextBox' || type === 'TextArea') {
                        const inEl = selectedItem.querySelector('input, textarea');
                        if (inEl) {
                            inEl.placeholder = value;
                            inEl.value = value;
                        }
                    } else if (type === 'Button') {
                        const el = selectedItem.querySelector('button');
                        if (el) el.innerHTML = (props.icon ? props.icon + ' ' : '') + value;
                    } else {
                        const el = selectedItem.querySelector('h3, h2, h1, p, label, .j-avatar, .j-panel-header, span');
                        if (el) el.innerText = value;
                        if (type === 'Avatar') {
                            props.icon = "";
                            selectedItem.setAttribute('data-props', JSON.stringify(props));
                            setTimeout(window.updateInspector, 10);
                        }
                    }
                }
                
                if (key === 'icon') {
                    if (type === 'Avatar') {
                        const el = selectedItem.querySelector('.j-avatar');
                        if (el) el.innerHTML = value;
                        props.text = "";
                        selectedItem.setAttribute('data-props', JSON.stringify(props));
                        setTimeout(window.updateInspector, 10);
                    } else if (type === 'Button') {
                        const el = selectedItem.querySelector('button');
                        if (el) el.innerHTML = (value ? value + ' ' : '') + (props.text || 'Interactive Button');
                    }
                }

                if (key === 'btnStyle' && type === 'Button') {
                    const el = selectedItem.querySelector('button');
                    if (el) {
                        el.className = 'j-btn'; // reset
                        el.classList.add('j-btn-' + value.toLowerCase());
                    }
                }

                if (type === 'Card') {
                    if (key === 'title') { const tEl = selectedItem.querySelector('.card-title-mock'); if (tEl) tEl.innerText = value; }
                    if (key === 'subtitle') { const sEl = selectedItem.querySelector('.card-subtitle-mock'); if (sEl) sEl.innerText = value; }
                    if (key === 'content') { const cEl = selectedItem.querySelector('.card-content-mock'); if (cEl) cEl.innerText = value; }
                }

                if (type === 'CreditCard') {
                    if (key === 'submitText') {
                        const btnEl = selectedItem.querySelector('button');
                        if (btnEl) btnEl.innerText = value;
                    }
                }

                if ((key === 'options' || key === 'icon') && (type === 'SelectOne' || type === 'SelectOneIcon')) {
                    const select = selectedItem.querySelector('select');
                    if (select) {
                        select.innerHTML = '';
                        const opts = (props.options || '').split(',');
                        opts.forEach(o => {
                            const opt = document.createElement('option');
                            const val = o.trim();
                            if (type === 'SelectOneIcon') {
                                const parts = val.split('|');
                                const optVal = parts[0] ? parts[0].trim() : 'val';
                                const optLabel = parts[1] ? parts[1].trim() : optVal;
                                const optIcon = parts[2] ? parts[2].trim() : (props.icon || '⭐');
                                opt.innerText = optIcon + ' ' + optLabel;
                            } else {
                                opt.innerText = val;
                            }
                            select.appendChild(opt);
                        });
                    }
                }
                if (key === 'icon' && type === 'Avatar') {
                    const el = selectedItem.querySelector('.j-avatar');
                    if (el) el.innerHTML = value;
                    props.text = "";
                    selectedItem.setAttribute('data-props', JSON.stringify(props));
                    setTimeout(window.updateInspector, 10);
                }
                if (type === 'ProgressBar') {
                    const fill = selectedItem.querySelector('.j-progressbar-fill');
                    if (key === 'value' || key === 'max') {
                        const val = parseInt(selectedItem.getAttribute('data-props-value') || props.value || 60);
                        const mx = parseInt(selectedItem.getAttribute('data-props-max') || props.max || 100);
                        const pct = (key === 'value' ? value : val) / (key === 'max' ? value : mx) * 100;
                        fill.style.width = pct + '%';
                    }
                    if (key === 'color') {
                        fill.style.background = value;
                        fill.style.boxShadow = `0 0 10px ${value}`;
                    }
                    if (key === 'indeterminate') {
                        if (value) fill.classList.add('j-progress-indeterminate');
                        else fill.classList.remove('j-progress-indeterminate');
                    }
                    if (key === 'showPercent') {
                        const lbl = selectedItem.querySelector('.j-progressbar-label');
                        if (lbl) lbl.style.display = value ? 'block' : 'none';
                    }
                }
                if (key === 'columns' && (type === 'Panel' || type === 'Grid')) {
                    const container = selectedItem.querySelector('.canvas-container');
                    if (container) container.style.gridTemplateColumns = `repeat(${value}, 1fr)`;
                }
                if (type === 'Spinner' && key === 'value') {
                    const disp = selectedItem.querySelector('.j-spinner-display');
                    if (disp) disp.innerText = value;
                }
                if (type.startsWith('Chart') && key === 'bgColor') {
                    selectedItem.style.borderColor = value;
                    const span = selectedItem.querySelector('span');
                    if (span) span.style.color = value;
                }
                window.updateGeneratedCode();
            };

            window.jettraFileCache = {};

            window.restoreFilesFromCache = function() {
                const cachedTree = localStorage.getItem('jettra_designer_tree');
                const cachedFilesStr = localStorage.getItem('jettra_designer_files');
                
                if (cachedTree && cachedFilesStr) {
                    const viewer = document.getElementById('explorer-tree-view');
                    if (viewer) viewer.innerHTML = cachedTree;
                    try {
                        window.jettraFileCache = JSON.parse(cachedFilesStr);
                        availableModels = [];
                        Object.keys(window.jettraFileCache).forEach(path => {
                            if (path.endsWith('Model.java')) {
                                const name = path.split('/').pop().replace('.java','');
                                availableModels.push({ name: name, content: window.jettraFileCache[path] });
                            }
                        });
                        window.updateModelSelect();
                    } catch(e) {}
                }
            };

            window.clearProjectCache = function() {
                localStorage.removeItem('jettra_designer_tree');
                localStorage.removeItem('jettra_designer_files');
                window.jettraFileCache = {};
                availableModels = [];
                const viewer = document.getElementById('explorer-tree-view');
                if (viewer) viewer.innerHTML = '';
                window.updateModelSelect();
            };
        """;
        
        String scriptPart2 = """

            window.loadFiles = function(input) {
                const triggerLoad = () => {
                    const viewer = document.getElementById('explorer-tree-view');
                    if (!viewer) return;
                    viewer.innerHTML = 'Loading...';
                    
                    const files = (input.tagName === 'INPUT' ? input : input.querySelector('input')).files;
                    const excludeTarget = input.getAttribute('data-exclude-target') === 'true';
                    
                    availableModels = [];
                    window.jettraFileCache = {};
                    const tree = {};
                    const readPromises = [];

                    for (let i = 0; i < files.length; i++) {
                        const f = files[i];
                        const relPath = f.webkitRelativePath || f.name;
                        
                        if (excludeTarget && (relPath.includes('/target/') || relPath.includes('target/'))) continue;
                        if (!relPath.endsWith('.java') && !relPath.endsWith('.properties')) continue;

                        const parts = relPath.split('/');
                        let curr = tree;
                        for (let j = 0; j < parts.length; j++) {
                            const part = parts[j];
                            if (!curr[part]) curr[part] = { _children: {}, _file: null, _path: relPath };
                            if (j === parts.length - 1) {
                                curr[part]._file = f;
                            }
                            curr = curr[part]._children;
                        }

                        readPromises.push(new Promise((resolve) => {
                            const reader = new FileReader();
                            reader.onload = (e) => {
                                const content = e.target.result;
                                window.jettraFileCache[relPath] = content;
                                if (f.name.endsWith('Model.java')) {
                                    availableModels.push({ name: f.name.replace('.java',''), content: content });
                                }
                                resolve();
                            };
                            reader.readAsText(f);
                        }));
                    }

                    function renderTree(node, name, depth = 0) {
                        const childrenKeys = Object.keys(node._children);
                        const isFile = !!node._file;
                        if (!isFile && childrenKeys.length === 0) return "";

                        let cssClass = "";
                        if (name.endsWith('Page.java')) cssClass = "file-page";
                        if (name.endsWith('Model.java')) cssClass = "file-model";
                        if (name.endsWith('.properties')) cssClass = "file-props";

                        let action = isFile ? `ondblclick="window.openClass('${name}')"` : "";
                        let html = `<div class="project-file ${cssClass}" style="padding-left:${depth * 12}px" ${action}>${isFile ? '📄' : '📂'} ${name}</div>`;
                        for (const child in node._children) {
                            html += renderTree(node._children[child], child, depth + 1);
                        }
                        return html;
                    }

                    Promise.all(readPromises).then(() => {
                        let finalHtml = "";
                        for (const root in tree) finalHtml += renderTree(tree[root], root);
                        viewer.innerHTML = finalHtml;
                        window.updateModelSelect();
                        window.updateInspector();
                        
                        try {
                            localStorage.setItem('jettra_designer_tree', finalHtml);
                            localStorage.setItem('jettra_designer_files', JSON.stringify(window.jettraFileCache));
                            window.show3DMessage("Proyecto Cargado", "El explorador ha procesado los archivos.");
                        } catch(e) {
                            console.warn("Could not cache files", e);
                        }
                    });
                };

                window.show3DConfirm(
                    "Explorador de Proyecto", 
                    "¿Desea cargar los archivos de esta carpeta al explorador?", 
                    triggerLoad
                );
            };

            window.openClass = function(name) {
                const isPage = name.endsWith('Page.java');
                const isModel = name.endsWith('Model.java');

                if (isModel) {
                    window.selectModel(name.replace('.java', ''));
                    return;
                }

                if (isPage) {
                    const canvas = document.getElementById('canvas-drop-area');
                    const fullPathKey = Object.keys(window.jettraFileCache).find(k => k.endsWith('/' + name) || k === name);
                    const content = fullPathKey ? window.jettraFileCache[fullPathKey] : undefined;
                    
                    const doOpen = () => {
                        if (content) window.loadFileContent(fullPathKey);
                        else window.show3DMessage("Aviso", "No se encontró contenido para " + name);
                    };

                    if (canvas.querySelectorAll('.canvas-item').length > 0) {
                        window.show3DConfirm("Abrir Clase", "¿Cerrar diseño actual y abrir " + name + "?", doOpen);
                    } else {
                        doOpen();
                    }
                }
            };

            window.updateModelSelect = function() {
                const container = document.getElementById('model-select-container');
                if (!container) return;
                
                if (availableModels.length === 0) {
                    container.innerHTML = '<span style="color:#666; font-size:11px">No models found in project</span>';
                    return;
                }
                
                let html = '<select class="j-input" style="padding:4px; font-size:11px" onchange="window.selectModel(this.value)">';
                html += '<option value="">-- Select Model --</option>';
                availableModels.forEach(m => {
                    const selected = viewModelName === m.name ? 'selected' : '';
                    html += `<option value="${m.name}" ${selected}>${m.name}</option>`;
                });
                html += '</select>';
                container.innerHTML = html;
            };

            window.generateCRUD = function() {
                const canvas = document.getElementById('canvas-drop-area');
                if (!canvas) return;
                if (!currentModel) {
                    window.show3DMessage("Error", "Por favor, seleccione un modelo en el explorador de proyectos o en el selector de la parte superior.");
                    return;
                }
                
                const mName = currentModel.name;
                const baseName = mName.replace("Model", "");
                const repoName = baseName + "Repository";
                
                // Clear canvas to show informative message
                canvas.innerHTML = `
                    <div class="canvas-placeholder" style="color:var(--jettra-accent); display:flex; flex-direction:column; align-items:center; justify-content:center; height:100%; text-align:center;">
                        <span style="font-size:48px; margin-bottom:20px;">⚡</span>
                        <h3>CRUD MVC GENERADO</h3>
                        <p>Se ha analizado el modelo <strong>${mName}</strong> y se ha generado el controlador de vista completo.</p>
                        <p style="font-size:12px; margin-top:10px; opacity:0.7;">Revise el panel de la derecha para ver el código fuente Java.</p>
                    </div>
                `;

                let tblHeaders = ""; let tblRows = ""; let modalBody = "";
                
                modelFields.forEach(field => {
                    const CapName = field.name.charAt(0).toUpperCase() + field.name.slice(1);
                    const vName = field.name;
                    const isList = field.type.toLowerCase().includes('list');
                    const isCustom = /^[A-Z]/.test(field.type) && !['String','Integer','Double','Boolean','Long','Float'].includes(field.type);
                    
                    tblHeaders += `            new TD("${CapName}"),\\n`;
                    tblRows += `                new TD(String.valueOf(p.get${CapName}())),\\n`;
                    
                    modalBody += `        Div group_${vName} = new Div(); group_${vName}.addClass("form-group");\\n`;
                    modalBody += `        group_${vName}.add(new Label("label_${vName}", "${CapName}"));\\n`;
                    
                    if (isList) {
                        modalBody += `        SelectMany sel_${vName} = new SelectMany("sel_${vName}");\\n`;
                        modalBody += `        sel_${vName}.setId("input_${vName}").addClass("j-input");\\n`;
                        modalBody += `        group_${vName}.add(sel_${vName});\\n`;
                    } else if (isCustom) {
                        modalBody += `        SelectOne sel_${vName} = new SelectOne("sel_${vName}", "");\\n`;
                        modalBody += `        sel_${vName}.setId("input_${vName}").addClass("j-input");\\n`;
                        modalBody += `        group_${vName}.add(sel_${vName});\\n`;
                    } else {
                        modalBody += `        TextBox tb_${vName} = new TextBox("text", "${vName}");\\n`;
                        modalBody += `        tb_${vName}.setId("input_${vName}").addClass("j-input");\\n`;
                        modalBody += `        group_${vName}.add(tb_${vName});\\n`;
                    }
                    modalBody += `        form.add(group_${vName});\\n\\n`;
                });

                let javaCode = `package com.jettra.example.pages;\\n\\n`;
                javaCode += `import com.jettra.example.dashboard.DashboardBasePage;\\n`;
                javaCode += `import com.jettra.example.model.${mName};\\n`;
                javaCode += `import com.jettra.example.repository.${repoName};\\n`;
                javaCode += `import io.jettra.wui.complex.*;\\n`;
                javaCode += `import io.jettra.wui.components.*;\\n`;
                javaCode += `import java.util.*;\\n\\n`;
                javaCode += `public class ${baseName}Page extends DashboardBasePage {\\n\\n`;
                javaCode += `    private ${mName} model = new ${mName}();\\n\\n`;
                javaCode += `    public ${baseName}Page() {\\n        super("${baseName} Administration");\\n    }\\n\\n`;
                javaCode += `    @Override\\n    protected void initCenter(Center center, String user) {\\n`;
                javaCode += `        Div container = new Div(); container.setStyle("padding", "30px");\\n\\n`;
                javaCode += `        Header title = new Header(2, "Mantenimiento de ${baseName}");\\n`;
                javaCode += `        title.setStyle("color", "var(--jettra-accent)").setStyle("margin-bottom", "20px");\\n`;
                javaCode += `        container.add(title);\\n\\n`;
                javaCode += `        Button addBtn = new Button("➕ Nuevo ${baseName}"); addBtn.addClass("j-btn-primary");\\n`;
                javaCode += `        addBtn.addClickListener(() -> { showModal("Crear ${baseName}", "save"); });\\n`;
                javaCode += `        container.add(addBtn).add(new Divide());\\n\\n`;
                javaCode += `        Datatable table = new Datatable();\\n`;
                javaCode += `        table.addHeaderRow(new Row(\\n${tblHeaders}            new TD("Acciones")));\\n\\n`;
                javaCode += `        List<${mName}> list = ${repoName}.findAll();\\n`;
                javaCode += `        for (${mName} p : list) {\\n`;
                javaCode += `            TD actions = new TD();\\n`;
                javaCode += `            Button edit = new Button("✏️"); edit.addClass("j-btn");\\n`;
                javaCode += `            edit.addClickListener(() -> { this.model = p; showModal("Editar ${baseName}", "update"); });\\n`;
                javaCode += `            actions.add(edit);\\n`;
                javaCode += `            table.addRow(new Row(\\n${tblRows}                actions));\\n`;
                javaCode += `        }\\n        container.add(table);\\n        center.add(container);\\n        setupModal();\\n    }\\n\\n`;
                javaCode += `    private void showModal(String title, String action) {\\n        document.getElementById("crud-modal").style.display = "flex";\\n    }\\n\\n`;
                javaCode += `    private void setupModal() {\\n`;
                javaCode += `        Modal modal = new Modal("crud-modal");\\n`;
                javaCode += `        modal.addClass("j-modal-content");\\n`;
                javaCode += `        Form form = new Form("crud-form", "");\\n\\n`;
                javaCode += modalBody;
                javaCode += `        Button saveBtn = new Button("GUARDAR"); saveBtn.addClass("j-btn-primary");\\n`;
                javaCode += `        modal.add(new Header(3, "Datos del Registro")).add(form).add(saveBtn);\\n`;
                javaCode += `        this.add(modal);\\n    }\\n}\\n`;

                document.getElementById('generated-code-display').value = javaCode;
                document.getElementById('generated-code-hidden').value = javaCode;
                window.show3DMessage("CRUD Generado", "Se ha generado el código base siguiendo el patrón MVC.");
            };

            window.clearDesigner = function() {
                window.show3DConfirm("Limpiar", "¿Borrar elementos?", () => {
                    document.getElementById('canvas-drop-area').innerHTML = '<div class="canvas-placeholder">Start dragging...</div>';
                    selectedItem = null;
                    window.updateInspector();
                });
            };

            };

            window.updateGeneratedCode = function() {
                if (window.isSyncing) return;
                const display = document.getElementById('generated-code-display');
                const hidden = document.getElementById('generated-code-hidden');
                if (!display || !hidden) return;

                const canvasItems = document.querySelectorAll('#canvas-drop-area > .canvas-item, .canvas-container > .canvas-item');
                if (canvasItems.length === 0) {
                    display.innerText = "// No components added yet";
                    return;
                }

                let code = `package com.jettra.example.pages;\\n\\nimport io.jettra.wui.complex.*;\\nimport io.jettra.wui.components.*;\\n\\n`;
                code += `public class GeneratedPage extends DashboardBasePage {\\n`;
                code += `    private ${viewModelName} model = ${viewModelName}.getInstance();\\n\\n`;
                code += `    protected void initCenter(Center center, String username) {\\n`;
                
                function walk(items, container) {
                    let out = "";
                    items.forEach(it => {
                        const type = it.getAttribute('data-type');
                        const props = JSON.parse(it.getAttribute('data-props'));
                        const v = props.id && props.id.trim() !== "" ? props.id : (type.toLowerCase() + "_" + Math.floor(Math.random()*10000));
                        
                        const handleCommon = (compVar) => {
                            let out = "";
                            if (props.update) out += `        ${compVar}.setUpdate("${props.update}");\\n`;
                            return out;
                        };

                        const handleEvents = (compVar) => {
                            let evOut = "";
                            Object.keys(props.events).forEach(ev => {
                                const eventCode = props.events[ev].replace(/\\\\n/g, '\\n');
                                evOut += `        ${compVar}.${ev}(${eventCode});\\n`;
                            });
                            return evOut;
                        };

                        switch(type) {
                            case 'Divide': out += `        ${container}.add(new Divide());\\n`; break;
                            case 'Separator': out += `        ${container}.add(new Separator());\\n`; break;
                            case 'Header': out += `        ${container}.add(new Header(1, "${props.text}"));\\n`; break;
                            case 'Paragraph': out += `        ${container}.add(new Paragraph("${props.text}"));\\n`; break;
                            case 'Span': out += `        ${container}.add(new Span("${props.text}"));\\n`; break;
                            case 'Label': out += `        ${container}.add(new Label("${props.text}"));\\n`; break;
                            case 'Button': 
                                out += `        ${type} ${v} = new ${type}("${props.text}");\\n`;
                                if (props.btnStyle) out += `        ${v}.setStyle(${type}.Style.${props.btnStyle});\\n`;
                                if (props.icon) out += `        ${v}.setIcon("${props.icon}");\\n`;
                                out += handleCommon(v);
                                out += handleEvents(v);
                                out += `        ${container}.add(${v});\\n`;
                                break;
                            case 'TextBox':
                            case 'TextArea':
                                out += `        ${type} ${v} = new ${type}("${props.text || type}", "${props.text || type}");\\n`;
                                out += handleCommon(v);
                                out += handleEvents(v);
                                out += `        ${container}.add(${v});\\n`;
                                break;
                            case 'Avatar':
                                out += `        Avatar ${v} = new Avatar();\\n`;
                                if (props.text) out += `        ${v}.setText("${props.text}");\\n`;
                                if (props.icon) out += `        ${v}.setIcon("${props.icon}");\\n`;
                                out += handleEvents(v);
                                out += `        ${container}.add(${v});\\n`;
                                break;
                            case 'ProgressBar':
                                out += `        ProgressBar ${v} = new ProgressBar(${props.value || 0}, ${props.max || 100});\\n`;
                                if (props.color) out += `        ${v}.setColor("${props.color}");\\n`;
                                if (props.indeterminate) out += `        ${v}.setIndeterminate(true);\\n`;
                                if (props.showPercent === false) out += `        ${v}.setShowPercent(false);\\n`;
                                out += handleCommon(v);
                                out += handleEvents(v);
                                out += `        ${container}.add(${v});\\n`;
                                break;
                            case 'Button': 
                                out += `        Button ${v} = new Button("${props.text}");\\n`;
                                out += handleCommon(v);
                                out += handleEvents(v);
                                out += `        ${container}.add(${v});\\n`;
                                break;
                            case 'TextBox':
                                out += `        TextBox ${v} = new TextBox("${v}", "Enter value");\\n`;
                                if (props.binding) out += `        ${v}.setValue(model.get${props.binding.charAt(0).toUpperCase() + props.binding.slice(1)}());\\n`;
                                out += handleCommon(v);
                                out += handleEvents(v);
                                out += `        ${container}.add(${v});\\n`;
                                break;
                            case 'TextArea':
                                out += `        TextArea ${v} = new TextArea("${v}", "");\\n`;
                                if (props.binding) out += `        ${v}.setValue(model.get${props.binding.charAt(0).toUpperCase() + props.binding.slice(1)}());\\n`;
                                out += handleCommon(v);
                                out += handleEvents(v);
                                out += `        ${container}.add(${v});\\n`;
                                break;
                            case 'Clock':
                                out += `        Clock ${v} = new Clock("${props.text || 'clock'}");\\n`;
                                out += handleCommon(v);
                                out += handleEvents(v);
                                out += `        ${container}.add(${v});\\n`;
                                break;
                            case 'Spinner':
                                out += `        Spinner ${v} = new Spinner("${v}", ${props.value || 0});\\n`;
                                if (props.min !== undefined && props.min !== "") out += `        ${v}.setMin(${props.min});\\n`;
                                if (props.max !== undefined && props.max !== "") out += `        ${v}.setMax(${props.max});\\n`;
                                if (props.step !== undefined && props.step !== "") out += `        ${v}.setStep(${props.step});\\n`;
                                out += handleCommon(v);
                                out += handleEvents(v);
                                out += `        ${container}.add(${v});\\n`;
                                break;
                            case 'ScheduleControl': {
                                out += `        ScheduleControl ${v} = new ScheduleControl("${v}");\\n`;
                                if (props.binding) out += `        ${v}.setValue(model.get${props.binding.charAt(0).toUpperCase() + props.binding.slice(1)}());\\n`;
                                if (props.showTimeRemaining) out += `        ${v}.setShowTimeRemaining(true);\\n`;
                                if (props.onTimeReached) out += `        ${v}.setOnTimeReached("${props.onTimeReached}");\\n`;
                                out += handleCommon(v);
                                out += handleEvents(v);
                                out += `        ${container}.add(${v});\\n`;
                                break;
                            }
                            case 'ChartsBar':
                            case 'ChartsDoughnut':
                            case 'ChartsLine':
                            case 'ChartsPie':
                            case 'ChartsRadar': {
                                out += `        ${type} ${v} = new ${type}("${v}");\\n`;
                                const lArg = props.labels ? `"${props.labels.split(',').map(s => s.trim()).join('", "')}"` : '"Label 1", "Label 2"';
                                const dLabel = props.datasetLabel ? props.datasetLabel : "Data";
                                const dData = props.data ? props.data : "10, 20";
                                const dBg = props.bgColor ? props.bgColor : "#00ffff";
                                out += `        ${v}.setLabels(${lArg});\\n`;
                                out += `        ${v}.addDataset("${dLabel}", new Number[]{${dData}}, new String[]{"${dBg}"}, new String[]{"${dBg}"});\\n`;
                                out += handleCommon(v);
                                out += handleEvents(v);
                                out += `        ${container}.add(${v});\\n`;
                                break;
                            }
                            case 'Loading':
                                out += `        Loading ${v} = new Loading();\\n`;
                                if (props.size) out += `        ${v}.setSize(Loading.Size.${props.size});\\n`;
                                if (props.color) out += `        ${v}.setColor("${props.color}");\\n`;
                                out += handleCommon(v);
                                out += handleEvents(v);
                                out += `        ${container}.add(${v});\\n`;
                                break;
                            case 'Modal': {
                                const mId = props.idGen || "modal_" + Math.floor(Math.random()*1000);
                                out += `        Modal ${v} = new Modal("${mId}");\\n`;
                                const kidsM = it.querySelectorAll(':scope > .canvas-container > .canvas-item');
                                if (kidsM.length > 0) out += walk(kidsM, v);
                                out += handleCommon(v);
                                out += handleEvents(v);
                                out += `        ${container}.add(${v});\\n`;
                                break;
                            }
                            case 'Card': {
                                out += `        Card ${v} = new Card();\\n`;
                                if (props.title) out += `        ${v}.setTitle("${props.title}");\\n`;
                                if (props.subtitle) out += `        ${v}.setSubtitle("${props.subtitle}");\\n`;
                                if (props.content) out += `        ${v}.setContentText("${props.content}");\\n`;
                                if (props.imageUrl) out += `        ${v}.setImageUrl("${props.imageUrl}");\\n`;
                                const kidsCard = it.querySelectorAll(':scope > .canvas-container > .canvas-item, :scope > div > .canvas-container > .canvas-item');
                                if (kidsCard.length > 0) out += walk(kidsCard, v);
                                out += handleCommon(v);
                                out += handleEvents(v);
                                out += `        ${container}.add(${v});\\n`;
                                break;
                            }
                            case 'MenuItem':
                            case 'TreeItem':
                            case 'Tab':
                                out += `        ${type} ${v} = new ${type}("${props.text || type}");\\n`;
                                const kidsNested = it.querySelectorAll(':scope > .canvas-container > .canvas-item, :scope > div > .canvas-container > .canvas-item, :scope > span > .canvas-container > .canvas-item');
                                if (kidsNested.length > 0) out += walk(kidsNested, v);
                                out += handleCommon(v);
                                out += handleEvents(v);
                                out += `        ${container}.add(${v});\\n`;
                                break;
                            case 'SelectOne':
                            case 'SelectOneIcon':
                                if (props.text && props.text !== type) {
                                    out += `        ${type} ${v} = new ${type}("${v}", "${props.text}");\\n`;
                                } else {
                                    out += `        ${type} ${v} = new ${type}("${v}", "${type}");\\n`;
                                }
                                if (props.options) {
                                    const opts = props.options.split(',');
                                    opts.forEach(o => {
                                        if (type === 'SelectOneIcon') {
                                            const parts = o.trim().split('|');
                                            const optVal = parts[0] ? parts[0].trim() : 'val';
                                            const optLabel = parts[1] ? parts[1].trim() : optVal;
                                            const optIcon = parts[2] ? parts[2].trim() : (props.icon || '⭐');
                                            out += `        ${v}.addOption("${optVal}", "${optLabel}", "${optIcon}");\\n`;
                                        } else {
                                            const parts = o.trim().split('|');
                                            const optVal = parts[0] ? parts[0].trim() : 'val';
                                            const optLabel = parts[1] ? parts[1].trim() : optVal;
                                            out += `        ${v}.addOption("${optVal}", "${optLabel}");\\n`;
                                        }
                                    });
                                }
                                out += handleCommon(v);
                                out += handleEvents(v);
                                out += `        ${container}.add(${v});\\n`;
                                break;
                            case 'Panel':
                            case 'Grid':
                            case 'Board':
                            case 'MenuBar':
                            case 'RadioGroupButton':
                            case 'CheckBoxGroup':
                            case 'Tree':
                            case 'TabView':
                            case 'Div':
                            case 'LayoutDisplay':
                                if (type === 'RadioGroupButton' || type === 'CheckBoxGroup') {
                                    out += `        ${type} ${v} = new ${type}("${props.name || v}");\\n`;
                                } else {
                                    out += `        ${type} ${v} = new ${type}("${v}");\\n`;
                                }
                                if (props.columns && (type === 'Grid' || type === 'Panel')) out += `        ${v}.setColumns(${props.columns});\\n`;
                                const kids = it.querySelectorAll(':scope > .canvas-container > .canvas-item, :scope > div > .canvas-container > .canvas-item');
                                if (kids.length > 0) out += walk(kids, v);
                                if (props.icon) out += `        // Try using icon property if supported for ${type}\\n        try { ${v}.getClass().getMethod("setIcon", String.class).invoke(${v}, "${props.icon}"); } catch(Exception ignored) {}\\n`;
                                out += handleCommon(v);
                                out += handleEvents(v);
                                out += `        ${container}.add(${v});\\n`;
                                break;
                            case 'DatePicker':
                            case 'Time':
                                out += `        ${type} ${v} = new ${type}("${v}", "${props.text || type}");\\n`;
                                out += handleCommon(v);
                                out += handleEvents(v);
                                out += `        ${container}.add(${v});\\n`;
                                break;
                            case 'Calendar':
                            case 'Schedule':
                            case 'Timeline':
                            case 'Organigram':
                                out += `        ${type} ${v} = new ${type}();\\n`;
                                out += handleCommon(v);
                                out += handleEvents(v);
                                out += `        ${container}.add(${v});\\n`;
                                break;
                            case 'Map':
                                out += `        Map ${v} = new Map("${v}");\\n`;
                                out += `        ${v}.setCenter(${props.lat || 0.0}, ${props.lng || 0.0}, ${props.zoom || 13});\\n`;
                                if (props.markerTitle) out += `        ${v}.setMarker("${props.markerTitle}");\\n`;
                                if (props.enableSearch) out += `        ${v}.setEnableSearch(true);\\n`;
                                if (props.enableRelief) out += `        ${v}.setEnableRelief(true);\\n`;
                                out += handleCommon(v);
                                out += handleEvents(v);
                                out += `        ${container}.add(${v});\\n`;
                                break;
                            case 'QR':
                                out += `        QR ${v} = new QR("${v}");\\n`;
                                if (props.text) out += `        ${v}.setText("${props.text}");\\n`;
                                if (props.width) out += `        ${v}.setWidth(${props.width});\\n`;
                                if (props.height) out += `        ${v}.setHeight(${props.height});\\n`;
                                if (props.colorDark) out += `        ${v}.setColorDark("${props.colorDark}");\\n`;
                                if (props.colorLight) out += `        ${v}.setColorLight("${props.colorLight}");\\n`;
                                out += handleCommon(v);
                                out += handleEvents(v);
                                out += `        ${container}.add(${v});\\n`;
                                break;
                            case 'BarCode':
                                out += `        BarCode ${v} = new BarCode("${v}");\\n`;
                                if (props.text) out += `        ${v}.setText("${props.text}");\\n`;
                                if (props.format) out += `        ${v}.setFormat("${props.format}");\\n`;
                                if (props.lineColor) out += `        ${v}.setLineColor("${props.lineColor}");\\n`;
                                out += handleCommon(v);
                                out += handleEvents(v);
                                out += `        ${container}.add(${v});\\n`;
                                break;
                            case 'OTPValidator':
                                out += `        OTPValidator ${v} = new OTPValidator("${v}");\\n`;
                                if (props.amountOfDigits) out += `        ${v}.setAmountOfDigits(${props.amountOfDigits});\\n`;
                                out += handleCommon(v);
                                out += handleEvents(v);
                                out += `        ${container}.add(${v});\\n`;
                                break;
                            case 'Catcha':
                                out += `        Catcha ${v} = new Catcha("${v}");\\n`;
                                if (props.amountOfImagesToValidate) out += `        ${v}.setAmountOfImagesToValidate(${props.amountOfImagesToValidate});\\n`;
                                out += handleCommon(v);
                                out += handleEvents(v);
                                out += `        ${container}.add(${v});\\n`;
                                break;
                            case 'CreditCard':
                                out += `        CreditCard ${v} = new CreditCard("${v}");\\n`;
                                if (props.formAction) out += `        ${v}.setFormAction("${props.formAction}");\\n`;
                                if (props.submitText) out += `        ${v}.setSubmitText("${props.submitText}");\\n`;
                                const kidsCC = it.querySelectorAll(':scope > .canvas-container > .canvas-item, :scope > div > .canvas-container > .canvas-item');
                                if (kidsCC.length > 0) out += walk(kidsCC, v);
                                out += handleCommon(v);
                                out += handleEvents(v);
                                out += `        ${container}.add(${v});\\n`;
                                break;
                            default: 
                                // Catch-all for components without direct text args (e.g., CheckBox, ToggleSwitch, Spinner, etc)
                                if (props.text && props.text !== type && type !== 'Spinner') {
                                    out += `        ${type} ${v} = new ${type}("${props.text}");\\n`;
                                } else {
                                    out += `        ${type} ${v} = new ${type}("${v}");\\n`;
                                }
                                if (props.icon) out += `        // Try using icon property if supported\\n        try { ${v}.getClass().getMethod("setIcon", String.class).invoke(${v}, "${props.icon}"); } catch(Exception ignored) {}\\n`;
                                out += handleCommon(v);
                                out += handleEvents(v);
                                out += `        ${container}.add(${v});\\n`;
                        }
                    });
                    return out;
                }
                code += walk(document.querySelectorAll('#canvas-drop-area > .canvas-item'), "center");
                code += walk(document.querySelectorAll('#modal-list-container > .canvas-item'), "center");
                code += `    }\\n}`;
                
                display.value = code;
                hidden.value = code;
            };

            window.syncCodeToCanvas = function() {
                window.isSyncing = true;
                const display = document.getElementById('generated-code-display');
                if (!display) {
                    window.isSyncing = false;
                    return;
                }
                const code = display.value;
                const canvas = document.getElementById('canvas-drop-area');
                
                canvas.innerHTML = '';
                const mtArea = document.getElementById('modal-list-container');
                if (mtArea) mtArea.innerHTML = '';
                
                let foundAny = false;
                let varMap = {}; 
                const lines = code.split('\\n');
                
                let currentModalArea = mtArea;

                lines.forEach(line => {
                    let cleanLine = line.trim();
                    if (cleanLine.startsWith("//") || cleanLine.length === 0) return;

                    let currentVar = null;
                    
                    // Match object instantiation (e.g., io.jettra.wui.complex.Modal codeModal = new io.jettra.wui.complex.Modal("clock-code-modal");)
                    // Group 1: Type name, Group 2: Variable name, Group 3: Constructor args
                    let mInst = /(?:[a-zA-Z0-9_.]+\\.)?([A-Z][a-zA-Z0-9_]*)\\s+([a-zA-Z0-9_]+)\\s*=\\s*new\\s+(?:[a-zA-Z0-9_.]+\\.)?[A-Z][a-zA-Z0-9_]*\\s*\\((.*?)\\)/.exec(cleanLine);
                    
                    if (mInst) {
                        foundAny = true;
                        let type = mInst[1];
                        let vname = mInst[2];
                        let args = mInst[3];
                        
                        currentVar = vname;
                        
                        // We skip non-UI things if necessary, but window.addComponentToCanvas handles known types
                        let tempParent = document.createElement('div');
                        window.addComponentToCanvas(type, tempParent);
                        let el = tempParent.lastElementChild;
                        if (el) {
                            el.setAttribute('data-var', vname);
                            varMap[vname] = el;
                            
                            let props = JSON.parse(el.getAttribute('data-props') || "{}");
                            if (args && args.trim().length > 0) {
                                let cleanParams = args.replace(/"/g, '').split(',');
                                if (cleanParams.length > 0) {
                                    if (['Header','Paragraph','Button','Clock','Span','Label','Alert','Notification','Link','Downloader','CheckBox','RadioButton','ToggleSwitch','MenuItem','TreeItem','Tab','Card'].includes(type)) {
                                        props.text = cleanParams.length > 1 ? cleanParams[1].trim() : cleanParams[0].trim();
                                    }
                                    if (type === 'Modal') {
                                        let mName = cleanParams.length > 1 ? cleanParams[1].trim() : (cleanParams.length > 0 ? cleanParams[0].trim() : 'Modal Component');
                                        props.text = mName;
                                        props.idGen = cleanParams[0].trim();
                                    }
                                    if (type === 'Board') props.text = cleanParams.length > 1 ? cleanParams[1].trim() : (cleanParams.length > 0 ? cleanParams[0].trim() : 'New Board');
                                    if (['Panel','Grid','Div','LayoutDisplay','TabView','Tree','MenuBar'].includes(type)){
                                        props.columns = parseInt(cleanParams[0].trim()) || 2;
                                    }
                                    if (type === 'ProgressBar') { props.value = parseInt(cleanParams[0])||0; props.max = parseInt(cleanParams[1])||100; }
                                }
                            }
                            el.setAttribute('data-props', JSON.stringify(props));
                            const inner = el.querySelector('h3, h2, h1, p, button, label, .j-avatar, .j-panel-header, span');
                            if (props.text && inner) inner.innerText = props.text;
                        }
                    } else {
                        // Extract starting variable from chains (e.g., container.add(...))
                        let firstVarMatch = /^([a-zA-Z0-9_]+)\\./.exec(cleanLine);
                        if (firstVarMatch) currentVar = firstVarMatch[1];
                    }

                    if (!currentVar && !cleanLine.includes(".add")) return; 

                    let parentEl = varMap[currentVar];
                    if (currentVar === 'center' || currentVar === 'container') parentEl = canvas; 
                    if (!parentEl && currentVar === 'center') parentEl = canvas;
                    if (!parentEl && currentVar === 'codeModal') parentEl = canvas;

                    // Match all .add() calls
                    let addMatches = [...cleanLine.matchAll(/\\.add\\s*\\((.*?)\\)/g)];
                    addMatches.forEach(mAdd => {
                        let childArg = mAdd[1].trim();
                        if (parentEl) {
                           if (childArg.startsWith("new ")) {
                               let inlineTypeMatch = /new\\s+(?:[a-zA-Z0-9_.]+\\.)?([A-Z][a-zA-Z0-9_]*)\\s*\\((.*?)\\)/.exec(childArg);
                               if (inlineTypeMatch) {
                                   let type = inlineTypeMatch[1];
                                   let args = inlineTypeMatch[2];
                                   let targetContainer = parentEl.querySelector('.canvas-container') || parentEl;
                                   
                                   window.addComponentToCanvas(type, targetContainer);
                                   let el = targetContainer.lastElementChild;
                                   if(el) {
                                       let props = JSON.parse(el.getAttribute('data-props') || "{}");
                                       if (args && args.trim().length > 0) {
                                          let cleanParams = args.replace(/"/g, '').split(',');
                                          if (['Header','Paragraph','Button','Clock','Span','Label','Card'].includes(type)) {
                                             props.text = cleanParams.length > 1 ? cleanParams[1].trim() : cleanParams[0].trim();
                                          }
                                       }
                                       el.setAttribute('data-props', JSON.stringify(props));
                                       const inner = el.querySelector('h3, h2, h1, p, button, label, span');
                                       if (props.text && inner) inner.innerText = props.text;
                                   }
                               }
                           } else if(!childArg.includes(".")) {
                               let childEl = varMap[childArg];
                               if (childEl) {
                                   let targetContainer = parentEl;
                                   if (parentEl !== canvas && parentEl.classList.contains('canvas-item')) {
                                       let innerCont = parentEl.querySelector('.canvas-container');
                                       if (!innerCont) {
                                           let possibleContainers = parentEl.querySelectorAll('div');
                                           if (possibleContainers.length > 0) targetContainer = possibleContainers[possibleContainers.length-1];
                                       } else {
                                           targetContainer = innerCont;
                                       }
                                   }
                                   targetContainer.appendChild(childEl);
                               }
                           }
                        }
                    });

                    // Match all .setXxx() calls
                    let setMatches = [...cleanLine.matchAll(/\\.set([A-Z][a-zA-Z0-9_]*)\\s*\\((.*?)\\)/g)];
                    setMatches.forEach(mSet => {
                       let method = mSet[1];
                       let args = mSet[2].replace(/"/g, '').trim();
                       let el = varMap[currentVar];
                       if (el) {
                           let props = JSON.parse(el.getAttribute('data-props') || "{}");
                           if (method === 'Text') {
                               props.text = args;
                               const inner = el.querySelector('h3, h2, h1, p, button, label, span');
                               if (inner) inner.innerText = props.text;
                           }
                           el.setAttribute('data-props', JSON.stringify(props));
                       }
                    });
                });
                
                Object.values(varMap).forEach(el => {
                    if (!canvas.contains(el) && el.parentElement && !el.parentElement.classList.contains('canvas-container')) {
                        if (el.getAttribute('data-type') === 'Modal' && mtArea) mtArea.appendChild(el);
                        else canvas.appendChild(el);
                    }
                });

                if (Object.keys(varMap).length === 0 && !foundAny) {
                    canvas.innerHTML = '<div class="canvas-placeholder">Start dragging components here to design</div>';
                }
                
                document.getElementById('generated-code-hidden').value = code;
                display.value = code;
                window.show3DMessage("Sync Complete", "Canvas has been rebuilt exactly from the structured Java source code.");
                setTimeout(() => { window.isSyncing = false; }, 100);
            };

            window.previewInterface = function() {
                const canvas = document.getElementById('canvas-drop-area');
                const pcontent = document.getElementById('preview-content-area');
                if (!canvas || !pcontent) return;
                
                pcontent.innerHTML = canvas.innerHTML;
                
                const draggables = pcontent.querySelectorAll('[draggable]');
                draggables.forEach(d => {
                    d.removeAttribute('draggable');
                    d.oncontextmenu = null;
                    d.onclick = null;
                    d.classList.remove('selected');
                });
                
                const placeholders = pcontent.querySelectorAll('.canvas-placeholder');
                placeholders.forEach(p => p.remove());

                const deleteTools = pcontent.querySelectorAll('.delete-tool');
                deleteTools.forEach(d => d.remove());
                
                const items = pcontent.querySelectorAll('.canvas-item, .canvas-container, .board-container-mock, .modal-container-mock');
                items.forEach(c => {
                    c.classList.remove('canvas-item');
                    c.classList.remove('canvas-container');
                    c.classList.remove('selected');
                    if (c.style.border && c.style.border.includes('dashed')) {
                        c.style.border = 'none';
                    }
                });
                
                const header = pcontent.querySelector('#canvas-header');
                if (header) header.style.display = 'none';

                document.getElementById('preview-modal').style.display = 'block';
            };

        """;
        
        Script script = new Script(scriptPart1 + scriptPart2);
        
        center.add(style);
        center.add(script);
    }
}
