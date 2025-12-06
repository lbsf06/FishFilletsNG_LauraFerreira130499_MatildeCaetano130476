package pt.iscte.poo.objects;

import pt.iscte.poo.utils.Point2D;

public class Cup extends MovelObject { // classe Cup que herda de MovelObject

    public Cup(Point2D p) { // construtor que chama o construtor da superclasse MovelObject
        super(p, "cup");
    }

    public boolean leve() {
        return true;
    }

    @Override
    public TipoObjeto getTipo() { // metodo herdado de MovelObject usa o enum TipoObjeto
        return TipoObjeto.CUP;
    }

    @Override
    public int getLayer() { // metodo herdado de MovelObject
        return 3;
    }
}
