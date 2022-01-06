package me.zombie_striker.swai.assignablecode;

import me.zombie_striker.swai.data.PersonalityMatrix;

public class AssignableIfContains extends AssignableCode implements  AssignableIf{

    private PersonalityMatrix matrix;
    private int arrayPallet;
    private int pallet2;

    public AssignableIfContains(PersonalityMatrix matrix, int palletIndex1 , int palletIndex2) {
        super("IFCONTAINS");
        this.matrix = matrix;
        this.arrayPallet = palletIndex1;
        this.pallet2 = palletIndex2;
    }

    public boolean compare(){
        if (arrayPallet < 0 || pallet2 < 0 || arrayPallet >= matrix.getCode().length || pallet2 >= matrix.getCode().length)
            return false;
        if(matrix.getCode()[arrayPallet] instanceof AssignableArrayField && matrix.getCode()[pallet2] instanceof AssignableField) {
            AssignableArrayField field1 = (AssignableArrayField) matrix.getCode()[arrayPallet];
            AssignableField field2 = (AssignableField) matrix.getCode()[pallet2];
            for(int i = 0; i < field1.getArrayLength();i++){
                if(field1.getObjectInstanceAt(i) == matrix.getIntAtField(field2))
                    return true;
            }
        }
        return false;
    }


    @Override
    public String toString() {
        return "IFCONTAINS ("+ arrayPallet +","+pallet2+") {";
    }

    @Override
    public AssignableCode clone(PersonalityMatrix matrix) {
        return new AssignableIfContains(matrix, arrayPallet,pallet2);
    }


    public int getVar1(){
        return arrayPallet;
    }

    public int getVar2() {
        return pallet2;
    }


    public void setVar1(int var1){
        this.arrayPallet = var1;
    }
    public void setVar2(int var2){
        this.pallet2 = var2;
    }
}
