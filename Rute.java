import java.util.ArrayList;

public abstract class Rute {
    int kolonneX;
    int radY;
    Labyrint minLabyrint;
    ArrayList<Rute> naboRuter = new ArrayList<>(); // bruker arraylist fordi det er enklere
    boolean visited = false;

    Rute(int setX, int setY, Labyrint setLabyrint) {
        kolonneX = setX;
        radY = setY;
        minLabyrint = setLabyrint;
    }

    public abstract String tilTegn();

    public abstract void gaa(Rute currentRute, ArrayList<Tuppel> utveien);

    public void leggTilNabo(Rute ruteObj) {
        naboRuter.add(ruteObj);
    }

    public void finnUtvei() {
        gaa(this, new ArrayList<>());
    }

    public boolean getVisited() {
        return visited;
    }

    public void setVisited(boolean setBoolean) {
        visited = setBoolean;
    }

}
