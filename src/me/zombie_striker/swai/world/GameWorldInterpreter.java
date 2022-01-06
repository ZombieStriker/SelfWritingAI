package me.zombie_striker.swai.world;

import me.zombie_striker.swai.Main;
import me.zombie_striker.swai.data.DataBank;
import me.zombie_striker.swai.data.PersonalityMatrix;
import me.zombie_striker.swai.game.AbstractGame;

import java.util.*;

public class GameWorldInterpreter implements Interpreter {

    private List<PersonalityMatrix> persons = new LinkedList<>();
    private HashMap<PersonalityMatrix, Integer> scores = new HashMap<>();

    private boolean warpspeed = true;
    private boolean finetuning = false;

    public List<PersonalityMatrix> getMatrices() {
        return persons;
    }

    public PersonalityMatrix createPersonality(int linesofcode, int objectsINRam, int inputsize, int readonlySize, boolean genPersonality) {
        PersonalityMatrix mat = new PersonalityMatrix(linesofcode, objectsINRam, inputsize, readonlySize, genPersonality, 1);
        persons.add(mat);
        return mat;
    }

    public void onTerminate(PersonalityMatrix controller) {
        if (Main.games.containsKey(controller)) {
            Main.games.get(controller).onTerminate();
            int activeGamesCount = 0;
            for (AbstractGame g : Main.games.values()) {
                if (g.isActive())
                    activeGamesCount++;
            }
            if (activeGamesCount < 1) {
                Main.reset = true;
            }
        }
    }


    public void tick() {
        long lastTime = System.currentTimeMillis();
        for (int j = 0; j < (warpspeed ? 9000 : 1); j++) {
            if (System.currentTimeMillis() - lastTime > 11)
                break;
            List<AbstractGame> gamesAlreadyRan = new ArrayList<>();
            for (Map.Entry<PersonalityMatrix, AbstractGame> e : new HashSet<>(Main.games.entrySet())) {
                AbstractGame game = e.getValue();
                if (!gamesAlreadyRan.contains(game)) {
                    gamesAlreadyRan.add(game);
                    game.gameTick();
                }
                int[] vision = game.getVision(e.getKey());
                for (int i = 0; i < Math.min(vision.length, e.getKey().getPalletReadOnly().length); i++) {
                    e.getKey().getPalletReadOnly()[i] = vision[i];
                }
                int linesRan = e.getKey().run(false, -1);
                game.handleInputs(e.getKey(), handleInputs(e.getKey()));
                game.tick(linesRan);
            }
        }
        postCall();
    }

    public int[] handleInputs(PersonalityMatrix matrix) {
        int[] inputs = interpretPallet(matrix, 0/*matrix.getPallet().length - 1 - matrix.getPalletsForInputs()*/, matrix.getPalletsForInputs());
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
            scores.put(matrix, Math.max(0, score));
        }

    }

    public boolean getWarpSpeed() {
        return warpspeed;
    }

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
        if (scores.containsKey(matrix)) {
            if(i!=0)
            scores.put(matrix, scores.get(matrix) / i);
        }
    }


    public void postCall() {
        if (!getWarpSpeed()) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (Main.reset) {
            int requiredAmountOfPlayers = Main.gameType.getRequiredAmountOfControllers();
            int[] bestscore = new int[requiredAmountOfPlayers];
            Arrays.fill(bestscore, Integer.MIN_VALUE);
            List[] bests = new LinkedList[requiredAmountOfPlayers];
            for (int i = 0; i < bests.length; i++) {
                bests[i] = new LinkedList();
                for (PersonalityMatrix matrix : getMatrices()) {
                    if (Main.games.get(matrix).getControllerIndex(matrix) != i)
                        continue;
                    int k = getScore(matrix);
                    if (bestscore[i] < k) {
                        bestscore[i] = k;
                        bests[i].clear();
                        bests[i].add(matrix);
                    } else if (bestscore[i] == k) {
                        bests[i].add(matrix);
                    }
                }
                Main.log("Best score for round " + Main.round + " = " + bestscore[i] + " (Batch " + i + " = " + bests[i].size() + ")");
            }
            Main.round++;
            Main.gameworldInterpreter.reset();
            Main.gameworldInterpreter.purge();
            Main.games.clear();

            int amount = 0;
            for (int i = 0; i < bests.length; i++) {
                Collections.sort(bests[i]);
                Collections.reverse(bests[i]);
            }

            /*if (hasEnabledFineTuning()) {
                amount++;
                PersonalityMatrix oldest = null;
                for (PersonalityMatrix top : best) {
                    if (oldest == null || oldest.getGeneration() > top.getGeneration())
                        oldest = top;
                }
                PersonalityMatrix[] array = new PersonalityMatrix[requiredAmountOfPlayers];
                for (int i = 0; i < array.length; i++) {
                    PersonalityMatrix top = oldest.clone();
                    Main.gameworldInterpreter.addPersonality(top);
                    array[i] = top;
                }
                for (int i = 0; i < array.length; i++)
                    Main.games.put(array[i], Main.gameType.createNewGame(array, Main.gameworldInterpreter, amount, Main.round));

                while (amount < Main.MAX_PERSONALITIES_PER_GAME) {
                    amount++;
                    for (int i = 0; i < array.length; i++) {
                        array[i].setFineTuningIndex(array[i].getFineTuningIndex() + 1);
                        PersonalityMatrix variation = best.get(0).clone();
                        variation.setGeneration(Main.round);
                        variation.improveCode(((Math.sin(Main.round / 50.0) + 1) / 2) * best.size());
                        Main.gameworldInterpreter.addPersonality(variation);
                        array[i] = variation;
                    }
                    AbstractGame savedGame = null;
                    for (int j = 0; j < array.length; j++) {
                        if (savedGame == null || j % requiredAmountOfPlayers == 0) {
                            savedGame = Main.gameType.createNewGame(array, Main.gameworldInterpreter, amount, Main.round);
                        }
                        Main.games.put(array[j], savedGame);
                    }
                    //Increment tops finetuning variable
                }
            } else*/
            {
                // if (bests[0].size() >= ((Main.MAX_PERSONALITIES_PER_GAME * 2) / 3) + 1) {
                for (int i = 0; i < (Main.PERSONALITIES_PER_ROW * 2) / 3; i++) {
                    amount++;
                    PersonalityMatrix[] array = new PersonalityMatrix[requiredAmountOfPlayers];
                    for (int j = 0; j < array.length; j++) {
                        PersonalityMatrix same;
                        same = ((PersonalityMatrix) bests[j].get(DataBank.seededRandom(Math.abs(bestscore[j]), Main.MAX_PERSONALITIES_PER_GAME, Main.PERSONALITIES_PER_ROW).nextInt(bests[j].size()))).clone();
                        Main.gameworldInterpreter.addPersonality(same);
                        array[j] = same;
                    }
                    AbstractGame savedGame = null;
                    for (int j = 0; j < array.length; j++) {
                        if (savedGame == null || j % requiredAmountOfPlayers == 0) {
                            savedGame = Main.gameType.createNewGame(array, Main.gameworldInterpreter, amount, Main.round);
                        }
                        Main.games.put(array[j], savedGame);
                    }
                }
                /*} else {
                    for (PersonalityMatrix matrix : best) {
                        if (best.size() >= ((Main.MAX_PERSONALITIES_PER_GAME * 2) / 3) + 1)
                            if (amount >= Main.MAX_PERSONALITIES_PER_GAME / Main.PERSONALITIES_PER_ROW)
                                break;
                        amount++;
                        PersonalityMatrix[] array = new PersonalityMatrix[requiredAmountOfPlayers];
                        for (int j = 0; j < array.length; j++) {
                            PersonalityMatrix same = matrix.clone();
                            Main.gameworldInterpreter.addPersonality(same);
                            array[j] = same;
                        }
                        AbstractGame savedGame = null;
                        for (int j = 0; j < array.length; j++) {
                            if (savedGame == null || j % requiredAmountOfPlayers == 0) {
                                savedGame = Main.gameType.createNewGame(array, Main.gameworldInterpreter, amount, Main.round);
                            }
                            Main.games.put(array[j], savedGame);
                        }
                    }
                }*/

                //if (amount + best.size() > Main.MAX_PERSONALITIES_PER_GAME) {
                for (int count = amount; count < Main.MAX_PERSONALITIES_PER_GAME; count++) {
                    amount++;
                    PersonalityMatrix[] array = new PersonalityMatrix[requiredAmountOfPlayers];
                    for (int j = 0; j < array.length; j++) {
                        PersonalityMatrix varied = ((PersonalityMatrix) bests[j].get(DataBank.seededRandom(amount, Main.MAX_PERSONALITIES_PER_GAME, bestscore[j]).nextInt(bests[j].size()))).clone();
                        varied.setGeneration(Main.round);
                        varied.improveCode(((Math.sin(Main.round / 50.0) + 1) / 2) * bests[j].size());
                        Main.gameworldInterpreter.addPersonality(varied);
                        array[j] = varied;
                    }

                    AbstractGame savedGame = null;
                    for (int j = 0; j < array.length; j++) {
                        if (savedGame == null || j % requiredAmountOfPlayers == 0) {
                            savedGame = Main.gameType.createNewGame(array, Main.gameworldInterpreter, amount, Main.round);
                        }
                        Main.games.put(array[j], savedGame);
                    }
                }
                /*} else {
                    if (amount < Main.MAX_PERSONALITIES_PER_GAME)
                        for (PersonalityMatrix matrix : best) {
                            amount++;
                            PersonalityMatrix[] array = new PersonalityMatrix[requiredAmountOfPlayers];
                            for (int j = 0; j < array.length; j++) {
                                PersonalityMatrix varied = matrix.clone();
                                varied.setGeneration(Main.round);
                                varied.improveCode(((Math.sin(Main.round / 50.0) + 1) / 2) * best.size());
                                Main.gameworldInterpreter.addPersonality(varied);
                                array[j] = varied;
                            }

                            AbstractGame savedGame = null;
                            for (int j = 0; j < array.length; j++) {
                                if (savedGame == null || j % requiredAmountOfPlayers == 0) {
                                    savedGame = Main.gameType.createNewGame(array, Main.gameworldInterpreter, amount, Main.round);
                                }
                                Main.games.put(array[j], savedGame);
                            }
                            if (amount == Main.MAX_PERSONALITIES_PER_GAME)
                                break;

                        }
                }
                }*/
            }
            //Main.bestscore = Integer.MIN_VALUE;
            Main.reset = false;
        }
    }
}
