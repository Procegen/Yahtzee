package HW.HW08;
import java.util.ArrayList;
import java.util.Collections;

import HW.HW08.Game;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

public class Row {
	int WIDTH = 150;
	int outValue = 0;
	int lookNum = 0;
	Label output;
	GridPane root;
	Game.Type type;
	ArrayList<Dice> addList;
	boolean updated = false;
	ArrayList<Double> tempList;
	
	public Row(Game.Type t) {
		root = new GridPane();
		addList = new ArrayList<Dice>();
		
		type = t;
		Label label = new Label(t.getName());
		output = new Label("0");
		
		root.add(label, 0, 0);
		root.add(output, 1, 0);
		root.getColumnConstraints().add(new ColumnConstraints(150));
		root.getColumnConstraints().add(new ColumnConstraints(30));
		GridPane.setMargin(label, new Insets(5, 0, 5, 10));
		root.setStyle("-fx-background-color: white");
		if (type != Game.Type.Bonus && type != Game.Type.Sum) {
			root.setOnMouseClicked(e -> update());
		}
		else {
			updated = true;
		}
	}
	
	// Called every time the user rolls the dice
	public void calculate() {
		sortDice();
		if (type == Game.Type.Ones || type == Game.Type.Twos || type == Game.Type.Threes || type == Game.Type.Fours || type == Game.Type.Fives || type == Game.Type.Sixes) {
			if (type == Game.Type.Ones) {
				lookNum = 1;
			}
			else if (type == Game.Type.Twos) {
				lookNum = 2;
			}
			else if (type == Game.Type.Threes) {
				lookNum = 3;
			}
			else if (type == Game.Type.Fours) {
				lookNum = 4;
			}
			else if (type == Game.Type.Fives) {
				lookNum = 5;
			}
			else {
				lookNum = 6;
			}
			adding();
		}
		else if (type == Game.Type.ThreeKind) {
			threeKind();
		}
		else if (type == Game.Type.FourKind) {
			fourKind();
		}
		else if (type == Game.Type.FullHouse) {
			fullHouse();
		}
		else if (type == Game.Type.SStraight) {
			sstraight();
		}
		else if (type == Game.Type.LStraight) {
			lstraight();
		}
		else if (type == Game.Type.Chance) {
			chance();
		}
		else if (type == Game.Type.Sum) {
		}
		else if (type == Game.Type.Bonus) {
		}
		else {
			yahtzee();
		}
		output.setText(Integer.toString(outValue));
	}
	
	public GridPane getRow() {
		return root;
	}
	
	// Called when user clicks on the row
	private void update() {
		if (!updated) {
			updated = true;
			root.setStyle("-fx-background-color: grey");
			sum();
			
			Game.score += outValue;
			Game.reset();
			
			// update text
			for (Row i : Game.rowList) {
				if (i != this && !i.updated && i.type != Game.Type.Bonus && i.type != Game.Type.Sum) {
					i.outValue = 0;
					i.output.setText("0");
				}
			}
			
			addList.clear();
			updateExtras();
			Game.calculateScore();
		}
	}
	
	private void updateExtras() {
		for (Row i : Game.rowList) {
			if (i.type == Game.Type.Sum) {
				i.output.setText(Integer.toString(Game.sum));
			}
			else if (i.type == Game.Type.Bonus) {
				int bonusCount = 0;
				for (Row j : Game.rowList) {
					if ((j.type == Game.Type.Ones && i.updated) || (j.type == Game.Type.Twos && i.updated) || 
							(j.type == Game.Type.Threes && i.updated) || (j.type == Game.Type.Fours && i.updated) || 
							(j.type == Game.Type.Fives && i.updated) || (j.type == Game.Type.Sixes && i.updated)) {
						bonusCount++;
					}
				}
				if (bonusCount == 6) {
					if (Game.sum >= 63) {
						i.outValue = 35;
						i.output.setText("35");
					}
				}
			}
		}
	}
	
	
	// Calculates adding
	private void adding() {
		for (Dice i : Game.diceList) {
			if (i.dotNum == lookNum && !addList.contains(i)) {
				addList.add(i);
				outValue += i.dotNum;
			}
		}
	}
	
	// Calculates three of a kind
	private void threeKind() {
		// Count duplicates
		double dupNum = 0, dupCount = 1;
		double dupNum2 = 0, dupCount2 = 1;
		double previous = 0;
		double sum = 0;
		for (int i=0; i < tempList.size(); ++i) {
		    if (tempList.get(i) == previous) {
		        dupCount++;
		        dupNum = tempList.get(i);
		    }
		    else {
		    	if (dupCount == 3) {
		    		dupNum2 = dupNum;
		    		dupCount2 = 3;
		    	}
		        dupNum = 0;
		        dupCount = 1;
		    }
		    sum += tempList.get(i);
		    previous = tempList.get(i);
		}
		
		if (dupCount >= 3 || dupCount2 >= 3) {
			outValue = (int)sum;
		}
	}
	
	// Calculates four of a kind
	private void fourKind() {
		// Count duplicates
		double dupNum = 0, dupCount = 1;
		double previous = 0;
		double sum = 0;
		for (int i=0; i < tempList.size(); ++i) {
		    if (tempList.get(i) == previous) {
		        dupCount++;
		        dupNum = tempList.get(i);
		    }
		    else if (dupCount < 4){
		        dupNum = 0;
		        dupCount = 1;
		    }
		    sum += tempList.get(i);
		    previous = tempList.get(i);
		}
		
		if (dupCount >= 4) {
			outValue = (int)sum;
		}
	}
	
	// Calculates full house
	private void fullHouse() {
		// Count duplicates
		double dupNum = 0, dupCount = 1;
		double dupNum2 = 0, dupCount2 = 1;
		double previous = 0;
		for (int i=0; i < tempList.size(); ++i) {
		    if (tempList.get(i) == previous) {
		        dupCount++;
		        dupNum = tempList.get(i);
		    }
		    else {
		    	if (dupCount >= 2) {
		    		dupNum2 = dupNum;
		    		dupCount2 = dupCount;
		    	}
		        dupNum = 0;
		        dupCount = 1;
		    }
		    previous = tempList.get(i);
		}
		
		if ((dupCount == 2 && dupCount2 == 3) || (dupCount == 3 && dupCount2 == 2)) {
			outValue = 25;
		}
	}
	
	private void sstraight() {
		double count = 0;
		double previous = 0;
		for (int i=0; i < tempList.size(); ++i) {
		    if (tempList.get(i) == previous + 1) {
		    	count++;
		    }
		    else if (tempList.get(i) != previous){
		    	count = 0;
		    }
		    previous = tempList.get(i);
		}
		if (count >= 3) {
			outValue = 30;
		}
	}
	
	private void lstraight() {
		boolean straight = true;
		double previous = 0;
		for (int i=0; i < tempList.size(); ++i) {
		    if (i != 0 && tempList.get(i) != previous + 1) {
		    	straight = false;
		    }
		    previous = tempList.get(i);
		}
		if (straight) {
			outValue = 40;
		}
	}
	
	private void chance() {
		for (Dice i : Game.diceList) {
			addList.add(i);
			outValue += i.dotNum;
		}
	}
	
	private void sum() {
		if (type == Game.Type.Ones || type == Game.Type.Twos || type == Game.Type.Threes
				|| type == Game.Type.Fours || type == Game.Type.Fives || type == Game.Type.Sixes) {
			Game.sum += outValue;
		}
	}
	
	private void bonus() {
		
	}
	
	private void yahtzee() {
		double dupNum = 0, dupCount = 1;
		double previous = 0;
		for (int i=0; i < tempList.size(); ++i) {
		    if (tempList.get(i) == previous) {
		        dupCount++;
		        dupNum = tempList.get(i);
		    }
		    else {
		        previous = tempList.get(i);
		        dupNum = 0;
		        dupCount = 1;
		    }
		}
		if (dupCount == 5) {
			outValue = 50;
		}
	}
	
	private void sortDice() {
		tempList = new ArrayList<Double>();
		
		for (int i = 0; i < Game.diceList.size(); i++) {
			tempList.add(Game.diceList.get(i).dotNum);
		}
		Collections.sort(tempList);
	}
}
