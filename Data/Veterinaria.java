package Data;

import Model.Cliente;
import Model.Mascota;
import Model.Cita;
import Model.EstadoCita;
import java.util.ArrayList;

public class Veterinaria {

    private ArrayList<Cliente> clientes;
    private ArrayList<Mascota> mascotas;
    private ArrayList<Cita> citas;

    private int contadorMascotas;
    private int contadorCitas;

    private PersistenciaArchivo persistencia;

    public Veterinaria() {
        this.persistencia = new PersistenciaArchivo();

        this.clientes = persistencia.cargarClientes();
        this.mascotas = persistencia.cargarMascotas(clientes);
        this.citas = persistencia.cargarCitas(mascotas);

        this.contadorMascotas = calcularUltimoIdMascota();
        this.contadorCitas = calcularUltimoIdCita();
    }

    public void registrarCliente(Cliente c) {

        if (buscarClientePorDocumento(c.getDocumento()) != null) {
            throw new IllegalArgumentException("Ya existe un cliente con ese documento.");
        }
        clientes.add(c);
        persistencia.guardarClientes(clientes);
    }

    public Cliente buscarClientePorDocumento(String documento) {
        for (Cliente c : clientes) {
            if (c.getDocumento().equalsIgnoreCase(documento)) {
                return c;
            }
        }
        return null;
    }

    public ArrayList<Cliente> getClientes() {
        return clientes;
    }

    public void actualizarClientes() {
        persistencia.guardarClientes(clientes);
    }

    public void eliminarCliente(String documento) {
        Cliente c = buscarClientePorDocumento(documento);
        if (c != null) {
            clientes.remove(c);
            persistencia.guardarClientes(clientes);
        }
    }

    public void registrarMascota(Mascota m) {
        contadorMascotas++;
        m.setId(contadorMascotas);
        mascotas.add(m);
        persistencia.guardarMascotas(mascotas);
    }

    public Mascota buscarMascotaPorId(int id) {
        for (Mascota m : mascotas) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }

    public ArrayList<Mascota> getMascotas() {
        return mascotas;
    }

    public void actualizarMascotas() {
        persistencia.guardarMascotas(mascotas);
    }

    public void eliminarMascota(int id) {
        Mascota m = buscarMascotaPorId(id);
        if (m != null) {
            mascotas.remove(m);
            persistencia.guardarMascotas(mascotas);
        }
    }

    public ArrayList<Mascota> listarMascotasDeCliente(String documento) {
        ArrayList<Mascota> resultado = new ArrayList<>();
        for (Mascota m : mascotas) {
            if (m.getDueno() != null && m.getDueno().getDocumento().equalsIgnoreCase(documento)) {
                resultado.add(m);
            }
        }
        return resultado;
    }

    public void agendarCita(Cita c) {
        contadorCitas++;
        c.setId(contadorCitas);
        citas.add(c);
        persistencia.guardarCitas(citas);
    }

    public ArrayList<Cita> getCitas() {
        return citas;
    }

    public ArrayList<Cita> listarCitasActivas() {
        ArrayList<Cita> resultado = new ArrayList<>();
        for (Cita c : citas) {
            if (c.getEstado() == EstadoCita.EN_ESPERA || c.getEstado() == EstadoCita.EN_PROCESO) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    public ArrayList<Cita> listarCitasHistorico() {
        ArrayList<Cita> resultado = new ArrayList<>();
        for (Cita c : citas) {
            if (c.getEstado() == EstadoCita.ATENDIDA || c.getEstado() == EstadoCita.CANCELADA) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    public Cita buscarCitaEnHorario(String fecha, String hora, int idIgnorar) {
        for (Cita c : citas) {
            if (c.getId() == idIgnorar) {
                continue;
            }
            if (c.getEstado() == EstadoCita.EN_ESPERA || c.getEstado() == EstadoCita.EN_PROCESO) {
                if (c.getFecha().equals(fecha) && c.getHora().equals(hora)) {
                    return c;
                }
            }
        }
        return null;
    }

    public ArrayList<Cita> listarCitasDeMascota(int idMascota) {
        ArrayList<Cita> resultado = new ArrayList<>();
        for (Cita c : citas) {
            if (c.getMascota() != null && c.getMascota().getId() == idMascota) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    public void actualizarCitas() {
        persistencia.guardarCitas(citas);
    }

    private int calcularUltimoIdMascota() {
        int max = 0;
        for (Mascota m : mascotas) {
            if (m.getId() > max) {
                max = m.getId();
            }
        }
        return max;
    }

    private int calcularUltimoIdCita() {
        int max = 0;
        for (Cita c : citas) {
            if (c.getId() > max) {
                max = c.getId();
            }
        }
        return max;
    }
}
