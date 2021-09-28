package me.zombie_striker.swai.game.battledroids;

import me.zombie_striker.swai.data.PersonalityMatrix;
import me.zombie_striker.swai.game.defaults.Tile;

public class BulletTile extends Tile {

    private double dx;
    private double dz;
    private PersonalityMatrix shooter;

    public BulletTile(double x, double y, double dx, double dz, PersonalityMatrix shooter) {
        super(x, y, 7);
        this.dx = dx;
        this.dz = dz;
        this.shooter= shooter;
    }

    public double getXD() {
        return dx;
    }
    public double getZD(){
        return dz;
    }

    public PersonalityMatrix getShooter() {
        return shooter;
    }
}
