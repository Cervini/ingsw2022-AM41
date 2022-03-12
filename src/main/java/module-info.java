module it.polimi.ingsw.am41 {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens it.polimi.ingsw.am41 to javafx.fxml;
    exports it.polimi.ingsw.am41;
}