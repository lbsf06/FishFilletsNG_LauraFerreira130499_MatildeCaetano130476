package objects;

public class stone extends StaticObject {

    public stone(pt.iscte.poo.utils.Point2D p) {
        super(p, "stone");
    }

    public boolean leve() {
        return false;
    }

    public int getLayer() {
        return 2;
    }
}
