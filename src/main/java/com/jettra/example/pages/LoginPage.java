package com.jettra.example.pages;

import io.jettra.wui.core.Page;
import io.jettra.wui.components.Login;
import io.jettra.wui.core.annotations.Init;

public class LoginPage extends Page {

    private Login loginForm;

    public LoginPage() {
        super("Login");
    }

    @Init
    public void initUI() {
        loginForm = new Login("/login");

        // Styling the login form generically with 3D futuristic aesthetics
        loginForm.setProperty("style", "margin: 10% auto; padding: 30px; width: 350px; " +
            "background: rgba(20, 30, 50, 0.85); box-shadow: 0 0 20px cyan; " +
            "border-radius: 15px; backdrop-filter: blur(10px); " +
            "color: white; font-family: 'Courier New', monospace; text-align: center;");
        
        loginForm.getUsername().setProperty("style", "width: 100%; padding: 10px; margin-bottom: 15px; " +
            "background: #0f172a; border: 1px solid cyan; color: white; border-radius: 5px;");
            
        loginForm.getPassword().setProperty("style", "width: 100%; padding: 10px; margin-bottom: 15px; " +
            "background: #0f172a; border: 1px solid cyan; color: white; border-radius: 5px;");
            
        loginForm.getLoginButton().setProperty("style", "width: 100%; padding: 12px; margin-bottom: 10px; " +
            "background: cyan; color: #0f172a; border: none; border-radius: 5px; font-weight: bold; cursor: pointer; " +
            "box-shadow: 0 0 10px cyan;");
            
        loginForm.getForgotPasswordButton().setProperty("style", "background: transparent; border: none; " +
            "color: cyan; cursor: pointer; text-decoration: underline; font-size: 0.9em;");

        add(loginForm);
    }
}
