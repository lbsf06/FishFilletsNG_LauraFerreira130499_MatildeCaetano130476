package objects;

import pt.iscte.poo.utils.Point2D;

public class PeixePequeno extends Peixe {

    public PeixePequeno(Point2D p) {

        super(p, "S", "smallFishRight");
    }

    public int getLayer() {
        return 1;
    }
}
