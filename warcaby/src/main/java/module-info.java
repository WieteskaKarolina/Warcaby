module com.example.warcaby {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens com.example.warcaby to javafx.fxml;
    exports com.example.warcaby;
    exports com.example.gameforpio;
    opens com.example.gameforpio to javafx.fxml;
}