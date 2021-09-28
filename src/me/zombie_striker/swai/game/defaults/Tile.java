package me.zombie_striker.swai.game.defaults;

import me.zombie_striker.swai.data.PersonalityMatrix;

public class Tile {

    private double x;
    private double y;
    private int type;

    public Tile(double x, double y, int type){
        this.x =x;
        this.y = y;
        this.type = type;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getType() {
        return type;
    }

    public void setX(double v) {
        this.x = v;
    }
    public void setY(double v){
        this.y = v;
    }
}
