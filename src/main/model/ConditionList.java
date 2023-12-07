package model;

import org.json.JSONArray;
import persistence.WritableArray;

import java.util.*;

// Stores a list of genetic conditions that patients can possibly suffer from
public class ConditionList implements WritableArray {
    private List<Condition> conditions;

    // EFFECTS: Constructs an empty list to hold genetic conditions
    public ConditionList() {
        conditions = new ArrayList<Condition>();
    }

    // MODIFIES: this
    // EFFECTS: Adds the genetic condition to the current list.
    // If the name of the condition already exists, then do nothing.
    // Returns true if the condition is added and false otherwise.
    public boolean addCondition(Condition condition) {
        if (conditionExists(condition)) {
            return false;
        } else {
            conditions.add(condition);
            EventLog.getInstance().logEvent(new Event("Added a new condition, "
                    + condition.getName() + ", to the list of conditions."));
            return true;
        }
    }

    // MODIFIES: this
    // EFFECTS: Removes the genetic condition with the specified name from the current list.
    // If it doesn't exist, then do nothing.
    // Returns true if the condition is removed and false otherwise.
    public boolean removeCondition(String conditionName) {
        boolean found = false;
        List<Condition> newConditions = new ArrayList<Condition>();

        for (Condition c : this.conditions) {
            if (!c.getName().equals(conditionName)) {
                newConditions.add(c);
            } else {
                found = true;
                EventLog.getInstance().logEvent(new Event("Removed the condition, "
                        + conditionName + ", from the list of conditions."));
            }
        }

        this.conditions = newConditions;
        return found;
    }

    // EFFECTS: Returns true if the specified condition already exists in the list, false otherwise
    public boolean conditionExists(Condition condition) {
        for (Condition c : this.conditions) {
            if (c.getName().equals(condition.getName())) {
                return true;
            }
        }

        return false;
    }

    // EFFECTS: Returns the list of current genetic conditions
    public List<Condition> getConditions() {
        return conditions;
    }

    // EFFECTS: Converts ConditionList object to a JSONObject and returns it
    @Override
    public JSONArray toJsonArray() {
        JSONArray jsonArr = new JSONArray();

        for (Condition c : conditions) {
            jsonArr.put(c.toJson());
        }

        return jsonArr;
    }
}
