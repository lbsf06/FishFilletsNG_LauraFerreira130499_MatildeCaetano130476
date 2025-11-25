package objects;

public class anchor extends StaticObject {

    public anchor(pt.iscte.poo.utils.Point2D p) {
        super(p, "anchor");
    }

    public boolean leve() {
        return false;
    }

    public int getLayer() {
        return 2;
    }
}
