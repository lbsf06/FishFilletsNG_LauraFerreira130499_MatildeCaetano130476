package objects;

public class ParedeComBuraco extends Parede {

    public ParedeComBuraco(pt.iscte.poo.utils.Point2D p) {
        super(p, "holedWall");
    }

    @Override
    public boolean podeAtravessar(String nomeObj) {
        return nomeObj.equals("smallFishLeft") ||
                nomeObj.equals("smallFishRight") ||
                nomeObj.equals("S");
    }

}
