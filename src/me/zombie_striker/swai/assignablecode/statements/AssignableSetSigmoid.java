package me.zombie_striker.swai.assignablecode.statements;

import me.zombie_striker.swai.assignablecode.AssignableCode;
import me.zombie_striker.swai.assignablecode.AssignableField;
import me.zombie_striker.swai.assignablecode.AssignableStatement;
import me.zombie_striker.swai.data.DataBank;
import me.zombie_striker.swai.data.PersonalityMatrix;

import java.util.Random;

public class AssignableSetSigmoid extends AssignableStatement {

    private PersonalityMatrix matrix;
    private int fieldindex;
private int extendedRangePallet;

    public AssignableSetSigmoid(PersonalityMatrix matrix, int fieldindex, int extendedRange) {
        super("SIGMOID", null);
        this.matrix = matrix;
        this.fieldindex = fieldindex;
        this.extendedRangePallet = extendedRange;
    }

    public void call() {
        if (fieldindex >= 0 && matrix.getCode().length > fieldindex)
            if (extendedRangePallet >= 0 && matrix.getCode().length > extendedRangePallet)
            if (matrix.getCode()[fieldindex] instanceof AssignableField) {
                if (((AssignableField) matrix.getCode()[fieldindex]).isReadOnly())
                    return;
                    matrix.getPallet()[(((AssignableField) matrix.getCode()[fieldindex]).getPalletIndex())] = (int) (DataBank.sigmoid(((AssignableField) matrix.getCode()[fieldindex]).getObjectInstance()*extendedRangePallet) );
            }
    }

    @Override
    public String toString() {
        return "SIGMOID (" + fieldindex +","+extendedRangePallet+ ");";
    }

    @Override
    public AssignableCode clone(PersonalityMatrix matrix) {
        return new AssignableSetSigmoid(matrix,  fieldindex,extendedRangePallet);
    }

    public int getField() {
        return fieldindex;
    }

}
