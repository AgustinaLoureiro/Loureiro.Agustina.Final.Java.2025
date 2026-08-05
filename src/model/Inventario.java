package model;

import exception.EmpleadoNoEncontradoException;
import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import repository.Almacenable;
import repository.CSVConvertible;

public class Inventario<T extends CSVConvertible & Serializable> implements Almacenable<T> {

    private List<T> lista = new ArrayList<>();

    @Override
    public void agregar(T elemento) {
        lista.add(elemento);
    }

    @Override
    public void eliminarSegun(Predicate<T> criterio) {
        lista.removeIf(criterio);
    }

    @Override
    public List<T> obtenerTodos() {
        return lista;
    }

    @Override
    public T buscar(Predicate<T> criterio) throws EmpleadoNoEncontradoException {
        for (T e : lista) {
            if (criterio.test(e)) {
                return e;
            }
        }
        throw new EmpleadoNoEncontradoException("No se encontró ningún empleado con ese criterio");
    }

    @Override
    public void ordenar() {
        Collections.sort((List) lista);
    }

    @Override
    public void ordenar(Comparator<T> comparador) {
        lista.sort(comparador);
    }

    @Override
    public List<T> filtrar(Predicate<T> criterio) {
        List<T> filtrado = new ArrayList<>();
        for (T e : lista) {
            if (criterio.test(e)) {
                filtrado.add(e);
            }
        }
        return filtrado;
    }

    @Override
    public List<T> transformar(Function<T, T> operador) {
        List<T> transformado = new ArrayList<>();
        for (T e : lista) {
            transformado.add(operador.apply(e));
        }
        return transformado;
    }

    @Override
    public int contar(Predicate<T> criterio) {
        int c = 0;
        for (T e : lista) {
            if (criterio.test(e)) {
                c++;
            }
        }
        return c;
    }

    @Override
    public void guardarEnBinario(String ruta) throws Exception {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruta))) {
            oos.writeObject(lista);
        }
    }

    @Override
    public void cargarDesdeBinario(String ruta) throws Exception {
        lista.clear();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ruta))) {
            lista = (List<T>) ois.readObject();
        }
    }

    @Override
    public void guardarEnCSV(String ruta) throws Exception {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            for (T item : lista) {
                bw.write(item.toCSV());
                bw.newLine();
            }
        }
    }

    @Override
    public void cargarDesdeCSV(String ruta, Function<String, T> fromCSV) throws Exception {
        lista.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lista.add(fromCSV.apply(linea));
            }
        }
    }

    @Override
    public void cargarDesdeJSON(String ruta) throws Exception {
        lista.clear();
        StringBuilder contenido = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                contenido.append(linea).append("\n");
            }
        }
        String texto = contenido.toString().trim();
        texto = texto.substring(1, texto.length() - 1).trim(); // saca los [ ] externos
        String[] bloques = texto.split("\\}\\s*,\\s*\\{");
        for (String bloque : bloques) {
            String b = bloque.trim();
            if (!b.startsWith("{")) {
                b = "{" + b;
            }
            if (!b.endsWith("}")) {
                b = b + "}";
            }
            Empleado emp = Empleado.fromJSON(b);
            lista.add((T) emp);
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new InventarioIterator();
    }

    private class InventarioIterator implements Iterator<T> {

        private int indiceActual = 0;

        @Override
        public boolean hasNext() {
            return indiceActual < lista.size();
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No hay más elementos en el inventario");
            }
            T elemento = lista.get(indiceActual);
            indiceActual++;
            return elemento;
        }

        @Override
        public void remove() {
            if (indiceActual <= 0) {
                throw new IllegalStateException("Debe llamarse next() antes de remove()");
            }
            indiceActual--;
            lista.remove(indiceActual);
        }
    }

    public static void mostrarEmpleados(List<? extends Empleado> lista) {
        for (Empleado e : lista) {
            System.out.println(e);
        }
    }

    public static void agregarEmpleados(List<? super Empleado> lista, Empleado e) {
        lista.add(e);
    }

    @Override
    public void guardarEnJSON(String ruta) throws Exception {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            bw.write("[\n");
            for (int i = 0; i < lista.size(); i++) {
                bw.write(((Empleado) lista.get(i)).toJSON());
                if (i < lista.size() - 1) {
                    bw.write(",\n");
                }
            }
            bw.write("\n]");
        }
    }

    @Override
    public void exportarTXT(String ruta, Predicate<T> criterio) throws Exception {
        List<T> filtrados = filtrar(criterio);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ruta))) {
            bw.write("========================================");
            bw.newLine();
            bw.write("   REPORTE DE EMPLEADOS");
            bw.newLine();
            bw.write("   Fecha: " + LocalDate.now());
            bw.newLine();
            bw.write("========================================");
            bw.newLine();
            bw.newLine();
            for (T item : filtrados) {
                bw.write(item.toString());
                bw.newLine();
                bw.write("----------------------------------------");
                bw.newLine();
            }
            bw.write("Total: " + filtrados.size() + " empleados");
        }
    }

}
