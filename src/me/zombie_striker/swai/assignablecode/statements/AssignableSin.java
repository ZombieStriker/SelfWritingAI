package me.zombie_striker.swai.assignablecode.statements;

import me.zombie_striker.swai.assignablecode.AssignableCode;
import me.zombie_striker.swai.assignablecode.AssignableField;
import me.zombie_striker.swai.assignablecode.AssignableStatement;
import me.zombie_striker.swai.data.PersonalityMatrix;

public class AssignableSin extends AssignableStatement {

    private PersonalityMatrix matrix;
    private int fieldindex;
    private int palletIndex;

    public AssignableSin(PersonalityMatrix matrix, int fieldindex, int palletIndex) {
        super("SIN", null);
        this.matrix = matrix;
        this.fieldindex = fieldindex;
        this.palletIndex = palletIndex;
    }

    public void call() {
        if (fieldindex >= 0 && matrix.getCode().length > fieldindex) {
            if (palletIndex >= 0 && matrix.getCode().length > palletIndex)
            if (matrix.getCode()[fieldindex] instanceof AssignableField) {
                if (((AssignableField) matrix.getCode()[fieldindex]).isReadOnly())
                    return;
                matrix.getPallet()[((AssignableField) matrix.getCode()[fieldindex]).getPalletIndex()] = (int) Math.sin((matrix.getIntAtField(palletIndex)/50)*Math.PI);
            }
        }
    }

    @Override
    public String toString() {
        return "SIN (" + fieldindex + "," + palletIndex + "); ";
    }

    @Override
    public AssignableCode clone(PersonalityMatrix matrix) {
        return new AssignableSin(matrix, fieldindex, palletIndex);
    }

    public int getField() {
        return fieldindex;
    }

    public int getSecondField() {
        return palletIndex;
    }


    public void setFieldindex(int var1){
        this.fieldindex = var1;
    }
    public void setVar2(int var2){
        this.palletIndex = var2;
    }
}
