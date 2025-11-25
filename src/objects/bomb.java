package objects;

public class bomb extends StaticObject {

    public bomb(pt.iscte.poo.utils.Point2D p) {
        super(p, "bomb");
    }

    public boolean leve() {
        return true;
    }

    public int getLayer() {
        return 2;
    }
}
