package com.jettra.example.pages;

import io.jettra.wui.complex.Avatar;
import io.jettra.wui.complex.AvatarGroup;
import io.jettra.wui.complex.Center;
import io.jettra.wui.components.Div;
import io.jettra.wui.components.Header;
import io.jettra.wui.components.Paragraph;

/**
 * Showcase page for the new Avatar component.
 */
public class AvatarPage extends DashboardBasePage {

    public AvatarPage() {
        super("Avatar Showcase");
    }

    @Override
    protected void initCenter(Center center, String username) {
        Div container = new Div();
        container.setStyle("padding", "30px");
        
        Header h1 = new Header(1, "Avatar Component Showcase");
        container.add(h1);
        
        Paragraph p = new Paragraph("The Avatar component is used to represent a user or an entity through images, icons, or labels.");
        container.add(p);

        // --- Basic Types ---
        container.add(new Header(3, "Basic Types (Image, Label, Icon)"));
        Div row1 = new Div();
        row1.setStyle("display", "flex").setStyle("gap", "20px").setStyle("margin-bottom", "30px");
        
        row1.add(Avatar.image("https://i.pravatar.cc/150?u=11", "User 1").setSize(Avatar.Size.LG).setShape(Avatar.Shape.CIRCLE));
        row1.add(Avatar.label("JD", "#4f46e5").setSize(Avatar.Size.LG).setShape(Avatar.Shape.CIRCLE));
        row1.add(Avatar.icon("<svg width='32' height='32' viewBox='0 0 24 24' fill='none' stroke='currentColor' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'><path d='M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2'></path><circle cx='12' cy='7' r='4'></circle></svg>").setSize(Avatar.Size.LG).setShape(Avatar.Shape.CIRCLE));
        container.add(row1);

        // --- Shapes ---
        container.add(new Header(3, "Shapes (Circle & Rounded)"));
        Div row2 = new Div();
        row2.setStyle("display", "flex").setStyle("gap", "20px").setStyle("margin-bottom", "30px");
        
        row2.add(Avatar.image("https://i.pravatar.cc/150?u=2", "User 2").setShape(Avatar.Shape.CIRCLE).setSize(Avatar.Size.LG));
        row2.add(Avatar.image("https://i.pravatar.cc/150?u=3", "User 3").setShape(Avatar.Shape.ROUNDED).setSize(Avatar.Size.LG));
        container.add(row2);

        // --- Sizes ---
        container.add(new Header(3, "Sizes (XS to XL)"));
        Div row3 = new Div();
        row3.setStyle("display", "flex").setStyle("gap", "15px").setStyle("align-items", "center").setStyle("margin-bottom", "30px");
        
        row3.add(Avatar.image("https://i.pravatar.cc/150?u=4", "XS").setSize(Avatar.Size.XS).setShape(Avatar.Shape.CIRCLE));
        row3.add(Avatar.image("https://i.pravatar.cc/150?u=5", "SM").setSize(Avatar.Size.SM).setShape(Avatar.Shape.CIRCLE));
        row3.add(Avatar.image("https://i.pravatar.cc/150?u=6", "MD").setSize(Avatar.Size.MD).setShape(Avatar.Shape.CIRCLE));
        row3.add(Avatar.image("https://i.pravatar.cc/150?u=7", "LG").setSize(Avatar.Size.LG).setShape(Avatar.Shape.CIRCLE));
        row3.add(Avatar.image("https://i.pravatar.cc/150?u=8", "XL").setSize(Avatar.Size.XL).setShape(Avatar.Shape.CIRCLE));
        container.add(row3);

        // --- Badges ---
        container.add(new Header(3, "With Status Badges"));
        Div row4 = new Div();
        row4.setStyle("display", "flex").setStyle("gap", "20px").setStyle("margin-bottom", "30px");
        
        row4.add(Avatar.image("https://i.pravatar.cc/150?u=9", "Online").setSize(Avatar.Size.LG).setShape(Avatar.Shape.CIRCLE).setBadge("#22c55e"));
        row4.add(Avatar.image("https://i.pravatar.cc/150?u=10", "Offline").setSize(Avatar.Size.LG).setShape(Avatar.Shape.CIRCLE).setBadge("#94a3b8"));
        row4.add(Avatar.image("https://i.pravatar.cc/150?u=11", "Busy").setSize(Avatar.Size.LG).setShape(Avatar.Shape.CIRCLE).setBadge("#ef4444"));
        container.add(row4);

        // --- Groups ---
        container.add(new Header(3, "Avatar Group (Overlapping)"));
        AvatarGroup group = new AvatarGroup();
        group.add(Avatar.image("https://i.pravatar.cc/150?u=21", "U1").setSize(Avatar.Size.MD).setShape(Avatar.Shape.CIRCLE));
        group.add(Avatar.image("https://i.pravatar.cc/150?u=22", "U2").setSize(Avatar.Size.MD).setShape(Avatar.Shape.CIRCLE));
        group.add(Avatar.image("https://i.pravatar.cc/150?u=23", "U3").setSize(Avatar.Size.MD).setShape(Avatar.Shape.CIRCLE));
        group.add(Avatar.image("https://i.pravatar.cc/150?u=24", "U4").setSize(Avatar.Size.MD).setShape(Avatar.Shape.CIRCLE));
        group.add(Avatar.label("+3", "#334155").setSize(Avatar.Size.MD).setShape(Avatar.Shape.CIRCLE));
        container.add(group);
        
        center.add(container);
    }
}
