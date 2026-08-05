package loureiro_agustina_final;

import config.RutasArchivos;
import exception.SalarioInvalidoException;
import java.time.LocalDate;
import java.util.List;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.*;
import repository.Almacenable;
import java.util.Optional;

public class App extends Application {

    private Almacenable<Empleado> inventario = new Inventario<>();
    private ObservableList<Empleado> listaObservable = FXCollections.observableArrayList();
    private TableView<Empleado> tabla = new TableView<>();

    @Override
    public void start(Stage stage) {
        cargarDatosEjemplo();

        TableColumn<Empleado, String> colLegajo = new TableColumn<>("Legajo");
        colLegajo.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getLegajo())));

        TableColumn<Empleado, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre() + " " + data.getValue().getApellido()));

        TableColumn<Empleado, String> colDni = new TableColumn<>("DNI");
        colDni.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDni()));

        TableColumn<Empleado, String> colCategoria = new TableColumn<>("Categoria");
        colCategoria.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategoria().toString()));

        TableColumn<Empleado, String> colAntiguedad = new TableColumn<>("Antiguedad");
        colAntiguedad.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAntiguedad() + " anios"));

        TableColumn<Empleado, String> colSalario = new TableColumn<>("Salario");
        colSalario.setCellValueFactory(data -> new SimpleStringProperty(String.format("%.2f", data.getValue().calcularSalario())));

        tabla.getColumns().addAll(colLegajo, colNombre, colDni, colCategoria, colAntiguedad, colSalario);
        tabla.setItems(listaObservable);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Button btnOrdenarLegajo = new Button("Ordenar por Legajo");
        btnOrdenarLegajo.setOnAction(e -> {
            inventario.ordenar();
            actualizarTabla();
        });

        Button btnOrdenarApellido = new Button("Ordenar por Apellido");
        btnOrdenarApellido.setOnAction(e -> {
            inventario.ordenar((a, b) -> a.getApellido().compareToIgnoreCase(b.getApellido()));
            actualizarTabla();
        });

        Button btnOrdenarSalario = new Button("Ordenar por Salario");
        btnOrdenarSalario.setOnAction(e -> {
            inventario.ordenar((a, b) -> Double.compare(a.calcularSalario(), b.calcularSalario()));
            actualizarTabla();
        });

        Button btnFiltrarVendedores = new Button("Ver Vendedores");
        btnFiltrarVendedores.setOnAction(e -> {
            List<Empleado> vendedores = inventario.filtrar(emp -> emp instanceof Vendedor);
            tabla.setItems(FXCollections.observableArrayList(vendedores));
        });

        Button btnMostrarTodos = new Button("Mostrar Todos");
        btnMostrarTodos.setOnAction(e -> {
            tabla.setItems(listaObservable);
            actualizarTabla();
        });

        Button btnActualizarCategorias = new Button("Actualizar Categorias");
        btnActualizarCategorias.setOnAction(e -> {
            inventario.transformar(emp -> {
                emp.actualizarCategoria();
                return emp;
            });
            actualizarTabla();
        });

        Button btnAumentarSalario = new Button("Aumento 10%");
        btnAumentarSalario.setOnAction(e -> {
            try {
                for (Empleado emp : inventario.obtenerTodos()) {
                    emp.actualizarSalario(10);
                }
                actualizarTabla();
            } catch (SalarioInvalidoException ex) {
                mostrarAlerta("Error", ex.getMessage());
            }
        });

        Button btnEliminar = new Button("Eliminar Seleccionado");
        btnEliminar.setOnAction(e -> {
            Empleado seleccionado = tabla.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                inventario.eliminarSegun(emp -> emp.getLegajo() == seleccionado.getLegajo());
                actualizarTabla();
            } else {
                mostrarAlerta("Aviso", "Selecciona un empleado primero");
            }
        });

        Button btnGuardarCSV = new Button("Guardar CSV");
        btnGuardarCSV.setOnAction(e -> {
            try {
                inventario.guardarEnCSV(RutasArchivos.getRutaCSVString());
                mostrarAlerta("Exito", "CSV guardado correctamente");
            } catch (Exception ex) {
                mostrarAlerta("Error", ex.getMessage());
            }
        });

        Button btnGuardarBIN = new Button("Guardar BIN");
        btnGuardarBIN.setOnAction(e -> {
            try {
                inventario.guardarEnBinario(RutasArchivos.getRutaBINString());
                mostrarAlerta("Exito", "BIN guardado correctamente");
            } catch (Exception ex) {
                mostrarAlerta("Error", ex.getMessage());
            }
        });

        Button btnGuardarJSON = new Button("Guardar JSON");
        btnGuardarJSON.setOnAction(e -> {
            try {
                inventario.guardarEnJSON(RutasArchivos.getRutaJSONString());
                mostrarAlerta("Exito", "JSON guardado correctamente");
            } catch (Exception ex) {
                mostrarAlerta("Error", ex.getMessage());
            }
        });

        Button btnExportarTXT = new Button("Exportar TXT");
        btnExportarTXT.setOnAction(e -> {
            try {
                inventario.exportarTXT(RutasArchivos.getRutaTXTString(), emp -> emp.getAntiguedad() > 5);
                mostrarAlerta("Exito", "TXT exportado correctamente");
            } catch (Exception ex) {
                mostrarAlerta("Error", ex.getMessage());
            }
        });

        Button btnCargarCSV = new Button("Cargar CSV");
        btnCargarCSV.setOnAction(e -> {
            try {
                inventario.cargarDesdeCSV(RutasArchivos.getRutaCSVString(), Empleado::fromCSV);
                actualizarTabla();
                mostrarAlerta("Exito", "CSV cargado correctamente");
            } catch (Exception ex) {
                mostrarAlerta("Error", ex.getMessage());
            }
        });

        Button btnCargarJSON = new Button("Cargar JSON");
        btnCargarJSON.setOnAction(e -> {
            try {
                inventario.cargarDesdeJSON(RutasArchivos.getRutaJSONString());
                actualizarTabla();
                mostrarAlerta("Exito", "JSON cargado correctamente");
            } catch (Exception ex) {
                mostrarAlerta("Error", ex.getMessage());
            }
        });

        Button btnAgregar = new Button("Agregar Empleado");
        btnAgregar.setOnAction(e -> mostrarDialogoAgregar());

        FlowPane panelBotones = new FlowPane();
        panelBotones.setHgap(8);
        panelBotones.setVgap(8);
        panelBotones.setPadding(new Insets(10));
        panelBotones.getChildren().addAll(
                btnAgregar,
                btnOrdenarLegajo, btnOrdenarApellido, btnOrdenarSalario,
                btnFiltrarVendedores, btnMostrarTodos,
                btnActualizarCategorias, btnAumentarSalario,
                btnEliminar, btnGuardarCSV, btnGuardarBIN,
                btnGuardarJSON, btnExportarTXT, btnCargarCSV, btnCargarJSON
        );

        BorderPane root = new BorderPane();
        root.setCenter(tabla);
        root.setBottom(panelBotones);

        Scene scene = new Scene(root, 1000, 600);
        stage.setTitle("Gestion de Empleados - Loureiro Agustina");
        stage.setScene(scene);
        stage.show();
    }

    private void cargarDatosEjemplo() {
        inventario.agregar(new Vendedor(150000, "Ana", "Garcia", "12345678", LocalDate.of(1990, 5, 15), "Buenos Aires", LocalDate.of(2020, 3, 1)));
        inventario.agregar(new Operario(500, "Carlos", "Lopez", "23456789", LocalDate.of(1985, 8, 20), "Cordoba", LocalDate.of(2015, 6, 1)));
        inventario.agregar(new Administrativo(120000, Sector.RRHH, "Maria", "Fernandez", "34567890", LocalDate.of(1992, 2, 10), "Rosario", LocalDate.of(2018, 1, 15)));
        inventario.agregar(new Directivo(300000, true, true, 10, "Roberto", "Martinez", "45678901", LocalDate.of(1975, 11, 30), "Buenos Aires", LocalDate.of(2010, 4, 1)));
        inventario.agregar(new JefeDepartamento("Ventas", 5, 250000, false, true, 7, "Laura", "Perez", "56789012", LocalDate.of(1980, 7, 25), "Mendoza", LocalDate.of(2012, 9, 1)));
        actualizarTabla();
    }

    private void actualizarTabla() {
        listaObservable.setAll(inventario.obtenerTodos());
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void mostrarDialogoAgregar() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Agregar Empleado");
        dialog.setHeaderText("Complete los datos del nuevo empleado");

        ButtonType btnAgregarType = new ButtonType("Agregar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnAgregarType, ButtonType.CANCEL);

        // --- Campos comunes a todos los tipos ---
        ComboBox<String> comboTipo = new ComboBox<>();
        comboTipo.getItems().addAll("VENDEDOR", "OPERARIO", "ADMINISTRATIVO", "DIRECTIVO", "JEFE_DEPTO");
        comboTipo.setValue("VENDEDOR");

        TextField txtNombre = new TextField();
        TextField txtApellido = new TextField();
        TextField txtDni = new TextField();
        TextField txtLocalidad = new TextField();
        DatePicker dpFechaNacimiento = new DatePicker();
        DatePicker dpFechaIngreso = new DatePicker(LocalDate.now());

        GridPane gridComunes = new GridPane();
        gridComunes.setHgap(10);
        gridComunes.setVgap(8);
        gridComunes.addRow(0, new Label("Tipo:"), comboTipo);
        gridComunes.addRow(1, new Label("Nombre:"), txtNombre);
        gridComunes.addRow(2, new Label("Apellido:"), txtApellido);
        gridComunes.addRow(3, new Label("DNI:"), txtDni);
        gridComunes.addRow(4, new Label("Localidad:"), txtLocalidad);
        gridComunes.addRow(5, new Label("Fecha Nacimiento:"), dpFechaNacimiento);
        gridComunes.addRow(6, new Label("Fecha Ingreso:"), dpFechaIngreso);

        // --- Campos especificos, que cambian segun el tipo elegido ---
        GridPane gridEspecifico = new GridPane();
        gridEspecifico.setHgap(10);
        gridEspecifico.setVgap(8);

        TextField txtSueldoBase = new TextField();
        TextField txtValorHora = new TextField();
        ComboBox<Sector> comboSector = new ComboBox<>(FXCollections.observableArrayList(Sector.values()));
        CheckBox chkAutoEmpresa = new CheckBox("Auto empresa");
        CheckBox chkTelEmpresa = new CheckBox("Tel empresa");
        TextField txtDiasAdicionales = new TextField();
        TextField txtDepartamento = new TextField();
        TextField txtCantSubordinados = new TextField();

        Runnable actualizarCamposEspecificos = () -> {
            gridEspecifico.getChildren().clear();
            switch (comboTipo.getValue()) {
                case "VENDEDOR":
                    gridEspecifico.addRow(0, new Label("Sueldo Base:"), txtSueldoBase);
                    break;
                case "OPERARIO":
                    gridEspecifico.addRow(0, new Label("Valor Hora:"), txtValorHora);
                    break;
                case "ADMINISTRATIVO":
                    gridEspecifico.addRow(0, new Label("Sueldo Base:"), txtSueldoBase);
                    gridEspecifico.addRow(1, new Label("Sector:"), comboSector);
                    break;
                case "DIRECTIVO":
                    gridEspecifico.addRow(0, new Label("Sueldo Base:"), txtSueldoBase);
                    gridEspecifico.addRow(1, chkAutoEmpresa, chkTelEmpresa);
                    gridEspecifico.addRow(2, new Label("Dias Adicionales:"), txtDiasAdicionales);
                    break;
                case "JEFE_DEPTO":
                    gridEspecifico.addRow(0, new Label("Sueldo Base:"), txtSueldoBase);
                    gridEspecifico.addRow(1, chkAutoEmpresa, chkTelEmpresa);
                    gridEspecifico.addRow(2, new Label("Dias Adicionales:"), txtDiasAdicionales);
                    gridEspecifico.addRow(3, new Label("Departamento:"), txtDepartamento);
                    gridEspecifico.addRow(4, new Label("Subordinados:"), txtCantSubordinados);
                    break;
            }
        };
        actualizarCamposEspecificos.run();
        comboTipo.setOnAction(e -> {
            actualizarCamposEspecificos.run();
            dialog.getDialogPane().getScene().getWindow().sizeToScene();
        });

        VBox contenido = new VBox(15, gridComunes, new Separator(), gridEspecifico);
        contenido.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(contenido);

        Optional<ButtonType> resultado = dialog.showAndWait();
        if (resultado.isPresent() && resultado.get() == btnAgregarType) {
            try {
                String tipo = comboTipo.getValue();
                String nombre = txtNombre.getText();
                String apellido = txtApellido.getText();
                String dni = txtDni.getText();
                String localidad = txtLocalidad.getText().isBlank() ? "Sin especificar" : txtLocalidad.getText();
                LocalDate fechaNacimiento = dpFechaNacimiento.getValue();
                LocalDate fechaIngreso = dpFechaIngreso.getValue();

                if (nombre.isBlank() || apellido.isBlank() || dni.isBlank() || fechaNacimiento == null || fechaIngreso == null) {
                    mostrarAlerta("Error", "Complete todos los campos comunes");
                    return;
                }

                Empleado nuevo;
                switch (tipo) {
                    case "VENDEDOR":
                        nuevo = new Vendedor(Double.parseDouble(txtSueldoBase.getText()), nombre, apellido, dni, fechaNacimiento, localidad, fechaIngreso);
                        break;
                    case "OPERARIO":
                        nuevo = new Operario(Double.parseDouble(txtValorHora.getText()), nombre, apellido, dni, fechaNacimiento, localidad, fechaIngreso);
                        break;
                    case "ADMINISTRATIVO":
                        nuevo = new Administrativo(Double.parseDouble(txtSueldoBase.getText()), comboSector.getValue(), nombre, apellido, dni, fechaNacimiento, localidad, fechaIngreso);
                        break;
                    case "DIRECTIVO":
                        nuevo = new Directivo(Double.parseDouble(txtSueldoBase.getText()), chkAutoEmpresa.isSelected(), chkTelEmpresa.isSelected(),
                                Integer.parseInt(txtDiasAdicionales.getText().isBlank() ? "0" : txtDiasAdicionales.getText()),
                                nombre, apellido, dni, fechaNacimiento, localidad, fechaIngreso);
                        break;
                    case "JEFE_DEPTO":
                        nuevo = new JefeDepartamento(txtDepartamento.getText(),
                                Integer.parseInt(txtCantSubordinados.getText().isBlank() ? "0" : txtCantSubordinados.getText()),
                                Double.parseDouble(txtSueldoBase.getText()), chkAutoEmpresa.isSelected(), chkTelEmpresa.isSelected(),
                                Integer.parseInt(txtDiasAdicionales.getText().isBlank() ? "0" : txtDiasAdicionales.getText()),
                                nombre, apellido, dni, fechaNacimiento, localidad, fechaIngreso);
                        break;
                    default:
                        throw new IllegalStateException("Tipo no reconocido");
                }

                inventario.agregar(nuevo);
                actualizarTabla();
                mostrarAlerta("Exito", "Empleado agregado correctamente");
            } catch (NumberFormatException ex) {
                mostrarAlerta("Error", "Verifique que los campos numericos tengan valores validos");
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
