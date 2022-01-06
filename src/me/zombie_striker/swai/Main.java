package me.zombie_striker.swai;

import me.zombie_striker.swai.data.DataBank;
import me.zombie_striker.swai.data.PersonalityMatrix;
import me.zombie_striker.swai.game.AbstractGame;
import me.zombie_striker.swai.game.GameEnum;
import me.zombie_striker.swai.game.battledroids.BattleDroidSimulatorGame;
import me.zombie_striker.swai.game.buildabot.BuildABotGame;
import me.zombie_striker.swai.game.imagerecreator.DrawImageGame;
import me.zombie_striker.swai.game.mario.MarioGame;
import me.zombie_striker.swai.game.pong.Pong2Game;
import me.zombie_striker.swai.game.pong.PongGame;
import me.zombie_striker.swai.game.writetext.WriteTextGame;
import me.zombie_striker.swai.windows.Window;
import me.zombie_striker.swai.windows.types.CodeViewerCore;
import me.zombie_striker.swai.windows.types.GameViewWindowCore;
import me.zombie_striker.swai.world.GameWorldInterpreter;

import java.io.File;
import java.net.URISyntaxException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static boolean running = true;

    public static GameWorldInterpreter gameworldInterpreter = new GameWorldInterpreter();

    public static List<Window> windows = new LinkedList<>();

    public static int PERSONALITIES_PER_ROW = 6;
    public static int MAX_PERSONALITIES_PER_GAME = PERSONALITIES_PER_ROW * PERSONALITIES_PER_ROW;

    public static void setPersonalitiesPerRow(int i){
        PERSONALITIES_PER_ROW = i;
        MAX_PERSONALITIES_PER_GAME = i*i;
    }

    public static HashMap<PersonalityMatrix, AbstractGame> games = new HashMap<>();
    public static int round = 0;
    public static GameEnum gameType = null;

   // public static int bestscore = Integer.MIN_VALUE;
    public static boolean reset = false;

    public static String[] textlog = new String[20];


    public static void log(String message) {
        for (int i = textlog.length - 1 - 1; i >= 0; i--) {
            textlog[i + 1] = textlog[i];
        }
        textlog[0] = message;
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
            if (line.toLowerCase().startsWith("view")) {
                String[] commandargs = line.split(" ");
                String name=null;
                if (commandargs.length > 1) {
                    name = commandargs[1];
                }
                File file = new File(getRunningJarLocation().getParentFile(), name+".machinecode");
                PersonalityMatrix loaded;
                if (name==null && gameworldInterpreter.getMatrices().size()>0) {
                    loaded = gameworldInterpreter.getMatrices().get(0);
                }else if (!file.exists()) {
                    loaded = new PersonalityMatrix(100,10,5,10,true,0);
                }else{
                    loaded = PersonalityMatrix.load(file);
                }
                windows.add(new Window(new CodeViewerCore(loaded)));

            }
            if (line.toLowerCase().startsWith("saveai")) {
                String[] commandargs = line.split(" ");
                if (commandargs.length == 1) {
                    System.out.println("Usage >saveAI 1 <name>");
                    continue;
                }
                int aiindex = 0;
                if (commandargs.length > 1) {
                    try {
                        aiindex = Integer.parseInt(commandargs[1]);
                    }catch(Exception e3){
                        e3.printStackTrace();
                    }
                }
                String name;
                if (commandargs.length > 2) {
                    name = commandargs[2];
                } else {
                    name = "" + DataBank.chars[ThreadLocalRandom.current().nextInt(DataBank.chars.length)] + DataBank.chars[ThreadLocalRandom.current().nextInt(DataBank.chars.length)] + DataBank.chars[ThreadLocalRandom.current().nextInt(DataBank.chars.length)];
                }
                PersonalityMatrix matrixToSave = null;
                int countdown = aiindex;
                for(Map.Entry<PersonalityMatrix, AbstractGame> p : games.entrySet()){
                    if(countdown<=0){
                        matrixToSave = p.getKey();
                        break;
                    }
                    if(p.getValue().isActive())
                    countdown--;
                }
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
                    games.put(matrix, new BuildABotGame(matrix, gameworldInterpreter, GameEnum.PONG,0));
                }
                gameType = GameEnum.BUILDAPONG;
                windows.add( new Window(new GameViewWindowCore()));
            } else if (line.toLowerCase().startsWith("runpong2")) {
                if (gameworldInterpreter.getMatrices().size() < 2) {
                    for (int i = 0; i < MAX_PERSONALITIES_PER_GAME; i++) {
                        PersonalityMatrix matrix = gameworldInterpreter.createPersonality(500, 20, 3, 10, true);
                        PersonalityMatrix matrix2 = gameworldInterpreter.createPersonality(500, 20, 3, 10, true);
                        AbstractGame game = new Pong2Game(new PersonalityMatrix[]{matrix,matrix2}, gameworldInterpreter, 1);
                       games.put(matrix, game);
                        games.put(matrix2, game);
                    }
                }
                gameType = GameEnum.PONG2;
                windows.add( new Window(new GameViewWindowCore()));
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
                windows.add( new Window(new GameViewWindowCore()));
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
                windows.add( new Window(new GameViewWindowCore()));
            } else if (line.toLowerCase().startsWith("runwrite")) {
                if (gameworldInterpreter.getMatrices().size() == 0) {
                    for (int i = 0; i < MAX_PERSONALITIES_PER_GAME; i++) {
                        gameworldInterpreter.createPersonality(700+1000, 1000, 800, 4 * 10, true);
                    }
                }
                for (PersonalityMatrix matrix : gameworldInterpreter.getMatrices()) {
                    games.put(matrix, new WriteTextGame(matrix, gameworldInterpreter, round));
                }
                gameType = GameEnum.WRITE_ALICE;
                windows.add( new Window(new GameViewWindowCore()));
            } else if (line.toLowerCase().startsWith("rundraw")) {
                if (gameworldInterpreter.getMatrices().size() == 0) {
                    for (int i = 0; i < MAX_PERSONALITIES_PER_GAME; i++) {
                        PersonalityMatrix matrix = gameworldInterpreter.createPersonality(1200, 100, 3, 4+(15*15*3)+5, true);
                        PersonalityMatrix matrix2 = gameworldInterpreter.createPersonality(1200, 100, 2, 4+(15*15*3)+5, true);
                        AbstractGame game = new DrawImageGame(new PersonalityMatrix[]{matrix,matrix2}, gameworldInterpreter,0);
                        games.put(matrix,game);
                        games.put(matrix2,game);
                    }
                }
                gameType = GameEnum.DRAW_CAT;
                windows.add( new Window(new GameViewWindowCore()));
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
                windows.add( new Window(new GameViewWindowCore()));
            }
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
