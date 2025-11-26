package objects;

import java.util.Random;

import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;
import pt.iscte.poo.utils.Direction;

public abstract class GameCharacter extends GameObject {
	
	public GameCharacter(Point2D p) {
		super(p);
	}

	public abstract void move(Direction d);
	
	public void move(Vector2D dir) {
		Random rand = new Random();
		Point2D destination = new Point2D(rand.nextInt(10), rand.nextInt(10)); 
		setPosition(destination);		
	}

	@Override
	public int getLayer() {
		return 2;
	}
	
}
