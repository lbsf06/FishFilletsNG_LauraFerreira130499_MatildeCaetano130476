package pt.iscte.poo.objects;

import pt.iscte.poo.utils.Point2D;

public class ParedeNormal extends Parede { // classe ParedeNormal que herda de Parede

    public ParedeNormal(Point2D p) {
        super(p, "W");
    }

    @Override
    public TipoObjeto getTipo() {
        return TipoObjeto.PAREDE_NORMAL;
    }

}
