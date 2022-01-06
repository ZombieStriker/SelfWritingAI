package me.zombie_striker.swai.game.pong.entities;

public class PongPaddle {

    private int x;
    private int y;

    private final int height = 10;

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHeight() {
        return height;
    }
}
