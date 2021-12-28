package agh.ics.oop;


import java.util.Arrays;

public class OptionsParser {
    public static void main (String[] args){
        String[] moves = {"f", "a", "b", "r",  "FOWARD", "f"};
        Vector2d.MoveDirection[] moveSteps = parse(moves);
        //System.out.println(moveSteps.toString());
        for (int i=0 ; i< moveSteps.length ; i++){
            System.out.println(moveSteps[i].toString());
        }
        System.out.println("aaa");

        String [] argsnew = new String[]{ "LEFT", "l" ,"aa", "f","KKK" , "FOWARD","r","bal","b"};
        Vector2d.MoveDirection[] movesOfAnimal = OptionsParser.parse(argsnew);

        for (int i =0 ; i<movesOfAnimal.length; i++){
            System.out.println(movesOfAnimal[i].toString());
        }


    }
    public static Vector2d.MoveDirection[] parse(String[] moves){
        Vector2d.MoveDirection[] newMoves = new Vector2d.MoveDirection[moves.length];
        int i = 0;
        int x = 0;
        while(i<moves.length){
            switch (moves[i]){
                case "b":
                case "BACKWARD":
                    newMoves[x] =  Vector2d.MoveDirection.BACKWARD;
                    x++;
                    break;
                case "f":
                case "FOWARD":
                    newMoves[x] = Vector2d.MoveDirection.FOWARD;
                    x++;
                    break;
                case "l":
                case  "LEFT":
                    newMoves[x] = Vector2d.MoveDirection.LEFT;
                    x++;
                    break;
                case "r":
                case "RIGHT":
                    newMoves[x] = Vector2d.MoveDirection.RIGHT;
                    x++;
                    break;
            }
            i++;
        }

        return Arrays.copyOfRange(newMoves, 0, x);
    }
}
