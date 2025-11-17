package pt.iscte.poo.objects;
import pt.iscte.poo.utils.Point2D;

public class water extends GameObject {
    public water (Point2D p) {
        super (p, "water");
    }
    @Override
    public int getLayer() {
        return 0; // água fica por baixo de tudo
    }
}

