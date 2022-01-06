package me.zombie_striker.swai.assignablecode.statements;

import me.zombie_striker.swai.assignablecode.AssignableArrayField;
import me.zombie_striker.swai.assignablecode.AssignableCode;
import me.zombie_striker.swai.assignablecode.AssignableField;
import me.zombie_striker.swai.assignablecode.AssignableStatement;
import me.zombie_striker.swai.data.DataBank;
import me.zombie_striker.swai.data.PersonalityMatrix;

public class AssignableNeuron extends AssignableStatement {

    private int arrayindex;
    private int writeIndex;
    private PersonalityMatrix matrix;

    public AssignableNeuron(PersonalityMatrix matrix, int writeindex, int arrayIndex) {
        super("NEURON",null);
        this.matrix = matrix;
        this.arrayindex = arrayIndex;
        this.writeIndex = writeindex;
    }

    @Override
    public AssignableCode clone(PersonalityMatrix matrix) {
        return new AssignableNeuron(matrix,writeIndex,arrayindex);
    }

    @Override
    public String toString() {
        return "NEURON ("+writeIndex+","+arrayindex+")";
    }

    @Override
    public void call() {
        if(matrix.getCode().length > arrayindex && matrix.getCode().length > writeIndex)
        if(matrix.getCode()[arrayindex] instanceof AssignableArrayField && matrix.getCode()[writeIndex] instanceof AssignableField){
            AssignableArrayField array = (AssignableArrayField) matrix.getCode()[arrayindex];
            int value = 0;
            for(int i = 0; i < array.getArrayLength();i++){
                value += array.getObjectInstanceAt(i);
            }
            matrix.getPallet()[((AssignableField) matrix.getCode()[writeIndex]).getPalletIndex()] = (int) (DataBank.sigmoid(value/100)*100);
        }
    }
}
