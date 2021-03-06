package me.zombie_striker.swai.assignablecode.statements;

import me.zombie_striker.swai.assignablecode.AssignableCode;
import me.zombie_striker.swai.assignablecode.AssignableField;
import me.zombie_striker.swai.assignablecode.AssignableStatement;
import me.zombie_striker.swai.data.PersonalityMatrix;

public class AssignableIncrement extends AssignableStatement {

    private PersonalityMatrix matrix;
    private int fieldindex;

    public AssignableIncrement(PersonalityMatrix matrix, int fieldindex) {
        super("INCREMENT", null);
        this.matrix = matrix;
        this.fieldindex = fieldindex;
    }

    public void call() {
        if (fieldindex >= 0 && matrix.getCode().length > fieldindex)
            if (matrix.getCode()[fieldindex] instanceof AssignableField) {
                if (((AssignableField) matrix.getCode()[fieldindex]).isReadOnly())
                    return;
                if (matrix.getIntAtField(fieldindex) < 0 || matrix.getIntAtField(fieldindex) >= matrix.getPallet().length)
                    return;
                matrix.getPallet()[((AssignableField) matrix.getCode()[fieldindex]).getPalletIndex()] = (matrix.getPallet()[((AssignableField) matrix.getCode()[fieldindex]).getPalletIndex()] + 1);
            }
    }


    @Override
    public String toString() {
        return "INC (" + fieldindex + ");";
    }

    @Override
    public AssignableCode clone(PersonalityMatrix matrix) {
        return new AssignableIncrement(matrix, fieldindex);
    }


    public int getField() {
        return fieldindex;
    }
}
