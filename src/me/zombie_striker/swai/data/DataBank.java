package me.zombie_striker.swai.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class DataBank {

    public static final char[] chars = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static double sigmoid(double x) {
        return (1/( 1 + Math.pow(Math.E,(-1*x))));
    }

    public static String translate(int value) {
        if (value >= chars.length)
            return (value - chars.length) + "";
        if (value < 0)
            return value + "";
        return chars[value] + "";
    }

    private static int INCREMENTALRANDOMNESS = 10;

    public static Random seededRandom(int input1, int input2, int input3){
        Random random1 = new Random(input1);
        Random random2 = new Random(random1.nextInt(369)+input1);
        Random random3 = new Random(random2.nextInt(7183043)+random1.nextInt(input2+1254)+(input3+1));

        INCREMENTALRANDOMNESS++;
        if(INCREMENTALRANDOMNESS> 123456789){
            INCREMENTALRANDOMNESS=9;
        }
        return new Random(random3.nextInt(INCREMENTALRANDOMNESS));
    }

    public static byte getIndexOf(char c) {
        for (byte i = 0; i < chars.length; i++) {
            if (chars[i] == c)
                return i;
        }
        return -1;
    }

    public static int getRandomExponentialDistribution(int range, int lambda){
        return (int) (-(Math.log(ThreadLocalRandom.current().nextDouble())/Math.sqrt(lambda)) * range);
    }

    public static byte[] encode(String value) {
        byte[] message = new byte[value.length()];
        String uppercase = value.toUpperCase();
        for (int i = 0; i < value.length(); i++) {
            for (int j = 0; j < chars.length; j++) {
                if (chars[j] == uppercase.charAt(i)) {
                    message[i] = (byte) j;
                }
            }
        }
        return message;
    }


    public static String readFile(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int ch; (ch = is.read()) != -1; ) {
            sb.append((char) ch);
        }
        return sb.toString();
    }
}
