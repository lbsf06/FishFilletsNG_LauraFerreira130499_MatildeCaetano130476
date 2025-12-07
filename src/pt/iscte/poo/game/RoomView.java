package pt.iscte.poo.game;

import pt.iscte.poo.objects.GameObject;


public interface RoomView {

	void onObjectAdded(GameObject obj);

	void onObjectRemoved(GameObject obj);

	void showGameOver(String message);

}
