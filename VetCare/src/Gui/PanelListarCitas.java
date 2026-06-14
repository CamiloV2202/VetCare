package Gui;

import Data.Veterinaria;
import Model.Cita;
import Model.EstadoCita;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class PanelListarCitas extends JPanel {

    private Veterinaria veterinaria;

    private JTable tabla;
    private DefaultTableModel modelo;

    private JButton btnAtender;
    private JButton btnAtendida;
    private JButton btnModificar;
    private JButton btnCancelar;

    private ArrayList<Cita> citasEnTabla;

    private static final DateTimeFormatter FORMATO_HORA =
            DateTimeFormatter.ofPattern("hh:mm a", Locale.US);

    public PanelListarCitas(Veterinaria veterinaria) {
        this.veterinaria = veterinaria;
        setLayout(new BorderLayout(10, 10));

        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelo.addColumn("ID");
        modelo.addColumn("Mascota");
        modelo.addColumn("Fecha");
        modelo.addColumn("Hora");
        modelo.addColumn("Prioridad");
        modelo.addColumn("Motivo");
        modelo.addColumn("Estado");
        modelo.addColumn("Modificada");
        tabla = new JTable(modelo);

        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(t, value,
                        isSelected, hasFocus, row, column);
                if (!isSelected) {
                    String prioridad = modelo.getValueAt(row, 4).toString();
                    if (prioridad.equals("Prioritaria")) {
                        comp.setBackground(new Color(255, 224, 224));
                    } else {
                        comp.setBackground(Color.WHITE);
                    }
                }
                return comp;
            }
        });

        btnAtender = new JButton("Atender");
        btnAtendida = new JButton("Cita ya atendida");
        btnModificar = new JButton("Modificar");
        btnCancelar = new JButton("Cancelar");

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBotones.add(btnAtender);
        panelBotones.add(btnAtendida);
        panelBotones.add(btnModificar);
        panelBotones.add(btnCancelar);

        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        btnAtender.addActionListener(e -> atender());
        btnAtendida.addActionListener(e -> citaYaAtendida());
        btnModificar.addActionListener(e -> modificar());
        btnCancelar.addActionListener(e -> cancelar());

        refrescar();
    }

    private Cita citaSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona una cita en la tabla primero.",
                    "Sin seleccion", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return citasEnTabla.get(fila);
    }

    private void atender() {
        Cita cita = citaSeleccionada();
        if (cita == null) {
            return;
        }
        if (cita.getEstado() == EstadoCita.EN_PROCESO) {
            JOptionPane.showMessageDialog(this, "La cita ya esta en proceso.");
            return;
        }
        cita.setEstado(EstadoCita.EN_PROCESO);
        veterinaria.actualizarCitas();
        JOptionPane.showMessageDialog(this, "La cita ahora esta En proceso.");
        refrescar();
    }

    private void citaYaAtendida() {
        Cita cita = citaSeleccionada();
        if (cita == null) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Marcar esta cita como atendida?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String resultado = calcularResultado(cita.getHora());
        String horaReal = LocalTime.now().format(FORMATO_HORA);

        String obs = JOptionPane.showInputDialog(this,
                "Observaciones de la atencion (opcional):",
                cita.getObservaciones());
        if (obs == null) {
            obs = cita.getObservaciones();
        }

        cita.setEstado(EstadoCita.ATENDIDA);
        cita.setHoraAtencion(horaReal);
        cita.setResultadoAtencion(resultado);
        cita.setObservaciones(obs.replace(";", ","));
        veterinaria.actualizarCitas();

        JOptionPane.showMessageDialog(this,
                "Cita atendida a las " + horaReal + ".\n" + resultado);
        refrescar();
    }

    private void cancelar() {
        Cita cita = citaSeleccionada();
        if (cita == null) {
            return;
        }

        String razon = JOptionPane.showInputDialog(this,
                "Por que se cancela la cita?");
        if (razon == null) {
            return;
        }
        razon = razon.trim();
        if (razon.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debes indicar el motivo de la cancelacion.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        cita.setEstado(EstadoCita.CANCELADA);
        cita.setMotivoCancelacion(razon.replace(";", ","));
        veterinaria.actualizarCitas();

        JOptionPane.showMessageDialog(this,
                "Cita cancelada. Aparecera en el Historial.");
        refrescar();
    }

    private void modificar() {
        Cita cita = citaSeleccionada();
        if (cita == null) {
            return;
        }

        JComboBox<Integer> cDia = new JComboBox<>();
        for (int d = 1; d <= 31; d++) {
            cDia.addItem(d);
        }
        JComboBox<Integer> cMes = new JComboBox<>();
        for (int m = 1; m <= 12; m++) {
            cMes.addItem(m);
        }
        JComboBox<Integer> cAnio = new JComboBox<>();
        int anioActual = Calendar.getInstance().get(Calendar.YEAR);
        for (int a = anioActual - 1; a <= anioActual + 3; a++) {
            cAnio.addItem(a);
        }
        JComboBox<Integer> cHora = new JComboBox<>();
        for (int h = 1; h <= 12; h++) {
            cHora.addItem(h);
        }
        JComboBox<String> cMin = new JComboBox<>();
        for (int min = 0; min <= 59; min++) {
            cMin.addItem(String.format("%02d", min));
        }
        JComboBox<String> cAmPm = new JComboBox<>(new String[]{"AM", "PM"});
        JComboBox<String> cPrioridad = new JComboBox<>(new String[]{"Normal", "Prioritaria"});
        JTextField tMotivo = new JTextField();

        precargarFecha(cita.getFecha(), cDia, cMes, cAnio);
        precargarHora(cita.getHora(), cHora, cMin, cAmPm);
        cPrioridad.setSelectedItem(cita.getPrioridad());
        tMotivo.setText(cita.getMotivo());

        JPanel panelFecha = new JPanel(new GridLayout(1, 3, 5, 0));
        panelFecha.add(cDia);
        panelFecha.add(cMes);
        panelFecha.add(cAnio);

        JPanel panelHora = new JPanel(new GridLayout(1, 3, 5, 0));
        panelHora.add(cHora);
        panelHora.add(cMin);
        panelHora.add(cAmPm);

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.add(new JLabel("Fecha (Dia/Mes/Año):"));
        form.add(panelFecha);
        form.add(new JLabel("Hora (Hora/Min/AM-PM):"));
        form.add(panelHora);
        form.add(new JLabel("Prioridad:"));
        form.add(cPrioridad);
        form.add(new JLabel("Motivo:"));
        form.add(tMotivo);

        int op = JOptionPane.showConfirmDialog(this, form, "Modificar cita",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (op != JOptionPane.OK_OPTION) {
            return;
        }

        int dia = (int) cDia.getSelectedItem();
        int mes = (int) cMes.getSelectedItem();
        int anio = (int) cAnio.getSelectedItem();
        try {
            LocalDate.of(anio, mes, dia);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "La fecha seleccionada no existe (revisa el dia para ese mes).",
                    "Fecha invalida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String motivo = tMotivo.getText().trim();
        if (motivo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El motivo es obligatorio.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int h = (int) cHora.getSelectedItem();
        String horaTexto = String.format("%02d", h) + ":" + cMin.getSelectedItem()
                + " " + cAmPm.getSelectedItem();

        String nuevaFecha = String.format("%02d/%02d/%04d", dia, mes, anio);

        LocalDateTime cuando = construirFechaHora(anio, mes, dia, h,
                (String) cMin.getSelectedItem(), (String) cAmPm.getSelectedItem());
        if (cuando.isBefore(LocalDateTime.now().withSecond(0).withNano(0))) {
            JOptionPane.showMessageDialog(this,
                    "No puedes mover la cita a una fecha u hora que ya paso.",
                    "Fecha pasada", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cita conflicto = veterinaria.buscarCitaEnHorario(nuevaFecha, horaTexto, cita.getId());
        if (conflicto != null) {
            JOptionPane.showMessageDialog(this,
                    "Ya hay otra cita agendada para esa fecha y hora (Mascota: "
                    + conflicto.getMascota().getNombre() + ").\nElige otro horario.",
                    "Horario ocupado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        cita.setFecha(nuevaFecha);
        cita.setHora(horaTexto);
        cita.setPrioridad((String) cPrioridad.getSelectedItem());
        cita.setMotivo(motivo.replace(";", ","));
        cita.setModificada(true);
        veterinaria.actualizarCitas();

        JOptionPane.showMessageDialog(this,
                "Cita modificada. Queda marcada como modificada.");
        refrescar();
    }

    private LocalDateTime construirFechaHora(int anio, int mes, int dia,
            int hora12, String minuto, String ampm) {
        int hora24;
        if (ampm.equals("AM")) {
            hora24 = (hora12 == 12) ? 0 : hora12;
        } else {
            hora24 = (hora12 == 12) ? 12 : hora12 + 12;
        }
        int min = Integer.parseInt(minuto);
        return LocalDateTime.of(anio, mes, dia, hora24, min);
    }

    private String calcularResultado(String horaPrevista) {
        try {
            LocalTime prevista = LocalTime.parse(horaPrevista, FORMATO_HORA);
            LocalTime ahora = LocalTime.now();
            long min = Duration.between(prevista, ahora).toMinutes();
            if (min == 0) {
                return "Atendida a la hora prevista";
            } else if (min > 0) {
                return "Atendida " + min + " min despues de lo previsto";
            } else {
                return "Atendida " + (-min) + " min antes de lo previsto";
            }
        } catch (Exception ex) {
            return "Atendida (hora no comparable)";
        }
    }

    private void precargarFecha(String fecha, JComboBox<Integer> cDia,
            JComboBox<Integer> cMes, JComboBox<Integer> cAnio) {
        try {
            String[] p = fecha.split("/");
            cDia.setSelectedItem(Integer.parseInt(p[0]));
            cMes.setSelectedItem(Integer.parseInt(p[1]));
            cAnio.setSelectedItem(Integer.parseInt(p[2]));
        } catch (Exception ex) {
        }
    }

    private void precargarHora(String hora, JComboBox<Integer> cHora,
            JComboBox<String> cMin, JComboBox<String> cAmPm) {
        try {
            String[] partes = hora.split(" ");
            String[] hm = partes[0].split(":");
            int h = Integer.parseInt(hm[0]);
            String min = hm[1];
            if (partes.length > 1) {
                cAmPm.setSelectedItem(partes[1]);
                cHora.setSelectedItem(h);
            } else {
                String ampm = (h >= 12) ? "PM" : "AM";
                int h12 = h % 12;
                if (h12 == 0) {
                    h12 = 12;
                }
                cHora.setSelectedItem(h12);
                cAmPm.setSelectedItem(ampm);
            }
            cMin.setSelectedItem(min);
        } catch (Exception ex) {
        }
    }

    public void refrescar() {
        modelo.setRowCount(0);
        citasEnTabla = veterinaria.listarCitasActivas();
        for (Cita c : citasEnTabla) {
            Object[] fila = {
                c.getId(),
                c.getMascota().getNombre(),
                c.getFecha(),
                c.getHora(),
                c.getPrioridad(),
                c.getMotivo(),
                c.getEstado().getEtiqueta(),
                (c.isModificada() ? "Sí" : "No")
            };
            modelo.addRow(fila);
        }
    }
}
