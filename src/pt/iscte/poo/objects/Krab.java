package pt.iscte.poo.objects;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Krab extends GameCharacter {

	public Krab(Point2D p) {
		super(p);
		this.imageName = "krab";
	}

	@Override
	public void move(Direction d) {
		if (d == null)
			return;
		Point2D destination = position.plus(d.asVector());
		setPosition(destination);
	}

	@Override
	public TipoObjeto getTipo() {
		return TipoObjeto.KRAB;
	}
}
