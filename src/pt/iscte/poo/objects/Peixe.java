package pt.iscte.poo.objects;

import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import pt.iscte.poo.game.GameEngine;

public abstract class Peixe extends GameObject {

    protected String leftImg;   // imagem virado à esquerda
    protected String rightImg;  // imagem virado à direita

    public Peixe(Point2D p, String leftImg, String rightImg) {
        super(p, leftImg);    // começa virado para a esquerda
        this.leftImg = leftImg;
        this.rightImg = rightImg;
    }

    public void move(Direction d) {
        Point2D next = position.plus(d.asVector());

        if (GameEngine.getInstance().canMoveTo(next)) {
            position = next;

            // muda a imagem consoante a direção
            if (d == Direction.RIGHT)
                imageName = rightImg;
            else if (d == Direction.LEFT)
                imageName = leftImg;
        }
    }
}
