module de.tobiasreich.kaiser {
    requires javafx.controls;
    requires javafx.fxml;
        requires javafx.web;
                requires kotlin.stdlib;
    
        requires org.controlsfx.controls;
            requires com.dlsc.formsfx;
            requires validatorfx;
            requires org.kordamp.ikonli.javafx;
            requires org.kordamp.bootstrapfx.core;
            requires eu.hansolo.tilesfx;
    
    opens de.tobiasreich.kaiser to javafx.fxml;
    exports de.tobiasreich.kaiser;
}