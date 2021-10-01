package me.zombie_striker.swai.assignablecode.statements;

import me.zombie_striker.swai.assignablecode.AssignableCode;
import me.zombie_striker.swai.assignablecode.AssignableField;
import me.zombie_striker.swai.assignablecode.AssignableStatement;
import me.zombie_striker.swai.data.PersonalityMatrix;

public class AssignableDistance extends AssignableStatement {

    private PersonalityMatrix matrix;
    private int fieldindex;
    private int palletIndex1;
    private int palletIndex2;
    private int palletIndex3;
    private int palletIndex4;

    public AssignableDistance(PersonalityMatrix matrix, int fieldindex, int palletIndex1, int palletIndex2,int palletIndex3, int palletIndex4) {
        super("DISTANCE", null);
        this.matrix = matrix;
        this.fieldindex = fieldindex;
        this.palletIndex1 = palletIndex1;
        this.palletIndex2 = palletIndex2;
        this.palletIndex3 = palletIndex3;
        this.palletIndex4 = palletIndex4;
    }

    public void call() {
        if (fieldindex >= 0 && matrix.getCode().length > fieldindex)
            if (matrix.getCode()[fieldindex] instanceof AssignableField && matrix.getCode()[palletIndex1] instanceof AssignableField&& matrix.getCode()[palletIndex2] instanceof AssignableField) {
                if (((AssignableField) matrix.getCode()[fieldindex]).isReadOnly())
                    return;
                if (matrix.getIntAtField(palletIndex1) < 0 || matrix.getIntAtField(palletIndex1) >= matrix.getPallet().length)
                    return;
                if (matrix.getIntAtField(palletIndex2) < 0 || matrix.getIntAtField(palletIndex2) >= matrix.getPallet().length)
                    return;
                if (matrix.getIntAtField(palletIndex3) < 0 || matrix.getIntAtField(palletIndex3) >= matrix.getPallet().length)
                    return;
                if (matrix.getIntAtField(palletIndex4) < 0 || matrix.getIntAtField(palletIndex4) >= matrix.getPallet().length)
                    return;
                if (matrix.getIntAtField(fieldindex) < 0 || matrix.getIntAtField(fieldindex) >= matrix.getPallet().length)
                    return;
                matrix.getPallet()[matrix.getIntAtField(fieldindex)] = (int) Math.sqrt(Math.abs((matrix.getIntAtField(palletIndex1)-(matrix.getIntAtField(palletIndex2))
                        + ((matrix.getIntAtField(palletIndex3)-(matrix.getIntAtField(palletIndex4)))))));
            }
    }

    @Override
    public String toString() {
        return "DISTANCE (" + fieldindex + "," + palletIndex1 +  "," + palletIndex2 +","+ palletIndex3 +  "," + palletIndex4 +");";
    }

    @Override
    public AssignableCode clone(PersonalityMatrix matrix) {
        return new AssignableDistance(matrix, fieldindex, palletIndex1, palletIndex2,palletIndex3,palletIndex4);
    }

    public int getField() {
        return fieldindex;
    }
}
