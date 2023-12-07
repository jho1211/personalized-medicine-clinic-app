package model;

import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ConditionTest {
    private Condition testCondition;

    @BeforeEach
    public void runBefore() {
        testCondition = new Condition("Genetic Condition A", "ACATGCATG", 10);
    }

    @Test
    public void testConstructor() {
        assertEquals("Genetic Condition A", testCondition.getName());
        assertEquals("ACATGCATG", testCondition.getSequence());
        assertEquals(10, testCondition.getChromosomeNumber());
    }

    @Test
    public void testToString() {
        assertEquals("Genetic Condition A", testCondition.toString());
    }

    @Test
    public void testGetCondInfo() {
//        condInfo.append("Condition Name: " + name + "\n");
//        condInfo.append("Chromosome Number: " + chromosomeNum + "\n");
//        condInfo.append("Mutated Sequence: " + sequence + "\n");
        assertEquals("Condition Name: Genetic Condition A\n" +
                "Chromosome Number: 10\n" +
                "Mutated Sequence: ACATGCATG\n", testCondition.getFullInfo());
        assertTrue(getEventLog().contains("Retrieved information about the condition, Genetic Condition A"));
    }

    // EFFECTS: Returns a list of all the event descriptions in EventLog
    public ArrayList<String> getEventLog() {
        ArrayList<String> events = new ArrayList<>();
        for (Event e : EventLog.getInstance()) {
            events.add(e.getDescription());
        }

        return events;
    }
}
