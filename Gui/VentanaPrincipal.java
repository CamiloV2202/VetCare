package Gui;

import Data.Veterinaria;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;

public class VentanaPrincipal extends JFrame {

    private Veterinaria veterinaria;
    private JTabbedPane pestanas;

    private PanelRegistroCliente panelCliente;
    private PanelRegistroMascota panelMascota;
    private PanelAgendarCita panelCita;
    private PanelListarCitas panelListar;
    private PanelHistorialClinico panelHistorial;

    public VentanaPrincipal() {
        this.veterinaria = new Veterinaria();

        setTitle("VetCare - Clinica Veterinaria Huellitas");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        panelCliente = new PanelRegistroCliente(veterinaria);
        panelMascota = new PanelRegistroMascota(veterinaria);
        panelCita = new PanelAgendarCita(veterinaria);
        panelListar = new PanelListarCitas(veterinaria);
        panelHistorial = new PanelHistorialClinico(veterinaria);

        pestanas = new JTabbedPane();
        pestanas.addTab("Clientes", panelCliente);
        pestanas.addTab("Mascotas", panelMascota);
        pestanas.addTab("Agendar Cita", panelCita);
        pestanas.addTab("Citas", panelListar);
        pestanas.addTab("Historial Citas", panelHistorial);

        pestanas.addChangeListener(e -> refrescarPanelActual());

        add(pestanas, BorderLayout.CENTER);
    }

    private void refrescarPanelActual() {
        int i = pestanas.getSelectedIndex();
        switch (i) {
            case 0: panelCliente.refrescar(); break;
            case 1: panelMascota.refrescar(); break;
            case 2: panelCita.refrescar(); break;
            case 3: panelListar.refrescar(); break;
            case 4: panelHistorial.refrescar(); break;
        }
    }
}
