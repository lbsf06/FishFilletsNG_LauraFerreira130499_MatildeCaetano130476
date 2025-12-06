package pt.iscte.poo.objects;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Anchor extends MovelObject { // classe Anchor que herda de MovelObject

    private boolean jaEmpurrada = false; // flag para verificar se ja foi empurrada

    public Anchor(Point2D p) { // construtor que chama o construtor da superclasse MovelObject
        super(p, "anchor");
    }

    public boolean leve() { // metodo herdado de MovelObject
        return false;
    }

    @Override
    public boolean podeSerEmpurrado(Direction dir) { // Permite empurrar a ancora apenas uma vez para a esquerda ou
                                                     // direita
        return !jaEmpurrada && (dir == Direction.LEFT || dir == Direction.RIGHT);
    }

    @Override
    public void notificarEmpurrado() { // metodo herdado de MovelObject, marca que ja foi empurrada
        jaEmpurrada = true;
    }

    @Override
    public TipoObjeto getTipo() {// metodo herdado de MovelObject usa o enum TipoObjeto
        return TipoObjeto.ANCHOR;
    }

    public int getLayer() {
        return 2;
    }
}
