package pt.iscte.poo.objects;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Buoy extends StaticObject {

	public Buoy(Point2D p) {
		super(p, "buoy");
	}

	@Override
	public boolean leve() {
		return true;
	}

	@Override
	public boolean movel() {
		return true;
	}

	@Override
	public boolean podeSerEmpurrado(Direction dir) {
		if (dir == Direction.LEFT || dir == Direction.RIGHT)
			return true;
		return dir == Direction.DOWN;
	}

	@Override
	public boolean aguentaPeso() {
		return true;
	}

	private boolean subidaBloqueada = false;

	public boolean deveSubir() {
		if (subidaBloqueada) {
			subidaBloqueada = false;
			return false;
		}
		return true;
	}

	@Override
	public void notificarEmpurrado() {
		// Sempre que for mexida, para de tentar subir por um tick
		this.subidaBloqueada = true;
	}

	@Override
	public TipoObjeto getTipo() {
		return TipoObjeto.BUOY;
	}

	@Override
	public int getLayer() {
		return 2;
	}
}
