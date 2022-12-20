package evol.gen;
//import agh.ics.oop.gui.App;
//import javafx.application.Platform;
import java.util.*;


public class SimulationEngine implements IEngine,Runnable {

    private final IWorldMap myMap;
    //private App observer;
    private int moveDelay;
    private final boolean isAppOpening;
    private final Vector2d[] positions;//tylko raz z tego pola korzystamy
    public SimulationEngine(IWorldMap map, Vector2d[] positions, int moveDelay) {
        this.myMap = map;
        this.positions = positions;
        //this.observer = app;
        this.moveDelay = moveDelay;
        this.isAppOpening = true;

        Arrays.stream(positions).forEach(position-> {
            Animal animal = new Animal(this.myMap, position);
            this.myMap.place(animal);//jak zwierze najdzie na inne, to nie zostanie dodane do mapy
        });
        //System.out.println(myMap.toString());
    }

    public SimulationEngine(IWorldMap map, Vector2d[] positions) {
        this.myMap = map; //nasza kochana mapa
        this.positions = positions; //pozycje startowe
        this.isAppOpening =false;

        Arrays.stream(positions).forEach(position-> {
            Animal animal = new Animal(this.myMap, position);
            this.myMap.place(animal);//jak zwierze najdzie na inne, to nie zostanie dodane do mapy
        });
        System.out.println(myMap.toString());
    }

    @Override
    public void run() {
//
//        Symulacja każdego dnia składa się z poniższej sekwencji kroków:
//
//        usunięcie martwych zwierząt z mapy,
//        skręt i przemieszczenie każdego zwierzęcia,
//        konsumpcja roślin na których pola weszły zwierzęta,
//        rozmnażanie się najedzonych zwierząt znajdujących się na tym samym polu,
//        wzrastanie nowych roślin na wybranych polach mapy.



            //mamy listę zwierzątek; teraz musimy uzyskać informację o ich aktywowanym genie:
           //w petli:
                     // tworzymy teraz listę aktywnych genów (animal.activatedGen)
                    //podaną listę aktywnych genów dodajemy do parsera, przeksztalci nam je w MoveDirection)
                    // zwierzątka się zaczynają poruszać (tutaj zajdzie duzo roznych akcji)
           //warunek pętli: (poniewaz wiemy, ze kazdy zwierzak ma taką samą długość genomu, to wybierzmy jeden ze zwierzakow i dla niego robmy warunek)
        int counter = 0;
        int animalCounter = this.myMap.getAnimals().size();
        int index = 0;
        List<Animal> listAnimals = new ArrayList<Animal>(this.myMap.getAnimals().values());
        while(!listAnimals.isEmpty() && counter < listAnimals.get(0).gen.length()){//jesli zwierzakow nie ma to nie ma sensu uruchamiac petli, dzialamy do ostatniego dnia! (tzn. do dlugosci genomu)

            listAnimals = new ArrayList<Animal>(this.myMap.getAnimals().values()); //wazne! to sie bedzie zmieniac! zwierzaki będą dodawane i usuwane
            animalCounter = this.myMap.getAnimals().size();//to sie bedzie zmieniac (zwierzaki mogą być usuwane i dodawane)
            index = 0;//po kazdym dniu wracamy do indeksu zerowego (pierwszego zwierzaka)
            StringBuilder activatedGens = new StringBuilder(); //string aktywnych genow!
            listAnimals.stream().forEach((animal)->{
                activatedGens.append(animal.gen.charAt(animal.activatedGen));
                //gdy to juz zrobimy, dane zwierze zmienia aktywny gen na kolejny u niego na liscie
                animal.nextActivatedGen();

            });

            MoveDirection[] directions = (new OptionsParser()).parse(activatedGens.toString().split(""));
            for(MoveDirection dir : directions){
                Animal animal = listAnimals.get(index);
                animal.move(dir);
                index = (index + 1) % animalCounter;
                System.out.println(this.myMap);
            }
            counter++;
        }
    }

}
