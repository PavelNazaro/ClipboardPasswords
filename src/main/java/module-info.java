module com.example.clipboardpasswords {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.datatransfer;
    requires java.desktop;
    requires org.json;
    requires json.simple;


    opens com.example.clipboardpasswords to javafx.fxml;
    exports com.example.clipboardpasswords;
}