public class Tuppel {
    int kolonneX;
    int radY;

    Tuppel(int setX, int setY) {
        kolonneX = setX;
        radY = setY;
    }

    @Override
    public String toString() {
        return "(" + kolonneX + "," + radY + ")";
    }

    public int getX() {
        return kolonneX;
    }

    public int getY() {
        return radY;
    }
}
