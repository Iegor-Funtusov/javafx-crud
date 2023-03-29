module ua.com.alevel {
    requires javafx.controls;
    requires javafx.fxml;
    requires rxjava2.extensions;
    requires io.reactivex.rxjava2;
    requires org.reactivestreams;
    requires java.sql;

    opens ua.com.alevel to javafx.fxml;
    exports ua.com.alevel;
    exports ua.com.alevel.controller;
    exports ua.com.alevel.persistence.entity;
    exports ua.com.alevel.persistence.type;
    exports ua.com.alevel.dto;

    opens ua.com.alevel.controller to javafx.fxml;
}
