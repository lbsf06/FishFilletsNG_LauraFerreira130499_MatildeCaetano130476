package objects;

import pt.iscte.poo.utils.Point2D;

public abstract class StaticObject extends GameObject {

    public StaticObject(Point2D p, String img) {
        super(p, img);
    }

    public boolean aguentaPeso() {
        return true;
    }

    public boolean podeAtravessar(String nomeDoPeixe) {
        return false;
    }

    @Override
    public int getLayer() {
        return 1;
    }
}
