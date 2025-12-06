package pt.iscte.poo.objects;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public abstract class StaticObject extends GameObject {

    public StaticObject(Point2D p, String img) {
        super(p);
        this.imageName = img;
    }

    public boolean aguentaPeso() {
        return true;
    }

    public boolean podeAtravessar(GameCharacter character) {
        return false;
    }

    public boolean leve() {
        return false;
    }

    public boolean movel() {
        return false;
    }

    public boolean podeSerEmpurrado(Direction dir) {
        return true;
    }

    public void notificarEmpurrado() {

    }

    public boolean permitePassagem(GameObject mover) {
        if (mover != null) {
            TipoObjeto tipo = mover.getTipo();
            if (tipo == TipoObjeto.SMALL_FISH || tipo == TipoObjeto.BIG_FISH || tipo == TipoObjeto.KRAB)
                return podeAtravessar((GameCharacter) mover);
        }
        return false;
    }

    @Override
    public TipoObjeto getTipo() {
        return TipoObjeto.OTHER_STATIC;
    }

    @Override
    public int getLayer() {
        return 1;
    }
}
