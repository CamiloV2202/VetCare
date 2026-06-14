package Gui;

import Data.Veterinaria;
import Model.Cliente;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;

public class PanelRegistroCliente extends JPanel {

    private Veterinaria veterinaria;

    private JTextField txtDocumento;
    private JTextField txtNombre;
    private JTextField txtTelefono;
    private JTextField txtEmail;
    private JTextField txtDireccion;

    private JButton btnRegistrar;
    private JButton btnModificar;
    private JButton btnEliminar;

    private JTable tabla;
    private DefaultTableModel modelo;

    private String docSeleccionado = null;

    public PanelRegistroCliente(Veterinaria veterinaria) {
        this.veterinaria = veterinaria;
        setLayout(new BorderLayout(10, 10));

        JPanel formulario = new JPanel(new GridLayout(5, 2, 5, 5));

        txtDocumento = new JTextField();
        txtNombre = new JTextField();
        txtTelefono = new JTextField();
        txtEmail = new JTextField();
        txtDireccion = new JTextField();

        formulario.add(new JLabel("Documento:"));
        formulario.add(txtDocumento);
        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);
        formulario.add(new JLabel("Telefono:"));
        formulario.add(txtTelefono);
        formulario.add(new JLabel("Email:"));
        formulario.add(txtEmail);
        formulario.add(new JLabel("Direccion:"));
        formulario.add(txtDireccion);

        btnRegistrar = new JButton("Registrar Cliente");
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
        modelo.addColumn("Documento");
        modelo.addColumn("Nombre");
        modelo.addColumn("Telefono");
        modelo.addColumn("Email");
        modelo.addColumn("Direccion");
        modelo.addColumn("Mascotas");
        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);

        add(norte, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        btnRegistrar.addActionListener(e -> registrar());
        btnModificar.addActionListener(e -> modificar());
        btnEliminar.addActionListener(e -> eliminar());

        tabla.getSelectionModel().addListSelectionListener(e -> cargarSeleccion());

        refrescar();
    }

    private void registrar() {
        try {
            String doc = txtDocumento.getText().trim();
            String nombre = txtNombre.getText().trim();
            String tel = txtTelefono.getText().trim();
            String email = txtEmail.getText().trim();
            String dir = txtDireccion.getText().trim();

            if (!datosValidos(doc, nombre, tel)) {
                return;
            }

            Cliente c = new Cliente(doc, nombre, tel, email, dir);
            veterinaria.registrarCliente(c);

            JOptionPane.showMessageDialog(this, "Cliente registrado correctamente.");
            limpiarCampos();
            refrescar();

        } catch (IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this, "Ocurrio un error inesperado: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificar() {
        if (docSeleccionado == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un cliente de la tabla primero.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nombre = txtNombre.getText().trim();
        String tel = txtTelefono.getText().trim();
        String email = txtEmail.getText().trim();
        String dir = txtDireccion.getText().trim();

        if (!datosValidos(docSeleccionado, nombre, tel)) {
            return;
        }

        Cliente c = veterinaria.buscarClientePorDocumento(docSeleccionado);
        if (c != null) {
            c.setNombre(nombre);
            c.setTelefono(tel);
            c.setEmail(email);
            c.setDireccion(dir);
            veterinaria.actualizarClientes();

            JOptionPane.showMessageDialog(this, "Cliente modificado correctamente.");
            limpiarCampos();
            docSeleccionado = null;
            refrescar();
        }
    }

    private void eliminar() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un cliente de la tabla primero.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String doc = modelo.getValueAt(fila, 0).toString();

        int numMascotas = veterinaria.listarMascotasDeCliente(doc).size();
        if (numMascotas > 0) {
            JOptionPane.showMessageDialog(this,
                    "No se puede eliminar el cliente porque tiene " + numMascotas
                    + " mascota(s) registrada(s).\nElimina o reasigna sus mascotas primero.",
                    "Accion no permitida", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Seguro que deseas eliminar este cliente?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            veterinaria.eliminarCliente(doc);
            JOptionPane.showMessageDialog(this, "Cliente eliminado.");
            limpiarCampos();
            docSeleccionado = null;
            refrescar();
        }
    }

    private boolean datosValidos(String doc, String nombre, String tel) {
        if (doc.isEmpty() || nombre.isEmpty() || tel.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El documento, nombre y telefono son obligatorios.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!doc.matches("\\d+")) {
            JOptionPane.showMessageDialog(this,
                    "El documento solo puede contener numeros.",
                    "Dato invalido", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!nombre.matches("[\\p{L} ]+")) {
            JOptionPane.showMessageDialog(this,
                    "El nombre solo puede contener letras.",
                    "Dato invalido", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!tel.matches("\\d+")) {
            JOptionPane.showMessageDialog(this,
                    "El telefono solo puede contener numeros.",
                    "Dato invalido", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void limpiarCampos() {
        txtDocumento.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        txtDireccion.setText("");
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            return;
        }
        docSeleccionado = modelo.getValueAt(fila, 0).toString();
        Cliente c = veterinaria.buscarClientePorDocumento(docSeleccionado);
        if (c != null) {
            txtDocumento.setText(c.getDocumento());
            txtNombre.setText(c.getNombre());
            txtTelefono.setText(c.getTelefono());
            txtEmail.setText(c.getEmail());
            txtDireccion.setText(c.getDireccion());
        }
    }

    public void refrescar() {
        modelo.setRowCount(0);
        for (Cliente c : veterinaria.getClientes()) {
            int numMascotas = veterinaria.listarMascotasDeCliente(c.getDocumento()).size();
            Object[] fila = { c.getDocumento(), c.getNombre(), c.getTelefono(),
                    c.getEmail(), c.getDireccion(), numMascotas };
            modelo.addRow(fila);
        }
    }
}
