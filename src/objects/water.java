package objects;

import pt.iscte.poo.utils.Point2D;

public class Water extends GameObject {

    public Water(Point2D p) {
        super(p);
<<<<<<< Updated upstream
        this.imageName = "water";
=======
>>>>>>> Stashed changes
    }

    @Override
    public int getLayer() {
        return 0;
    }
}
