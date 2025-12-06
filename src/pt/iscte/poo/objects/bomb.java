package pt.iscte.poo.objects;
import pt.iscte.poo.utils.Point2D;

public class Bomb extends StaticObject {

    private boolean falling;

    public Bomb(Point2D p) {
        super(p, "bomb");
    }

    public boolean leve() {
        return true;
    }

    @Override
    public boolean movel() {
        return true;
    }

    public void setFalling(boolean falling) {
        this.falling = falling;
    }

    public boolean wasFalling() {
        return falling;
    }

    public int getLayer() {
        return 2;
    }
}
