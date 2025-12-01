package pt.iscte.poo.game;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Direction;

import java.awt.event.KeyEvent;

public class GameEngine implements Observer {

    private Room room;
    private final ImageGUI gui;
    private final RoomView roomView;
    private final HighscoreManager highscoreManager = HighscoreManager.getInstance();
    private int lastTickProcessed;
    private long gameStartTime;
    private int totalMoves;

    // nível atual (room0, room1, etc.)
    private int currentLevel = 0;

    // qual peixe está selecionado: true = small, false = big
    private boolean smallSelected = true;

    // contadores de movimentos
    private int movesSmall = 0;
    private int movesBig = 0;

    public GameEngine() {
        gui = ImageGUI.getInstance();
        roomView = new GuiRoomView(gui);
        lastTickProcessed = gui.getTicks();
    }

    public void startGame() {
        totalMoves = 0;
        gameStartTime = System.currentTimeMillis();
        loadLevel(0);
        updateStatusMessage();
    }

    private void loadLevel(int level) {
        gui.hideGameOverOverlay();
        this.currentLevel = level;
        gui.clearImages(); // limpa imagens antigas
        File f = new File("rooms/room" + level + ".txt");
        room = Room.readRoom(f, roomView);
        if (room != null)
            room.applyGravity();
        movesSmall = 0;
        movesBig = 0;
        smallSelected = true; // por default começa no small
        updateStatusMessage();
        lastTickProcessed = gui.getTicks();
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
        int currentTick = gui.getTicks();
        boolean tickOccurred = currentTick != lastTickProcessed;

        if (gui.wasKeyPressed()) {
            handleKey();
        } else if (tickOccurred) {
            handleTick();
            lastTickProcessed = currentTick;
        }

        gui.update();
    }

    private void handleKey() {
        int key = gui.keyPressed();

        if (room != null && room.isGameOver() && key != KeyEvent.VK_R)
            return;

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

        if (moved) {
            totalMoves++;
            updateStatusMessage();
            room.applyGravity();
        }

        if (room != null &&
                room.getSmallFish() != null &&
                room.getBigFish() != null &&
                room.checkLevelExit(room.getSmallFish()) &&
                room.checkLevelExit(room.getBigFish())) {
            advanceOrFinish();
        }

    }


    private void handleTick() {
        if (room == null || room.isGameOver())
            return;
        room.applyGravity();
    }

    private void advanceOrFinish() {
        int nextLevel = currentLevel + 1;
        if (levelExists(nextLevel)) {
            loadLevel(nextLevel);
        } else {
            finishGame();
        }
    }

    private boolean levelExists(int level) {
        File f = new File("rooms/room" + level + ".txt");
        return f.exists();
    }

    private void finishGame() {
        long totalTime = System.currentTimeMillis() - gameStartTime;
        String name = gui.showInputDialog("Fim do Jogo", "Introduza o nome do jogador:");
        if (name == null || name.isBlank())
            name = "Jogador";
        highscoreManager.recordScore(name.trim(), totalTime, totalMoves);
        String table = highscoreManager.formatHighscores();
        gui.showMessage("Top 10 Highscores", table);
        totalMoves = 0;
        gameStartTime = System.currentTimeMillis();
        loadLevel(0);
    }
}
