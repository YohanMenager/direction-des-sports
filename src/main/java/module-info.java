module sio.leo.direction.des.sports {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires javafx.base;
    requires javafx.graphics;
    requires java.sql;

    opens sio.leo.direction.des.sports to javafx.fxml;
    exports sio.leo.direction.des.sports;
}
