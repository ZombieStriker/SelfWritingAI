package me.zombie_striker.swai.assignablecode.statements;

import me.zombie_striker.swai.assignablecode.AssignableCode;
import me.zombie_striker.swai.assignablecode.AssignableField;
import me.zombie_striker.swai.assignablecode.AssignableStatement;
import me.zombie_striker.swai.data.PersonalityMatrix;

public class AssignableDecrement extends AssignableStatement {

    private PersonalityMatrix matrix;
    private int fieldindex;

    public AssignableDecrement(PersonalityMatrix matrix, int fieldindex) {
        super("DECREMENT", null);
        this.matrix = matrix;
        this.fieldindex = fieldindex;
    }

    public void call() {
        if(fieldindex >= 0 && matrix.getCode().length > fieldindex)
        if(matrix.getCode()[fieldindex] instanceof AssignableField) {
            if(((AssignableField) matrix.getCode()[fieldindex]).isReadOnly())
            return;
            if(matrix.getByteForField(fieldindex)<0 || matrix.getByteForField(fieldindex)  >= matrix.getPallet().length )
    return;
                matrix.getPallet()[((AssignableField) matrix.getCode()[fieldindex]).getPalletIndex()] = (byte) (matrix.getPallet()[((AssignableField) matrix.getCode()[fieldindex]).getPalletIndex()] - 1);

        }
    }

    @Override
    public String toString() {
        if(matrix.getCode()[fieldindex] instanceof AssignableField)
            if(matrix.getByteForField(fieldindex)>=0 && matrix.getByteForField(fieldindex)  < matrix.getPallet().length )
                return "DECREMENT (" + matrix.getCode()[fieldindex] .getName()+"=L"+fieldindex   + ");   (" + (
                matrix.getPallet()[matrix.getByteForField(fieldindex)]
        ) + ")";
        if(matrix.getCode()[fieldindex]==null)
            return "DECREMENT (INVALID) (INVALID)";
        return "DECREMENT (" + matrix.getCode()[fieldindex] .getName()+"=L"+fieldindex   + ");   (" +"INVALID"
         + ")";
    }

    @Override
    public AssignableCode clone(PersonalityMatrix matrix) {
        return new AssignableDecrement(matrix, fieldindex);
    }

    public int getField() {
        return fieldindex;
    }
}
