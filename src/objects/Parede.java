package objects;

import pt.iscte.poo.utils.Point2D;

public abstract class Parede extends StaticObject {

    public Parede(Point2D p, String img) {
        super(p, img);
    }

    @Override
    public boolean podeAtravessar(String nomeObj) {
        return false;
    }

    @Override
    public int getLayer() {
        return 1;
    }
}
