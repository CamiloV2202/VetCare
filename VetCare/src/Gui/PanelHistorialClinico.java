package Gui;

import Data.Veterinaria;
import Model.Cita;
import Model.EstadoCita;
import Model.Mascota;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;

public class PanelHistorialClinico extends JPanel {

    private Veterinaria veterinaria;

    private JComboBox<String> comboFiltro;
    private JButton btnVer;
    private JTable tabla;
    private DefaultTableModel modelo;

    public PanelHistorialClinico(Veterinaria veterinaria) {
        this.veterinaria = veterinaria;
        setLayout(new BorderLayout(10, 10));

        JPanel arriba = new JPanel(new FlowLayout(FlowLayout.LEFT));
        comboFiltro = new JComboBox<>();
        btnVer = new JButton("Ver");
        arriba.add(new JLabel("Mascota:"));
        arriba.add(comboFiltro);
        arriba.add(btnVer);

        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelo.addColumn("ID");
        modelo.addColumn("Mascota");
        modelo.addColumn("Fecha");
        modelo.addColumn("Hora prevista");
        modelo.addColumn("Estado");
        modelo.addColumn("Atencion");
        modelo.addColumn("Motivo");
        modelo.addColumn("Cancelacion");
        modelo.addColumn("Modificada");
        tabla = new JTable(modelo);

        add(arriba, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnVer.addActionListener(e -> mostrarHistorial());

        refrescar();
    }

    private void mostrarHistorial() {
        modelo.setRowCount(0);

        String filtro = (String) comboFiltro.getSelectedItem();

        for (Cita c : veterinaria.listarCitasHistorico()) {

            String nombreMascota = c.getMascota().getNombre();

            if (filtro != null && !filtro.equals("Todas las mascotas")
                    && !filtro.equals(nombreMascota)) {
                continue;
            }

            String atencion;
            if (c.getEstado() == EstadoCita.ATENDIDA) {
                atencion = c.getHoraAtencion() + " - " + c.getResultadoAtencion();
            } else {
                atencion = "-";
            }

            String cancelacion;
            if (c.getEstado() == EstadoCita.CANCELADA) {
                cancelacion = c.getMotivoCancelacion();
            } else {
                cancelacion = "-";
            }

            Object[] fila = {
                c.getId(),
                nombreMascota,
                c.getFecha(),
                c.getHora(),
                c.getEstado().getEtiqueta(),
                atencion,
                c.getMotivo(),
                cancelacion,
                (c.isModificada() ? "Sí" : "No")
            };
            modelo.addRow(fila);
        }

        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "No hay citas en el historial para ese filtro.",
                    "Sin historial", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void refrescar() {
        comboFiltro.removeAllItems();
        comboFiltro.addItem("Todas las mascotas");
        for (Mascota m : veterinaria.getMascotas()) {
            comboFiltro.addItem(m.getNombre());
        }
        mostrarHistorial();
    }
}
