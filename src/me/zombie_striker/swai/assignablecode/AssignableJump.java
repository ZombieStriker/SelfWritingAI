package me.zombie_striker.swai.assignablecode;

import me.zombie_striker.swai.data.PersonalityMatrix;

public class AssignableJump extends AssignableCode{

    private int lineToSkipTo;
    private PersonalityMatrix matrix;
    private boolean useField;

    public AssignableJump(PersonalityMatrix matrix, int lineToSkipTo, boolean useField) {
        super("JUMP");
        this.matrix = matrix;
        this.useField = useField;
        this.lineToSkipTo = lineToSkipTo;
    }

    public int getLineToSkipTo() {
        return lineToSkipTo;
    }

    public boolean usesField() {
        return useField;
    }
    @Override
    public String toString() {
        if (useField) {
            if(matrix.getCode()[lineToSkipTo] instanceof AssignableField) {
                return "JUMP ("+lineToSkipTo + ")  (" + matrix.getByteForField(lineToSkipTo) + ")";
            }else{
                return "JUMP (INVALID)";
            }
        } else {
            return "JUMPTO (" + lineToSkipTo + ")";
        }
    }

    @Override
    public AssignableCode clone(PersonalityMatrix matrix) {
        return new AssignableJump(matrix,lineToSkipTo,useField);
    }
}
