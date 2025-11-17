package pt.iscte.poo.game;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Direction;

import java.awt.event.KeyEvent;

public class Main {
    public static void main(String[] args) {
        GameEngine engine= GameEngine.getInstance();
        ImageGUI gui = ImageGUI.getInstance();

        engine.start();
        while (!gui.wasWindowClosed()) {

            try {
                synchronized(gui) {
                    gui.wait();   // ESPERA até haver um evento real
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int key = gui.keyPressed();
            if (key == KeyEvent.VK_SPACE) {
                engine.switchPeixe();
                gui.update();
                continue;  // Sai do ciclo e espera nova tecla
            }
            if (Direction.isDirection(key)) {
                engine.keyPressed(Direction.directionFor(key));
            }
        }
    }
}

