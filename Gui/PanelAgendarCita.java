package Gui;

import Data.Veterinaria;
import Model.Mascota;
import Model.Cita;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Calendar;

public class PanelAgendarCita extends JPanel {

    private Veterinaria veterinaria;

    private JComboBox<Mascota> comboMascota;

    private JComboBox<Integer> comboDia;
    private JComboBox<Integer> comboMes;
    private JComboBox<Integer> comboAnio;

    private JComboBox<Integer> comboHora;
    private JComboBox<String> comboMinuto;
    private JComboBox<String> comboAmPm;

    private JComboBox<String> comboPrioridad;

    private JTextField txtMotivo;

    private JButton btnAgendar;

    public PanelAgendarCita(Veterinaria veterinaria) {
        this.veterinaria = veterinaria;
        setLayout(new BorderLayout(10, 10));

        JPanel formulario = new JPanel(new GridLayout(6, 2, 5, 5));

        comboMascota = new JComboBox<>();

        comboDia = new JComboBox<>();
        for (int d = 1; d <= 31; d++) {
            comboDia.addItem(d);
        }

        comboMes = new JComboBox<>();
        for (int m = 1; m <= 12; m++) {
            comboMes.addItem(m);
        }

        comboAnio = new JComboBox<>();
        int anioActual = Calendar.getInstance().get(Calendar.YEAR);
        for (int a = anioActual; a <= anioActual + 3; a++) {
            comboAnio.addItem(a);
        }

        JPanel panelFecha = new JPanel(new GridLayout(1, 3, 5, 0));
        panelFecha.add(comboDia);
        panelFecha.add(comboMes);
        panelFecha.add(comboAnio);

        comboHora = new JComboBox<>();
        for (int h = 1; h <= 12; h++) {
            comboHora.addItem(h);
        }

        comboMinuto = new JComboBox<>();
        for (int min = 0; min <= 59; min++) {
            comboMinuto.addItem(String.format("%02d", min));
        }

        comboAmPm = new JComboBox<>(new String[]{"AM", "PM"});

        JPanel panelHora = new JPanel(new GridLayout(1, 3, 5, 0));
        panelHora.add(comboHora);
        panelHora.add(comboMinuto);
        panelHora.add(comboAmPm);

        comboPrioridad = new JComboBox<>(new String[]{"Normal", "Prioritaria"});

        txtMotivo = new JTextField();

        formulario.add(new JLabel("Mascota:"));
        formulario.add(comboMascota);
        formulario.add(new JLabel("Fecha (Dia/Mes/Año):"));
        formulario.add(panelFecha);
        formulario.add(new JLabel("Hora (Hora/Min/AM-PM):"));
        formulario.add(panelHora);
        formulario.add(new JLabel("Prioridad:"));
        formulario.add(comboPrioridad);
        formulario.add(new JLabel("Motivo:"));
        formulario.add(txtMotivo);

        btnAgendar = new JButton("Agendar Cita");
        formulario.add(new JLabel(""));
        formulario.add(btnAgendar);

        add(formulario, BorderLayout.NORTH);

        btnAgendar.addActionListener(e -> agendar());

        refrescar();
    }

    private void agendar() {
        try {
            Mascota mascota = (Mascota) comboMascota.getSelectedItem();

            if (mascota == null) {
                JOptionPane.showMessageDialog(this,
                        "Primero debes registrar una mascota.",
                        "Sin mascotas", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int dia = (int) comboDia.getSelectedItem();
            int mes = (int) comboMes.getSelectedItem();
            int anio = (int) comboAnio.getSelectedItem();

            try {
                java.time.LocalDate.of(anio, mes, dia);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "La fecha seleccionada no existe (revisa el dia para ese mes).",
                        "Fecha invalida", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String fecha = String.format("%02d/%02d/%04d", dia, mes, anio);

            int hora = (int) comboHora.getSelectedItem();
            String minuto = (String) comboMinuto.getSelectedItem();
            String ampm = (String) comboAmPm.getSelectedItem();
            String horaTexto = String.format("%02d", hora) + ":" + minuto + " " + ampm;

            java.time.LocalDateTime cuando = construirFechaHora(anio, mes, dia, hora, minuto, ampm);
            if (cuando.isBefore(java.time.LocalDateTime.now().withSecond(0).withNano(0))) {
                JOptionPane.showMessageDialog(this,
                        "No puedes agendar una cita en una fecha u hora que ya paso.",
                        "Fecha pasada", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String prioridad = (String) comboPrioridad.getSelectedItem();
            String motivo = txtMotivo.getText().trim();

            if (motivo.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "El motivo es obligatorio.",
                        "Datos incompletos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Cita conflicto = veterinaria.buscarCitaEnHorario(fecha, horaTexto, 0);
            if (conflicto != null) {
                JOptionPane.showMessageDialog(this,
                        "Ya hay una cita agendada para esa fecha y hora (Mascota: "
                        + conflicto.getMascota().getNombre() + ").\nElige otro horario.",
                        "Horario ocupado", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Cita c = new Cita(0, mascota, fecha, horaTexto, motivo);
            c.setPrioridad(prioridad);
            veterinaria.agendarCita(c);

            JOptionPane.showMessageDialog(this, "Cita agendada correctamente.");
            limpiarCampos();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Ocurrio un error inesperado: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private java.time.LocalDateTime construirFechaHora(int anio, int mes, int dia,
            int hora12, String minuto, String ampm) {
        int hora24;
        if (ampm.equals("AM")) {
            hora24 = (hora12 == 12) ? 0 : hora12;
        } else {
            hora24 = (hora12 == 12) ? 12 : hora12 + 12;
        }
        int min = Integer.parseInt(minuto);
        return java.time.LocalDateTime.of(anio, mes, dia, hora24, min);
    }

    private void limpiarCampos() {
        txtMotivo.setText("");
        comboPrioridad.setSelectedIndex(0);
    }

    public void refrescar() {
        comboMascota.removeAllItems();
        for (Mascota m : veterinaria.getMascotas()) {
            comboMascota.addItem(m);
        }
    }
}
