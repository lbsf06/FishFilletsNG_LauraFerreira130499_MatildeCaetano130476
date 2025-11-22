package objects;

public class Tronco extends StaticObject {

    public Tronco(pt.iscte.poo.utils.Point2D p) {
        super(p, "trunk");
    }

    @Override
    public boolean aguentaPeso() {
        return false;
    }

}
