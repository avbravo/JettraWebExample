package com.jettra.example.pages;

import io.jettra.wui.complex.Center;
import io.jettra.wui.components.Div;
import io.jettra.wui.components.Header;
import io.jettra.wui.components.Paragraph;
import io.jettra.wui.components.FileUpload;

public class FileUploadPage extends DashboardBasePage {

    public FileUploadPage() {
        super("FileUpload Component");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Header h1 = new Header(1, "FileUpload Component Showcase");
        container.add(h1);
        
        container.add(new Paragraph("The FileUpload component allows users to select files from their device."));
        
        container.add(new Header(3, "Auto-upload upon Selection"));
        FileUpload autoUpload = new FileUpload("auto-upload")
            .setAutoUpload(true)
            .setDestination("/home/user/auto-uploads");
        container.add(autoUpload);
        
        container.add(new Header(3, "Manual Upload (with Confirmation Button)"));
        FileUpload manualUpload = new FileUpload("manual-upload")
            .setAutoUpload(false)
            .setDestination("/home/user/manual-uploads");
        container.add(manualUpload);
        
        container.add(new Header(3, "Multiple File Selection"));
        FileUpload multiUpload = new FileUpload("multi-upload").setMultiple(true);
        container.add(multiUpload);

        container.add(new Header(3, "Restricted File types (Images Only)"));
        FileUpload imageUpload = new FileUpload("image-upload").setAccept("image/*");
        container.add(imageUpload);
        
        center.add(container);
    }
}
