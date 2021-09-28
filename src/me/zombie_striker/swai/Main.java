package me.zombie_striker.swai;

import me.zombie_striker.swai.data.DataBank;
import me.zombie_striker.swai.data.PersonalityMatrix;
import me.zombie_striker.swai.game.AbstractGame;
import me.zombie_striker.swai.game.GameEnum;
import me.zombie_striker.swai.game.battledroids.BattleDroidSimulatorGame;
import me.zombie_striker.swai.game.buildabot.BuildABotGame;
import me.zombie_striker.swai.game.mario.MarioGame;
import me.zombie_striker.swai.game.pong.PongGame;
import me.zombie_striker.swai.gamewindow.Window;
import me.zombie_striker.swai.world.GameWorldInterpreter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class Main {

    public static boolean running = true;

    public static GameWorldInterpreter gameworldInterpreter = new GameWorldInterpreter();

    public static Window window;

    public static final int MAX_PERSONALITIES_PER_GAME = 36;
    public static final int PERSONALITIES_PER_ROW = 6;

    public static HashMap<PersonalityMatrix, AbstractGame> game = new HashMap<>();
    public static int round = 0;
    public static GameEnum gameType = null;

    public static int bestscore = -1;
    public static boolean reset = false;

    private static String[] textlog = new String[20];



    public PersonalityMatrix personalityMatrixCreator;
    public void createCreatorPersonalityMatrix(){
        personalityMatrixCreator = new PersonalityMatrix(20000, 200, 20,500, true);
    }

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
        for (Map.Entry<PersonalityMatrix, AbstractGame> gameEntry : game.entrySet()) {
            int xpos = index % (MAX_PERSONALITIES_PER_GAME / PERSONALITIES_PER_ROW);
            int ypos = index / PERSONALITIES_PER_ROW;
            int width = image.getWidth() / (MAX_PERSONALITIES_PER_GAME / PERSONALITIES_PER_ROW);
            int height = (image.getHeight() - 200) / PERSONALITIES_PER_ROW;
            g.drawImage(gameEntry.getValue().render(), xpos * width, ypos * height, width, height, null);

            g.setColor(Color.GRAY);
            g.drawString(gameEntry.getKey().getUUID().toString(), xpos * width, (ypos * height) + 12);
            index++;
        }
        int textHeight = image.getHeight() - 200;
        g.setFont(Font.getFont(Font.SANS_SERIF));
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
                int maxcalllines = 100;
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
            } else if (line.toLowerCase().startsWith("learnpong")) {
                if(gameworldInterpreter.getMatrices().size()==0){
                    for (int i = 0; i < MAX_PERSONALITIES_PER_GAME; i++) {
                        gameworldInterpreter.createPersonality(10000,1000,20,501);
                    }
                }
                for (PersonalityMatrix matrix : gameworldInterpreter.getMatrices()) {
                    game.put(matrix, new BuildABotGame(matrix,gameworldInterpreter,GameEnum.PONG));
                }
                gameType=GameEnum.BUILDAPONG;
                window = new Window();
            } else if (line.toLowerCase().startsWith("runpong")) {
                if(gameworldInterpreter.getMatrices().size()==0){
                    for (int i = 0; i < MAX_PERSONALITIES_PER_GAME; i++) {
                        gameworldInterpreter.createPersonality(500,20,3,10);
                    }
                }
                for (PersonalityMatrix matrix : gameworldInterpreter.getMatrices()) {
                    game.put(matrix, new PongGame(matrix,gameworldInterpreter,1));
                }
                gameType=GameEnum.PONG;
                window = new Window();
            } else if (line.toLowerCase().startsWith("runbattle")) {
                if(gameworldInterpreter.getMatrices().size()==0){
                    for (int i = 0; i < MAX_PERSONALITIES_PER_GAME; i++) {
                        gameworldInterpreter.createPersonality(500,100,10,120);
                    }
                }
                for (PersonalityMatrix matrix : gameworldInterpreter.getMatrices()) {
                    game.put(matrix, new BattleDroidSimulatorGame(matrix,gameworldInterpreter,1));
                }
                gameType=GameEnum.BATTLE;
                window = new Window();
            } else if (line.toLowerCase().startsWith("runmario")) {
                if(gameworldInterpreter.getMatrices().size()==0){
                    for (int i = 0; i < MAX_PERSONALITIES_PER_GAME; i++) {
                        gameworldInterpreter.createPersonality(700,20,4,120);
                    }
                }
                for (PersonalityMatrix matrix : gameworldInterpreter.getMatrices()) {
                    game.put(matrix, new MarioGame(matrix,gameworldInterpreter));
                }
                gameType=GameEnum.MARIO;
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
                        bestscore = -1;
                        int amount = MAX_PERSONALITIES_PER_GAME;
                        while (true) {
                            for (PersonalityMatrix matrix : best) {
                                amount--;
                                PersonalityMatrix varied = matrix.clone();
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
}
