package me.zombie_striker.swai.assignablecode;

import me.zombie_striker.swai.data.PersonalityMatrix;

public class AssignableGoSub extends AssignableCode{

    private int lineToJumpTo;

    public AssignableGoSub(int lineToJumpTo) {
        super("GOSUB");
        this.lineToJumpTo = lineToJumpTo;
    }

    public int getLineToJumpTo() {
        return lineToJumpTo;
    }

    @Override
    public AssignableCode clone(PersonalityMatrix matrix) {
        return null;
    }

    @Override
    public String toString() {
        return "GOSUB ("+lineToJumpTo+")";
    }
}
