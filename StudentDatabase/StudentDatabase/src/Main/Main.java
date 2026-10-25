package Main;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Main extends Application {

    private TableView<Student> tableView;
    private ObservableList<Student> studentList;
    private int nextId = 1; // Auto-increment ID

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Mahasiswa");

        // TableView
        tableView = new TableView<>();
        studentList = FXCollections.observableArrayList();
        loadFromDatabase(); // Load initial data from the database

        // Table Columns
        TableColumn<Student, Integer> columnId = new TableColumn<>("ID");
        columnId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());

        TableColumn<Student, String> columnName = new TableColumn<>("Nama");
        columnName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        TableColumn<Student, String> columnGender = new TableColumn<>("Gender");
        columnGender.setCellValueFactory(cellData -> cellData.getValue().genderProperty());

        TableColumn<Student, String> columnMajor = new TableColumn<>("Jurusan");
        columnMajor.setCellValueFactory(cellData -> cellData.getValue().majorProperty());

        TableColumn<Student, Double> columnIpk = new TableColumn<>("IPK");
        columnIpk.setCellValueFactory(cellData -> cellData.getValue().ipkProperty().asObject());

        tableView.getColumns().addAll(columnId, columnName, columnGender, columnMajor, columnIpk);
        tableView.setItems(studentList);

        // Form fields
        TextField nameField = new TextField();
        nameField.setPromptText("Nama");

        ToggleGroup genderGroup = new ToggleGroup();
        RadioButton genderMale = new RadioButton("Pria");
        genderMale.setToggleGroup(genderGroup);
        RadioButton genderFemale = new RadioButton("Perempuan");
        genderFemale.setToggleGroup(genderGroup);

        ComboBox<String> majorBox = new ComboBox<>();
        majorBox.setItems(FXCollections.observableArrayList("SI", "CS", "DB"));
        majorBox.setPromptText("Jurusan");

        Spinner<Double> ipkSpinner = new Spinner<>(0.0, 4.0, 3.0, 0.1);
        ipkSpinner.setEditable(true);

        // Buttons
        Button addButton = new Button("Add");
        Button clearButton = new Button("Clear");

        // Add button action
        addButton.setOnAction(e -> {
            String name = nameField.getText();
            String gender = genderMale.isSelected() ? "Pria" : genderFemale.isSelected() ? "Perempuan" : null;
            String major = majorBox.getValue();
            Double ipk = ipkSpinner.getValue();

            if (name.isEmpty() || gender == null || major == null) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill all the fields!");
                return;
            }

            // Auto-increment ID logic
            Student newStudent = new Student(nextId++, name, gender, major, ipk);
            studentList.add(newStudent);
            saveToDatabase(newStudent);

            // Clear fields after adding
            nameField.clear();
            genderGroup.selectToggle(null);
            majorBox.getSelectionModel().clearSelection();
            ipkSpinner.getValueFactory().setValue(3.0);
        });

        // Clear button action
        clearButton.setOnAction(e -> {
            nameField.clear();
            genderGroup.selectToggle(null);
            majorBox.getSelectionModel().clearSelection();
            ipkSpinner.getValueFactory().setValue(3.0);
        });

        // Layout
        GridPane formLayout = new GridPane();
        formLayout.setPadding(new Insets(10));
        formLayout.setHgap(10);
        formLayout.setVgap(10);

        formLayout.add(new Label("Nama:"), 0, 0);
        formLayout.add(nameField, 1, 0);
        formLayout.add(new Label("Gender:"), 0, 1);
        formLayout.add(genderMale, 1, 1);
        formLayout.add(genderFemale, 2, 1);
        formLayout.add(new Label("Jurusan:"), 0, 2);
        formLayout.add(majorBox, 1, 2);
        formLayout.add(new Label("IPK:"), 0, 3);
        formLayout.add(ipkSpinner, 1, 3);
        formLayout.add(addButton, 0, 4);
        formLayout.add(clearButton, 1, 4);

        VBox root = new VBox(10, formLayout, tableView);
        root.setPadding(new Insets(10));

        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    // Load data from the database
    private void loadFromDatabase() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM student";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String gender = rs.getString("gender");
                String major = rs.getString("major");
                double ipk = rs.getDouble("ipk");

                studentList.add(new Student(id, name, gender, major, ipk));

                // Update nextId to ensure no duplicates
                nextId = Math.max(nextId, id + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Save a new student to the database
    private void saveToDatabase(Student student) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO student (id, nama, gender, jurusan, ipk) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, student.getId());
            stmt.setString(2, student.getName());
            stmt.setString(3, student.getGender());
            stmt.setString(4, student.getMajor());
            stmt.setDouble(5, student.getIpk());
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Show alert dialog
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
