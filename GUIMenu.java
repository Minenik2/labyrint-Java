import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUIMenu implements ActionListener {
    private GUILabyrint labyrintGUI = new GUILabyrint();
    private GUILabyrintGenerator labyrintGenerateGUI = new GUILabyrintGenerator();

    private JFrame frame = new JFrame();
    private JPanel mainPanel = new JPanel(); // hoved panel der menupanel og backbuttonpanel befinner seg
    private JPanel menuPanel = new JPanel();
    private JPanel backButtonPanel = new JPanel();
    private JPanel labyrintGUIMenuPanel = labyrintGUI.getMenuPanel();
    private JLabel infoText = new JLabel(
            "Velkommen til 'Labyrint', et program som kan genere labyrinter og finne alle mulige utveier fra en labyrint");

    private JButton loadLabyrintButton = new JButton("Last Ned En Labyrint");
    private JButton generateLabyrintButton = new JButton("Generer En Labyrint");
    private JButton backButton = new JButton("back"); // knapp for å gå tilbake

    public GUIMenu() {
        builGUI();
    }

    private void builGUI() {
        // kode jeg fant på stackoverflow for å at background skal funke på mac, vet
        // ikke om det funker fordi har ikke mac selv
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //

        JPanel buttonPanel = new JPanel();

        labyrintGenerateGUI.buildGUI();

        loadLabyrintButton.addActionListener(this);
        generateLabyrintButton.addActionListener(new GenerateLabyrintButtonEvent());
        backButton.addActionListener(new BackButtonEvent());

        backButtonPanel.setLayout(new BoxLayout(backButtonPanel, BoxLayout.X_AXIS));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        buttonPanel.add(infoText);
        buttonPanel.add(loadLabyrintButton);
        buttonPanel.add(generateLabyrintButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        // menuPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        menuPanel.add(buttonPanel);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        // legger til menupanel i mainpanel
        mainPanel.add(menuPanel);

        frame.add(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Labyrinten");
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loadLabyrintButton) {
            mainPanel.remove(menuPanel);
            backButtonPanel.add(backButton);
            mainPanel.add(backButtonPanel);
            mainPanel.add(labyrintGUIMenuPanel);
            mainPanel.updateUI();
            frame.pack();
        }
    }

    // når brukeren klikker på back button går brukeren tilbake til menyen
    class BackButtonEvent implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainPanel.removeAll();
            mainPanel.add(menuPanel);
            mainPanel.updateUI();
        }
    }

    // når brukeren ønsker å generere en ny labyrint
    class GenerateLabyrintButtonEvent implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainPanel.remove(menuPanel);
            backButtonPanel.add(backButton);
            mainPanel.add(backButtonPanel);
            mainPanel.add(labyrintGenerateGUI.getMenuPanel());
            mainPanel.updateUI();
        }
    }
}
