package Model;

public class Mascota {

    private int id;
    private String nombre;
    private String especie;
    private String raza;
    private int edad;
    private Sexo sexo;
    private Cliente dueno;

    public Mascota(int id, String nombre, String especie, String raza, int edad, Sexo sexo, Cliente dueno) {
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.edad = edad;
        this.sexo = sexo;
        this.dueno = dueno;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }

    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public Sexo getSexo() { return sexo; }
    public void setSexo(Sexo sexo) { this.sexo = sexo; }

    public Cliente getDueno() { return dueno; }
    public void setDueno(Cliente dueno) { this.dueno = dueno; }

    @Override
    public String toString() {
        return nombre + " - " + especie + " (" + raza + ")";
    }
}
