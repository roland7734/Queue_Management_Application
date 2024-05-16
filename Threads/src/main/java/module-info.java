module com.threads.threads {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens com.threads.threads to javafx.fxml;
   // exports com.threads.threads;
    exports com.threads.threads.gui;
    opens com.threads.threads.gui to javafx.fxml;
}