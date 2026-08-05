package model;

import exception.SalarioInvalidoException;
import java.time.LocalDate;
import java.time.LocalTime;

public class Operario extends Empleado {

    private double valorHora;
    private int horasTrabajadas;
    private int diasFranco;

    public Operario(double valorHora, String nombre, String apellido, String dni, LocalDate fechaNacimiento, String localidad, LocalDate fechaIngreso) {
        super(nombre, apellido, dni, fechaNacimiento, localidad, fechaIngreso);
        this.valorHora = valorHora;
    }

    public Operario(double valorHora, String nombre, String apellido, String dni, LocalDate fechaNacimiento, LocalDate fechaIngreso) {
        this(valorHora, nombre, apellido, dni, fechaNacimiento, "Sin especificar", fechaIngreso);
    }

    public Operario(String nombre, String apellido, String dni) {
        this(0.0, nombre, apellido, dni, LocalDate.now(), "Sin especificar", LocalDate.now());
    }

    public void registrarHoras(LocalTime horaEntrada, LocalTime horaSalida) {
        int horas = horaSalida.getHour() - horaEntrada.getHour();
        this.horasTrabajadas += horas;
    }

    public void resetHoras() {
        this.horasTrabajadas = 0;
    }

    @Override
    public double calcularSalario() {
        return horasTrabajadas * valorHora;
    }

    @Override
    public void actualizarSalario(double porcentaje) throws SalarioInvalidoException {
        if (porcentaje < 0) {
            throw new SalarioInvalidoException("El porcentaje no puede ser negativo: " + porcentaje);
        }
        valorHora = valorHora + (valorHora * porcentaje / 100);
    }

    public double getValorHora() {
        return valorHora;
    }

    public int getHorasTrabajadas() {
        return horasTrabajadas;
    }

    public int getDiasFranco() {
        return diasFranco;
    }

    public void setDiasFranco(int diasFranco) {
        this.diasFranco = diasFranco;
    }

    @Override
    public String toString() {
        return super.toString() + "\n" + String.format("""
                Valor Hora: %.2f
                Horas Trabajadas: %d
                Dias Franco: %d
                Salario: %.2f""", valorHora, horasTrabajadas, diasFranco, calcularSalario());
    }

    @Override
    public String toCSV() {
        return "OPERARIO," + super.toCSV() + "," + valorHora + "," + horasTrabajadas + "," + diasFranco;
    }

    @Override
    public String toJSON() {
        return "{\n"
                + "  \"tipo\": \"OPERARIO\",\n"
                + "  \"legajo\": " + getLegajo() + ",\n"
                + "  \"nombre\": \"" + getNombre() + "\",\n"
                + "  \"apellido\": \"" + getApellido() + "\",\n"
                + "  \"dni\": \"" + getDni() + "\",\n"
                + "  \"fechaNacimiento\": \"" + getFechaNacimiento() + "\",\n"
                + "  \"localidad\": \"" + getLocalidad() + "\",\n"
                + "  \"fechaIngreso\": \"" + getFechaIngreso() + "\",\n"
                + "  \"diasVacaciones\": " + getDiasVacaciones() + ",\n"
                + "  \"categoria\": \"" + getCategoria() + "\",\n"
                + "  \"valorHora\": " + valorHora + ",\n"
                + "  \"horasTrabajadas\": " + horasTrabajadas + ",\n"
                + "  \"diasFranco\": " + diasFranco + "\n"
                + "}";
    }

}
