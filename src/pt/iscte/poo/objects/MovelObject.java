package pt.iscte.poo.objects;

import pt.iscte.poo.utils.Point2D;
public abstract class MovelObject extends StaticObject {

    public MovelObject(Point2D p, String img) {
        super(p, img);
    }

    @Override
    public boolean movel() {
        return true;
    }
}
