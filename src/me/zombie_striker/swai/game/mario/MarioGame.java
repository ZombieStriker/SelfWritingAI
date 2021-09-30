package me.zombie_striker.swai.game.mario;

import me.zombie_striker.swai.Main;
import me.zombie_striker.swai.data.PersonalityMatrix;
import me.zombie_striker.swai.game.AbstractGame;
import me.zombie_striker.swai.game.defaults.Tile;
import me.zombie_striker.swai.world.Interpreter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MarioGame extends AbstractGame {

    private int tilesize = 1;

    private double marioX = 5;
    private double marioY = 3;
    private double marioYDir = 0;
    private double marioWalkSpeed = 0.3;

    private int marioWidth = 1;
    private int marioHeight = 1;

    private double wallX = 0;
    private double wallspeed = 0.1;


    private boolean goingRight = false;
    private boolean goingLeft = false;
    private boolean jumping = false;
    private boolean ducking = false;

    private int[] magicmatrix = {12, 110, 100, 9, 85, 7, 65, 5, 45, 3, 25, 1, 1005, 2005, 3003 ,400 ,5003, 2, 4, 6, 8, 10};

    private Tile[][] tilesAroundChar = new Tile[400][10];
    private double gravity = 0.09;
    private boolean fail = false;

    private PersonalityMatrix controller;
    private Interpreter interpreter;

    public MarioGame(PersonalityMatrix controller, Interpreter interpreter) {
        this.controller = controller;
        this.interpreter = interpreter;
        for (int i = 0; i < tilesAroundChar.length; i++) {
            generateTileFor(i);
        }
    }

    public int magicNumber(int seed) {
        return magicmatrix[seed % magicmatrix.length];
    }

    private void generateTileFor(int i) {
        int randomNumber1 = magicNumber(i);
        int randomNumber2 = magicNumber(randomNumber1);
        int randomNumber3 = magicNumber(randomNumber2);
        int randomNumber4 = magicNumber(randomNumber3);
        int randomNumber5 = magicNumber(randomNumber4);
        int randomNumber6 = magicNumber(randomNumber5);
        if (i < 0)
            return;
        if (i < 8) {
            for (int j = 0; j < 10; j++) {
                if (j == 0) {
                    tilesAroundChar[i][j] = new Tile(i, j, 1);
                } else {
                    tilesAroundChar[i][j] = new Tile(i, j, 0);
                }
            }
            return;
        }

        int height = randomNumber5 % 4;
        for (int j = 0; j < 10; j++) {
            if (height > j) {
                tilesAroundChar[i][j] = new Tile(i, j, randomNumber6 % 4);
            } else {
                tilesAroundChar[i][j] = new Tile(i, j, 0);
            }
        }

    }


    @Override
    public void handleInputs(int[] inputs) {
        goingRight = inputs[0]> 0;
        goingLeft = inputs[1]> 0;
        jumping = inputs[2]> 0;
    }

    @Override
    public void tick(int linesRan) {
        if (fail)
            return;
        Tile tileUnderMario = null;
        if ((marioX / tilesize) >= 0)
            if (marioY -1 >= 0 && marioY -1 < tilesAroundChar[(int) (marioX / tilesize)].length)
                tileUnderMario = tilesAroundChar[(int) (marioX / tilesize)][(int) (marioY -1)];

        Tile tileFrontofMario = null;
        if ((marioX / tilesize) >= 0)
            if (marioY >= 0 && marioY + marioYDir < tilesAroundChar[(int) (marioX / tilesize)].length)
                tileFrontofMario = tilesAroundChar[(int) ((marioX + marioWidth+1) / tilesize)][(int) (marioY)];

        Tile tileBackofMario = null;
        if ((marioX / tilesize) >= 0)
            if (marioY >= 0 && marioY + marioYDir < tilesAroundChar[(int) (marioX / tilesize)].length)
                tileBackofMario = tilesAroundChar[(int) ((marioX - 1) / tilesize)][(int) (marioY)];

            if(tileUnderMario==null || tileUnderMario.getType()==0){
                //Air tile
                marioYDir -= gravity;
            }

        if (goingRight)
            if (tileFrontofMario != null && tileFrontofMario.getType() == 0) {
                marioX+=marioWalkSpeed;
               interpreter.increaseScore(controller, 100000);
            }
        if (goingLeft)
            if (tileBackofMario != null && tileBackofMario.getType() == 0) {
                marioX -= marioWalkSpeed;
                Main.gameworldInterpreter.loseScore(controller, 100000);
            }


        if (jumping) {
            if (tileUnderMario != null && tileUnderMario.getType() != 0) {
                marioYDir = 1;
            }
        } else {
            if (tileUnderMario != null && tileUnderMario.getType() != 0) {
                marioYDir = 0;
            }
        }

        marioY += marioYDir;

        Main.gameworldInterpreter.loseScore(controller,linesRan/1000);
        if (tileUnderMario == null && marioY < 4) {
            //   fail = true;
            interpreter.onTerminate(controller);
        }
        wallX += wallspeed;
        if (wallX >= marioX) {
            interpreter.onTerminate(controller);
        }
    }

    int screentilewidth = 10;

    @Override
    public BufferedImage render() {
        BufferedImage subimage = new BufferedImage(screentilewidth * tilesize, tilesAroundChar[(int) (0 / tilesize)].length * tilesize, BufferedImage.TYPE_INT_RGB);
        Graphics2D gs = (Graphics2D) subimage.getGraphics();
        if (fail) {
            gs.setColor(Color.RED);
            gs.fillRect(0, 0, subimage.getWidth(), subimage.getHeight());
        } else {
            gs.setColor(Color.BLACK);
            gs.fillRect(0, 0, subimage.getWidth(), subimage.getHeight());
            int mariooffset = (int) (marioX - (screentilewidth / 2));
            for (int x = mariooffset; x < mariooffset + screentilewidth; x++) {
                if (x >= 0)
                    for (int y = 0; y < tilesAroundChar[x].length; y++) {
                        Tile tile = tilesAroundChar[x][y];
                        if (tile == null)
                            continue;
                        if (tile.getType() == 0) {
                            gs.setColor(Color.CYAN);
                        } else if (tile.getType() == 1) {
                            gs.setColor(Color.MAGENTA);
                        } else if (tile.getType() == 2) {
                            gs.setColor(Color.GREEN);
                        } else if (tile.getType() == 3) {
                            gs.setColor(Color.RED);
                        }
                        gs.fillRect((x * tilesize) - (mariooffset * tilesize), (tilesAroundChar[x].length - 1 - y) * tilesize, tilesize, tilesize);
                    }
            }
            gs.setColor(Color.WHITE);
            gs.fillRect((int) (marioX - mariooffset), (int) (10 - marioY) * tilesize, marioWidth, marioHeight);
            gs.setColor(Color.RED);
            gs.fillRect(0, 0, (int) (wallX - (mariooffset)), 10 * tilesize);
        }
        gs.dispose();
        return subimage;
    }

    @Override
    public void onTerminate() {

    }

    @Override
    public void gameTick() {

    }

    @Override
    public int[] getVision() {
        int[] vision = new int[3 + 100];
        vision[0] = (byte) marioX;
        vision[1] = (byte) marioY;
        vision[2] = (byte) (marioYDir*100);
        for (int i = 0; i < 100; i++) {
            int x = i % 10;
            int y = i / 10;
            if(x+marioX-5 < 0) {
                vision[3+i] = 5;
            }else if (tilesAroundChar[(int) (x+marioX-5)][y] == null) {
                vision[3 + i] = -1;
            } else {
                vision[3 + i] = (byte) tilesAroundChar[(int) (x+marioX-5)][y].getType();
            }
        }
        return vision;
    }
}
