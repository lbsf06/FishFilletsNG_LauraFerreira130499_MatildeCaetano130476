package pt.iscte.poo.objects;

import pt.iscte.poo.utils.Point2D;

public class Tronco extends StaticObject {

    public Tronco(Point2D p) {// construtor que chama o construtor da superclasse StaticObject
        super(p, "trunk");
    }

    @Override
    public boolean aguentaPeso() {// metodo herdado de StaticObject
        return false;
    }

    @Override
    public TipoObjeto getTipo() { // metodo herdado de StaticObject usa o enum TipoObjeto
        return TipoObjeto.TRONCO;
    }

}
