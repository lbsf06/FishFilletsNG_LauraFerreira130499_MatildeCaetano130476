package objects;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.game.GameEngine;

public abstract class Peixe extends GameObject {

    protected String leftImg;
    protected String rightImg;
    protected boolean foraDoMapa = false;

    public Peixe(Point2D p, String leftImg, String rightImg) {
        super(p, leftImg);
        this.leftImg = leftImg;
        this.rightImg = rightImg;
    }

    public void move(Direction d) {

        if (foraDoMapa)
            return;

        Point2D next = position.plus(d.asVector());

        if (next.getX() < 0 || next.getX() >= 10 ||
                next.getY() < 0 || next.getY() >= 10) {

            foraDoMapa = true;
            position = next;
            return;
        }

        if (GameEngine.getInstance().canMoveTo(next)) {
            position = next;

            if (d == Direction.RIGHT)
                imageName = rightImg;
            else if (d == Direction.LEFT)
                imageName = leftImg;
        }
    }

    public int getLayer() {
        return 2;
    }
}
