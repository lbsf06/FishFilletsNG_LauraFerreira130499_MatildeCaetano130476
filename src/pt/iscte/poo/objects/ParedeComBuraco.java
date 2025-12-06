package pt.iscte.poo.objects;

import pt.iscte.poo.utils.Point2D;

public class ParedeComBuraco extends Parede { // classe ParedeComBuraco que herda de Parede

    public ParedeComBuraco(Point2D p) { // construtor que chama o construtor da superclasse Parede
        super(p, "holedWall");
    }

    @Override
    public boolean podeAtravessar(GameCharacter character) { // metodo herdado de Parede, permite atravessar se for
                                                             // SMALL_FISH ou KRAB
        TipoObjeto tipo = character.getTipo();
        return tipo == TipoObjeto.SMALL_FISH || tipo == TipoObjeto.KRAB;
    }

    @Override
    public boolean permitePassagem(GameObject mover) { // metodo herdado de StaticObject, permite passagem se o objeto
                                                       // for CUP
        if (mover != null && mover.getTipo() == TipoObjeto.CUP)
            return true;
        return super.permitePassagem(mover);
    }

    @Override
    public TipoObjeto getTipo() { // metodo herdado de StaticObject usa o enum TipoObjeto
        return TipoObjeto.PAREDE_COM_BURACO;
    }
}
