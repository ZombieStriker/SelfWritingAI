package me.zombie_striker.swai.game.battledroids;

import me.zombie_striker.swai.Main;
import me.zombie_striker.swai.data.PersonalityMatrix;
import me.zombie_striker.swai.game.AbstractGame;
import me.zombie_striker.swai.game.defaults.Tile;
import me.zombie_striker.swai.world.Interpreter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class BattleDroidSimulatorGame extends AbstractGame {

    private boolean forward = false;
    private boolean back = false;
    private boolean left = false;
    private boolean right = false;

    private boolean lookleft = false;
    private boolean lookright = false;

    private boolean shoot = false;
    private boolean reload = false;
    private int reloadCooldown = 0;
    private static final int RELOAD_COOLDOWN_MAX = 20;

    private double droidX = ThreadLocalRandom.current().nextInt(199) + 1;
    private double droidY = 0;
    private double droidZ = ThreadLocalRandom.current().nextInt(199) + 1;

    private static final int BULLETS_PER_RELOAD = 35;
    private int bulletCount = BULLETS_PER_RELOAD;
    private int reloads = 5;
    private int shootDelay = 0;
    private static final int SHOOT_DELAY = 5;

    private final double VIEW_STEP = 0.1;

    private double yaw = ThreadLocalRandom.current().nextInt(360) / 180 * Math.PI;

    private static Tile[][][] map;
    private static HashMap<PersonalityMatrix, Tile> players = new HashMap<>();
    private static List<BulletTile> bulletTiles = new ArrayList<>();

    private int health = 5;

    private static int timer = 0;
    private static final int timerIncreaseByPerShot = 3;

    @Override
    public void onTerminate() {
        players.remove(getControllers()[0]);
        if (players.size() <= 0)
            resetMap();
    }

    @Override
    public void gameTick() {
        timer--;
        int i = 0;
        BulletTile[] deadBullets = new BulletTile[bulletTiles.size()];
        for (BulletTile bt : bulletTiles) {
            for(int traveltime =0; traveltime < 30; traveltime++) {
                if ((int) (bt.getX() + bt.getXD()) != (int) (bt.getX()) ||
                        (int) (bt.getY() + bt.getZD()) != (int) (bt.getY())) {
                    map[(int) bt.getX()][0][(int) bt.getY()] = null;
                    if (map[(int) (bt.getX() + bt.getXD())][0][(int) (bt.getY() + bt.getZD())] != null) {
                        if (map[(int) (bt.getX() + bt.getXD())][0][(int) (bt.getY() + bt.getZD())].getType() == 4) {
                            map[(int) (bt.getX() + bt.getXD())][0][(int) (bt.getY() + bt.getZD())] = new Tile((int) (bt.getX() + bt.getXD()), (int) (bt.getY() + bt.getZD()), 3);
                           getInterpreter().increaseScore(bt.getShooter(), 1000);
                            timer += timerIncreaseByPerShot;
                        }
                        deadBullets[i] = bt;
                        i++;
                        break;
                    } else {
                        map[(int) (bt.getX() + bt.getXD())][0][(int) (bt.getY() + bt.getZD())] = bt;
                    }
                } else {
                    //Same Tile
                }
                bt.setY(bt.getY() + bt.getZD());
                bt.setX(bt.getX() + bt.getXD());
            }
        }
        for (BulletTile bt : deadBullets) {
            if (bt != null)
                bulletTiles.remove(bt);
        }
    }

    public BattleDroidSimulatorGame(PersonalityMatrix controller, Interpreter interpreter, int round) {
        super(null,controller,interpreter);
        if (map == null) {
            resetMap();
        }
        players.put(controller, map[(int) droidX][0][(int) droidZ] = new Tile((int) droidX, (int) droidZ, 4));
    }

    private void resetMap() {
        Main.log("Map Reset");
        timer = 20 * 10;
        map = new Tile[201][1][201];
        for (int x = 0; x < 201; x++) {
            map[x][0][0] = new Tile(x, 0, 1 + x % 2);
            map[x][0][200] = new Tile(x, 200, 1 + x % 2);
        }
        for (int z = 0; z < 201; z++) {
            map[0][0][z] = new Tile(0, z, 5 + z % 2);
            map[200][0][z] = new Tile(200, z, 5 + z % 2);
        }
    }

    @Override
    public boolean displayOneView() {
        return true;
    }
    @Override
    public void handleInputs(PersonalityMatrix old, int[] inputs) {
        forward = inputs[0]> 0;
        back = inputs[1]> 0;
        left = inputs[2]> 0;
        right = inputs[3]> 0;

        lookleft = inputs[4]> 0;
        lookright = inputs[5]> 0;

        shoot = inputs[6]> 0;
        reload = inputs[7]> 0;
    }

    @Override
    public void tick(int linesRan) {
        getInterpreter().increaseScore(getControllers()[0], 1);
        if (lookleft) {
            yaw -= Math.PI / 40;
        }
        if (lookright) {
            yaw += Math.PI / 40;
        }
        double cosy = Math.cos(yaw);
        double siny = Math.sin(yaw);
        double droidXT = droidX;
        double droidZT = droidZ;
        if (forward) {
            droidXT += (cosy) - (cosy * siny);
            droidZT += (siny) - (cosy * siny);
        }
        if (back) {
            droidXT -= (cosy) - (cosy * siny);
            droidZT -= (siny) - (cosy * siny);
        }
        if (right) {
            droidXT += (siny) - (cosy * siny);
            droidZT += (cosy) - (cosy * siny);
        }
        if (left) {
            droidXT -= (siny) - (cosy * siny);
            droidZT -= (cosy) - (cosy * siny);
        }
        if (droidXT >= 0 && droidXT < 201 && droidZT >= 0 && droidZT < 201) {
            if (map[(int) droidX][0][(int) droidZ] != null) {
                if (map[(int) droidX][0][(int) droidZ].getType() == 3) {
                    health--;
                    if(health<=0) {
                        getInterpreter().onTerminate(getControllers()[0]);
                        map[(int) droidX][0][(int) droidZ] = null;
                        return;
                    }
                }
            }
            if (map[(int) droidXT][(int) droidY][(int) droidZT] == null) {
                map[(int) droidX][0][(int) droidZ] = null;
                droidX = droidXT;
                droidZ = droidZT;
                map[(int) droidX][0][(int) droidZ] = new Tile((int) droidX, (int) droidZ, 4);
            }
        }
        /*if (lookat == 4) {
            if (forward || back || left || right || lookleft || lookright)
                if (forward != back || left != right || lookright != lookleft)
                    Main.world.incrementScore(controller, 100);
        }*/
        if (reload) {
            if (reloads > 0) {
                if (reloadCooldown > 0) {
                    reloadCooldown--;
                    if (reloadCooldown == 0) {
                        bulletCount = BULLETS_PER_RELOAD;
                        reloads--;
                    }
                } else {
                    reloadCooldown = RELOAD_COOLDOWN_MAX;
                }
            }
        }
        if(shootDelay > 0)
        shootDelay--;
        if (shoot) {
            if (reloadCooldown <= 0)
                if (bulletCount > 0) {
                    if (shootDelay > 0) {
                        shootDelay = SHOOT_DELAY;
                        bulletCount--;
                        double xdelta = Math.cos((yaw / Math.PI));
                        double zdelta = Math.sin((yaw / Math.PI));

                        BulletTile newBullet = new BulletTile(droidX + xdelta, droidZ + zdelta, xdelta, zdelta, getControllers()[0]);

                        if (map[(int) (droidX + xdelta)][(int) droidY][(int) (droidZ + zdelta)] != null) {
                            if (map[(int) (droidX + xdelta)][(int) droidY][(int) (droidZ + zdelta)].getType() == 4) {
                                map[(int) (droidX + xdelta)][(int) droidY][(int) (droidZ + zdelta)] = new Tile(droidX + xdelta, droidZ + zdelta, 3);
                                getInterpreter().increaseScore(getControllers()[0], 1000);
                                timer += timerIncreaseByPerShot;
                            }
                        } else {
                            map[(int) (droidX + xdelta)][(int) droidY][(int) (droidZ + zdelta)] = newBullet;
                            bulletTiles.add(newBullet);
                        }
                    }
                }
        }
        if (timer <= 0) {
            getInterpreter().onTerminate(getControllers()[0]);
            return;
        }
    }

    @Override
    public BufferedImage render() {
        BufferedImage subimage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D gs = (Graphics2D) subimage.getGraphics();
        int[] visionDistance = getVisionDistance();
        int[] vision = getVision(getControllers()[0]);
        for (int i = 0; i < 100; i++) {
            if (vision[INPUTS + i] == 1) {
                gs.setColor(new Color(67, 78, 105));
            } else if (vision[INPUTS + i] == 2) {
                gs.setColor(new Color(104, 117, 150));
            } else if (vision[INPUTS + i] == 3) {
                gs.setColor(Color.MAGENTA);
            } else if (vision[INPUTS + i] == 4) {
                gs.setColor(Color.RED);
            } else if (vision[INPUTS + i] == 5) {
                gs.setColor(new Color(123, 108, 108));
            } else if (vision[INPUTS + i] == 6) {
                gs.setColor(new Color(110, 87, 87));
            } else if (vision[INPUTS + i] == 7) {
                gs.setColor(Color.YELLOW);
            } else {
                gs.setColor(Color.BLACK);
            }
            gs.fillRect(i, 50 - Math.min(50, (900 / ((int) (visionDistance[i] * VIEW_STEP) + 3))), 1, Math.min(100, (900 / ((int) (visionDistance[i] * VIEW_STEP) + 3)) * 2));
        }
        gs.dispose();
        return subimage;
    }

    public int[] getVisionDistance() {
        int[] vision = new int[100];

        for (double i = 0; i < 100; i++) {
            double xdelta = Math.cos((yaw / Math.PI) + (((i - 50) / 20) / Math.PI)) * VIEW_STEP;
            double zdelta = Math.sin((yaw / Math.PI) + (((i - 50) / 20) / Math.PI)) * VIEW_STEP;

            Tile hit = null;

            double xd = 0;
            double zd = 0;

            for (int d = 0; d < 300 / VIEW_STEP; d++) {
                xd += xdelta;
                zd += zdelta;
                if (droidX + xd >= 201 || droidZ + zd >= 201)
                    break;
                if (droidX + xd < 0 || droidZ + zd < 0)
                    break;
                Tile lookAtTest = map[(int) (droidX + xd)][(int) droidY][(int) (droidZ + zd)];
                if (lookAtTest != null) {
                    if (lookAtTest.getX() == droidX && lookAtTest.getY() == droidZ)
                        continue;
                    hit = lookAtTest;
                    vision[(int) i] = d;
                    break;
                }
            }

            if (hit == null) {
                vision[(int) i] = -1;
            }
        }

        return vision;
    }

    public static final int INPUTS = 5;

    @Override
    public int[] getVision(PersonalityMatrix old) {
        int[] vision = new int[INPUTS + 100];
        vision[0] = (byte) droidX;
        vision[1] = (byte) droidY;
        vision[2] = (byte) (droidZ);
        vision[3] = bulletCount;
        vision[4] = reloads;

        for (double i = 0; i < 100; i++) {
            double xdelta = Math.cos((yaw / Math.PI) + (((i - 50) / 20) / Math.PI)) * VIEW_STEP;
            double zdelta = Math.sin((yaw / Math.PI) + (((i - 50) / 20) / Math.PI)) * VIEW_STEP;

            Tile hit = null;

            double xd = 0;
            double zd = 0;

            for (int d = 0; d < 300 / VIEW_STEP; d++) {
                xd += xdelta;
                zd += zdelta;
                if (droidX + xd >= 201 || droidZ + zd >= 201)
                    break;
                if (droidX + xd < 0 || droidZ + zd < 0)
                    break;
                Tile lookAtTest = map[(int) (droidX + xd)][(int) droidY][(int) (droidZ + zd)];
                if (lookAtTest != null) {
                    if (lookAtTest.getX() == droidX && lookAtTest.getY() == droidZ)
                        continue;
                    hit = lookAtTest;
                    break;
                }
            }

            if (hit == null) {
                vision[(int) (INPUTS + i)] = -1;
            } else {
                vision[(int) (INPUTS + i)] = hit.getType();
            }
        }

        return vision;
    }

    @Override
    public boolean isActive() {
        return health > 0 && timer > 0;
    }
}
