package pt.iscte.poo.objects;

import pt.iscte.poo.utils.Point2D;

public abstract class StaticObject extends GameObject {

    public StaticObject(Point2D p, String img) {
        super(p);
        this.imageName = img; // ensure GUI knows which sprite to render
    }

    public boolean aguentaPeso() {
        return true;
    }

    public boolean podeAtravessar(GameCharacter character) {
        return false;
    }

    /**
     * Indica se o objeto pode ser arrastado pelo SmallFish.
     * Por omissao os objetos sao pesados.
     */
    public boolean leve() {
        return false;
    }

    @Override
    public int getLayer() {
        return 1;
    }
}
