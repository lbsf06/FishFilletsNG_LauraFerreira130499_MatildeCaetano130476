package pt.iscte.poo.game;

import pt.iscte.poo.objects.GameObject;

/**
 * Defines the minimal contract a room needs to keep the presentation in sync
 * without having to know about the concrete GUI implementation.
 */
public interface RoomView {

	void onObjectAdded(GameObject obj);

	void onObjectRemoved(GameObject obj);

	void showGameOver(String message);

}
