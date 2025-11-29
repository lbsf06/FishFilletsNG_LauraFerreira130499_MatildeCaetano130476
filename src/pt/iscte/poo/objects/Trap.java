package pt.iscte.poo.objects;

public class Trap extends StaticObject {

    public Trap(pt.iscte.poo.utils.Point2D p) {
        super(p, "trap");
    }

    public boolean leve() {
        return false;
    }

    public boolean movel() {
        return false;
    }

    public int getLayer() {
        return 1;
    }

    @Override
    public boolean podeAtravessar(GameCharacter character) {
        return character instanceof SmallFish;
    }
}
