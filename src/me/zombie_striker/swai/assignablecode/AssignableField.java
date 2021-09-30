package me.zombie_striker.swai.assignablecode;

import me.zombie_striker.swai.data.DataBank;
import me.zombie_striker.swai.data.PersonalityMatrix;

public class AssignableField extends AssignableCode{

    private int referenceID;
    private boolean readonly;
    private PersonalityMatrix matrix;

    public AssignableField(PersonalityMatrix matrix, String name, int referenceID, boolean useReadOnly) {
        super(name);
        this.matrix = matrix;
        this.referenceID = referenceID;
        this.readonly = useReadOnly;
    }


    public int getObjectInstance(){
        if(readonly)
          return  matrix.getPalletReadOnly()[referenceID];
        return matrix.getPallet()[referenceID];
    }

    @Override
    public String toString() {
        if(readonly)
            return "FIELDRO ("+getName()+","+referenceID +")";
        return "FIELD ("+getName()+","+referenceID +")";
    }

    @Override
    public AssignableCode clone(PersonalityMatrix matrix) {
        return new AssignableField(matrix,getName(),referenceID,readonly);
    }

    public int getPalletIndex() {
        return referenceID;
    }

    public boolean isReadOnly() {
        return readonly;
    }
    public int getReferenceID() {
        return referenceID;
    }
}
