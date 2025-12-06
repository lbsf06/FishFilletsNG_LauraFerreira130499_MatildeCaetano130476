package pt.iscte.poo.objects;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public abstract class GameObject implements ImageTile { // O imageTile faz com que para obtermos a imagem tenhamos de
                                                        // declarar a posição do objeto e o nome da imagem

    protected Point2D position; // As subclasses podem aceder a estes atributos
    protected String imageName;

    public GameObject(Point2D p) { // O point2D p é a posição do objeto inicial
        this.position = p;

    }

    public void setPosition(Point2D p) { // Método para alterar a posição do objeto
        this.position = p;
    }

    public abstract TipoObjeto getTipo();// Método abstrato que obriga as subclasses a implementar o método getTipo, que
                                         // retorna o tipo do objeto

    @Override // sobrescreve o método getName da interface ImageTile
    public String getName() { // Método getter da classe abstrata ImageTile que serve para obter o nome da
                              // imagem do objeto
        // A GUI vai chamar isto para saber que imagem desenhar.
        return imageName;
    }

    @Override // sobrescreve o método getPosition da interface ImageTile
    public Point2D getPosition() { // Método getter da classe abstrata ImageTile que serve para obter a posição do
                                   // objeto
        return position; // A GUI desenha a imagem aqui.
    }

    @Override // sobrescreve o método getLayer da interface ImageTile
    public int getLayer() { // Método da classe abstrata ImageTile
        return 1;
    }
}
