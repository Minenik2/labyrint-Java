import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * This object makes a GUI for the 'last ned en labyrint' button The point of
 * the GUI is that the user will load a labyrint and click on a point in the
 * labyrint. The program then finds all the possible exit routes and lists them
 * down as buttons for the user to click true and see all the possible ways of
 * exiting the labyrint. Code is mainly written with Norwegian variables and
 * comments. Sorry for the inconvinience (´ω｀*)
 */
public class GUILabyrint implements ActionListener {
    private JLabel label = new JLabel(
            "Velkommen til 'labyrint', dette programmed beregner alle de ulikene måtene å gå ut fra et punk til utveien. Vennligst klikk på et hvitt felt og se på alle de mulighetene til utvei");

    private ArrayList<ArrayList<JButton>> labyrintJButtonList = new ArrayList<>();
    private ArrayList<JButton> visUtveiJButtonList = new ArrayList<>();
    private Labyrint labyrint = new Labyrint();
    private ArrayList<ArrayList<Tuppel>> utveier;

    // oppretter knapp
    private JButton chooseButton = new JButton("Velg en annen labyrint");

    // oppretter paneler
    private JPanel menuPanel = new JPanel();
    private JPanel utveiPanel = new JPanel(); // panel som viser knapper som brukeren kan klikke pa for a fa utveier

    public GUILabyrint() {
        buildGUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // setter bakgrunnsfargene til knappene tilbake til default
        for (int i = 0; i < labyrintJButtonList.size(); i++) {
            for (int j = 0; j < labyrintJButtonList.get(i).size(); j++) {
                if (!labyrintJButtonList.get(i).get(j).getText().equals("#")) {
                    labyrintJButtonList.get(i).get(j).setBackground(null);
                }
            }
        }

        // visUtveiButton
        for (int i = 0; i < visUtveiJButtonList.size(); i++) {
            if (e.getSource() == visUtveiJButtonList.get(i)) {
                // setter teksten til å ha nye ruter
                label.setText("<html><body><p>Utveier funnet: " + utveier.size()
                        + ", de korteste utveiene er markert gult!<br> Du ser på utvei nr " + (i + 1) + " og den har "
                        + utveier.get(i).size() + " ruter</p></body></html>");
                // nullstiller farge pa andre knapper
                for (int j = 0; j < visUtveiJButtonList.size(); j++) {
                    visUtveiJButtonList.get(j).setBackground(null);
                }
                visKortesteVeiKnapp();
                visUtveiJButtonList.get(i).setBackground(Color.BLACK);
                visUtveiFra(utveier.get(i));
                utveiPanel.updateUI();
                return;
            }
        }

        // fjerner alle tidligere knapper
        for (JButton x : visUtveiJButtonList) {
            utveiPanel.remove(x);
        }
        visUtveiJButtonList.clear();

        // labyrint button
        for (int i = 0; i < labyrintJButtonList.size(); i++) {
            for (int j = 0; j < labyrintJButtonList.get(i).size(); j++) {
                if (e.getSource() == labyrintJButtonList.get(i).get(j)) {
                    utveier = labyrint.finnUtveiFra(j, i);
                    try {
                        visUtveiFra(utveier.get(0));
                    } catch (IndexOutOfBoundsException exceptionObj) {
                        // hvis brukeren klikker på en svart eller en rute uten utveier sjer det
                        // indexOutOfBounds, forteller dette til brukeren ved .setText
                        label.setText("Det finnes ingen utveier fra den ruten, prøv igjen :c");
                        utveiPanel.updateUI();
                        return;
                    }

                    // System.out.println(utveier);
                    // hviser hvor mange utveier som ble funnet i tekst
                    label.setText("<html><body><p>Utveier funnet: " + utveier.size()
                            + ", de korteste utveiene er markert gult!<br> Du ser på utvei nr 1 og den har "
                            + utveier.get(0).size()
                            + " ruter<br> klikk på knappene under for å se på en annen utvei!</p></body></html>");

                    utveiPanel.setLayout(new GridLayout(4, 0));
                    utveiPanel.setPreferredSize(new Dimension(200, 200));
                    for (int k = 0; k < utveier.size(); k++) {
                        // oppretter en knapp som viser en utvei retning
                        JButton visUtveiKnapp = new JButton(".");
                        visUtveiKnapp.addActionListener(this);
                        // setter knappen til opaque for mac
                        visUtveiKnapp.setOpaque(true);
                        visUtveiKnapp.setBorderPainted(false);
                        // legger den knappen inni visutveiKnapp
                        visUtveiJButtonList.add(visUtveiKnapp);
                        utveiPanel.add(visUtveiKnapp);
                    }
                    // gjor de knappene for korteste vei gule
                    visKortesteVeiKnapp();
                    // gjor den forste knappen svart fordi det er den utveien vi er pa
                    visUtveiJButtonList.get(0).setBackground(Color.BLACK);
                    menuPanel.add(utveiPanel);
                    utveiPanel.updateUI();
                    // #Den siste kommenterte koden under ble skrevet under testing, nå funker den
                    // uten men hvis det oppstår problemer med resize prøv å sette inn den siste
                    // linjen:
                    //
                    // #vi setter size og packer senere slik at knappene blir synelige
                    // #hvis vi bare bruker .pack så noen ganger kan man ikke se endringene som ble
                    // #gjort pa knappene og panelene
                    // frame.setSize(1280, 720);
                    // frame.pack();
                }
            }
        }
    }

    // klasse for chooseButton
    // chooseButton, brukeren velger en annen labyrint
    class ChooseButtonEvent implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // reseter GUI og loader in med den nye labyrinten brukeren
            // valgte
            resetGUI();
            buildGUI();
            menuPanel.updateUI();
        }
    }

    // starter opp hele GUI-et
    private void buildGUI() {
        int response;

        JFileChooser chooser = new JFileChooser(".");

        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        response = chooser.showOpenDialog(null);

        if (response == JFileChooser.APPROVE_OPTION) {
            try {
                labyrint.lesFraFil(chooser.getSelectedFile());
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        // menuPanel

        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.PAGE_AXIS));

        // choose another labyrint button
        menuPanel.add(chooseButton);

        // lager en if setning slik at knappen ikke får en actionlistener vær gang
        // brukeren velger en ny labyrint
        if (chooseButton.getActionListeners().length != 1) {
            chooseButton.addActionListener(new ChooseButtonEvent());
        }

        // the panel with the text
        JPanel textPanel = new JPanel();
        textPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        // textPanel.setLayout(new GridLayout(1, 1));
        textPanel.add(label);

        // panel with the labyrint
        JPanel labyrintPanel = new JPanel();
        Rute[][] labyrintArray = labyrint.returnLabyrintArray();
        labyrintPanel.setLayout(new GridLayout(labyrintArray.length, labyrintArray[0].length));

        for (int i = 0; i < labyrintArray.length; i++) {
            // oppretter en arrayliste med JButton
            ArrayList<JButton> jButtonList = new ArrayList<>();
            for (int j = 0; j < labyrintArray[0].length; j++) {
                JButton knapp = new JButton(labyrintArray[i][j].tilTegn());
                // Gir knappen en event listener
                knapp.addActionListener(this);
                // setter knappen til opaque for mac
                knapp.setOpaque(true);
                knapp.setBorderPainted(false);
                // legger knappen inni arraylisten
                jButtonList.add(knapp);
                if (labyrintArray[i][j].tilTegn().equals("#")) {
                    knapp.setBackground(Color.BLACK);
                }
                labyrintPanel.add(knapp);
            }
            // legger arraylisten med knapper inni en arraylist slik at den blir
            // to dimensjonel og representerer vår labyrint
            labyrintJButtonList.add(jButtonList);
        }

        // set up frame and display it
        labyrintPanel.setMinimumSize(new Dimension(200, 200));
        menuPanel.add(labyrintPanel);
        menuPanel.add(textPanel);
    }

    // reseter interface, brukes når brukeren klikker på velg en annen labyrint
    // knapp
    private void resetGUI() {
        label.setText(
                "Velkommen til 'labyrint', dette programmed beregner alle de ulikene måtene å gå ut fra et punk til utveien. Vennligst klikk på et hvitt felt og se på alle de mulighetene til utvei");
        menuPanel.removeAll();
        utveiPanel.removeAll();

        labyrintJButtonList.clear();
        visUtveiJButtonList.clear();
        labyrint = new Labyrint();
        if (utveier != null) {
            utveier.clear();
        }

        // oppretter knapp
        // private JButton button = new JButton("Click Me");

        // oppretter paneler
        // private JPanel menuPanel = new JPanel();
        // private JPanel utveiPanel = new JPanel(); // panel som viser knapper som
        // brukeren kan klikke pa for a fa utveier
    }

    public void showGUI() {
        JFrame frame = new JFrame();
        frame.add(menuPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Labyrinten");
        frame.pack();
        frame.setVisible(true);
    }

    public JPanel getMenuPanel() {
        return menuPanel;
    }

    private void visUtveiFra(ArrayList<Tuppel> tupperList) {
        for (Tuppel tuppel : tupperList) {
            labyrintJButtonList.get(tuppel.getY()).get(tuppel.getX()).setBackground(Color.GREEN);
        }
        // gjor knappen spilleren klikket pa til rod
        labyrintJButtonList.get(tupperList.get(0).getY()).get(tupperList.get(0).getX()).setBackground(Color.RED);
    }

    private void visKortesteVeiKnapp() {
        int shortest = utveier.get(0).size();
        // finner den korteste veien
        for (int i = 0; i < utveier.size(); i++) {
            // finner den korteste veien
            if (utveier.get(i).size() < shortest) {
                shortest = utveier.get(i).size();
            }
        }
        // sammenligner om det finnes samme korteste veier
        for (int i = 0; i < utveier.size(); i++) {
            if (utveier.get(i).size() == shortest) {
                // gjor de korteste vei knappene til gul bakgrunn
                visUtveiJButtonList.get(i).setBackground(Color.YELLOW);
            }
        }
    }

    public static void main(String[] args) {
        new GUILabyrint().showGUI();
    }
}