package model;

import org.json.JSONObject;
import persistence.Writable;

/*
Represents a genetic condition that a patient can suffer from.
Contains the name of the condition, the mutated DNA sequence, and its chromosome location
 */
public class Condition implements Writable {

    private final String name;
    private final String sequence;
    private final int chromosomeNum;

    // EFFECTS: Constructs a genetic condition with the given name, sequence, and chromosome number
    public Condition(String name, String seq, int chromosomeNum) {
        this.name = name;
        this.sequence = seq;
        this.chromosomeNum = chromosomeNum;
    }

    // EFFECTS: Returns the full information about the genetic condition
    public String getFullInfo() {
        EventLog.getInstance().logEvent(new Event("Retrieved information about the condition, " + name));
        StringBuilder condInfo = new StringBuilder();
        condInfo.append("Condition Name: " + name + "\n");
        condInfo.append("Chromosome Number: " + chromosomeNum + "\n");
        condInfo.append("Mutated Sequence: " + sequence + "\n");

        return condInfo.toString();
    }

    // EFFECTS: Returns the name of the genetic condition
    public String getName() {
        return this.name;
    }

    // EFFECTS: Returns the sequence of DNA associated with the genetic condition
    public String getSequence() {
        return this.sequence;
    }

    // EFFECTS: Returns the chromosome number where the DNA sequence is located
    public int getChromosomeNumber() {
        return this.chromosomeNum;
    }

    // EFFECTS: Converts Condition object to a JSONObject and returns it
    @Override
    public JSONObject toJson() {
        JSONObject jsonObj = new JSONObject();

        jsonObj.put("name", name);
        jsonObj.put("seq", sequence);
        jsonObj.put("chrNum", chromosomeNum);

        return jsonObj;
    }

    // EFFECTS: Converts the Condition object to a string which just contains its name
    @Override
    public String toString() {
        return name;
    }
}
