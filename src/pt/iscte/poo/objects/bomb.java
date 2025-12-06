package pt.iscte.poo.objects;

import pt.iscte.poo.utils.Point2D;

public class Bomb extends MovelObject { // classe Bomb que herda de MovelObject

    private boolean falling; // atributo para verificar se a bomba esta a cair

    public Bomb(Point2D p) {
        super(p, "bomb");
    }

    public boolean leve() {
        return true;
    }

    public void setFalling(boolean falling) { // Setter para ver se a bomba está a cair
        this.falling = falling;
    }

    public boolean wasFalling() { // Getter para ver se a bomba já caiu
        return falling;
    }

    @Override
    public TipoObjeto getTipo() { // metodo herdado de MovelObject usa o enum TipoObjeto
        return TipoObjeto.BOMB;
    }

    public int getLayer() { // metodo herdado de MovelObject
        return 2;
    }
}
