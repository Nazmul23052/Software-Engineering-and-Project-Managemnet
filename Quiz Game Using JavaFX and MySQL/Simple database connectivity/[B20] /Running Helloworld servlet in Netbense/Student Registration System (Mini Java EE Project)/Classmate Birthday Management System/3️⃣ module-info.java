module BirthdayApp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.birthday to javafx.fxml;
    exports com.birthday;
}
