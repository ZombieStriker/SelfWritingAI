package me.zombie_striker.swai.game.writetext;

import me.zombie_striker.swai.Main;
import me.zombie_striker.swai.data.DataBank;
import me.zombie_striker.swai.data.PersonalityMatrix;
import me.zombie_striker.swai.game.AbstractGame;
import me.zombie_striker.swai.world.Interpreter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class WriteTextGame extends AbstractGame {

    private static List<String> alice_in_wonderland;
    private List<String> computerGeneratedText;
    private int writerIndex = 0;
    private int stoppedAt = Integer.MAX_VALUE;
    private int round;

    private static String[] lastLines = new String[900];
    private static int previousBestScore = 0;

    private File writeTo;
    private FileWriter fw;

    static {
        WordBank.initWords();
        alice_in_wonderland = readAndCleanupText("/AliceInWonderland.txt");
        StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < alice_in_wonderland.size(); i++) {
            String p = alice_in_wonderland.get(i);
            if (WordBank.getWordType(p) == WordType.UNKNOWN) {
                sb2.append(p + " ");
                if (sb2.length() > 100) {
                    System.out.println(DataBank.ANSI_RED + sb2.toString() + DataBank.ANSI_RESET);
                    sb2 = new StringBuilder();
                }
            }
        }
        if (sb2.length() > 0) {
            System.out.println(DataBank.ANSI_RED + sb2.toString() + DataBank.ANSI_RESET);
            System.out.println("FIX THESE");
        }
    }

    public WriteTextGame(PersonalityMatrix personalityMatrix, Interpreter interpreter, int round) {
        super(null,personalityMatrix,interpreter);
        computerGeneratedText = new LinkedList<>();
        this.round = round;
        File f1 = new File(Main.getRunningJarLocation().getParentFile(), "TextLogs");
        if (!f1.exists())
            f1.mkdirs();
        writeTo = new File(f1, "Round_" + round + "_text_" + getControllers()[0].getUUID().toString() + ".txt");

        writerIndex += 40;
    }

    private StringBuilder sb = new StringBuilder();
    private boolean wroteOnce = false;

    private int previousWord = -1;
    private int timesUsedPreviousWord = 0;

    private int maxWords = 20;

    @Override
    public void handleInputs(PersonalityMatrix old, int[] inputs) {
        if (writerIndex == ((round / 100) + 1) * 100) {
            writerIndex = alice_in_wonderland.size();
        }
        if (writerIndex < alice_in_wonderland.size()) {
            writerIndex++;
            if (writerIndex < alice_in_wonderland.size()) {
                if(true){
                    int highestScore = 0;
                    int highscoreindex = 0;
                    for(int i = 0; i < inputs.length;i++){
                        if(inputs[i] > highestScore){
                            highscoreindex = i;
                            highestScore = inputs[i];
                        }
                    }
                    String word = WordBank.getWordAtIndex(highscoreindex);
                    WordType type = WordBank.getWordType(alice_in_wonderland.get(writerIndex));

                    if (type != null && type != WordType.UNKNOWN) {
                        if (maxWords < 0) {
                            stoppedAt = writerIndex;
                            writerIndex = alice_in_wonderland.size();
                            return;
                        }else{
                            maxWords--;
                        }
                    }

                    if(word==null){
                        computerGeneratedText.add("[?]");
                    }else{
                        WordType typechosen = WordBank.getWordType(word);
                        if (typechosen.getId() == type.getId()) {
                            maxWords += 1;
                        }
                        computerGeneratedText.add(word);
                    }

                }else {
                    int typeIndex = inputs[0];
                    int wordIndexSingle = inputs[1];
                    int wordIndexTens = inputs[2];
                    int wordIndexHundreds = inputs[3];
                    int wordIndex = (wordIndexHundreds * 100) + (wordIndexTens * 10) + wordIndexSingle;
                    if (typeIndex < 0) {
                        typeIndex = -typeIndex;
                    }
                    if (wordIndex < 0) {
                        wordIndex = -(wordIndex);
                    }
                    WordType type = WordBank.getWordType(alice_in_wonderland.get(writerIndex));
                    if (typeIndex == type.getId() || type == WordType.UNKNOWN) {
                        maxWords += 1;
                    }
                    if (type != null && type != WordType.UNKNOWN) {
                        if (maxWords < writerIndex) {
                            stoppedAt = writerIndex;
                            writerIndex = alice_in_wonderland.size();
                            return;
                        }
                    }
                    for (WordType wt : WordType.values())
                        if ((typeIndex % WordType.values().length) == wt.getId()) {
                            computerGeneratedText.add(WordBank.getWord(wt, wordIndex % WordBank.getAllType(wt).size()));
                        }
                }
            }
        } else {
            if (writerIndex == alice_in_wonderland.size() * 2) {
                writerIndex++;
                return;
            }
            for (int j = 0; j < 500; j++) {
                writerIndex++;
                int wordindex = writerIndex - alice_in_wonderland.size();
                if (stoppedAt < wordindex) {
                    writerIndex = ((alice_in_wonderland.size() * 2));
                    break;
                }
                if (wordindex < alice_in_wonderland.size()) {
                    if (computerGeneratedText.size() <= wordindex)
                        continue;
                    WordType type = WordBank.getWordType(alice_in_wonderland.get(wordindex));
                    WordType typechosen = WordBank.getWordType(computerGeneratedText.get(wordindex));
                    int typeListSize = 1;
                    if (WordBank.getAllType(type) != null)
                        typeListSize = WordBank.getAllType(type).size();
                    if (alice_in_wonderland.get(wordindex).equals(computerGeneratedText.get(wordindex))) {
                        if (type != WordType.GRAMMAR) {
                            getInterpreter().increaseScore(getControllers()[0], typeListSize * typeListSize);
                        } else {
                            getInterpreter().increaseScore(getControllers()[0], 1);
                        }
                    }else if (type == typechosen){
                        getInterpreter().increaseScore(getControllers()[0],1);
                    }
                    /*if (type != null && type != WordType.UNKNOWN) {
                        int positiveCorrectionScore = Math.abs(wordindexForType - wordindexForTypeChosen);
                        int positiveCorrectionScore2 = Math.abs(type.getId() - typechosen.getId());
                        if (positiveCorrectionScore > 0) {
                            int bap = ((typeListSize - positiveCorrectionScore) * (WordType.values().length - positiveCorrectionScore2)) / typeListSize;
                            interpreter.increaseScore(controller, bap * bap);
                        }
                    }*/


                    if (sb.length() > 200) {
                        if (sb.toString().replaceAll("\\!", "").replaceAll(" ", "").replaceAll("\\.", "").replaceAll(",", "").replaceAll("\\?", "").length() <= 1) {
                            sb = new StringBuilder();
                        } else {
                            if(previousBestScore < getInterpreter().getScore(getControllers()[0])){
                                try {
                                    if (fw == null) {
                                        fw = new FileWriter(writeTo);
                                    }
                                    fw.write(sb.toString().replaceAll("\\! \\!", " !!").replaceAll("\\! \\!", " !!").replaceAll(" \\!", "!").replaceAll(" \\.", ".").replaceAll(" ,", ",") + "\n");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            sb = new StringBuilder();
                        }
                    }
                    if (computerGeneratedText.size() > wordindex)
                        if (computerGeneratedText.get(wordindex) != null)
                            sb.append(computerGeneratedText.get(wordindex) + " ");
                }
            }
        }
    }

    @Override
    public void tick(int linesRan) {
        if (writerIndex > alice_in_wonderland.size() * 2) {
            getInterpreter().onTerminate(getControllers()[0]);
        }

    }

    @Override
    public BufferedImage render() {
        BufferedImage bufferedImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        int progress = 200 * writerIndex / alice_in_wonderland.size();
        Graphics2D g = (Graphics2D) bufferedImage.getGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        g.setColor(new Color(101, 232, 32));
        g.fillRect(0, 100, Math.min(200, progress), 50);
        g.setColor(new Color(54, 115, 25));
        g.fillRect(0, 100, Math.max(0, Math.min(200, progress - 200)), 50);
        return bufferedImage;
    }

    @Override
    public void onTerminate() {
        try {
            if (!wroteOnce && sb.length() >= 2) {
                boolean hasSame = false;
                String[] tempArray = new String[lastLines.length];
                for (int l = lastLines.length - 1; l > 0; l--) {
                    tempArray[l] = lastLines[l - 1];
                    if (lastLines[l] != null)
                        if (lastLines[l].equalsIgnoreCase(sb.toString())) {
                            hasSame = true;
                            break;
                        }
                }
                if (!hasSame) {
                    lastLines = tempArray;
                    lastLines[0] = sb.toString();
                }


                if (hasSame) {
                    sb = new StringBuilder();
                } else if (sb.length() > 2) {
                    if(previousBestScore < getInterpreter().getScore(getControllers()[0])) {
                        previousBestScore = getInterpreter().getScore(getControllers()[0]);
                        try {
                            if (fw == null) {
                                fw = new FileWriter(writeTo);
                            }
                            fw.write(sb.toString().replaceAll("\\! \\!", " !!").replaceAll("\\! \\!", " !!").replaceAll(" \\!", "!").replaceAll(" \\.", ".").replaceAll(" ,", ",") + "\n");

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        wroteOnce = true;
                        Main.log("Saving text " + writeTo.getName());
                    }
                }
            }
            if (fw != null) {
                fw.flush();
                fw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gameTick() {

    }

    private int[] previousVision = null;
    private int previousIndex = -1;

    @Override
    public int[] getVision(PersonalityMatrix old) {
        if (writerIndex >= alice_in_wonderland.size()) {
            return new int[0];
        }
        if (previousVision != null && previousIndex == writerIndex - 1) {
            int[] newintArray = previousVision;
            for (int i = 1; i < newintArray.length / 4; i++) {
                newintArray[(i * 4) - 4 + 0] = newintArray[(i * 4) + 0];
                newintArray[(i * 4) - 4 + 1] = newintArray[(i * 4) + 1];
            }


            String word = alice_in_wonderland.get(writerIndex);
            WordType type = WordBank.getWordType(word);
                newintArray[0] = type.getId();

            List<String> words = WordBank.getAllType(type);
            if (words == null) {
                newintArray[1] = -1;
                newintArray[2] = -1;
                newintArray[3] = -1;
            } else {
                for (int j = 0; j < words.size(); j++) {
                    if (words.get(j).equalsIgnoreCase(word)) {
                        newintArray[1] = j / 100;
                        newintArray[2] = j % 100;
                        newintArray[3] = j % 10;
                        break;
                    }
                }
            }
            previousVision = newintArray;
            previousIndex = writerIndex;
            return newintArray;
        }

        int[] vision = new int[4 * 10];
        for (int i = 0; i < vision.length / 4; i++) {
            if (writerIndex - i < 0)
                continue;
            if (writerIndex - i >= computerGeneratedText.size())
                return vision;
            String word = alice_in_wonderland.get(writerIndex - i);
            WordType type = WordBank.getWordType(word);
            if (type == WordType.GRAMMAR)
                vision[(i * 4) + 0] = 0;
            if (type == WordType.PRONOUNS)
                vision[(i * 4) + 0] = 1;
            if (type == WordType.NOUNS)
                vision[(i * 4) + 0] = 2;
            if (type == WordType.ADJECTIVES)
                vision[(i * 4) + 0] = 3;
            if (type == WordType.ADVERBS)
                vision[(i * 4) + 0] = 4;
            if (type == WordType.VERBS)
                vision[(i * 4) + 0] = 5;
            if (type == WordType.DETERMINER)
                vision[(i * 4) + 0] = 6;
            if (type == WordType.CONJUNCTION)
                vision[(i * 4) + 0] = 7;
            if (type == WordType.PREPOSITION)
                vision[(i * 4) + 0] = 8;
            if (type == WordType.INTERJECTION)
                vision[(i * 4) + 0] = 9;
            if (type == WordType.NAME)
                vision[(i * 4) + 0] = 10;
            if (type == WordType.NUMERAL)
                vision[(i * 4) + 0] = 11;

            List<String> words = WordBank.getAllType(type);
            if (words == null) {
                vision[(i * 4) + 1] = -1;
                vision[(i * 4) + 2] = -1;
                vision[(i * 4) + 3] = -1;
            } else {
                for (int j = 0; j < words.size(); j++) {
                    if (words.get(j).equalsIgnoreCase(word)) {
                        vision[(i * 4) + 1] = j / 100;
                        vision[(i * 4) + 2] = j % 100;
                        vision[(i * 4) + 3] = j % 10;
                        break;
                    }
                }
            }
        }
        previousIndex = writerIndex;
        previousVision = vision;
        return vision;
    }

    @Override
    public boolean isActive() {
        return writerIndex < (alice_in_wonderland.size()*2);
    }

    @Override
    public boolean displayOneView() {
        return true;
    }

    public static List<String> readAndCleanupText(String filename) {
        InputStream is = WriteTextGame.class.getResourceAsStream(filename);
        String text = null;
        try {
            text = DataBank.readFile(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        text = text.toLowerCase();
        text = text.replaceAll("-", " - ").replaceAll(",", " , ").replaceAll(":", "").replaceAll("\\)", " ) ").replaceAll("\\(", " ( ").replaceAll("\\?", " ? ").replaceAll(";", "").replaceAll("â", " ").replaceAll("\\!", " ! ").replaceAll("\t", " ").replaceAll("\\.", " . ")
                .replaceAll("'", "").replaceAll("\"", " \" ").replaceAll("\n", " ").replaceAll(";", "")
                .replaceAll("  ", " ").replaceAll("  ", " ").replaceAll("  ", " ").replaceAll("  ", " ");
        String[] words = text.split(" ");
        List<String> wor = new LinkedList<>();
        for (int i = 0; i < words.length; i++) {
            wor.add(words[i]);
        }
        return wor;
    }
}
