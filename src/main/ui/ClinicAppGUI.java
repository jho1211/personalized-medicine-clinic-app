package ui;

import model.*;
import model.Event;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import static java.lang.System.exit;

// The graphical user interface for the clinic app
public class ClinicAppGUI extends JFrame implements ActionListener, WindowListener {
    private Clinic clinic;

    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private static final String saveLocation = "./data/clinicData.json";

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

    private PatientComponent patientComponent;
    private ConditionComponent conditionComponent;

    public ClinicAppGUI() {
        super("Personalized Medicine Clinic App");
        initializeFields();
        initializeMainFrame();
        initializeComponents();
        drawComponents();
        pack();
        promptLoadMostRecentSave();
        addWindowListener(this);
    }

    // MODIFIES: this
    // EFFECTS: Initializes the fields for JSONReader and JSONWriter
    private void initializeFields() {
        jsonWriter = new JsonWriter(saveLocation);
        jsonReader = new JsonReader(saveLocation);
        clinic = new Clinic(null, null);
    }

    // MODIFIES: this
    // EFFECTS: Asks the user if they want to load the most recent save for clinic
    // If no or cancel, then do nothing. Otherwise, loads the most recent save
    private void promptLoadMostRecentSave() {
        int res = showConfirmationBox("Would you like to load the most recent save of the clinic?");

        if (res == 0) {
            loadClinic();
        }
    }

    // MODIFIES: this
    // EFFECTS: Draws all the components of the GUI in the listed order
    private void drawComponents() {
        drawPersistenceButtons();
        drawLogo();
        drawPatientPanel();
        drawConditionPanel();
    }

    // MODIFIES: this
    // EFFECTS: Initializes the patient and condition features of the GUI
    private void initializeComponents() {
        patientComponent = new PatientComponent(clinic);
        conditionComponent = new ConditionComponent(clinic);
    }

    // MODIFIES: this
    // EFFECTS: Draws a DNA Logo onto the main JPanel
    private void drawLogo() {
        ImageIcon logo = new ImageIcon("./data/dna-icon.png");
        JLabel logoLabel = new JLabel(logo);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(logoLabel);
    }

    // MODIFIES: this
    // EFFECTS: Draws the buttons that allow the user to save and load the clinic
    private void drawPersistenceButtons() {
        JPanel persistenceArea = new JPanel();
        persistenceArea.setLayout(new FlowLayout());

        JButton saveBtn = new JButton("Save");
        saveBtn.setActionCommand("save");
        saveBtn.addActionListener(this);

        JButton loadBtn = new JButton("Load");
        loadBtn.setActionCommand("load");
        loadBtn.addActionListener(this);

        persistenceArea.add(saveBtn);
        persistenceArea.add(loadBtn);

        add(persistenceArea);
    }

    // MODIFIES: this
    // EFFECTS: Gets the panel from patientComponent and adds it to the JFrame
    private void drawPatientPanel() {
        JPanel patientPanel = patientComponent.getPanel();
        add(patientPanel);
    }

    // MODIFIES: this
    // EFFECTS: Gets the panel from conditionComponent and adds it to the JFrame
    private void drawConditionPanel() {
        JPanel conditionPanel = conditionComponent.getPanel();
        add(conditionPanel);
    }

    // MODIFIES: this
    // EFFECTS: Draw the base JFrame
    private void initializeMainFrame() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(true);
    }

    // EFFECTS: Creates a dialog box with the specified message
    private void showDialogBox(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    // EFFECTS: Creates a dialog box with the specified message
    private int showConfirmationBox(String msg) {
        return JOptionPane.showConfirmDialog(null, msg);
    }

    // EFFECTS: Saves the current state of the clinic app
    // Lets the user know with a dialog box that the clinic was saved
    // successfully or unsuccessfully
    private void saveClinic() {
        try {
            jsonWriter.saveClinicToJson(clinic);
            showDialogBox("Successfully saved the clinic data.");
        } catch (IOException e) {
            showDialogBox("Error occurred while saving the clinic.");
        }
    }

    // MODIFIES: this
    // EFFECTS: Loads the most recently saved state of the clinic app
    // Lets the user know with a dialog box that the clinic was loaded
    // successfully or unsuccessfully
    private void loadClinic() {
        try {
            clinic = jsonReader.read();
            showDialogBox("Successfully loaded the clinic data.");
        } catch (IOException e) {
            showDialogBox("Error occurred while loading the clinic data.");
        }

        patientComponent.updateClinic(clinic);
        patientComponent.updatePatientList();
        patientComponent.updateAllGenomesforPatients();

        conditionComponent.updateClinic(clinic);
        conditionComponent.updateConditionList();
    }

    @Override
    // EFFECTS: Reads the action command and performs the corresponding action
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("save")) {
            saveClinic();
        } else if (e.getActionCommand().equals("load")) {
            loadClinic();
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    // EFFECTS: Prints out each event in the event log when the main window is closed
    @Override
    public void windowClosing(WindowEvent e) {
        EventLog log = EventLog.getInstance();
        for (Event evt : log) {
            System.out.println(evt.toString());
        }

        dispose();
        exit(0);
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
