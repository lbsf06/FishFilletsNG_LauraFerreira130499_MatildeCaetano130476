package pt.iscte.poo.game;

import pt.iscte.poo.objects.GameObject;
import pt.iscte.poo.utils.Point2D;


public interface RoomObjectFactory {

	GameObject create(char symbol, Point2D position);

}
