package model;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConditionListTest {
    private ConditionList testConditionList;
    private Condition testCondition1;
    private Condition testCondition2;
    private Condition testCondition3;

    @BeforeEach
    public void runBefore() {
        testConditionList = new ConditionList();
        testCondition1 = new Condition("Genetic Condition A", "ACATGCATG", 10);
        testCondition2 = new Condition("Genetic Condition B", "TGACGTTAA", 5);
        testCondition3 = new Condition("Genetic Condition C", "ATATATATA", 7);
    }

    @Test
    public void testAddMultipleConditionsNotInList() {
        boolean res1 = testConditionList.addCondition(testCondition1);
        boolean res2 = testConditionList.addCondition(testCondition2);
        boolean res3 = testConditionList.addCondition(testCondition3);

        List<Condition> conditions = testConditionList.getConditions();

        assertTrue(res1);
        assertTrue(res2);
        assertTrue(res3);

        assertEquals(3, conditions.size());
        assertEquals(testCondition1, conditions.get(0));
        assertEquals(testCondition2, conditions.get(1));
        assertEquals(testCondition3, conditions.get(2));
    }

    @Test
    public void testAddMultipleConditionsSomeInList() {
        boolean res1 = testConditionList.addCondition(testCondition1);
        boolean res2 = testConditionList.addCondition(testCondition1);
        boolean res3 = testConditionList.addCondition(testCondition2);

        List<Condition> conditions = testConditionList.getConditions();

        assertTrue(res1);
        assertFalse(res2);
        assertTrue(res3);

        assertEquals(2, conditions.size());
        assertEquals(testCondition1, conditions.get(0));
        assertEquals(testCondition2, conditions.get(1));
    }

    @Test
    public void testRemoveConditionNotInList() {
        testConditionList.addCondition(testCondition1);
        testConditionList.addCondition(testCondition2);

        boolean res = testConditionList.removeCondition("Genetic Condition C");

        List<Condition> conditions = testConditionList.getConditions();

        assertEquals(2, conditions.size());
        assertEquals(testCondition1, conditions.get(0));
        assertEquals(testCondition2, conditions.get(1));
        assertFalse(res);
    }

    @Test
    public void testRemoveMultipleConditions() {
        testConditionList.addCondition(testCondition1);
        testConditionList.addCondition(testCondition2);
        testConditionList.addCondition(testCondition3);

        boolean res1 = testConditionList.removeCondition("Genetic Condition A");
        boolean res2 = testConditionList.removeCondition("Genetic Condition C");

        List<Condition> conditions = testConditionList.getConditions();

        assertEquals(1, conditions.size());
        assertEquals(testCondition2, conditions.get(0));
        assertTrue(res1);
        assertTrue(res2);
    }
}
