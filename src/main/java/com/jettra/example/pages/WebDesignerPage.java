package com.jettra.example.pages;

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
        canvasHeader.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px").setStyle("border-bottom", "1px solid rgba(0,255,255,0.1)").setStyle("padding-bottom", "10px");
        
        Header canvasTitle = new Header(4, "Visual Design Canvas");
        canvasTitle.setStyle("color", "var(--jettra-accent)").setStyle("margin", "0");
        
        Button crudBtn = new Button("CRUD ⚡");
        crudBtn.addClass("j-btn-primary");
        crudBtn.setStyle("font-size", "11px").setStyle("padding", "5px 12px");
        crudBtn.setProperty("type", "button");
        crudBtn.setProperty("onclick", "generateCRUD()");
        
        canvasHeader.add(canvasTitle).add(crudBtn);
        canvasArea.add(canvasHeader);

        Div canvasPlaceholder = new Div();
        canvasPlaceholder.addClass("canvas-placeholder");
        canvasPlaceholder.setContent("Start dragging components here to design your page");
        canvasPlaceholder.setStyle("color", "rgba(0,255,255,0.2)").setStyle("text-align", "center").setStyle("margin-top", "100px");
        canvasArea.add(canvasPlaceholder);

        // 3. Code View
        Div codeView = new Div();
        codeView.setStyle("flex", "0 0 350px").setStyle("background", "rgba(10,20,30,0.9)").setStyle("border", "1px solid var(--jettra-accent)").setStyle("padding", "15px").setStyle("border-radius", "8px").setStyle("display", "flex").setStyle("flex-direction", "column");
        
        Header codeHeader = new Header(4, "Java Source Code");
        codeHeader.setStyle("color", "var(--jettra-accent)").setStyle("margin-bottom", "10px");
        
        Div codeContainer = new Div();
        codeContainer.setProperty("id", "generated-code-display");
        codeContainer.setStyle("flex", "1").setStyle("background", "#050a10").setStyle("color", "#a9b7c6").setStyle("padding", "10px").setStyle("font-family", "monospace").setStyle("font-size", "11px").setStyle("white-space", "pre-wrap").setStyle("border-radius", "4px").setStyle("overflow-y", "auto").setStyle("border", "1px solid #333");
        codeContainer.setContent("// Drag components to start generating code...");

        // Hidden input to send code to server
        TextBox hiddenCode = new TextBox("generated-code-hidden", "");
        hiddenCode.setProperty("id", "generated-code-hidden");
        hiddenCode.setStyle("display", "none");

        Div actions = new Div();
        actions.setStyle("margin-top", "10px").setStyle("display", "flex").setStyle("gap", "10px");
        
        Button saveBtn = new Button("GENERATE CLASS");
        saveBtn.addClass("j-btn-primary");
        saveBtn.setProperty("type", "submit");
        
        Button openBtn = new Button("OPEN FILE");
        openBtn.addClass("j-btn-secondary");
        openBtn.setProperty("type", "button");

        Button clearBtn = new Button("CLEAR ALL");
        clearBtn.addClass("j-btn-danger");
        clearBtn.setProperty("type", "button");
        clearBtn.setProperty("onclick", "window.clearDesigner()");
        
        actions.add(saveBtn).add(openBtn).add(clearBtn);
        codeView.add(codeHeader).add(codeContainer).add(hiddenCode).add(actions);

        // 4. Property Inspector (Floating/Right Sidebar)
        Div inspector = new Div();
        inspector.setProperty("id", "property-inspector");
        inspector.setStyle("flex", "0 0 250px").setStyle("background", "rgba(20,35,55,0.9)").setStyle("border", "1px solid var(--jettra-accent)").setStyle("padding", "15px").setStyle("border-radius", "8px").setStyle("display", "none").setStyle("flex-direction", "column");
        
        Header inspectorHeader = new Header(4, "Properties");
        inspectorHeader.setStyle("color", "var(--jettra-accent)").setStyle("margin-bottom", "10px");
        
        Div propList = new Div();
        propList.setProperty("id", "inspector-properties");
        propList.setStyle("flex", "1");

        inspector.add(inspectorHeader).add(propList);

        container.add(sidebar).add(canvasArea).add(codeView).add(inspector);
        
        // 5. Event Editor Modal
        Modal eventModal = new Modal("event-editor-modal");
        eventModal.addClass("modal-3d-effect");
        eventModal.setStyle("display", "none").setStyle("background", "linear-gradient(145deg, rgba(30, 50, 80, 0.98), rgba(15, 25, 45, 1))")
                 .setStyle("backdrop-filter", "blur(25px)").setStyle("padding", "40px").setStyle("border-radius", "30px")
                 .setStyle("width", "750px").setStyle("border", "1px solid rgba(0, 255, 255, 0.5)")
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
        
        saveForm.add(modelSelectionRow).add(container);
        center.add(saveForm);

        // Add Designer Scripts and Styles
        setupDesignerAssets(center);
    }

    private Div createProjectExplorer() {
        Div explorer = new Div();
        Header h = new Header(5, "Project Explorer");
        h.setStyle("color", "var(--jettra-accent)").setStyle("margin-bottom", "10px");
        explorer.add(h);

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
        addPaletteCategory(palette, "Forms", new String[]{"Button", "CheckBox", "RadioButton", "SelectOne", "SelectOneIcon", "TextBox", "TextArea", "ToggleSwitch", "FileUpload", "FolderSelector"});
        // Navigation
        addPaletteCategory(palette, "Navigation", new String[]{"Link", "Menu", "MenuBar"});
        // Feedback
        addPaletteCategory(palette, "Feedback", new String[]{"ProgressBar", "Spinner", "Alert", "Notification", "Clock"});
        // Layout & Display
        addPaletteCategory(palette, "Layout & Display", new String[]{"Grid", "Panel", "Board", "Avatar", "Carousel", "Table", "TabView", "Modal", "Tree", "Div", "Image", "LayoutDisplay"});

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
                    transform: translateY(50px) scale(0.8) rotateX(-25deg); 
                }
                to { 
                    opacity: 1; 
                    transform: translateY(0) scale(1) rotateX(0); 
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
                from { opacity: 0; transform: translateY(-30px) scale(0.9) rotateX(-15deg); }
                to { opacity: 1; transform: translateY(0) scale(1) rotateX(0); }
            }
            .glass-panel {
                background: linear-gradient(145deg, rgba(30, 41, 59, 0.8), rgba(15, 23, 42, 0.9));
                backdrop-filter: blur(16px);
                border: 1px solid rgba(255, 255, 255, 0.1);
            }
        """);
        
        Script script = new Script("""
            // Designer GLOBALS
            var selectedItem = null;
            var currentModel = null;
            var modelFields = [];
            var availableModels = [];
            var viewModelName = "BaseViewModel";
            var activeEventProperty = null;
            var projectFilesMap = {};

            // EXPOSE FUNCTIONS TO WINDOW EXPLICITLY
            window.drag = function(ev) {
                ev.dataTransfer.setData("type", ev.currentTarget.getAttribute("data-type"));
                ev.dataTransfer.effectAllowed = "move";
            };

            function setupCanvasHandlers() {
                const canvas = document.getElementById('canvas-area');
                if (!canvas) return;
                
                canvas.ondragover = function(ev) {
                    ev.preventDefault();
                    ev.dataTransfer.dropEffect = "move";
                };

                canvas.ondrop = function(ev) {
                    ev.preventDefault();
                    const type = ev.dataTransfer.getData("type");
                    if (!type) return; 
                    const target = ev.target.closest('.canvas-container');
                    window.addComponentToCanvas(type, target || canvas);
                };
            }

            if (document.readyState === 'loading') {
                document.addEventListener('DOMContentLoaded', setupCanvasHandlers);
            } else {
                setupCanvasHandlers();
            }

            window.addComponentToCanvas = function(type, parent) {
                const canvas = document.getElementById('canvas-area');
                const targetParent = parent || canvas;
                if (!targetParent) return;

                const placeholder = document.querySelector('.canvas-placeholder');
                if (placeholder) placeholder.style.display = 'none';

                const wrapper = document.createElement('div');
                wrapper.className = 'canvas-item';
                wrapper.setAttribute('data-type', type);
                wrapper.setAttribute('data-props', JSON.stringify({text: type, columns: 2, events: {}, binding: ""}));
                
                wrapper.onclick = (e) => { e.stopPropagation(); selectElement(wrapper); };
                wrapper.oncontextmenu = (e) => { e.preventDefault(); e.stopPropagation(); selectElement(wrapper); showInspector(); };
                
                let content = '';
                switch(type) {
                    case 'Header': content = '<h2>New Header</h2>'; break;
                    case 'Paragraph': content = '<p style="color:#ccc">Sample text...</p>'; break;
                    case 'Divide': content = '<div style="border-top:2px solid var(--jettra-accent); margin:15px 0; opacity:0.5; width:100%"></div>'; break;
                    case 'Button': content = '<button class="j-btn-primary" type="button">Interactive Button</button>'; break;
                    case 'TextBox': content = '<input type="text" placeholder="TextBox..." style="width:100%; padding:10px; background:#111; color:#fff; border:1px solid #444; border-radius:4px" onfocus="this.blur()">'; break;
                    case 'Panel': 
                    case 'Grid':
                        content = '<div class="canvas-container" style="display:grid; grid-template-columns:1fr 1fr; gap:10px"></div>'; 
                        break;
                    case 'Modal':
                        content = '<div class="canvas-container modal-container-mock" style="padding:20px; border-radius:15px; border:2px solid var(--jettra-accent); background:rgba(20,40,70,0.4)">' +
                                  '<h3 style="margin-top:0; color:var(--jettra-accent)">Modal Component</h3>' +
                                  '</div>';
                        break;
                    case 'Board':
                        content = '<div class="canvas-container board-container-mock" style="padding:20px; border-radius:20px; border:1px solid var(--jettra-accent); background:rgba(15,23,42,0.4)">' +
                                  '<h2 style="margin-top:0; color:var(--jettra-accent); font-size:16px;">New Board</h2>' +
                                  '<div style="display:grid; grid-template-columns:1fr 1fr 1fr; gap:10px; min-height:80px;">' +
                                  '<div class="canvas-container" style="border:1px dashed rgba(0,255,255,0.2); border-radius:8px;"></div>' +
                                  '<div class="canvas-container" style="border:1px dashed rgba(0,255,255,0.2); border-radius:8px;"></div>' +
                                  '<div class="canvas-container" style="border:1px dashed rgba(0,255,255,0.2); border-radius:8px;"></div>' +
                                  '</div>' +
                                  '</div>';
                        break;
                    case 'Avatar':
                        content = '<div class="j-avatar j-avatar-circle j-avatar-md" style="background:var(--jettra-accent); color:#000; display:flex; align-items:center; justify-content:center; font-weight:bold">AV</div>';
                        break;
                    case 'ProgressBar':
                        content = '<div class="j-progressbar-container" style="width:100%; height:12px; background:rgba(255,255,255,0.05); border-radius:6px; overflow:hidden; border:1px solid rgba(255,255,255,0.1)">' +
                                  '<div class="j-progressbar-fill" style="width:60%; height:100%; background:var(--jettra-accent); box-shadow:0 0 10px var(--jettra-accent)"></div>' +
                                  '</div>';
                        break;
                    default: content = `<div style="padding:10px; border:1px solid #444; color:#888">${type} Placeholder</div>`;
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
                        <span class="inspector-label">Text Content</span>
                        <input type="text" class="inspector-input" value="${props.text || ""}" onchange="updateProp('text', this.value)">
                    </div>
                `;

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
                        <div class="inspector-row">
                            <span class="inspector-label">Avatar Icon</span>
                            <div style="display:grid; grid-template-columns: repeat(5, 1fr); gap:5px; margin-top:5px;">
                                ${['👤','👥','🏠','⚙️','🔔','📧','📁','📊','🚀','🌙','☀️','➕','✏️','🗑️','✅'].map(icon => 
                                    `<div class="icon-preset" style="cursor:pointer; padding:5px; text-align:center; border:1px solid ${props.icon === icon ? 'var(--jettra-accent)' : 'rgba(255,255,255,0.1)'}" onclick="updateProp('icon', '${icon}')">${icon}</div>`
                                ).join('')}
                            </div>
                            <input type="text" class="inspector-input" style="margin-top:5px" placeholder="Custom icon/unicode" value="${props.icon || ""}" onchange="updateProp('icon', this.value)">
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
                const props = JSON.parse(selectedItem.getAttribute('data-props'));
                const currentCode = props.events[evName] || "e -> { \\n    // Enter code here \\n}";
                
                const html = `
                    <div style="display:flex; flex-direction:column; gap:10px;">
                        <span style="font-size:12px; color:#aaa">Editing event handler for <b>${evName}</b></span>
                        <textarea id="event-code-editor" style="width:100%; height:200px; background:#0f172a; color:#00ffff; border:1px solid #00ffff; border-radius:8px; padding:10px; font-family:monospace; font-size:12px; outline:none; resize:none;">${currentCode.replace(/\\\\n/g, '\\n')}</textarea>
                        <div style="font-size:10px; color:#666">Supports Java Lambda syntax, e.g., e -> { ... }</div>
                    </div>
                `;

                window.show3DConfirm(
                    "Event Editor: " + evName,
                    html,
                    () => {
                        const newCode = document.getElementById('event-code-editor').value;
                        props.events[evName] = newCode.replace(/\\n/g, '\\\\n');
                        selectedItem.setAttribute('data-props', JSON.stringify(props));
                        window.updateGeneratedCode();
                        window.updateInspector();
                    }
                );
            };

            window.selectModel = function(name) {
                viewModelName = name;
                const model = availableModels.find(m => m.name === name);
                if (model) {
                    currentModel = model;
                    window.parseModelFields(model.file);
                    window.show3DConfirm("Model Attached", "Se ha vinculado el modelo " + name + " a la vista.", () => {});
                    // Auto close after 2s if no action? No, user asked for dialog.
                } else {
                    modelFields = [];
                    window.updateInspector();
                    window.updateGeneratedCode();
                }
            };

            window.parseModelFields = function(file) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    const content = e.target.result;
                    // Simple Regex to find private fields
                    const fieldRegex = /private\\s+\\w+\\s+(\\w+);/g;
                    let match;
                    modelFields = [];
                    while ((match = fieldRegex.exec(content)) !== null) {
                        modelFields.push(match[1]);
                    }
                    window.updateInspector();
                    window.updateGeneratedCode();
                };
                reader.readAsText(file);
            };

            window.updateProp = function(key, value) {
                if (!selectedItem) return;
                const props = JSON.parse(selectedItem.getAttribute('data-props'));
                props[key] = value;
                selectedItem.setAttribute('data-props', JSON.stringify(props));
                
                const type = selectedItem.getAttribute('data-type');
                if (key === 'text') {
                    const el = selectedItem.querySelector('h2, p, button, label, .j-avatar');
                    if (el) el.innerText = value;
                    if (type === 'Avatar') {
                        props.icon = "";
                        selectedItem.setAttribute('data-props', JSON.stringify(props));
                        setTimeout(window.updateInspector, 10);
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
                window.updateGeneratedCode();
            };

            var projectFilesMap = {};

            window.loadFiles = function(input) {
                const triggerLoad = () => {
                    const viewer = document.getElementById('explorer-tree-view');
                    if (!viewer) return;
                    viewer.innerHTML = 'Loading...';
                    
                    const files = input.querySelector('input').files;
                    const excludeTarget = input.getAttribute('data-exclude-target') === 'true';
                    
                    availableModels = [];
                    projectFilesMap = {};
                    const tree = {};

                    for (let i = 0; i < files.length; i++) {
                        const f = files[i];
                        const relPath = f.webkitRelativePath;
                        
                        if (excludeTarget && (relPath.includes('/target/') || relPath.includes('target/'))) continue;
                        if (!relPath.endsWith('.java') && !relPath.endsWith('.properties')) continue;

                        const parts = relPath.split('/');
                        let curr = tree;
                        for (let j = 0; j < parts.length; j++) {
                            const part = parts[j];
                            if (!curr[part]) curr[part] = { _children: {}, _file: null, _path: relPath };
                            if (j === parts.length - 1) {
                                curr[part]._file = f;
                                projectFilesMap[relPath] = f;
                            }
                            curr = curr[part]._children;
                        }

                        if (f.name.endsWith('Model.java')) {
                            availableModels.push({ name: f.name.replace('.java',''), file: f });
                        }
                    }

                    function renderTree(node, name, depth = 0) {
                        const childrenKeys = Object.keys(node._children);
                        const isFile = !!node._file;
                        if (!isFile && childrenKeys.length === 0) return "";

                        let cssClass = "";
                        if (name.endsWith('Page.java')) cssClass = "file-page";
                        if (name.endsWith('Model.java')) cssClass = "file-model";
                        if (name.endsWith('.properties')) cssClass = "file-props";

                        let action = isFile ? `onclick="window.openClass('${name}')" ondblclick="window.loadFileContent('${node._path}')"` : "";
                        let html = `<div class="project-file ${cssClass}" style="padding-left:${depth * 12}px" ${action}>${isFile ? '📄' : '📂'} ${name}</div>`;
                        for (const child in node._children) {
                            html += renderTree(node._children[child], child, depth + 1);
                        }
                        return html;
                    }

                    let finalHtml = "";
                    for (const root in tree) finalHtml += renderTree(tree[root], root);
                    viewer.innerHTML = finalHtml;
                    window.updateModelSelect();
                    window.updateInspector();
                };

                window.show3DConfirm(
                    "Explorador de Proyecto", 
                    "¿Desea cargar los archivos de esta carpeta al explorador?", 
                    triggerLoad
                );
            };

            window.loadFileContent = function(path) {
                const file = projectFilesMap[path];
                if (!file) return;

                const reader = new FileReader();
                reader.onload = function(e) {
                    const content = e.target.result;
                    document.getElementById('generated-code-display').innerText = content;
                    document.getElementById('generated-code-hidden').value = content;
                    
                    if (path.endsWith('Page.java')) {
                        window.show3DConfirm("Page Loaded", "Page content loaded into source view: " + path, () => {});
                    }
                };
                reader.readAsText(file);
            };

            window.show3DConfirm = function(title, body, yesAction) {
                const modal = document.getElementById('confirm-3d-modal');
                document.getElementById('confirm-title').innerText = title;
                document.getElementById('confirm-body').innerHTML = body;
                const yesBtn = document.getElementById('confirm-yes-btn');
                yesBtn.onclick = () => {
                   window.close3DConfirm();
                   yesAction();
                };
                modal.style.display = 'block';
                window.apply3DTracking(modal);
            };

            window.close3DConfirm = function() {
                const modal = document.getElementById('confirm-3d-modal');
                modal.style.display = 'none';
                modal.onmousemove = null;
                modal.style.transform = '';
            };

            window.apply3DTracking = function(el) {
                el.onmousemove = (e) => {
                    const rect = el.getBoundingClientRect();
                    const x = e.clientX - rect.left;
                    const y = e.clientY - rect.top;
                    const xc = rect.width / 2;
                    const yc = rect.height / 2;
                    const dx = x - xc;
                    const dy = y - yc;
                    el.style.transform = `perspective(2000px) rotateX(${-dy / 20}deg) rotateY(${dx / 20}deg) translateZ(50px)`;
                };
                el.onmouseleave = () => {
                    el.style.transform = `perspective(2000px) rotateX(0) rotateY(0) translateZ(0)`;
                };
            };

            window.updateModelSelect = function() {
                const container = document.getElementById('model-select-container');
                if (!container) return;
                
                let html = `<select class="inspector-input" onchange="window.selectModel(this.value)">`;
                html += `<option value="">-- Select Model --</option>`;
                availableModels.forEach(m => {
                    html += `<option value="${m.name}" ${viewModelName === m.name ? 'selected' : ''}>${m.name}</option>`;
                });
                html += `</select>`;
                container.innerHTML = html;
            };

            window.openClass = function(name) {
                const canvas = document.getElementById('canvas-area');
                if (!canvas) return;

                // Validation for {nombre}Page.java
                const isPage = name.endsWith('Page.java');
                const isModel = name.endsWith('Model.java');

                if (!isPage && !isModel && name.endsWith('.java')) {
                    // It's a java class but does NOT follow Page or Model syntax
                    const fileObj = projectFilesMap[name];
                    if (fileObj) window.loadFileContent(fileObj.webkitRelativePath);
                    return;
                }

                if (isPage) {
                    const items = canvas.querySelectorAll('.canvas-item');
                    const fileObj = projectFilesMap[name];

                    const doOpen = () => {
                        if (fileObj) {
                            window.loadFileContent(fileObj.webkitRelativePath);
                        }
                        // For demonstration, we clear the canvas since we don't have a real parser for the .java code yet
                        canvas.innerHTML = '<div class="canvas-placeholder">Start dragging components here to design ' + name + '</div>';
                        window.updateGeneratedCode();
                    };

                    if (items.length > 0) {
                        window.show3DConfirm(
                            "Confirmación 3D", 
                            "Hay elementos en el diseñador. ¿Desea borrarlos y abrir " + name + " en el Diseñador? Esto también mostrará el código.",
                            doOpen
                        );
                    } else {
                        // Si no hay ningun elemento en el diseñador agregado se muestra el contenido del codigo y se muestra el diseño en el diseñador.
                        doOpen();
                    }
                } else if (isModel) {
                    window.selectModel(name.replace('.java',''));
                }
            };

            window.generateCRUD = function() {
                const canvas = document.getElementById('canvas-area');
                if (!canvas) return;
                if (!currentModel) {
                    window.show3DConfirm("Error", "Please select a View Model in the Project Explorer first.", () => {});
                    return;
                }
                
                canvas.innerHTML = "";
                const placeholder = document.querySelector('.canvas-placeholder');
                if (placeholder) placeholder.remove();

                // 1. Title Header
                window.addComponentToCanvas('Header', canvas);
                const lastHeader = canvas.lastElementChild;
                lastHeader.setAttribute('data-props', JSON.stringify({text: currentModel.name + " Management", columns: 2, events: {}, binding: ""}));
                const h2 = lastHeader.querySelector('h2');
                if (h2) h2.innerText = currentModel.name + " Management";

                // 2. Action Bar (Grid with Add Button)
                window.addComponentToCanvas('Grid', canvas);
                const grid = canvas.lastElementChild;
                const gridContainer = grid.querySelector('.canvas-container');
                grid.setAttribute('data-props', JSON.stringify({text: "Actions", columns: 2, events: {}, binding: ""}));
                
                window.addComponentToCanvas('Button', gridContainer);
                const addBtn = gridContainer.lastElementChild;
                const modalId = currentModel.name.toLowerCase() + "-modal-" + Math.floor(Math.random()*1000);
                addBtn.setAttribute('data-props', JSON.stringify({text: "⚡ Add New " + currentModel.name, columns: 2, events: {onClick: "e -> { \\n    document.getElementById('" + modalId + "').style.display = 'block';\\n}"}, binding: ""}));
                const btn = addBtn.querySelector('button');
                if (btn) {
                    btn.innerText = "⚡ Add New " + currentModel.name;
                    btn.className = "j-btn-primary btn-3d";
                }

                // 3. Data Table
                window.addComponentToCanvas('Table', canvas);
                const table = canvas.lastElementChild;
                table.setAttribute('data-props', JSON.stringify({text: currentModel.name + " List", columns: 2, events: {}, binding: ""}));

                // 4. Modal with Form Fields
                window.addComponentToCanvas('Modal', canvas);
                const modal = canvas.lastElementChild;
                modal.id = modalId;
                modal.setAttribute('data-props', JSON.stringify({text: "Enter " + currentModel.name + " Details", columns: 2, events: {}, binding: ""}));
                const modalContainer = modal.querySelector('.canvas-container') || modal;
                
                // Add fields from model
                modelFields.forEach(field => {
                    const fieldRow = document.createElement('div');
                    fieldRow.style.marginBottom = "10px";
                    fieldRow.innerHTML = `<label style="font-size:10px; color:#aaa; display:block">${field}</label>`;
                    modalContainer.appendChild(fieldRow);
                    
                    window.addComponentToCanvas('TextBox', modalContainer);
                    const tb = modalContainer.lastElementChild;
                    tb.setAttribute('data-props', JSON.stringify({text: field, columns: 2, events: {}, binding: field}));
                });

                // Modal Actions (Save/Cancel)
                const modalActions = document.createElement('div');
                modalActions.style.display = "flex";
                modalActions.style.gap = "10px";
                modalActions.style.marginTop = "20px";
                modalContainer.appendChild(modalActions);

                window.addComponentToCanvas('Button', modalActions);
                const saveBtn = modalActions.lastElementChild;
                saveBtn.setAttribute('data-props', JSON.stringify({text: "Save " + currentModel.name, columns: 2, events: {onClick: "e -> { \\n    window.show3DConfirm('Data Saved', 'Saving " + currentModel.name + " to database...', () => {}); \\n    document.getElementById('" + modalId + "').style.display = 'none'; \\n}"}, binding: ""}));
                
                window.addComponentToCanvas('Button', modalActions);
                const cancelBtn = modalActions.lastElementChild;
                cancelBtn.setAttribute('data-props', JSON.stringify({text: "Cancel", columns: 2, events: {onClick: "e -> { \\n    document.getElementById('" + modalId + "').style.display = 'none'; \\n}"}, binding: ""}));

                window.show3DConfirm(
                    "CRUD Generado", 
                    "Se ha generado automáticamente el layout CRUD para " + currentModel.name + ". Hemos añadido " + modelFields.length + " campos vinculados al modelo.",
                    () => {}
                );
                window.updateGeneratedCode();
            };

            window.clearDesigner = function() {
                window.show3DConfirm(
                    "Clear Canvas", 
                    "Are you sure you want to delete all elements and start over?", 
                    () => {
                        const canvas = document.getElementById('canvas-area');
                        if (!canvas) return;
                        canvas.innerHTML = '<div class="canvas-placeholder">Start dragging components here to design</div>';
                        
                        document.getElementById('generated-code-display').innerText = "// Designer Cleared";
                        document.getElementById('generated-code-hidden').value = "";
                        
                        selectedItem = null;
                        currentModel = null;
                        window.updateInspector();
                        window.show3DConfirm("Canvas Cleared", "Designer session has been reset.", () => {});
                    }
                );
            };

            window.updateGeneratedCode = function() {
                const display = document.getElementById('generated-code-display');
                const hidden = document.getElementById('generated-code-hidden');
                if (!display || !hidden) return;

                const canvasItems = document.querySelectorAll('#canvas-area > .canvas-item, .canvas-container > .canvas-item');
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
                        const v = type.toLowerCase() + "_" + Math.floor(Math.random()*1000);
                        
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
                            case 'Header': out += `        ${container}.add(new Header(1, "${props.text}"));\\n`; break;
                            case 'Paragraph': out += `        ${container}.add(new Paragraph("${props.text}"));\\n`; break;
                            case 'Divide': out += `        ${container}.add(new Divide());\\n`; break;
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
                            case 'Panel':
                            case 'Grid':
                                out += `        ${type} ${v} = new ${type}(${props.columns});\\n`;
                                const kids = it.querySelectorAll(':scope > .canvas-container > .canvas-item');
                                if (kids.length > 0) out += walk(kids, v);
                                out += handleCommon(v);
                                out += handleEvents(v);
                                out += `        ${container}.add(${v});\\n`;
                                break;
                            default: 
                                out += `        ${type} ${v} = new ${type}();\\n`;
                                out += handleCommon(v);
                                out += handleEvents(v);
                                out += `        ${container}.add(${v});\\n`;
                        }
                    });
                    return out;
                }
                code += walk(document.querySelectorAll('#canvas-area > .canvas-item'), "center");
                code += `    }\\n}`;
                
                display.innerText = code;
                hidden.value = code;
            };

        """);
        
        center.add(style);
        center.add(script);
    }
}
