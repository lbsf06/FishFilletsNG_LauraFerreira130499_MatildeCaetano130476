package pt.iscte.poo.utils;

import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.Random;

/**
 * @author POO2016
 * @version 07-Nov-2021
 * 
 *          Cardinal directions
 *
 */
public enum Direction implements Serializable {
	LEFT(new Vector2D(-1, 0)), UP(new Vector2D(0, -1)), RIGHT(new Vector2D(1, 0)), DOWN(new Vector2D(0, 1));

	private final Vector2D vector;

	Direction(Vector2D vector) {
		this.vector = vector;
	}

	public Vector2D asVector() {
		return vector;
	}

	public static Direction directionFor(int keyCode) {
        return switch (keyCode) {
            case KeyEvent.VK_DOWN -> DOWN;
            case KeyEvent.VK_UP -> UP;
            case KeyEvent.VK_LEFT -> LEFT;
            case KeyEvent.VK_RIGHT -> RIGHT;
            default -> throw new IllegalArgumentException();
        };

    }

	public static boolean isDirection(int lastKeyPressed) {
		return lastKeyPressed >= KeyEvent.VK_LEFT && lastKeyPressed <= KeyEvent.VK_DOWN;
	}

	public Direction opposite() {
        return switch (this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            default -> LEFT;
        };
	}

	public static Direction random() {
		Random generator = new Random();
		return values()[generator.nextInt(values().length)];
	}

	public static Direction forVector(Vector2D v) {
		for (Direction d : values())
			if (v.equals(d.asVector()))
				return d;
		throw new IllegalArgumentException();
	}
}
