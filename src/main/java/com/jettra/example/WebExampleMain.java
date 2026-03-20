package com.jettra.example;

import com.jettra.server.JettraServer;
import com.jettra.server.config.JettraConfigProperty;
import com.jettra.server.config.ConfigInjector;

public class WebExampleMain {

    @JettraConfigProperty(name = "app.title")
    private String appTitle;

    public void initUI() {
        ConfigInjector.inject(this);
        System.out.println("Iniciando aplicación Web: " + appTitle);
        
        System.out.println("=========================================");
        System.out.println("--- JettraWUI: Construyendo Interfaz ---");
        System.out.println("--- Componente: Page3DFuturisticView ---");
        System.out.println("--- Componente: GlassmorphismForm    ---");
        System.out.println("--- Componente: NeonButton(Ingresar) ---");
        System.out.println("--- Componente: DataGridModel        ---");
        System.out.println("=========================================");
    }

    public static void main(String[] args) {
        WebExampleMain app = new WebExampleMain();
        
        // 1. Inicializar la UI abstracta a través del framework JettraWUI
        app.initUI();

        System.out.println("Levantando servidor de enrutamiento JettraServer empotrado...");
        
        // 2. Levantar el HTTP Core (JettraServer rehusará el server.port de jettra-config.properties si está cargado)
        JettraServer server = new JettraServer();
        server.start();
    }
}
