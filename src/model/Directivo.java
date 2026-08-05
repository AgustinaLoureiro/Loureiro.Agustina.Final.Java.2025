package model;

import exception.SalarioInvalidoException;
import java.time.LocalDate;

public class Directivo extends Empleado {

    private double sueldoBase;
    private boolean autoEmpresa;
    private boolean telEmpresa;
    private int diasAdicionales;

    public Directivo(double sueldoBase, boolean autoEmpresa, boolean telEmpresa, int diasAdicionales, String nombre, String apellido, String dni, LocalDate fechaNacimiento, String localidad, LocalDate fechaIngreso) {
        super(nombre, apellido, dni, fechaNacimiento, localidad, fechaIngreso);
        this.sueldoBase = sueldoBase;
        this.autoEmpresa = autoEmpresa;
        this.telEmpresa = telEmpresa;
        this.diasAdicionales = diasAdicionales;
    }

    public Directivo(double sueldoBase, boolean autoEmpresa, boolean telEmpresa, int diasAdicionales, String nombre, String apellido, String dni, LocalDate fechaNacimiento, LocalDate fechaIngreso) {
        this(sueldoBase, autoEmpresa, telEmpresa, diasAdicionales, nombre, apellido, dni, fechaNacimiento, "Sin especificar", fechaIngreso);
    }

    public Directivo(String nombre, String apellido, String dni) {
        this(0.0, false, false, 0, nombre, apellido, dni, LocalDate.now(), "Sin especificar", LocalDate.now());
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

    public boolean isAutoEmpresa() {
        return autoEmpresa;
    }

    public void setAutoEmpresa(boolean autoEmpresa) {
        this.autoEmpresa = autoEmpresa;
    }

    public boolean isTelEmpresa() {
        return telEmpresa;
    }

    public void setTelEmpresa(boolean telEmpresa) {
        this.telEmpresa = telEmpresa;
    }

    public int getDiasAdicionales() {
        return diasAdicionales;
    }

    public void setDiasAdicionales(int diasAdicionales) {
        this.diasAdicionales = diasAdicionales;
    }

    @Override
    public String toString() {
        return super.toString() + "\n" + String.format("""
                Auto empresa: %s
                Tel empresa: %s
                Dias adicionales vacaciones: %d
                Salario: %.2f""", autoEmpresa ? "Si" : "No", telEmpresa ? "Si" : "No", diasAdicionales, calcularSalario());
    }

    protected String datosCSV() {
        return super.toCSV() + "," + sueldoBase + "," + autoEmpresa + "," + telEmpresa + "," + diasAdicionales;
    }

    @Override
    public String toCSV() {
        return "DIRECTIVO," + datosCSV();
    }

    @Override
    public String toJSON() {
        return "{\n"
                + "  \"tipo\": \"DIRECTIVO\",\n"
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
                + "  \"autoEmpresa\": " + autoEmpresa + ",\n"
                + "  \"telEmpresa\": " + telEmpresa + ",\n"
                + "  \"diasAdicionales\": " + diasAdicionales + "\n"
                + "}";
    }

}
