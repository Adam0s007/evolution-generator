package evol.gen;
import java.util.*;

import static java.lang.Math.random;

public class GrassField extends AbstractWorldMap{
    int rownik;
    public final int width;
    public final int height;

    private HashMap<Vector2d,Grass> grasses = new HashMap<>();


    public GrassField(int width,int height,int n, boolean isItDeathField){
        super(new Vector2d(width-1,height-1));
        this.width = width;
        this.height = height;
        this.deathfield.put(new Vector2d(0,0), 0);
        this.rownik= (int)((height-1)/5) + 1;
        this.isItDeathField = isItDeathField;
        this.createProperNumberOfGrass(n);
    }

    //public void createGrasses(int n){ //definiuje ile mozna stworzyc nowych trawek i randomowo je umeiszcza tam gdzie nie ma obiektow
    //    for(int i =0; i < n; i++){
    //        createEnoughGrasses(isItDeathField);
    //    }
    //}

    public Vector2d getRandomVectorFromMap(){
        int randomX = (int)(random() * (width));
        int randomY = (int)(random() * (height));
        return new Vector2d(randomX, randomY);
    }

    public Vector2d getRandomVectorFromEquator(){
        int randomX = (int)(random() * (width));
        int randomY = (int)(random() * (rownik)) + (int)(height/2) -1;
        return new Vector2d(randomX, randomY);
    }

    private boolean canPlaceGrassInEquator(){
        for (int i=0; i<width; i++){
            for (int j=((int)(width/2) -1); j<((int)(width/2) -1+rownik); j++){
                if (grassAt(new Vector2d(i,j))==null){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canMakeNewGrass(){
        return this.grasses.size() < height*width;
    }


    // W App trzeba się upewnić że dostajemy ilość traw mniejszą bądź równą ilości miejsc na mapie
    public void createEnoughGrasses(boolean isItDeathField){
        if (!isItDeathField){
                if (!canMakeNewGrass()) { return;}
                int oneInFive = (int)(random()*(5));
                if (oneInFive==0 || !canPlaceGrassInEquator()){
                    while (true) {
                        Vector2d zmienna = this.getRandomVectorFromMap();
                        if (grassAt(zmienna) == null) {
                            grasses.put(zmienna, new Grass(zmienna));
                            break;
                        }
                    }
                }
                else {
                    while (true) {
                        Vector2d zmienna = this.getRandomVectorFromEquator();
                        if (grassAt(zmienna) == null) {
                            grasses.put(zmienna, new Grass(zmienna));
                            break;
                        }
                    }
                }

        }
        else{
            if (!canMakeNewGrass()) { return;}
                int oneInFive = (int)(random()*(5));
                if (oneInFive==0){
                    while (true) {
                        Vector2d zmienna = this.getRandomVectorFromMap();
                        if (grassAt(zmienna) == null) {
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

                        if (grassAt(zmienna) == null && deathfield.get(zmienna) <= Collections.min(deathfield.values()))
                        {
                            grasses.put(zmienna, new Grass(zmienna));
                            break;
                        }
                        else{
                            Vector2d pos = null;
                            for (Vector2d dead : deathfield.keySet()) {
                                int minim = Collections.max(deathfield.values());
                                if (deathfield.get(dead) <= minim && grassAt(dead) == null){
                                    minim = deathfield.get(dead);
                                    pos = dead;
                                }
                            }
                            if (pos != null) {
                                grasses.put(pos, new Grass(pos));
                                break;
                            }
                        }
                    }
                }
        }
    }
    public void createProperNumberOfGrass(int n){
        if (n==-1) {
            for (int i = 0; i < this.newGrasses; i++) {
                this.createEnoughGrasses(this.isItDeathField);
            }
        }
        else{
            for(int i =0; i < n; i++){
                createEnoughGrasses(isItDeathField);
            }
        }
    }

    public Grass grassAt(Vector2d position){
        return grasses.get(position);
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


    public HashMap<Vector2d,PriorityQueue<Animal>> getAnimals(){ return animals;}
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

    public int freePlaces(){
        return this.width*this.height - this.animals.size();
    }

    public int emptyFields(){
        int quantity = 0;
        HashSet<Vector2d> usedMap = new HashSet();
        for (Vector2d key : this.animals.keySet()){
            if(usedMap.add(key)){
             quantity++;
            }
        }
        for(Animal animal: this.temporaryAnimalsArray){
            if(usedMap.add(animal.position)){
                quantity++;
            }
        }
        for(Vector2d key : this.grasses.keySet()){
            if(usedMap.add(key)){
                quantity++;
            }
        }

        return this.width*this.height - quantity;
    }

}
