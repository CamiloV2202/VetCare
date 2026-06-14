package Model;

public class Cliente extends Persona {

    private String email;
    private String direccion;

    public Cliente(String documento, String nombre, String telefono, String email, String direccion) {
        super(documento, nombre, telefono);
        this.email = email;
        this.direccion = direccion;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
}
