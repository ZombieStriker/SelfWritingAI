package me.zombie_striker.swai.assignablecode.statements;

import me.zombie_striker.swai.assignablecode.AssignableCode;
import me.zombie_striker.swai.assignablecode.AssignableField;
import me.zombie_striker.swai.assignablecode.AssignableStatement;
import me.zombie_striker.swai.data.PersonalityMatrix;

public class AssignableAbsolute extends AssignableStatement {

    private PersonalityMatrix matrix;
    private int fieldindex;
    private int palletIndex;

    public AssignableAbsolute(PersonalityMatrix matrix, int fieldindex, int palletIndex) {
        super("ABS", null);
        this.matrix = matrix;
        this.fieldindex = fieldindex;
        this.palletIndex = palletIndex;
    }

    public void call() {
        if (fieldindex >= 0 && matrix.getCode().length > fieldindex)
            if (matrix.getCode()[fieldindex] instanceof AssignableField && matrix.getCode()[palletIndex] instanceof AssignableField) {
                if (((AssignableField) matrix.getCode()[fieldindex]).isReadOnly())
                    return;
                if (matrix.getIntAtField(palletIndex) < 0 || matrix.getIntAtField(palletIndex) >= matrix.getPallet().length)
                    return;
                if (matrix.getIntAtField(fieldindex) < 0 || matrix.getIntAtField(fieldindex) >= matrix.getPallet().length)
                    return;
                matrix.getPallet()[matrix.getIntAtField(fieldindex)] =
                        Math.abs(matrix.getIntAtField(palletIndex));
            }
    }

    @Override
    public String toString() {
        return "ABS (" + fieldindex + "," + palletIndex + ");";
    }

    @Override
    public AssignableCode clone(PersonalityMatrix matrix) {
        return new AssignableAbsolute(matrix, fieldindex, palletIndex);
    }

    public int getField() {
        return fieldindex;
    }

    public int getIncrementField() {
        return palletIndex;
    }
}
