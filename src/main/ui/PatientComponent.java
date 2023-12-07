package ui;

import model.Clinic;
import model.Condition;
import model.Patient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

// The patient-related components for the main GUI
public class PatientComponent extends ComponentUtils implements ActionListener {
    private JPanel patientPanel;
    private Clinic clinic;

    private JLabel patientInfoLabel;
    private JTextArea patientInfo;
    private JList patientList;

    private JFrame patientViewFrame;

    private JFrame patientInputFrame;
    private JTextField fullNameEntryField;
    private JTextField dobEntryField;
    private JTextField phnEntryField;

    private JFrame diagnosisFrame;
    private JTextArea diagnosisReportText;

    // EFFECTS: Initializes a new panel and draws the patient GUI features on it
    public PatientComponent(Clinic clinic) {
        patientPanel = new JPanel();
        patientPanel.setLayout(new GridLayout(0, 1));
        this.clinic = clinic;

        initializeAddPatientFrame();
        initializeDiagnosePatientFrame();
        initializeViewPatientFrame();

        drawPatientList();
        drawPatientButtons();
    }

    // MODIFIES: this
    // EFFECTS: Draws the patient add JFrame and is hidden at start
    public void initializeAddPatientFrame() {
        patientInputFrame = new JFrame("New Patient Form");
        drawPopupFrame(patientInputFrame, 400, 200);

        patientInputFrame.setLayout(new GridLayout(0, 2));
        JLabel nameLabel = new JLabel("Full Name");
        fullNameEntryField = new JTextField(5);
        JLabel dobLabel = new JLabel("Date of Birth (MM/DD/YYYY)");
        dobEntryField = new JTextField(5);
        JLabel phnLabel = new JLabel("PHN (10 digits)");
        phnEntryField = new JTextField(5);

        JButton cancelBtn = new JButton("CANCEL");
        cancelBtn.addActionListener(this);
        cancelBtn.setActionCommand("cancelAdd");
        JButton submitBtn = new JButton("SUBMIT");
        submitBtn.addActionListener(this);
        submitBtn.setActionCommand("confirmAdd");

        patientInputFrame.add(nameLabel);
        patientInputFrame.add(fullNameEntryField);
        patientInputFrame.add(dobLabel);
        patientInputFrame.add(dobEntryField);
        patientInputFrame.add(phnLabel);
        patientInputFrame.add(phnEntryField);
        patientInputFrame.add(cancelBtn);
        patientInputFrame.add(submitBtn);
    }

    // MODIFIES: this
    // EFFECTS: Draws the patient view JFrame and is hidden at start
    public void initializeViewPatientFrame() {
        patientViewFrame = new JFrame("View Patient");
        drawPopupFrame(patientViewFrame, 400, 300);

        patientViewFrame.setLayout(new GridLayout(0, 1));

        patientInfoLabel = new JLabel();
        patientInfo = new JTextArea(5, 15);
        patientInfo.setEditable(false);

        JButton closeBtn = new JButton("CLOSE");
        closeBtn.addActionListener(this);
        closeBtn.setActionCommand("closeView");

        patientViewFrame.add(patientInfoLabel);
        patientViewFrame.add(patientInfo);
        patientViewFrame.add(closeBtn);
    }

    // MODIFIES: this
    // EFFECTS: Draws the diagnose patient JFrame which is hidden at start
    public void initializeDiagnosePatientFrame() {
        diagnosisFrame = new JFrame("Diagnosis Report");
        diagnosisFrame.setLayout(new BoxLayout(diagnosisFrame.getContentPane(), BoxLayout.Y_AXIS));
        drawPopupFrame(diagnosisFrame, 400, 300);

        diagnosisReportText = new JTextArea(5, 15);
        diagnosisReportText.setEditable(false);
        diagnosisReportText.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        diagnosisFrame.add(diagnosisReportText);

        JScrollPane scroll = new JScrollPane(diagnosisReportText);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        diagnosisFrame.add(scroll);

        JButton closeBtn = new JButton("CLOSE");
        closeBtn.setSize(new Dimension(80, 50));
        closeBtn.addActionListener(this);
        closeBtn.setActionCommand("closeReport");
        diagnosisFrame.add(closeBtn);
    }

    // MODIFIES: this
    // EFFECTS: Draws a selectable list containing all the current patients
    // at the clinic
    public void drawPatientList() {
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.PAGE_AXIS));

        JLabel patientListLabel = new JLabel("Current Patients");

        DefaultListModel listModel = new DefaultListModel();
        patientList = new JList(listModel);
        patientList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane listScroller = new JScrollPane(patientList);
        listScroller.setPreferredSize(new Dimension(250, 80));
        listScroller.setAlignmentX(patientPanel.LEFT_ALIGNMENT);

        listPanel.add(patientListLabel);
        listPanel.add(listScroller);

        patientPanel.add(listPanel);
    }

    // MODIFIES: this
    // EFFECTS: Draw the add/remove patient buttons
    public void drawPatientButtons() {
        JPanel modifyPatientsPanel = new JPanel();
        modifyPatientsPanel.setLayout(new BoxLayout(modifyPatientsPanel, BoxLayout.X_AXIS));

        JButton addPatientBtn = new JButton("Add New Patient");
        addPatientBtn.setActionCommand("addPatient");
        addPatientBtn.addActionListener(this);

        JButton removePatientBtn = new JButton("Remove Patient");
        removePatientBtn.setActionCommand("removePatient");
        removePatientBtn.addActionListener(this);

        JButton viewPatientBtn = new JButton("View Patient");
        viewPatientBtn.setActionCommand("viewPatient");
        viewPatientBtn.addActionListener(this);

        JButton diagnosePatientBtn = new JButton("Diagnose Patient");
        diagnosePatientBtn.setActionCommand("diagnose");
        diagnosePatientBtn.addActionListener(this);

        modifyPatientsPanel.add(addPatientBtn);
        modifyPatientsPanel.add(removePatientBtn);
        modifyPatientsPanel.add(viewPatientBtn);
        modifyPatientsPanel.add(diagnosePatientBtn);

        patientPanel.add(modifyPatientsPanel);
    }

    // MODIFIES: this
    // EFFECTS: Returns the patient info for the current patient selected in the list
    public void displayPatientInfo() {
        int idx = patientList.getSelectedIndex();

        if (idx != -1) {
            Patient patient = clinic.getPatients().get(idx);

            patientInfoLabel.setText("Displaying Patient Info for : " + patient.getFullName());
            patientInfo.setText(patient.getAllPatientInfo());

            patientViewFrame.setVisible(true);
        }
    }

    // EFFECTS: Returns the patient info with their full name, age, date of birth, and PHN
    public String formatPatientInfo(String name, String dob, String phn) {
        StringBuilder infoString = new StringBuilder();

        infoString.append("Full Name: " + name + "\n");
        infoString.append("Date of Birth: " + dob + "\n");
        infoString.append("PHN: " + phn);

        return infoString.toString();
    }

    // MODIFIES: this
    // EFFECTS: Updates all the patients' genomes who are registered in the clinic
    // Nothing happens for a patient if they do not have a genome file stored
    public void updateAllGenomesforPatients() {
        for (Patient p : clinic.getPatients()) {
            p.readGenome();
        }
    }

    // MODIFIES: this
    // EFFECTS: Updates the clinic with the most recent data
    public void updateClinic(Clinic clinic) {
        this.clinic = clinic;
    }

    // MODIFIES: this
    // EFFECTS: Updates the patient list selection with the current patients
    public void updatePatientList() {
        DefaultListModel listModel = new DefaultListModel();
        listModel.addAll(clinic.getPatients());
        patientList.setModel(listModel);
    }

    // MODIFIES: this
    // EFFECTS: Resets the fields for the next patient
    public void resetAddPatientFields() {
        fullNameEntryField.setText("");
        dobEntryField.setText("");
        phnEntryField.setText("");
    }

    // MODIFIES: this
    // EFFECTS: Creates a new patient with the specified details user inputted
    // If the info is incomplete/invalid, then the user is notified
    // Otherwise, add the new patient and inform the user the patient
    // was successfully created and refresh the patient list.
    public void addPatient() {
        String name = fullNameEntryField.getText();
        String dob = dobEntryField.getText();
        String phn = phnEntryField.getText();
        int res = showConfirmationBox("Are you sure you want to add the following patient? "
                + "Please confirm their details:\n" + formatPatientInfo(name, dob, phn));

        if (res == 0) {
            Patient newPatient = new Patient(name, dob, phn);
            clinic.addPatient(newPatient); // todo: validate user input before adding patient
            updatePatientList();
            // Hide the add new patient frame
            patientInputFrame.dispatchEvent(new WindowEvent(patientInputFrame, WindowEvent.WINDOW_CLOSING));
            showDialogBox("Patient was successfully added!");
            resetAddPatientFields();
        }
    }

    // MODIFIES: this
    // EFFECTS: Creates a new dialog box that prompts the user for confirmation
    // to remove the current patient selected
    // Inform the user the patient was successfully removed and refreshes the patient list.
    public void removePatient() {
        int idx = patientList.getSelectedIndex();
        if (idx != -1) {
            Patient patient = clinic.getPatients().get(idx);
            String phn = patient.getPHN();

            int res = showConfirmationBox("Are you sure you want to delete the data for "
                    + patient.getFullName() + "?");

            // 0 = yes, 1 = no, 2 = cancel
            if (res == 0) {
                clinic.removePatient(phn);
                updatePatientList();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Diagnoses the patient and creates a new frame that shows the results
    // of the diagnosis
    public void diagnosePatient() {
        int idx = patientList.getSelectedIndex();

        if (idx != -1) {
            Patient patient = clinic.getPatients().get(idx);
            java.util.List<Condition> conditions = clinic.getConditions();

            StringBuilder report = new StringBuilder();
            report.append("Diagnosis Report for " + patient.getFullName()
                    + " (PHN: " + patient.getPHN() + "):\n");

            report.append(patient.getFullDiagnosisReport(conditions));

            diagnosisReportText.setText(report.toString());
            diagnosisFrame.setVisible(true);
        }
    }

    // EFFECTS: Returns the panel
    public JPanel getPanel() {
        return patientPanel;
    }

    @Override
    // EFFECTS: Calls the desired function based on the action selected
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("addPatient")) {
            patientInputFrame.setVisible(true);
        } else if (e.getActionCommand().equals("removePatient")) {
            removePatient();
        } else if (e.getActionCommand().equals("confirmAdd")) {
            addPatient();
        } else if (e.getActionCommand().equals("diagnose")) {
            diagnosePatient();
        } else if (e.getActionCommand().equals("cancelAdd")) {
            patientInputFrame.dispatchEvent(new WindowEvent(patientInputFrame, WindowEvent.WINDOW_CLOSING));
        } else if (e.getActionCommand().equals("closeReport")) {
            diagnosisFrame.dispatchEvent(new WindowEvent(diagnosisFrame, WindowEvent.WINDOW_CLOSING));
        } else if (e.getActionCommand().equals("closeView")) {
            patientViewFrame.dispatchEvent(new WindowEvent(patientInputFrame, WindowEvent.WINDOW_CLOSING));
        } else if (e.getActionCommand().equals("viewPatient")) {
            displayPatientInfo();
        }
    }
}
