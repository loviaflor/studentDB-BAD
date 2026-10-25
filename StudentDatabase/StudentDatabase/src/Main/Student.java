package Main;

import javafx.beans.property.*;

public class Student {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty gender;
    private final StringProperty major;
    private final DoubleProperty ipk;

    // Constructor
    public Student(int id, String name, String gender, String major, double ipk) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.gender = new SimpleStringProperty(gender);
        this.major = new SimpleStringProperty(major);
        this.ipk = new SimpleDoubleProperty(ipk);
    }

    // Getters
    public int getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public String getGender() {
        return gender.get();
    }

    public String getMajor() {
        return major.get();
    }

    public double getIpk() {
        return ipk.get();
    }

    // Setters
    public void setId(int id) {
        this.id.set(id);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public void setMajor(String major) {
        this.major.set(major);
    }

    public void setIpk(double ipk) {
        this.ipk.set(ipk);
    }

    // Properties for TableView binding
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty genderProperty() {
        return gender;
    }

    public StringProperty majorProperty() {
        return major;
    }

    public DoubleProperty ipkProperty() {
        return ipk;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id.get() +
                ", name='" + name.get() + '\'' +
                ", gender='" + gender.get() + '\'' +
                ", major='" + major.get() + '\'' +
                ", ipk=" + ipk.get() +
                '}';
    }
}
