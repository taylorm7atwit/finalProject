package javaFX;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class javaFX extends Application{
	//Main
	public static void main(String[] args) {
		launch(args);
	}	
	
	//Stage
	@Override
	public void start(Stage arg0) throws Exception {
		Scene primaryScene = new Scene(createContent());
		arg0.setScene(primaryScene);
		arg0.show();
	}
	
	/*VARIABLES (CHILDREN)*/
	//pane creation
	private Pane menu = new Pane();
	//2d array keeps track of each squares x and y coordinate
	private Tile[][] board = new Tile[5][5];
	//true = players can click, false = players can't click
	private boolean play = true; 
	//true = player X's turn, false = player O's turn
	private boolean turnX = true;
	//arrayList of possible combinations for 5 in a row
	private List<Combo> combos = new ArrayList<>();
	
	/*PARENT*/
	private Parent createContent() {
		//Stage Setup
		menu.setPrefSize(800,800);
		menu.setStyle("-fx-background-color: MEDIUMTURQUOISE;");
		
		//title
		Label title = new Label("Fish vs Cat (5x5 Tic-Tac-Toe)");
		title.setStyle("-fx-background-color: MEDIUMTURQUOISE;");
		title.setFont(Font.font("Courier New",FontWeight.BOLD, 30));
		title.setTranslateX(50);
		title.setTranslateY(7);
		menu.getChildren().add(title);
		
		//5x5 grid
		for(int i = 0;i<5;i++) {
			for(int b = 0; b<5;b++) {
				Tile box = new Tile();
				box.setTranslateX(50+b*140);
				box.setTranslateY(50+i*140);
				
				menu.getChildren().add(box);
				board[b][i] = box;
			}
		}

		//end button
		Button endButton = new Button();
		endButton.setText("EXIT");
		endButton.setStyle("-fx-background-color: DARKCYAN");
		endButton.setFont(Font.font("Courier New",FontWeight.BOLD, 20));
		endButton.setTranslateX(645);
		endButton.setTranslateY(7);
		menu.getChildren().add(endButton);
		
		//result if end button pressed
		 endButton.setOnAction((event) -> {
			System.out.println("Bye!!");
			 System.exit(0); 
		 });
		 
		/*COMBINATIONS*/
		//horizontal
		for(int y = 0; y<5;y++) {
			combos.add(new Combo(board[0][y],board[1][y],board[2][y],board[3][y],board[4][y]));
		}
		//vertical
		for(int x = 0; x<5;x++) {
			combos.add(new Combo(board[x][0],board[x][1],board[x][2],board[x][3],board[x][4]));
		}
		//diagonal TopRight to BottomLeft
		combos.add(new Combo(board[4][0],board[3][1],board[2][2],board[1][3],board[0][4]));
		
		//diagonal TopLeft to BottomRight
		combos.add(new Combo(board[0][0],board[1][1],board[2][2],board[3][3],board[4][4]));
		
		return menu;
	}
	
	//Tile Class
	private class Tile extends StackPane{
		//creating token
		private Text token = new Text();
		
		//creates one tile (140 x 140)
		public Tile() {
			Rectangle outline = new Rectangle(140, 140);
			outline.setFill(Color.LIGHTPINK);
			outline.setStroke(Color.BLACK);
			token.setFont(Font.font("Courier New",FontWeight.BOLD, 30));
			
			setAlignment(Pos.CENTER);
			getChildren().addAll(outline, token);
			
			//places tokens
			setOnMouseClicked(event ->{
				if(!play) {
					return;
				}
				if(event.getButton()==MouseButton.PRIMARY) {
					if(!turnX)
						return;
					drawX();
					turnX = false;
					checkTally();
				}
				else if(event.getButton()==MouseButton.SECONDARY) {
					if(turnX)
						return;
					drawO();
					turnX = true;
					checkTally();

				}
			});
		}
		 
		//draws X (left click)
		private void drawX() {
			token.setText("<><");
		}
		//draws O (right click)
		private void drawO() {
			token.setText("=^._.^=");
		}
		//gets token in a box
		public String getToken() {
			return token.getText();
		}
		//center of box X value
		public double getCenterX() {
			return getTranslateX() + 70;
		}
		//center of box Y value
		public double getCenterY() {
			return getTranslateY() + 70;
		}
		
	}	
	
	//Combo class
	private class Combo{
		private Tile[] tiles;
		//constructor
		public Combo(Tile...tiles) {
			this.tiles = tiles;
		}
		//returns true if there is 5 in a row
		public boolean isComplete() {
			if(tiles[0].getToken().isEmpty())
				return false;
			return tiles[0].getToken().equals(tiles[1].getToken())
					&& tiles[0].getToken().equals(tiles[2].getToken())
					&& tiles[0].getToken().equals(tiles[3].getToken())
					&& tiles[0].getToken().equals(tiles[4].getToken());
		}
	}
		
	//stops game if 5 in a row is met
	private void checkTally() {
		for(int i = 0; i<combos.size();i++) {
			if(combos.get(i).isComplete()) {
				System.out.println("Congratulations!!");
				play = false;
				winLine(combos.get(i));
				break;
			}
		}
	}
	
	//draws line through winning combination
	private void winLine(Combo combos) {
		Line line = new Line();
		line.setStartX(combos.tiles[0].getCenterX());
		line.setStartY(combos.tiles[0].getCenterY());
		line.setEndX(combos.tiles[0].getCenterX());
		line.setEndY(combos.tiles[0].getCenterY());
		line.setStrokeWidth(5);
		line.setStroke(Color.WHITE);
		menu.getChildren().add(line);
		
		Timeline animation = new Timeline();
		animation.getKeyFrames().add(new KeyFrame(Duration.seconds(1),
				new KeyValue(line.endXProperty(), combos.tiles[4].getCenterX()),
				new KeyValue(line.endYProperty(), combos.tiles[4].getCenterY())));
		animation.play();
		
	}
	
}


