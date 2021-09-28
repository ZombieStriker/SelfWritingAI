package me.zombie_striker.swai.assignablecode;

import me.zombie_striker.swai.data.PersonalityMatrix;

public abstract  class AssignableCode {

    private String name;

    public AssignableCode(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    @Override
    public String toString() {
        return name+"();";
    }

    public abstract AssignableCode clone(PersonalityMatrix matrix);

}
