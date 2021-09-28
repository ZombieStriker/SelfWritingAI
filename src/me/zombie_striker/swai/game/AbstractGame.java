package me.zombie_striker.swai.game;

import java.awt.*;

public abstract class AbstractGame {
    public abstract void handleInputs(int[] inputs);

    public abstract void tick(int linesRan);

    public abstract Image render();

    public abstract void onTerminate();

    public abstract void gameTick();

    public abstract int[] getVision();
}
