package me.zombie_striker.swai.assignablecode;

import me.zombie_striker.swai.data.PersonalityMatrix;

public class AssignableForEach extends AssignableCode implements AssignableFor{
    private final int indexstore;
    private final int loopthrough;
    private final PersonalityMatrix matrix;

    public AssignableForEach(PersonalityMatrix matrix, int loopthrough, int indexStore) {
        super("FOREACH");
        this.matrix = matrix;
        this.loopthrough = loopthrough;
        this.indexstore = indexStore;
    }

    @Override
    public AssignableCode clone(PersonalityMatrix matrix) {
        return new AssignableForEach(matrix,loopthrough,indexstore);
    }

    @Override
    public boolean loopagain() {
        if(indexstore < 0 || loopthrough < 0 || loopthrough >= matrix.getCode().length || indexstore >= matrix.getCode().length)
            return false;
        if(matrix.getCode()[indexstore] instanceof  AssignableField && matrix.getCode()[loopthrough] instanceof AssignableArrayField) {
            boolean loopagain = (( this.matrix.getPallet()[((AssignableField) matrix.getCode()[indexstore]).getPalletIndex()] < ((AssignableArrayField)matrix.getCode()[loopthrough]).getArrayLength()));
            this.matrix.getPallet()[((AssignableField) matrix.getCode()[indexstore]).getPalletIndex()] = this.matrix.getPallet()[((AssignableField) matrix.getCode()[indexstore]).getPalletIndex()] + 1;
            return loopagain;
        }
        return false;
    }

    @Override
    public String toString() {
        return "FOREACH ("+loopthrough+","+indexstore+")";
    }
}
