package pt.iscte.poo.objects;
import pt.iscte.poo.utils.Point2D;

public class Stone extends StaticObject {

    public Stone(Point2D p) {
        super(p, "stone");
    }

    public boolean leve() {
        return false;
    }

    @Override
    public boolean movel() {
        return true;
    }

    private boolean krabSpawned = false;

    public boolean canSpawnKrab() {
        return !krabSpawned;
    }

    public void markKrabSpawned() {
        this.krabSpawned = true;
    }

    @Override
    public TipoObjeto getTipo() {
        return TipoObjeto.STONE;
    }

    public int getLayer() {
        return 2;
    }
}
