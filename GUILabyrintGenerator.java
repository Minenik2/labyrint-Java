import java.io.File;
import java.io.FileWriter;
import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUILabyrintGenerator extends Labyrint {
    private JPanel menuPanel = new JPanel();
    private JPanel buttonPanel = new JPanel();
    private JPanel labyrintPanel = new JPanel();
    private JLabel infoLabel = new JLabel(
            "<html><body>Generer en Labyrint og lagre den!<br> Advarsel: veldig store labyrinter kan crashe systemet</html></body>");
    private JButton generateButton = new JButton("Generer en labyrint");
    private JButton saveButton = new JButton("Lagre som fil");
    private JTextField xText = new JTextField(20);
    private JTextField yText = new JTextField(20);

    private GUILabyrintGenerator megSelv = this;

    public void generateLabyrint(int lengde, int bredde) {
        // det er mulig å bruke arraylist istedenfor array slik at man ikke trenger å
        // vise lengden på labyrinten på starten av filen, men jeg føler at oppgaven vil
        // ha bruk av array.
        labyrintArray = new Rute[lengde][bredde];

        // denne for løkken kan være en rekursjon i teori men jeg føler det
        // overkompliserer koden
        for (int i = 0; i < lengde; i++) {
            for (int j = 0; j < bredde; j++) {
                int randomNumber = new Random().nextInt(120);
                if (i == 0 || j == 0 || i == lengde - 1 || j == bredde - 1) {
                    randomNumber = new Random().nextInt(60);
                }

                if (randomNumber < 50) {
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
    }

    public void buildGUI() {
        saveButton.addActionListener(new SaveFileButtonEvent());
        generateButton.addActionListener(new GenerateButtonEvent());

        // xText.setPreferredSize(new Dimension(200, 200));
        xText.setMaximumSize(new Dimension(200, 200));
        yText.setMaximumSize(new Dimension(200, 200));
        labyrintPanel.setPreferredSize(new Dimension(200, 200));
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.PAGE_AXIS));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        buttonPanel.add(xText);
        buttonPanel.add(yText);
        buttonPanel.add(generateButton);
        buttonPanel.add(saveButton);
        menuPanel.add(infoLabel);
        menuPanel.add(buttonPanel);
        menuPanel.add(labyrintPanel);
    }

    public JPanel getMenuPanel() {
        return menuPanel;
    }

    class GenerateButtonEvent implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (yText.getText().equals("") || xText.getText().equals("")) {
                infoLabel.setText("Velg lengde og bredde først!");
                return;
            }
            generateLabyrint(Integer.parseInt(yText.getText()), Integer.parseInt(xText.getText()));

            // viser labyrinten
            Rute[][] labyrintArray = returnLabyrintArray();
            labyrintPanel.setLayout(new GridLayout(labyrintArray.length, labyrintArray[0].length));
            labyrintPanel.removeAll();

            for (int i = 0; i < labyrintArray.length; i++) {
                for (int j = 0; j < labyrintArray[0].length; j++) {
                    JButton knapp = new JButton(labyrintArray[i][j].tilTegn());
                    if (labyrintArray[i][j].tilTegn().equals("#")) {
                        knapp.setBackground(Color.BLACK);
                    }
                    labyrintPanel.add(knapp);
                }
            }

            infoLabel.setText("En labyrint generert! Hvis du likte den kan du lagre :3");
            labyrintPanel.updateUI();
        }
    }

    class SaveFileButtonEvent implements ActionListener {
        // lagre filen
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser(".");
            fileChooser.setDialogTitle("Specify a file to save");

            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                System.out.println("Save as file: " + fileToSave.getAbsolutePath());

                try (FileWriter writer = new FileWriter(fileToSave.getAbsolutePath())) {
                    String innhold = labyrintArray.length + " " + labyrintArray[0].length + "\n";
                    innhold = innhold.concat(megSelv.toString());
                    writer.write(innhold);
                } catch (Exception exception) {
                    System.out.println(exception);
                }
                infoLabel.setText("Labyrint Lagret!");
            }
        }
    }

    public void showGUI() {
        JFrame frame = new JFrame();
        buildGUI();
        frame.add(menuPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Labyrinten");
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new GUILabyrintGenerator().showGUI();
    }
}
