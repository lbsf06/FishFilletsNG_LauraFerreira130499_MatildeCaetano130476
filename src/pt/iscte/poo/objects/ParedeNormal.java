package pt.iscte.poo.objects;
import pt.iscte.poo.utils.Point2D;

public class ParedeNormal extends Parede {

    public ParedeNormal(Point2D p) {
        super(p, "W");
    }

    @Override
    public TipoObjeto getTipo() {
        return TipoObjeto.PAREDE_NORMAL;
    }

}
