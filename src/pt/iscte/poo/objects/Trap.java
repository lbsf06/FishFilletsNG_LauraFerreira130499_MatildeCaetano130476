package pt.iscte.poo.objects;

import pt.iscte.poo.utils.Point2D;

public class Trap extends StaticObject { // classe Trap que herda de StaticObject

    public Trap(Point2D p) {
        super(p, "trap"); // chama o construtor da superclasse StaticObject
    }

    public boolean leve() {
        return false;
    }

    public boolean movel() {
        return false;
    }

    @Override
    public TipoObjeto getTipo() {
        return TipoObjeto.TRAP; // usa o enum TipoObjeto
    }

    @Override
    public boolean podeAtravessar(GameCharacter character) { // metodo herdado de staticObject
        return character.getTipo() == TipoObjeto.SMALL_FISH; // apenas SMALL_FISH pode atravessar
    }
}
