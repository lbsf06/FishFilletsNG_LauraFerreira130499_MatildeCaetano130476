package pt.iscte.poo.objects;

import pt.iscte.poo.utils.Point2D;

public class TuboDeAco extends StaticObject {

    private final boolean horizontal;

    public TuboDeAco(Point2D p, boolean horizontal) {
        super(p, horizontal ? "steelHorizontal" : "steelVertical");
        this.horizontal = horizontal;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    @Override
    public boolean aguentaPeso() {
        return true;
    }
}
