package pt.iscte.poo.objects;
import pt.iscte.poo.utils.Point2D;

public class Stone extends StaticObject {

    public Stone(Point2D p) {
        super(p, "stone");
    }

    public boolean leve() {
        return false;
    }

    @Override
    public boolean movel() {
        return true;
    }

    public int getLayer() {
        return 2;
    }
}
