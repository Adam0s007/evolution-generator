package evol.gen;

abstract class AbstractWorldMapElement implements IMapElement{
    public Vector2d position;

    public AbstractWorldMapElement(Vector2d position){
        this.position = position;
    }

    public Vector2d getPosition(){
        return this.position;
    }
    public void setPosition(Vector2d position){
        this.position = position;
    }
    public boolean isAt(Vector2d position1){
        return position.equals(position1);
    }
//case NORTH: return "N";
//            case NORTHEAST: return "!";
//            case SOUTH: return "S";
//            case SOUTHEAST: return ")";
//            case WEST: return "W";
//            case SOUTHWEST: return "/";
//            case EAST: return "E";
//            case NORTHWEST: return "@";
    public String getLinkToImage(){
        String result;
        switch(this.toString()){
            case ("N") -> result = "src/main/resources/animalnorth.png";
            case ("!") -> result ="src/main/resources/animalnortheast.png";
            case (")") -> result ="src/main/resources/animalsoutheast.png";
            case ("/") -> result ="src/main/resources/animalsouthwest.png";
            case ("@") -> result ="src/main/resources/animalnorthwest.png";
            case ("E") -> result = "src/main/resources/animaleast.png";
            case ("S") -> result = "src/main/resources/animal.png";
            case ("W") -> result = "src/main/resources/animalwest.png";
            case ("*") -> result = "src/main/resources/grass.png";
            default -> result = null;
        }
        return result;
    }
}