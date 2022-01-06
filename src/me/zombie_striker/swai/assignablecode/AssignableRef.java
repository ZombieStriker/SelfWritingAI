package me.zombie_striker.swai.assignablecode;

import me.zombie_striker.swai.data.PersonalityMatrix;

public class AssignableRef extends AssignableCode{

    private int ref;

    public AssignableRef(int ref) {
        super("REF");
        this.ref=  ref;
    }

    @Override
    public AssignableCode clone(PersonalityMatrix matrix) {
        return new AssignableRef(ref);
    }

    @Override
    public String toString() {
        return "REF ("+ref+")";
    }

    public int getRef() {
        return ref;
    }
}
