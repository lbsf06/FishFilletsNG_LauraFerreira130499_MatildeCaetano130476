package pt.iscte.poo.objects;
import pt.iscte.poo.utils.Point2D;

public class Tronco extends StaticObject {

    public Tronco(Point2D p) {
        super(p, "trunk");
    }

    @Override
    public boolean aguentaPeso() {
        return false;
    }

}
