package evol.gen;

public class Grass extends AbstractWorldMapElement{

    public Grass(Vector2d position){
        super(position);
    }

    public String toString(){
        return "*";
    }

    public int getEnergy(){
        return 0;
    }


}
