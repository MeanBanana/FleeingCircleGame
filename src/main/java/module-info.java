module circle.game.circlegame {
    requires javafx.controls;
    requires javafx.fxml;


    opens circle.game.circlegame to javafx.fxml;
    exports circle.game.circlegame;
}