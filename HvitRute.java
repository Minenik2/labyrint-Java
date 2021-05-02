import java.util.ArrayList;

public class HvitRute extends Rute {

    HvitRute(int setX, int setY, Labyrint setLabyrint) {
        super(setX, setY, setLabyrint);
    }

    @Override
    public String tilTegn() {
        return ".";
    }

    @Override
    public void gaa(Rute currentRute, ArrayList<Tuppel> utveien) {
        // System.out.println("Vi er på " + kolonneX + ", " + radY);
        ArrayList<Tuppel> nyUtveien = new ArrayList<>(utveien);
        nyUtveien.add(new Tuppel(kolonneX, radY));
        currentRute.setVisited(true);
        for (int i = 0; i < naboRuter.size(); i++) {
            if (currentRute != naboRuter.get(i) && !naboRuter.get(i).getVisited()) {
                naboRuter.get(i).gaa(this, nyUtveien);
            }
        }
        currentRute.setVisited(false);
    }
}
