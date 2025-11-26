package pt.iscte.poo.objects;

import pt.iscte.poo.utils.Point2D;

public class water extends GameObject {
    public water(Point2D p) {
        super(p);
        this.imageName = "water";
    }

    @Override
    public int getLayer() {
        return 0;
    }
}
