package ui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import utilities.Utilities;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

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
    private RadioButton radioButton2D;

    @FXML
    private RadioButton radioButton3D;

    @FXML
    private ComboBox<String> boards;

    @FXML
    private CheckBox aiStarts;

    @FXML
    private TextFlow information;

    //endregion

    //region AUTRES ATTRIBUTS

    private double xOffSet = 0;
    private double yOffSet = 0;

    private final ToggleGroup shapeGroup = new ToggleGroup();

    private final ToggleGroup dimensionGroup = new ToggleGroup();

    private int currentPlayer;

    //endregion

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        makeStageDraggable();
        linkRadioButtons();
        createTictactoe2D();
        initializeInformation();

        dimensionGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle t1) {
                if (dimensionGroup.getSelectedToggle().equals(radioButton2D)) {
                    fetchBoards("2D");
                } else {
                    fetchBoards("3D");
                }
            }
        });

        fetchBoards("2D");
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
                    if (event.getButton() == MouseButton.PRIMARY && stackPane.getChildren().size() == 0) {
                        Text playerShape;
                        // CROIX
                        if (currentPlayer == 0) {
                            icon.setContent("m386.667 45.564-45.564-45.564-147.77 147.769-147.769-147.769-45.564 45.564 147.769 147.769-147.769 147.77 45.564 45.564 147.769-147.769 147.769 147.769 45.564-45.564-147.768-147.77z");
                            icon.setFill(Color.web("#e63946"));
                            icon.setScaleX(0.08);
                            icon.setScaleY(0.08);
                            playerShape = new Text(Utilities.LINE_SEPARATOR + "Cross");
                            playerShape.setFill(Color.web("#e63946"));
                        // CERCLE
                        } else {
                            icon.setContent("m257.778 515.556c-142.137 0-257.778-115.642-257.778-257.778s115.641-257.778 257.778-257.778 257.778 115.641 257.778 257.778-115.642 257.778-257.778 257.778zm0-451.112c-106.61 0-193.333 86.723-193.333 193.333s86.723 193.333 193.333 193.333 193.333-86.723 193.333-193.333-86.723-193.333-193.333-193.333z");
                            icon.setFill(Color.web("#ffe3a7"));
                            icon.setScaleX(0.07);
                            icon.setScaleY(0.07);
                            playerShape = new Text(Utilities.LINE_SEPARATOR + "Circle");
                            playerShape.setFill(Color.web("#ffe3a7"));
                        }
                        playerShape.setFont(Font.font("System", FontWeight.BOLD, 13));
                        Text playerMoveStartLine = new Text(" played at line ");
                        Text playerMoveLine = new Text(Integer.toString(finalI + 1));
                        Text playerMoveComa = new Text(", column ");
                        Text playerMoveColumn = new Text(Integer.toString(finalJ + 1));
                        playerMoveStartLine.setFill(Color.web("#abb2bf"));
                        playerMoveStartLine.setFont(Font.font(13));
                        playerMoveLine.setFill(Color.web("#abb2bf"));
                        playerMoveLine.setFont(Font.font("System", FontWeight.BOLD, 13));
                        playerMoveComa.setFill(Color.web("#abb2bf"));
                        playerMoveComa.setFont(Font.font(13));
                        playerMoveColumn.setFill(Color.web("#abb2bf"));
                        playerMoveColumn.setFont(Font.font("System", FontWeight.BOLD, 13));
                        information.getChildren().addAll(playerShape, playerMoveStartLine, playerMoveLine, playerMoveComa, playerMoveColumn);
                        currentPlayer = Math.abs(currentPlayer - 1);
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

    //region GESTION DE LA FENETRE DES PARAMETRES

    private void linkRadioButtons() {
        radioButtonCross.setToggleGroup(shapeGroup);
        radioButtonCross.setSelected(true);
        radioButtonCircle.setToggleGroup(shapeGroup);

        radioButton2D.setToggleGroup(dimensionGroup);
        radioButton2D.setSelected(true);
        radioButton3D.setToggleGroup(dimensionGroup);
    }

    private void fetchBoards(String dimension) {
        if (!boards.getItems().isEmpty()) {
            boards.getItems().clear();
        }
        File file = new File("files/" + dimension);
        Utilities.fetchFiles(file, f -> boards.getItems().add(String.join(" ", f.getName().substring(0, f.getName().length() - 4).split("_"))));
        // Trie la liste des plateaux pour que le premier plateau proposé soit le plateau vide
        int index = IntStream.range(0, boards.getItems().size())
                .filter(i -> boards.getItems().get(i).equals(dimension + " Empty"))
                .findFirst().orElse(0);
        if (index > 0) {
            boards.getItems().add(0, boards.getItems().get(index));
            boards.getItems().remove(index + 1);
        }

        boards.getItems().add("Import custom board...");
        boards.getSelectionModel().selectFirst();
    }

    /**
     * Gère l'appuie sur le bouton "New game".
     * Vérifie quelle forme a été choisie par le joueur.
     */
    @FXML
    private void handleNewGameClick(MouseEvent event) {
        currentPlayer = shapeGroup.getSelectedToggle().equals(radioButtonCross) ? 0 : 1;
        tictactoe2D.getChildren().clear();
        createTictactoe2D();
        information.getChildren().clear();
        initializeInformation();
    }

    //endregion

    //region GESTION DE LA FENETRE DES INFORMATIONS

    private void initializeInformation() {
        Text player = new Text("Player ");
        player.setFill(Color.web("#abb2bf"));
        player.setFont(Font.font("System", FontWeight.BOLD, 13));
        Text playerStart = new Text("starts and plays ");
        playerStart.setFill(Color.web("#abb2bf"));
        playerStart.setFont(Font.font(13));
        Text playerShape;
        if (currentPlayer == 0) {
            playerShape = new Text("Cross");
            playerShape.setFill(Color.web("#e63946"));
        } else {
            playerShape = new Text("Circle");
            playerShape.setFill(Color.web("#ffe3a7"));
        }
        playerShape.setFont(Font.font("System", FontWeight.BOLD, 13));
        information.getChildren().addAll(player, playerStart, playerShape);
    }

    //endregion
}