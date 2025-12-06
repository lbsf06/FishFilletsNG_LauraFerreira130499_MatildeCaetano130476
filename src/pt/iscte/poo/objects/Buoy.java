package pt.iscte.poo.objects;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

public class Buoy extends MovelObject { // classe Buoy que herda de MovelObject

	public Buoy(Point2D p) { // construtor que chama o construtor da superclasse MovelObject
		super(p, "buoy");
	}

	@Override
	public boolean podeSerEmpurrado(Direction dir) { // metodo herdado de MovelObject, permite empurrar para esquerda,
														// direita ou para baixo
		if (dir == Direction.LEFT || dir == Direction.RIGHT)
			return true;
		return dir == Direction.DOWN;
	}

	@Override
	public boolean aguentaPeso() { // metodo herdado de MovelObject
		return true;
	}

	private boolean subidaBloqueada = false; // flag para verificar se a subida esta bloqueada

	public boolean deveSubir() {// metodo herdado de MovelObject, verifica se a boia pode subir/flutuar
		if (subidaBloqueada) {
			subidaBloqueada = false;
			return false;
		}
		return true;
	}

	@Override
	public void notificarEmpurrado() {// metodo herdado de MovelObject, bloqueia a subida da boia apos ser empurrada

		this.subidaBloqueada = true;
	}

	@Override
	public TipoObjeto getTipo() { // metodo herdado de MovelObject usa o enum TipoObjeto
		return TipoObjeto.BUOY;
	}

	@Override
	public int getLayer() { // metodo herdado de MovelObject
		return 2;
	}
}
