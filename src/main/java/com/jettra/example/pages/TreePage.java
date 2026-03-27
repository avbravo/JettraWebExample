package com.jettra.example.pages;

import io.jettra.wui.complex.Center;
import io.jettra.wui.complex.Tree;
import io.jettra.wui.complex.Modal;
import io.jettra.wui.components.Div;
import io.jettra.wui.components.Header;
import io.jettra.wui.components.Paragraph;
import io.jettra.wui.components.Button;

public class TreePage extends DashboardBasePage {

    public TreePage() {
        super("Tree Component Showcase");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Div headerRow = new Div();
        headerRow.setStyle("display", "flex").setStyle("justify-content", "space-between").setStyle("align-items", "center").setStyle("margin-bottom", "15px");
        
        Header h1 = new Header(1, "Tree Component Showcase");
        h1.setStyle("margin", "0");
        headerRow.add(h1);
        
        Button codeBtn = new Button("Code");
        codeBtn.addClass("j-btn");
        codeBtn.setStyle("border-color", "var(--jettra-accent)").setStyle("color", "var(--jettra-accent)");
        codeBtn.setProperty("onclick", "document.getElementById('tree-code-modal').style.display = 'block'");
        headerRow.add(codeBtn);
        
        container.add(headerRow);

        // --- Code Modal ---
        Modal codeModal = new Modal("tree-code-modal");
        codeModal.setStyle("display", "none").setStyle("background", "var(--jettra-glass)")
                 .setStyle("backdrop-filter", "blur(10px)")
                 .setStyle("padding", "20px").setStyle("border-radius", "8px")
                 .setStyle("width", "90%").setStyle("max-width", "800px")
                 .setStyle("border", "1px solid var(--jettra-border)");
        
        codeModal.add(new Header(3, "Tree Implementation Example").setStyle("margin-top", "0").setStyle("color", "var(--jettra-accent)"));
        
        Div codeContainer = new Div();
        codeContainer.setStyle("background", "rgba(0,0,0,0.4)").setStyle("padding", "15px")
                     .setStyle("border-radius", "4px").setStyle("overflow-x", "auto")
                     .setStyle("margin-bottom", "20px").setStyle("border", "1px solid rgba(255,255,255,0.1)");
        
        String javaCode = "Tree myTree = new Tree();\n" +
                          "Tree.TreeItem root = new Tree.TreeItem(\"Root\");\n\n" +
                          "Tree.TreeItem folder = new Tree.TreeItem(\"Folder\");\n" +
                          "folder.addItem(new Tree.TreeItem(\"File 1\"));\n" +
                          "folder.addItem(new Tree.TreeItem(\"File 2\"));\n\n" +
                          "root.addItem(folder);\n" +
                          "myTree.addItem(root);\n" +
                          "center.add(myTree);";
                           
        io.jettra.wui.core.UIComponent pre = new io.jettra.wui.core.UIComponent("pre") {};
        pre.setStyle("margin", "0");
        io.jettra.wui.core.UIComponent codeTag = new io.jettra.wui.core.UIComponent("code") {};
        codeTag.setProperty("id", "tree-java-code");
        codeTag.setStyle("color", "#a5d6ff").setStyle("font-family", "monospace").setStyle("font-size", "0.9rem");
        codeTag.setContent(javaCode.replace("<", "&lt;").replace(">", "&gt;"));
        
        pre.add(codeTag);
        codeContainer.add(pre);
        codeModal.add(codeContainer);
        
        Div modalActions = new Div();
        modalActions.setStyle("display", "flex").setStyle("justify-content", "flex-end").setStyle("gap", "10px");
        
        Button copyBtn = new Button("Copy");
        copyBtn.addClass("j-btn");
        copyBtn.setProperty("onclick", "navigator.clipboard.writeText(document.getElementById('tree-java-code').innerText).then(() => { this.innerText='Copied!'; setTimeout(() => this.innerText='Copy', 2000); })");
        
        Button closeBtn = new Button("Close");
        closeBtn.addClass("j-btn");
        closeBtn.setStyle("background", "transparent").setStyle("border-color", "var(--jettra-border)");
        closeBtn.setProperty("onclick", "document.getElementById('tree-code-modal').style.display = 'none'");
        
        modalActions.add(closeBtn).add(copyBtn);
        codeModal.add(modalActions);
        
        container.add(codeModal);
        // --- End Code Modal ---

        container.add(new Paragraph("The Tree component displays hierarchical data in an interactive, expandable tree structure."));
        
        container.add(new Header(3, "Static Example (File System)"));
        Tree tree1 = new Tree();
        tree1.setStyle("background", "rgba(0,0,0,0.2)").setStyle("padding", "20px").setStyle("border-radius", "8px").setStyle("border", "1px solid var(--jettra-border)").setStyle("margin-bottom", "30px");
        
        Tree.TreeItem root = new Tree.TreeItem("System Root");
        Tree.TreeItem f1 = new Tree.TreeItem("Documents");
        f1.addItem(new Tree.TreeItem("reports.pdf")).addItem(new Tree.TreeItem("notes.txt"));
        Tree.TreeItem f2 = new Tree.TreeItem("Source Code");
        f2.addItem(new Tree.TreeItem("Main.java")).addItem(new Tree.TreeItem("App.java"));
        root.addItem(f1).addItem(f2);
        tree1.add(root);
        container.add(tree1);

        container.add(new Header(3, "Dynamic Random Example"));
        Tree tree2 = new Tree();
        tree2.setStyle("background", "rgba(0,255,255,0.03)").setStyle("padding", "20px").setStyle("border-radius", "8px").setStyle("border", "1px solid rgba(0,255,255,0.2)");
        
        Tree.TreeItem randomRoot = new Tree.TreeItem("Generated Data Root");
        generateRandomTree(randomRoot, 1, 3);
        tree2.add(randomRoot);
        container.add(tree2);
        
        center.add(container);
    }

    private void generateRandomTree(Tree.TreeItem parent, int depth, int maxDepth) {
        if (depth > maxDepth) return;
        String[] prefixes = {"Alpha", "Beta", "Gamma", "Delta", "Epsilon"};
        int count = (int) (Math.random() * 3) + 2;
        for (int i = 0; i < count; i++) {
            String label = prefixes[(int) (Math.random() * prefixes.length)] + " " + depth + "-" + (i + 1);
            Tree.TreeItem item = new Tree.TreeItem(label);
            parent.addItem(item);
            if (Math.random() > 0.3) {
                generateRandomTree(item, depth + 1, maxDepth);
            }
        }
    }
}
