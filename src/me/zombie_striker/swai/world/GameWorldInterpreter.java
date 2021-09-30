package me.zombie_striker.swai.world;

import me.zombie_striker.swai.Main;
import me.zombie_striker.swai.data.PersonalityMatrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameWorldInterpreter implements Interpreter {

    private List<PersonalityMatrix> persons = new ArrayList<>();
    private HashMap<PersonalityMatrix, Integer> scores = new HashMap<>();

    public List<PersonalityMatrix> getMatrices() {
        return persons;
    }

    public PersonalityMatrix createPersonality(int linesofcode, int objectsINRam, int inputsize, int readonlySize, boolean genPersonality) {
        PersonalityMatrix mat = new PersonalityMatrix(linesofcode, objectsINRam, inputsize, readonlySize, genPersonality,1);
        persons.add(mat);
        return mat;
    }

    public void onTerminate(PersonalityMatrix controller) {
        Main.games.get(controller).onTerminate();
        Main.games.remove(controller);
        if (Main.games.size() < 1) {
            Main.reset = true;
        }
    }


    public void tick() {
        long lastTime = System.currentTimeMillis();
        for (int j = 0; j < 200; j++) {
            if (System.currentTimeMillis() - lastTime > 11)
                break;
            boolean gametick = false;
            for (PersonalityMatrix matrix : getMatrices()) {
                if (Main.games.containsKey(matrix)) {
                    if (!gametick) {
                        gametick = true;
                        Main.games.get(matrix).gameTick();
                    }
                    int[] vision = Main.games.get(matrix).getVision();
                    for (int i = 0; i < vision.length; i++) {
                        matrix.getPalletReadOnly()[i] = vision[i];
                    }
                    int linesRan = matrix.run(false, -1);
                    Main.games.get(matrix).handleInputs(handleInputs(matrix));
                    Main.games.get(matrix).tick(linesRan);
                }
            }
        }
        Main.postCall();
    }

    public int[] handleInputs(PersonalityMatrix matrix) {
        int[] inputs = interpretPallet(matrix, matrix.getPallet().length - 1 - matrix.getPalletsForInputs(), matrix.getPalletsForInputs());
        return inputs;
    }


    public void reset() {
        scores.clear();
    }

    public void purge() {
        persons.clear();
    }

    public int getScore(PersonalityMatrix matrix) {
        if (scores.containsKey(matrix))
            return scores.get(matrix);
        return 0;
    }

    public void addPersonality(PersonalityMatrix best) {
        this.persons.add(best);
    }

    public int[] interpretPallet(PersonalityMatrix matrix, int startingIndex, int amountOfEntries) {
        int[] b = new int[amountOfEntries];
        for (int i = 0; i < amountOfEntries; i++) {
            b[i] = (matrix.getPallet()[startingIndex + i]);
        }
        return b;
    }

    public void loseScore(PersonalityMatrix matrix, int byHowMuch) {
        if (scores.containsKey(matrix)) {
            if (scores.get(matrix) - byHowMuch >= 0)
                scores.put(matrix, scores.get(matrix) - byHowMuch);
        } else {
            scores.put(matrix, 0);
        }
    }

    @Override
    public void increaseScore(PersonalityMatrix matrix, int score) {
        if (scores.containsKey(matrix)) {
            scores.put(matrix, scores.get(matrix) + score);
        } else {
            scores.put(matrix, Math.max(0,score));
        }

    }
}
