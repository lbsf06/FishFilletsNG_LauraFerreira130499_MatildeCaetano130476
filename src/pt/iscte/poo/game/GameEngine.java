package pt.iscte.poo.game;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.objects.Cup;
import pt.iscte.poo.objects.GameObject;
import pt.iscte.poo.utils.Direction;

import java.awt.event.KeyEvent;

public class GameEngine implements Observer {

    private Room room;
    private ImageGUI gui;

    // nível atual (room0, room1, etc.)
    private int currentLevel = 0;

    // qual peixe está selecionado: true = small, false = big
    private boolean smallSelected = true;

    // contadores de movimentos
    private int movesSmall = 0;
    private int movesBig = 0;

    public GameEngine() {
        gui = ImageGUI.getInstance();
    }

    public void startGame() {
        loadLevel(0);
        updateStatusMessage();
    }

    private void loadLevel(int level) {
        this.currentLevel = level;
        gui.clearImages(); // limpa imagens antigas
        File f = new File("rooms/room" + level + ".txt");
        room = Room.readRoom(f);
        movesSmall = 0;
        movesBig = 0;
        smallSelected = true; // por default começa no small
        updateStatusMessage();
        gui.update();
       
    }

    private void restartLevel() {
        loadLevel(currentLevel);
    }

    private void updateStatusMessage() {
        String selected = smallSelected ? "SmallFish" : "BigFish";
        String msg = "Level: " + currentLevel +
                " | Selected: " + selected +
                " | Moves Small: " + movesSmall +
                " | Moves Big: " + movesBig;
        gui.setStatusMessage(msg);
    }

    @Override
    public void update(Observable o, Object arg) {
        // distinguir entre TICK e TECLA
        if (gui.wasKeyPressed()) {
            handleKey();
        } else {
            handleTick();
        }
        gui.update();
    }

    private void handleKey() {
        int key = gui.keyPressed();

        // Trocar peixe com espaço
        if (key == KeyEvent.VK_SPACE) {
            smallSelected = !smallSelected;
            updateStatusMessage();
            return;
        }

        // Reiniciar nível com R
        if (key == KeyEvent.VK_R) {
            restartLevel();
            return;
        }

        // Movimento
        boolean moved = false;

        if (Direction.isArrowKey(key)) {
            Direction d = Direction.directionFor(key);

            if (smallSelected && room.getSmallFish() != null) {
                if (room.tryMove(room.getSmallFish(), d)) {
                    movesSmall++;
                    moved = true;
                }
            } else if (!smallSelected && room.getBigFish() != null) {
                if (room.tryMove(room.getBigFish(), d)) {
                    movesBig++;
                    moved = true;
                }
            }
        }

        if (moved)
            updateStatusMessage();

         if (room.checkLevelExit(room.getSmallFish()) && room.checkLevelExit(room.getBigFish())) {
            loadLevel(currentLevel + 1);
        }

    }


    private void handleTick() {
        // Aqui no futuro entra a GRAVIDADE e outros comportamentos automáticos.
        // Por agora não faz nada (para o jogo básico andar com os peixes).
    }
}
