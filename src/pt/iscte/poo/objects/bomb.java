package pt.iscte.poo.objects;

public class Bomb extends StaticObject {

    public Bomb(pt.iscte.poo.utils.Point2D p) {
        super(p, "bomb");
    }

    public boolean leve() {
        return true;
    }

    @Override
    public boolean movel() {
        return true;
    }

    public int getLayer() {
        return 2;
    }
}
