package com.jettra.example.pages;

import io.jettra.wui.complex.Center;
import io.jettra.wui.complex.Modal;
import io.jettra.wui.complex.Tree;
import io.jettra.wui.complex.Panel;
import io.jettra.wui.components.*;
import io.jettra.wui.components.TextArea;
import io.jettra.wui.core.annotations.InjectProperties;
import java.util.Properties;

/**
 * JettraWebDesigner: A visual designer for JettraWUI interfaces.
 */
public class WebDesignerPage extends DashboardBasePage {

    @InjectProperties(name = "messages")
    private Properties msg;

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
        
        actions.add(saveBtn).add(openBtn);
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
        eventModal.setStyle("display", "none").setStyle("background", "var(--jettra-glass)").setStyle("backdrop-filter", "blur(15px)").setStyle("padding", "30px").setStyle("border-radius", "12px").setStyle("width", "700px").setStyle("border", "1px solid var(--jettra-accent)");
        
        Header modalHeader = new Header(3, "⚡ Event Handler Editor");
        modalHeader.setStyle("color", "var(--jettra-accent)").setStyle("margin-top", "0");
        
        TextArea codeInput = new TextArea("event-code-input", "e -> { \n    // Write your Java code here\n}");
        codeInput.setStyle("width", "100%").setStyle("height", "300px").setStyle("background", "#000").setStyle("color", "#0ff").setStyle("font-family", "monospace").setStyle("padding", "15px").setStyle("border", "1px solid #333").setStyle("border-radius", "4px");
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px").setStyle("margin-top", "20px");
        
        Button cancelBtn = new Button("Cancel");
        cancelBtn.addClass("j-btn");
        cancelBtn.setProperty("type", "button");
        cancelBtn.setProperty("onclick", "document.getElementById('event-editor-modal').style.display = 'none'");
        
        Button saveEventBtn = new Button("Save Handler");
        saveEventBtn.addClass("j-btn-primary");
        saveEventBtn.setProperty("onclick", "saveEventHandler()");
        
        modalActions.add(cancelBtn).add(saveEventBtn);
        eventModal.add(modalHeader).add(codeInput).add(modalActions);
        
        center.add(eventModal);
        
        saveForm.add(container);
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
        folderSel.excludeTarget(true);
        folderSel.setStyle("width", "100%").setStyle("margin-bottom", "10px");
        
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
        addPaletteCategory(palette, "Typography", new String[]{"Header", "Paragraph", "Span", "Label", "Separator"});
        // Forms
        addPaletteCategory(palette, "Forms", new String[]{"Button", "CheckBox", "Form", "RadioButton", "SelectOne", "SelectOneIcon", "Spinner", "TextBox", "TextArea", "ToggleSwitch"});
        // Navigation
        addPaletteCategory(palette, "Navigation", new String[]{"Link", "Menu", "MenuBar"});
        // Feedback
        addPaletteCategory(palette, "Feedback", new String[]{"Alert", "Modal", "Notification", "SessionTimeout"});
        // Layout & Display
        addPaletteCategory(palette, "Layout & Display", new String[]{"Avatar", "AvatarGroup", "Carousel", "Datatable", "Div", "Divide", "FileUpload", "FolderSelector", "Grid", "Image", "Panel", "Table", "TabView", "Tree"});

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
            .inspector-row { margin-bottom: 12px; }
            .inspector-label { display: block; font-size: 11px; color: #999; margin-bottom: 4px; }
            .inspector-input { width: 100%; background: #111; border: 1px solid #444; color: #fff; padding: 5px; border-radius: 3px; font-size: 12px; }
            .project-file { padding: 4px 8px; font-size: 12px; color: #ccc; cursor: pointer; border-radius: 3px; display: flex; align-items: center; gap: 8px; }
            .project-file:hover { background: rgba(0,255,255,0.1); color: #fff; }
            .file-page { color: #f1c40f !important; font-weight: bold; }
            .file-model { color: #2ecc71 !important; font-weight: bold; }
            .canvas-container { border: 1px dashed #555; padding: 15px; border-radius: 6px; position: relative; min-height: 60px; }
            .view-model-row { margin-bottom: 15px; padding-bottom: 10px; border-bottom: 1px solid #333; }
            .btn-event { background: var(--jettra-accent); color: #000; border: none; padding: 4px 8px; border-radius: 3px; font-size: 10px; cursor: pointer; margin-top: 5px; }
            @keyframes fadeIn { from { opacity: 0; transform: translateY(5px); } to { opacity: 1; transform: translateY(0); } }
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
                    case 'Button': content = '<button class="j-btn-primary">Interactive Button</button>'; break;
                    case 'TextBox': content = '<input type="text" placeholder="TextBox..." style="width:100%; padding:10px; background:#111; color:#fff; border:1px solid #444; border-radius:4px">'; break;
                    case 'Panel': 
                    case 'Grid':
                        content = '<div class="canvas-container" style="display:grid; grid-template-columns:1fr 1fr; gap:10px"></div>'; 
                        break;
                    default: content = `<div style="padding:10px; border:1px solid #444; color:#888">${type} Placeholder</div>`;
                }

                wrapper.innerHTML = content + '<div class="delete-tool" onclick="this.parentElement.remove(); updateGeneratedCode();">×</div>';
                parent.appendChild(wrapper);
                updateGeneratedCode();
            }

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

                const supportedEvents = ['onClick', 'onChange', 'onAction'];
                html += `
                    <div class="inspector-row">
                        <span class="inspector-label">Events</span>
                        ${supportedEvents.map(ev => `
                            <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:5px">
                                <span style="font-size:10px">${ev}</span>
                                <button class="btn-event" onclick="openEventEditor('${ev}')">Edit Handler</button>
                            </div>
                        `).join('')}
                    </div>
                `;

                inspector.innerHTML = html;
            }

            window.selectModel = function(name) {
                viewModelName = name;
                const model = availableModels.find(m => m.name === name);
                if (model) {
                    currentModel = model;
                    window.parseModelFields(model.file);
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

            window.openEventEditor = function(eventName) {
                activeEventProperty = eventName;
                const props = JSON.parse(selectedItem.getAttribute('data-props'));
                const existingCode = props.events[eventName] || "e -> { \\n    // Code for " + eventName + "\\n}";
                document.querySelector('#event-code-input textarea').value = existingCode;
                document.getElementById('event-editor-modal').style.display = 'block';
            };

            window.saveEventHandler = function() {
                const code = document.querySelector('#event-code-input textarea').value;
                const props = JSON.parse(selectedItem.getAttribute('data-props'));
                props.events[activeEventProperty] = code;
                selectedItem.setAttribute('data-props', JSON.stringify(props));
                document.getElementById('event-editor-modal').style.display = 'none';
                window.updateGeneratedCode();
            };

            window.updateProp = function(key, value) {
                if (!selectedItem) return;
                const props = JSON.parse(selectedItem.getAttribute('data-props'));
                props[key] = value;
                selectedItem.setAttribute('data-props', JSON.stringify(props));
                
                const type = selectedItem.getAttribute('data-type');
                if (key === 'text') {
                    const el = selectedItem.querySelector('h2, p, button, label');
                    if (el) el.innerText = value;
                }
                if (key === 'columns' && (type === 'Panel' || type === 'Grid')) {
                    const container = selectedItem.querySelector('.canvas-container');
                    if (container) container.style.gridTemplateColumns = `repeat(${value}, 1fr)`;
                }
                window.updateGeneratedCode();
            };

            var projectFilesMap = {};

            window.loadFiles = function(input) {
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
                window.updateInspector();
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
                        alert("Page content loaded into source view: " + path);
                    }
                };
                reader.readAsText(file);
            };

            window.openClass = function(name) {
                const canvas = document.getElementById('canvas-area');
                if (!canvas) return;
                if (name.endsWith('Page.java')) {
                   alert("Opening Page: " + name + ". Clearing canvas for new design.");
                   canvas.innerHTML = '<div class="canvas-placeholder">Start dragging components here to design ' + name + '</div>';
                   window.updateGeneratedCode();
                } else if (name.endsWith('Model.java')) {
                   window.selectModel(name.replace('.java',''));
                }
            };

            window.generateCRUD = function() {
                const canvas = document.getElementById('canvas-area');
                if (!canvas) return;
                if (!currentModel) {
                    alert("Please select a View Model in the Project Explorer first.");
                    return;
                }
                
                canvas.innerHTML = "";
                const placeholder = document.querySelector('.canvas-placeholder');
                if (placeholder) placeholder.remove();

                window.addComponentToCanvas('Header', canvas);
                const lastHeader = canvas.lastElementChild;
                lastHeader.setAttribute('data-props', JSON.stringify({text: currentModel.name + " Management", columns: 2, events: {}, binding: ""}));
                const h2 = lastHeader.querySelector('h2');
                if (h2) h2.innerText = currentModel.name + " Management";

                window.addComponentToCanvas('Grid', canvas);
                const grid = canvas.lastElementChild;
                const gridContainer = grid.querySelector('.canvas-container');
                
                window.addComponentToCanvas('Button', gridContainer);
                const addBtn = gridContainer.lastElementChild;
                addBtn.setAttribute('data-props', JSON.stringify({text: "Add New " + currentModel.name, columns: 2, events: {onClick: "e -> { \\n    document.getElementById('" + currentModel.name.toLowerCase() + "-modal').style.display = 'block';\\n}"}, binding: ""}));
                const btn = addBtn.querySelector('button');
                if (btn) btn.innerText = "Add New " + currentModel.name;

                window.addComponentToCanvas('Table', canvas);
                window.addComponentToCanvas('Modal', canvas);
                const modal = canvas.lastElementChild;
                modal.setAttribute('data-props', JSON.stringify({text: "Edit " + currentModel.name, columns: 2, events: {}, binding: ""}));
                
                alert("CRUD layout generated for " + currentModel.name + ". Fields detected: " + modelFields.join(", "));
                window.updateGeneratedCode();
            };

            window.updateGeneratedCode = function() {
                const display = document.getElementById('generated-code-display');
                const hidden = document.getElementById('generated-code-hidden');
                if (!display || !hidden) return;

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
                        
                        switch(type) {
                            case 'Header': out += `        ${container}.add(new Header(1, "${props.text}"));\\n`; break;
                            case 'Paragraph': out += `        ${container}.add(new Paragraph("${props.text}"));\\n`; break;
                            case 'Divide': out += `        ${container}.add(new Divide());\\n`; break;
                            case 'Button': 
                                out += `        Button ${v} = new Button("${props.text}");\\n`;
                                Object.keys(props.events).forEach(ev => {
                                    const eventCode = props.events[ev].replace(/\\\\n/g, '\\n');
                                    out += `        ${v}.${ev}(${eventCode});\\n`;
                                });
                                out += `        ${container}.add(${v});\\n`;
                                break;
                            case 'TextBox':
                                out += `        TextBox ${v} = new TextBox("${v}", "Enter value");\\n`;
                                if (props.binding) out += `        ${v}.setValue(model.get${props.binding.charAt(0).toUpperCase() + props.binding.slice(1)}());\\n`;
                                out += `        ${container}.add(${v});\\n`;
                                break;
                            case 'TextArea':
                                out += `        TextArea ${v} = new TextArea("${v}", "");\\n`;
                                if (props.binding) out += `        ${v}.setValue(model.get${props.binding.charAt(0).toUpperCase() + props.binding.slice(1)}());\\n`;
                                out += `        ${container}.add(${v});\\n`;
                                break;
                            case 'Panel':
                            case 'Grid':
                                out += `        ${type} ${v} = new ${type}(${props.columns});\\n`;
                                const kids = it.querySelectorAll(':scope > .canvas-container > .canvas-item');
                                if (kids.length > 0) out += walk(kids, v);
                                out += `        ${container}.add(${v});\\n`;
                                break;
                            default: out += `        ${container}.add(new ${type}());\\n`;
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
