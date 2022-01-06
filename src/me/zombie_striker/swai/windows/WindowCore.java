package me.zombie_striker.swai.windows;

import javax.swing.*;
import java.awt.image.BufferedImage;

public interface WindowCore {

    void tick();
    BufferedImage render(JFrame window);

    void init(JFrame window);
}
