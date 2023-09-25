package application;
	
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Main extends Application {

    private static final int ROWS = 6;
    private static final int COLUMNS = 7;
    private static final int CIRCLE_RADIUS = 30;

    private int currentPlayer = 1; // Player 1 starts
    private Circle[][] circles = new Circle[ROWS][COLUMNS];
    private Label winLabel = new Label();
    
    private Stage primaryStage;


    @Override
    public void start(Stage primaryStage) {
        
        
        this.primaryStage = primaryStage;
        showStartScreen();
        
        
    }

    //Create grid pane
    
    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setBackground(new Background(new BackgroundFill(Color.CADETBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        gridPane.setHgap(7);
        gridPane.setVgap(7);
        

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                Circle circle = createEmptyCircle();
                circles[row][col] = circle;
                int currentCol = col;
                circle.setOnMouseClicked(e -> handleMouseClick(currentCol, gridPane));
                gridPane.add(circle, col, row);
            }
        }

        return gridPane;
    }

    //Create circles
    
    private Circle createEmptyCircle() {
        Circle circle = new Circle(CIRCLE_RADIUS, Color.WHITE);
        circle.setStroke(Color.BLACK);
        return circle;
    }
    
    //Mouse click handler

    private void handleMouseClick(int col, GridPane gridPane) {
        int row = dropCircle(col);
        if (row != -1 && circles[row][col].getFill() == Color.WHITE) {
            circles[row][col].setFill(currentPlayer == 1 ? Color.RED : Color.YELLOW);
            
            
            if (checkWin(row, col)) {
                gridPane.setDisable(true);
                winLabel.setText("Player " + currentPlayer + " wins!");
            }
            if(currentPlayer == 1)
            	currentPlayer = 2;
            else
            	currentPlayer = 1;
        }
    }

    private int dropCircle(int col) {
        for (int row = ROWS - 1; row >= 0; row--) {
            if (circles[row][col].getFill() == Color.WHITE) {
                return row;
            }
        }
        return -1;
    }
    
    //Check for win
    
    private boolean checkWin(int row, int col) {
        Color currentColor = (Color) circles[row][col].getFill();

        //Horizontal check
        int count = 0;
        for (int c = 0; c < COLUMNS; c++) {
            if (circles[row][c].getFill() == currentColor) {
                count++;
                if (count == 4) {
                    return true;
                }
            } else {
                count = 0;
            }
        }

        //Vertical check
        count = 0;
        for (int r = 0; r < ROWS; r++) {
            if (circles[r][col].getFill() == currentColor) {
                count++;
                if (count == 4) {
                    return true;
                }
            } else {
                count = 0;
            }
        }

        //Diagonal check (bottom-left to top right)
        count = 0;
        int startRow = Math.max(row - col, 0);
        int startCol = Math.max(col - row, 0);
        for (int r = startRow, c = startCol; r < ROWS && c < COLUMNS; r++, c++) {
            if (circles[r][c].getFill() == currentColor) {
                count++;
                if (count == 4) {
                    return true;
                }
            } else {
                count = 0;
            }
        }

        //Diagonal check (top-right to bottom-left)
        count = 0;
        startRow = Math.min(row + col, ROWS - 1);
        startCol = Math.max(col - (ROWS - 1 - row), 0);
        for (int r = startRow, c = startCol; r >= 0 && c < COLUMNS; r--, c++) {
            if (circles[r][c].getFill() == currentColor) {
                count++;
                if (count == 4) {
                    return true;
                }
            } else {
                count = 0;
            }
        }

        return false;
    }

   

    public static void main(String[] args) {
        launch(args);
    }
    
    //Start screen
    
    private void showStartScreen() {
        BorderPane startPane = new BorderPane();
        startPane.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, null, null)));

        Button playButton = new Button("PLAY");
        playButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 20px;");
        playButton.setOnAction(e -> startGame());
        
        VBox vbox = new VBox(new Label("Connect 4 Game"), playButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(20);
        
        startPane.setCenter(vbox);

        Scene startScene = new Scene(startPane, 470, 500);
        primaryStage.setTitle("Connect 4 - Start Screen");
        primaryStage.setScene(startScene);
        primaryStage.show();
    }
    
    
    //Start game
    
    private void startGame() {
    	
    	GridPane gridPane = createGridPane();
        VBox root = new VBox(gridPane, winLabel);
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root, 470, 500);
    	
        primaryStage.setTitle("Connect 4");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    
}

