package me.zombie_striker.swai.assignablecode.statements;

import me.zombie_striker.swai.assignablecode.AssignableCode;
import me.zombie_striker.swai.assignablecode.AssignableField;
import me.zombie_striker.swai.assignablecode.AssignableStatement;
import me.zombie_striker.swai.data.PersonalityMatrix;

public class AssignableSetField extends AssignableStatement {

    private PersonalityMatrix matrix;
    private int fieldindex;
    private boolean useField;
    private int palletIndex;

    public AssignableSetField(PersonalityMatrix matrix, int fieldindex, boolean useField, int palletIndex) {
        super("SET", null);
        this.matrix = matrix;
        this.fieldindex = fieldindex;
        this.useField = useField;
        this.palletIndex = palletIndex;
    }

    public void call() {
        if (useField) {
            if (matrix.getCode()[fieldindex] instanceof AssignableField && matrix.getCode()[palletIndex] instanceof AssignableField) {
                if (((AssignableField) matrix.getCode()[fieldindex]).isReadOnly())
                    return;
                matrix.getPallet()[((AssignableField) matrix.getCode()[fieldindex]).getPalletIndex()] = ((AssignableField) matrix.getCode()[palletIndex]).getObjectInstance();
            }
        } else {
            if (matrix.getCode()[fieldindex] instanceof AssignableField) {
                if (((AssignableField) matrix.getCode()[fieldindex]).isReadOnly())
                    return;
                matrix.getPallet()[((AssignableField) matrix.getCode()[fieldindex]).getPalletIndex()] = matrix.getPallet()[palletIndex];
            }
        }
    }

    @Override
    public String toString() {
        if (useField) {
            if (matrix.getCode()[fieldindex] instanceof AssignableField && matrix.getCode()[palletIndex] instanceof AssignableField)
                if(matrix.getByteForField(fieldindex) >= 0 && matrix.getByteForField(fieldindex) < matrix.getPallet().length)
                return "SET (" + fieldindex + " | " + useField + " |" + palletIndex + ");   (" + (
                        matrix.getPallet()[matrix.getByteForField(fieldindex)] + " | " + ((AssignableField) matrix.getCode()[palletIndex]).getObjectInstance()
                ) + ")";
            return "SET (" + fieldindex + " | " + useField + " |" + palletIndex + "); (INVALID)";
        } else {
            if (matrix.getCode()[fieldindex] instanceof AssignableField)

                return "SET (" + fieldindex + " | " + useField + " |" + palletIndex + ");   (" + (
                        matrix.getPallet()[matrix.getByteForField(fieldindex)] + " | " + matrix.getPallet()[palletIndex]
                ) + ")";
            return "SET (" + fieldindex + " | " + useField + " |" + palletIndex + "); (INVALID)";
        }
    }

    @Override
    public AssignableCode clone(PersonalityMatrix matrix) {
        return new AssignableSetField(matrix, fieldindex, useField, palletIndex);
    }
    public int getField() {
        return fieldindex;
    }
    public int getSecondField(){
        return palletIndex;
    }

    public boolean isUseField() {
        return useField;
    }
}
