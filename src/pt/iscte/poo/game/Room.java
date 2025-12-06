package pt.iscte.poo.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

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
import pt.iscte.poo.objects.Stone;
import pt.iscte.poo.objects.Trap;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Vector2D;

public class Room {

	private final List<GameObject> objects;
	private final RoomView view;
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
		if (obj instanceof SmallFish small)
			sf = small;
		else if (obj instanceof BigFish big)
			bf = big;

		if (view != null)
			view.onObjectAdded(obj);
	}

	public void removeObject(GameObject obj) {
		objects.remove(obj);
		if (obj == sf)
			sf = null;
		else if (obj == bf)
			bf = null;

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

		if (canEnter(character, current, target)) {
			character.move(dir);
			return true;
		}

		if (character instanceof SmallFish) {
			return tryPushLightObject((SmallFish) character, dir, target);
		}

		if (character instanceof BigFish) {
			return tryPushBigFish((BigFish) character, dir);
		}

		return false;
	}

	private boolean tryPushLightObject(SmallFish fish, Direction dir, Point2D target) {
		StaticObject lightObject = getSingleLightObject(target);
		if (lightObject == null)
			return false;

		Point2D pushTarget = target.plus(dir.asVector());
		if (!canObjectOccupy(lightObject, pushTarget))
			return false;

		lightObject.setPosition(pushTarget);
		fish.move(dir);
		return true;
	}

	private boolean tryPushBigFish(BigFish fish, Direction dir) {
		boolean horizontal = dir == Direction.LEFT || dir == Direction.RIGHT;
		int max = horizontal ? Integer.MAX_VALUE : 1;
		return pushChain(fish, dir, max);
	}

	private boolean pushChain(BigFish fish, Direction dir, int maxObjects) {
		List<StaticObject> chain = new ArrayList<>();
		Vector2D delta = dir.asVector();
		Point2D check = fish.getPosition().plus(delta);

		while (true) {
			if (!isInside(check))
				return false;

			StaticObject mover = chain.isEmpty() ? null : chain.get(chain.size() - 1);
			if (mover != null && canObjectOccupy(mover, check))
				break;

			StaticObject movable = getSingleMovableObject(check);
			if (movable == null || (movable instanceof Buoy && dir == Direction.DOWN && !(fish instanceof BigFish)) || !movable.podeSerEmpurrado(dir))
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
			if (obj == character || obj instanceof Water)
				continue;

			if (obj instanceof GameCharacter)
				return false;

			if (obj instanceof Trap && character instanceof BigFish) {
				killFish(character);
				return false;
			}

			if (obj instanceof StaticObject staticObj) {
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
			if (obj instanceof Water)
				continue;
			if (!(obj instanceof StaticObject staticObj))
				return null;
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
			if (obj instanceof Water)
				continue;
			return false;
		}
		return true;
	}

	private boolean canObjectOccupy(GameObject mover, Point2D position) {
		if (!isInside(position))
			return false;
		for (GameObject obj : objectsAt(position)) {
			if (obj instanceof Water || obj == mover)
				continue;
			if (obj instanceof StaticObject staticObj) {
				if (!staticObj.permitePassagem(mover))
					return false;
			} else {
				return false;
			}
		}
		return true;
	}

	

	public void applyGravity() {
		List<StaticObject> movable = new ArrayList<>();
		for (GameObject obj : objects) {
			if (obj instanceof StaticObject staticObj && staticObj.movel()) {
				movable.add(staticObj);
			}
		}

		movable.sort(Comparator.comparingInt((StaticObject o) -> o.getPosition().getY()).reversed());

		for (StaticObject obj : movable) {
			if (!hasSupport(obj)) {
				if (obj instanceof Bomb bomb)
					bomb.setFalling(true);

				Point2D target = obj.getPosition().plus(Direction.DOWN.asVector());
				if (canObjectOccupy(obj, target)) {
					obj.setPosition(target);
				} else if (breakTroncoIfHeavy(obj, target) && canObjectOccupy(obj, target)) {
					obj.setPosition(target);
				} else if (obj instanceof Bomb bomb) {
					if (bomb.wasFalling() && triggerBombCollision(bomb, target))
						continue;
				}
			} else if (obj instanceof Bomb bomb) {
				if (bomb.wasFalling() && handleBombSupport(bomb))
					continue;
				bomb.setFalling(false);
			}
		}

		for (StaticObject obj : movable) {
    if (obj instanceof Buoy buoy) {

        // Verificar se está a suportar um objecto móvel → deve afundar
        if (hasMovableOnTop(buoy)) {
            applyNormalGravity(buoy);
            continue;
        }

        // Tenta subir se não bloqueada
        if (buoy.deveSubir()) {
            Point2D above = buoy.getPosition().plus(Direction.UP.asVector());
            if (canObjectOccupy(buoy, above)) {
                buoy.setPosition(above);
                continue;
            }
        }

        // Caso não consiga subir → segue regra normal (afundar se não tiver suporte)
        applyNormalGravity(buoy);
			}
		}
	}

	private boolean hasSupport(StaticObject obj) {
		Point2D below = obj.getPosition().plus(Direction.DOWN.asVector());
		if (!isInside(below))
			return true;

		for (GameObject other : objectsAt(below)) {
			if (other instanceof Water)
				continue;
			if (other instanceof GameCharacter)
				return true;
			if (other instanceof StaticObject staticObj && staticObj.aguentaPeso())
				return true;
		}

		return false;
	}

	private boolean hasMovableOnTop(StaticObject obj) {
    Point2D above = obj.getPosition().plus(Direction.UP.asVector());
    if (!isInside(above)) return false;

    for (GameObject g : objectsAt(above)) {
        if (g instanceof StaticObject s && s.movel())
            return true;
    }
    return false;
	}

	private void applyNormalGravity(StaticObject obj) {
    	if (!hasSupport(obj)) {
        	Point2D target = obj.getPosition().plus(Direction.DOWN.asVector());
        	if (canObjectOccupy(obj, target)) {
            	obj.setPosition(target);
        	}
    	}
	}

	private boolean triggerBombCollision(Bomb bomb, Point2D target) {
		boolean hitSolid = false;
		for (GameObject other : objectsAt(target)) {
			if (other instanceof Water)
				continue;
			if (other instanceof GameCharacter)
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
			if (obj instanceof Water)
				continue;
			if (obj instanceof GameCharacter)
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
			if (obj instanceof Water)
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

	private boolean breakTroncoIfHeavy(StaticObject mover, Point2D target) {
		if (mover == null || mover.leve())
			return false;

		List<GameObject> targetObjects = new ArrayList<>(objectsAt(target));
		boolean removed = false;
		for (GameObject obj : targetObjects) {
			if (obj instanceof Water)
				continue;
			if (obj instanceof Tronco) {
				removeObject(obj);
				removed = true;
			} else {
				return false;
			}
		}
		return removed;
	}

}

