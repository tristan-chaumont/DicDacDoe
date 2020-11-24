package ui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import tictactoe.StructureTicTacToe;
import tictactoe.TicTacToe_2D;
import tictactoe.TicTacToe_3D;
import utilities.ColorsUtilities;
import utilities.IconsUtilities;
import utilities.Utilities;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

public class LaunchController implements Initializable {

    //region ATTRIBUTS FXML

    @FXML
    private AnchorPane parent;

    @FXML
    private HBox titleBar;

    @FXML
    private HBox boardPane;

    @FXML
    private RadioButton radioButtonCross;

    @FXML
    private RadioButton radioButtonCircle;

    @FXML
    private RadioButton radioButton2D;

    @FXML
    private RadioButton radioButton3D;

    @FXML
    private ComboBox<String> comboBoxFiles;

    @FXML
    private CheckBox aiStarts;

    @FXML
    private TextFlow information;

    //endregion

    //region AUTRES ATTRIBUTS

    private StructureTicTacToe ticTacToe;

    private HashMap<String, File> files;

    private double xOffSet = 0;
    private double yOffSet = 0;

    private final ToggleGroup shapeGroup = new ToggleGroup();

    private final ToggleGroup dimensionGroup = new ToggleGroup();

    private int currentPlayer;

    private int currentDepth;

    //endregion

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        makeStageDraggable();
        currentDepth = 1;

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

        linkRadioButtons();
        initializeTicTacToe();
        createTictactoe2D();
        initializeInformation();
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

    private Node createSpacer() {
        final Region spacer = new Region();
        // Make it always grow or shrink according to the available space
        VBox.setVgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    private void makeGridPane(GridPane gridPane, int columns, int rows, int width, int height) {
        for (int i = 0; i < columns; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setMinWidth(width);
            colConst.setPrefWidth(width);
            colConst.setMaxWidth(width);
            gridPane.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < rows; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setMinHeight(height);
            rowConst.setPrefHeight(height);
            rowConst.setMaxHeight(height);
            gridPane.getRowConstraints().add(rowConst);
        }
    }

    private void createTictactoe() {
        if (dimensionGroup.getSelectedToggle().equals(radioButton2D)) {
            createTictactoe2D();
        } else {
            createTictactoe3D();
        }
    }

    /**
     * Crée l'interface du TicTacToe 2D, composée d'un GridPane et de StackPane qui vont accueillir les formes.
     * Affecte un clickListener sur chaque StackPane pour permettre au joueur d'effectuer son coup.
     */
    private void createTictactoe2D() {
        int rows = 4, columns = 4;

        GridPane board = new GridPane();
        board.setMinSize(400, 400);
        board.setPrefSize(400, 400);
        board.setMaxSize(400, 400);

        makeGridPane(board, columns, rows, 100, 100);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                StackPane stackPane = makeTicTacToeCell(i, j);

                char cell = ((TicTacToe_2D) ticTacToe).getCell(i + 1, j + 1);
                if (cell == 'X') {
                    stackPane.getChildren().add(IconsUtilities.makeGroupCross(0.08, 0.08));
                } else if (cell == 'O') {
                    stackPane.getChildren().add(IconsUtilities.makeGroupCircle(0.07, 0.07));
                }

                final int finalI = i;
                final int finalJ = j;

                stackPane.setOnMouseClicked(event -> {
                    Group icon;
                    if (event.getButton() == MouseButton.PRIMARY && stackPane.getChildren().size() == 0) {
                        // CROIX
                        if (currentPlayer == 0) {
                            icon = IconsUtilities.makeGroupCross(0.08, 0.08);
                        } else {
                            icon = IconsUtilities.makeGroupCircle(0.07, 0.07);
                        }
                        writeMoveInformation(finalI, finalJ);
                        currentPlayer = Math.abs(currentPlayer - 1);

                        stackPane.getChildren().add(icon);
                    }
                });
                board.add(stackPane, j, i);
            }
        }

        boardPane.getChildren().add(board);
    }

    /**
     * Crée l'interface du TicTacToe 3D, composée d'une VBox accueillant chaque étage et d'une HBox accueillant l'étage actuel.
     * Affecte un clickListener sur chaque étage pour changer l'affichage du morpion actuel.
     */
    private void createTictactoe3D() {
        int rows = 4, columns = 4;

        // Fenêtre de tous les étages du morpion.
        VBox stagesPane = new VBox();
        stagesPane.setAlignment(Pos.CENTER);
        stagesPane.setMinSize(310, 700);
        stagesPane.setPrefSize(310, 700);
        stagesPane.setMaxSize(310, 700);
        stagesPane.getChildren().add(createSpacer()); // Ajoute le premier spacer
        // Fenêtre du morpion actuel.
        HBox currentStagePane = new HBox();
        currentStagePane.setAlignment(Pos.CENTER);
        currentStagePane.setMinSize(600, 620);
        currentStagePane.setPrefSize(600, 620);
        currentStagePane.setMaxSize(600, 620);

        currentStagePane.getStyleClass().add("separation-border");

        GridPane[] stages = new GridPane[4];
        for (int i = 0; i < stages.length; i++) {
            stages[i] = new GridPane();
        }

        GridPane currentStage = new GridPane();
        currentStage.setMinSize(400, 400);
        currentStage.setPrefSize(400, 400);
        currentStage.setMaxSize(400, 400);

        for (GridPane stage : stages) {
            makeGridPane(stage, columns, rows, 30, 30);
            stage.setMinSize(120, 120);
            stage.setPrefSize(120, 120);
            stage.setMaxSize(120, 120);
        }

        makeGridPane(currentStage, columns, rows, 100, 100);

        // Fenêtre de tous les étages
        for (int d = 0; d < stages.length; d++) {
            for (int l = 0; l < rows; l++) {
                for (int c = 0; c < columns; c++) {
                    StackPane stackPane = makeTicTacToeCell(l, c);
                    stackPane.getStyleClass().add("tictactoe-cell-stage");

                    char cell = ((TicTacToe_3D) ticTacToe).getCell(l + 1, c + 1, (stages.length + 1) - (d + 1));
                    if (cell == 'X') {
                        stackPane.getChildren().add(IconsUtilities.makeGroupCross(0.024, 0.024));
                    } else if (cell == 'O') {
                        stackPane.getChildren().add(IconsUtilities.makeGroupCircle(0.021, 0.021));
                    }

                    stages[d].add(stackPane, c, l);
                }
            }
            stagesPane.getChildren().add(stages[d]);
            // Ajoute un spacer après l'étage
            stagesPane.getChildren().add(createSpacer());

        }

        // Fenêtre de l'étage actuel
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                StackPane stackPane = makeTicTacToeCell(i, j);

                char cell = ((TicTacToe_3D) ticTacToe).getCell(i + 1, j + 1, this.currentDepth);
                if (cell == 'X') {
                    stackPane.getChildren().add(IconsUtilities.makeGroupCross(0.08, 0.08));
                } else if (cell == 'O') {
                    stackPane.getChildren().add(IconsUtilities.makeGroupCircle(0.07, 0.07));
                }

                final int finalI = i;
                final int finalJ = j;

                stackPane.setOnMouseClicked(event -> {
                    Group icon;
                    if (event.getButton() == MouseButton.PRIMARY && stackPane.getChildren().size() == 0) {
                        // CROIX
                        if (currentPlayer == 0) {
                            icon = IconsUtilities.makeGroupCross(0.08, 0.08);
                        } else {
                            icon = IconsUtilities.makeGroupCircle(0.07, 0.07);
                        }
                        writeMoveInformation(finalI, finalJ);
                        currentPlayer = Math.abs(currentPlayer - 1);

                        stackPane.getChildren().add(icon);
                    }
                });
                currentStage.add(stackPane, j, i);
            }
        }

        currentStagePane.getChildren().add(currentStage);

        boardPane.getChildren().addAll(stagesPane, currentStagePane);
    }

    private StackPane makeTicTacToeCell(int i, int j) {
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
        return stackPane;
    }

    /**
     * Initialise la structure du TicTacToe en fonction de la dimension choisie.
     */
    public void initializeTicTacToe() {
        ArrayList<Integer> boardList;
        char[] boardArray;

        // Si le plateau est en 2D
        if (dimensionGroup.getSelectedToggle().equals(radioButton2D)) {
            ticTacToe = new TicTacToe_2D();
            boardList = Utilities.parseBoard(2, files.get(comboBoxFiles.getValue()).getPath());
            boardArray = new char[(int) Math.pow(4, 2)];
        } else {
            ticTacToe = new TicTacToe_3D();
            boardList = Utilities.parseBoard(3, files.get(comboBoxFiles.getValue()).getPath());
            boardArray = new char[(int) Math.pow(4, 3)];
        }

        for (int i = 0; i < boardList.size(); i++) {
            switch (boardList.get(i)) {
                case -1:
                    boardArray[i] = ' ';
                    break;
                case 0:
                    boardArray[i] = 'O';
                    break;
                case 1:
                    boardArray[i] = 'X';
                    break;
            }
        }

        ticTacToe.setCells(boardArray);
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

    /**
     * Récupère tous les plateaux créés par les utilisateurs en fonction de la dimension choisie.
     * @param dimension
     *      Dimension choisie : 2D ou 3D.
     */
    private void fetchBoards(String dimension) {
        files = new HashMap<>();
        if (!comboBoxFiles.getItems().isEmpty()) {
            comboBoxFiles.getItems().clear();
        }
        File directory = new File("files/" + dimension);
        Utilities.fetchFiles(directory, file -> {
            String fileName = String.join(" ", file.getName().substring(0, file.getName().length() - 4).split("_"));
            comboBoxFiles.getItems().add(fileName);
            files.put(fileName, file);
        });
        // Trie la liste des plateaux pour que le premier plateau proposé soit le plateau vide
        int index = IntStream.range(0, comboBoxFiles.getItems().size())
                .filter(i -> comboBoxFiles.getItems().get(i).equals(dimension + " Empty"))
                .findFirst().orElse(0);
        if (index > 0) {
            comboBoxFiles.getItems().add(0, comboBoxFiles.getItems().get(index));
            comboBoxFiles.getItems().remove(index + 1);
        }

        comboBoxFiles.getItems().add("Import custom board...");
        comboBoxFiles.getSelectionModel().selectFirst();
    }

    /**
     * Gère l'appuie sur le bouton "New game".
     * Vérifie quelle forme a été choisie par le joueur.
     * Initialise un nouveau plateau avec le fichier et la dimension choisis.
     * Vérifie si l'IA doit commencer.
     */
    @FXML
    private void handleNewGameClick(MouseEvent event) {
        currentPlayer = shapeGroup.getSelectedToggle().equals(radioButtonCross) ? 0 : 1;
        // Parse le plateau et crée le tableau de Cells.
        initializeTicTacToe();
        boardPane.getChildren().clear();
        createTictactoe();
        information.getChildren().clear();
        initializeInformation();
    }

    //endregion

    //region GESTION DE LA FENETRE DES INFORMATIONS

    /**
     * Crée la première ligne de la fenêtre des informations.
     * Informe l'utilisateur du joueur qui commence ainsi que de la forme qu'il utilise.
     */
    private void initializeInformation() {
        Text player = new Text("Player ");
        player.setFill(Color.web(ColorsUtilities.GREY));
        player.setFont(Font.font("System", FontWeight.BOLD, 13));
        Text playerStart = new Text("starts and plays ");
        playerStart.setFill(Color.web(ColorsUtilities.GREY));
        playerStart.setFont(Font.font(13));
        Text playerShape;
        if (currentPlayer == 0) {
            playerShape = new Text("Cross");
            playerShape.setFill(Color.web(ColorsUtilities.RED));
        } else {
            playerShape = new Text("Circle");
            playerShape.setFill(Color.web(ColorsUtilities.YELLOW));
        }
        playerShape.setFont(Font.font("System", FontWeight.BOLD, 13));
        information.getChildren().addAll(player, playerStart, playerShape);
    }

    /**
     * Écrit le mouvement du joueur dans la fenêtre des informations.
     * @param line
     *      Ligne sur laquelle le joueur a effectué son mouvement.
     * @param column
     *      Colonne sur laquelle le joueur a effectué son mouvement.
     */
    private void writeMoveInformation(int line, int column) {
        Text playerShape;
        // CROIX
        if (currentPlayer == 0) {
            playerShape = new Text(Utilities.LINE_SEPARATOR + "Cross");
            playerShape.setFill(Color.web(ColorsUtilities.RED));
            // CERCLE
        } else {
            playerShape = new Text(Utilities.LINE_SEPARATOR + "Circle");
            playerShape.setFill(Color.web(ColorsUtilities.YELLOW));
        }
        playerShape.setFont(Font.font("System", FontWeight.BOLD, 13));
        Text playerMoveStartLine = new Text(" played at line ");
        Text playerMoveLine = new Text(Integer.toString(line + 1));
        Text playerMoveComa = new Text(", column ");
        Text playerMoveColumn = new Text(Integer.toString(column + 1));
        playerMoveStartLine.setFill(Color.web(ColorsUtilities.GREY));
        playerMoveStartLine.setFont(Font.font(13));
        playerMoveLine.setFill(Color.web(ColorsUtilities.GREY));
        playerMoveLine.setFont(Font.font("System", FontWeight.BOLD, 13));
        playerMoveComa.setFill(Color.web(ColorsUtilities.GREY));
        playerMoveComa.setFont(Font.font(13));
        playerMoveColumn.setFill(Color.web(ColorsUtilities.GREY));
        playerMoveColumn.setFont(Font.font("System", FontWeight.BOLD, 13));
        information.getChildren().addAll(playerShape, playerMoveStartLine, playerMoveLine, playerMoveComa, playerMoveColumn);
    }

    //endregion
}