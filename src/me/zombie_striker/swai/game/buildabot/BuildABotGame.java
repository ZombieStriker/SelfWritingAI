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

    private PersonalityMatrix creator;
    private Interpreter interpreter;

    public List<PersonalityMatrix> getMatrices() {
        return Collections.singletonList(matrixToWorkOn);
    }

    private boolean warpspeed = true;

    public BuildABotGame(PersonalityMatrix creator, Interpreter interpreter, GameEnum gametype) {
        this.gametype = gametype;
        this.creator = creator;
        this.interpreter = interpreter;
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
    public void handleInputs(int[] inputs) {
        if (goodToGo)
            return;
        int highest_score = 0;
        int index = 0;
        for (int i = 0; i < 14; i++) {
            if (inputs[i] > highest_score) {
                index = i;
            }
        }
        if (matrixToWorkOn != null) {
            if (index == 0) {
                //Do nothing. Write nothing.
            } else if (index == 1) {
                matrixToWorkOn.getCode()[indexOfWritingCode] = new AssignableReturn();
                //  } else if (index == 2) {
                //     matrixToWorkOn.getCode()[indexOfWritingCode] = new AssignableField(matrixToWorkOn,);
                //TODO: DO NOT WRITE FIELDS RANDOMLY
            } else if (index == 3) {
                matrixToWorkOn.getCode()[indexOfWritingCode] = new AssignableJump(matrixToWorkOn, inputs[19], false);
            } else if (index == 4) {
                matrixToWorkOn.getCode()[indexOfWritingCode] = new AssignableJump(matrixToWorkOn, inputs[19], true);
            } else if (index == 5) {
                matrixToWorkOn.getCode()[indexOfWritingCode] = new AssignableIfLessThan(matrixToWorkOn, inputs[17], inputs[18], inputs[19]);
            } else if (index == 6) {
                matrixToWorkOn.getCode()[indexOfWritingCode] = new AssignableIfEquals(matrixToWorkOn, inputs[17], inputs[18], inputs[19]);
            } else if (index == 7) {
                matrixToWorkOn.getCode()[indexOfWritingCode] = new AssignableGoSub(inputs[19]);
            } else if (index == 8) {
                matrixToWorkOn.getCode()[indexOfWritingCode] = new AssignableIncrement(matrixToWorkOn, inputs[17]);
            } else if (index == 9) {
                matrixToWorkOn.getCode()[indexOfWritingCode] = new AssignableDecrement(matrixToWorkOn, inputs[17]);
            } else if (index == 10) {
                matrixToWorkOn.getCode()[indexOfWritingCode] = new AssignableAdd(matrixToWorkOn, inputs[17], inputs[18]);
            } else if (index == 11) {
                matrixToWorkOn.getCode()[indexOfWritingCode] = new AssignableSubtract(matrixToWorkOn, inputs[17], inputs[18]);
            } else if (index == 12) {
                matrixToWorkOn.getCode()[indexOfWritingCode] = new AssignableSetField(matrixToWorkOn, inputs[17], inputs[18]);
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
            int[] vision = game.getVision();
            for (int i = 0; i < vision.length; i++) {
                matrixToWorkOn.getPalletReadOnly()[i] = vision[i];
            }
            int linesSubRan = matrixToWorkOn.run(false, -1);
            game.handleInputs(handleSubGameInputs(matrixToWorkOn));
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
    public int[] getVision() {
        int batch = 5;
        int[] linesOfCode = new int[100 * batch + 2];
        for (int i = Math.max(0, indexOfWritingCode - 50); i < Math.min(indexOfWritingCode + 50, matrixToWorkOn.getCode().length); i++) {
            int lineCode = -1;
            int variable1 = -1;
            int variable2 = -1;
            int variable3 = -1;
           /* if (matrixToWorkOn.getCode()[i] == null || matrixToWorkOn.getCode()[i] instanceof AssignablePostField) {
                lineCode = 0;
            } else */
            if (matrixToWorkOn.getCode()[i] instanceof AssignableReturn) {
                lineCode = 1;
            } else if (matrixToWorkOn.getCode()[i] instanceof AssignableField) {
                lineCode = 2;
                variable1 = ((AssignableField) matrixToWorkOn.getCode()[i]).getReferenceID();
            } else if (matrixToWorkOn.getCode()[i] instanceof AssignableJump) {
                if (((AssignableJump) matrixToWorkOn.getCode()[i]).usesField()) {
                    lineCode = 3;
                    variable3 = ((AssignableJump) matrixToWorkOn.getCode()[i]).getLineToSkipTo();
                } else {
                    lineCode = 4;
                    variable3 = ((AssignableJump) matrixToWorkOn.getCode()[i]).getLineToSkipTo();
                }
            } else if (matrixToWorkOn.getCode()[i] instanceof AssignableIfLessThan) {
                lineCode = 5;
                variable1 = ((AssignableIfLessThan) matrixToWorkOn.getCode()[i]).getVar1();
                variable2 = ((AssignableIfLessThan) matrixToWorkOn.getCode()[i]).getVar2();
                variable3 = ((AssignableIfLessThan) matrixToWorkOn.getCode()[i]).getSkippableLines();
            } else if (matrixToWorkOn.getCode()[i] instanceof AssignableIfEquals) {
                lineCode = 6;
                variable1 = ((AssignableIfEquals) matrixToWorkOn.getCode()[i]).getVar1();
                variable2 = ((AssignableIfEquals) matrixToWorkOn.getCode()[i]).getVar2();
                variable3 = ((AssignableIfEquals) matrixToWorkOn.getCode()[i]).getSkippableLines();
            } else if (matrixToWorkOn.getCode()[i] instanceof AssignableGoSub) {
                lineCode = 7;
                variable3 = ((AssignableGoSub) matrixToWorkOn.getCode()[i]).getLineToJumpTo();
            } else if (matrixToWorkOn.getCode()[i] instanceof AssignableDecrement) {
                lineCode = 8;
                variable1 = ((AssignableDecrement) matrixToWorkOn.getCode()[i]).getField();
            } else if (matrixToWorkOn.getCode()[i] instanceof AssignableIncrement) {
                lineCode = 9;
                variable1 = ((AssignableIncrement) matrixToWorkOn.getCode()[i]).getField();
            } else if (matrixToWorkOn.getCode()[i] instanceof AssignableAdd) {
                lineCode = 10;
                variable1 = ((AssignableAdd) matrixToWorkOn.getCode()[i]).getField();
                variable2 = ((AssignableAdd) matrixToWorkOn.getCode()[i]).getIncrementField();
            } else if (matrixToWorkOn.getCode()[i] instanceof AssignableSubtract) {
                lineCode = 11;
                variable1 = ((AssignableSubtract) matrixToWorkOn.getCode()[i]).getField();
                variable2 = ((AssignableSubtract) matrixToWorkOn.getCode()[i]).getIncrementField();
            } else if (matrixToWorkOn.getCode()[i] instanceof AssignableSetField) {
                lineCode = 12;
                variable1 = ((AssignableSetField) matrixToWorkOn.getCode()[i]).getField();
                variable2 = ((AssignableSetField) matrixToWorkOn.getCode()[i]).getSecondField();
            } else {

            }

            linesOfCode[(i * batch) + 0] = i;
            linesOfCode[(i * batch) + 1] = lineCode;
            linesOfCode[(i * batch) + 2] = variable1;
            linesOfCode[(i * batch) + 3] = variable2;
            linesOfCode[(i * batch) + 4] = variable3;

        }
        linesOfCode[100 * batch] = gameScore;
        linesOfCode[100 * batch + 1] = goodToGo ? 1 : 0;
        if (goodToGo) {
            int[] gamevision = game.getVision();
            for (int i = 0; i < Math.min(gamevision.length, 17); i++) {
                linesOfCode[batch + 2 + i] = gamevision[i];
            }
        }
        return linesOfCode;
    }

    @Override
    public void loseScore(PersonalityMatrix matrix, int score) {
        gameScore -= Math.max(0, score);
        interpreter.loseScore(creator, score);
    }

    @Override
    public void increaseScore(PersonalityMatrix matrix, int score) {
        gameScore += score;
        interpreter.increaseScore(creator, score);
    }

    public void onTerminate(PersonalityMatrix controller) {
        if (rewriteAttempts >= 50) {
            interpreter.onTerminate(creator);
            return;
        }
        goodToGo = false;
        indexOfWritingCode = 0;
        interationsOfWritingCode = 0;
        if (highestGameScore < gameScore) {
            highestGameScore = gameScore;
            interpreter.increaseScore(creator, 1000 * highestGameScore);
        }
        gameScore = 0;
        rewriteAttempts++;

        matrixToWorkOn = new PersonalityMatrix(100, 8, 3, 10, false, 1);
        game = gametype.createNewGame(matrixToWorkOn, this, 1);
    }

    @Override
    public int getScore(PersonalityMatrix matrix) {
        if (matrix == matrixToWorkOn)
            return gameScore;
        if (matrix == creator)
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
}
