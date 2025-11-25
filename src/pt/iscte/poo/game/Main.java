package pt.iscte.poo.game;

import java.awt.event.KeyEvent;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Direction;

public class Main {

    public static void main(String[] args) {

        GameEngine engine = GameEngine.getInstance();
        ImageGUI gui = ImageGUI.getInstance();

        engine.start();

        while (!gui.wasWindowClosed()) {

            // Espera até o GUI sinalizar que houve uma tecla nova
            try {
                synchronized (gui) {
                    gui.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int key = gui.keyPressed();

            // Mudar peixe (espaço)
            if (key == KeyEvent.VK_SPACE) {
                engine.switchPeixe();
                gui.update();
                continue;
            }

            // Movimento
            if (Direction.isDirection(key)) {
                engine.keyPressed(Direction.directionFor(key));
            }
        }
    }
}

/* package pt.iscte.poo.game;

import pt.iscte.poo.gui.ImageGUI;

public class Main {

	public static void main(String[] args) {
		ImageGUI gui = ImageGUI.getInstance();
		GameEngine engine = new GameEngine();
		gui.setStatusMessage("Good luck!");
		gui.registerObserver(engine);
		gui.go();
	}
	
}
 */