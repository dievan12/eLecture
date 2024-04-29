module lecture {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;

    opens lecture.ui to javafx.fxml;
    exports lecture.ui;

}
