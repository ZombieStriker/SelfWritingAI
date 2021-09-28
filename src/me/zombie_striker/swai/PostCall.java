package me.zombie_striker.swai;

import me.zombie_striker.swai.data.PersonalityMatrix;

import java.util.ArrayList;
import java.util.List;

public class PostCall extends Callable {
    @Override
    public void call() {

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (Main.reset) {
            List<PersonalityMatrix> best = new ArrayList<>();
            for (PersonalityMatrix matrix : Main.gameworldInterpreter.getMatrices()) {
                int k = Main.gameworldInterpreter.getScore(matrix);
                if (Main.bestscore < k) {
                    Main.bestscore = k;
                    best.clear();
                    best.add(matrix);
                } else if (Main.bestscore == k) {
                    best.add(matrix);
                }
            }
            Main.log("Best score for round " + Main.round + " = " + Main.bestscore + " (Batch = " + best.size() + ")");
            Main.round++;
            Main.gameworldInterpreter.reset();
            Main.gameworldInterpreter.purge();
            Main.game.clear();
            int amount = 0;
            while (true) {
                for (PersonalityMatrix matrix : best) {
                    amount++;
                    PersonalityMatrix same = matrix.clone();
                    Main.gameworldInterpreter.addPersonality(same);
                    Main.game.put(same, Main.gameType.createNewGame(same, Main.gameworldInterpreter, Main.round));
                    if (amount >= Main.MAX_PERSONALITIES_PER_GAME / Main.PERSONALITIES_PER_ROW)
                        break;
                }
                if (amount == Main.MAX_PERSONALITIES_PER_GAME)
                    break;
                for (PersonalityMatrix matrix : best) {
                    amount++;
                    PersonalityMatrix varied = matrix.clone();
                    varied.randomizeSomeLines((Math.sin(Main.round / 50.0) + 1) * 25);
                    Main.gameworldInterpreter.addPersonality(varied);
                    Main.game.put(varied, Main.gameType.createNewGame(varied, Main.gameworldInterpreter, Main.round));
                    if (amount == Main.MAX_PERSONALITIES_PER_GAME)
                        break;
                }
                if (amount == Main.MAX_PERSONALITIES_PER_GAME)
                    break;
            }
            Main.bestscore = -1;
            Main.reset = false;
        }
    }
}
