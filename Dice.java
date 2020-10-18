package HW.HW08;
import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Dice extends Group {
	double dotNum;
	static double radius = 7;
	static double size = 80;
	Rectangle dice;
	boolean keep = false;
	double diceX = 0;
	double diceY = 0;

	public Dice(double x, double y, double num) {
		dotNum = num;
		diceX = x;
		diceY = y;
		this.setTranslateX(x);
		this.setTranslateY(y);
		addDice();
		addDots();
		setOnMouseClicked(e -> clickDice());
	}

	public void update() {
	}
	
	private void addDice() {
		dice = new Rectangle(size, size);
		dice.setArcHeight(20.0d); 
		dice.setArcWidth(20.0d);
		dice.setX(0);
		dice.setY(0);
		dice.setFill(Color.WHITE);
		this.getChildren().add(dice);
	}

	private void addDots() {
		ArrayList<Circle> dots = new ArrayList<Circle>();
		double dot1X = size / 6, dot1Y = size / 6;
		double dot2X = size / 6 * 5, dot2Y = size / 6;
		double dot3X = size / 6, dot3Y = size / 2;
		double dot4X = size / 2, dot4Y = size / 2;
		double dot5X = size / 6 * 5, dot5Y = size / 2;
		double dot6X = size / 6, dot6Y = size / 6 * 5;
		double dot7X = size / 6 * 5, dot7Y =  size / 6 * 5;
		
		Circle dot1 = new Circle(dot1X, dot1Y, radius);
		Circle dot2 = new Circle(dot2X, dot2Y, radius);
		Circle dot3 = new Circle(dot3X, dot3Y, radius);
		Circle dot4 = new Circle(dot4X, dot4Y, radius);
		Circle dot5 = new Circle(dot5X, dot5Y, radius);
		Circle dot6 = new Circle(dot6X, dot6Y, radius);
		Circle dot7 = new Circle(dot7X, dot7Y, radius);
		
		dots.add(dot1);
		dots.add(dot2);
		dots.add(dot3);
		dots.add(dot4);
		dots.add(dot5);
		dots.add(dot6);
		dots.add(dot7);
		
		for (Circle i : dots) {
			i.setVisible(false);
			i.setFill(Color.RED);
			this.getChildren().add(i);
		}
		
		if (dotNum == 1) {
			dot4.setVisible(true);
		} else if (dotNum == 2) {
			dot2.setVisible(true);
			dot6.setVisible(true);
		} else if (dotNum == 3) {
			dot2.setVisible(true);
			dot6.setVisible(true);
			dot4.setVisible(true);
		} else if (dotNum == 4) {
			dot2.setVisible(true);
			dot6.setVisible(true);
			dot1.setVisible(true);
			dot7.setVisible(true);
		} else if (dotNum == 5) {
			dot1.setVisible(true);
			dot2.setVisible(true);
			dot4.setVisible(true);
			dot6.setVisible(true);
			dot7.setVisible(true);
		} else {
			for (Circle i : dots) {
				i.setVisible(true);
			}
			dot4.setVisible(false);
		}
	}
	
	public void clickDice() {
		if (keep) {
			dice.setFill(Color.WHITE);
			keep = false;
		}
		else {
			dice.setFill(Color.GREY);
			keep = true;
		}
	}
}
