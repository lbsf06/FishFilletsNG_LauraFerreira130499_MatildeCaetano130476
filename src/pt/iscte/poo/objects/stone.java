package pt.iscte.poo.objects;

import pt.iscte.poo.utils.Point2D;

public class Stone extends MovelObject { // classe Stone que herda de MovelObject

    public Stone(Point2D p) {
        super(p, "stone");
    }

    @Override
    public boolean leve() {
        return false;
    }

    private boolean krabSpawned = false; // Variável para rastrear se um Krab já foi criado, começa como false

    public boolean canSpawnKrab() { // Se um Krab já foi criado, não é possivél criar outro
        return !krabSpawned;
    }

    public void markKrabSpawned() { // Marca que um Krab foi criado
        this.krabSpawned = true;
    }

    @Override
    public TipoObjeto getTipo() { // Usa o enum TipoObjeto, herdado de MovelObject
        return TipoObjeto.STONE;
    }

    @Override
    public int getLayer() {
        return 2;
    }
}
