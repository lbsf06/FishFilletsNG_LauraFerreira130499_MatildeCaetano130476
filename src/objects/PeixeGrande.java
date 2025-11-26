package objects;

import pt.iscte.poo.utils.Point2D;

public class PeixeGrande extends Peixe {

    public PeixeGrande(Point2D p) {
        super(p, "B", "bigFishRight");
    }

    public int getLayer() {
        return 2;
    }

}OLa