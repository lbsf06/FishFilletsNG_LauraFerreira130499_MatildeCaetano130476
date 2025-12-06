package pt.iscte.poo.objects;
import pt.iscte.poo.utils.Point2D;

public class ParedeComBuraco extends Parede {

    public ParedeComBuraco(Point2D p) {
        super(p, "holedWall");
    }

    @Override
    public boolean podeAtravessar(GameCharacter character) {
        return character instanceof SmallFish;
    }

    @Override
    public boolean permitePassagem(GameObject mover) {
        if (mover instanceof Cup)
            return true;
        return super.permitePassagem(mover);
    }

}
