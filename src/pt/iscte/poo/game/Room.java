package pt.iscte.poo.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import pt.iscte.poo.objects.Water;
import pt.iscte.poo.objects.BigFish;
import pt.iscte.poo.objects.Buoy;
import pt.iscte.poo.objects.GameCharacter;
import pt.iscte.poo.objects.GameObject;
import pt.iscte.poo.objects.ParedeComBuraco;
import pt.iscte.poo.objects.ParedeNormal;
import pt.iscte.poo.objects.SmallFish;
import pt.iscte.poo.objects.Tronco;
import pt.iscte.poo.objects.StaticObject;
import pt.iscte.poo.objects.TuboDeAco;
import pt.iscte.poo.objects.Anchor;
import pt.iscte.poo.objects.Bomb;
import pt.iscte.poo.objects.Cup;
import pt.iscte.poo.objects.Krab;
import pt.iscte.poo.objects.Stone;
import pt.iscte.poo.objects.Trap;
import pt.iscte.poo.objects.TipoObjeto;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Room {

	private final List<GameObject> objects;
	private final RoomView view;
	private final Random random = new Random();
	private final Set<Krab> freshKrabs = new HashSet<>();
	private String roomName;
	private int width;
	private int height;
	private boolean gameOver;

	private SmallFish sf;
	private BigFish bf;

	public Room(RoomView view) {
		this.view = view;
		this.objects = new ArrayList<>();
	}

	private void setName(String name) {
		roomName = name;
	}

	public String getName() {
		return roomName;
	}

	public void addObject(GameObject obj) {
		if (obj == null)
			return;

		objects.add(obj);
		if (obj.getTipo() == TipoObjeto.SMALL_FISH)
			sf = (SmallFish) obj;
		else if (obj.getTipo() == TipoObjeto.BIG_FISH)
			bf = (BigFish) obj;
		else if (obj.getTipo() == TipoObjeto.KRAB)
			freshKrabs.add((Krab) obj);

		if (view != null)
			view.onObjectAdded(obj);
	}

	public void removeObject(GameObject obj) {
		objects.remove(obj);
		if (obj == sf)
			sf = null;
		else if (obj == bf)
			bf = null;
		if (obj != null && obj.getTipo() == TipoObjeto.KRAB)
			freshKrabs.remove(obj);

		if (view != null)
			view.onObjectRemoved(obj);
	}

	public SmallFish getSmallFish() {
		return sf;
	}

	public BigFish getBigFish() {
		return bf;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public static Room readRoom(File f, RoomView view) {
		return readRoom(f, view, new DefaultRoomObjectFactory());
	}

	public static Room readRoom(File f, RoomView view, RoomObjectFactory factory) {

		Room r = new Room(view);
		r.setName(f.getName());

		List<String> linhas = new ArrayList<>();

		try (Scanner sc = new Scanner(f)) {
			while (sc.hasNextLine()) {
				linhas.add(sc.nextLine());
			}
		} catch (FileNotFoundException e) {
			System.err.println("ERRO: ficheiro " + f.getName() + " nao encontrado!");
			return r;
		}

		int altura = linhas.size();
		int largura = linhas.stream().mapToInt(String::length).max().orElse(0);
		r.height = altura;
		r.width = largura;

		for (int y = 0; y < altura; y++) {
			for (int x = 0; x < largura; x++) {
				GameObject agua = new Water(new Point2D(x, y));
				r.addObject(agua);
			}
		}

		RoomObjectFactory effectiveFactory = factory != null ? factory : new DefaultRoomObjectFactory();

		for (int y = 0; y < altura; y++) {
			String linha = linhas.get(y);

			for (int x = 0; x < largura; x++) {
				char c = x < linha.length() ? linha.charAt(x) : ' ';
				Point2D p = new Point2D(x, y);

				GameObject obj = effectiveFactory.create(c, p);
				if (obj != null)
					r.addObject(obj);
			}
		}

		return r;
	}

	public boolean tryMove(GameCharacter character, Direction dir) {
		if (character == null || dir == null)
			return false;

		Point2D current = character.getPosition();
		Point2D target = current.plus(dir.asVector());

		boolean moved = false;

		if (canEnter(character, current, target)) {
			character.move(dir);
			moved = true;
		} else if (character.getTipo() == TipoObjeto.SMALL_FISH) {
			moved = tryPushLightObject(character, dir, target);
		} else if (character.getTipo() == TipoObjeto.BIG_FISH) {
			moved = tryPushBigFish(character, dir);
		}

		if (moved)
			moveKrabs();

		return moved;
	}

	private boolean tryPushLightObject(GameCharacter fish, Direction dir, Point2D target) {
		StaticObject lightObject = getSingleLightObject(target);
		if (lightObject == null)
			return false;

		if (!lightObject.podeSerEmpurrado(dir))
			return false;

		if (lightObject.getTipo() == TipoObjeto.BUOY && dir == Direction.DOWN)
			return false;

		Point2D pushTarget = target.plus(dir.asVector());
		if (!canObjectOccupy(lightObject, pushTarget))
			return false;

		lightObject.setPosition(pushTarget);
		if ((dir == Direction.LEFT || dir == Direction.RIGHT) && lightObject.getTipo() == TipoObjeto.STONE)
			onStoneMovedHorizontally((Stone) lightObject);

		lightObject.notificarEmpurrado();
		fish.move(dir);
		return true;
	}

	private boolean tryPushBigFish(GameCharacter fish, Direction dir) {
		boolean horizontal = dir == Direction.LEFT || dir == Direction.RIGHT;
		int max = horizontal ? Integer.MAX_VALUE : 1;
		return pushChain(fish, dir, max);
	}

	private boolean pushChain(GameCharacter fish, Direction dir, int maxObjects) {
		List<StaticObject> chain = new ArrayList<>();
		Vector2D delta = dir.asVector();
		Point2D check = fish.getPosition().plus(delta);
		boolean isBigFish = fish.getTipo() == TipoObjeto.BIG_FISH;

		while (true) {
			if (!isInside(check))
				return false;

			StaticObject mover = chain.isEmpty() ? null : chain.get(chain.size() - 1);
			if (mover != null && canObjectOccupy(mover, check))
				break;

			StaticObject movable = getSingleMovableObject(check);
			if (movable == null || (movable.getTipo() == TipoObjeto.BUOY && dir == Direction.DOWN && !isBigFish)
					|| !movable.podeSerEmpurrado(dir))
				return false;

			chain.add(movable);
			if (chain.size() > maxObjects)
				return false;

			check = check.plus(delta);
		}

		if (chain.isEmpty())
			return false;

		for (int i = chain.size() - 1; i >= 0; i--) {
			StaticObject obj = chain.get(i);
			obj.setPosition(obj.getPosition().plus(delta));
			if ((dir == Direction.LEFT || dir == Direction.RIGHT) && obj.getTipo() == TipoObjeto.STONE)
				onStoneMovedHorizontally((Stone) obj);

			obj.notificarEmpurrado();
		}

		fish.move(dir);
		return true;
	}

	private boolean canEnter(GameCharacter character, Point2D current, Point2D target) {
		boolean currentInside = isInside(current);
		boolean targetInside = isInside(target);

		if (!currentInside && targetInside)
			return false;

		if (!targetInside)
			return true;

		for (GameObject obj : objectsAt(target)) {
			if (obj == character || obj.getTipo() == TipoObjeto.WATER)
				continue;

			if (obj.getTipo() == TipoObjeto.KRAB) {
				Krab krab = (Krab) obj;
				if (character.getTipo() == TipoObjeto.SMALL_FISH) {
					killFish(character);
					return false;
				}
				if (character.getTipo() == TipoObjeto.BIG_FISH) {
					destroyObject(krab);
					continue;
				}
			}

			if (isCharacter(obj))
				return false;

			if (obj.getTipo() == TipoObjeto.TRAP && character.getTipo() == TipoObjeto.BIG_FISH) {
				killFish(character);
				return false;
			}

			if (isStatic(obj)) {
				StaticObject staticObj = (StaticObject) obj;
				if (!staticObj.permitePassagem(character))
					return false;
			}
		}

		return true;
	}

	private List<GameObject> objectsAt(Point2D position) {
		List<GameObject> list = new ArrayList<>();
		for (GameObject obj : objects) {
			if (obj.getPosition().equals(position))
				list.add(obj);
		}
		return list;
	}

	public boolean isInside(Point2D p) {
		if (p == null)
			return false;
		return p.getX() >= 0 && p.getX() < width && p.getY() >= 0 && p.getY() < height;
	}

	public boolean checkLevelExit(GameObject fish) {
		Point2D pos = fish.getPosition();

		return !isInside(pos);
	}

	private StaticObject getSingleLightObject(Point2D position) {
		StaticObject obj = getSingleMovableObject(position);
		if (obj != null && obj.leve())
			return obj;
		return null;
	}

	private StaticObject getSingleMovableObject(Point2D position) {
		StaticObject movable = null;
		List<StaticObject> blockers = new ArrayList<>();
		for (GameObject obj : objectsAt(position)) {
			if (obj.getTipo() == TipoObjeto.WATER)
				continue;
			if (!isStatic(obj))
				return null;
			StaticObject staticObj = (StaticObject) obj;
			if (staticObj.movel()) {
				if (movable != null)
					return null;
				movable = staticObj;
			} else {
				blockers.add(staticObj);
			}
		}

		if (movable == null)
			return null;

		for (StaticObject blocker : blockers) {
			if (!blocker.permitePassagem(movable))
				return null;
		}

		return movable;
	}

	private boolean isTileFree(Point2D position) {
		for (GameObject obj : objectsAt(position)) {
			if (obj.getTipo() == TipoObjeto.WATER)
				continue;
			return false;
		}
		return true;
	}

	private boolean canObjectOccupy(GameObject mover, Point2D position) {
		if (!isInside(position))
			return false;
		for (GameObject obj : objectsAt(position)) {
			if (obj.getTipo() == TipoObjeto.WATER || obj == mover)
				continue;
			if (isStatic(obj)) {
				StaticObject staticObj = (StaticObject) obj;
				if (!staticObj.permitePassagem(mover))
					return false;
			} else {
				return false;
			}
		}
		return true;
	}

	private void onStoneMovedHorizontally(Stone stone) {
		if (stone == null || !stone.canSpawnKrab())
			return;

		Point2D spawn = stone.getPosition().plus(Direction.UP.asVector());
		if (!isInside(spawn))
			return;
		if (!isTileFree(spawn))
			return;

		stone.markKrabSpawned();
		addObject(new Krab(spawn));
	}

	public void applyGravity() {
		List<StaticObject> movable = new ArrayList<>();
		for (GameObject obj : objects) {
			if (isStatic(obj)) {
				StaticObject staticObj = (StaticObject) obj;
				if (staticObj.movel())
					movable.add(staticObj);
			}
		}

		movable.sort(Comparator.comparingInt((StaticObject o) -> o.getPosition().getY()).reversed());

		for (StaticObject obj : movable) {
			if (obj.getTipo() == TipoObjeto.BUOY)
				continue;
			if (!hasSupport(obj)) {
				if (obj.getTipo() == TipoObjeto.BOMB)
					((Bomb) obj).setFalling(true);

				Point2D target = obj.getPosition().plus(Direction.DOWN.asVector());
				if (canObjectOccupy(obj, target)) {
					obj.setPosition(target);
				} else if (breakTroncoIfHeavy(obj, target) && canObjectOccupy(obj, target)) {
					obj.setPosition(target);
				} else if (handleAnchorCrush(obj, target)) {
					continue;
				} else if (obj.getTipo() == TipoObjeto.BOMB) {
					Bomb bomb = (Bomb) obj;
					if (bomb.wasFalling() && triggerBombCollision(bomb, target))
						continue;
				}
			} else if (obj.getTipo() == TipoObjeto.BOMB) {
				Bomb bomb = (Bomb) obj;
				if (bomb.wasFalling() && handleBombSupport(bomb))
					continue;
				bomb.setFalling(false);
			}
		}

		for (StaticObject obj : movable) {
			if (obj.getTipo() != TipoObjeto.BUOY)
				continue;
			Buoy buoy = (Buoy) obj;

			StaticObject supported = getMovableOnTop(buoy);
			if (supported != null) {
				sinkBuoy(buoy, supported);
				continue;
			}

			if (!buoy.deveSubir())
				continue;

			Point2D above = buoy.getPosition().plus(Direction.UP.asVector());
			if (canObjectOccupy(buoy, above))
				buoy.setPosition(above);
		}

		List<Krab> krabs = getKrabs();
		krabs.sort(Comparator.comparingInt((Krab k) -> k.getPosition().getY()).reversed());
		for (Krab krab : krabs) {
			if (!krabHasSupport(krab))
				moveKrabTo(krab, krab.getPosition().plus(Direction.DOWN.asVector()));
		}
	}

	private boolean hasSupport(StaticObject obj) {
		Point2D below = obj.getPosition().plus(Direction.DOWN.asVector());
		if (!isInside(below))
			return true;

		for (GameObject other : objectsAt(below)) {
			if (other.getTipo() == TipoObjeto.WATER)
				continue;
			if (isCharacter(other))
				return true;
			if (isStatic(other)) {
				StaticObject staticObj = (StaticObject) other;
				if (staticObj.aguentaPeso()) {
					if (obj.getTipo() == TipoObjeto.CUP && other.getTipo() == TipoObjeto.PAREDE_COM_BURACO)
						continue;
					return true;
				}
			}
		}

		return false;
	}

	private StaticObject getMovableOnTop(StaticObject obj) {
		Point2D above = obj.getPosition().plus(Direction.UP.asVector());
		if (!isInside(above))
			return null;

		for (GameObject g : objectsAt(above)) {
			if (isStatic(g)) {
				StaticObject s = (StaticObject) g;
				if (s.movel())
					return s;
			}
		}
		return null;
	}

	private void sinkBuoy(Buoy buoy, StaticObject supported) {
		Point2D below = buoy.getPosition().plus(Direction.DOWN.asVector());
		if (!canObjectOccupy(buoy, below))
			return;

		buoy.setPosition(below);

		if (supported == null)
			return;

		Point2D supportedTarget = supported.getPosition().plus(Direction.DOWN.asVector());
		if (canObjectOccupy(supported, supportedTarget))
			supported.setPosition(supportedTarget);
	}

	private void applyNormalGravity(StaticObject obj) {
    	if (!hasSupport(obj)) {
        	Point2D target = obj.getPosition().plus(Direction.DOWN.asVector());
        	if (canObjectOccupy(obj, target)) {
            	obj.setPosition(target);
        	}
    	}
	}

	private boolean isCharacter(GameObject obj) {
		if (obj == null)
			return false;
		TipoObjeto tipo = obj.getTipo();
		return tipo == TipoObjeto.SMALL_FISH || tipo == TipoObjeto.BIG_FISH || tipo == TipoObjeto.KRAB;
	}

	private boolean isStatic(GameObject obj) {
		if (obj == null)
			return false;
		TipoObjeto tipo = obj.getTipo();
		return tipo != TipoObjeto.WATER && tipo != TipoObjeto.SMALL_FISH && tipo != TipoObjeto.BIG_FISH
				&& tipo != TipoObjeto.KRAB;
	}

	private List<Krab> getKrabs() {
		List<Krab> krabs = new ArrayList<>();
		for (GameObject obj : objects) {
			if (obj.getTipo() == TipoObjeto.KRAB)
				krabs.add((Krab) obj);
		}
		return krabs;
	}

	private void moveKrabs() {
		for (Krab krab : getKrabs()) {
			if (!objects.contains(krab))
				continue;
			if (freshKrabs.contains(krab))
				continue;
			Direction dir = random.nextBoolean() ? Direction.LEFT : Direction.RIGHT;
			moveKrabTo(krab, krab.getPosition().plus(dir.asVector()));
		}
		freshKrabs.clear();
	}

	private boolean moveKrabTo(Krab krab, Point2D target) {
		if (krab == null || !objects.contains(krab))
			return false;
		if (!isInside(target))
			return false;

		List<GameObject> occupants = new ArrayList<>(objectsAt(target));
		for (GameObject obj : occupants) {
			if (obj.getTipo() == TipoObjeto.WATER || obj == krab)
				continue;

			if (obj.getTipo() == TipoObjeto.TRAP) {
				destroyObject(krab);
				return false;
			}

			if (obj.getTipo() == TipoObjeto.SMALL_FISH) {
				killFish((GameCharacter) obj);
				continue;
			}

			if (obj.getTipo() == TipoObjeto.BIG_FISH) {
				destroyObject(krab);
				return false;
			}

			if (obj.getTipo() == TipoObjeto.KRAB)
				return false;

			if (isStatic(obj)) {
				StaticObject staticObj = (StaticObject) obj;
				if (!staticObj.permitePassagem(krab))
					return false;
				continue;
			}

			if (isCharacter(obj))
				return false;
		}

		if (!objects.contains(krab))
			return false;

		krab.setPosition(target);
		return true;
	}

	private boolean krabHasSupport(Krab krab) {
		if (krab == null)
			return true;

		Point2D below = krab.getPosition().plus(Direction.DOWN.asVector());
		if (!isInside(below))
			return true;

		for (GameObject other : objectsAt(below)) {
			if (other.getTipo() == TipoObjeto.WATER)
				continue;
			if (other.getTipo() == TipoObjeto.TRAP)
				return false;
			if (other.getTipo() == TipoObjeto.KRAB)
				return true;
			if (isStatic(other)) {
				StaticObject staticObj = (StaticObject) other;
				if (staticObj.aguentaPeso())
					return true;
			}
		}

		return false;
	}

	private boolean triggerBombCollision(Bomb bomb, Point2D target) {
		boolean hitSolid = false;
		for (GameObject other : objectsAt(target)) {
			if (other.getTipo() == TipoObjeto.WATER)
				continue;
			if (isCharacter(other))
				continue;
			hitSolid = true;
			break;
		}
		if (!hitSolid)
			return false;

		explodeBomb(bomb);
		return true;
	}

	private boolean handleBombSupport(Bomb bomb) {
		Point2D below = bomb.getPosition().plus(Direction.DOWN.asVector());
		if (!isInside(below))
			return false;

		List<GameObject> belowObjects = objectsAt(below);
		boolean hasSolid = false;
		for (GameObject obj : belowObjects) {
			if (obj.getTipo() == TipoObjeto.WATER)
				continue;
			if (isCharacter(obj))
				return false;
			hasSolid = true;
		}

		if (hasSolid) {
			explodeBomb(bomb);
			return true;
		}

		return false;
	}

	private void explodeBomb(Bomb bomb) {
		List<GameObject> toRemove = new ArrayList<>();
		collectBlastTargets(bomb.getPosition(), toRemove);
		for (Direction dir : new Direction[] { Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT }) {
			Point2D pos = bomb.getPosition().plus(dir.asVector());
			if (isInside(pos))
				collectBlastTargets(pos, toRemove);
		}

		for (GameObject obj : toRemove) {
			destroyObject(obj);
		}
	}

	private void collectBlastTargets(Point2D position, List<GameObject> collector) {
		for (GameObject obj : objectsAt(position)) {
			if (obj.getTipo() == TipoObjeto.WATER)
				continue;
			if (!collector.contains(obj))
				collector.add(obj);
		}
	}

	private void destroyObject(GameObject obj) {
		if (obj == null)
			return;

		boolean smallDied = obj == sf;
		boolean bigDied = obj == bf;
		removeObject(obj);
		if (smallDied || bigDied)
			notifyGameOver();
	}

	private void killFish(GameCharacter fish) {
		destroyObject(fish);
	}

	private void notifyGameOver() {
		if (!gameOver) {
			gameOver = true;
			if (view != null)
				view.showGameOver("Game Over! Press R to try again.");
		}
	}

	private boolean handleAnchorCrush(StaticObject obj, Point2D target) {
		if (obj == null || obj.getTipo() != TipoObjeto.ANCHOR)
			return false;
		if (!isInside(target))
			return false;

		GameCharacter fishToKill = null;
		for (GameObject occupant : objectsAt(target)) {
			if (occupant == obj || occupant.getTipo() == TipoObjeto.WATER)
				continue;

			if (occupant.getTipo() == TipoObjeto.SMALL_FISH) {
				fishToKill = (GameCharacter) occupant;
				continue;
			}

			if (isStatic(occupant)) {
				StaticObject staticOccupant = (StaticObject) occupant;
				if (!staticOccupant.permitePassagem(obj))
					return false;
				continue;
			}

			return false;
		}

		if (fishToKill == null)
			return false;

		killFish(fishToKill);
		obj.setPosition(target);
		return true;
	}

	private boolean breakTroncoIfHeavy(StaticObject mover, Point2D target) {
		if (mover == null || mover.leve())
			return false;

		List<GameObject> targetObjects = new ArrayList<>(objectsAt(target));
		boolean removed = false;
		for (GameObject obj : targetObjects) {
			if (obj.getTipo() == TipoObjeto.WATER)
				continue;
			if (obj.getTipo() == TipoObjeto.TRONCO) {
				removeObject(obj);
				removed = true;
			} else {
				return false;
			}
		}
		return removed;
	}

}
