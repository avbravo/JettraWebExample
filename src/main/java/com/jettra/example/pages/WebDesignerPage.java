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

        // 1. Palette
        Div palette = createCategorizedPalette();
        palette.setStyle("flex", "0 0 250px").setStyle("background", "rgba(20,30,50,0.8)").setStyle("border", "1px solid var(--jettra-accent)").setStyle("padding", "15px").setStyle("border-radius", "8px").setStyle("overflow-y", "auto");

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

        container.add(palette).add(canvasArea).add(codeView);
        saveForm.add(container);
        center.add(saveForm);

        // Add Designer Scripts and Styles
        setupDesignerAssets(center);
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
        addPaletteCategory(palette, "Layout & Display", new String[]{"Avatar", "AvatarGroup", "Carousel", "Datatable", "Div", "Grid", "Image", "Table", "TabView"});

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
            .canvas-item { position: relative; margin-bottom: 20px; border: 1px transparent dashed; transition: border 0.2s; min-height: 20px; padding: 10px; border-radius: 4px; }
            .canvas-item:hover { border-color: rgba(0,255,255,0.4); background: rgba(0,255,255,0.02); }
            .canvas-item .delete-tool { position: absolute; top: -8px; right: -8px; background: #ff4444; color: white; width: 18px; height: 18px; border-radius: 50%; display: none; justify-content: center; align-items: center; cursor: pointer; font-size: 10px; z-index: 100; box-shadow: 0 2px 5px rgba(0,0,0,0.5); }
            .canvas-item:hover .delete-tool { display: flex; }
            .palette-category { animation: fadeIn 0.5s ease-out; }
            @keyframes fadeIn { from { opacity: 0; transform: translateY(5px); } to { opacity: 1; transform: translateY(0); } }
        """);
        
        Script script = new Script("""
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
                addComponentToCanvas(type);
            };

            function addComponentToCanvas(type) {
                const placeholder = document.querySelector('.canvas-placeholder');
                if (placeholder) placeholder.style.display = 'none';

                const wrapper = document.createElement('div');
                wrapper.className = 'canvas-item';
                wrapper.setAttribute('data-type', type);
                
                let content = '';
                switch(type) {
                    // Typography
                    case 'Header': content = '<h2>New Header</h2>'; break;
                    case 'Paragraph': content = '<p style="color:#ccc">Sample paragraph text content for your page...</p>'; break;
                    case 'Span': content = '<span style="color:#0ff">Sample Span</span>'; break;
                    case 'Label': content = '<label style="color:#aaa; font-weight:bold">Field Label:</label>'; break;
                    case 'Separator': content = '<hr style="border:0; border-top:1px solid rgba(0,255,255,0.3); margin:10px 0">'; break;
                    
                    // Forms
                    case 'Button': content = '<button class="j-btn-primary" style="padding:8px 15px; border-radius:4px; background:var(--jettra-accent); color:#000; border:none; cursor:pointer">Interactive Button</button>'; break;
                    case 'CheckBox': content = '<label style="color:#fff"><input type="checkbox"> Option Check</label>'; break;
                    case 'Form': content = '<div style="border:1px solid #444; padding:15px; border-radius:5px; background:rgba(255,255,255,0.05)">[ Form Container ]</div>'; break;
                    case 'RadioButton': content = '<label style="color:#fff"><input type="radio" name="options"> Radio Selection</label>'; break;
                    case 'SelectOne': content = '<select style="padding:8px; background:#222; color:#fff; border:1px solid #444; width:100%"><option>Select Option...</option></select>'; break;
                    case 'SelectOneIcon': content = '<div style="display:flex; align-items:center; gap:10px; background:#222; padding:8px; border-radius:4px; border:1px solid #444">🌐 <span>Icon Selection</span></div>'; break;
                    case 'Spinner': content = '<div style="display:flex; align-items:center; gap:10px"><div style="width:20px;height:20px;border:2px solid #0ff;border-top-color:transparent;border-radius:50%;animation:spin 1s linear infinite"></div><span style="color:#0ff">Loading...</span></div><style>@keyframes spin {100%{transform:rotate(360deg)}}</style>'; break;
                    case 'TextBox': content = '<input type="text" placeholder="Type something..." style="padding:10px; background:#111; color:#fff; border:1px solid #444; border-radius:4px; width:100%">'; break;
                    case 'ToggleSwitch': content = '<div style="display:flex; gap:10px; color:#fff"><span>Off</span><div style="width:40px;height:20px;background:#333;border-radius:10px;position:relative"><div style="width:18px;height:18px;background:#0ff;border-radius:50%;position:absolute;right:1px;top:1px"></div></div><span>On</span></div>'; break;
                    
                    // Navigation
                    case 'Link': content = '<a href="#" style="color:#0ff; text-decoration:none">→ Hyperlink Source</a>'; break;
                    case 'Menu': content = '<div style="background:#222; border:1px solid #444; border-radius:4px; overflow:hidden"><div style="padding:8px; border-bottom:1px solid #333; color:#fff">Menu Item 1</div><div style="padding:8px; color:#fff">Menu Item 2</div></div>'; break;
                    case 'MenuBar': content = '<div style="display:flex; gap:20px; background:#1a1a1a; padding:10px; border-radius:4px; border:1px solid #444; color:#fff"><span>Home</span><span>Features</span><span>Contact</span></div>'; break;
                    
                    // Feedback
                    case 'Alert': content = '<div style="padding:12px; background:rgba(0,255,255,0.1); border-left:4px solid #0ff; color:#fff; border-radius:2px"><strong>Success!</strong> Your action was registered.</div>'; break;
                    case 'Modal': content = '<div style="border:1px solid #0ff; padding:20px; border-radius:8px; background:rgba(0,0,0,0.8); text-align:center"><div style="color:#0ff; margin-bottom:10px">Modal Preview</div><button class="j-btn-secondary">Close</button></div>'; break;
                    case 'Notification': content = '<div style="position:relative; width:200px; padding:10px; background:#333; border-radius:4px; color:#fff; font-size:12px; border-left:4px solid #0ff">New message received!</div>'; break;
                    case 'SessionTimeout': content = '<div style="padding:10px; background:rgba(255,0,0,0.1); border:1px solid #f44; color:#f44; border-radius:4px; font-size:11px">⚠️ Session will expire in 60s</div>'; break;
                    
                    // Layout & Display
                    case 'Avatar': content = '<div style="width:40px;height:40px;background:#0ff;color:#000;border-radius:50%;display:flex;justify-content:center;align-items:center;font-weight:bold">JD</div>'; break;
                    case 'AvatarGroup': content = '<div style="display:flex; -webkit-mask:none"><div style="width:30px;height:30px;background:#0ff;border-radius:50%;border:2px solid #000;z-index:3"></div><div style="width:30px;height:30px;background:#0af;border-radius:50%;border:2px solid #000;margin-left:-10px;z-index:2"></div><div style="width:30px;height:30px;background:#05f;border-radius:50%;border:2px solid #000;margin-left:-10px;z-index:1"></div></div>'; break;
                    case 'Carousel': content = '<div style="height:100px; background:#111; border:1px solid #444; border-radius:4px; display:flex; justify-content:center; align-items:center; color:#666">Carousel Slider</div>'; break;
                    case 'Datatable': content = '<table style="width:100%; border-collapse:collapse; color:#fff; font-size:12px"><tr style="background:#222"><th style="padding:8px; border:1px solid #444">ID</th><th style="padding:8px; border:1px solid #444">Name</th></tr><tr><td style="padding:8px; border:1px solid #444">1</td><td style="padding:8px; border:1px solid #444">Admin</td></tr></table>'; break;
                    case 'Div': content = '<div style="height:50px; border:1px solid #555; border-radius:4px; background:rgba(255,255,255,0.02); display:flex; justify-content:center; align-items:center; color:#444">Container Div</div>'; break;
                    case 'Grid': content = '<div style="display:grid; grid-template-columns:1fr 1fr; gap:10px"><div style="border:1px solid #444;height:30px"></div><div style="border:1px solid #444;height:30px"></div></div>'; break;
                    case 'Image': content = '<div style="width:100%; height:80px; background:#111; border:1px solid #444; border-radius:4px; display:flex; flex-direction:column; justify-content:center; align-items:center"><svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="#666" stroke-width="2"><rect x="3" y="3" width="18" height="18" rx="2" ry="2"></rect><circle cx="8.5" cy="8.5" r="1.5"></circle><polyline points="21 15 16 10 5 21"></polyline></svg><span style="color:#666; font-size:10px; margin-top:5px">Image Placeholder</span></div>'; break;
                    case 'Table': content = '<div style="border:1px solid #444; padding:10px; border-radius:4px; color:#aaa; font-size:11px; text-align:center">Simple Table Structure</div>'; break;
                    case 'TabView': content = '<div style="display:flex; border-bottom:1px solid #444"><div style="padding:5px 10px; border-bottom:2px solid #0ff; color:#0ff">Tab 1</div><div style="padding:5px 10px; color:#666">Tab 2</div></div>'; break;
                }

                wrapper.innerHTML = content + '<div class="delete-tool" onclick="this.parentElement.remove(); updateGeneratedCode();">×</div>';
                canvas.appendChild(wrapper);
                updateGeneratedCode();
            }

            function updateGeneratedCode() {
                const items = document.querySelectorAll('.canvas-item');
                let code = 'package com.jettra.example.pages;\\n\\n';
                code += 'import io.jettra.wui.complex.*;\\n';
                code += 'import io.jettra.wui.components.*;\\n';
                code += 'import java.util.Map;\\n\\n';
                code += '/**\\n * Generated by JettraWebDesigner\\n */\\n';
                code += 'public class MyNewPage extends DashboardBasePage {\\n\\n';
                code += '    public MyNewPage() {\\n';
                code += '        super("Dynamic Page Design");\\n';
                code += '    }\\n\\n';
                code += '    @Override\\n';
                code += '    protected void initCenter(Center center, String username) {\\n';
                code += '        center.add(new Header(1, "Page Content"));\\n\\n';
                
                items.forEach(item => {
                    const type = item.getAttribute('data-type');
                    switch(type) {
                        // Typography
                        case 'Header': code += '        center.add(new Header(2, "New Header"));\\n'; break;
                        case 'Paragraph': code += '        center.add(new Paragraph("Sample paragraph text content..."));\\n'; break;
                        case 'Span': code += '        center.add(new Span("Sample Span"));\\n'; break;
                        case 'Label': code += '        center.add(new Label("Field Label"));\\n'; break;
                        case 'Separator': code += '        center.add(new Separator());\\n'; break;
                        
                        // Forms
                        case 'Button': code += '        center.add(new Button("Action Button"));\\n'; break;
                        case 'CheckBox': code += '        center.add(new CheckBox("cb_id", "Option Label", "val"));\\n'; break;
                        case 'Form': code += '        center.add(new Form("form_id", "/target"));\\n'; break;
                        case 'RadioButton': code += '        center.add(new RadioButton("radio_id", "Selection", "val"));\\n'; break;
                        case 'SelectOne': code += '        center.add(new SelectOne("select_id", "Select..."));\\n'; break;
                        case 'SelectOneIcon': code += '        center.add(new SelectOneIcon("icon_id", "Icon Selection"));\\n'; break;
                        case 'Spinner': code += '        center.add(new Spinner("Processing..."));\\n'; break;
                        case 'TextBox': code += '        center.add(new TextBox("text_id", "Enter value"));\\n'; break;
                        case 'ToggleSwitch': code += '        center.add(new ToggleSwitch("toggle_id", "Feature Toggle", "true"));\\n'; break;
                        
                        // Navigation
                        case 'Link': code += '        center.add(new Link("#", "Source Link"));\\n'; break;
                        case 'Menu': code += '        center.add(new Menu());\\n'; break;
                        case 'MenuBar': code += '        center.add(new MenuBar());\\n'; break;
                        
                        // Feedback
                        case 'Alert': code += '        center.add(new Alert("Alert message here", Alert.Type.INFO));\\n'; break;
                        case 'Modal': code += '        center.add(new Modal("modal_id", "Modal Title"));\\n'; break;
                        case 'Notification': code += '        center.add(new Notification("New alert message"));\\n'; break;
                        case 'SessionTimeout': code += '        center.add(new SessionTimeoutDialog(30, 60));\\n'; break;
                        
                        // Layout & Display
                        case 'Avatar': code += '        center.add(Avatar.label("JD", "blue"));\\n'; break;
                        case 'AvatarGroup': code += '        center.add(new AvatarGroup());\\n'; break;
                        case 'Carousel': code += '        center.add(new Carousel());\\n'; break;
                        case 'Datatable': code += '        center.add(new Datatable());\\n'; break;
                        case 'Div': code += '        center.add(new Div());\\n'; break;
                        case 'Grid': code += '        center.add(new Grid(2));\\n'; break;
                        case 'Image': code += '        center.add(new Image("image.png", "Description"));\\n'; break;
                        case 'Table': code += '        center.add(new Table());\\n'; break;
                        case 'TabView': code += '        center.add(new TabView());\\n'; break;
                    }
                });

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
