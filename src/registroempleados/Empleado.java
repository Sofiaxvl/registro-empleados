package registroempleados;

public class Empleado {

    private int id;
    private String nombre;
    private String cargo;
    private double salario;
    private String correo;

    public Empleado() {
    }

    public Empleado(int id, String nombre, String cargo, double salario, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.cargo = cargo;
        this.salario = salario;
        this.correo = correo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public double getSalario() { return salario; }
    public void setSalario(double salario) { this.salario = salario; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
}