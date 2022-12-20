package evol.gen;

public enum MapDirection {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;//zgodnie z kierunkiem wskazowek zegara zapisane!
    public String toString(){
        switch(this) {
            case NORTH: return "N";
            case NORTHEAST: return "NE";
            case SOUTH: return "S";
            case SOUTHEAST: return "SE";
            case WEST: return "W";
            case SOUTHWEST: return "SW";
            case EAST: return "E";
            case NORTHWEST: return "NW";
        }
        return "Nieznany kierunek";
    }
    public MapDirection next(){
        MapDirection[] arr = MapDirection.values();
        int nextIndex = (this.ordinal() + 1)%arr.length;
        return arr[nextIndex];
    }
    public MapDirection previous(){
        MapDirection[] arr = MapDirection.values();
        int nextIndex;
        if(this.ordinal() == 0){
            nextIndex = arr.length -1;
        }else{
            nextIndex = (this.ordinal() - 1);
        }
        return arr[nextIndex];
    }
    public Vector2d toUnitVector(){
        switch(this){
            case NORTH: return new Vector2d(0,1);
            case SOUTH: return new Vector2d(0,-1);
            case WEST: return new Vector2d(-1,0);
            case EAST: return new Vector2d(1,0);
            case NORTHEAST: return new Vector2d(1,1);
            case SOUTHEAST: return new Vector2d(1,-1);
            case NORTHWEST: return new Vector2d(-1,1);
            case SOUTHWEST: return new Vector2d(-1,-1);
        }
        return null;
    }

}
