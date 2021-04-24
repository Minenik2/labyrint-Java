import java.util.ArrayList;

public class SortRute extends Rute {

    SortRute(int setX, int setY, Labyrint setLabyrint) {
        super(setX, setY, setLabyrint);
    }

    @Override
    public String tilTegn() {
        return "#";
    }

    @Override
    public void gaa(Rute currentRute, ArrayList<Tuppel> utveien) {
        // ingenting fordi svart rute
    }
}
