package com.jettra.example.pages;

import io.jettra.wui.complex.Center;
import io.jettra.wui.components.*;
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
        canvasArea.setStyle("flex", "1").setStyle("background", "rgba(0,0,0,0.3)").setStyle("border", "2px dashed var(--jettra-accent)").setStyle("border-radius", "8px").setStyle("padding", "20px").setStyle("position", "relative").setStyle("overflow-y", "auto");
        
        Header canvasPlaceholder = new Header(3, "Visual Design Canvas");
        canvasPlaceholder.addClass("canvas-placeholder");
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
        addPaletteCategory(palette, "Forms", new String[]{"Button", "CheckBox", "Form", "RadioButton", "SelectOne", "SelectOneIcon", "Spinner", "TextBox", "ToggleSwitch"});
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
            .palette-category { animation: fadeIn 0.5s ease-out; }
            .inspector-row { margin-bottom: 12px; }
            .inspector-label { display: block; font-size: 11px; color: #999; margin-bottom: 4px; }
            .inspector-input { width: 100%; background: #111; border: 1px solid #444; color: #fff; padding: 5px; border-radius: 3px; font-size: 12px; }
            .project-file { padding: 4px 8px; font-size: 12px; color: #ccc; cursor: pointer; border-radius: 3px; display: flex; align-items: center; gap: 8px; }
            .project-file:hover { background: rgba(0,255,255,0.1); color: #fff; }
            .canvas-container { border: 1px dashed #555; padding: 15px; border-radius: 6px; position: relative; min-height: 60px; }
            .view-model-row { margin-bottom: 15px; padding-bottom: 10px; border-bottom: 1px solid #333; }
            @keyframes fadeIn { from { opacity: 0; transform: translateY(5px); } to { opacity: 1; transform: translateY(0); } }
        """);
        
        Script script = new Script("""
            let selectedItem = null;
            let viewModelName = "BaseViewModel";

            function drag(ev) {
                ev.dataTransfer.setData("type", ev.target.getAttribute("data-type"));
            }

            const canvas = document.getElementById('canvas-area');
            canvas.ondragover = function(ev) {
                ev.preventDefault();
            };

            canvas.ondrop = function(ev) {
                ev.preventDefault();
                const type = ev.dataTransfer.getData("type");
                
                // Nesting support: check if dropped into a container
                const target = ev.target.closest('.canvas-container');
                if (target) {
                    addComponentToCanvas(type, target);
                } else {
                    addComponentToCanvas(type, canvas);
                }
            };

            function addComponentToCanvas(type, parent = canvas) {
                const placeholder = document.querySelector('.canvas-placeholder');
                if (placeholder) placeholder.style.display = 'none';

                const wrapper = document.createElement('div');
                wrapper.className = 'canvas-item';
                wrapper.setAttribute('data-type', type);
                wrapper.setAttribute('data-props', JSON.stringify({text: type, columns: 2}));
                
                wrapper.onclick = (e) => {
                    e.stopPropagation();
                    selectElement(wrapper);
                };

                // Context Menu for attributes
                wrapper.oncontextmenu = (e) => {
                    e.preventDefault();
                    e.stopPropagation();
                    selectElement(wrapper);
                    showInspector();
                };
                
                let content = '';
                switch(type) {
                    case 'Header': content = '<h2>New Header</h2>'; break;
                    case 'Paragraph': content = '<p style="color:#ccc">Sample paragraph text content...</p>'; break;
                    case 'Label': content = '<label style="color:#aaa; font-weight:bold">Field Label:</label>'; break;
                    case 'Separator': content = '<hr style="border:0; border-top:1px solid rgba(0,255,255,0.3); margin:10px 0">'; break;
                    case 'Divide': content = '<div style="border-top:2px solid var(--jettra-accent); margin:15px 0; opacity:0.5; width:100%; box-shadow:0 0 5px var(--jettra-glow)"></div>'; break;
                    case 'Button': content = '<button class="j-btn-primary" style="padding:8px 15px; border-radius:4px; background:var(--jettra-accent); color:#000; border:none; cursor:pointer">Interactive Button</button>'; break;
                    case 'TextBox': content = '<input type="text" placeholder="Type something..." style="padding:10px; background:#111; color:#fff; border:1px solid #444; border-radius:4px; width:100%">'; break;
                    case 'ToggleSwitch': content = '<div style="display:flex; gap:10px; color:#fff"><span>Off</span><div style="width:40px;height:20px;background:#333;border-radius:10px;position:relative"><div style="width:18px;height:18px;background:#0ff;border-radius:50%;position:absolute;right:1px;top:1px"></div></div><span>On</span></div>'; break;
                    case 'Panel': 
                        content = '<div class="canvas-container" style="display:grid; grid-template-columns:1fr 1fr; gap:10px"></div>'; 
                        break;
                    case 'Tree': content = '<div style="color:#0ff; font-size:12px">▶ Tree Root<div style="padding-left:15px; color:#aaa; font-size:11px">▶ Sub Item 1</div></div>'; break;
                    default: content = `<div style="padding:10px; border:1px solid #444; color:#888">${type} Placeholder</div>`;
                }

                wrapper.innerHTML = content + '<div class="delete-tool" onclick="this.parentElement.remove(); updateGeneratedCode();">×</div>';
                parent.appendChild(wrapper);
                updateGeneratedCode();
            }

            function selectElement(el) {
                if (selectedItem) selectedItem.classList.remove('selected');
                selectedItem = el;
                selectedItem.classList.add('selected');
                updateInspector();
            }

            function showInspector() {
                document.getElementById('property-inspector').style.display = 'flex';
            }

            function updateInspector() {
                if (!selectedItem) return;
                const type = selectedItem.getAttribute('data-type');
                const props = JSON.parse(selectedItem.getAttribute('data-props'));
                const inspector = document.getElementById('inspector-properties');
                
                let html = `
                    <div class="view-model-row">
                        <span class="inspector-label">View Model</span>
                        <input type="text" class="inspector-input" value="${viewModelName}" onchange="viewModelName=this.value; updateGeneratedCode();">
                    </div>
                    <div class="inspector-row">
                        <span class="inspector-label">Component Type</span>
                        <div style="color:var(--jettra-accent); font-weight:bold">${type}</div>
                    </div>
                    <div class="inspector-row">
                        <span class="inspector-label">Text Content</span>
                        <input type="text" class="inspector-input" value="${props.text || ""}" onchange="updateProp('text', this.value)">
                    </div>
                `;

                if (type === 'Panel' || type === 'Grid') {
                    html += `
                        <div class="inspector-row">
                            <span class="inspector-label">Columns</span>
                            <input type="number" class="inspector-input" value="${props.columns || 2}" min="1" max="12" onchange="updateProp('columns', this.value)">
                        </div>
                    `;
                }

                html += `
                    <div class="inspector-row">
                        <span class="inspector-label">Events</span>
                        <select class="inspector-input" onchange="addEvent(this.value)">
                            <option value="">Add Event...</option>
                            <option value="onClick">onClick</option>
                            <option value="onChange">onChange</option>
                        </select>
                        <div id="event-list" style="margin-top:5px; font-size:10px; color:#aaa"></div>
                    </div>
                `;

                inspector.innerHTML = html;
            }

            function updateProp(key, value) {
                if (!selectedItem) return;
                const props = JSON.parse(selectedItem.getAttribute('data-props'));
                props[key] = value;
                selectedItem.setAttribute('data-props', JSON.stringify(props));
                
                // Live Update Canvas
                const type = selectedItem.getAttribute('data-type');
                if (key === 'text') {
                    if (type === 'Header') selectedItem.querySelector('h2').innerText = value;
                    if (type === 'Paragraph') selectedItem.querySelector('p').innerText = value;
                    if (type === 'Button') selectedItem.querySelector('button').innerText = value;
                    if (type === 'Label') selectedItem.querySelector('label').innerText = value + ":";
                }
                if (key === 'columns' && (type === 'Panel' || type === 'Grid')) {
                    selectedItem.querySelector('.canvas-container').style.gridTemplateColumns = `repeat(${value}, 1fr)`;
                }
                
                updateGeneratedCode();
            }

            function addEvent(name) {
                if (!name) return;
                const list = document.getElementById('event-list');
                const div = document.createElement('div');
                div.innerHTML = `⚡ ${name} code generated`;
                list.appendChild(div);
                updateGeneratedCode();
            }

            function loadFiles(input) {
                const viewer = document.getElementById('explorer-tree-view');
                viewer.innerHTML = '<div style="color:var(--jettra-accent); font-size:11px">Loading files...</div>';
                
                const files = input.querySelector('input').files;
                const root = {};

                for (let i = 0; i < files.length; i++) {
                    const path = files[i].webkitRelativePath.split('/');
                    let current = root;
                    for (const seg of path) {
                        if (!current[seg]) current[seg] = {};
                        current = current[seg];
                    }
                }

                function buildTreeHtml(node, name, depth = 0) {
                    const isFile = Object.keys(node).length === 0 || name.endsWith('.java');
                    if (isFile) {
                        return `<div class="project-file" style="padding-left:${depth * 15}px" onclick="openFile('${name}')">📄 ${name}</div>`;
                    }
                    let html = `<div style="padding-left:${depth * 15}px; color:#fff; font-size:12px; margin:4px 0">📂 <strong>${name}</strong></div>`;
                    for (const child in node) {
                        html += buildTreeHtml(node[child], child, depth + 1);
                    }
                    return html;
                }

                viewer.innerHTML = buildTreeHtml(root, "Project Root");
            }

            function openFile(name) {
                alert("Opening " + name + " in designer... (Simulation)");
                // In a real app, we would fetch the class content and populate the canvas
            }

            function updateGeneratedCode() {
                const canvasItems = document.querySelectorAll('#canvas-area > .canvas-item, .canvas-container > .canvas-item');
                let code = 'package com.jettra.example.pages;\\n\\n';
                code += 'import io.jettra.wui.complex.*;\\n';
                code += 'import io.jettra.wui.components.*;\\n';
                code += 'import java.util.Map;\\n\\n';
                code += '/**\\n * Generated by JettraWebDesigner\\n */\\n';
                code += `public class MyNewPage extends DashboardBasePage {\\n\\n`;
                code += `    private ${viewModelName} model = ${viewModelName}.getInstance();\\n\\n`;
                code += '    public MyNewPage() {\\n';
                code += '        super("Dynamic Page Design");\\n';
                code += '    }\\n\\n';
                code += '    @Override\\n';
                code += '    protected void initCenter(Center center, String username) {\\n';
                
                function processNode(items, containerVar = "center") {
                   let out = "";
                   items.forEach(item => {
                        const type = item.getAttribute('data-type');
                        const props = JSON.parse(item.getAttribute('data-props'));
                        const varName = type.toLowerCase() + "_" + Math.floor(Math.random()*1000);
                        
                        switch(type) {
                            case 'Header': out += `        ${containerVar}.add(new Header(2, "${props.text}"));\\n`; break;
                            case 'Paragraph': out += `        ${containerVar}.add(new Paragraph("${props.text}"));\\n`; break;
                            case 'Divide': out += `        ${containerVar}.add(new Divide());\\n`; break;
                            case 'Button': 
                                out += `        Button ${varName} = new Button("${props.text}");\\n`;
                                out += `        ${varName}.onAction(e -> { /* logic */ });\\n`;
                                out += `        ${containerVar}.add(${varName});\\n`;
                                break;
                            case 'Panel':
                                out += `        Panel ${varName} = new Panel(${props.columns});\\n`;
                                const children = item.querySelectorAll(':scope > .canvas-container > .canvas-item');
                                if (children.length > 0) {
                                    out += processNode(children, varName);
                                }
                                out += `        ${containerVar}.add(${varName});\\n`;
                                break;
                            default: out += `        ${containerVar}.add(new ${type}());\\n`;
                        }
                   });
                   return out;
                }

                code += processNode(document.querySelectorAll('#canvas-area > .canvas-item'));
                code += '    }\\n';
                code += '}';

                document.getElementById('generated-code-display').innerText = code;
                document.getElementById('generated-code-hidden').value = code;
            }
        """);
        
        center.add(style);
        center.add(script);
    }
}
