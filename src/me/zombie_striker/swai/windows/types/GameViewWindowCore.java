package me.zombie_striker.swai.windows.types;

import me.zombie_striker.swai.Main;
import me.zombie_striker.swai.data.PersonalityMatrix;
import me.zombie_striker.swai.game.AbstractGame;
import me.zombie_striker.swai.windows.WindowCore;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameViewWindowCore implements WindowCore {
    @Override
    public void tick() {
        Main.gameworldInterpreter.tick();
    }

    @Override
    public BufferedImage render(JFrame frame) {
        int index = 0;
        BufferedImage image = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        List<PersonalityMatrix> list = new ArrayList<>(Main.games.keySet());
        Collections.sort(list);
        g.setFont(new Font("Courier", Font.BOLD, 12));
        List<AbstractGame> renderedGames = new ArrayList<>();
        for (PersonalityMatrix matrix : list) {
            AbstractGame ag = Main.games.get(matrix);
            if(!renderedGames.contains(ag)){
                renderedGames.add(ag);
            }else if (ag.displayOneView()){
                continue;
            }
            int xpos = index % (Main.MAX_PERSONALITIES_PER_GAME / Main.PERSONALITIES_PER_ROW);
            int ypos = index / Main.PERSONALITIES_PER_ROW;
            int width = image.getWidth() / (Main.MAX_PERSONALITIES_PER_GAME / Main.PERSONALITIES_PER_ROW);
            int height = (image.getHeight() - 200) / Main.PERSONALITIES_PER_ROW;
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
        for (int i = 0; i < Main.textlog.length; i++) {
            if (Main.textlog[i] != null)
                g.drawString(Main.textlog[i], 0, textHeight + (12 * i) + 11);
        }
        g.dispose();
        return image;
    }

    @Override
    public void init(JFrame window) {

    }
}
