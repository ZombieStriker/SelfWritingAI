package me.zombie_striker.swai.assignablecode;

import me.zombie_striker.swai.data.PersonalityMatrix;

public class AssignableReturn extends AssignableCode {
    public AssignableReturn() {
        super("RETURN");
    }

    @Override
    public AssignableCode clone(PersonalityMatrix matrix) {
        return new AssignableReturn();
    }

    @Override
    public String toString() {
        return "RETURN;";
    }
}
