module com.example.commandlefinalproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.commandlefinalproject to javafx.fxml;
    exports com.example.commandlefinalproject;
}