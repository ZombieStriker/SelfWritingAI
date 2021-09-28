package me.zombie_striker.swai.assignablecode;

public abstract class AssignableStatement extends  AssignableCode{

    private Class[] types;

    public AssignableStatement(String name, Class[] types){
        super(name);
        this.types = types;
    }

    public abstract void call();
}
