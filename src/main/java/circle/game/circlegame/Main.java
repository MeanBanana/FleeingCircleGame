package circle.game.circlegame;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class Main extends Application {

    static Circle circle = new Circle();
    private final int circleRadius = 50;
    private boolean gameWin = false;
    private PauseTransition pauseTransition;
    private double mouseX = 0;
    private double mouseY = 0;
    private final double safeDistance = 100; // Minimum safe distance before circle flees

    @Override
    public void start(Stage primaryStage) {
        Pane pane = new Pane();
        pane.getChildren().add(circle);

        circle.setRadius(circleRadius);
        circle.setFill(Color.RED);

        Scene scene = new Scene(pane);

        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();

        double sceneWidth = scene.getWidth();
        double sceneHeight = scene.getHeight();

        circle.setCenterX(scene.getWidth() / 2);
        circle.setCenterY(scene.getHeight() / 2);

        Alert instrAlert = new Alert(Alert.AlertType.INFORMATION);
        instrAlert.setTitle("You Win");
        instrAlert.setHeaderText(null);
        instrAlert.setContentText("Click the circle to win");

        DialogPane instrDP = instrAlert.getDialogPane();
        instrDP.lookup(".content.label").setStyle("-fx-font-size: 14px;");

        instrAlert.showAndWait();

        // Track mouse position
        scene.setOnMouseMoved(e -> {
            mouseX = e.getX();
            mouseY = e.getY();

            // Move circle if mouse is too close
            double distToCircle = distance(mouseX, mouseY, circle.getCenterX(), circle.getCenterY());

            if (distToCircle < safeDistance && !gameWin) {
                if (pauseTransition == null) {
                    pauseTransition = new PauseTransition(Duration.millis(100));
                    pauseTransition.setOnFinished(event -> moveCircleToSafeLocation(sceneWidth, sceneHeight));
                }
                pauseTransition.playFromStart();
            }
        });

        circle.setOnMouseClicked(e -> CircleClick());
    }

    private void CircleClick() {
        gameWin = true;

        Alert alertWin = new Alert(Alert.AlertType.INFORMATION);
        alertWin.setTitle("You Win");
        alertWin.setHeaderText(null);
        alertWin.setContentText("You win\nCongratulations!!!");

        DialogPane dialogPane = alertWin.getDialogPane();
        dialogPane.lookup(".content.label").setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button exitButton = (Button) dialogPane.lookupButton(ButtonType.OK);
        exitButton.setText("Exit");

        alertWin.setOnCloseRequest(event -> System.exit(0));
        alertWin.setOnHidden(event -> System.exit(0));

        alertWin.showAndWait();
    }

    private void moveCircleToSafeLocation(double sceneWidth, double sceneHeight) {
        if (!gameWin) {
            Random random = new Random();
            double randX, randY;

            // When circle is to close to mouse, move
            do {
                randX = circleRadius + random.nextDouble() * (sceneWidth - 2 * circleRadius);
                randY = circleRadius + random.nextDouble() * (sceneHeight - 2 * circleRadius);
            } while (distance(mouseX, mouseY, randX, randY) < safeDistance * 1.5); // Move far away

            circle.setCenterX(randX);
            circle.setCenterY(randY);
        }
    }

    private double distance(double x1, double y1, double x2, double y2) {
        // Distance formula used to calculate the distance between circle and mouse
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public static void main(String[] args) {
        launch(args);
    }
}