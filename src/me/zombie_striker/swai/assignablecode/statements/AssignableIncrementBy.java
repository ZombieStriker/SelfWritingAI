package me.zombie_striker.swai.assignablecode.statements;

import me.zombie_striker.swai.assignablecode.AssignableCode;
import me.zombie_striker.swai.assignablecode.AssignableField;
import me.zombie_striker.swai.assignablecode.AssignableStatement;
import me.zombie_striker.swai.data.PersonalityMatrix;

public class AssignableIncrementBy extends AssignableStatement {

    private PersonalityMatrix matrix;
    private int fieldindex;
    private int palletIndex;

    public AssignableIncrementBy(PersonalityMatrix matrix, int fieldindex, int palletIndex) {
        super("INCBY", null);
        this.matrix = matrix;
        this.fieldindex = fieldindex;
        this.palletIndex = palletIndex;
    }

    public void call() {
        if (fieldindex >= 0 && matrix.getCode().length > fieldindex)
            if (matrix.getCode()[fieldindex] instanceof AssignableField && matrix.getCode()[palletIndex] instanceof AssignableField) {
                if (((AssignableField) matrix.getCode()[fieldindex]).isReadOnly())
                    return;
                if (matrix.getByteForField(palletIndex) < 0 || matrix.getByteForField(palletIndex) >= matrix.getPallet().length)
                    return;
                matrix.getPallet()[matrix.getByteForField(palletIndex)] =
                        (byte) (matrix.getByteForField(palletIndex) + (matrix.getPallet()[matrix.getByteForField(palletIndex)]));
            }
    }

    @Override
    public String toString() {
        if (matrix.getCode()[fieldindex] instanceof AssignableField && matrix.getCode()[palletIndex] instanceof AssignableField)

            if (matrix.getByteForField(palletIndex) >= 0 && matrix.getByteForField(palletIndex) < matrix.getPallet().length)
                return "INCBY (" + matrix.getCode()[fieldindex].getName() + "=L" + fieldindex + " | " + palletIndex + ");   (" + (
                        matrix.getPallet()[matrix.getByteForField(palletIndex)] + " | " + matrix.getByteForField(palletIndex)
                ) + ")";
        if (matrix.getCode()[fieldindex] == null)
            return "INCBY (INVALID) (INVALID)";
        return "INCBY (" + matrix.getCode()[fieldindex].getName() + "=L" + fieldindex + " | " + palletIndex + ");   (" + ("INVALID") + ")";
    }

    @Override
    public AssignableCode clone(PersonalityMatrix matrix) {
        return new AssignableIncrementBy(matrix, fieldindex, palletIndex);
    }

    public int getField() {
        return fieldindex;
    }

    public int getIncrementField() {
        return palletIndex;
    }
}
