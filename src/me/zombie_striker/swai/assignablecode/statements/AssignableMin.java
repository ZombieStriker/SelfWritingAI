package me.zombie_striker.swai.assignablecode.statements;

import me.zombie_striker.swai.assignablecode.AssignableCode;
import me.zombie_striker.swai.assignablecode.AssignableField;
import me.zombie_striker.swai.assignablecode.AssignableStatement;
import me.zombie_striker.swai.data.PersonalityMatrix;

public class AssignableMin extends AssignableStatement {

    private PersonalityMatrix matrix;
    private int fieldindex;
    private int palletIndex1;
    private int palletIndex2;

    public AssignableMin(PersonalityMatrix matrix, int fieldindex, int palletIndex1, int palletIndex2) {
        super("MIN", null);
        this.matrix = matrix;
        this.fieldindex = fieldindex;
        this.palletIndex1 = palletIndex1;
        this.palletIndex2 = palletIndex2;
    }

    public void call() {
        if (fieldindex >= 0 && matrix.getCode().length > fieldindex)
            if (palletIndex1 >= 0 && matrix.getCode().length > palletIndex1)
                if (palletIndex2 >= 0 && matrix.getCode().length > palletIndex2)
            if (matrix.getCode()[fieldindex] instanceof AssignableField && matrix.getCode()[palletIndex1] instanceof AssignableField&& matrix.getCode()[palletIndex2] instanceof AssignableField) {
                if (((AssignableField) matrix.getCode()[fieldindex]).isReadOnly())
                    return;
                    matrix.getPallet()[((AssignableField) matrix.getCode()[fieldindex]).getReferenceID()] = Math.min(
                            (matrix.getIntAtField(palletIndex1)) , (matrix.getIntAtField(palletIndex2)));
            }
    }

    @Override
    public String toString() {
        return "MIN (" + fieldindex + "," + palletIndex1+"," +palletIndex2+ ");";
    }

    @Override
    public AssignableCode clone(PersonalityMatrix matrix) {
        return new AssignableMin(matrix, fieldindex, palletIndex1,palletIndex2);
    }

    public int getField() {
        return fieldindex;
    }

    public int getField1() {
        return palletIndex1;
    }
    public int getField2() {
        return palletIndex2;
    }
}
