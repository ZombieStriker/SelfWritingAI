package me.zombie_striker.swai.assignablecode;

import me.zombie_striker.swai.data.PersonalityMatrix;

public class AssignableCloseBracket extends AssignableCode {
    public AssignableCloseBracket() {
        super("}");
    }

    @Override
    public AssignableCode clone(PersonalityMatrix matrix) {
        return new AssignableCloseBracket();
    }

    @Override
    public String toString() {
        return "}";
    }
}
