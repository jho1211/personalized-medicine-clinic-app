package ui;

import model.*;
import persistence.*;

import java.io.IOException;
import java.util.*;

// Application for Clinic to manage patients. Code is modified from TellerApp.
public class ClinicApp {
    private Clinic clinic;
    private Scanner input;

    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private static final String saveLocation = "./data/clinicData.json";

    public ClinicApp() {
        runClinic();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runClinic() {
        boolean clinicOpen = true;
        initClinic();

        while (clinicOpen) {
            displayClinicWelcome();
            String userCommand = input.next();
            userCommand = userCommand.toLowerCase();

            if (userCommand.equals("q")) {
                clinicOpen = false;
            } else {
                readCommand(userCommand);
            }
        }
        System.out.println("Would you like to save the clinic? This will override any previous data! Y/N");
        String saveOpt = input.next().toLowerCase();
        if (saveOpt.equals("y")) {
            saveData();
        }
        System.out.println("\nThe clinic is now closing... Goodbye");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private void readCommand(String command) {
        if (command.equals("a")) {
            addNewPatient();
        } else if (command.equals("ac")) {
            addCondition();
        } else if (command.equals("r")) {
            removePatient();
        } else if (command.equals("rc")) {
            removeCondition();
        } else if (command.equals("l")) {
            listPatients();
        } else if (command.equals("d")) {
            diagnosePatient();
        } else if (command.equals("lc")) {
            listConditions();
        } else if (command.equals("u")) {
            updateGenomeForPatient();
        } else if (command.equals("v")) {
            showPatientInfo();
        } else if (command.equals("s")) {
            saveData();
        } else {
            System.out.println("Invalid command, please try again.");
        }
    }

    // EFFECTS: Saves the Clinic data to a JSON file
    public void saveData() {
        try {
            jsonWriter.saveClinicToJson(clinic);
            System.out.println("Clinic data was successfully saved to file.");
        } catch (IOException e) {
            System.out.println("Error while saving file to clinic.");
        }
    }

    // MODIFIES: this
    // EFFECTS: Initializes the clinic and loads saved clinic data if user chooses
    private void initClinic() {
        jsonWriter = new JsonWriter(saveLocation);
        jsonReader = new JsonReader(saveLocation);
        this.clinic = new Clinic(null, null);

        input = new Scanner(System.in);
        input.useDelimiter("\n");

        loadLastSave();

        updateAllGenomesforPatients();
    }

    // MODIFIES: this
    // EFFECTS: prompts user if they want to load the saved clinic data
    // If yes, loads the data
    // If no or if file can't be read, creates a blank clinic
    private void loadLastSave() {
        System.out.println("Would you like to load the previous save for the clinic data? Y/N");
        String loadSave = input.next().toLowerCase();

        if (loadSave.equals("y")) {
            try {
                clinic = jsonReader.read();
                System.out.println("The latest clinic data has been loaded.");
            } catch (IOException e) {
                System.out.println("The data for the clinic could not be read. An empty clinic has been created.");
            }
        } else {
            System.out.println("An empty clinic has been created.");
        }
    }

    // EFFECTS: Displays a list of options for user input
    private void displayClinicWelcome() {
        System.out.println("\nWelcome to our clinic management system, please choose the following options:");
        System.out.println("\ta -> Add New Patient");
        System.out.println("\tac -> Add New Genetic Condition");
        System.out.println("\tr -> Remove Patient");
        System.out.println("\trc -> Remove Genetic Condition");
        System.out.println("\tu -> Update Patient's Genome");
        System.out.println("\tv -> View Patient Info");
        System.out.println("\tl -> List Current Patients");
        System.out.println("\tlc -> List Current Genetic Conditions");
        System.out.println("\td -> Diagnosis Report for Patient");
        System.out.println("\ts -> Save the Clinic Data");
        System.out.println("\tq -> Close the Clinic");
    }

    // REQUIRES: Valid date of birth (MM/DD/YYYY) and PHN for user input
    // MODIFIES: this
    // EFFECTS: Adds a new patient to the clinic if
    // they are not already registered with the clinic
    private void addNewPatient() {
        System.out.print("Enter full name of patient: ");
        String fullName = input.next();

        System.out.print("Enter date of birth in MM/DD/YYYY format: ");
        String dob = input.next();

        System.out.print("Enter the personal health number for the patient: ");
        String phn = input.next();

        Patient newPatient = new Patient(fullName, dob, phn);
        boolean res = clinic.addPatient(newPatient);

        if (res) {
            System.out.println("The patient, " + fullName + ", was successfully added to the patient list.");
        } else {
            System.out.println("The patient, " + fullName
                    + ", was not added because they are already registered with the clinic");
        }
    }

    // MODIFIES: this
    // EFFECTS: Removes the patient at the specified number in the
    // patient list that is displayed to the user
    private void removePatient() {
        listPatients();
        System.out.println("Please specify the PHN of the patient you wish to remove: ");
        String phn = input.next();

        boolean res = clinic.removePatient(phn);

        if (res) {
            System.out.println("\nThe patient with PHN " + phn + " was successfully removed from the patient list.\n");
        } else {
            System.out.println("\nThe patient with PHN " + phn + " does not exist, please check the PHN again.\n");
        }
    }

    // EFFECTS: Displays all the current patients registered with the clinic
    // If no patients registered, tells user that
    private void listPatients() {
        List<Patient> patients = clinic.getPatients();

        if (patients.size() == 0) {
            System.out.println("\nThere are no patients registered with the clinic yet.");
            return;
        }

        System.out.println("\nThe following patients are registered with the clinic:");

        for (int i = 0; i < patients.size(); i++) {
            Patient curPatient = patients.get(i);
            String hasGenome;

            if (curPatient.getGenome().equals("")) {
                hasGenome = "Patient doesn't have a genome imported yet.";
            } else {
                hasGenome = "Patient has a genome imported.";
            }

            System.out.println(i + 1 + ". " + curPatient.getFullName() + " (PHN: " + curPatient.getPHN() + ")"
                    + " - " + hasGenome);
        }
    }

    // EFFECTS: Produces a diagnosis report for a patient listing all their genetic conditions
    private void diagnosePatient() {
        listPatients();

        System.out.print("Enter the PHN of the patient you wish to diagnose: ");
        String phn = input.next();
        Patient patient = clinic.findPatient(phn);

        if (patient == null) {
            System.out.println("\nThe specified patient could not be found, please try again.");
        } else if (patient.hasNoGenome()) {
            System.out.println("\nThe specified patient does not have a genome file yet, so we can't diagnose.");
        } else {
            System.out.println("\nDiagnosis report for " + patient.getFullName() + " (" + patient.getPHN() + ")");
            for (Condition c : clinic.getConditions()) {
                boolean res = patient.diagnose(c);

                if (res) {
                    System.out.println(c.getName() + " - positive");

                } else {
                    System.out.println(c.getName() + " - negative");
                }

                printDiagnosisReport(c, patient);
            }
        }
    }

    // EFFECTS: Returns the results of a sequence alignment with the condition sequence
    // The condition sequence and the most similar part of the reference sequence is returned with
    // the non-similar parts redacted
    // The score of the alignment is displayed
    public String printDiagnosisReport(Condition condition, Patient p) {
        SequenceAligner seqAligner = new SequenceAligner(condition.getSequence(), p.getGenome());
        seqAligner.getMostSimilarAlignment();
        String similarityScore = Integer.toString(seqAligner.getSimilarityPercentage());
        return "Patient Genome: " + seqAligner.getRedactedSeq() + "\n"
                + "Condition Seq:  " + seqAligner.getTargetSeq()
                + "\n(Similarity: " + similarityScore + "%)\n";
    }

    // REQUIRES: valid DNA sequence for genetic condition
    // MODIFIES: this
    // EFFECTS: Adds a new condition to the clinic if it doesn't already exist based on user input
    private void addCondition() {
        System.out.print("Enter name of genetic condition: ");
        String name = input.next();

        System.out.print("Enter sequence of mutated DNA: ");
        String seq = input.next();

        System.out.print("Enter the chromosome number where mutation is located: ");
        int chrNum = input.nextInt();

        Condition newCondition = new Condition(name, seq, chrNum);
        boolean res = clinic.addCondition(newCondition);

        if (res) {
            System.out.println("\nThe condition, " + name + ", was successfully added to the conditions list.");
        } else {
            System.out.println("\nThe condition, " + name
                    + ", was not added because they are already in the conditions list.");
        }
    }

    // MODIFIES: this
    // EFFECTS: Removes the condition with the specified name from user input
    private void removeCondition() {
        listConditions();
        System.out.print("Enter the name of the genetic condition you wish to remove: ");
        String name = input.next();

        boolean res = clinic.removeCondition(name);

        if (res) {
            System.out.println("\nThe condition, " + name + ", was removed from the list of conditions.");
        } else {
            System.out.println("\nThe condition, " + name + ", couldn't be removed.");
        }
    }

    // EFFECTS: Outputs the list of genetic conditions that have been added so far
    // If there are no conditions added yet, let user know
    private void listConditions() {
        List<Condition> conditions = clinic.getConditions();

        if (conditions.size() == 0) {
            System.out.println("\nThere are no genetic conditions added to the clinic yet.");
            return;
        }

        System.out.println("\nThe following genetic conditions are inputted in the clinic:");

        for (int i = 0; i < conditions.size(); i++) {
            Condition curCondition = conditions.get(i);
            System.out.println(i + 1 + ". " + curCondition.getName() + " (mutation in "
                    + "chromosome " + curCondition.getChromosomeNumber() + ")");
        }
    }

    // MODIFIES: this
    // EFFECTS: Updates the patient with the specified PHN with a new genome
    // If they do not have a genome file stored, then do nothing.
    private void updateGenomeForPatient() {
        listPatients();
        System.out.print("\nEnter the PHN of the patient you want to update: ");
        String phn = input.next();

        Patient patient = clinic.findPatient(phn);

        if (patient == null) {
            System.out.println("\nThe specified patient couldn't be found.");
        } else {
            patient.readGenome();
            System.out.println("\nThe patient's genome was updated if their genome exists.");
        }
    }

    // MODIFIES: this
    // EFFECTS: Updates all the patients' genomes who are registered in the clinic
    // Nothing happens for a patient if they do not have a genome file stored
    private void updateAllGenomesforPatients() {
        for (Patient p : clinic.getPatients()) {
            p.readGenome();
        }
    }

    // EFFECTS: Returns the details of the patient with the specified PHN
    // If they don't exist, then returns nothing
    private void showPatientInfo() {
        listPatients();
        System.out.print("\nEnter the PHN of the patient you want to view: ");
        String phn = input.next();
        Patient patient = clinic.findPatient(phn);

        if (patient != null) {
            System.out.println("PATIENT INFO FOR " + patient.getPHN());
            System.out.println("Full Name:" + patient.getFullName());
            System.out.println("Age:" + patient.getAge());
            System.out.println("Date of Birth:" + patient.getDOB());
            System.out.println("Notes:" + patient.getNotes());
        } else {
            System.out.println("The specified patient could not be found.");
        }
    }
}
