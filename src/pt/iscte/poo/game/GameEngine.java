package pt.iscte.poo.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.objects.GameObject;
import pt.iscte.poo.objects.Parede;
import pt.iscte.poo.objects.Peixe;
import pt.iscte.poo.objects.PeixeGrande;
import pt.iscte.poo.objects.PeixePequeno;
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

    private GameEngine() {
    }

    public void start() {
        gui = ImageGUI.getInstance();
        loadLevel(0);
        gui.go();
    }

    public void loadLevel(int n) {
        objects.clear();

        try {
            Scanner sc = new Scanner(new File("room0.txt"));
            int y = 0;

            while (sc.hasNextLine()) {
                String line = sc.nextLine();

                for (int x = 0; x < line.length(); x++) {
                    char c = line.charAt(x);
                    GameObject obj = createObject(c, x, y);

                    if (obj != null) {
                        objects.add(obj);

                        // Guarda o primeiro peixe como selecionado
                        if (obj instanceof Peixe && peixeSelecionado == null)
                            peixeSelecionado = (Peixe) obj;
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
            default -> null; // água
        };
    }

    public boolean canMoveTo(Point2D next) {

        for (GameObject o : objects) {
            if (o.getPosition().equals(next)) {
                if (o instanceof Parede)
                    return false; // não passar paredes
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
            if (o instanceof Peixe && o != peixeSelecionado) {
                peixeSelecionado = (Peixe) o;
                return;
            }
        }
    }

}
