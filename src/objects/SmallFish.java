package objects;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class PeixePequeno extends GameCharacter {

    public PeixePequeno(Point2D p) {
        super(p);
        this.imageName = "smallFishLeft";
    }

    public void move(Direction d) {
        Point2D next = position.plus(d.asVector());
        this.position = next;

        if (d == Direction.RIGHT)
            imageName = "smallFishRight";
        else if (d == Direction.LEFT)
            imageName = "smallFishLeft";
    }
}
