package ui;

import model.Clinic;
import model.Condition;
import model.Patient;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import java.awt.event.WindowEvent;

// The condition-related components for the main GUI
public class ConditionComponent extends ComponentUtils implements ActionListener {
    private JPanel conditionPanel;
    private Clinic clinic;

    private JFrame newConditionFrame;
    private JFrame viewConditionFrame;

    private JTextField conditionNameField;
    private JTextField chromosomeNumField;
    private JTextField sequenceField;

    private JList conditionsList;

    private JLabel conditionInfoLabel;
    private JTextArea conditionInfoBox;

    public ConditionComponent(Clinic clinic) {
        conditionPanel = new JPanel();
        conditionPanel.setLayout(new GridLayout(0, 1));
        this.clinic = clinic;

        initializeAddConditionFrame();
        initializeViewPatientFrame();
        drawPatientList();
        drawConditionButtons();
    }

    // MODIFIES: this
    // EFFECTS: Draws the condition add JFrame and is hidden at start
    public void initializeAddConditionFrame() {
        newConditionFrame = new JFrame("New Patient Form");
        drawPopupFrame(newConditionFrame, 400, 200);

        newConditionFrame.setLayout(new GridLayout(0, 2));
        JLabel nameLabel = new JLabel("Condition Name");
        conditionNameField = new JTextField(5);
        JLabel chromNumLabel = new JLabel("Chromosome Number (1-23, X, Y)");
        chromosomeNumField = new JTextField(5);
        JLabel seqLabel = new JLabel("Mutation Sequence");
        sequenceField = new JTextField(5);

        JButton cancelBtn = new JButton("CANCEL");
        cancelBtn.addActionListener(this);
        cancelBtn.setActionCommand("cancelAdd");
        JButton submitBtn = new JButton("SUBMIT");
        submitBtn.addActionListener(this);
        submitBtn.setActionCommand("confirmAdd");

        newConditionFrame.add(nameLabel);
        newConditionFrame.add(conditionNameField);
        newConditionFrame.add(chromNumLabel);
        newConditionFrame.add(chromosomeNumField);
        newConditionFrame.add(seqLabel);
        newConditionFrame.add(sequenceField);
        newConditionFrame.add(cancelBtn);
        newConditionFrame.add(submitBtn);
    }

    // MODIFIES: this
    // EFFECTS: Draws the patient view JFrame and is hidden at start
    public void initializeViewPatientFrame() {
        viewConditionFrame = new JFrame("View Condition");
        drawPopupFrame(viewConditionFrame, 400, 300);

        viewConditionFrame.setLayout(new GridLayout(0, 1));

        conditionInfoLabel = new JLabel();
        conditionInfoBox = new JTextArea(5, 15);
        conditionInfoBox.setEditable(false);

        JButton closeBtn = new JButton("CLOSE");
        closeBtn.addActionListener(this);
        closeBtn.setActionCommand("closeView");

        viewConditionFrame.add(conditionInfoLabel);
        viewConditionFrame.add(conditionInfoBox);
        viewConditionFrame.add(closeBtn);
    }

    // MODIFIES: this
    // EFFECTS: Draws a selectable list containing all the current patients
    // at the clinic
    public void drawPatientList() {
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.PAGE_AXIS));

        JLabel conditionListLabel = new JLabel("Current Conditions");

        DefaultListModel listModel = new DefaultListModel();
        conditionsList = new JList(listModel);
        conditionsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane listScroller = new JScrollPane(conditionsList);
        listScroller.setPreferredSize(new Dimension(250, 80));
        listScroller.setAlignmentX(conditionPanel.LEFT_ALIGNMENT);

        listPanel.add(conditionListLabel);
        listPanel.add(listScroller);

        conditionPanel.add(listPanel);
    }

    // MODIFIES: this
    // EFFECTS: Draws the add/remove/view condition buttons
    public void drawConditionButtons() {
        JPanel conditionButtonsPanel = new JPanel();
        conditionButtonsPanel.setLayout(new BoxLayout(conditionButtonsPanel, BoxLayout.X_AXIS));

        JButton addConditionBtn = new JButton("Add New Condition");
        addConditionBtn.setActionCommand("add");
        addConditionBtn.addActionListener(this);

        JButton removeConditionBtn = new JButton("Remove Condition");
        removeConditionBtn.setActionCommand("remove");
        removeConditionBtn.addActionListener(this);

        JButton viewConditionBtn = new JButton("View Condition");
        viewConditionBtn.setActionCommand("view");
        viewConditionBtn.addActionListener(this);

        conditionButtonsPanel.add(addConditionBtn);
        conditionButtonsPanel.add(removeConditionBtn);
        conditionButtonsPanel.add(viewConditionBtn);

        conditionPanel.add(conditionButtonsPanel);
    }

    // MODIFIES: this
    // EFFECTS: Fetches the text from the fields and constructs a new condition
    // Prompts the user to confirm the details. If they cancel or say no, then nothing happens.
    public void addCondition() {
        String name = conditionNameField.getText();
        Integer chromNum = Integer.parseInt(chromosomeNumField.getText());
        String seq = sequenceField.getText();
        int res = showConfirmationBox("Are you sure you want to add the following condition? "
                + "Please confirm their details:\n"
                + "Condition Name: " + name + "\n"
                + "Chromosome Number: " + Integer.toString(chromNum) + "\n"
                + "Sequence: " + seq);

        if (res == 0) {
            Condition newCondition = new Condition(name, seq, chromNum);
            clinic.addCondition(newCondition); // todo: validate user input before adding condition
            updateConditionList();
            // Hide the add new patient frame
            newConditionFrame.dispatchEvent(new WindowEvent(newConditionFrame, WindowEvent.WINDOW_CLOSING));
            showDialogBox("Condition was successfully added!");
            resetAddConditionFields();
        }
    }

    // MODIFIES: this
    // EFFECTS: Resets the text in the add condition fields to empty
    public void resetAddConditionFields() {
        conditionNameField.setText("");
        chromosomeNumField.setText("");
        sequenceField.setText("");
    }

    // MODIFIES: this
    // EFFECTS: Takes the selected condition and removes it from the clinic
    // If no condition selected, does nothing
    public void removeCondition() {
        int idx = conditionsList.getSelectedIndex();
        if (idx != -1) {
            Condition cond = clinic.getConditions().get(idx);

            int res = showConfirmationBox("Are you sure you want to delete the data for "
                    + cond.getName() + "?");

            // 0 = yes, 1 = no, 2 = cancel
            if (res == 0) {
                clinic.removeCondition(cond.getName());
                updateConditionList();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Toggles the visibility of the conditionFrame and updates the textarea with the
    // condition information for the selected condition
    public void displayConditionInfo() {
        int idx = conditionsList.getSelectedIndex();

        if (idx != -1) {
            Condition cond = clinic.getConditions().get(idx);

            conditionInfoLabel.setText("Displaying information for " + cond.getName());
            conditionInfoBox.setText(cond.getFullInfo());

            viewConditionFrame.setVisible(true);
        }
    }

    // MODIFIES: this
    // EFFECTS: Updates the clinic with the most recent data
    public void updateClinic(Clinic clinic) {
        this.clinic = clinic;
    }

    // MODIFIES: this
    // EFFECTS: Updates the patient list selection with the current patients
    public void updateConditionList() {
        DefaultListModel listModel = new DefaultListModel();
        listModel.addAll(clinic.getConditions());
        conditionsList.setModel(listModel);
    }

    // EFFECTS: Returns the panel
    public JPanel getPanel() {
        return conditionPanel;
    }

    @Override
    // EFFECTS: Calls the desired function based on the action provided
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("add")) {
            newConditionFrame.setVisible(true);
        } else if (e.getActionCommand().equals("confirmAdd")) {
            addCondition();
        } else if (e.getActionCommand().equals("remove")) {
            removeCondition();
        } else if (e.getActionCommand().equals("view")) {
            displayConditionInfo();
        } else if (e.getActionCommand().equals("cancelAdd")) {
            newConditionFrame.dispatchEvent(new WindowEvent(newConditionFrame, WindowEvent.WINDOW_CLOSING));
        } else if (e.getActionCommand().equals("closeView")) {
            viewConditionFrame.dispatchEvent(new WindowEvent(newConditionFrame, WindowEvent.WINDOW_CLOSING));
        }
    }
}
