package pt.iscte.poo.game;

import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.objects.GameObject;

/**
 * Default {@link RoomView} backed by {@link ImageGUI}.
 */
public class GuiRoomView implements RoomView {

	private final ImageGUI gui;

	public GuiRoomView(ImageGUI gui) {
		this.gui = gui;
	}

	@Override
	public void onObjectAdded(GameObject obj) {
		gui.addImage(obj);
	}

	@Override
	public void onObjectRemoved(GameObject obj) {
		gui.removeImage(obj);
	}

	@Override
	public void showGameOver(String message) {
		gui.showGameOverOverlay(message);
	}
}
