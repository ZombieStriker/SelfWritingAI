package me.zombie_striker.swai;

import me.zombie_striker.swai.data.DataBank;
import me.zombie_striker.swai.data.PersonalityMatrix;
import me.zombie_striker.swai.game.AbstractGame;
import me.zombie_striker.swai.game.GameEnum;
import me.zombie_striker.swai.game.battledroids.BattleDroidSimulatorGame;
import me.zombie_striker.swai.game.buildabot.BuildABotGame;
import me.zombie_striker.swai.game.imagerecreator.DrawImageGame;
import me.zombie_striker.swai.game.mario.MarioGame;
import me.zombie_striker.swai.game.pong.PongGame;
import me.zombie_striker.swai.game.writetext.WriteTextGame;
import me.zombie_striker.swai.gamewindow.Window;
import me.zombie_striker.swai.world.GameWorldInterpreter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URISyntaxException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static boolean running = true;

    public static GameWorldInterpreter gameworldInterpreter = new GameWorldInterpreter();

    public static Window window;

    public static int PERSONALITIES_PER_ROW = 9;
    public static int MAX_PERSONALITIES_PER_GAME = PERSONALITIES_PER_ROW * PERSONALITIES_PER_ROW;

    public static HashMap<PersonalityMatrix, AbstractGame> games = new HashMap<>();
    public static int round = 0;
    public static GameEnum gameType = null;

    public static int bestscore = Integer.MIN_VALUE;
    public static boolean reset = false;

    private static String[] textlog = new String[20];


    public static void log(String message) {
        for (int i = textlog.length - 1 - 1; i >= 0; i--) {
            textlog[i + 1] = textlog[i];
        }
        textlog[0] = message;
    }

    public static BufferedImage render() {
        int index = 0;
        BufferedImage image = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        List<PersonalityMatrix> list = new ArrayList<>(games.keySet());
        Collections.sort(list);
        g.setFont(new Font("Courier", Font.BOLD, 12));
        for (PersonalityMatrix matrix : list) {
            AbstractGame ag = games.get(matrix);
            int xpos = index % (MAX_PERSONALITIES_PER_GAME / PERSONALITIES_PER_ROW);
            int ypos = index / PERSONALITIES_PER_ROW;
            int width = image.getWidth() / (MAX_PERSONALITIES_PER_GAME / PERSONALITIES_PER_ROW);
            int height = (image.getHeight() - 200) / PERSONALITIES_PER_ROW;
            BufferedImage render = ag.render();
            g.drawImage(render, xpos * width, ypos * height, width, height, null);

            Random seededRandom = new Random((matrix.getGeneration() * 3) + 0);
            Random seededRandom2 = new Random((matrix.getGeneration() * 3) + 1);
            Random seededRandom3 = new Random((matrix.getGeneration() * 3) + 2);

            g.setColor(new Color(seededRandom.nextInt(255), seededRandom2.nextInt(255), seededRandom3.nextInt(255)));
            g.fillRect(xpos * width, (ypos * height), width, 12);

            g.setColor(Color.GRAY);
            if(matrix.getUUID().toString().length() >= width/10) {
                g.drawString(matrix.getUUID().toString().substring(0,width/10), xpos * width, (ypos * height) + 12);
            }else{
                g.drawString(matrix.getUUID().toString(), xpos * width, (ypos * height) + 12);
            }
            index++;
        }
        int textHeight = image.getHeight() - 200;

        g.setColor(Color.WHITE);
        for (int i = 0; i < textlog.length; i++) {
            if (textlog[i] != null)
                g.drawString(textlog[i], 0, textHeight + (12 * i) + 11);
        }
        g.dispose();
        return image;
    }


    public static void main(String... args) {
        while (running) {
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if (line.startsWith("printpallet")) {
                String[] commandargs = line.split(" ");
                int id = 1;
                if (commandargs.length > 1)
                    id = Integer.parseInt(commandargs[1]);

                int aiIndex = 0;
                for (PersonalityMatrix matrix : gameworldInterpreter.getMatrices()) {
                    if (aiIndex < id) {
                        aiIndex++;
                        continue;
                    }
                    for (int i = 0; i < (matrix.getPallet().length / 25) + 1; i++) {
                        StringBuilder sb = new StringBuilder();
                        int ii = i * 25;
                        for (int j = 0; j < 25; j++) {
                            if (ii + j >= matrix.getPallet().length)
                                break;
                            String entry = DataBank.translate(matrix.getPallet()[ii + j]);
                            if (entry.length() < 4) {
                                entry += " ".repeat(4 - entry.length());
                            }
                            sb.append(entry + "|");
                        }
                        System.out.println(sb.toString());
                    }
                    break;
                }
            }
            if (line.equalsIgnoreCase("show")) {
                window = new Window();
            }
            if (line.toLowerCase().startsWith("loadai")) {
                String[] commandargs = line.split(" ");
                if (commandargs.length == 1) {
                    System.out.println("Usage >loadAI ABC");
                    continue;
                }
                String filename = commandargs[1] + ".machinecode";
                File file = new File(getRunningJarLocation().getParentFile(), filename);
                if (!file.exists()) {
                    System.out.println("File " + filename + " does not exist");
                    continue;
                }
                PersonalityMatrix loaded = PersonalityMatrix.load(file);
                gameworldInterpreter.addPersonality(loaded);

                System.out.println("Loaded " + loaded.getUUID().toString() + ".");
            }
            if (line.toLowerCase().startsWith("saveai")) {
                String[] commandargs = line.split(" ");
                if (commandargs.length == 1) {
                    System.out.println("Usage >saveAI 1 <name>");
                    continue;
                }
                int aiindex = 0;
                if (commandargs.length > 1) {
                    aiindex = Integer.parseInt(commandargs[1]);
                }
                String name;
                if (commandargs.length > 2) {
                    name = commandargs[2];
                } else {
                    name = "" + DataBank.chars[ThreadLocalRandom.current().nextInt(DataBank.chars.length)] + DataBank.chars[ThreadLocalRandom.current().nextInt(DataBank.chars.length)] + DataBank.chars[ThreadLocalRandom.current().nextInt(DataBank.chars.length)];
                }
                PersonalityMatrix matrixToSave = gameworldInterpreter.getMatrices().get(Math.min(gameworldInterpreter.getMatrices().size(), aiindex));
                File writeTo = new File(getRunningJarLocation().getParentFile(), name + ".machinecode");
                matrixToSave.saveTo(writeTo);
                System.out.println("Saving PersonalityMatrix " + matrixToSave.getUUID().toString() + " to " + writeTo.getPath());
            }
            if (line.equalsIgnoreCase("printAI")) {
                int aiIndex = 0;
                for (PersonalityMatrix matrix : gameworldInterpreter.getMatrices()) {
                    System.out.println("============= Matrix " + aiIndex + " (" + matrix.getUUID().toString() + ")");
                    for (int i = 0; i < matrix.getCode().length; i++) {
                        if (matrix.getCode()[i] != null) {
                            System.out.println(i + ": " + matrix.getCode()[i].toString());
                        }
                    }
                    aiIndex++;
                }
            } else if (line.startsWith("printlog")) {
                String[] commandargs = line.split(" ");
                int maxcalllines = 500;
                if (commandargs.length > 2)
                    maxcalllines = Integer.parseInt(commandargs[2]);
                int matrixindex = 1;
                if (commandargs.length > 1)
                    matrixindex = Integer.parseInt(commandargs[1]);

                int aiIndex = 0;
                for (PersonalityMatrix matrix : gameworldInterpreter.getMatrices()) {
                    if (aiIndex < matrixindex) {
                        aiIndex++;
                        continue;
                    }
                    matrix.run(true, maxcalllines);
                    break;
                }
            } else if (line.toLowerCase().startsWith("setrows")) {
                String[] commandargs = line.split(" ");
                if (commandargs.length == 1) {
                    System.out.println("Usage >setrows <rows>");
                    continue;
                }
                PERSONALITIES_PER_ROW = Integer.parseInt(commandargs[1]);
                MAX_PERSONALITIES_PER_GAME = PERSONALITIES_PER_ROW * PERSONALITIES_PER_ROW;
                System.out.println("Set PPR to " + PERSONALITIES_PER_ROW + " (Max to " + MAX_PERSONALITIES_PER_GAME + ")");
            } else if (line.toLowerCase().startsWith("togglefinetuning")) {
                gameworldInterpreter.setFineTuning(!gameworldInterpreter.hasEnabledFineTuning());
                System.out.println("Toggling FineTuning to " + gameworldInterpreter.hasEnabledFineTuning() + ".");
            } else if (line.toLowerCase().startsWith("togglespeed")) {
                gameworldInterpreter.setWarpSpeed(!gameworldInterpreter.getWarpSpeed());
                System.out.println("Toggling WarpSpeed to " + gameworldInterpreter.getWarpSpeed() + ".");

            } else if (line.toLowerCase().startsWith("learnpong")) {
                if (gameworldInterpreter.getMatrices().size() == 0) {
                    for (int i = 0; i < MAX_PERSONALITIES_PER_GAME; i++) {
                        gameworldInterpreter.createPersonality(10000, 1000, 20, 520, true);
                    }
                }
                for (PersonalityMatrix matrix : gameworldInterpreter.getMatrices()) {
                    games.put(matrix, new BuildABotGame(matrix, gameworldInterpreter, GameEnum.PONG));
                }
                gameType = GameEnum.BUILDAPONG;
                window = new Window();
            } else if (line.toLowerCase().startsWith("runpong")) {
                if (gameworldInterpreter.getMatrices().size() == 0) {
                    for (int i = 0; i < MAX_PERSONALITIES_PER_GAME; i++) {
                        gameworldInterpreter.createPersonality(500, 20, 3, 10, true);
                    }
                }
                for (PersonalityMatrix matrix : gameworldInterpreter.getMatrices()) {
                    games.put(matrix, new PongGame(matrix, gameworldInterpreter, 1));
                }
                gameType = GameEnum.PONG;
                window = new Window();
            } else if (line.toLowerCase().startsWith("runbattle")) {
                if (gameworldInterpreter.getMatrices().size() == 0) {
                    for (int i = 0; i < MAX_PERSONALITIES_PER_GAME; i++) {
                        gameworldInterpreter.createPersonality(500, 100, 10, 120, true);
                    }
                }
                for (PersonalityMatrix matrix : gameworldInterpreter.getMatrices()) {
                    games.put(matrix, new BattleDroidSimulatorGame(matrix, gameworldInterpreter, 1));
                }
                gameType = GameEnum.BATTLE;
                window = new Window();
            } else if (line.toLowerCase().startsWith("runwrite")) {
                if (gameworldInterpreter.getMatrices().size() == 0) {
                    for (int i = 0; i < MAX_PERSONALITIES_PER_GAME; i++) {
                        gameworldInterpreter.createPersonality(7000, 300, 5, 4 * 200, true);
                    }
                }
                for (PersonalityMatrix matrix : gameworldInterpreter.getMatrices()) {
                    games.put(matrix, new WriteTextGame(matrix, gameworldInterpreter, round));
                }
                gameType = GameEnum.WRITE;
                window = new Window();
            } else if (line.toLowerCase().startsWith("rundraw")) {
                if (gameworldInterpreter.getMatrices().size() == 0) {
                    for (int i = 0; i < MAX_PERSONALITIES_PER_GAME; i++) {
                        gameworldInterpreter.createPersonality(1000, 100, 3, 40, true);
                    }
                }
                for (PersonalityMatrix matrix : gameworldInterpreter.getMatrices()) {
                    games.put(matrix, new DrawImageGame(matrix, gameworldInterpreter,0));
                }
                gameType = GameEnum.DRAW_CAT;
                window = new Window();
            } else if (line.toLowerCase().startsWith("runmario")) {
                if (gameworldInterpreter.getMatrices().size() == 0) {
                    for (int i = 0; i < MAX_PERSONALITIES_PER_GAME; i++) {
                        gameworldInterpreter.createPersonality(700, 20, 4, 120, true);
                    }
                }
                for (PersonalityMatrix matrix : gameworldInterpreter.getMatrices()) {
                    games.put(matrix, new MarioGame(matrix, gameworldInterpreter));
                }
                gameType = GameEnum.MARIO;
                window = new Window();


            } else if (line.startsWith("runall")) {
                String[] commandargs = line.split(" ");
                int timesRun = 1;
                if (commandargs.length > 1)
                    timesRun = Integer.parseInt(commandargs[1]);
                int percetnage = 10;
                if (commandargs.length > 2)
                    percetnage = Integer.parseInt(commandargs[2]);

                for (PersonalityMatrix matrix : gameworldInterpreter.getMatrices()) {
                    matrix.populateRam();
                }
                List<PersonalityMatrix> best = new ArrayList<>();
                for (int p = 0; p < timesRun; p++) {
                    int aiIndex = 0;
                    for (PersonalityMatrix matrix : gameworldInterpreter.getMatrices()) {
                        System.out.println("============= Matrix " + aiIndex + " (" + matrix.getUUID().toString() + ")");
                        matrix.populateRam();
                        matrix.run();
                        aiIndex++;
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    for (PersonalityMatrix matrix : gameworldInterpreter.getMatrices()) {
                        int k = gameworldInterpreter.getScore(matrix);
                        if (bestscore < k) {
                            bestscore = k;
                            best.clear();
                            best.add(matrix);
                        } else if (bestscore == k) {
                            best.add(matrix);
                        }
                    }
                    gameworldInterpreter.reset();
                    if (p + 1 < timesRun) {
                        gameworldInterpreter.purge();
                        bestscore = Integer.MIN_VALUE;
                        int amount = MAX_PERSONALITIES_PER_GAME;
                        while (true) {
                            for (PersonalityMatrix matrix : best) {
                                amount--;
                                PersonalityMatrix varied = matrix.clone();
                                varied.setGeneration(round);
                                varied.randomizeSomeLines(percetnage);
                                gameworldInterpreter.addPersonality(varied);
                                if (amount == 0)
                                    break;
                            }
                            if (amount == 0)
                                break;
                        }
                    } else {
                        gameworldInterpreter.purge();
                        for (PersonalityMatrix matrix : best) {
                            gameworldInterpreter.addPersonality(matrix);
                        }
                    }
                }

                System.out.println("Done! Best pool contains \"" + best.size() + "\" with score " + bestscore);
            }

        }
    }

    public static void postCall() {
        if (!gameworldInterpreter.getWarpSpeed()) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
            Main.games.clear();

            int amount = 0;
            Collections.sort(best);
            Collections.reverse(best);

            if (best.size() >= ((Main.MAX_PERSONALITIES_PER_GAME*2)/3)+1){
                for (int i = 0; i < (PERSONALITIES_PER_ROW*2)/3;i++) {
                    amount++;
                    PersonalityMatrix same = best.get(DataBank.seededRandom(Math.abs(bestscore),MAX_PERSONALITIES_PER_GAME,PERSONALITIES_PER_ROW).nextInt(best.size())).clone();
                    Main.gameworldInterpreter.addPersonality(same);
                    Main.games.put(same, Main.gameType.createNewGame(same, Main.gameworldInterpreter, round));
                }
            }else {
                for (PersonalityMatrix matrix : best) {
                    if (best.size() >= ((MAX_PERSONALITIES_PER_GAME * 2) / 3) + 1)
                        if (amount >= Main.MAX_PERSONALITIES_PER_GAME / PERSONALITIES_PER_ROW)
                            break;
                    amount++;
                    PersonalityMatrix same = matrix.clone();
                    Main.gameworldInterpreter.addPersonality(same);
                    Main.games.put(same, Main.gameType.createNewGame(same, Main.gameworldInterpreter, round));
                }
            }

            if (amount + best.size() > MAX_PERSONALITIES_PER_GAME) {
                for (int count = amount; count < MAX_PERSONALITIES_PER_GAME; count++) {
                    amount++;
                    PersonalityMatrix varied = best.get(DataBank.seededRandom(amount,MAX_PERSONALITIES_PER_GAME,bestscore).nextInt(best.size())).clone();
                    varied.setGeneration(Main.round);
                    varied.improveCode((Math.sin(Main.round / 50.0) + 1) * 25);
                    Main.gameworldInterpreter.addPersonality(varied);
                    Main.games.put(varied, Main.gameType.createNewGame(varied, Main.gameworldInterpreter, round));
                }
            } else {
                if (amount < Main.MAX_PERSONALITIES_PER_GAME)
                    for (PersonalityMatrix matrix : best) {
                        amount++;
                        PersonalityMatrix varied = matrix.clone();
                        varied.setGeneration(Main.round);
                        varied.improveCode((int) (((Math.sin((double) Main.round / 50.0)) + 1) * 50));
                        Main.gameworldInterpreter.addPersonality(varied);
                        Main.games.put(varied, Main.gameType.createNewGame(varied, Main.gameworldInterpreter, round));
                        if (amount == Main.MAX_PERSONALITIES_PER_GAME)
                            break;
                    }
            }
            //if (amount == Main.MAX_PERSONALITIES_PER_GAME)
            //   break;
            //}
            Main.bestscore = Integer.MIN_VALUE;
            Main.reset = false;
        }
    }

    public static File getRunningJarLocation() {
        try {
            return new File(Main.class.getProtectionDomain().getCodeSource().getLocation()
                    .toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
