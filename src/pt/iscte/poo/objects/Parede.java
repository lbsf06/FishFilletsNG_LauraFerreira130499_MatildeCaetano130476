package pt.iscte.poo.objects;

import pt.iscte.poo.utils.Point2D;

public abstract class Parede extends StaticObject { // classe abstrata que extende StaticObject

    public Parede(Point2D p, String img) {
        super(p, img); // chama o construtor da superclasse StaticObject
    }

    @Override
    public boolean podeAtravessar(GameCharacter character) { // metodo herdado de GameObject, default é false
        return false;
    }

    @Override
    public int getLayer() { // metodo herdado de GameObject, default é 1
        return 1;
    }
}

// Serve para permitir a criação de mais tipos de paredes conferindo
// flexibilidade ao código.