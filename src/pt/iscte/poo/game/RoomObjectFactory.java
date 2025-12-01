package pt.iscte.poo.game;

import pt.iscte.poo.objects.GameObject;
import pt.iscte.poo.utils.Point2D;

/**
 * Responsible for translating the characters that describe a room into game
 * objects.
 */
public interface RoomObjectFactory {

	GameObject create(char symbol, Point2D position);

}
