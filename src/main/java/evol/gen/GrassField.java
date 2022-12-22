package evol.gen;
import java.util.*;

import static java.lang.Math.random;

public class GrassField extends AbstractWorldMap{
    int n;
    int rownik;
    private final int width;
    private final int height;

    private HashMap<Vector2d,Grass> grasses = new HashMap<>();

    private HashMap<Vector2d, Integer> deathfield = new HashMap<>();
    public GrassField(int width,int height,int n){
        super(new Vector2d(width,height));//nie wplynie na wielkosc mapy w klasie nadrzednej!
        this.n = n;
        this.width = width;
        this.height = height;
        this.createGrasses(this.n);
        this.deathfield.put(new Vector2d(0,0), 0);
        this.rownik= (int)(height/5) + 1;
    }

    public void createGrasses(int n){ //definiuje ile mozna stworzyc nowych trawek i randomowo je umeiszcza tam gdzie nie ma obiektow
        for(int i = 0; i < n; i++){
            Vector2d newVec = uniqPosVector(new Vector2d(topRight.x,topRight.y));
            grasses.put(newVec,new Grass(newVec));

        }
    }

    public Vector2d getRandomVectorFromMap(){
        int randomX = (int)(random() * (width));
        int randomY = (int)(random() * (height));
        return new Vector2d(randomX, randomY);
    }

    public Vector2d getRandomVectorFromEquator(){
        int randomX = (int)(random() * (width));
        int randomY = (int)(random() * (rownik)) + (int)((width-1)/2);
        return new Vector2d(randomX, randomY);
    }


    // W App trzeba się upewnić że dostajemy ilość traw mniejszą bądź równą ilości miejsc na mapie
    public void createEnoughGrasses(boolean isItDeathField){
        if (!isItDeathField){
            while (grasses.size()<n) {
                int oneInFive = (int)(random()*(5));
                if (oneInFive==0){
                    while (true) {
                        Vector2d zmienna = this.getRandomVectorFromMap();
                        if (!(objectAt(zmienna) instanceof Grass)) {
                            grasses.put(zmienna, new Grass(zmienna));
                            break;
                        }
                    }
                }
                else {
                    while (true) {
                        Vector2d zmienna = this.getRandomVectorFromEquator();
                        if (!(objectAt(zmienna) instanceof Grass)) {
                            grasses.put(zmienna, new Grass(zmienna));
                            break;
                        }
                    }
                }
            }
        }
        else{
            while (grasses.size()<n) {
                int oneInFive = (int)(random()*(5));
                if (oneInFive==0){
                    while (true) {
                        Vector2d zmienna = this.getRandomVectorFromMap();
                        if (!(objectAt(zmienna) instanceof Grass)) {
                            grasses.put(zmienna, new Grass(zmienna));
                            break;
                        }
                    }
                }
                else {
                    while (true) {
                        Vector2d zmienna = this.getRandomVectorFromMap();
                        if (!deathfield.containsKey(zmienna)){
                            deathfield.put(zmienna,0);
                        }
                        Collections.min(deathfield.values());
                        if (!(objectAt(zmienna) instanceof Grass) && deathfield.get(zmienna) <= Collections.min(deathfield.values()))
                        {
                            grasses.put(zmienna, new Grass(zmienna));
                            break;
                        }
                    }
                }
            }
        }
    }


    public boolean isOccupied(Vector2d position) {
        if(super.isOccupied(position))return true;//jesli tam jest zwierzę to zwroc true
        return grasses.containsKey(position); // jesli tam jest roslina zwroc true
        //zwroc false w przeciwnym wypadku
    }


    public Object objectAt(Vector2d position){                        //zwroci obiekt lub null (jesli nie znalezione)
        return (super.objectAt(position) != null) ?//najpierw sprawdzi czy tam jest zwierze
                super.objectAt(position) : // po pierwsze jesli jest tam zwierzę to zwroci to zwierze, jesli nie to trawe!
                grasses.get(position);
    }

    @Override
    public HashMap<Vector2d, Animal> getAnimals() {
        return null;
    }


    public HashMap<Vector2d, Grass> getGrasses() {
        return grasses;
    }

    public void positionChangedGrass(Vector2d oldPosition, Vector2d newPosition){
        Grass grass = this.grasses.get(oldPosition);
        this.grasses.remove(oldPosition);
        this.grasses.put(newPosition, grass);
    }

    protected void eatingGrass(Vector2d vector){
        if(super.getNumberOfAnimalsAtPosition(vector) > 0){
            this.grasses.remove(vector);
            super.eatingGrass(vector);
        }
    }

    protected void eatingTime(){
        ArrayList<Vector2d> originalVectorki= new ArrayList<>();
        Set<Vector2d> keys = this.grasses.keySet();
        originalVectorki.addAll(keys);
        originalVectorki.forEach(vector->{
           this.eatingGrass(vector);
        });
    }


    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        return;
    }
}
