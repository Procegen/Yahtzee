package HW.HW08;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Game extends Application{
	int WIDTH = 500;
	int HEIGHT = 600;
	int PLAYBOX_WIDTH = 330;
	int SCOREBOX_WIDTH = 170;
	static int rollCount = 0;
	static int score = 0;
	static int sum = 0;
	static Label totalScore;
	static ArrayList<Dice> diceList = new ArrayList<Dice>();
	static ArrayList<Row> rowList = new ArrayList<Row>();
	static Button rollBtn;
	static Group diceBox;
	static boolean gameEnd = false;

	static enum Type 
	{ 
	    Ones("Ones"), Twos("Twos"), Threes("Threes"), Fours("Fours"), 
	    Fives("Fives"), Sixes("Sixes"), Sum("Sum"), Bonus("Bonus"),
	    ThreeKind("Three of a kind"), FourKind("Four of a kind"), 
	    FullHouse("Full House"), SStraight("Small straight"), LStraight("Large straight"), 
	    Chance("Chance"), Yahtzee("Yahtzee"); 
		
		private String name;
		 
	    Type(String fullName) {
	        this.name = fullName;
	    }
	 
	    public String getName() {
	        return name;
	    }
	} 
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		initUI(stage);
	}

	public void initUI(Stage stage) {
		HBox root = new HBox();
		Scene scene = new Scene(root, WIDTH, HEIGHT);
		stage.setScene(scene);
		stage.setTitle("Yahtzee");
		stage.show();
		stage.setResizable(false);

		VBox playBox = new VBox();
		playBox.setStyle("-fx-background-color: LightBlue");
		playBox.setPrefHeight(HEIGHT);
		playBox.setPrefWidth(PLAYBOX_WIDTH);

		VBox scoreBox = new VBox();
		scoreBox.setStyle("-fx-background-color: Orange");
		scoreBox.setPrefHeight(HEIGHT);
		scoreBox.setPrefWidth(SCOREBOX_WIDTH);
		
		diceBox = new Group();
		diceBox.setStyle("-fx-background-color: Purple");
		diceBox.setTranslateX(PLAYBOX_WIDTH / 2 - 90);
		diceBox.setTranslateY(PLAYBOX_WIDTH / 2 - 90);
		diceBox.setVisible(false);
		
		rollBtn = new Button("ROLL DICE");
		rollBtn.setTranslateX(PLAYBOX_WIDTH / 2 - 100);
		rollBtn.setTranslateY(HEIGHT * 0.3);
		rollBtn.setPrefHeight(50);
		rollBtn.setPrefWidth(200);
		rollBtn.setStyle("-fx-font-size:14");
		rollBtn.setOnAction(
	    	(ActionEvent e)->{
	    		rollDice(); 
	    		rollCount++; 
	    		diceBox.setVisible(true);
	    		updateRows();
	    		if (rollCount >= 3) {
	    			rollBtn.setDisable(true);
	    		}
	    	});
		
		root.getChildren().addAll(playBox, scoreBox);
		playBox.getChildren().addAll(diceBox, rollBtn);
		
		// Add rows
		for (Type day : Type.values()) { 
			Row r = new Row(day);
			rowList.add(r);
			scoreBox.getChildren().add(r.getRow());
		}
		Label totalScoreText = new Label("Total Score:");
		totalScore = new Label(Integer.toString(score));
		VBox.setMargin(totalScoreText, new Insets(5, 0, 5, 10));
		VBox.setMargin(totalScore, new Insets(5, 0, 5, 10));
		totalScoreText.setStyle("-fx-font-size:20");
		totalScore.setStyle("-fx-font-size:20");
		
		scoreBox.getChildren().addAll(totalScoreText, totalScore);
		
		createDice();
	}
	
	public void createDice() {
		double diceX = 0, diceY = 0;
		for (double i = 0; i < 5; i++) {
			if (i % 2 == 0) {
				diceX = 0;
				if (i != 0) {
					diceY += 100;
				}
			}
			else {
				diceX = 100;
			}
			Dice d = new Dice(diceX, diceY, ThreadLocalRandom.current().nextInt(1, 7));
			diceBox.getChildren().add(d);
			diceList.add(d);
		}
	}
	
	public void rollDice() {
		for (Dice i : new ArrayList<>(diceList)) {
		    if (!i.keep) {
		    	diceBox.getChildren().remove(i);
		    	Dice d = new Dice(i.diceX, i.diceY, ThreadLocalRandom.current().nextInt(1, 7));
		    	diceList.remove(i);
		    	diceList.add(d);
		    	diceBox.getChildren().add(d);
		    }
		}
		for (Row i : rowList) {
			i.addList.clear();
			i.outValue = 0;
		}
	}
	
	public void updateRows() {
		for (Row i : rowList) {
			if (!i.updated) {
				i.calculate();
			}
		}
	}
	
	public static void reset() {
		rollCount = 0;
		rollBtn.setDisable(false);
		diceBox.setVisible(false);
		
		for (Dice i : diceList) {
			i.keep = false;
		}
	}
	
	public static void calculateScore() {
		boolean allUpdated = true;
		score = 0;
		for (Row i : rowList) {
			if (!i.updated) {
				allUpdated = false;
			}
		}
		if (allUpdated && !gameEnd) {
			for (Row i : rowList) {
				score += Integer.valueOf(i.output.getText());
			}
			score -= sum;
			totalScore.setText(Integer.toString(score));
			gameEnd = true;
		}
	}
}
