package evol.gen;

import java.util.ArrayList;

public class World {
    public static void run(Direction[] directions){
        for(Direction direction: directions ) {
            switch(direction) {
                case f:
                    System.out.println("Zwierzak idzie do przodu");
                    break;
                case r:
                    System.out.println("Zwierzak skreca w prawo");
                    break;
                case b:
                    System.out.println("Zwierzak idzie do tyłu");
                    break;
                case l:
                    System.out.println("Zwierzak skreca w lewo");
                    break;
                default:
                    System.out.println("Nieznany kierunek");
            }
        }
    }
    public static void main(String[] args) {

        Animal animal = new Animal();
        String slowo = "012394942210";
        System.out.println(animal.mutations(slowo));
        IWorldMap map = new GrassField(10,10,10);
        System.out.println(map);
        Vector2d[] vectors = {new Vector2d(1,2),new Vector2d(3,5),new Vector2d(2,2)};
        IEngine engine = new SimulationEngine(map,vectors);
        engine.run();
    }

}