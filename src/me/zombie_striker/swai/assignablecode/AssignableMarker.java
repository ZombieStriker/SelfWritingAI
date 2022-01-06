package me.zombie_striker.swai.assignablecode;

import me.zombie_striker.swai.data.PersonalityMatrix;

public class AssignableMarker extends AssignableCode{

    private String name;

    public AssignableMarker(String name){
        super("MARKER");
        this.name = name;
    }

    @Override
    public String toString() {
        return "MARKER ("+name+")";
    }

    public String getMarkerName() {
        return name;
    }
    @Override
    public AssignableCode clone(PersonalityMatrix matrix) {
        return new AssignableMarker(name);
    }
}
