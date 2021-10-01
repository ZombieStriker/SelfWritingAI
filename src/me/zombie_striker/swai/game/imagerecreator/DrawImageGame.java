package me.zombie_striker.swai.game.imagerecreator;

import me.zombie_striker.swai.Main;
import me.zombie_striker.swai.data.PersonalityMatrix;
import me.zombie_striker.swai.game.AbstractGame;
import me.zombie_striker.swai.world.Interpreter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.LinkedList;

public class DrawImageGame extends AbstractGame {

    private static final int CATZ = 4;
    private static BufferedImage[] copycat;
    private static int[][][] copycatred;
    private static int[][][] copycatgreen;
    private static int[][][] copycatblue;


    static {
        try {
            copycat = new BufferedImage[CATZ];
            for(int i = 0; i < CATZ;i++) {
                copycat[i] = ImageIO.read(DrawImageGame.class.getResourceAsStream("/cat" + (i + 1) + ".png"));
                copycatred = new int[copycat[i].getWidth()][copycat[i].getHeight()][CATZ];
                copycatgreen = new int[copycat[i].getWidth()][copycat[i].getHeight()][CATZ];
                copycatblue = new int[copycat[i].getWidth()][copycat[i].getHeight()][CATZ];
                int[] colors = new int[3];
                WritableRaster raster = copycat[i].getRaster();
                for (int x = 0; x < copycat[i].getWidth(); x++)
                    for (int y = 0; y < copycat[i].getHeight(); y++) {
                        colors = raster.getPixel(x, y, colors);
                        copycatred[x][y][i] = colors[0];
                        copycatgreen[x][y][i] = colors[1];
                        copycatblue[x][y] [i]= colors[2];
                    }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Integer> redP = new LinkedList<>();
    private List<Integer> greenP = new LinkedList<>();
    private List<Integer> blueP = new LinkedList<>();

    private List<Integer> red = new LinkedList<>();
    private List<Integer> green = new LinkedList<>();
    private List<Integer> blue = new LinkedList<>();

    private PersonalityMatrix matrix;
    private Interpreter interpreter;

    private int indexX = 0;
    private int indexY = 0;

    private int round;
    private int zoomlevel = 1;

    private long startTime = System.currentTimeMillis();
    private long postTicks = -1;

    public DrawImageGame(PersonalityMatrix matrix, Interpreter interpreter, int round) {
        this.matrix = matrix;
        this.interpreter = interpreter;
        this.round = round;
    }

    private void addPointsForCorrectness(){
        for (int x = 0; x < zoomlevel / 2; x++) {
            for (int y = 0; y < zoomlevel / 2; y++) {
                if (redP.size() > 0) {
                    int r2 = redP.get(Math.min(redP.size() - 1, x + (y * (zoomlevel / 2))));
                    int g2 = greenP.get(Math.min(greenP.size() - 1, x + (y * (zoomlevel / 2))));
                    int b2 = blueP.get(Math.min(blueP.size() - 1, x + (y * (zoomlevel / 2))));

                    int ccr = 0;
                    int ccg = 0;
                    int ccb = 0;

                    for (int x1 = 0; x1 < copycatred.length / (zoomlevel / 2); x1++) {
                        for (int y1 = 0; y1 < copycatred.length / (zoomlevel / 2); y1++) {
                            ccr += copycatred[x1 + (copycatred.length * x / (zoomlevel / 2))][y1 + (copycatred.length * y / (zoomlevel / 2))][round % CATZ];
                            ccg += copycatgreen[x1 + (copycatred.length * x / (zoomlevel / 2))][y1 + (copycatred.length * y / (zoomlevel / 2))][round % CATZ];
                            ccb += copycatblue[x1 + (copycatred.length * x / (zoomlevel / 2))][y1 + (copycatred.length * y / (zoomlevel / 2))][round % CATZ];
                        }
                    }
                    ccr /= (copycatred.length / (zoomlevel / 2)) * (copycatred.length / (zoomlevel / 2));
                    ccg /= (copycatred.length / (zoomlevel / 2)) * (copycatred.length / (zoomlevel / 2));
                    ccb /= (copycatred.length / (zoomlevel / 2)) * (copycatred.length / (zoomlevel / 2));

                    interpreter.increaseScore(matrix, (int) ((((Math.sqrt(100.0 * (Math.abs(256 * 3
                                    - Math.abs((r2 - ccr) + (g2 - ccg) + (b2 - ccb))
                            )))))))
                    );
                }
            }
        }
    }

    @Override
    public void handleInputs(int[] inputs) {
        int r = inputs[0];
        int g = inputs[1];
        int b = inputs[2];

        if (interpreter.getWarpSpeed()) {
            if (zoomlevel > ((round / 128) % (copycatred.length - 5)) + 5) {
                if (postTicks == -1) {
                    addPointsForCorrectness();
                    postTicks = System.currentTimeMillis();
                }
                if (System.currentTimeMillis() - postTicks > 200) {
                    if (round % copycatred.length == copycatred.length - 1) {
                        if (Main.games.size() <= 3) {
                            try {
                                File f1 = new File(Main.getRunningJarLocation().getParentFile(), "/images/");
                                if (!f1.exists())
                                    f1.mkdirs();
                                File f2 = new File(f1, "image_" + round / copycatred.length + ".png");
                                if (!f2.exists()) {
                                    f2.createNewFile();
                                    ImageIO.write(render(), "png", f2);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    interpreter.onTerminate(matrix);
                    return;
                }
                return;
            }
        } else {
            if (zoomlevel >= ((round / 128) % copycatred.length) + 1) {
                if (postTicks == -1) {
                    addPointsForCorrectness();
                    postTicks = System.currentTimeMillis();
                }

                if (System.currentTimeMillis() - postTicks > 200) {
                    interpreter.onTerminate(matrix);
                }
                return;
            }
        }

        if (r < 0 || g < 0 || b < 0) {
            if (postTicks == -1) {
                red.add(Math.max(0, r));
                green.add(Math.max(0, g));
                blue.add(Math.max(0, b));
            }
            indexX++;
            if (indexX >= zoomlevel) {
                indexX = 0;
                indexY++;
                if (indexY >= zoomlevel) {
                    if (postTicks == -1) {
                        zoomlevel *= 2;
                        indexY = 0;

                        redP = red;
                        greenP = green;
                        blueP = blue;
                        red = new LinkedList<>();
                        green = new LinkedList<>();
                        blue = new LinkedList<>();
                    }
                }
            }
            return;
        } else {
            if (postTicks == -1) {
                red.add(r);
                green.add(g);
                blue.add(b);
            }

            indexX++;
            if (indexX >= zoomlevel) {
                indexX = 0;
                indexY++;
                if (indexY >= zoomlevel) {
                    if (postTicks == -1) {
                        zoomlevel *= 2;
                        indexY = 0;

                        redP = red;
                        greenP = green;
                        blueP = blue;

                        red = new LinkedList<>();
                        green = new LinkedList<>();
                        blue = new LinkedList<>();
                    }
                }
            }
        }
    }

    @Override
    public void tick(int linesRan) {
        if (zoomlevel >= copycatred.length) {
            interpreter.onTerminate(matrix);
        }
    }


    @Override
    public BufferedImage render() {
        BufferedImage bi = new BufferedImage(copycatred.length, copycatred.length, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) bi.getGraphics();
        g.setColor(Color.MAGENTA);
        g.drawRect(0, 0, copycatred.length, copycatred.length);
        for (int x = 0; x < zoomlevel / 2; x++) {
            for (int y = 0; y < zoomlevel / 2; y++) {
                if (x >= copycatred.length || y >= copycatred.length) {
                    continue;
                }
                int r1 = redP.get(x + (y * (zoomlevel / 2)));
                int g1 = greenP.get(x + (y * (zoomlevel / 2)));
                int b1 = blueP.get(x + (y * (zoomlevel / 2)));

                if (r1 == -1 || g1 == -1 || b1 == -1) {
                    g.setColor(Color.BLACK);
                } else if (r1 >= 255 || g1 >= 255 || b1 >= 255) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(new Color(r1, g1, b1));
                }
                g.fillRect((int) (x * (copycatred.length * 2 / zoomlevel)), (int) (y * (copycatred.length * 2 / zoomlevel)),
                        copycatred.length * 2 / zoomlevel, copycatred.length * 2 / zoomlevel);
            }
        }
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
        int[] vision = new int[4 + (9 * 3)];
        vision[0] = zoomlevel;
        vision[1] = (int) (indexX * (copycatred.length / zoomlevel));
        vision[2] = (int) (indexY * (copycatred.length / zoomlevel));
        vision[3] = round%CATZ;
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                vision[4 + ((x + (y * 3)) * 3) + 0] = copycatred[(int) (Math.min(99, Math.max(0, indexX + x - 1) * (copycatred.length / zoomlevel)))]
                        [(int) (Math.min(99, Math.max(0, indexY + y - 1) * (copycatred.length / zoomlevel)))][round%CATZ];
                vision[4 + ((x + (y * 3)) * 3) + 1] = copycatgreen[(int) (Math.min(99, Math.max(0, indexX + x - 1) * (copycatred.length / zoomlevel)))]
                        [(int) (Math.min(99, Math.max(0, indexY + y - 1) * (copycatred.length / zoomlevel)))][round%CATZ];
                vision[4 + ((x + (y * 3)) * 3) + 2] = copycatblue[(int) (Math.min(99, Math.max(0, indexX + x - 1) * (copycatred.length / zoomlevel)))]
                        [(int) (Math.min(99, Math.max(0, indexY + y - 1) * (copycatred.length / zoomlevel)))][round%CATZ];
            }
        }
        return vision;
    }
}
