package me.zombie_striker.swai.assignablecode;

import me.zombie_striker.swai.data.PersonalityMatrix;

public class AssignableIfEquals extends AssignableCode{

    private PersonalityMatrix matrix;
    private int pallet1;
    private int pallet2;
    private int skippablelines;

    public AssignableIfEquals(PersonalityMatrix matrix, int palletIndex1 , int palletIndex2, int skippableLines) {
        super("IFEQUALS");
        this.matrix = matrix;
        this.pallet1 = palletIndex1;
        this.pallet2 = palletIndex2;
        this.skippablelines = skippableLines;
    }

    public boolean compare(){
        if(matrix.getCode()[pallet1] instanceof AssignableField && matrix.getCode()[pallet2] instanceof AssignableField) {
            AssignableField field1 = (AssignableField) matrix.getCode()[pallet1];
            AssignableField field2 = (AssignableField) matrix.getCode()[pallet2];
            if (field1.getObjectInstance() == field2.getObjectInstance()) {
                return true;
            }
        }
        return false;
    }

    public int getSkippableLines() {
        return skippablelines;
    }


    @Override
    public String toString() {
        if(matrix.getCode()[pallet1] instanceof AssignableField && matrix.getCode()[pallet2] instanceof AssignableField)
        return "IF EQUALS ("+pallet1+" "+(matrix.getCode()[pallet1].getName()+" | "+pallet2+" "+matrix.getCode()[pallet2].getName())+") (SKIPS "+skippablelines+")";
        return "IF EQUALS ("+pallet1+" | "+pallet2+" INVALID)";
    }

    @Override
    public AssignableCode clone(PersonalityMatrix matrix) {
        return new AssignableIfEquals(matrix,pallet1,pallet2,skippablelines);
    }


    public int getVar1(){
        return pallet1;
    }

    public int getVar2() {
        return pallet2;
    }
}
