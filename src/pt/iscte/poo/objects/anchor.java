package pt.iscte.poo.objects;

import pt.iscte.poo.utils.Direction;

public class Anchor extends StaticObject {

    private boolean jaEmpurrada = false;

    public Anchor(pt.iscte.poo.utils.Point2D p) {
        super(p, "anchor");
    }

    public boolean leve() {
        return false;
    }

    @Override
    public boolean movel() {
        return true;
    }

    @Override
    public boolean podeSerEmpurrado(Direction dir) {
        return !jaEmpurrada && (dir == Direction.LEFT || dir == Direction.RIGHT);
    }

    @Override
    public void notificarEmpurrado() {
        jaEmpurrada = true;
    }

    public int getLayer() {
        return 2;
    }
}
