package objects;

import pt.iscte.poo.utils.Point2D;

public class TubodeAço extends StaticObject {

    private boolean horizontal;

    public TubodeAço(Point2D p, boolean horizontal) {
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
