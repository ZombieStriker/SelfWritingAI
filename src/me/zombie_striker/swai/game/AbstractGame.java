package me.zombie_striker.swai.game;

import me.zombie_striker.swai.data.PersonalityMatrix;
import me.zombie_striker.swai.world.Interpreter;

import java.awt.image.BufferedImage;

public abstract class AbstractGame {

    private Interpreter interpreter;
    private IMap map;
    private PersonalityMatrix[] controllers;

    public AbstractGame(IMap map, PersonalityMatrix controller, Interpreter interpreter){
        this.map = map;
        this.controllers = new PersonalityMatrix[]{controller};
        this.interpreter = interpreter;
    }
    public AbstractGame(IMap map, PersonalityMatrix[] controllers, Interpreter interpreter){
        this.map = map;
        this.controllers = controllers;
        this.interpreter = interpreter;
    }

    public Interpreter getInterpreter() {
        return interpreter;
    }

    public PersonalityMatrix[] getControllers() {
        return controllers;
    }

    public IMap getMap(){
        return map;
    }

    public abstract void handleInputs(PersonalityMatrix matrix, int[] inputs);

    public abstract void tick(int linesRan);

    public abstract BufferedImage render();

    public abstract void onTerminate();

    public abstract void gameTick();

    public abstract int[] getVision(PersonalityMatrix matrix);

    public abstract boolean isActive();

    public abstract boolean displayOneView();

    public int getControllerIndex(PersonalityMatrix matrix){
        for(int i = 0; i < controllers.length;i++){
            if(controllers[i]==matrix)
                return i;
        }
        return -1;
    }
}
