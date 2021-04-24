import java.util.ArrayList;

public class Aapning extends SortRute {

    Aapning(int setX, int setY, Labyrint setLabyrint) {
        super(setX, setY, setLabyrint);
    }

    @Override
    public String tilTegn() {
        return "O";
    }

    @Override
    public void gaa(Rute currentRute, ArrayList<Tuppel> utveien) {
        //System.out.println("UTVEI! på " + kolonneX + ", " + radY);
        utveien.add(new Tuppel(kolonneX, radY));
        minLabyrint.leggTilUtvei(utveien);
    }
}
