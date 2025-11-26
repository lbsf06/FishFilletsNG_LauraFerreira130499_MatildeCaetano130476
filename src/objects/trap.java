package objects;

public class trap extends StaticObject {

    public trap(pt.iscte.poo.utils.Point2D p) {
        super(p, "trap");
    }

    public boolean leve() {
        return false;
    }

    public int getLayer() {
        return 2;
    }

    @Override
    public boolean podeAtravessar(GameCharacter character) {
        return character instanceof SmallFish;
    }
}
