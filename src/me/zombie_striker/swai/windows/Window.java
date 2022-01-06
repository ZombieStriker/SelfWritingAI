package me.zombie_striker.swai.windows;

import me.zombie_striker.swai.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Window extends Canvas implements Runnable {

    private BufferedImage image = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);

    private Thread thread;
    private JFrame window;
    public boolean running = true;

    private WindowCore core;

    public Window(WindowCore core) {
        this.core = core;
        window = new JFrame("SWAI View");
        window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        window.setSize(1000, 1000+50);
        window.setLayout(new BorderLayout());
        window.add(this, BorderLayout.CENTER);
        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        core.init(window);

        thread = new Thread(this);
        thread.start();
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }


    @Override
    public void run() {
        long lasttime = System.nanoTime();
        double nspertick = 1000000000D / 60D;

        int frames = 0;
        int ticks = 0;

        long lasttimer = System.currentTimeMillis();
        double delta = 0;
        while (running && window.isShowing()) {
            long now = System.nanoTime();
            delta += (now - lasttime) / nspertick;
            lasttime = now;

            boolean shouldRender = true;

            delta=1;

            while (delta >= 1) {
                ticks++;
                core.tick();
                delta--;
                shouldRender = true;
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (shouldRender) {
                frames++;
                image = core.render(window);
                render();
            }

            if (System.currentTimeMillis() - lasttimer >= 1000) {
                lasttimer += 1000;
                frames = 0;
                ticks = 0;
            }
        }
        Main.windows.remove(this);
    }


    private void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.BLACK);

        g.fillRect(0, 0, getWidth(), getHeight());

        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);

        g.dispose();
        bs.show();
    }
}
