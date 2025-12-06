package pt.iscte.poo.objects;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public abstract class StaticObject extends GameObject { // Classe abstrata que herda de GameObject

    public StaticObject(Point2D p, String img) {
        super(p); // Chama o construtor da superclasse GameObject para inicializar a posição
        this.imageName = img; // Define o nome da imagem do objeto
    }

    public boolean aguentaPeso() { // Default é que aguenta peso
        return true;
    }

    public boolean podeAtravessar(GameCharacter character) { // Default é que não pode atravessar
        return false;
    }

    public boolean leve() { // Default é que não é leve
        return false;
    }

    public boolean movel() { // Default é que não é móvel
        return false;
    }

    public boolean podeSerEmpurrado(Direction dir) { // Default é que pode ser empurrado em qualquer direção
        return true;
    }

    public void notificarEmpurrado() { // Cada classe pode implementar este método se precisar de fazer algo quando for
                                       // empurrada

    }

    public boolean permitePassagem(GameObject mover) {
        if (mover != null) {
            TipoObjeto tipo = mover.getTipo();
            if (tipo == TipoObjeto.SMALL_FISH || tipo == TipoObjeto.BIG_FISH || tipo == TipoObjeto.KRAB)
                return podeAtravessar((GameCharacter) mover);
        }
        return false;
    }

    @Override
    public TipoObjeto getTipo() {
        return TipoObjeto.OTHER_STATIC;
    }

    @Override
    public int getLayer() {
        return 1;
    }
}
