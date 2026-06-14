package Gui;

import Data.Veterinaria;
import Model.Cliente;
import Model.Mascota;
import Model.Sexo;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;

public class PanelRegistroMascota extends JPanel {

    private Veterinaria veterinaria;

    private JTextField txtNombre;
    private JTextField txtEspecie;
    private JTextField txtRaza;
    private JTextField txtEdad;
    private JComboBox<Sexo> comboSexo;
    private JComboBox<Cliente> comboDueno;

    private JButton btnRegistrar;
    private JButton btnModificar;
    private JButton btnEliminar;

    private JTable tabla;
    private DefaultTableModel modelo;

    private int idSeleccionado = -1;

    public PanelRegistroMascota(Veterinaria veterinaria) {
        this.veterinaria = veterinaria;
        setLayout(new BorderLayout(10, 10));

        JPanel formulario = new JPanel(new GridLayout(6, 2, 5, 5));

        txtNombre = new JTextField();
        txtEspecie = new JTextField();
        txtRaza = new JTextField();
        txtEdad = new JTextField();

        comboSexo = new JComboBox<>(Sexo.values());

        comboDueno = new JComboBox<>();

        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);
        formulario.add(new JLabel("Especie:"));
        formulario.add(txtEspecie);
        formulario.add(new JLabel("Raza:"));
        formulario.add(txtRaza);
        formulario.add(new JLabel("Edad:"));
        formulario.add(txtEdad);
        formulario.add(new JLabel("Sexo:"));
        formulario.add(comboSexo);
        formulario.add(new JLabel("Dueño:"));
        formulario.add(comboDueno);

        btnRegistrar = new JButton("Registrar Mascota");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);

        JPanel norte = new JPanel(new BorderLayout());
        norte.add(formulario, BorderLayout.CENTER);
        norte.add(panelBotones, BorderLayout.SOUTH);

        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Especie");
        modelo.addColumn("Raza");
        modelo.addColumn("Edad");
        modelo.addColumn("Dueño");
        tabla = new JTable(modelo);

        add(norte, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnRegistrar.addActionListener(e -> registrar());
        btnModificar.addActionListener(e -> modificar());
        btnEliminar.addActionListener(e -> eliminar());

        tabla.getSelectionModel().addListSelectionListener(e -> cargarSeleccion());

        refrescar();
    }

    private void registrar() {
        try {
            String nombre = txtNombre.getText().trim();
            String especie = txtEspecie.getText().trim();
            String raza = txtRaza.getText().trim();

            if (!textosValidos(nombre, especie, raza)) {
                return;
            }

            int edad = Integer.parseInt(txtEdad.getText().trim());
            if (!edadValida(edad)) {
                return;
            }

            Sexo sexo = (Sexo) comboSexo.getSelectedItem();
            Cliente dueno = (Cliente) comboDueno.getSelectedItem();

            if (dueno == null) {
                JOptionPane.showMessageDialog(this,
                        "Primero debes registrar un cliente para asignarlo como dueño.",
                        "Sin dueño", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Mascota m = new Mascota(0, nombre, especie, raza, edad, sexo, dueno);
            veterinaria.registrarMascota(m);

            JOptionPane.showMessageDialog(this, "Mascota registrada correctamente.");
            limpiarCampos();
            refrescar();

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(this,
                    "La edad debe ser un numero entero (ejemplo: 3).",
                    "Edad invalida", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Ocurrio un error inesperado: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona una mascota de la tabla primero.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String nombre = txtNombre.getText().trim();
            String especie = txtEspecie.getText().trim();
            String raza = txtRaza.getText().trim();

            if (!textosValidos(nombre, especie, raza)) {
                return;
            }

            int edad = Integer.parseInt(txtEdad.getText().trim());
            if (!edadValida(edad)) {
                return;
            }

            Sexo sexo = (Sexo) comboSexo.getSelectedItem();
            Cliente dueno = (Cliente) comboDueno.getSelectedItem();

            if (dueno == null) {
                JOptionPane.showMessageDialog(this,
                        "Debes asignar un dueño.",
                        "Sin dueño", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Mascota m = veterinaria.buscarMascotaPorId(idSeleccionado);
            if (m != null) {
                m.setNombre(nombre);
                m.setEspecie(especie);
                m.setRaza(raza);
                m.setEdad(edad);
                m.setSexo(sexo);
                m.setDueno(dueno);
                veterinaria.actualizarMascotas();

                JOptionPane.showMessageDialog(this, "Mascota modificada correctamente.");
                limpiarCampos();
                idSeleccionado = -1;
                refrescar();
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(this,
                    "La edad debe ser un numero entero (ejemplo: 3).",
                    "Edad invalida", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Ocurrio un error inesperado: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona una mascota de la tabla primero.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Integer.parseInt(modelo.getValueAt(fila, 0).toString());

        int numCitas = veterinaria.listarCitasDeMascota(id).size();
        if (numCitas > 0) {
            JOptionPane.showMessageDialog(this,
                    "No se puede eliminar la mascota porque tiene " + numCitas
                    + " cita(s) agendada(s).\nElimina sus citas primero.",
                    "Accion no permitida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Seguro que deseas eliminar esta mascota?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            veterinaria.eliminarMascota(id);
            JOptionPane.showMessageDialog(this, "Mascota eliminada.");
            limpiarCampos();
            idSeleccionado = -1;
            refrescar();
        }
    }

    private boolean textosValidos(String nombre, String especie, String raza) {
        if (nombre.isEmpty() || especie.isEmpty() || raza.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El nombre, especie y raza son obligatorios.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!nombre.matches("[\\p{L} ]+")) {
            JOptionPane.showMessageDialog(this,
                    "El nombre solo puede contener letras.",
                    "Dato invalido", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!especie.matches("[\\p{L} ]+")) {
            JOptionPane.showMessageDialog(this,
                    "La especie solo puede contener letras.",
                    "Dato invalido", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!raza.matches("[\\p{L} ]+")) {
            JOptionPane.showMessageDialog(this,
                    "La raza solo puede contener letras.",
                    "Dato invalido", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean edadValida(int edad) {
        if (edad < 0) {
            JOptionPane.showMessageDialog(this,
                    "La edad no puede ser negativa.",
                    "Edad invalida", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (edad > 100) {
            JOptionPane.showMessageDialog(this,
                    "La edad no puede ser mayor a 100 años.",
                    "Edad invalida", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtEspecie.setText("");
        txtRaza.setText("");
        txtEdad.setText("");
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            return;
        }
        idSeleccionado = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
        Mascota m = veterinaria.buscarMascotaPorId(idSeleccionado);
        if (m != null) {
            txtNombre.setText(m.getNombre());
            txtEspecie.setText(m.getEspecie());
            txtRaza.setText(m.getRaza());
            txtEdad.setText(String.valueOf(m.getEdad()));
            comboSexo.setSelectedItem(m.getSexo());
            comboDueno.setSelectedItem(m.getDueno());
        }
    }

    public void refrescar() {

        comboDueno.removeAllItems();
        for (Cliente c : veterinaria.getClientes()) {
            comboDueno.addItem(c);
        }

        modelo.setRowCount(0);
        for (Mascota m : veterinaria.getMascotas()) {
            Object[] fila = {
                m.getId(), m.getNombre(), m.getEspecie(), m.getRaza(),
                m.getEdad(), m.getDueno().getNombre()
            };
            modelo.addRow(fila);
        }
    }
}
