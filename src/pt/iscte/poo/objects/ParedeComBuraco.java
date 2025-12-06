package pt.iscte.poo.objects;
import pt.iscte.poo.utils.Point2D;

public class ParedeComBuraco extends Parede {

    public ParedeComBuraco(Point2D p) {
        super(p, "holedWall");
    }

	@Override
	public boolean podeAtravessar(GameCharacter character) {
		TipoObjeto tipo = character.getTipo();
		return tipo == TipoObjeto.SMALL_FISH || tipo == TipoObjeto.KRAB;
	}

    @Override
    public boolean permitePassagem(GameObject mover) {
        if (mover != null && mover.getTipo() == TipoObjeto.CUP)
            return true;
        return super.permitePassagem(mover);
    }

	@Override
	public TipoObjeto getTipo() {
		return TipoObjeto.PAREDE_COM_BURACO;
	}
}
