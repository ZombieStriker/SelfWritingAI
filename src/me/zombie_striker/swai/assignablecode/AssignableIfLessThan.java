package me.zombie_striker.swai.assignablecode;

import me.zombie_striker.swai.data.PersonalityMatrix;

public class AssignableIfLessThan extends AssignableCode implements  AssignableIf{

    private PersonalityMatrix matrix;
    private int pallet1;
    private int pallet2;

    public AssignableIfLessThan(PersonalityMatrix matrix, int palletIndex1 , int palletIndex2) {
        super("IFLESSTHAN");
        this.matrix = matrix;
        this.pallet1 = palletIndex1;
        this.pallet2 = palletIndex2;
    }

    public boolean compare(){
        if (pallet1 < 0 || pallet2 < 0 || pallet1 >= matrix.getCode().length || pallet2 >= matrix.getCode().length)
            return false;
        if(matrix.getCode()[pallet1] instanceof AssignableField && matrix.getCode()[pallet2] instanceof AssignableField) {

            AssignableField field1 = (AssignableField) matrix.getCode()[pallet1];
            AssignableField field2 = (AssignableField) matrix.getCode()[pallet2];
            if (field1.getObjectInstance() < field2.getObjectInstance()) {
                return true;
            }
        }
        return false;
    }



    @Override
    public String toString() {
        return "IFLESSTHAN ("+pallet1+" , "+pallet2+") {";
    }

    @Override
    public AssignableCode clone(PersonalityMatrix matrix) {
        return new AssignableIfLessThan(matrix,pallet1,pallet2);
    }

    public int getVar1(){
        return pallet1;
    }

    public int getVar2() {
        return pallet2;
    }


    public void setVar1(int var1){
        this.pallet1 = var1;
    }
    public void setVar2(int var2){
        this.pallet2 = var2;
    }
}
