package ui;

import alphabeta.Tree;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tictactoe.StructureTicTacToe;
import tictactoe.TicTacToe_2D;
import tictactoe.TicTacToe_3D;
import utilities.ColorsUtilities;
import utilities.IconsUtilities;
import utilities.TicTacToeUtilities;
import utilities.Utilities;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
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

    private int currentDepth;

    private GridPane[] stages;

    private GridPane currentStage;

    private boolean[] stagesSelected;

    private int rows = 4, columns = 4;

    private boolean gameOver, draw;

    private boolean aiPlays;

    //endregion

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        makeStageDraggable();

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
        handleComboBoxChange();
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

    private void makeGridPane(GridPane gridPane, int width, int height) {
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
        currentStage = new GridPane();
        currentStage.setMinSize(400, 400);
        currentStage.setPrefSize(400, 400);
        currentStage.setMaxSize(400, 400);

        makeGridPane(currentStage, 100, 100);

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
                    if (event.getButton() == MouseButton.PRIMARY && stackPane.getChildren().size() == 0 && !gameOver) {
                        // CROIX
                        if (!aiStarts.isSelected() && !aiPlays) {
                            icon = IconsUtilities.makeGroupCross(0.08, 0.08);
                            ((TicTacToe_2D) ticTacToe).setCell('X', finalI + 1, finalJ + 1);
                        } else {
                            icon = IconsUtilities.makeGroupCircle(0.07, 0.07);
                            ((TicTacToe_2D) ticTacToe).setCell('O', finalI + 1, finalJ + 1);
                        }
                        writeMoveInformation(finalI, finalJ);
                        stackPane.getChildren().add(icon);

                        // On vérifie si c'est un coup gagnant, auquel cas on stoppe la partie
                        handleWinningState(finalI, finalJ);

                        // On laisse l'IA jouer
                        makeAIPlays();
                    }
                });
                currentStage.add(stackPane, j, i);
            }
        }

        boardPane.getChildren().add(currentStage);
    }

    /**
     * Crée l'interface du TicTacToe 3D, composée d'une VBox accueillant chaque étage et d'une HBox accueillant l'étage actuel.
     * Affecte un clickListener sur chaque étage pour changer l'affichage du morpion actuel.
     */
    private void createTictactoe3D() {
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

        stages = new GridPane[4];
        for (int i = 0; i < stages.length; i++) {
            stages[i] = new GridPane();
        }

        currentStage = new GridPane();
        currentStage.setMinSize(400, 400);
        currentStage.setPrefSize(400, 400);
        currentStage.setMaxSize(400, 400);

        for (GridPane stage : stages) {
            makeGridPane(stage, 30, 30);
            stage.setMinSize(120, 120);
            stage.setPrefSize(120, 120);
            stage.setMaxSize(120, 120);
        }

        makeGridPane(currentStage, 100, 100);

        // Fenêtre de tous les étages
        for (int d = 0; d < stages.length; d++) {
            stages[d].getStyleClass().add("tictactoe-stage-" + d);
            for (int l = 0; l < rows; l++) {
                for (int c = 0; c < columns; c++) {
                    StackPane stackPane = makeTicTacToeCell(l, c);
                    stackPane.getStyleClass().add("tictactoe-cell-stage");
                    stackPane.getStyleClass().add("tictactoe-stage-cells-" + d);

                    char cell = ((TicTacToe_3D) ticTacToe).getCell(l + 1, c + 1, d + 1);
                    if (cell == 'X') {
                        stackPane.getChildren().add(IconsUtilities.makeGroupCross(0.024, 0.024));
                    } else if (cell == 'O') {
                        stackPane.getChildren().add(IconsUtilities.makeGroupCircle(0.021, 0.021));
                    }

                    stages[d].add(stackPane, c, l);
                }
            }
            final int depth = d;
            // On change la couleur des bordures du morpion sélectionné actuellement
            stages[d].setOnMouseClicked(event -> {
                if (!stagesSelected[depth]) {
                    stages[depth].getStyleClass().add("tictactoe-stage-selected-" + depth);
                    for (int i = 0; i < stagesSelected.length; i++) {
                        if (stagesSelected[i]) {
                            stages[i].getStyleClass().remove("tictactoe-stage-selected-" + i);
                        }
                        stagesSelected[i] = false;
                    }
                    stagesSelected[depth] = true;
                    currentDepth = depth;
                    displayCurrentStage();
                }
            });
            stagesPane.getChildren().add(stages[d]);
            // Ajoute un spacer après l'étage
            stagesPane.getChildren().add(createSpacer());
        }
        // Le premier étage est sélectionné
        stages[0].getStyleClass().add("tictactoe-stage-selected-0");

        // Fenêtre de l'étage actuelle
        addCellOnRightStage();

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
        gameOver = false;
        draw = false;
        currentDepth = 0;
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
            stagesSelected = new boolean[]{true, false, false, false};
        }
        /*ticTacToe.getEmptyCell();
        String stringBoard = String.valueOf(ticTacToe.getCells());
        int nbCross = (int) stringBoard.chars().mapToObj(c -> (char) c).collect(Collectors.toList()).stream().filter(v -> v == 'X').count();
        int nbCircles = (int) stringBoard.chars().mapToObj(c -> (char) c).collect(Collectors.toList()).stream().filter(v -> v == 'O').count();

        if (Math.abs(nbCross - nbCircles) > 1) {
            //TODO lever une erreur
        }*/

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

    private void addCellOnLeftStage(Group icon, int row, int column) {
        addCellOnLeftStageHelper(icon, row, column, currentDepth);
    }

    private void addCellOnLeftStage(Group icon, int row, int column, int depth) {
        addCellOnLeftStageHelper(icon, row, column, depth);
    }

    private void addCellOnLeftStageHelper(Group icon, int row, int column, int depth) {
        ((StackPane) stages[depth].getChildren().get(column + (row * 4))).getChildren().add(icon);
        if ((aiStarts.isSelected() && aiPlays) || (!aiStarts.isSelected() && !aiPlays)) {
            ticTacToe.setCell('X', row + 1, column + 1, depth + 1);
        } else {
            ticTacToe.setCell('O', row + 1, column + 1, depth + 1);
        }
    }

    private void addCellOnRightStage() {
        // Fenêtre de l'étage actuel
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                StackPane stackPane = makeTicTacToeCell(i, j);

                char cell = ((TicTacToe_3D) ticTacToe).getCell(i + 1, j + 1, currentDepth + 1);
                if (cell == 'X') {
                    stackPane.getChildren().add(IconsUtilities.makeGroupCross(0.08, 0.08));
                } else if (cell == 'O') {
                    stackPane.getChildren().add(IconsUtilities.makeGroupCircle(0.07, 0.07));
                }

                final int finalI = i;
                final int finalJ = j;

                stackPane.setOnMouseClicked(event -> {
                    Group rightIcon;
                    Group leftIcon;
                    if (event.getButton() == MouseButton.PRIMARY && stackPane.getChildren().size() == 0 && !gameOver && !aiPlays) {
                        // CROIX ou CERCLE
                        if (!aiStarts.isSelected() && !aiPlays) {
                            rightIcon = IconsUtilities.makeGroupCross(0.08, 0.08);
                            leftIcon = IconsUtilities.makeGroupCross(0.024, 0.024);
                        } else {
                            rightIcon = IconsUtilities.makeGroupCircle(0.07, 0.07);
                            leftIcon = IconsUtilities.makeGroupCircle(0.021, 0.021);
                        }
                        stackPane.getChildren().add(rightIcon);
                        stackPane.requestLayout();

                        addCellOnLeftStage(leftIcon, finalI, finalJ);

                        writeMoveInformation(finalI, finalJ);

                        handleWinningState(finalI, finalJ);

                        // On fait jouer l'IA
                        makeAIPlays();
                    }
                });
                currentStage.add(stackPane, j, i);
            }
        }
    }

    private void displayCurrentStage() {
        currentStage.getChildren().clear();
        addCellOnRightStage();
    }

    private void handleWinningState(int row, int column) {
        handleWinningStateHelper(row + 1, column + 1, currentDepth + 1);
    }

    private void handleWinningState(int row, int column, int depth) {
        handleWinningStateHelper(row + 1, column + 1, depth + 1);
    }

    private void handleWinningStateHelper(int row, int column, int depth) {
        boolean won;
        if (dimensionGroup.getSelectedToggle().equals(radioButton2D)) {
            won = ((TicTacToe_2D) ticTacToe).findSolutionFromCell(row, column);
        } else {
            won = ((TicTacToe_3D) ticTacToe).findSolutionFromCell(row, column, depth);
        }
        if (((aiStarts.isSelected() && !aiPlays) || (!aiStarts.isSelected() && aiPlays)) && !won) {
            draw = ticTacToe.getEmptyCell().size() == 0;
            if (draw) {
                gameOver = true;
            }
        }
        if (won || draw) {
            gameOver = true;
            makeWinningScreen();
        }
    }

    private void makeWinningScreen() {
        ticTacToe.getWinningCells().forEach(c -> {
            String[] pos = TicTacToeUtilities.getCellPos(c).split(",");
            int row = Integer.parseInt(pos[0]);
            int column = Integer.parseInt(pos[1]);
            int depth = Integer.parseInt(pos[2]);
            Group icon;
            if ((aiStarts.isSelected() && aiPlays) || (!aiStarts.isSelected() && !aiPlays)) {
                if (dimensionGroup.getSelectedToggle().equals(radioButton2D)) {
                    icon = IconsUtilities.makeGroupCross(0.08, 0.08, ColorsUtilities.GREEN);
                } else {
                    icon = IconsUtilities.makeGroupCross(0.024, 0.024, ColorsUtilities.GREEN);
                }
            } else {
                if (dimensionGroup.getSelectedToggle().equals(radioButton2D)) {
                    icon = IconsUtilities.makeGroupCircle(0.07, 0.07, ColorsUtilities.GREEN);
                } else {
                    icon = IconsUtilities.makeGroupCircle(0.021, 0.021, ColorsUtilities.GREEN);
                }
            }
            if (dimensionGroup.getSelectedToggle().equals(radioButton2D)) {
                ((StackPane) currentStage.getChildren().get(column + (row * 4))).getChildren().clear();
                ((StackPane) currentStage.getChildren().get(column + (row * 4))).getChildren().add(icon);
            } else {
                ((StackPane) stages[depth].getChildren().get(column + (row * 4))).getChildren().clear();
                ((StackPane) stages[depth].getChildren().get(column + (row * 4))).getChildren().add(icon);
            }
        });
        writeWinningState();
    }

    private void makeAIPlays() {
        if (!gameOver) {
            Group rightIcon, leftIcon;
            aiPlays = true;
            Tree tree;
            if (dimensionGroup.getSelectedToggle().equals(radioButton2D)) {
                tree = new Tree(2, aiStarts.isSelected() ? 'X' : 'O', ticTacToe);
            } else {
                tree = new Tree(3, aiStarts.isSelected() ? 'X' : 'O', ticTacToe);
            }
            int playedCell = tree.nextStep();
            String[] aiMove = TicTacToeUtilities.getCellPos(playedCell).split(",");
            int aiRow = Integer.parseInt(aiMove[0]);
            int aiColumn = Integer.parseInt(aiMove[1]);
            int aiDepth = Integer.parseInt(aiMove[2]);
            if (aiStarts.isSelected()) {
                rightIcon = IconsUtilities.makeGroupCross(0.08, 0.08);
                leftIcon = IconsUtilities.makeGroupCross(0.024, 0.024);
                ticTacToe.setCell('X', aiRow + 1, aiColumn + 1, aiDepth + 1);
            } else {
                rightIcon = IconsUtilities.makeGroupCircle(0.07, 0.07);
                leftIcon = IconsUtilities.makeGroupCircle(0.021, 0.021);
                ticTacToe.setCell('O', aiRow + 1, aiColumn + 1, aiDepth + 1);
            }
            writeMoveInformation(aiRow, aiColumn, aiDepth);
            if (dimensionGroup.getSelectedToggle().equals(radioButton2D)) {
                ((StackPane) currentStage.getChildren().get(aiColumn + (aiRow * 4))).getChildren().add(rightIcon);
                handleWinningState(aiRow, aiColumn);
            } else {
                addCellOnLeftStage(leftIcon, aiRow, aiColumn, aiDepth);
                if (aiDepth == currentDepth) {
                    ((StackPane) currentStage.getChildren().get(aiColumn + (aiRow * 4))).getChildren().add(rightIcon);
                }
                handleWinningState(aiRow, aiColumn, aiDepth);
            }
            aiPlays = false;
        }
    }

    //endregion

    //region GESTION DE LA FENETRE DES PARAMETRES

    private void linkRadioButtons() {
        radioButton2D.setToggleGroup(dimensionGroup);
        radioButton2D.setSelected(true);
        radioButton3D.setToggleGroup(dimensionGroup);
    }

    private void handleComboBoxChange() {
        comboBoxFiles.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null && newValue.toLowerCase().equals("import custom board...")) {
                Stage stage = (Stage) parent.getScene().getWindow();
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
                fileChooser.getExtensionFilters().add(extFilter);
                fileChooser.setTitle("Import custom board");
                File file = fileChooser.showOpenDialog(stage);
                if (file != null) {
                    boolean correctName = dimensionGroup.getSelectedToggle().equals(radioButton2D) ? file.getName().startsWith("2D_") : file.getName().startsWith("3D_");
                    if (correctName) {
                        boolean fileAlreadyExists = false;
                        for (Map.Entry<String, File> entry : files.entrySet()) {
                            if (entry.getValue().getName().equals(file.getName())) {
                                fileAlreadyExists = true;
                                break;
                            }
                        }
                        if (fileAlreadyExists) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Failed to import board");
                            alert.setHeaderText("This file name already exists.");
                            alert.showAndWait();
                        } else {
                            String dimension = dimensionGroup.getSelectedToggle().equals(radioButton2D) ? "2D" : "3D";
                            File newFile = new File(String.format("src/files/%s/%s", dimension, file.getName()));
                            try {
                                Files.copy(file.toPath(), newFile.toPath());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            fetchBoards(dimension);
                        }

                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Failed to import board");
                        alert.setHeaderText("Your file name must start with either \"2D_\" or \"3D_\", depending on the chosen dimension.");
                        alert.showAndWait();
                    }
                }
                comboBoxFiles.getSelectionModel().selectFirst();
            }
        });
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
        File directory = new File("src/files/" + dimension + "/");
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
        // Parse le plateau et crée le tableau de Cells.
        initializeTicTacToe();
        boardPane.getChildren().clear();
        createTictactoe();
        information.getChildren().clear();
        if (aiStarts.isSelected()) {
            aiPlays = true;
        }
        initializeInformation();
        if (aiStarts.isSelected()) {
            makeAIPlays();
        }
    }

    //endregion

    //region GESTION DE LA FENETRE DES INFORMATIONS

    /**
     * Crée la première ligne de la fenêtre des informations.
     * Informe l'utilisateur du joueur qui commence ainsi que de la forme qu'il utilise.
     */
    private void initializeInformation() {
        Text player;
        if (aiStarts.isSelected()) {
            player = new Text("AI ");
        } else {
            player = new Text("Player ");
        }
        player.setFill(Color.web(ColorsUtilities.GREY));
        player.setFont(Font.font("System", FontWeight.BOLD, 13));
        Text playerStart = new Text("starts and plays ");
        playerStart.setFill(Color.web(ColorsUtilities.GREY));
        playerStart.setFont(Font.font(13));
        Text playerShape;
        if (aiStarts.isSelected() && aiPlays || !aiStarts.isSelected() && !aiPlays) {
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
     * @param row
     *      Ligne sur laquelle le joueur a effectué son mouvement.
     * @param column
     *      Colonne sur laquelle le joueur a effectué son mouvement.
     */
    private void writeMoveInformation(int row, int column) {
        writeMoveInformationHelper(row, column, currentDepth);
    }

    /**
     * Écrit le mouvement du joueur dans la fenêtre des informations.
     * @param row
     *      Ligne sur laquelle le joueur a effectué son mouvement.
     * @param column
     *      Colonne sur laquelle le joueur a effectué son mouvement.
     */
    private void writeMoveInformation(int row, int column, int depth) {
        writeMoveInformationHelper(row, column, depth);
    }

    private void writeMoveInformationHelper(int row, int column, int depth) {
        Text playerShape;
        // CROIX
        if ((aiStarts.isSelected() && aiPlays) || (!aiStarts.isSelected() && !aiPlays)) {
            playerShape = new Text(Utilities.LINE_SEPARATOR + "Cross");
            playerShape.setFill(Color.web(ColorsUtilities.RED));
            // CERCLE
        } else {
            playerShape = new Text(Utilities.LINE_SEPARATOR + "Circle");
            playerShape.setFill(Color.web(ColorsUtilities.YELLOW));
        }
        playerShape.setFont(Font.font("System", FontWeight.BOLD, 13));
        Text playerMoveStartLine = new Text(" played at line ");
        Text playerMoveLine = new Text(Integer.toString(row + 1));
        Text playerMoveComaColumn = new Text(", column ");
        Text playerMoveColumn = new Text(Integer.toString(column + 1));
        Text playerMoveComaDepth = new Text(", depth ");
        Text playerMoveDepth = new Text(Integer.toString(depth + 1));
        playerMoveStartLine.setFill(Color.web(ColorsUtilities.GREY));
        playerMoveStartLine.setFont(Font.font(13));
        playerMoveLine.setFill(Color.web(ColorsUtilities.GREY));
        playerMoveLine.setFont(Font.font("System", FontWeight.BOLD, 13));
        playerMoveComaColumn.setFill(Color.web(ColorsUtilities.GREY));
        playerMoveComaColumn.setFont(Font.font(13));
        playerMoveColumn.setFill(Color.web(ColorsUtilities.GREY));
        playerMoveColumn.setFont(Font.font("System", FontWeight.BOLD, 13));
        playerMoveComaDepth.setFill(Color.web(ColorsUtilities.GREY));
        playerMoveComaDepth.setFont(Font.font(13));
        playerMoveDepth.setFill(Color.web(ColorsUtilities.GREY));
        playerMoveDepth.setFont(Font.font("System", FontWeight.BOLD, 13));
        if (dimensionGroup.getSelectedToggle().equals(radioButton2D)) {
            information.getChildren().addAll(playerShape, playerMoveStartLine, playerMoveLine, playerMoveComaColumn, playerMoveColumn);
        } else {
            information.getChildren().addAll(playerShape, playerMoveStartLine, playerMoveLine, playerMoveComaColumn, playerMoveColumn, playerMoveComaDepth, playerMoveDepth);
        }
    }

    private void writeWinningState() {
        if (draw) {
            Text result = new Text(Utilities.LINE_SEPARATOR + "Game over! Draw.");
            result.setFill(Color.web(ColorsUtilities.GREEN));
            result.setFont(Font.font("System", FontWeight.BOLD, 13));
            information.getChildren().addAll(result);
        } else {
            Text player;
            if (aiPlays) {
                player = new Text(Utilities.LINE_SEPARATOR + "AI ");
            } else {
                player = new Text(Utilities.LINE_SEPARATOR + "Player ");
            }
            player.setFill(Color.web(ColorsUtilities.GREY));
            player.setFont(Font.font("System", FontWeight.BOLD, 13));
            Text winner = new Text("wins the game!");
            winner.setFill(Color.web(ColorsUtilities.GREEN));
            winner.setFont(Font.font("System", FontWeight.BOLD, 13));
            information.getChildren().addAll(player, winner);
        }
    }

    //endregion
}