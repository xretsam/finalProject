package Client.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedOutputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import Client.Model.Board;
import Client.Model.Piece;
import Client.Network.Client;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class GameController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;


    @FXML
    private GridPane boardView1;
    @FXML
    private GridPane boardView2;
    @FXML
    private GridPane currentBoardView;

    @FXML
    private Pane boardContainer;

    @FXML
    private Label endCause;

    @FXML
    private AnchorPane endGameMenu;

    @FXML
    private Button okBTmenu;

    @FXML
    private Button returnBT;

    @FXML
    private Button returnBTmenu;

    private Client client;

    private Thread clientThread;

    private Board boardModel;

    private PipedOutputStream clientOutput;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setClient(Client client) {
        this.client = client;
    }


    StackPane chosenTile;
    StackPane targetTile;
    private void onTileMousePressed(MouseEvent mouseEvent) {
//        System.out.println("MousePressed");
        StackPane tile = (StackPane) mouseEvent.getSource();
        if(!tile.getChildren().isEmpty()){
            if(tile.getUserData() != null) {
                Integer x1 = GridPane.getColumnIndex(chosenTile);
                Integer y1 = GridPane.getRowIndex(chosenTile);
                Integer x2 = GridPane.getColumnIndex(tile);
                Integer y2 = GridPane.getRowIndex(tile);
                if(x1 == null) x1 = 0;
                if(x2 == null) x2 = 0;
                if(y1 == null) y1 = 0;
                if(y2 == null) y2 = 0;
                doMove(x1,y1,x2,y2);
//                Piece[][] pieces = boardModel.getPieces();
//                pieces[7 - y2][x2] = pieces[7 - y1][x1];
//                pieces[7 - y1][x1] = null;
//                String move = ((char) ('a' + x1)) + "" + (8 - y1) + " " + ((char) ('a' + x2)) + "" + (8 - y2) + "\n";
//                try {
//                    drawBoard();
//                    clientOutput.write(move.getBytes());
//                    System.out.println("sent move" + move);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
                //move

                return;
            }
            if(!(tile.getChildren().get(0) instanceof ImageView)) return;
            Integer row = GridPane.getRowIndex(tile);
            if(row == null) row = 0;
            Integer column = GridPane.getColumnIndex(tile);
            if(column == null) column = 0;
            Piece piece = boardModel.getPieceAt(column, row);
            System.out.println(boardModel.getColor());
            System.out.println(piece.getColor());
            System.out.println(boardModel.getCurrentTurn());
            if(piece.getColor().equals(boardModel.getCurrentTurn()) && piece.getColor().equals(boardModel.getColor())) {
                drawMoves(tile);
                chosenTile = getTileByIndex(column, row, currentBoardView);
                chosenTile.toFront();
//                System.out.println(chosenTile.getChildren().isEmpty());
                onTileMouseDragged(mouseEvent);
            }
        } else {
            chosenTile = null;
            try {
                drawBoard();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void onTileMouseDragged(MouseEvent mouseEvent) {
        StackPane tile = (StackPane) mouseEvent.getSource();
        if (chosenTile == null) return;
        if(!chosenTile.getChildren().isEmpty()){
            if(!(chosenTile.getChildren().get(0) instanceof ImageView)) return;
            System.out.println((tile.getLayoutX() + mouseEvent.getX()) +  " " + (tile.getLayoutY() + mouseEvent.getY()));
            int[] indexes = getTileIndexes((tile.getLayoutX() + mouseEvent.getX()),(tile.getLayoutY() + mouseEvent.getY()));
            int x = indexes[0];
            int y = indexes[1];
            if(x < 8 && x >= 0 && y < 8 && y >= 0) {
                targetTile = getTileByIndex(x, y, currentBoardView);
            }
            chosenTile.getChildren().get(0).setTranslateX(mouseEvent.getX() - chosenTile.getChildren().get(0).boundsInLocalProperty().getValue().getWidth() / 2);
            chosenTile.getChildren().get(0).setTranslateY(mouseEvent.getY() - chosenTile.getChildren().get(0).boundsInLocalProperty().getValue().getHeight() / 2);
        }
    }

    private void onTileMouseReleased(MouseEvent mouseEvent) {
//        System.out.println("mouse released");
        StackPane tile = (StackPane) mouseEvent.getSource();
        if (chosenTile == null) return;
        if(!chosenTile.getChildren().isEmpty()) {
            if(targetTile == null) {
                System.out.println(chosenTile == tile);
                System.out.println((tile.getLayoutX() + mouseEvent.getX()) +  " " + (tile.getLayoutY() + mouseEvent.getY()));
                chosenTile.getChildren().get(0).setTranslateX(0);
                chosenTile.getChildren().get(0).setTranslateY(0);
            } else {
                Integer x1 = GridPane.getColumnIndex(chosenTile);
                Integer y1 = GridPane.getRowIndex(chosenTile);
                Integer x2 = GridPane.getColumnIndex(targetTile);
                Integer y2 = GridPane.getRowIndex(targetTile);
                if(x1 == null) x1 = 0;
                if(x2 == null) x2 = 0;
                if(y1 == null) y1 = 0;
                if(y2 == null) y2 = 0;
                doMove(x1,y1,x2,y2);
            }
        }
    }

    private int[] getTileIndexes(double X, double Y) {
        double tileSize = currentBoardView.getWidth() / 8;
        int x = (int) (X/tileSize);
        int y = (int) (Y/tileSize);
        System.out.println(x + " " + y + " " + tileSize);
        return new int[]{x,y};
    }

    private void doMove(int x1, int y1, int x2, int y2) {
        Piece[][] pieces = boardModel.getPieces();
        pieces[7 - y2][x2] = pieces[7 - y1][x1];
        pieces[7 - y1][x1] = null;
        String move = ((char) ('a' + x1)) + "" + (8 - y1) + " " + ((char) ('a' + x2)) + "" + (8 - y2) + "\n";
        try {
            drawBoard();
            clientOutput.write(move.getBytes());
            System.out.println("sent move" + move);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void onTileDragDetected(MouseEvent event) {
        System.out.println("start drag");
        Dragboard dragboard = ((Node) event.getSource()).startDragAndDrop(TransferMode.MOVE);
        ClipboardContent content = new ClipboardContent();
        content.putString("some data");
        dragboard.setContent(content);

        event.consume();
    }
    private void onTileDragOver(DragEvent dragEvent) {
//        System.out.println("dragged over");
        dragEvent.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        if (chosenTile == null) return;
        if(!chosenTile.getChildren().isEmpty()){
            if(!(chosenTile.getChildren().get(0) instanceof ImageView)) return;
            chosenTile.getChildren().get(0).setTranslateX(dragEvent.getX() - chosenTile.getChildren().get(0).boundsInLocalProperty().getValue().getWidth());
            chosenTile.getChildren().get(0).setTranslateY(dragEvent.getY() - chosenTile.getChildren().get(0).boundsInLocalProperty().getValue().getHeight());
        }
        dragEvent.consume();
    }

    private void onTileDragEntered(DragEvent dragEvent) {
        System.out.println("drag entered");
            dragEvent.consume();
        }

        private void onTileDragExited(DragEvent dragEvent) {
            System.out.println("drag exited");
            if (chosenTile == null) return;
            if(!chosenTile.getChildren().isEmpty()) {
                chosenTile.getChildren().get(0).setTranslateX(0);
                chosenTile.getChildren().get(0).setTranslateY(0);
    //            chosenTile = null;
            }
            dragEvent.consume();
        }
    private void onTileDragDropped(DragEvent dragEvent){
        System.out.println("Dragged into tile");
        StackPane tile = (StackPane) dragEvent.getTarget();
        Platform.runLater(() -> {
            tile.getChildren().clear();
            System.out.println(GridPane.getRowIndex(chosenTile) + " " + GridPane.getColumnIndex(chosenTile));
            tile.getChildren().setAll(chosenTile.getChildren());
        });
        dragEvent.setDropCompleted(true);
        dragEvent.consume();
    }

    @FXML
    private void onReturnBTAction(ActionEvent actionEvent) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Mainmenu.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
            MainMenuController controller = fxmlLoader.getController();
            controller.setStage(stage);
    }
    @FXML
    private void onOkBT(ActionEvent actionEvent) {
        endGameMenu.setVisible(false);
    }

    @FXML
    void initialize() throws IOException {
        if(client != null) clientThread = new Thread(client);
        currentBoardView = boardView1;
        setEventHandlers();
        currentBoardView.setVisible(true);

    }

    public void setOutput() throws IOException {
        clientOutput = new PipedOutputStream();
        client.setScanner(clientOutput);
    }
    void setEventHandlers() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Node tile = boardView1.getChildren().get(i * 8 + j);
                tile.setOnMousePressed(this::onTileMousePressed);
                tile.setOnMouseDragged(this::onTileMouseDragged);
                tile.setOnMouseReleased(this::onTileMouseReleased);
//                tile.setOnDragDetected(this::onTileDragDetected);
//                tile.setOnDragOver(this::onTileDragOver);
//                tile.setOnDragEntered(this::onTileDragEntered);
//                tile.setOnDragExited(this::onTileDragExited);
//                tile.setOnDragDropped(this::onTileDragDropped);
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Node tile = boardView2.getChildren().get(i * 8 + j);
                tile.setOnMousePressed(this::onTileMousePressed);
                tile.setOnMouseDragged(this::onTileMouseDragged);
                tile.setOnMouseReleased(this::onTileMouseReleased);
//                tile.setOnDragDetected(this::onTileDragDetected);
//                tile.setOnDragOver(this::onTileDragOver);
//                tile.setOnDragEntered(this::onTileDragEntered);
//                tile.setOnDragExited(this::onTileDragExited);
//                tile.setOnDragDropped(this::onTileDragDropped);
            }
        }
    }

    public void start() {
        clientThread.start();
    }

    private void clearBoard(GridPane board) {
        for(Node tile : board.getChildren()){
            Platform.runLater(() -> {
                ((StackPane) tile).getChildren().clear();
                tile.setUserData(null);
            });
        }
    }
    public void drawBoard() throws IOException {
        GridPane boardView = currentBoardView == boardView1 ? boardView2 : boardView1;
//        System.out.println("drawing board");
        Piece[][] pieces = boardModel.getPieces();
//        for (int i = 0; i < 8; i++) {
//            for (int j = 0; j < 8; j++) {
//                if(boardModel.getPieces()[i][j] == null) System.out.print("N ");
//                else System.out.print("P ");
//            }
//            System.out.println();
//        }
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[i].length; j++) {
                Piece piece = pieces[i][j];
                if(piece != null) {
                    ImageView pieceImage = getPieceImage(piece);
                    StackPane tile = (StackPane) getTileByIndex(j,7 - i,boardView);
                    pieceImage.setFitWidth(tile.getWidth());
                    pieceImage.setPreserveRatio(true);
                    Platform.runLater(() -> tile.getChildren().add(pieceImage));
                }
            }
        }
        boardView.setVisible(true);
        clearBoard(currentBoardView);
        currentBoardView.setVisible(false);
        currentBoardView = boardView;
    }

    private ImageView getPieceImage(Piece piece){
        String pieceName;
        if (piece.getColor().equals("white")) pieceName = "W";
        else pieceName = "B";
        switch (piece.getType()){
            case "pawn":
                pieceName += "P";
                break;
            case "rook":
                pieceName += "R";
                break;
            case "knight":
                pieceName += "N";
                break;
            case "bishop":
                pieceName += "B";
                break;
            case "queen":
                pieceName += "Q";
                break;
            case "king":
                pieceName += "K";
                break;
        }
        ImageView pieceImage = new ImageView(new Image(getClass().getResource("../../Pieces/" + pieceName + ".png").toExternalForm()));
        return pieceImage;
    }

    public void drawMoves(StackPane tile){
        try {
            drawBoard();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Integer x = GridPane.getColumnIndex(tile);
        if(x == null) x = 0;
        Integer y = GridPane.getRowIndex(tile);
        if(y == null) y = 0;
        Piece[][] pieces = boardModel.getPieces();
        Piece piece = pieces[7 - y][x];
        int[] availableMoves = piece.getAvailableMoves();
//        for (int i = 0; i < 8; i++) {
//            for (int j = 0; j < 8; j++) {
//                System.out.print(availableMoves[i * 8 + j] + " ");
//            }
//            System.out.println();
//        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(availableMoves[i * 8 + j] != 0) {
                    Circle mark = new Circle(tile.getWidth()/6, Color.web("#888888", 0.6));
                    StackPane markedTile = getTileByIndex(j, 7 - i, currentBoardView);
                    Platform.runLater(() -> {
                        markedTile.getChildren().add(mark);
                        markedTile.setUserData("marked");
                    });
                }
            }
        }
    }

    public StackPane getTileByIndex(int x, int y, GridPane board) {
        for(Node tile : board.getChildren()){
            Integer row = GridPane.getRowIndex(tile);
            if(row == null) row = 0;
            Integer column = GridPane.getColumnIndex(tile);
            if(column == null) column = 0;
            if(row == y && column == x) return (StackPane) tile;
        }
        return null;
    }
    public GridPane getBoardView() {
        return currentBoardView;
    }

    public void setEndGame(){
        Platform.runLater(() -> {
            endCause.setText(boardModel.getCause());
            endGameMenu.setVisible(true);
            returnBT.setVisible(true);
        });
    }

    public void setBoardModel(Board boardModel) {
        this.boardModel = boardModel;
    }
}
