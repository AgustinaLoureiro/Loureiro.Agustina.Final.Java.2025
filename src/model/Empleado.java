package model;

import exception.SalarioInvalidoException;
import java.io.Serializable;
import java.time.LocalDate;
import repository.CSVConvertible;

public abstract class Empleado implements CSVConvertible, Serializable, Comparable<Empleado> {

    private static final long serialVersionUID = 1L;
    private static int contadorLegajo = 100_000;

    private int legajo;
    private String nombre;
    private String apellido;
    private String dni;
    private LocalDate fechaNacimiento;
    private String localidad;
    private LocalDate fechaIngreso;
    private int diasVacaciones;
    private Categoria categoria;

    public Empleado(String nombre, String apellido, String dni, LocalDate fechaNacimiento, String localidad, LocalDate fechaIngreso) {
        this.legajo = ++contadorLegajo;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
        this.localidad = localidad;
        this.fechaIngreso = fechaIngreso;
        this.categoria = Categoria.TRAINEE;
    }

    public int getLegajo() {
        return legajo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getDni() {
        return dni;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public int getDiasVacaciones() {
        return diasVacaciones;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setDiasVacaciones(int diasVacaciones) {
        this.diasVacaciones = diasVacaciones;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public int getAntiguedad() {
        return LocalDate.now().getYear() - fechaIngreso.getYear();
    }

    public String getLocalidad() {
        return localidad;
    }

    public void actualizarCategoria() {
        int anios = getAntiguedad();
        if (anios < 1) {
            this.categoria = Categoria.TRAINEE;
        } else if (anios < 3) {
            this.categoria = Categoria.JUNIOR;
        } else if (anios < 6) {
            this.categoria = Categoria.SEMI_SENIOR;
        } else {
            this.categoria = Categoria.SENIOR;
        }
    }

    public abstract double calcularSalario();

    public abstract void actualizarSalario(double porcentaje) throws SalarioInvalidoException;

    @Override
    public String toString() {
        return "Empleado: " + nombre + " " + apellido + "\n"
                + "Legajo: " + legajo + "\n"
                + "DNI: " + dni + "\n"
                + "Ingreso: " + fechaIngreso + "\n"
                + "Antiguedad: " + getAntiguedad() + "anios\n"
                + "Categoria: " + categoria + "\n"
                + "Vacaciones: " + diasVacaciones + " dias";
    }

    @Override
    public String toHeaderCSV() {
        return "legajo,nombre,apellido,dni,fechaNacimiento,localidad,fechaIngreso,diasVacaciones,categoria";
    }

    @Override
    public String toCSV() {
        return legajo + "," + nombre + "," + apellido + "," + dni + "," + fechaNacimiento + "," + localidad + "," + fechaIngreso + "," + diasVacaciones + "," + categoria;
    }

    public static Empleado fromCSV(String linea) {
        String[] r = linea.split(",");
        switch (r[0]) {
            case "VENDEDOR":
                return new Vendedor(Double.parseDouble(r[10]), r[2], r[3], r[4], LocalDate.parse(r[5]), r[6], LocalDate.parse(r[7]));
            case "OPERARIO":
                return new Operario(Double.parseDouble(r[10]), r[2], r[3], r[4], LocalDate.parse(r[5]), r[6], LocalDate.parse(r[7]));
            case "ADMINISTRATIVO":
                return new Administrativo(Double.parseDouble(r[10]), Sector.valueOf(r[11]), r[2], r[3], r[4], LocalDate.parse(r[5]), r[6], LocalDate.parse(r[7]));
            case "DIRECTIVO":
                return new Directivo(Double.parseDouble(r[10]), Boolean.parseBoolean(r[11]), Boolean.parseBoolean(r[12]), Integer.parseInt(r[13]), r[2], r[3], r[4], LocalDate.parse(r[5]), r[6], LocalDate.parse(r[7]));
            case "JEFE_DEPTO":
                return new JefeDepartamento(r[14], Integer.parseInt(r[15]), Double.parseDouble(r[10]), Boolean.parseBoolean(r[11]), Boolean.parseBoolean(r[12]), Integer.parseInt(r[13]), r[2], r[3], r[4], LocalDate.parse(r[5]), r[6], LocalDate.parse(r[7]));
            default:
                throw new RuntimeException("Tipo desconocido: " + r[0]);
        }
    }

    private static String extraerValor(String bloque, String clave) {
        java.util.regex.Pattern patron = java.util.regex.Pattern.compile(
                "\"" + clave + "\"\\s*:\\s*(\"([^\"]*)\"|[^,}\\n]+)");
        java.util.regex.Matcher m = patron.matcher(bloque);
        if (m.find()) {
            String valor = m.group(1).trim();
            if (valor.startsWith("\"") && valor.endsWith("\"")) {
                return valor.substring(1, valor.length() - 1);
            }
            return valor.replace(",", "").trim();
        }
        return null;
    }

    public static Empleado fromJSON(String bloque) {
        String tipo = extraerValor(bloque, "tipo");
        String nombre = extraerValor(bloque, "nombre");
        String apellido = extraerValor(bloque, "apellido");
        String dni = extraerValor(bloque, "dni");
        LocalDate fechaNacimiento = LocalDate.parse(extraerValor(bloque, "fechaNacimiento"));
        String localidad = extraerValor(bloque, "localidad");
        LocalDate fechaIngreso = LocalDate.parse(extraerValor(bloque, "fechaIngreso"));
        int diasVacaciones = Integer.parseInt(extraerValor(bloque, "diasVacaciones"));
        Categoria categoria = Categoria.valueOf(extraerValor(bloque, "categoria"));

        Empleado emp;
        switch (tipo) {
            case "VENDEDOR":
                emp = new Vendedor(Double.parseDouble(extraerValor(bloque, "sueldoBase")),
                        nombre, apellido, dni, fechaNacimiento, localidad, fechaIngreso);
                break;
            case "OPERARIO":
                emp = new Operario(Double.parseDouble(extraerValor(bloque, "valorHora")),
                        nombre, apellido, dni, fechaNacimiento, localidad, fechaIngreso);
                break;
            case "ADMINISTRATIVO":
                emp = new Administrativo(Double.parseDouble(extraerValor(bloque, "sueldoBase")),
                        Sector.valueOf(extraerValor(bloque, "sector")),
                        nombre, apellido, dni, fechaNacimiento, localidad, fechaIngreso);
                break;
            case "DIRECTIVO":
                emp = new Directivo(Double.parseDouble(extraerValor(bloque, "sueldoBase")),
                        Boolean.parseBoolean(extraerValor(bloque, "autoEmpresa")),
                        Boolean.parseBoolean(extraerValor(bloque, "telEmpresa")),
                        Integer.parseInt(extraerValor(bloque, "diasAdicionales")),
                        nombre, apellido, dni, fechaNacimiento, localidad, fechaIngreso);
                break;
            case "JEFE_DEPTO":
                emp = new JefeDepartamento(
                        extraerValor(bloque, "departamentoCargo"),
                        Integer.parseInt(extraerValor(bloque, "cantSubordinados")),
                        Double.parseDouble(extraerValor(bloque, "sueldoBase")),
                        Boolean.parseBoolean(extraerValor(bloque, "autoEmpresa")),
                        Boolean.parseBoolean(extraerValor(bloque, "telEmpresa")),
                        Integer.parseInt(extraerValor(bloque, "diasAdicionales")),
                        nombre, apellido, dni, fechaNacimiento, localidad, fechaIngreso);
                break;
            default:
                throw new RuntimeException("Tipo desconocido en JSON: " + tipo);
        }
        emp.setDiasVacaciones(diasVacaciones);
        emp.setCategoria(categoria);
        return emp;
    }

    public abstract String toJSON();

    @Override
    public int compareTo(Empleado o) {
        return Integer.compare(this.legajo, o.legajo);
    }

}
