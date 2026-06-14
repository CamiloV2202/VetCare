package Data;

import Model.Cliente;
import Model.Mascota;
import Model.Cita;
import Model.Sexo;
import Model.EstadoCita;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class PersistenciaArchivo {

    private static final String SEP = ";";
    private final String archivoClientes = "clientes.csv";
    private final String archivoMascotas = "mascotas.csv";
    private final String archivoCitas = "citas.csv";

    public void guardarClientes(ArrayList<Cliente> clientes) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoClientes))) {
            for (Cliente c : clientes) {

                bw.write(c.getDocumento() + SEP + c.getNombre() + SEP + c.getTelefono()
                        + SEP + c.getEmail() + SEP + c.getDireccion());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error guardando clientes: " + e.getMessage());
        }
    }

    public ArrayList<Cliente> cargarClientes() {
        ArrayList<Cliente> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivoClientes))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }
                String[] datos = linea.split(SEP);

                Cliente c = new Cliente(datos[0], datos[1], datos[2], datos[3], datos[4]);
                lista.add(c);
            }
        } catch (IOException e) {

            System.out.println("No se encontro clientes.csv, se empieza vacio.");
        }
        return lista;
    }

    public void guardarMascotas(ArrayList<Mascota> mascotas) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoMascotas))) {
            for (Mascota m : mascotas) {

                bw.write(m.getId() + SEP + m.getNombre() + SEP + m.getEspecie() + SEP
                        + m.getRaza() + SEP + m.getEdad() + SEP + m.getSexo() + SEP
                        + m.getDueno().getDocumento());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error guardando mascotas: " + e.getMessage());
        }
    }

    public ArrayList<Mascota> cargarMascotas(ArrayList<Cliente> clientes) {
        ArrayList<Mascota> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivoMascotas))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }
                String[] datos = linea.split(SEP);
                int id = Integer.parseInt(datos[0]);
                String nombre = datos[1];
                String especie = datos[2];
                String raza = datos[3];
                int edad = Integer.parseInt(datos[4]);
                Sexo sexo = Sexo.valueOf(datos[5]);
                String docDueno = datos[6];

                Cliente dueno = buscarClientePorDoc(clientes, docDueno);

                Mascota m = new Mascota(id, nombre, especie, raza, edad, sexo, dueno);
                lista.add(m);
            }
        } catch (IOException e) {
            System.out.println("No se encontro mascotas.csv, se empieza vacio.");
        }
        return lista;
    }

    public void guardarCitas(ArrayList<Cita> citas) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoCitas))) {
            for (Cita c : citas) {

                bw.write(c.getId() + SEP + c.getMascota().getId() + SEP + c.getFecha() + SEP
                        + c.getHora() + SEP + c.getMotivo() + SEP + c.getObservaciones()
                        + SEP + c.getPrioridad() + SEP + c.getEstado().name()
                        + SEP + c.getHoraAtencion() + SEP + c.getResultadoAtencion()
                        + SEP + c.getMotivoCancelacion() + SEP + c.isModificada());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error guardando citas: " + e.getMessage());
        }
    }

    public ArrayList<Cita> cargarCitas(ArrayList<Mascota> mascotas) {
        ArrayList<Cita> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(archivoCitas))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }
                String[] datos = linea.split(SEP);
                int id = Integer.parseInt(datos[0]);
                int idMascota = Integer.parseInt(datos[1]);
                String fecha = datos[2];
                String hora = datos[3];
                String motivo = datos[4];

                String observaciones = (datos.length > 5) ? datos[5] : "";
                String prioridad = (datos.length > 6) ? datos[6] : "Normal";
                String estadoTexto = (datos.length > 7) ? datos[7] : "EN_ESPERA";
                String horaAtencion = (datos.length > 8) ? datos[8] : "";
                String resultadoAtencion = (datos.length > 9) ? datos[9] : "";
                String motivoCancelacion = (datos.length > 10) ? datos[10] : "";
                boolean modificada = (datos.length > 11) && Boolean.parseBoolean(datos[11]);

                EstadoCita estado;
                try {
                    estado = EstadoCita.valueOf(estadoTexto);
                } catch (Exception ex) {
                    estado = EstadoCita.EN_ESPERA;
                }

                Mascota m = buscarMascotaPorId(mascotas, idMascota);

                Cita c = new Cita(id, m, fecha, hora, motivo);
                c.setObservaciones(observaciones);
                c.setPrioridad(prioridad);
                c.setEstado(estado);
                c.setHoraAtencion(horaAtencion);
                c.setResultadoAtencion(resultadoAtencion);
                c.setMotivoCancelacion(motivoCancelacion);
                c.setModificada(modificada);
                lista.add(c);
            }
        } catch (IOException e) {
            System.out.println("No se encontro citas.csv, se empieza vacio.");
        }
        return lista;
    }

    private Cliente buscarClientePorDoc(ArrayList<Cliente> clientes, String doc) {
        for (Cliente c : clientes) {
            if (c.getDocumento().equalsIgnoreCase(doc)) {
                return c;
            }
        }
        return null;
    }

    private Mascota buscarMascotaPorId(ArrayList<Mascota> mascotas, int id) {
        for (Mascota m : mascotas) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }
}
