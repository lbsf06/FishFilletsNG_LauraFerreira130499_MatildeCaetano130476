package pt.iscte.poo.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import objects.GameObject;
import objects.water;
import objects.Parede;
import objects.Peixe;
import objects.PeixeGrande;
import objects.PeixePequeno;

import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class GameEngine {

    private static GameEngine instance = null;

    public static GameEngine getInstance() {
        if (instance == null)
            instance = new GameEngine();
        return instance;
    }

    private ImageGUI gui;
    private List<GameObject> objects = new ArrayList<>();
    private Peixe peixeSelecionado;

    public GameEngine() {
    }

    public void start() {
        gui = ImageGUI.getInstance();
        loadLevel(0);
        gui.go();
    }

    public void loadLevel(int n) {
        objects.clear();
        peixeSelecionado = null;

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                objects.add(new water(new Point2D(x, y)));
            }
        }

        File file = new File("room0.txt");

        try (Scanner sc = new Scanner(file)) {
            int y = 0;

            while (sc.hasNextLine()) {
                String line = sc.nextLine();

                for (int x = 0; x < line.length(); x++) {

                    char c = line.charAt(x);
                    GameObject obj = createObject(c, x, y);

                    if (obj != null) {
                        objects.add(obj);

                        String name = obj.getName();

                        if (peixeSelecionado == null &&
                                (name.equals("B") || name.equals("smallFishLeft"))) {
                            peixeSelecionado = (Peixe) obj;
                        }
                    }
                }
                y++;
            }

        } catch (FileNotFoundException e) {
            System.out.println("ERRO: room0.txt não encontrado!");
        }

        gui.clearImages();
        gui.addImages(new ArrayList<ImageTile>(objects));
        gui.update();
    }

    private GameObject createObject(char c, int x, int y) {
        Point2D p = new Point2D(x, y);

        return switch (c) {
            case 'B' -> new PeixeGrande(p);
            case 'S' -> new PeixePequeno(p);
            case 'W' -> new Parede(p);
            default -> null;
        };
    }

    public boolean canMoveTo(Point2D next) {

        for (GameObject o : objects) {
            if (o.getPosition().equals(next)) {

                if (o.getName().equals("W"))
                    return false;
            }
        }
        return true;
    }

    public void keyPressed(Direction d) {
        if (peixeSelecionado != null) {
            peixeSelecionado.move(d);
            gui.update();
        }
    }

    public void switchPeixe() {

        for (GameObject o : objects) {
            String name = o.getName();

            boolean isFish = name.equals("B") ||
                    name.equals("S") ||
                    name.equals("bigFishRight") ||
                    name.equals("smallFishRight");

            if (isFish && o != peixeSelecionado) {
                peixeSelecionado = (Peixe) o;
                return;
            }
        }
    }
}
