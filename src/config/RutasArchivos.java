package config;

import java.nio.file.Path;
import java.nio.file.Paths;

public interface RutasArchivos {

    static final String BASE = "src/data";
    static final String FILE_CSV = "empleados.csv";
    static final String FILE_BIN = "empleados.bin";

    static Path getRutaCSV() {
        return Paths.get(BASE, FILE_CSV);
    }

    static Path getRutaBIN() {
        return Paths.get(BASE, FILE_BIN);
    }

    static String getRutaCSVString() {
        return getRutaCSV().toString();
    }

    static String getRutaBINString() {
        return getRutaBIN().toString();
    }

    static final String FILE_JSON = "empleados.json";

    static Path getRutaJSON() {
        return Paths.get(BASE, FILE_JSON);
    }

    static String getRutaJSONString() {
        return getRutaJSON().toString();
    }

    static final String FILE_TXT = "reporte.txt";

    static String getRutaTXTString() {
        return Paths.get(BASE, FILE_TXT).toString();
    }
}
