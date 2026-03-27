package com.jettra.example.pages;

import io.jettra.wui.complex.Center;
import io.jettra.wui.components.Div;
import io.jettra.wui.components.Header;
import io.jettra.wui.components.Paragraph;
import io.jettra.wui.components.FolderSelector;

public class FolderSelectorPage extends DashboardBasePage {

    public FolderSelectorPage() {
        super("FolderSelector Component");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Header h1 = new Header(1, "FolderSelector Component Showcase");
        container.add(h1);
        
        container.add(new Paragraph("The FolderSelector allows managing references to local folders and their contents."));
        
        Header h3 = new Header(3, "Browse and Manage Folder Reference");
        FolderSelector selector = new FolderSelector("my-fs")
            .setReferenceLocation("/var/www/html/assets")
            .setReferenceContent("Images, CSS, JS");
        container.add(h3);
        container.add(selector);
        
        center.add(container);
    }
}
