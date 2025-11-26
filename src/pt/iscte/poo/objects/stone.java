package pt.iscte.poo.objects;

public class Stone extends StaticObject {

    public Stone(pt.iscte.poo.utils.Point2D p) {
        super(p, "stone");
    }

    public boolean leve() {
        return false;
    }

    public int getLayer() {
        return 2;
    }
}
