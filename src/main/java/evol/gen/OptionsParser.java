package evol.gen;
import java.util.*;

public class OptionsParser {
    public MoveDirection[] parse(String[] args){
        List<MoveDirection> Move_direction_list = new ArrayList<MoveDirection>();
        Arrays.stream(args).forEach(elem -> {
                if (elem.equals("0")) {
                    Move_direction_list.add(MoveDirection.rotate0);
                } else if (elem.equals("1")) {
                    Move_direction_list.add(MoveDirection.rotate1);
                } else if (elem.equals("2")) {
                    Move_direction_list.add(MoveDirection.rotate2);
                } else if (elem.equals("3")) {
                    Move_direction_list.add(MoveDirection.rotate3);
                }else if (elem.equals("4")) {
                    Move_direction_list.add(MoveDirection.rotate4);
                }else if (elem.equals("5")) {
                    Move_direction_list.add(MoveDirection.rotate5);
                }else if (elem.equals("6")) {
                    Move_direction_list.add(MoveDirection.rotate6);
                }else if (elem.equals("7")) {
                    Move_direction_list.add(MoveDirection.rotate7);
                }
        });
        int leng = Move_direction_list.size();
        return Move_direction_list.toArray(new MoveDirection[leng]);
    }
}
