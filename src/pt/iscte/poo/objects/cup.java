package pt.iscte.poo.objects;

public class Cup extends StaticObject {

    public Cup(pt.iscte.poo.utils.Point2D p) {
        super(p, "cup");
    }

    
    public boolean leve() {
        return true;
    }


    public int getLayer() {
        return 2;
    }
}
