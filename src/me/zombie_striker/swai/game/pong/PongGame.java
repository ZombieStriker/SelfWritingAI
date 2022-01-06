package me.zombie_striker.swai.game.pong;

import me.zombie_striker.swai.data.PersonalityMatrix;
import me.zombie_striker.swai.game.AbstractGame;
import me.zombie_striker.swai.world.Interpreter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class PongGame extends AbstractGame {


    private boolean fail = false;

    private int width = 40;
    private int height = 40;
    public PongGame(PersonalityMatrix controller, Interpreter interpreter, int round){
        super(new PongMap(new PersonalityMatrix[]{controller},40,40), controller,interpreter);
    }

    public void handleInputs(PersonalityMatrix old, int[] inputs) {
        if(inputs[0]> 0&&inputs[1]> 0){
         return;
        }
        if (inputs[0] > 0) {
           // Main.gameworldInterpreter.loseScore(controller,1);
            if (((PongMap)getMap()).getPaddles()[0].getY() > 0) {
                ((PongMap)getMap()).getPaddles()[0].setY(((PongMap)getMap()).getPaddles()[0].getY()-1);
            }
        }
        if (inputs[1]> 0) {
          //  Main.gameworldInterpreter.loseScore(controller,1);
            if (((PongMap)getMap()).getPaddles()[0].getY()+((PongMap)getMap()).getPaddles()[0].getHeight() < height) {(
                    (PongMap)getMap()).getPaddles()[0].setY(((PongMap)getMap()).getPaddles()[0].getY()+1);
            }
        }
    }

    public void tick(int linesRan) {
        if(fail) {
            getInterpreter().onTerminate(getControllers()[0]);
            return;
        }
        //Main.world.loseScore(controller,linesRan);
        if(((PongMap)getMap()).getBall().getX() + ((PongMap)getMap()).getBall().getDx()< 0){
            ((PongMap)getMap()).getBall().setDx(-((PongMap)getMap()).getBall().getDx());
        }
        if((((PongMap)getMap()).getBall().getX() + ((PongMap)getMap()).getBall().getDx())  == ((PongMap)getMap()).getPaddles()[0].getX()){
            if((((PongMap)getMap()).getBall().getY() >= ((PongMap)getMap()).getPaddles()[0].getY()) && (((PongMap)getMap()).getBall().getY() <= ((PongMap)getMap()).getPaddles()[0].getY()+((PongMap)getMap()).getPaddles()[0].getHeight())) {
                ((PongMap)getMap()).getBall().setDx(-((PongMap)getMap()).getBall().getDx());

                ((PongMap)getMap()).getBall().setDy(((PongMap)getMap()).getBall().getDy() + ((((PongMap)getMap()).getBall().getY() - (((PongMap)getMap()).getPaddles()[0].getY()+((PongMap)getMap()).getPaddles()[0].getHeight()/2))));

                getInterpreter().increaseScore(getControllers()[0],100);
            }
        }
        if((((PongMap)getMap()).getBall().getX()==((PongMap)getMap()).getPaddles()[0].getX())){
            if(((PongMap)getMap()).getPaddles()[0].getY() < (((PongMap)getMap()).getBall().getY())){
                getInterpreter().increaseScore(getControllers()[0],50-((((PongMap)getMap()).getBall().getY()-(((PongMap)getMap()).getPaddles()[0].getY()+(((PongMap)getMap()).getPaddles()[0].getHeight()/2)))));
            }else{
                getInterpreter().increaseScore(getControllers()[0],50-((((PongMap)getMap()).getPaddles()[0].getY()+(((PongMap)getMap()).getPaddles()[0].getHeight()/2))-((PongMap)getMap()).getBall().getY()));
            }
        }
        if(((PongMap)getMap()).getBall().getX() + (((PongMap)getMap()).getBall().getDx() )> width){
            //ballDirX=-ballDirX;
            fail = true;
            return;
        }
        if((((PongMap)getMap()).getBall().getY() + ((PongMap)getMap()).getBall().getDy()) > height){
            ((PongMap)getMap()).getBall().setDy(-((PongMap)getMap()).getBall().getDy());
        }
        if((((PongMap)getMap()).getBall().getY() + ((PongMap)getMap()).getBall().getDy()) < 0){
            ((PongMap)getMap()).getBall().setDy(-((PongMap)getMap()).getBall().getDy());
        }

        /*if(ballDirX < 0){
            ballDirX= Math.max(-1,-(velocity-ballDirY));
        }else if (ballDirX > 0){
            ballDirX= Math.min(1,(velocity-ballDirY));
        }else{
            ballDirX=1;
        }*/

        ((PongMap)getMap()).getBall().setX(((PongMap)getMap()).getBall().getX()+((PongMap)getMap()).getBall().getDx());
        ((PongMap)getMap()).getBall().setY(((PongMap)getMap()).getBall().getY()+((PongMap)getMap()).getBall().getDy());
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
            gs.fillRect(((PongMap)getMap()).getBall().getX(), ((PongMap)getMap()).getBall().getY(), 1, 1);
            gs.fillRect(((PongMap)getMap()).getPaddles()[0].getX(), ((PongMap)getMap()).getPaddles()[0].getY(), 1, ((PongMap)getMap()).getPaddles()[0].getHeight());
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
    public boolean displayOneView() {
        return true;
    }
    @Override
    public int[] getVision(PersonalityMatrix old) {
        int[] vision = new int[10];
        Arrays.fill(vision, (byte) 0);

        vision[0]= (byte) ((PongMap)getMap()).getBall().getX();
        vision[1]= (byte) ((PongMap)getMap()).getBall().getY();
        vision[2]= (byte) ((PongMap)getMap()).getBall().getDx();
        vision[3]= (byte) ((PongMap)getMap()).getBall().getDy();
        vision[4]= (byte) ((PongMap)getMap()).getPaddles()[0].getX();
        vision[5]= (byte) ((PongMap)getMap()).getPaddles()[0].getY();
        vision[6]= (byte) ((PongMap)getMap()).getPaddles()[0].getHeight();
        return vision;
    }

    @Override
    public boolean isActive() {
        return !fail;
    }
}
