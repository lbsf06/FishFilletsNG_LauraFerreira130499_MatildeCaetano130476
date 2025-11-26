package objects;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class BigFish extends GameCharacter {

    public BigFish(Point2D p) {
        super(p);
        this.imageName = "bigFishLeft";
    }

    public void move(Direction d) {
        Point2D next = position.plus(d.asVector());
        this.position = next;

        if (d == Direction.RIGHT)
            imageName = "bigFishRight";
        else if (d == Direction.LEFT)
            imageName = "bigFishLeft";
    }

}