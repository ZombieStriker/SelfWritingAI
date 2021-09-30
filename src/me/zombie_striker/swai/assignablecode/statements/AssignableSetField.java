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
            if (fieldindex >= 0 && matrix.getCode().length > fieldindex)
                if (matrix.getCode()[fieldindex] instanceof AssignableField && matrix.getCode()[palletIndex] instanceof AssignableField) {
                    if (((AssignableField) matrix.getCode()[fieldindex]).isReadOnly())
                        return;
                    matrix.getPallet()[((AssignableField) matrix.getCode()[fieldindex]).getPalletIndex()] = ((AssignableField) matrix.getCode()[palletIndex]).getObjectInstance();
                }
        } else {
            if (fieldindex >= 0 && matrix.getCode().length > fieldindex)
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
            return "SETF(" + fieldindex + "," + palletIndex + "); ";
        } else {
            return "SET (" + fieldindex + "," + palletIndex + "); ";
        }
    }

    @Override
    public AssignableCode clone(PersonalityMatrix matrix) {
        return new AssignableSetField(matrix, fieldindex, useField, palletIndex);
    }

    public int getField() {
        return fieldindex;
    }

    public int getSecondField() {
        return palletIndex;
    }

    public boolean isUseField() {
        return useField;
    }
}
