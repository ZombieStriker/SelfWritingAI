package me.zombie_striker.swai.game.pong;

import me.zombie_striker.swai.data.PersonalityMatrix;
import me.zombie_striker.swai.game.IMap;
import me.zombie_striker.swai.game.pong.entities.PongBall;
import me.zombie_striker.swai.game.pong.entities.PongPaddle;

public class PongMap implements IMap {

    private PongPaddle[] paddles;
    private PongBall ball;

    public PongMap(PersonalityMatrix[] controllers, int width, int height){
        paddles = new PongPaddle[controllers.length];
        for(int i = 0; i < controllers.length;i++){
            paddles[i] = new PongPaddle();
            if(i==0){
                paddles[i].setX(width-10);
                paddles[i].setY(height/2);
            }else{
                paddles[i].setX(10);
                paddles[i].setY(height/2);
            }
        }
        ball = new PongBall();
        if(paddles.length==1){
            ball.setX(1);
        }else {
            ball.setX(width / 2);
        }
        ball.setY(height/2);
        ball.setDx(1);
        ball.setDy(1);
    }

    public PongPaddle[] getPaddles() {
        return paddles;
    }

    public PongBall getBall() {
        return ball;
    }
}
