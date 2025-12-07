package pt.iscte.poo.game;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import pt.iscte.poo.objects.Anchor;
import pt.iscte.poo.objects.BigFish;
import pt.iscte.poo.objects.Bomb;
import pt.iscte.poo.objects.Buoy;
import pt.iscte.poo.objects.Cup;
import pt.iscte.poo.objects.GameObject;
import pt.iscte.poo.objects.ParedeComBuraco;
import pt.iscte.poo.objects.ParedeNormal;
import pt.iscte.poo.objects.SmallFish;
import pt.iscte.poo.objects.Stone;
import pt.iscte.poo.objects.Trap;
import pt.iscte.poo.objects.Tronco;
import pt.iscte.poo.objects.TuboDeAco;
import pt.iscte.poo.utils.Point2D;

//regista todos os símbolos dos objetos do jogo
public class DefaultRoomObjectFactory implements RoomObjectFactory {

	private final Map<Character, Function<Point2D, GameObject>> creators = new HashMap<>();

	public DefaultRoomObjectFactory() {
		register('W', ParedeNormal::new);
		register('X', ParedeComBuraco::new);
		register('Y', Tronco::new);
		register('H', p -> new TuboDeAco(p, true));
		register('V', p -> new TuboDeAco(p, false));
		register('C', Cup::new);
		register('R', Stone::new);
		register('A', Anchor::new);
		register('b', Bomb::new);
		register('T', Trap::new);
		register('B', BigFish::new);
		register('S', SmallFish::new);
		register('d', Buoy::new);
	}

	private void register(char symbol, Function<Point2D, GameObject> creator) {
		creators.put(symbol, creator);
	}

	@Override
	public GameObject create(char symbol, Point2D position) {
		Function<Point2D, GameObject> creator = creators.get(symbol);
		if (creator == null)
			return null;
		return creator.apply(position);
	}

}
