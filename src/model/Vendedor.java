package model;

import exception.SalarioInvalidoException;
import java.time.LocalDate;

public class Vendedor extends Empleado {

    private double sueldoBase;
    private int cantVentas;
    private double comision;

    public Vendedor(double sueldoBase, String nombre, String apellido, String dni, LocalDate fechaNacimiento, String localidad, LocalDate fechaIngreso) {
        super(nombre, apellido, dni, fechaNacimiento, localidad, fechaIngreso);
        this.sueldoBase = sueldoBase;
    }

    public Vendedor(double sueldoBase, String nombre, String apellido, String dni, LocalDate fechaNacimiento, LocalDate fechaIngreso) {
        this(sueldoBase, nombre, apellido, dni, fechaNacimiento, "Sin especificar", fechaIngreso);
    }

    public Vendedor(String nombre, String apellido, String dni) {
        this(0.0, nombre, apellido, dni, LocalDate.now(), "Sin especificar", LocalDate.now());
    }

    public double calcularComision() {
        if (cantVentas < 5) {
            comision = 0;
        } else if (cantVentas <= 10) {
            comision = sueldoBase * 0.10;
        } else if (cantVentas <= 15) {
            comision = sueldoBase * 0.20;
        } else if (cantVentas <= 20) {
            comision = sueldoBase * 0.30;
        } else {
            comision = sueldoBase * 0.35;
        }
        return comision;
    }

    @Override
    public double calcularSalario() {
        return sueldoBase + calcularComision();
    }

    @Override
    public void actualizarSalario(double porcentaje) throws SalarioInvalidoException {
        if (porcentaje < 0) {
            throw new SalarioInvalidoException("El porcentaje no puede ser negativo: " + porcentaje);
        }
        sueldoBase = sueldoBase + (sueldoBase * porcentaje / 100);
    }

    public void registrarVenta() {
        this.cantVentas++;
    }

    public void resetVentas() {
        this.cantVentas = 0;
    }

    public double getSueldoBase() {
        return sueldoBase;
    }

    public int getCantVentas() {
        return cantVentas;
    }

    @Override
    public String toString() {
        return super.toString() + "\n" + String.format("""
                                                Sueldo Base: %.2f
                                                Cantidad de Ventas en periodo: %d
                                                Sueldo Final: %.2f""", sueldoBase, cantVentas, calcularSalario());

    }

    @Override
    public String toCSV() {
        return "VENDEDOR," + super.toCSV() + "," + sueldoBase + "," + cantVentas;
    }

    @Override
    public String toJSON() {
        return "{\n"
                + "  \"legajo\": " + getLegajo() + ",\n"
                + "  \"tipo\": \"VENDEDOR\",\n"
                + "  \"nombre\": \"" + getNombre() + "\",\n"
                + "  \"apellido\": \"" + getApellido() + "\",\n"
                + "  \"dni\": \"" + getDni() + "\",\n"
                + "  \"fechaNacimiento\": \"" + getFechaNacimiento() + "\",\n"
                + "  \"localidad\": \"" + getLocalidad() + "\",\n"
                + "  \"fechaIngreso\": \"" + getFechaIngreso() + "\",\n"
                + "  \"diasVacaciones\": " + getDiasVacaciones() + ",\n"
                + "  \"categoria\": \"" + getCategoria() + "\",\n"
                + "  \"sueldoBase\": " + sueldoBase + ",\n"
                + "  \"cantVentas\": " + cantVentas + "\n"
                + "}";
    }

}
