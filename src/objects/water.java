package objects;

import pt.iscte.poo.utils.Point2D;

public class Water extends GameObject {
    public Water(Point2D p) {
        super(p, "water");
    }

    @Override
    public int getLayer() {
        return 0;
    }
}
