package me.zombie_striker.swai.game.pong;

import me.zombie_striker.swai.data.PersonalityMatrix;
import me.zombie_striker.swai.game.AbstractGame;
import me.zombie_striker.swai.world.Interpreter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class PongGame extends AbstractGame {

    private int scalar = 10;

    private int ballX = 0*scalar;
    private int ballY = 20*scalar;

    private int ballDirX= 1*scalar;
    private int ballDirY = 1 *scalar;

    private int paddleHeight = 4*scalar;
    private int paddleHeightOffset = (20-8)*scalar;
    private int paddleXOffset = 35*scalar;
    private boolean fail = false;

    private PersonalityMatrix controller;
    private Interpreter interpreter;

    public PongGame(PersonalityMatrix controller, Interpreter interpreter, int round){
        this.controller = controller;
        this.interpreter = interpreter;
      //  ballDirY = (round%3)-1;
    }

    public void handleInputs(int[] inputs) {
        if(inputs[0]> 0&&inputs[1]> 0){
         return;
        }
        if (inputs[0] > 0) {
           // Main.gameworldInterpreter.loseScore(controller,1);
            if (paddleHeightOffset > 0) {
                paddleHeightOffset -= scalar;
            }
        }
        if (inputs[1]> 0) {
          //  Main.gameworldInterpreter.loseScore(controller,1);
            if (paddleHeightOffset+paddleHeight < 40*scalar) {
                paddleHeightOffset += scalar;
            }
        }
    }

    public void tick(int linesRan) {
        if(fail)
            return;
        //Main.world.loseScore(controller,linesRan);
        if(ballX + ballDirX < 0){
            ballDirX=-ballDirX;
        }
        if((ballX + ballDirX)  == paddleXOffset){
            if(ballY >= paddleHeightOffset && ballY <= paddleHeightOffset+paddleHeight) {
                ballDirX=-ballDirX;

                ballDirY = ballDirY + ((ballY - (paddleHeightOffset+paddleHeight/2))/scalar);

                interpreter.increaseScore(controller,100);
            }
        }
        if(ballX==paddleXOffset){
            if(paddleHeightOffset < ballY){
                interpreter.increaseScore(controller,30-(ballY-(paddleHeightOffset+paddleHeight)));
            }else{
                interpreter.increaseScore(controller,30-((paddleHeightOffset)-ballY));
            }
        }
        if((ballX + ballDirX )> 40*scalar){
            //ballDirX=-ballDirX;
            //fail = true;
            interpreter.onTerminate(controller);
            return;
        }
        if((ballY + ballDirY) > 40*scalar){
            ballDirY=-ballDirY;
        }
        if((ballY + ballDirY) < 0*scalar){
            ballDirY=-ballDirY;
        }

        /*if(ballDirX < 0){
            ballDirX= Math.max(-1,-(velocity-ballDirY));
        }else if (ballDirX > 0){
            ballDirX= Math.min(1,(velocity-ballDirY));
        }else{
            ballDirX=1;
        }*/

        ballX += ballDirX;
        ballY += ballDirY;
    }

    @Override
    public BufferedImage render() {
        BufferedImage subimage = new BufferedImage(40, 40, BufferedImage.TYPE_INT_RGB);
        Graphics2D gs = (Graphics2D) subimage.getGraphics();
        if(fail){
            gs.setColor(Color.RED);
            gs.fillRect(0,0,subimage.getWidth(),subimage.getHeight());
        }else {
            gs.setColor(Color.BLACK);
            gs.fillRect(0, 0, subimage.getWidth(), subimage.getHeight());
            gs.setColor(Color.WHITE);
            gs.fillRect(ballX/scalar, ballY/scalar, 1, 1);
            gs.fillRect(paddleXOffset/scalar, paddleHeightOffset/scalar, 1, paddleHeight/scalar);
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
        int[] vision = new int[/*(9*9*2)+9*/10];
        Arrays.fill(vision, (byte) 0);

        vision[0]= (byte) ballX;
        vision[1]= (byte) ballY;
        vision[2]= (byte) ballDirX;
        vision[3]= (byte) ballDirY;
        vision[4]= (byte) paddleHeightOffset;
        vision[5]= (byte) paddleXOffset;
        vision[6]= (byte) paddleHeight;
        //vision[(xoffset) + (yoffset*9)] = 1;
        return vision;
    }
}
