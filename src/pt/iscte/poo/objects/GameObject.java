package pt.iscte.poo.objects;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public abstract class GameObject implements ImageTile {

    protected Point2D position;
    protected String imageName;

    public GameObject(Point2D p, String img) {
        this.position = p;
        this.imageName = img;
    }

    @Override
    public String getName() {
        return imageName;
    }

    @Override
    public Point2D getPosition() {
        return position;
    }

    @Override
    public int getLayer() {
        return 1;
    }
}
