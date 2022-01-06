package me.zombie_striker.swai.assignablecode.statements;

import me.zombie_striker.swai.assignablecode.AssignableCode;
import me.zombie_striker.swai.assignablecode.AssignableField;
import me.zombie_striker.swai.assignablecode.AssignableStatement;
import me.zombie_striker.swai.data.PersonalityMatrix;

import java.util.Random;

public class AssignableSetRandField extends AssignableStatement {

    private PersonalityMatrix matrix;
    private int fieldindex;
    private int palletIndex;
    private int magicNumber;
    private int magicInc = 0;

    public AssignableSetRandField(PersonalityMatrix matrix, int magicNumber, int fieldindex, int palletIndex) {
        super("RAND", null);
        this.matrix = matrix;
        this.fieldindex = fieldindex;
        this.palletIndex = palletIndex;
        this.magicNumber = magicNumber;
    }

    public void call() {
        if (fieldindex >= 0 && matrix.getCode().length > fieldindex)
            if (palletIndex >= 0 && matrix.getCode().length > palletIndex)
            if (matrix.getCode()[fieldindex] instanceof AssignableField && matrix.getCode()[palletIndex] instanceof AssignableField) {
                if (((AssignableField) matrix.getCode()[fieldindex]).isReadOnly())
                    return;
                Random random = new Random(magicNumber * (magicInc + 1));
                magicInc++;
                int i = ((AssignableField) matrix.getCode()[palletIndex]).getObjectInstance();
                if (i > 0)
                    matrix.getPallet()[((AssignableField) matrix.getCode()[fieldindex]).getPalletIndex()] = random.nextInt(i);
            }
    }

    @Override
    public String toString() {
        return "RAND (" + fieldindex + "," + palletIndex + ");";
    }

    @Override
    public AssignableCode clone(PersonalityMatrix matrix) {
        return new AssignableSetRandField(matrix, magicNumber, fieldindex, palletIndex);
    }

    public int getField() {
        return fieldindex;
    }

    public int getSecondField() {
        return palletIndex;
    }
}
