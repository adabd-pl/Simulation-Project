package agh.ics.oop;

import java.util.Objects;

public class Vector2d  {
    public static void main(String[] args) {

    }
    final public int x;
    final public int y;

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public  String toString() {
        String positionString = "(" + this.x + "," + this.y + ")";
        return positionString;
    }



    public  boolean precedes(Vector2d other) {
        if ((this.x <= other.x) && (this.y <= other.y)){
            return true;
        }
        return false;
    }
    public Vector2d add(Vector2d other){
        Vector2d newVector = new Vector2d(this.x + other.x,this.y + other.y);
        return newVector;
    }
    public boolean follows(Vector2d other){
        if ((this.x >= other.x) && (this.y >= other.y)){
            return true;
        }
        return false;
    }

    public Vector2d upperRight(Vector2d other){
        int newX = Math.max(this.x, other.x);
        int newY = Math.max(this.y , other.y);
        Vector2d newVector = new Vector2d(newX, newY);
        return newVector;
    }
    public Vector2d lowerLeft(Vector2d other){
        int newX = Math.min(this.x, other.x);
        int newY = Math.min(this.y , other.y);
        Vector2d newVector = new Vector2d(newX, newY);
        return newVector;
    }
    public Vector2d subtract(Vector2d other){
        int newX = this.x - other.x;
        int newY = this.y - other.y;
        Vector2d newVector = new Vector2d(newX, newY);
        return newVector;
    }

    public boolean equals(Object other){
        if (this == other)
            return true;
        if (!(other instanceof Vector2d))
            return false;

        Vector2d that = (Vector2d) other;
        if (this.x == that.x && this.y == that.y)
            return true;
        return false;
    }

    public Vector2d opposite(){
        Vector2d newVector = new Vector2d(this.x*(-1) , this.y*(-1));
        return newVector;
    }

    public enum MoveDirection {
        FOWARD,
        BACKWARD,
        RIGHT,
        LEFT
    }

    public enum MapDirection{
        NORTH,
        SOUTH,
        WEST,
        EAST,
        NORTHEAST,
        NORTHWEST,
        SOUTHEAST,
        SOUTHWEST;

        public String toString(){
            switch (this) {
                case WEST:
                    return "W -zachód";

                case SOUTH:
                    return "S -południe";

                case EAST:
                    return "E -wschód";

                case NORTH:
                    return "N -północ";

                case NORTHEAST:
                    return "NE -północny-wschód";
                case NORTHWEST:
                    return "NW -północny-zachód";

                case SOUTHEAST:
                    return "SE - południowy-wschód";
                case SOUTHWEST:
                    return "SW - południowy-zachód";
            }
            return "";
        }

        public MapDirection next(){
            switch (this) {
                case WEST:
                    return MapDirection.NORTHWEST;

                case SOUTH:
                    return MapDirection.SOUTHWEST;

                case EAST:
                    return MapDirection.SOUTHEAST;

                case NORTH:
                    return MapDirection.NORTHEAST;

                case NORTHWEST:
                    return MapDirection.NORTH;

                case SOUTHWEST:
                    return MapDirection.WEST;

                case NORTHEAST:
                    return MapDirection.EAST;

                case SOUTHEAST:
                    return MapDirection.SOUTH;

            }
            return null;
        }

        public MapDirection previous(){
            switch (this) {
                case WEST:
                    return MapDirection.SOUTH;

                case SOUTH:
                    return MapDirection.EAST;

                case EAST:
                    return MapDirection.NORTH;

                case NORTH:
                    return MapDirection.WEST;

            }
            return null;
        }

        public Vector2d toUnitVector(){
            switch (this) {
                case WEST:
                    return new Vector2d(-1,0);

                case SOUTH:
                    return new Vector2d(0,-1);

                case EAST:
                    return new Vector2d(1,0);

                case NORTH:
                    return new Vector2d(0,1);

            }
            return new Vector2d(0,0);
        }
    }
    @Override
    public int hashCode(){
        return Objects.hash(this.x, this.y);
    }

}
