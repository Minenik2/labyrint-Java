import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Labyrint {
    protected Rute[][] labyrintArray;
    private ArrayList<ArrayList<Tuppel>> utveier = new ArrayList<>();

    @Override
    public String toString() {
        String labyrintString = "";
        for (int i = 0; i < labyrintArray.length; i++) {
            for (int j = 0; j < labyrintArray[i].length; j++) {
                labyrintString = labyrintString.concat("" + labyrintArray[i][j].tilTegn());
            }
            labyrintString = labyrintString.concat("\n");
        }
        return labyrintString;
    }

    public void lesFraFil(File file) throws FileNotFoundException {
        Scanner scan = new Scanner(file);
        // det er mulig å bruke arraylist istedenfor array slik at man ikke trenger å
        // vise lengden på labyrinten på starten av filen, men jeg føler at oppgaven vil
        // ha bruk av array.
        labyrintArray = new Rute[Integer.parseInt(scan.next())][Integer.parseInt(scan.next())];
        scan.nextLine();

        // denne for løkken kan være en rekursjon i teori men jeg føler det
        // overkompliserer koden
        for (int i = 0; scan.hasNextLine(); i++) {
            String linje = scan.nextLine();

            for (int j = 0; j < linje.length(); j++) {
                if (linje.charAt(j) == '#') {
                    labyrintArray[i][j] = new SortRute(j, i, this);
                } else {
                    if (i == 0 || i == labyrintArray.length - 1 || j == 0 || j == labyrintArray[i].length - 1) {
                        labyrintArray[i][j] = new Aapning(j, i, this);
                    } else {
                        labyrintArray[i][j] = new HvitRute(j, i, this);
                    }
                }

                if (j > 0) {
                    labyrintArray[i][j - 1].leggTilNabo(labyrintArray[i][j]);
                    labyrintArray[i][j].leggTilNabo(labyrintArray[i][j - 1]);
                }
                if (i > 0) {
                    labyrintArray[i - 1][j].leggTilNabo(labyrintArray[i][j]);
                    labyrintArray[i][j].leggTilNabo(labyrintArray[i - 1][j]);
                }
            }
        }
        scan.close();
    }

    public ArrayList<ArrayList<Tuppel>> finnUtveiFra(int kol, int rad) {
        // nullstiller alle veiene slik at gammel informasjon ikke vises hvis brukeren
        // velger nye kordinater
        clearUtvei();
        labyrintArray[rad][kol].finnUtvei();
        return utveier;
    }

    public void leggTilUtvei(ArrayList<Tuppel> utveien) {
        utveier.add(utveien);
    }

    public void clearUtvei() {
        utveier.clear();
    }

    // funksjon som finner den korteste veien
    public ArrayList<ArrayList<Tuppel>> finnKortesteVei() {
        ArrayList<ArrayList<Tuppel>> kortesteArray = new ArrayList<>();
        ArrayList<Tuppel> min = utveier.get(0);

        // finner den korteste veien
        for (ArrayList<Tuppel> x : utveier) {
            if (x.size() < min.size()) {
                min = x;
            }
        }
        // sammenligner om det finnes samme korteste veier
        for (ArrayList<Tuppel> x : utveier) {
            if (x.size() == min.size()) {
                kortesteArray.add(x);
            }
        }
        // returnerer den korteste veien(e)
        return kortesteArray;
    }

    public Rute[][] returnLabyrintArray() {
        return labyrintArray;
    }
}