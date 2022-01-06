package me.zombie_striker.swai.game.imagerecreator;

import me.zombie_striker.swai.Main;
import me.zombie_striker.swai.data.DataBank;
import me.zombie_striker.swai.data.PersonalityMatrix;
import me.zombie_striker.swai.game.AbstractGame;
import me.zombie_striker.swai.world.Interpreter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.LinkedList;

public class DrawImageGame extends AbstractGame {

    private static final int TRAINING_DATA_SIZE = 10;
    private static BufferedImage[] copycat;
    private static int[][][] copycatred;
    private static int[][][] copycatgreen;
    private static int[][][] copycatblue;


    private static int[][][] startingCatRed;
    private static int[][][] startingCatGreen;
    private static int[][][] startingCatBlue;

    private static final int startingZoom = 8;

    static {
        try {
            copycat = new BufferedImage[TRAINING_DATA_SIZE];
            copycatred = new int[128][128][TRAINING_DATA_SIZE];
            copycatgreen = new int[128][128][TRAINING_DATA_SIZE];
            copycatblue = new int[128][128][TRAINING_DATA_SIZE];
            startingCatRed = new int[startingZoom][startingZoom][TRAINING_DATA_SIZE];
            startingCatGreen = new int[startingZoom][startingZoom][TRAINING_DATA_SIZE];
            startingCatBlue = new int[startingZoom][startingZoom][TRAINING_DATA_SIZE];
            for (int i = 0; i < TRAINING_DATA_SIZE; i++) {
                copycat[i] = ImageIO.read(DrawImageGame.class.getResourceAsStream("/rocks" + (i + 1) + ".png"));
                for (int x = 0; x < copycat[i].getWidth(); x++)
                    for (int y = 0; y < copycat[i].getHeight(); y++) {
                        int ccc = copycat[i].getRGB(x, y);

                        int blue = ccc & 0xff;
                        int green = (ccc & 0xff00) >> 8;
                        int red = (ccc & 0xff0000) >> 16;

                        copycatred[x][y][i] = red;
                        copycatgreen[x][y][i] = green;
                        copycatblue[x][y][i] = blue;
                    }
                for (int x = 0; x < copycatred.length; x++) {
                    for (int y = 0; y < copycatred.length; y++) {
                        startingCatRed[x / (128 / startingZoom)][y / (128 / startingZoom)][i] = startingCatRed[x / (128 / startingZoom)][y / (128 / startingZoom)][i] + copycatred[x][y][i];
                        startingCatGreen[x / (128 / startingZoom)][y / (128 / startingZoom)][i] = startingCatGreen[x / (128 / startingZoom)][y / (128 / startingZoom)][i] + copycatgreen[x][y][i];
                        startingCatBlue[x / (128 / startingZoom)][y / (128 / startingZoom)][i] = startingCatBlue[x / (128 / startingZoom)][y / (128 / startingZoom)][i] + copycatblue[x][y][i];
                    }
                }
                for (int x = 0; x < startingZoom; x++) {
                    for (int y = 0; y < startingZoom; y++) {
                        startingCatRed[x][y][i] = startingCatRed[x][y][i] / ((128 / startingZoom) * (128 / startingZoom));
                        startingCatGreen[x][y][i] = startingCatGreen[x][y][i] / ((128 / startingZoom) * (128 / startingZoom));
                        startingCatBlue[x][y][i] = startingCatBlue[x][y][i] / ((128 / startingZoom) * (128 / startingZoom));
                    }
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

    private int indexX = 0;
    private int indexY = 0;

    private int round;
    private int zoomlevel = startingZoom;

    private long postTicks = -1;
    private int imageID = 0;
    private boolean active = true;
    private boolean doneDrawing;
    private int loop = 0;

    public DrawImageGame(PersonalityMatrix[] matrix, Interpreter interpreter, int round) {
        super(null, matrix, interpreter);
        this.round = round;
        doneDrawing = false;
        ;
        int d = round / 10;
        imageID = d % TRAINING_DATA_SIZE;
    }

    private void addPointsForCorrectness() {
        for (int x = 0; x < zoomlevel / 2; x++) {
            for (int y = 0; y < zoomlevel / 2; y++) {
                if (redP.size() > 0) {
                    int r2 = redP.get(Math.min(redP.size() - 1, x + (y * (zoomlevel / 2))));
                    int g2 = greenP.get(Math.min(greenP.size() - 1, x + (y * (zoomlevel / 2))));
                    int b2 = blueP.get(Math.min(blueP.size() - 1, x + (y * (zoomlevel / 2))));

                    if (r2 == -1 || g2 == -1 || b2 == -1) {
                        continue;
                    }

                    int ccr = 0;
                    int ccg = 0;
                    int ccb = 0;

                    for (int x1 = 0; x1 < copycatred.length / (zoomlevel / 2); x1++) {
                        for (int y1 = 0; y1 < copycatred.length / (zoomlevel / 2); y1++) {
                            ccr += copycatred[x1 + (copycatred.length * x / (zoomlevel / 2))][y1 + (copycatred.length * y / (zoomlevel / 2))][imageID];
                            ccg += copycatgreen[x1 + (copycatred.length * x / (zoomlevel / 2))][y1 + (copycatred.length * y / (zoomlevel / 2))][imageID];
                            ccb += copycatblue[x1 + (copycatred.length * x / (zoomlevel / 2))][y1 + (copycatred.length * y / (zoomlevel / 2))][imageID];
                        }
                    }
                    ccr /= (copycatred.length / (zoomlevel / 2)) * (copycatred.length / (zoomlevel / 2));
                    ccg /= (copycatred.length / (zoomlevel / 2)) * (copycatred.length / (zoomlevel / 2));
                    ccb /= (copycatred.length / (zoomlevel / 2)) * (copycatred.length / (zoomlevel / 2));

                    int points = (int) (100.0 * (Math.abs(256 * 3
                            - Math.abs((r2 - ccr) + (g2 - ccg) + (b2 - ccb)
                    ))));
                    //points *= points;

                    getInterpreter().increaseScore(getControllers()[0], points);
                }
            }
        }
    }

    @Override
    public void handleInputs(PersonalityMatrix old, int[] inputs) {
        if (old == getControllers()[1]) {
            if (doneDrawing) {
                indexX++;
                if (indexX >= zoomlevel) {
                    indexX = 0;
                    indexY++;
                    if (indexY >= zoomlevel) {
                        int[] scores = new int[10];
                        int highestIndex = -1;
                        int highestScore = -1;
                        int secondHighestScore = -1;
                        for (int i = 0; i < old.getPalletsForInputs(); i++) {
                            scores[i] = inputs[i];
                            if (highestScore < scores[i]) {
                                secondHighestScore = highestScore;
                                highestScore = scores[i];
                                highestIndex = i;
                            }
                        }

                        if (doneDrawing) {
                            getInterpreter().increaseScore(getControllers()[1], 100);
                            if (highestIndex != imageID) {
                                getInterpreter().divideScoreBy(getControllers()[0], Math.max(1, highestScore));
                                getInterpreter().loseScore(getControllers()[1], Math.max(1, scores[imageID]));
                                active = false;
                            } else {
                                getInterpreter().increaseScore(getControllers()[1], highestScore - secondHighestScore);
                                doneDrawing = false;
                                active = true;
                                indexX = 0;
                                indexY = 0;
                                zoomlevel = startingZoom;
                                loop++;
                                imageID = DataBank.seededRandom(imageID, round + loop, round * imageID).nextInt(TRAINING_DATA_SIZE);
                                postTicks = -1;
                                if (loop > 50) {
                                    active = false;
                                }
                            }
                        }
                    }
                }
            }
            return;
        }
        if (doneDrawing)
            return;

        int r = inputs[0];
        int g = inputs[1];
        int b = inputs[2];

        if (getInterpreter().getWarpSpeed()) {
            if (zoomlevel > startingZoom) {
                if (postTicks == -1) {
                    addPointsForCorrectness();
                    postTicks = System.currentTimeMillis();
                }
                if (System.currentTimeMillis() - postTicks > 50) {
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

                    doneDrawing = true;
                    if (round % 2 == 1)
                        zoomlevel = startingZoom;
                    return;
                }
                return;
            }
        } else {
            if (zoomlevel > (((round / 128)) % (128 - startingZoom)) + startingZoom) {
                if (postTicks == -1) {
                    addPointsForCorrectness();
                    postTicks = System.currentTimeMillis();
                }

                if (System.currentTimeMillis() - postTicks > 1000) {
                    doneDrawing = true;
                    if (round % 2 == 1)
                        zoomlevel = startingZoom;
                }
                return;
            }
        }

        if (r < 0 || g < 0 || b < 0 || r > 255 || g > 255 || b > 255) {
            if (postTicks == -1) {
                red.add(r < 0 ? 0 : r > 255 ? 255 : r);
                green.add(g < 0 ? 0 : g > 255 ? 255 : g);
                blue.add(b < 0 ? 0 : b > 255 ? 255 : b);
            }
        } else {
            if (postTicks == -1) {
                red.add(r);
                green.add(g);
                blue.add(b);
            }
        }

        indexX++;
        if (indexX >= zoomlevel) {
            indexX = 0;
            indexY++;
            if (indexY >= zoomlevel) {
                zoomlevel *= 2;
                indexY = 0;

                redP = red;
                greenP = green;
                blueP = blue;

                red = new LinkedList<>();
                green = new LinkedList<>();
                blue = new LinkedList<>();
                if (zoomlevel >= 128) {
                    if (round % 2 == 1)
                        zoomlevel = startingZoom;
                    //imageID++;
                    doneDrawing = true;
                }

            }
        }
    }

    @Override
    public void tick(int linesRan) {
        if (!active) {
            getInterpreter().onTerminate(getControllers()[0]);
            getInterpreter().onTerminate(getControllers()[1]);
            return;
        }
    }


    @Override
    public BufferedImage render() {
        BufferedImage bi = new BufferedImage(copycatred.length, copycatred.length, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) bi.getGraphics();
        for (int x = 0; x < zoomlevel / 2; x++) {
            for (int y = 0; y < zoomlevel / 2; y++) {
                if (x >= copycatred.length || y >= copycatred.length) {
                    continue;
                }
                int r1;
                int g1;
                int b1;

                if (zoomlevel == startingZoom) {
                    r1 = startingCatRed[x][y][imageID];
                    g1 = startingCatGreen[x][y][imageID];
                    b1 = startingCatBlue[x][y][imageID];
                } else {
                    if (redP.size() <= x + (y * (zoomlevel / 2)))
                        continue;
                    r1 = redP.get(x + (y * (zoomlevel / 2)));
                    g1 = greenP.get(x + (y * (zoomlevel / 2)));
                    b1 = blueP.get(x + (y * (zoomlevel / 2)));
                }

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
    public boolean displayOneView() {
        return true;
    }

    @Override
    public int[] getVision(PersonalityMatrix old) {
        if (old == getControllers()[1] && doneDrawing)
            return new int[0];
        int[] vision = new int[4 + (15 * 15 * 3)];
        vision[0] = zoomlevel;
        vision[1] = (int) (indexX * (copycatred.length / zoomlevel));
        vision[2] = (int) (indexY * (copycatred.length / zoomlevel));
        vision[3] = -1;//imageID;
        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 15; y++) {
                if (redP.size() > x + (y * (zoomlevel / 2)) && (x - 7) + ((y - 7) * (zoomlevel / 2)) >= 0) {
                    if (zoomlevel > startingZoom && (old == getControllers()[0] || round % 2 == 0)) {
                        if (4 + (x + (y * 15) * 3) > vision.length)
                            continue;
                        vision[4 + ((x + (y * 15)) * 3) + 0] = redP.get(x - 7 + ((y - 7) * (zoomlevel / 2)));
                        vision[4 + ((x + (y * 15)) * 3) + 1] = greenP.get(x - 7 + ((y - 7) * (zoomlevel / 2)));
                        vision[4 + ((x + (y * 15)) * 3) + 2] = blueP.get(x - 7 + ((y - 7) * (zoomlevel / 2)));
                    } else {
                        if (4 + (x + (y * 15) * 3) > vision.length)
                            continue;
                        if(indexX + x -7 < 0)
                            continue;
                        if(indexY + y - 7 < 0)
                            continue;
                        if(indexX + x -7 > zoomlevel/2)
                            continue;
                        if(indexY + y - 7 > zoomlevel/2)
                            continue;
                        vision[4 + ((x + (y * 15)) * 3) + 0] = startingCatRed[indexX + x - 7][indexY + y - 7][imageID];
                        vision[4 + ((x + (y * 15)) * 3) + 1] = startingCatGreen[indexX + x - 7][indexY + y - 7][imageID];
                        vision[4 + ((x + (y * 15)) * 3) + 2] = startingCatBlue[indexX + x - 7][indexY + y - 7][imageID];
                    }
                }
            }
        }
        return vision;
    }

    @Override
    public boolean isActive() {
        return active;
    }
}
