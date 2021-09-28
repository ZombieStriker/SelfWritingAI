package me.zombie_striker.swai.assignablecode.statements;

import me.zombie_striker.swai.Main;
import me.zombie_striker.swai.assignablecode.AssignableCode;
import me.zombie_striker.swai.assignablecode.AssignableField;
import me.zombie_striker.swai.assignablecode.AssignableStatement;
import me.zombie_striker.swai.data.DataBank;
import me.zombie_striker.swai.data.PersonalityMatrix;


public class AssignablePostField extends AssignableStatement {

    private final PersonalityMatrix personalityMatrix;
    private int fieldindex;

    public AssignablePostField(PersonalityMatrix matrix, int fieldIndex) {
        super("PRINT", null);
        this.personalityMatrix = matrix;
        this.fieldindex = fieldIndex;
    }

    public void call() {
        if (personalityMatrix.getCode()[fieldindex] instanceof AssignableField) {
            AssignableField field = (AssignableField) personalityMatrix.getCode()[fieldindex];
            int value = personalityMatrix.getByteForField(field.getPalletIndex());
            //Main.world.pressedKey(personalityMatrix, value);
            //System.out.println(DataBank.ANSI_BLUE + field.getName()+": "+DataBank.translate(value) + DataBank.ANSI_RESET);
        }
    }

    @Override
    public String toString() {
        if (personalityMatrix.getCode()[fieldindex] instanceof AssignableField)
            return "PRINT(" + fieldindex + ") (" + DataBank.translate(personalityMatrix.getByteForField(personalityMatrix.getByteForField(fieldindex))) + ")";
        return "PRINT (" + fieldindex + ") (INVALID)";
    }

    @Override
    public AssignableCode clone(PersonalityMatrix matrix) {
        return new AssignablePostField(matrix, fieldindex);
    }
}
