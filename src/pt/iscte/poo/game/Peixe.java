package pt.iscte.poo.game;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.game.GameObject;
import pt.iscte.poo.game.GameEngine;

public abstract class Peixe extends GameObject {

    int movimentos = 0;

    public Peixe(Point2D p, String img) {
        super(p, img);
    }

    public void move(Direction d) {
        Point2D next = position.plus(d.asVector());

        if (GameEngine.getInstance().canMoveTo(next)) {
            this.position = next;
            movimentos++;
        }
    }

    public int getMovimentos() {
        return movimentos;
    }
}