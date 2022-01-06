package me.zombie_striker.swai.game;

import me.zombie_striker.swai.data.PersonalityMatrix;
import me.zombie_striker.swai.game.battledroids.BattleDroidSimulatorGame;
import me.zombie_striker.swai.game.buildabot.BuildABotGame;
import me.zombie_striker.swai.game.imagerecreator.DrawImageGame;
import me.zombie_striker.swai.game.mario.MarioGame;
import me.zombie_striker.swai.game.pong.Pong2Game;
import me.zombie_striker.swai.game.pong.PongGame;
import me.zombie_striker.swai.game.writetext.WriteTextGame;
import me.zombie_striker.swai.world.Interpreter;

public enum GameEnum {
    PONG,
    MARIO,
    BATTLE,
    BUILDAPONG,
    WRITE_ALICE,
    DRAW_CAT,
    WRITESENTENCE,
    PONG2;

    public int getRequiredAmountOfControllers(){
        if(this == PONG2)
            return 2;
        if(this == DRAW_CAT)
            return 2;
        return 1;
    }

    public AbstractGame createNewGame(PersonalityMatrix[] matrix, Interpreter interpreter,int gameindex, int round){
        if(this==PONG)
            return new PongGame(matrix[0],interpreter, round);
        if(this==PONG2)
            return new Pong2Game(matrix,interpreter, round);
        if(this==MARIO)
            return new MarioGame(matrix[0],interpreter);
        if(this==BATTLE)
            return new BattleDroidSimulatorGame(matrix[0],interpreter,round);
        if(this==BUILDAPONG)
            return new BuildABotGame(matrix[0],interpreter, PONG, round);
        if(this== WRITE_ALICE)
            return new WriteTextGame(matrix[0],interpreter, round);
        if(this==DRAW_CAT)
            return new DrawImageGame(matrix,interpreter, round);

        return null;
    }
}
