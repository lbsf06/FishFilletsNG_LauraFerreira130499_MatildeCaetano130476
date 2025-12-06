package pt.iscte.poo.objects;

import pt.iscte.poo.utils.Point2D;

public class TuboDeAco extends StaticObject {

    private final boolean horizontal;

    public TuboDeAco(Point2D p, boolean horizontal) {
        super(p, horizontal ? "steelHorizontal" : "steelVertical"); // chama o construtor da superclasse StaticObject
        this.horizontal = horizontal;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    @Override
    public boolean aguentaPeso() { // metoto herdado de StaticObject
        return true;
    }

    @Override
    public TipoObjeto getTipo() { // metoto herdado de StaticObject, usa o enum TipoObjeto
        return TipoObjeto.TUBO_DE_ACO;
    }
}
