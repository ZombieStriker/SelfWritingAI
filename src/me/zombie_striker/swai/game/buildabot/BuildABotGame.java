package me.zombie_striker.swai.game.buildabot;

import me.zombie_striker.swai.assignablecode.*;
import me.zombie_striker.swai.assignablecode.statements.*;
import me.zombie_striker.swai.data.PersonalityMatrix;
import me.zombie_striker.swai.game.AbstractGame;
import me.zombie_striker.swai.game.GameEnum;
import me.zombie_striker.swai.game.pong.PongGame;
import me.zombie_striker.swai.world.Interpreter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BuildABotGame extends AbstractGame implements Interpreter {

    private PersonalityMatrix matrixToWorkOn;
    private int indexOfWritingCode = 0;
    private int interationsOfWritingCode = 0;

    private boolean goodToGo = false;
    private int rewriteAttempts = 0;

    private int gameScore = 0;
    private int highestGameScore = 0;

    private AbstractGame game;
    private GameEnum gametype;

    private boolean finetuning = false;

    public List<PersonalityMatrix> getMatrices() {
        return Collections.singletonList(matrixToWorkOn);
    }

    private boolean warpspeed = true;

    private int round;

    public BuildABotGame(PersonalityMatrix creator, Interpreter interpreter, GameEnum gametype, int round) {
        super(null,creator,interpreter);
        this.gametype = gametype;
        this.round = round;
        if (gametype == GameEnum.PONG) {
            matrixToWorkOn = new PersonalityMatrix(100, 8, 3, 10, false, 1);
            game = new PongGame(matrixToWorkOn, this, 0);
        }
    }

    public void setMatrixToWorkOn(PersonalityMatrix work) {
        this.matrixToWorkOn = work;
        goodToGo = false;
    }

    public PersonalityMatrix getMatrixWorkedOn() {
        return matrixToWorkOn;
    }

    @Override
    public void handleInputs(PersonalityMatrix old, int[] inputs) {
        if (goodToGo)
            return;
        int highest_score = 0;
        int index = 0;
        for (int i = 0; i < 25; i++) {
            if (inputs[i] > highest_score) {
                index = i;
            }
        }
        if (matrixToWorkOn != null) {
            if (index == 0) {
                matrixToWorkOn.getCode()[indexOfWritingCode] = new AssignableReturn();
            } else if (index == 1) {
                //TODO: Finish handling all code inputs
            } else {

            }
        }
    }

    @Override
    public void tick(int linesRan) {
        if (!goodToGo) {
            for (int i = 0; i < 50; i++) {
                indexOfWritingCode++;
                if (indexOfWritingCode >= matrixToWorkOn.getCode().length) {
                    indexOfWritingCode = 0;
                    interationsOfWritingCode++;
                }
                if (interationsOfWritingCode >= 1) {
                    goodToGo = true;
                    break;
                }
            }
        } else {
            int[] vision = game.getVision(matrixToWorkOn);
            for (int i = 0; i < vision.length; i++) {
                matrixToWorkOn.getPalletReadOnly()[i] = vision[i];
            }
            int linesSubRan = matrixToWorkOn.run(false, -1);
            game.handleInputs(matrixToWorkOn,handleSubGameInputs(matrixToWorkOn));
            game.tick(linesRan);
        }
    }

    public int[] handleSubGameInputs(PersonalityMatrix matrix) {
        int[] inputs = interpretPallet(matrix, matrix.getPallet().length - 1 - matrix.getPalletsForInputs(), matrix.getPalletsForInputs());
        return inputs;
    }

    public int[] interpretPallet(PersonalityMatrix matrix, int startingIndex, int amountOfEntries) {
        int[] b = new int[amountOfEntries];
        for (int i = 0; i < amountOfEntries; i++) {
            b[i] = (matrix.getPallet()[startingIndex + i]);
        }
        return b;
    }

    @Override
    public BufferedImage render() {
        if (goodToGo) {
            return game.render();
        }
        BufferedImage bi = new BufferedImage(200, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) bi.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
        g.setColor(Color.WHITE);
        g.drawString("Line" + indexOfWritingCode + " of " + matrixToWorkOn.getCode().length + "  (" + ((indexOfWritingCode * 100) / matrixToWorkOn.getCode().length) + "% complete).", 0, 50);
        g.drawString("Iterations = " + interationsOfWritingCode, 0, 60);
        g.setColor(new Color(145, 190, 123));
        g.drawString("Round " + rewriteAttempts, 0, 70);
        g.dispose();
        return bi;
    }

    @Override
    public void onTerminate() {

    }

    @Override
    public void gameTick() {

    }

    @Override
    public int[] getVision(PersonalityMatrix old) {
        int batch = 6;
        int[] linesOfCode = new int[100 * batch + 2];
        for (int i = Math.max(0, indexOfWritingCode - 50); i < Math.min(indexOfWritingCode + 50, matrixToWorkOn.getCode().length); i++) {
            int lineCode = -1;
            int variable1 = -1;
            int variable2 = -1;
            int variable3 = -1;
            int variable4 = -1;
           //TODO: Work on vision

            linesOfCode[(i * batch) + 0] = i;
            linesOfCode[(i * batch) + 1] = lineCode;
            linesOfCode[(i * batch) + 2] = variable1;
            linesOfCode[(i * batch) + 3] = variable2;
            linesOfCode[(i * batch) + 4] = variable3;
            linesOfCode[(i * batch) + 5] = variable4;

        }
        linesOfCode[100 * batch] = gameScore;
        linesOfCode[100 * batch + 1] = goodToGo ? 1 : 0;
        if (goodToGo) {
            int[] gamevision = game.getVision(matrixToWorkOn);
            for (int i = 0; i < Math.min(gamevision.length, 17); i++) {
                linesOfCode[batch + 2 + i] = gamevision[i];
            }
        }
        return linesOfCode;
    }

    @Override
    public void loseScore(PersonalityMatrix matrix, int score) {
        gameScore -= Math.max(0, score);
        getInterpreter().loseScore(getControllers()[0], Math.max(0,score));
    }

    @Override
    public void increaseScore(PersonalityMatrix matrix, int score) {
        gameScore += score;
        getInterpreter().increaseScore(getControllers()[0], Math.max(0,score));
    }

    public void onTerminate(PersonalityMatrix controller) {
        if (rewriteAttempts >= Math.min(round/10,50)) {
            getInterpreter().onTerminate(getControllers()[0]);
            return;
        }
        goodToGo = false;
        indexOfWritingCode = 0;
        interationsOfWritingCode = 0;
        if (highestGameScore < gameScore) {
            highestGameScore = gameScore;
            if(highestGameScore>0)
            getInterpreter().increaseScore(getControllers()[0], 1000 * highestGameScore);
        }
        gameScore = 0;
        rewriteAttempts++;

        matrixToWorkOn = new PersonalityMatrix(100, 8, 3, 10, false, 1);
        game = gametype.createNewGame(new PersonalityMatrix[]{matrixToWorkOn}, this, rewriteAttempts,1);
    }

    @Override
    public boolean displayOneView() {
        return true;
    }
    @Override
    public int getScore(PersonalityMatrix matrix) {
        if (matrix == matrixToWorkOn)
            return gameScore;
        if (matrix == getControllers()[0])
            return highestGameScore;
        return 0;
    }

    @Override
    public boolean getWarpSpeed() {
        return warpspeed;
    }

    @Override
    public void setWarpSpeed(boolean warpSpeed) {
        this.warpspeed = warpSpeed;
    }


    public boolean hasEnabledFineTuning() {
        return finetuning;
    }

    public void setFineTuning(boolean b) {
        this.finetuning = b;
    }

    @Override
    public void divideScoreBy(PersonalityMatrix matrix, int i) {

    }

    @Override
    public boolean isActive() {
        return rewriteAttempts < Math.min(round/10,50);
    }
}
