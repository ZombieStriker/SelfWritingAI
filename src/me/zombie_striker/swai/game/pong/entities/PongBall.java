package me.zombie_striker.swai.game.pong.entities;

public class PongBall {

    private int x;
    private int y;
    private int dx;
    private int dy;

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
