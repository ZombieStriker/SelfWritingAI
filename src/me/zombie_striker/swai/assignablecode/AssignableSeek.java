package me.zombie_striker.swai.assignablecode;

import me.zombie_striker.swai.data.PersonalityMatrix;

public class AssignableSeek extends AssignableCode{

    private String name;

    public AssignableSeek(String name){
        super("SEEK");
        this.name = name;
    }

    @Override
    public String toString() {
        return "SEEK ("+name+")";
    }

    public String getMarkerName() {
        return name;
    }

    @Override
    public AssignableCode clone(PersonalityMatrix matrix) {
        return new AssignableSeek(name);
    }
}
