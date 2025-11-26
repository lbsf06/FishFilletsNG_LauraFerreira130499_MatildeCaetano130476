package pt.iscte.poo.objects;

public class cup extends StaticObject {

    public cup(pt.iscte.poo.utils.Point2D p) {
        super(p, "cup");
    }

    
    public boolean leve() {
        return true;
    }


    public int getLayer() {
        return 2;
    }
}
