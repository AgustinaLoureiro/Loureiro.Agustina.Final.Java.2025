package model;

import exception.SalarioInvalidoException;
import java.time.LocalDate;

public class Administrativo extends Empleado {

    private double sueldoBase;
    private Sector sector;
    private int cantEmpleadosACargo;

    public Administrativo(double sueldoBase, Sector sector, String nombre, String apellido, String dni, LocalDate fechaNacimiento, String localidad, LocalDate fechaIngreso) {
        super(nombre, apellido, dni, fechaNacimiento, localidad, fechaIngreso);
        this.sueldoBase = sueldoBase;
        this.sector = sector;
        this.cantEmpleadosACargo = 0;
    }

    public Administrativo(double sueldoBase, Sector sector, String nombre, String apellido, String dni, LocalDate fechaNacimiento, LocalDate fechaIngreso) {
        this(sueldoBase, sector, nombre, apellido, dni, fechaNacimiento, "Sin especificar", fechaIngreso);
    }

    public Administrativo(String nombre, String apellido, String dni) {
        this(0.0, Sector.RRHH, nombre, apellido, dni, LocalDate.now(), "Sin especificar", LocalDate.now());
    }

    @Override
    public double calcularSalario() {
        return sueldoBase;
    }

    @Override
    public void actualizarSalario(double porcentaje) throws SalarioInvalidoException {
        if (porcentaje < 0) {
            throw new SalarioInvalidoException("El porcentaje no puede ser negativo: " + porcentaje);
        }
        sueldoBase = sueldoBase + (sueldoBase * porcentaje / 100);
    }

    public double getSueldoBase() {
        return sueldoBase;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }

    public int getCantEmpleadosACargo() {
        return cantEmpleadosACargo;
    }

    public void setCantEmpleadosACargo(int cantEmpleadosACargo) {
        this.cantEmpleadosACargo = cantEmpleadosACargo;
    }

    @Override
    public String toString() {
        return super.toString() + "\n" + String.format("""
                Sector: %s
                Empleados a cargo: %d
                Salario: %.2f""", sector, cantEmpleadosACargo, calcularSalario());
    }

    @Override
    public String toCSV() {
        return "ADMINISTRATIVO," + super.toCSV() + "," + sueldoBase + "," + sector + "," + cantEmpleadosACargo;
    }

    @Override
    public String toJSON() {
        return "{\n"
                + "  \"tipo\": \"ADMINISTRATIVO\",\n"
                + "  \"legajo\": " + getLegajo() + ",\n"
                + "  \"nombre\": \"" + getNombre() + "\",\n"
                + "  \"apellido\": \"" + getApellido() + "\",\n"
                + "  \"dni\": \"" + getDni() + "\",\n"
                + "  \"fechaNacimiento\": \"" + getFechaNacimiento() + "\",\n"
                + "  \"localidad\": \"" + getLocalidad() + "\",\n"
                + "  \"fechaIngreso\": \"" + getFechaIngreso() + "\",\n"
                + "  \"diasVacaciones\": " + getDiasVacaciones() + ",\n"
                + "  \"categoria\": \"" + getCategoria() + "\",\n"
                + "  \"sueldoBase\": " + sueldoBase + ",\n"
                + "  \"sector\": \"" + sector + "\",\n"
                + "  \"cantEmpleadosACargo\": " + cantEmpleadosACargo + "\n"
                + "}";
    }

}
