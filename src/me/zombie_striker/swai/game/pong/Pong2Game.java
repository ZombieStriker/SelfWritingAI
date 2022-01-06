package me.zombie_striker.swai.game.pong;

import me.zombie_striker.swai.data.PersonalityMatrix;
import me.zombie_striker.swai.game.AbstractGame;
import me.zombie_striker.swai.world.Interpreter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class Pong2Game extends AbstractGame {


    private boolean fail = false;

    private int width = 40;
    private int height = 40;

    private PersonalityMatrix player1;
    private PersonalityMatrix player2;

    private int player2HeightLastHit = -1;
    private int player1HeightLastHit = -1;
    private int sameSpotCounter = 0;

    private int whowon = -1;

    public Pong2Game(PersonalityMatrix[] controllers, Interpreter interpreter, int round){
        super(new PongMap(controllers,40,40), controllers,interpreter);
        this.player1 = controllers[0];
        this.player2 = controllers[1];
    }

    public void handleInputs(PersonalityMatrix matrix, int[] inputs) {
        if(inputs[0]> 0&&inputs[1]> 0){
         return;
        }
        int paddleIndex = (matrix==player1)?0:1;
        if (inputs[0] > 0) {
           // Main.gameworldInterpreter.loseScore(controller,1);
            if (((PongMap)getMap()).getPaddles()[paddleIndex].getY() > 0) {
                ((PongMap)getMap()).getPaddles()[paddleIndex].setY(((PongMap)getMap()).getPaddles()[0].getY()-1);
            }
        }
        if (inputs[1]> 0) {
          //  Main.gameworldInterpreter.loseScore(controller,1);
            if (((PongMap)getMap()).getPaddles()[paddleIndex].getY()+((PongMap)getMap()).getPaddles()[0].getHeight() < height) {(
                    (PongMap)getMap()).getPaddles()[paddleIndex].setY(((PongMap)getMap()).getPaddles()[0].getY()+1);
            }
        }
    }

    public void tick(int linesRan) {
    }

    @Override
    public BufferedImage render() {
        BufferedImage subimage = new BufferedImage(40, 40, BufferedImage.TYPE_INT_RGB);
        Graphics2D gs = (Graphics2D) subimage.getGraphics();
        if(fail){
            if(whowon==0) {
                gs.setColor(new Color(168, 16, 16));
            }else{
                gs.setColor(new Color(16, 16, 168));
            }
            gs.fillRect(0,0,subimage.getWidth(),subimage.getHeight());
        }else {
            gs.setColor(Color.BLACK);
            gs.fillRect(0, 0, subimage.getWidth(), subimage.getHeight());
            gs.setColor(Color.WHITE);
            gs.fillRect(((PongMap)getMap()).getBall().getX(), ((PongMap)getMap()).getBall().getY(), 1, 1);
            for(int i = 0 ; i < getControllers().length;i++)
            gs.fillRect(((PongMap)getMap()).getPaddles()[i].getX(), ((PongMap)getMap()).getPaddles()[i].getY(), 1, ((PongMap)getMap()).getPaddles()[i].getHeight());
        }
        gs.dispose();
        return subimage;
    }

    @Override
    public void onTerminate() {

    }

    @Override
    public void gameTick() {
        if(fail) {
            getInterpreter().onTerminate(getControllers()[0]);
            getInterpreter().onTerminate(getControllers()[1]);
            return;
        }
        //Main.world.loseScore(controller,linesRan);
        if(((PongMap)getMap()).getBall().getX() + ((PongMap)getMap()).getBall().getDx()< 0){
            fail=true;
            whowon = 0;
            getInterpreter().increaseScore(player1,getInterpreter().getScore(player2));
            return;
        }
        //Hits directly
        if((((PongMap)getMap()).getBall().getX() + ((PongMap)getMap()).getBall().getDx())  == ((PongMap)getMap()).getPaddles()[0].getX()) {
            if ((((PongMap) getMap()).getBall().getY() >= ((PongMap) getMap()).getPaddles()[0].getY()) && (((PongMap) getMap()).getBall().getY() <= ((PongMap) getMap()).getPaddles()[0].getY() + ((PongMap) getMap()).getPaddles()[0].getHeight())) {
                ((PongMap) getMap()).getBall().setDx(-((PongMap) getMap()).getBall().getDx());

                ((PongMap) getMap()).getBall().setDy(((PongMap) getMap()).getBall().getDy() + ((((PongMap) getMap()).getBall().getY() - (((PongMap) getMap()).getPaddles()[0].getY() + ((PongMap) getMap()).getPaddles()[0].getHeight() / 2))));

                if(player1HeightLastHit!=((PongMap)getMap()).getPaddles()[0].getY()) {
                    player1HeightLastHit=((PongMap)getMap()).getPaddles()[0].getY();
                    getInterpreter().increaseScore(getControllers()[0], height);
                    sameSpotCounter=0;
                }else{
                    sameSpotCounter++;
                    if(sameSpotCounter > 5){
                        fail=true;
                    }
                }
            }
        }
        if((((PongMap)getMap()).getBall().getX() + ((PongMap)getMap()).getBall().getDx())  == ((PongMap)getMap()).getPaddles()[1].getX()){
            if((((PongMap)getMap()).getBall().getY() >= ((PongMap)getMap()).getPaddles()[1].getY()) && (((PongMap)getMap()).getBall().getY() <= ((PongMap)getMap()).getPaddles()[1].getY()+((PongMap)getMap()).getPaddles()[1].getHeight())) {
                ((PongMap)getMap()).getBall().setDx(-((PongMap)getMap()).getBall().getDx());

                ((PongMap)getMap()).getBall().setDy(((PongMap)getMap()).getBall().getDy() + ((((PongMap)getMap()).getBall().getY() - (((PongMap)getMap()).getPaddles()[1].getY()+((PongMap)getMap()).getPaddles()[1].getHeight()/2))));

                if(player2HeightLastHit!=((PongMap)getMap()).getPaddles()[1].getY()) {
                    player2HeightLastHit = ((PongMap) getMap()).getPaddles()[1].getY();
                    getInterpreter().increaseScore(getControllers()[1], height);
                    sameSpotCounter=0;
                }else{
                    sameSpotCounter++;
                    if(sameSpotCounter > 5){
                        fail=true;
                    }
                }
            }
        }

        //How close when it fails
        if((((PongMap)getMap()).getBall().getX()==((PongMap)getMap()).getPaddles()[0].getX())){
            if(((PongMap)getMap()).getPaddles()[0].getY() < (((PongMap)getMap()).getBall().getY())){
                getInterpreter().increaseScore(getControllers()[0],2*height-((((PongMap)getMap()).getBall().getY()-(((PongMap)getMap()).getPaddles()[0].getY()+(((PongMap)getMap()).getPaddles()[0].getHeight()/2)))));
            }else{
                getInterpreter().increaseScore(getControllers()[0],2*height-((((PongMap)getMap()).getPaddles()[0].getY()+(((PongMap)getMap()).getPaddles()[0].getHeight()/2))-((PongMap)getMap()).getBall().getY()));
            }
        }
        if((((PongMap)getMap()).getBall().getX()==((PongMap)getMap()).getPaddles()[1].getX())){
            if(((PongMap)getMap()).getPaddles()[1].getY() < (((PongMap)getMap()).getBall().getY())){
                getInterpreter().increaseScore(getControllers()[1],2*height-((((PongMap)getMap()).getBall().getY()-(((PongMap)getMap()).getPaddles()[1].getY()+(((PongMap)getMap()).getPaddles()[1].getHeight()/2)))));
            }else{
                getInterpreter().increaseScore(getControllers()[1],2*height-((((PongMap)getMap()).getPaddles()[1].getY()+(((PongMap)getMap()).getPaddles()[1].getHeight()/2))-((PongMap)getMap()).getBall().getY()));
            }
        }


        if(((PongMap)getMap()).getBall().getX() + (((PongMap)getMap()).getBall().getDx() )> width){
            fail=true;
            getInterpreter().increaseScore(player2,getInterpreter().getScore(player1));
            whowon=1;
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
    public boolean displayOneView() {
        return true;
    }
    @Override
    public int[] getVision(PersonalityMatrix matrix) {
        int[] vision = new int[10];
        Arrays.fill(vision, (byte) 0);
        int paddleIndex = (matrix==player1)?0:1;

        vision[0]= (byte) ((PongMap)getMap()).getBall().getX();
        vision[1]= (byte) ((PongMap)getMap()).getBall().getY();
        vision[2]= (byte) ((PongMap)getMap()).getBall().getDx();
        vision[3]= (byte) ((PongMap)getMap()).getBall().getDy();
        vision[4]= (byte) ((PongMap)getMap()).getPaddles()[paddleIndex].getX();
        vision[5]= (byte) ((PongMap)getMap()).getPaddles()[paddleIndex].getY();
        vision[6]= (byte) ((PongMap)getMap()).getPaddles()[paddleIndex].getHeight();
        return vision;
    }

    @Override
    public boolean isActive() {
        return !fail;
    }
}
