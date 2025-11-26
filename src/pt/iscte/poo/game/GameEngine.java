package pt.iscte.poo.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import objects.GameObject;
import objects.Water;
import objects.Parede;
import objects.ParedeComBuraco;
import objects.ParedeNormal;
import objects.Peixe;
import objects.PeixeGrande;
import objects.PeixePequeno;
import objects.Tronco;
import objects.TubodeAço;
import objects.cup;
import objects.stone;
import objects.bomb;
import objects.trap;
import objects.anchor;
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
                objects.add(new Water(new Point2D(x, y)));
            }
        }

        File file = new File("rooms/room0.txt");

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

    public void updateGUI() {
        gui.clearImages();
        gui.addImages(new ArrayList<ImageTile>(objects));
        gui.update();
    }

    private GameObject createObject(char c, int x, int y) {
        Point2D p = new Point2D(x, y);

        return switch (c) {
            case 'B' -> new PeixeGrande(p);
            case 'S' -> new PeixePequeno(p);
            case 'W' -> new ParedeNormal(p);
            case 'Y' -> new Tronco(p);
            case 'X' -> new ParedeComBuraco(p);
            case 'V' -> new TubodeAço(p, false);
            case 'H' -> new TubodeAço(p, true);
            case 'C' -> new cup(p);
            case 'R' -> new stone(p);
            case 'A' -> new anchor(p);
            case 'b' -> new bomb(p);
            case 'T' -> new trap(p);

            default -> null;
        };
    }

    public boolean canMoveTo(Point2D next) {

        for (GameObject o : objects) {

            if (o.getPosition().equals(next)) {

                String name = o.getName();

                if (name.equals("W"))
                    return false;

                if (name.equals("holedWall")) {

                    String pName = peixeSelecionado.getName();

                    if (pName.contains("smallFish"))
                        return true;

                    return false;
                }

                if (name.contains("steel"))
                    return false;

                if (name.equals("trunk"))
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
