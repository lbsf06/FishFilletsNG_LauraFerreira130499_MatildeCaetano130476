package pt.iscte.poo.game;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import objects.Water;
import objects.BigFish;
import objects.GameObject;
import objects.SmallFish;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Point2D;

public class Room {
	
	private List<GameObject> objects;
	private String roomName;
	
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
		
		// TODO
		
		Room r = new Room();
		r.setName(f.getName());
		
		GameObject water = new Water(new Point2D(0, 0));
		r.addObject(water);
		
		BigFish bf = new BigFish(new Point2D(2, 2));
		r.setBigFish(bf);
		r.addObject(bf);
		
		SmallFish sf = new SmallFish(new Point2D(3, 3));
		r.setSmallFish(sf);
		r.addObject(sf);
		
		return r;
		
	}
	
}