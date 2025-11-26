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
public enum Direction implements Serializable{

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
            case KeyEvent.VK_DOWN, KeyEvent.VK_S -> DOWN;
            case KeyEvent.VK_UP, KeyEvent.VK_W -> UP;
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> LEFT;
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> RIGHT;
            default -> throw new IllegalArgumentException();
        };

    }

    public static boolean isDirection(int keyCode) {
        return isArrowKey(keyCode) || isWasdKey(keyCode);
    }

    public static boolean isArrowKey(int keyCode) {
        return keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT ||
                keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN;
    }

    public static boolean isWasdKey(int keyCode) {
        return keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_A ||
                keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_D;
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
