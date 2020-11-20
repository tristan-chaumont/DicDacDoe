package ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import java.net.URL;
import java.util.ResourceBundle;

public class LaunchController implements Initializable {

    //region ATTRIBUTS FXML

    @FXML
    private AnchorPane parent;

    @FXML
    private HBox titleBar;

    @FXML
    private GridPane tictactoe2D;

    @FXML
    private RadioButton radioButtonCross;

    @FXML
    private RadioButton radioButtonCircle;

    @FXML
    private TextArea information;

    //endregion

    //region AUTRES ATTRIBUTS

    private double xOffSet = 0;
    private double yOffSet = 0;

    private final ToggleGroup radioButtonsGroup = new ToggleGroup();

    private int currentPlayer;

    //endregion

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        makeStageDraggable();
        linkRadioButtons();
        createTictactoe2D();
        information.setWrapText(true);
        information.setText("Player can start");
    }

    //region GESTION DE LA TITLE BAR CUSTOM

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

    //endregion

    //region GESTION DU TICTACTOE

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

                final int finalI = i;
                final int finalJ = j;

                stackPane.setOnMouseClicked(event -> {
                    SVGPath icon = new SVGPath();
                    // CROSS
                    if (event.getButton() == MouseButton.PRIMARY) {
                        if (currentPlayer == 0) {
                            icon.setContent("m386.667 45.564-45.564-45.564-147.77 147.769-147.769-147.769-45.564 45.564 147.769 147.769-147.769 147.77 45.564 45.564 147.769-147.769 147.769 147.769 45.564-45.564-147.768-147.77z");
                            icon.setFill(Color.web("#e63946"));
                            icon.setScaleX(0.08);
                            icon.setScaleY(0.08);
                        } else {
                            icon.setContent("m257.778 515.556c-142.137 0-257.778-115.642-257.778-257.778s115.641-257.778 257.778-257.778 257.778 115.641 257.778 257.778-115.642 257.778-257.778 257.778zm0-451.112c-106.61 0-193.333 86.723-193.333 193.333s86.723 193.333 193.333 193.333 193.333-86.723 193.333-193.333-86.723-193.333-193.333-193.333z");
                            icon.setFill(Color.web("#ffe3a7"));
                            icon.setScaleX(0.07);
                            icon.setScaleY(0.07);
                        }
                        currentPlayer = Math.abs(currentPlayer - 1);
                        information.setText(String.format("%s\n%s played column %d, line %d", information.getText(), currentPlayer == 1 ? "Cross" : "Circle", finalJ + 1, finalI + 1));
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

    //endregion

    //region FONCTIONS QUI GERENT LA FENETRE DES PARAMETRES

    private void linkRadioButtons() {
        radioButtonCross.setToggleGroup(radioButtonsGroup);
        radioButtonCross.setSelected(true);

        radioButtonCircle.setToggleGroup(radioButtonsGroup);

        System.out.println(radioButtonsGroup.getSelectedToggle());
    }

    /**
     * Gère l'appuie sur le bouton "New game".
     * Vérifie quelle forme a été choisie par le joueur.
     */
    @FXML
    private void handleNewGameClick(MouseEvent event) {
        currentPlayer = radioButtonsGroup.getSelectedToggle().equals(radioButtonCross) ? 0 : 1;
        tictactoe2D.getChildren().clear();
        createTictactoe2D();
    }

    //endregion

    //region FONCTIONS QUI GERENT LA FENETRE DES INFORMATIONS

    //TODO

    //endregion
}