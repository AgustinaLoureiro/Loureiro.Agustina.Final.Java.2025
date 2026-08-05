package loureiro_agustina_final;

import config.RutasArchivos;
import exception.EmpleadoNoEncontradoException;
import exception.SalarioInvalidoException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.*;
import repository.Almacenable;

public class Loureiro_Agustina_FINAL {

    public static void main(String[] args) {

        try {
            Almacenable<Empleado> inv = new Inventario<>();

            // 1) Agregar empleados
            inv.agregar(new Vendedor(150000, "Ana", "Garcia", "12345678",
                    LocalDate.of(1990, 5, 15), "Buenos Aires",
                    LocalDate.of(2020, 3, 1)));

            inv.agregar(new Operario(500, "Carlos", "Lopez", "23456789",
                    LocalDate.of(1985, 8, 20), "Cordoba",
                    LocalDate.of(2015, 6, 1)));

            inv.agregar(new Administrativo(120000, Sector.RRHH, "Maria", "Fernandez", "34567890",
                    LocalDate.of(1992, 2, 10), "Rosario",
                    LocalDate.of(2018, 1, 15)));

            inv.agregar(new Directivo(300000, true, true, 10, "Roberto", "Martinez", "45678901",
                    LocalDate.of(1975, 11, 30), "Buenos Aires",
                    LocalDate.of(2010, 4, 1)));

            inv.agregar(new JefeDepartamento("Ventas", 5, 250000, false, true, 7, "Laura", "Perez", "56789012",
                    LocalDate.of(1980, 7, 25), "Mendoza",
                    LocalDate.of(2012, 9, 1)));

            // Demostracion de wildcards: ? extends y ? super
            System.out.println("\n=== Wildcard ? extends Empleado (solo lectura) ===");
            Inventario.mostrarEmpleados(inv.obtenerTodos());

            System.out.println("\n=== Wildcard ? super Empleado (escritura) ===");
            List<Object> listaGenerica = new ArrayList<>();
            Inventario.agregarEmpleados(listaGenerica, new Vendedor(100000, "Test", "Wildcard", "99999999",
                    LocalDate.of(1995, 1, 1), "CABA", LocalDate.of(2022, 1, 1)));
            System.out.println("Elementos en la lista generica tras agregarEmpleados: " + listaGenerica.size());

            // 2) Listar todos
            System.out.println("=== Inventario Original ===");
            for (Empleado e : inv.obtenerTodos()) {
                System.out.println(e);
                System.out.println("---");
            }

            // 3) Orden natural (por legajo)
            inv.ordenar();
            System.out.println("\n=== Orden por Legajo ===");
            for (Empleado e : inv.obtenerTodos()) {
                System.out.println(e.getLegajo() + " - " + e.getNombre() + " " + e.getApellido());
            }

            // 4) Ordenar por apellido y por salario
            inv.ordenar((a, b) -> a.getApellido().compareToIgnoreCase(b.getApellido()));
            System.out.println("\n=== Orden por Apellido ===");
            for (Empleado e : inv.obtenerTodos()) {
                System.out.println(e.getLegajo() + " - " + e.getNombre() + " " + e.getApellido());
            }

            inv.ordenar((a, b) -> Double.compare(a.calcularSalario(), b.calcularSalario()));
            System.out.println("\n=== Orden por Salario ===");
            for (Empleado e : inv.obtenerTodos()) {
                System.out.println(e.getLegajo() + " - " + e.getNombre() + " " + e.getApellido() + " - " + e.calcularSalario());
            }

            // 5) Filtrar vendedores
            System.out.println("\n=== Solo Vendedores ===");
            List<Empleado> vendedores = inv.filtrar(e -> e instanceof Vendedor);
            for (Empleado e : vendedores) {
                System.out.println(e);
            }

            // 6) Contar empleados con mas de 5 anios de antiguedad
            int antiguos = inv.contar(e -> e.getAntiguedad() > 5);
            System.out.println("\nEmpleados con mas de 5 anios: " + antiguos);

            // 7) Actualizar categoria de todos
            System.out.println("\n=== Actualizando Categorias ===");
            inv.transformar(e -> {
                e.actualizarCategoria();
                return e;
            });

            // 8) Actualizar salario 10%
            System.out.println("\n=== Actualizando Salarios 10% ===");
            for (Empleado e : inv.obtenerTodos()) {
                e.actualizarSalario(10);
            }

            // 9) Buscar empleado por DNI
            Empleado encontrado = inv.buscar(e -> e.getDni().equals("12345678"));
            System.out.println("\n=== Empleado Encontrado ===");
            System.out.println(encontrado);

            // 10) Persistencia
            inv.guardarEnBinario(RutasArchivos.getRutaBINString());
            inv.guardarEnCSV(RutasArchivos.getRutaCSVString());
            inv.guardarEnJSON(RutasArchivos.getRutaJSONString());
            System.out.println("\nArchivos guardados correctamente.");

            // 11) Cargar desde CSV
            Almacenable<Empleado> invCSV = new Inventario<>();
            invCSV.cargarDesdeCSV(RutasArchivos.getRutaCSVString(), Empleado::fromCSV);
            System.out.println("\n=== Inventario cargado desde CSV ===");
            for (Empleado e : invCSV.obtenerTodos()) {
                System.out.println(e);
                System.out.println("---");
            }

            // 12) Eliminar empleado
            inv.eliminarSegun(e -> e.getDni().equals("23456789"));
            System.out.println("\n=== Despues de eliminar Carlos Lopez ===");
            for (Empleado e : inv.obtenerTodos()) {
                System.out.println(e.getNombre() + " " + e.getApellido());
            }

            // 13) Reporte TXT
            inv.exportarTXT(RutasArchivos.getRutaTXTString(), e -> e.getAntiguedad() > 5);
            System.out.println("Reporte TXT exportado correctamente.");

        } catch (EmpleadoNoEncontradoException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (SalarioInvalidoException e) {
            System.err.println("Error de salario: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error general: " + e.getMessage());
        }
    }
}
