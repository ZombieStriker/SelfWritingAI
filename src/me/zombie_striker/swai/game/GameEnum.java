package me.zombie_striker.swai.game;

import me.zombie_striker.swai.data.PersonalityMatrix;
import me.zombie_striker.swai.game.battledroids.BattleDroidSimulatorGame;
import me.zombie_striker.swai.game.buildabot.BuildABotGame;
import me.zombie_striker.swai.game.imagerecreator.DrawImageGame;
import me.zombie_striker.swai.game.mario.MarioGame;
import me.zombie_striker.swai.game.pong.PongGame;
import me.zombie_striker.swai.game.writetext.WriteTextGame;
import me.zombie_striker.swai.world.Interpreter;

public enum GameEnum {
    PONG,
    MARIO,
    BATTLE,
    BUILDAPONG,
    WRITE,
    DRAW_CAT;

    public AbstractGame createNewGame(PersonalityMatrix matrix, Interpreter interpreter, int round){
        if(this==PONG)
            return new PongGame(matrix,interpreter, round);
        if(this==MARIO)
            return new MarioGame(matrix,interpreter);
        if(this==BATTLE)
            return new BattleDroidSimulatorGame(matrix,interpreter,round);
        if(this==BUILDAPONG)
            return new BuildABotGame(matrix,interpreter, PONG);
        if(this==WRITE)
            return new WriteTextGame(matrix,interpreter, round);
        if(this==DRAW_CAT)
            return new DrawImageGame(matrix,interpreter, round);

        return null;
    }
}
