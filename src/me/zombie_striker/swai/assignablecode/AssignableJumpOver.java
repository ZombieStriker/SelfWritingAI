package me.zombie_striker.swai.assignablecode;

import me.zombie_striker.swai.data.PersonalityMatrix;

public class AssignableJumpOver extends AssignableCode implements AssignableOpenBracketType{

    public AssignableJumpOver(PersonalityMatrix matrix) {
        super("JUMPOVER");
    }

    @Override
    public String toString() {
        return "JUMPOVER {";
    }

    @Override
    public AssignableCode clone(PersonalityMatrix matrix) {
        return new AssignableJumpOver(matrix);
    }
}
