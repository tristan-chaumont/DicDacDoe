package ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class LaunchController implements Initializable {

    @FXML
    private AnchorPane parent;

    @FXML
    private HBox titleBar;

    @FXML
    private Label minimizeButton;

    @FXML
    private GridPane tictactoe2D;

    @FXML
    private RadioButton radioButtonCross;

    @FXML
    private RadioButton radioButtonCircle;

    @FXML
    private Label timer;

    private double xOffSet = 0;
    private double yOffSet = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        makeStageDraggable();
        createTictactoe2D();
        linkRadioButtons();
    }

    private void makeStageDraggable() {
        titleBar.setOnMousePressed((event) -> {
            xOffSet = event.getSceneX();
            yOffSet = event.getSceneY();
        });
        titleBar.setOnMouseDragged((event) -> {
            Launch.stage.setX(event.getScreenX() - xOffSet);
            Launch.stage.setY(event.getScreenY() - yOffSet);
        });
    }

    @FXML
    private void minimizeStage(MouseEvent event) {
        Launch.stage.setIconified(true);
    }

    @FXML
    private void closeApp(MouseEvent event) {
        System.exit(0);
    }

    private void createTictactoe2D() {
        int rows = 4, columns = 4;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                StackPane stackPane = new StackPane();
                stackPane.getStyleClass().add("tictactoe-cell");

                if (j == 0 && i != 0) {
                    stackPane.getStyleClass().add("first-column");
                }
                if (i == 0 && j != 0) {
                    stackPane.getStyleClass().add("first-row");
                }
                if (j == 0 && i == 0) {
                    stackPane.getStyleClass().add("first-cell");
                }

                stackPane.setOnMouseClicked(event -> {
                    SVGPath icon = new SVGPath();
                    // CROSS
                    if (event.getButton() == MouseButton.PRIMARY) {
                        icon.setContent("m386.667 45.564-45.564-45.564-147.77 147.769-147.769-147.769-45.564 45.564 147.769 147.769-147.769 147.77 45.564 45.564 147.769-147.769 147.769 147.769 45.564-45.564-147.768-147.77z");
                        icon.setFill(Color.web("#e63946"));
                        icon.setScaleX(0.08);
                        icon.setScaleY(0.08);
                    // CIRCLE
                    } else if(event.getButton() == MouseButton.SECONDARY) {
                        icon.setContent("m257.778 515.556c-142.137 0-257.778-115.642-257.778-257.778s115.641-257.778 257.778-257.778 257.778 115.641 257.778 257.778-115.642 257.778-257.778 257.778zm0-451.112c-106.61 0-193.333 86.723-193.333 193.333s86.723 193.333 193.333 193.333 193.333-86.723 193.333-193.333-86.723-193.333-193.333-193.333z");
                        icon.setFill(Color.web("#ffe3a7"));
                        icon.setScaleX(0.07);
                        icon.setScaleY(0.07);
                    }
                    Group group = new Group(icon);
                    group.setTranslateX(2);
                    group.setTranslateY(2);
                    stackPane.getChildren().add(group);
                });
                tictactoe2D.add(stackPane, j, i);
            }
        }
    }

    private void linkRadioButtons() {
        final ToggleGroup group = new ToggleGroup();
        radioButtonCross.setToggleGroup(group);
        radioButtonCross.setSelected(true);

        radioButtonCircle.setToggleGroup(group);

        System.out.println(group.getSelectedToggle());
    }

    private void resizeSvg(SVGPath svg, double width, double height) {
        double originalWidth = svg.prefWidth(-1);
        double originalHeight = svg.prefHeight(originalWidth);

        double scaleX = width / originalWidth;
        double scaleY = height / originalHeight;

        svg.setScaleX(scaleX);
        svg.setScaleY(scaleY);
    }
}