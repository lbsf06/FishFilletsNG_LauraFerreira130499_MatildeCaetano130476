package objects;

import pt.iscte.poo.utils.Point2D;

public class Parede extends GameObject {

    public Parede(Point2D p) {
        super(p, "W");
    }

    public int getLayer() {
        return 1;
    }
}
