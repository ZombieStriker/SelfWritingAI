package me.zombie_striker.swai.assignablecode.statements;

import me.zombie_striker.swai.assignablecode.AssignableCode;
import me.zombie_striker.swai.assignablecode.AssignableField;
import me.zombie_striker.swai.assignablecode.AssignableStatement;
import me.zombie_striker.swai.data.PersonalityMatrix;

public class AssignableModBy extends AssignableStatement {

    private PersonalityMatrix matrix;
    private int fieldindex;
    private int palletIndex;

    public AssignableModBy(PersonalityMatrix matrix, int fieldindex, int palletIndex) {
        super("MOD", null);
        this.matrix = matrix;
        this.fieldindex = fieldindex;
        this.palletIndex = palletIndex;
    }

    public void call() {
        if (fieldindex >= 0 && matrix.getCode().length > fieldindex)
            if (palletIndex >= 0 && matrix.getCode().length > palletIndex)
                if (matrix.getCode()[fieldindex] instanceof AssignableField && matrix.getCode()[palletIndex] instanceof AssignableField) {
                    if (((AssignableField) matrix.getCode()[fieldindex]).isReadOnly())
                        return;
                    if (matrix.getIntAtField(palletIndex) == 0)
                        return;
                    if (matrix.getIntAtField(fieldindex) < 0 || matrix.getIntAtField(fieldindex) >= matrix.getPallet().length)
                        return;
                    matrix.getPallet()[matrix.getIntAtField(fieldindex)] =
                            (matrix.getIntAtField(fieldindex) % (matrix.getIntAtField(palletIndex)));

                }
    }

    @Override
    public String toString() {
        return "MOD (" + fieldindex + "," + palletIndex + ");";
    }

    @Override
    public AssignableCode clone(PersonalityMatrix matrix) {
        return new AssignableModBy(matrix, fieldindex, palletIndex);
    }

    public int getField() {
        return fieldindex;
    }

    public int getIncrementField() {
        return palletIndex;
    }
}
