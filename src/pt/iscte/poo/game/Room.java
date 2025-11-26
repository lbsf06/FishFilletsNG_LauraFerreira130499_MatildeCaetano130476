package pt.iscte.poo.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import objects.water;
import objects.BigFish;
import objects.GameCharacter;
import objects.GameObject;
import objects.ParedeComBuraco;
import objects.ParedeNormal;
import objects.SmallFish;
import objects.Tronco;
import objects.StaticObject;
import objects.TuboDeAco;
import objects.anchor;
import objects.bomb;
import objects.cup;
import objects.stone;
import objects.trap;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.utils.Direction;

public class Room {

	private List<GameObject> objects;
	private String roomName;
	private int width;
	private int height;

	private SmallFish sf;
	private BigFish bf;

	public Room() {
		objects = new ArrayList<GameObject>();
	}

	private void setName(String name) {
		roomName = name;
	}

	public String getName() {
		return roomName;
	}

	public void addObject(GameObject obj) {
		objects.add(obj);
		ImageGUI.getInstance().addImage(obj);
	}

	public void removeObject(GameObject obj) {
		objects.remove(obj);
		ImageGUI.getInstance().removeImage(obj);
	}

	public SmallFish getSmallFish() {
		return sf;
	}

	public BigFish getBigFish() {
		return bf;
	}

	public void setSmallFish(SmallFish sf) {
		this.sf = sf;
	}

	public void setBigFish(BigFish bf) {
		this.bf = bf;
	}

	public static Room readRoom(File f) {

		Room r = new Room();
		r.setName(f.getName());

		List<String> linhas = new ArrayList<>();

		try (Scanner sc = new Scanner(f)) {
			while (sc.hasNextLine()) {
				linhas.add(sc.nextLine());
			}
		} catch (FileNotFoundException e) {
			System.err.println("ERRO: ficheiro " + f.getName() + " nÃ£o encontrado!");
			return r;
		}

		// 1Âº: preencher tudo com Ã¡gua
		int altura = linhas.size();
		int largura = linhas.stream().mapToInt(String::length).max().orElse(0);
		r.height = altura;
		r.width = largura;

		for (int y = 0; y < altura; y++) {
			for (int x = 0; x < largura; x++) {
				GameObject agua = new water(new Point2D(x, y));
				r.addObject(agua);
			}
		}

		// 2Âº: adicionar objetos do ficheiro por cima da Ã¡gua
		for (int y = 0; y < altura; y++) {
			String linha = linhas.get(y);

			for (int x = 0; x < largura; x++) {
				char c = x < linha.length() ? linha.charAt(x) : ' ';
				Point2D p = new Point2D(x, y);

				GameObject obj = null;

				switch (c) {
				case 'W' -> obj = new ParedeNormal(p);
				case 'X' -> obj = new ParedeComBuraco(p);
				case 'Y' -> obj = new Tronco(p);
				case 'H' -> obj = new TuboDeAco(p, true);
				case 'V' -> obj = new TuboDeAco(p, false);
				case 'C' -> obj = new cup(p);
				case 'R' -> obj = new stone(p);
				case 'A' -> obj = new anchor(p);
				case 'b' -> obj = new bomb(p);
				case 'T' -> obj = new trap(p);
				case 'B' -> {
					BigFish bf = new BigFish(p);
					r.setBigFish(bf);
					obj = bf;
				}
				case 'S' -> {
					SmallFish sf = new SmallFish(p);
					r.setSmallFish(sf);
					obj = sf;
				}
				case ' ' -> obj = null; // jǭ tem ǭgua
			}

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

		if (!canEnter(character, current, target))
			return false;

		character.move(dir);
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
			if (obj == character || obj instanceof water)
				continue;

			if (obj instanceof GameCharacter)
				return false;

			if (obj instanceof StaticObject staticObj) {
				if (!staticObj.podeAtravessar(character))
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

}

