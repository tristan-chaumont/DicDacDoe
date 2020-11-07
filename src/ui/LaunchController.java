package ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class LaunchController implements Initializable {

    @FXML
    private AnchorPane parent;

    @FXML
    private HBox titleBar;

    @FXML
    private Label minimizeButton;

    private double xOffSet = 0;
    private double yOffSet = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        makeStageDraggable();
    }

    private void makeStageDraggable() {
        titleBar.setOnMousePressed((event) -> {
            xOffSet = event.getSceneX();
            yOffSet = event.getSceneY();
        });
        titleBar.setOnMouseDragged((event) -> {
            Launch.stage.setX(event.getScreenX() - xOffSet);
            Launch.stage.setY(event.getScreenY() - yOffSet);
            //Launch.stage.setOpacity(0.8);
        });
        /*parent.setOnDragDone((event) -> {
            Launch.stage.setOpacity(1.0);
        });
        parent.setOnMouseReleased((event) -> {
            Launch.stage.setOpacity(1.0);
        });*/
    }

    @FXML
    private void minimizeStage(MouseEvent event) {
        Launch.stage.setIconified(true);
    }

    @FXML
    private void closeApp(MouseEvent event) {
        System.exit(0);
    }
}