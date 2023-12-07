package model;

import org.json.JSONObject;
import persistence.Writable;

import java.io.IOException;
import java.time.LocalDate;
import java.io.File;
import java.io.FileNotFoundException;

import java.time.Period;
import java.util.Scanner;
import java.util.List;

/*
Represents a patient at the clinic who has a full name, date of birth, age (in years),
personal health number (PHN, 10-digit number), genomic profile (as a string that consists of {A,T,G,C}),
and patient notes
 */
public class Patient implements Writable {

    private String fullName;
    private String dob;
    private String phn;
    private int age;
    private String genome;
    private String notes;

    // REQUIRES: dob must be written with format MM/DD/YYYY and phn must be a valid 10-digit personal health number
    // EFFECTS: Constructs a new patient given their full name, date of birth,
    // and personal health number. Age is generated based on their DOB.
    public Patient(String fullName, String dob, String phn) {
        this.fullName = fullName;
        this.dob = dob;
        this.phn = phn;
        this.genome = "";
        this.notes = "";

        updateAge();
    }

    // REQUIRES: genome is a valid DNA sequence which only consists of characters in {A,T,G,C}
    // MODIFIES: this
    // EFFECTS:  Replaces the patient's current genome with the given genome sequence
    public void updateGenome(String genome) {
        this.genome = genome;
    }

    // MODIFIES: this
    // EFFECTS:  Replaces the patient's current notes with the new notes
    public void updateNotes(String newNotes) {
        this.notes = newNotes;
    }

    // REQUIRES: this patient must have a genome sequence for the particular chromosome
    // where this condition is located
    // EFFECTS: Returns True if the patient has the specified genetic condition
    // based on their genome. Returns False if patient doesn't have condition.
    public boolean diagnose(Condition condition) {
        EventLog.getInstance().logEvent(new Event("Diagnosed the patient with PHN, "
                + phn + " for genetic conditions."));
        return this.genome.contains(condition.getSequence());
    }

    // EFFECTS: Returns the results of a sequence alignment with the condition sequence
    // The condition sequence and the most similar part of the reference sequence is returned with
    // the non-similar parts redacted
    // The score of the alignment is displayed
    public String getDiagnosisReport(Condition condition) {
        if (genome.equals("")) {
            return ""; // todo: throw a GenomeNotFoundError
        }
        SequenceAligner seqAligner = new SequenceAligner(condition.getSequence(), genome);
        seqAligner.getMostSimilarAlignment();
        String similarityScore = Integer.toString(seqAligner.getSimilarityPercentage());
        return "Diagnosis for " + condition.getName() + "\n"
                + "Patient Genome: " + seqAligner.getRedactedSeq() + "\n"
                + "Condition Seq:  " + seqAligner.getTargetSeq() + "\n"
                + "(Similarity: " + similarityScore + "%)\n\n";
    }

    // EFFECTS: Returns all the results of the sequence alignment with condition sequences
    public String getFullDiagnosisReport(List<Condition> conditions) {
        if (genome.equals("")) {
            return "";
        } else {
            EventLog.getInstance().logEvent(new Event("Retrieved a full diagnosis report for"
                    + " patient with PHN, " + phn));
            StringBuilder report = new StringBuilder();
            for (Condition c : conditions) {
                report.append(getDiagnosisReport(c));
            }
            return report.toString();
        }
    }

    // REQUIRES: The file must exist in data directory in the format PHN_genome.txt
    // MODIFIES: this
    // EFFECTS: Reads the patient's genome sequence from a text file in data
    // and updates the patient's genome. If the file doesn't exist, do nothing.
    public void readGenome() {
        String fileName = "./data/genomes/" + this.phn + "_genome.txt";
        StringBuilder genome = new StringBuilder();

        try {
            File file = new File(fileName);
            Scanner fileReader = new Scanner(file);

            while (fileReader.hasNextLine()) {
                genome.append(fileReader.nextLine());
            }

            fileReader.close();
        } catch (FileNotFoundException e) {
            return;
        }

        EventLog.getInstance().logEvent(new Event("Fetched the genome for the patient with PHN, "
                + phn + " from the data folder."));
        updateGenome(genome.toString());

    }

    // REQUIRES: Patient has a valid date of birth (MM/DD/YYYY)
    // MODIFIES: this
    // EFFECTS: Updates the age of the patient calculated based on their date of birth relative to the current date
    public void updateAge() {
        // Find the current day
        LocalDate curDate = LocalDate.now();

        String[] dates = dob.split("/");
        int birthMonth = Integer.parseInt(dates[0]);
        int birthDay = Integer.parseInt(dates[1]);
        int birthYear = Integer.parseInt(dates[2]);
        LocalDate birthDate = LocalDate.now().withMonth(birthMonth).withDayOfMonth(birthDay).withYear(birthYear);
        Period howLong = birthDate.until(curDate);

        this.age = howLong.getYears();
    }

    // EFFECTS: Returns true if patient has no genome yet and false otherwise
    public boolean hasNoGenome() {
        return genome.equals("");
    }

    // EFFECTS: Returns all the information about a patient
    public String getAllPatientInfo() {
        EventLog.getInstance().logEvent(new Event("Retrieved information about patient with PHN: " + phn));
        StringBuilder infoString = new StringBuilder();

        infoString.append("Full Name: " + fullName + "\n");
        infoString.append("PHN: " + phn + "\n");
        infoString.append("Age: " + age + "\n");
        infoString.append("Date of Birth: " + dob + "\n");
        infoString.append("Notes: " + notes);

        return infoString.toString();
    }

    // EFFECTS: Returns the patient's full name
    public String getFullName() {
        return this.fullName;
    }

    // EFFECTS: Returns the patient's date of birth
    public String getDOB() {
        return this.dob;
    }

    // EFFECTS: Returns the patient's personal health number
    public String getPHN() {
        return this.phn;
    }

    // EFFECTS: Returns the patient's age
    public int getAge() {
        return this.age;
    }

    // EFFECTS: Returns the patient's genome
    public String getGenome() {
        return this.genome;
    }

    // EFFECTS: Returns the notes stored for the patient
    public String getNotes() {
        return this.notes;
    }

    // EFFECTS: Converts Patient object to a JSONObject and returns it
    @Override
    public JSONObject toJson() {
        JSONObject jsonObj = new JSONObject();

        jsonObj.put("name", fullName);
        jsonObj.put("dob", dob);
        jsonObj.put("phn", phn);
        jsonObj.put("notes", notes);

        return jsonObj;
    }

    // EFFECTS: Returns the patient's full name and PHN
    @Override
    public String toString() {
        return fullName + " (PHN: " + phn + ")";
    }
}
