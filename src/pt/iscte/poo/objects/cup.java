package pt.iscte.poo.objects;
import pt.iscte.poo.utils.Point2D;

public class Cup extends StaticObject {

    public Cup(Point2D p) {
        super(p, "cup");
    }

    
    public boolean leve() {
        return true;
    }

    @Override
    public boolean movel() {
        return true;
    }

    @Override
    public TipoObjeto getTipo() {
        return TipoObjeto.CUP;
    }


    public int getLayer() {
        return 2;
    }
}
