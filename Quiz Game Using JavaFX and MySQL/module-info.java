module QuizGame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.quiz to javafx.fxml;
    exports com.quiz;
}
