package pt.iscte.poo.objects;

public class Bomb extends StaticObject {

    private boolean falling;

    public Bomb(pt.iscte.poo.utils.Point2D p) {
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
