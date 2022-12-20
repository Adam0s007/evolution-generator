package evol.gen;

import java.util.*;
import java.util.HashMap;

import java.util.concurrent.ThreadLocalRandom;
public class Animal extends AbstractWorldMapElement{

    private IWorldMap myMap;
    protected Random randomizer = ThreadLocalRandom.current();
    public int energy = 0; //ilosc energii jaką zwierzę posiada na mapie
    public String gen = ""; //gen danego zwierzaka

    public int activatedGen = 0; // mowi o tym, ktory gen jest aktywny danego dnia

    public int fullRandomness = 0; //WARIANT DO ZAZNACZENIA!

    public int parentsCounter = 0;
    protected final List<IPositionChangeObserver> observers = new ArrayList<>();
    private MapDirection orientation =  MapDirection.NORTH;
    //w konstruktorach przypisujemy zwierze do danej mapy, ale nie umieszczamy zwierzecia na tej mapie!!

    public Animal(IWorldMap map, Vector2d initialPosition){
            super(initialPosition);
            this.myMap = map;
            this.initialGen();
    }
    public String createNewGen(Animal Animal1,Animal Animal2){
        int totalEnergy = Animal1.energy + Animal2.energy;
        // nowy zwierzak ma otrzymac probabilityOfFirst*100 % genow Animal1 oraz probabilityOfSec*100% genow Animal2
        //  losowa liczba (wybranych również losowo) genów zmienia swoj stan na inny!-  mutacje
        String leftPart = this.parentRandomGen(Animal1.energy/totalEnergy,Animal1.gen);
        String rightPart = this.parentRandomGen(Animal2.energy/totalEnergy,Animal2.gen);
        StringBuilder sb = new StringBuilder();
        sb.append(leftPart);
        sb.append(rightPart);
        while(sb.toString().length() < Animal1.gen.length()) sb.append("0");
        String newGen = sb.toString();
        return this.mutations(newGen);
    }
    public Animal(IWorldMap map, Vector2d initialPosition,Animal Animal1, Animal Animal2){
        super(initialPosition);
        this.myMap = map;
        this.createNewGen(Animal1,Animal2);
    }

    public String parentRandomGen(double probability,String sequence){ // O(n)
        int numChars = (int) Math.floor(sequence.length() * probability);
        long seed = System.currentTimeMillis(); // Use the current time as the seed
        Random rand = new Random(seed);
        StringBuilder newGen = new StringBuilder();
        HashMap<Character, Boolean> selectedChars = new HashMap<>();
        for (int i = 0; i < numChars; i++) {
            int index = rand.nextInt(sequence.length());
            char c = sequence.charAt(index);
            if (!selectedChars.containsKey(c)) {
                newGen.append(c);
                selectedChars.put(c, true);
            } else {
                i--; // If the character has already been selected, decrement i to try again
            }
        }

        return newGen.toString(); // Print the result
    }

    public void initialGen(){
        int length = ((GrassField)this.myMap).genLimit; // desired length of the string
        StringBuilder sb = new StringBuilder();
        long seed = System.currentTimeMillis(); // Use the current time as the seed
        Random rand = new Random(seed);

        for (int i = 0; i < length; i++) {
            int randomInt = rand.nextInt(8); // generate a random number between 0 and 7
            char randomChar = (char)('0' + randomInt); // convert the random number to a character
            sb.append(randomChar); // add the character to the string
        }
        this.gen = sb.toString();
    }
    public String mutations(String gen){
        HashMap<Integer, Character> selectedChars = new HashMap<>(); //(pozycja, nowy znak)

        StringBuilder newGen = new StringBuilder();

        long seed = System.currentTimeMillis(); // Use the current time as the seed
        Random rand = new Random(seed);

        int quantity = rand.nextInt(gen.length());
        for(int i =0; i < quantity; i++){
            int index = rand.nextInt(gen.length());//losowany index do zamiany
            while(selectedChars.containsKey(index)) index = rand.nextInt(gen.length()); //ma znalezc inny ten index!
            char c = gen.charAt(index);
            char newChar;
            if(this.fullRandomness == 1){
                newChar = (char)(rand.nextInt(8) + '0');
                while(c == newChar) newChar = (char)(rand.nextInt(8) + '0');
            }
            else{
                int r = rand.nextInt(2);
                if (r == 0) {
                    // Decrease the char value
                    if (c == '7') {
                        newChar = '6';
                    } else if (c == '0') {
                        newChar = '7';
                    } else {
                        c--;
                        newChar = c;
                    }
                } else {
                    // Increase the char value
                    if (c == '7') {
                        newChar = '0';
                    } else if (c == '0') {
                        newChar = '1';
                    } else {
                        c++;
                        newChar = c;
                    }
                }
            }
            selectedChars.put(index,newChar);
        }
        for(int i =0; i < gen.length();i++){
            if(selectedChars.containsKey(i)) newGen.append(selectedChars.get(i));
            else newGen.append(gen.charAt(i));
        }
        //System.out.println(selectedChars);
        return newGen.toString();
    }


    public Animal(){
        super(new Vector2d(2,2));
        this.myMap = new RectangularMap(4, 4);
    }

    public String toString(){
        return orientation.toString();
    }

    public String extendedToString(){
        return position.toString() + " " + orientation.toString();
    }

    public void nextActivatedGen(){
        this.activatedGen = (this.activatedGen + 1) %this.gen.length();
    }

    public void move(MoveDirection direction0){
        switch(direction0){
            case rotate0:
                Vector2d newVectorek0 = position.add(orientation.toUnitVector());
                if(myMap instanceof GrassField && updatePositions(newVectorek0)) break;//sprawdzanie czy tam jest trawa
                else if(myMap.canMoveTo(newVectorek0)){
                    positionChanged(position, newVectorek0);
                    this.position = newVectorek0;
                }
                break;
            case rotate1:
                rotatingOrientation(1);
                break;
            case rotate2:
                rotatingOrientation(2);
                break;
            case rotate3:
                rotatingOrientation(3);
                break;
            case rotate4:
                rotatingOrientation(4);
                break;
            case rotate5:
                rotatingOrientation(5);
                break;
            case rotate6:
                rotatingOrientation(6);
                break;
            case rotate7:
                rotatingOrientation(7);
                break;
            default:
                System.out.println("Nieznany kierunek");
                }
        }
    public void rotatingOrientation(int range){
        for(int i=0;i<range;i++){
            orientation = orientation.next();
        }
    }


    public MapDirection getDirection() {
        return orientation;
    }
    public boolean updatePositions(Vector2d newVectorek0){
        GrassField mapa = (GrassField) this.myMap;
        if(mapa.objectAt(newVectorek0) instanceof Grass){
            //nowe polozenie trawy:
            Vector2d newVec = mapa.uniqPosVector(new Vector2d(mapa.boundary,mapa.boundary));
            Vector2d oldVec = ((Grass) mapa.objectAt(newVectorek0)).getPosition();
            ((Grass) mapa.objectAt(newVectorek0)).setPosition(newVec);
            mapa.positionChangedGrass(oldVec,newVec);
            //nowe polozenie zwierzaka
            mapa.positionChanged(position, newVectorek0);
            this.position = newVectorek0;

            return true;
        }
        return false;
    }


    public void addObserver(IPositionChangeObserver observer){
        this.observers.add(observer);
    }

    public void removeObserver(IPositionChangeObserver observer){
        this.observers.remove(observer);
    }

    protected void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        for(IPositionChangeObserver observer : this.observers){ //dla wszystkich obserwowanych map trzeba zrobic zmiane!
            observer.positionChanged(oldPosition, newPosition);
        }
    }

}

