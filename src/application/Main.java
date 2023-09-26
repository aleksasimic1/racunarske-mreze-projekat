package application;
	
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class Main extends Application {

    private static final int ROWS = 6;
    private static final int COLUMNS = 8;
    private static final int CIRCLE_RADIUS = 30;

    private int currentPlayer = 1; // Player 1 starts
    private Circle[] circles = new Circle[ROWS*COLUMNS];
    private Circle currentCircle;
    private Label winLabel = new Label();
    private Stage primaryStage;
    private static int PORT = 8901;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Color playerColor;
    private Color opponentColor;
    @Override
    public void start(Stage primaryStage) {
        
        
        this.primaryStage = primaryStage;
        showStartScreen();
        
        
    }
    public Main()
    {
    	
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
                circles[row*COLUMNS+col] = circle;
                int currentCol = row*COLUMNS+col;
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
        //int row = dropCircle(col);
        int req=col;
        out.println("MOV "+req);
        
        String resp2;
        Runnable runnable=()->{
        	String resp;
        	try {
    			
    			while(true) {
    				resp=in.readLine();
    				System.out.println(resp);
    				if(resp.startsWith("VALMOV"))
    				{
    					//winLabel.setText("Potez validan");
    					int loc=Integer.parseInt(resp.substring(6));
    					System.out.println((loc));
    					circles[loc].setFill(playerColor);
    				}else if(resp.startsWith("OPMOV "))
    				{
    					//winLabel.setText("Neprijatelj napravio potez");
    					int loc=Integer.parseInt(resp.substring(6));
    					System.out.println(loc);
    					circles[loc].setFill(opponentColor);
    				}
    				else if(resp.startsWith("VICTORY"))
    				{
    					Platform.runLater(new Runnable() {
    						@Override
    						public void run() {
    							winLabel.setText("Pobjeda");
    							Alert aler=new Alert(AlertType.INFORMATION);
    	    					aler.setContentText("Pobjeda");
    	    					aler.showAndWait();
    						}
    					});
    					    					
    					
    				}else if(resp.startsWith("DEFEAT"))
    				{
    					Platform.runLater(new Runnable() {
    						@Override
    						public void run() {
    							winLabel.setText("Pobjeda");
    							Alert aler=new Alert(AlertType.INFORMATION);
            					aler.setContentText("Gubitak");
            					aler.showAndWait();
    						}
    					});
    					
    				}else if(resp.startsWith("TIE"))
    				{
    					Platform.runLater(new Runnable() {
    						@Override
    						public void run() {
    							winLabel.setText("nerijeseno");
    							Alert aler=new Alert(AlertType.INFORMATION);
            					aler.setContentText("Nerijeseno");
            					aler.showAndWait();
    						}
    					});
    					
    				}else if(resp.startsWith("MSG"))
    				{
    					Platform.runLater(new Runnable() {
    						@Override
    						public void run() {
    							//winLabel.setText(resp.substring(4));
    							
    						}
    					});
    				}
    			}
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        };
        
        Thread t=new Thread(runnable);
        t.start();
        //System.out.println(req+" "+resp2);
        //this.setup();
        
        /*if (row != -1 && circles[row][col].getFill() == Color.WHITE) {
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
        */
    }


    
    
   

    public static void main(String[] args) {
        Main main=new Main();
        main.wrapper(args);
    }
    public void wrapper(String[] args)
    {
    	launch(args);
    	
    }
    //Start screen
    public void setup()
    {
    	try {
        	socket = new Socket("192.168.1.5", PORT);
			in = new BufferedReader(new InputStreamReader(
			    socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
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
        //this.play();
    }
    void play()
    {
    	String resp;
    	try {
			resp=in.readLine();
			if(resp.startsWith("WELCOME"))
			{
				String mark=resp.substring(8);
				System.out.println("Welcome "+mark);
				//primaryStage.setTitle(mark);
				if(mark.equals("zuti")) {
					playerColor=Color.YELLOW;
					opponentColor=Color.RED;
				}
					else {
					opponentColor=Color.YELLOW;
					playerColor=Color.RED;
				}
					
			}
			while(true)
			{
				resp=in.readLine();
				if(resp.startsWith("VALMOV"))
				{
					winLabel.setText("Potez validan");
					int loc=Integer.parseInt(resp.substring(6));
					circles[loc].setFill(playerColor);
				}else if(resp.startsWith("OPMOV "))
				{
					winLabel.setText("Neprijatelj napravio potez");
					int loc=Integer.parseInt(resp.substring(6));
					circles[loc].setFill(opponentColor);
				}
				else if(resp.startsWith("VICTORY"))
				{
					winLabel.setText("Pobjeda!!!!");
					break;
				}else if(resp.startsWith("DEFEAT"))
				{
					winLabel.setText("Gubitak...");
					break;
				}else if(resp.startsWith("TIE"))
				{
					winLabel.setText("Nerijeseno");
					break;
				}else if(resp.startsWith("MSG"))
				{
					winLabel.setText(resp.substring(4));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
        this.setup();
        String resp1;
        try {
			resp1=in.readLine();
			if(resp1.startsWith("WELCOME"))
			{
				String mark=resp1.substring(8);
				System.out.println("Welcome "+mark);
				primaryStage.setTitle(mark);
				if(mark.equals("zuti")) {
					playerColor=Color.YELLOW;
					opponentColor=Color.RED;
				}
					else {
					opponentColor=Color.YELLOW;
					playerColor=Color.RED;
				}
					
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        
        //main.play();
       
    }
    
    
}

