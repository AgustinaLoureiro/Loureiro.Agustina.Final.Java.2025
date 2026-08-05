package repository;

import exception.EmpleadoNoEncontradoException;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Almacenable<T> extends Iterable<T> {

    void agregar(T elemento);

    void eliminarSegun(Predicate<T> criterio);

    List<T> obtenerTodos();

    T buscar(Predicate<T> criterio) throws EmpleadoNoEncontradoException;

    void ordenar();

    void ordenar(Comparator<T> comparador);

    List<T> filtrar(Predicate<T> criterio);

    List<T> transformar(Function<T, T> operador);

    int contar(Predicate<T> criterio);

    void guardarEnBinario(String ruta) throws Exception;

    void cargarDesdeBinario(String ruta) throws Exception;

    void guardarEnCSV(String ruta) throws Exception;

    void cargarDesdeCSV(String ruta, Function<String, T> fromCSV) throws Exception;

    void guardarEnJSON(String ruta) throws Exception;
    
    void cargarDesdeJSON(String ruta) throws Exception;

    void exportarTXT(String ruta, Predicate<T> criterio) throws Exception;

}
