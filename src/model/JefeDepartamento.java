package model;

import java.time.LocalDate;

public class JefeDepartamento extends Directivo {

    private String departamentoCargo;
    private int cantSubordinados;

    public JefeDepartamento(String departamentoCargo, int cantSubordinados, double sueldoBase, boolean autoEmpresa, boolean telEmpresa, int diasAdicionales, String nombre, String apellido, String dni, LocalDate fechaNacimiento, String localidad, LocalDate fechaIngreso) {
        super(sueldoBase, autoEmpresa, telEmpresa, diasAdicionales, nombre, apellido, dni, fechaNacimiento, localidad, fechaIngreso);
        this.departamentoCargo = departamentoCargo;
        this.cantSubordinados = cantSubordinados;
    }

    public JefeDepartamento(String departamentoCargo, int cantSubordinados, double sueldoBase, boolean autoEmpresa, boolean telEmpresa, int diasAdicionales, String nombre, String apellido, String dni, LocalDate fechaNacimiento, LocalDate fechaIngreso) {
        this(departamentoCargo, cantSubordinados, sueldoBase, autoEmpresa, telEmpresa, diasAdicionales, nombre, apellido, dni, fechaNacimiento, "Sin especificar", fechaIngreso);
    }

    public JefeDepartamento(String nombre, String apellido, String dni) {
        this("Sin asignar", 0, 0.0, false, false, 0, nombre, apellido, dni, LocalDate.now(), "Sin especificar", LocalDate.now());
    }

    public String getDepartamentoCargo() {
        return departamentoCargo;
    }

    public void setDepartamentoCargo(String departamentoCargo) {
        this.departamentoCargo = departamentoCargo;
    }

    public int getCantSubordinados() {
        return cantSubordinados;
    }

    public void setCantSubordinados(int cantSubordinados) {
        this.cantSubordinados = cantSubordinados;
    }

    @Override
    public String toString() {
        return super.toString() + "\n" + String.format("""
                Departamento a cargo: %s
                Cantidad de subordinados: %d""", departamentoCargo, cantSubordinados);
    }

    @Override
    public String toCSV() {
        return "JEFE_DEPTO," + datosCSV() + "," + departamentoCargo + "," + cantSubordinados;
    }

    @Override
    public String toJSON() {
        return "{\n"
                + "  \"tipo\": \"JEFE_DEPTO\",\n"
                + "  \"legajo\": " + getLegajo() + ",\n"
                + "  \"nombre\": \"" + getNombre() + "\",\n"
                + "  \"apellido\": \"" + getApellido() + "\",\n"
                + "  \"dni\": \"" + getDni() + "\",\n"
                + "  \"fechaNacimiento\": \"" + getFechaNacimiento() + "\",\n"
                + "  \"localidad\": \"" + getLocalidad() + "\",\n"
                + "  \"fechaIngreso\": \"" + getFechaIngreso() + "\",\n"
                + "  \"diasVacaciones\": " + getDiasVacaciones() + ",\n"
                + "  \"categoria\": \"" + getCategoria() + "\",\n"
                + "  \"sueldoBase\": " + getSueldoBase() + ",\n"
                + "  \"autoEmpresa\": " + isAutoEmpresa() + ",\n"
                + "  \"telEmpresa\": " + isTelEmpresa() + ",\n"
                + "  \"diasAdicionales\": " + getDiasAdicionales() + ",\n"
                + "  \"departamentoCargo\": \"" + departamentoCargo + "\",\n"
                + "  \"cantSubordinados\": " + cantSubordinados + "\n"
                + "}";
    }

}
