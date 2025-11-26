package pt.iscte.poo.objects;

public class Anchor extends StaticObject {

    public Anchor(pt.iscte.poo.utils.Point2D p) {
        super(p, "anchor");
    }

    public boolean leve() {
        return false;
    }

    public int getLayer() {
        return 2;
    }
}
