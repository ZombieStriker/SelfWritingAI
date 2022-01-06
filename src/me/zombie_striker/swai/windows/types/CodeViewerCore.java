package me.zombie_striker.swai.windows.types;

import me.zombie_striker.swai.assignablecode.AssignableCloseBracket;
import me.zombie_striker.swai.assignablecode.AssignableOpenBracketType;
import me.zombie_striker.swai.data.PersonalityMatrix;
import me.zombie_striker.swai.windows.WindowCore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

public class CodeViewerCore implements WindowCore, MouseWheelListener, KeyListener {

    private int scrollHeight = 0;
    private StringBuilder[] linesEditing;

    private static final int TEXTSIZE = 14;

    private int textindex = 0;
    private int textline = 0;
    private int tick = 0;

    private PersonalityMatrix work;


    public CodeViewerCore(PersonalityMatrix work) {
        this.work = work;
        linesEditing = new StringBuilder[work.getCode().length];
        int tabs = 0;
        for (int i = 0; i < linesEditing.length; i++) {
            linesEditing[i] = new StringBuilder();
            if (work.getCode()[i] != null) {
                if(work.getCode()[i] instanceof AssignableCloseBracket)
                    tabs--;
                if(tabs <0)
                    tabs = 0;
                if(tabs > 0) {
                    linesEditing[i].append("  ".repeat(tabs) + work.getCode()[i].toString());
                }else {
                    linesEditing[i].append(work.getCode()[i].toString());
                }
                if(work.getCode()[i] instanceof AssignableOpenBracketType)
                    tabs++;
                if(tabs >=10)
                    tabs = 10;
            }
        }
    }


    @Override
    public void tick() {
        tick++;
    }

    @Override
    public BufferedImage render(JFrame window) {
        BufferedImage bi = new BufferedImage(window.getWidth(), window.getHeight()-300, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) bi.getGraphics();
        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
        g.setColor(new Color(22, 192, 22));
        g.setFont(new Font("Courier", Font.BOLD, 12));
        for (int i = 0; (i + 1) * 10 - (scrollHeight / 10) - (scrollHeight) < bi.getHeight(); i++) {
            if((i*12)-scrollHeight / 10 < 0)
                continue;
            g.drawString(i + ": ", 0, (i + 1) * 12 - (scrollHeight / 10) - (scrollHeight));
            String text = linesEditing[i].toString();
            if(text.length() == 0)
                text = " ";
            for (int cha = 0; cha < text.length(); cha++) {
                if ((cha == textindex && i == textline) || (textindex > text.length()&&textline==i)) {
                    g.setColor(new Color(220, 222, 222));
                }
                char charText = text.charAt(cha);
                if((charText+"").equals(" ") && textline==i ){
                charText = '_';
                }
                g.drawString(charText + "", 50 + (TEXTSIZE * cha), (i + 1) * 12 - (scrollHeight / 10) - (scrollHeight));

                g.setColor(new Color(22, 192, 22));
            }
        }
        return bi;
    }

    @Override
    public void init(JFrame window) {
        window.addMouseWheelListener(this);
        window.addKeyListener(this);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getPreciseWheelRotation() > 0) {
            if (linesEditing.length * 10 + 100 > scrollHeight + e.getPreciseWheelRotation() * 5) {
                scrollHeight += e.getPreciseWheelRotation() * 5;
            }
        } else {
            if (0 < scrollHeight + (e.getPreciseWheelRotation() * 5)) {
                scrollHeight += e.getPreciseWheelRotation() * 5;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (textline > 0)
                textline--;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (textline < linesEditing.length - 1)
                textline++;
            if (textline < linesEditing.length) {
                //Gen New Line
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if(textindex > linesEditing[textline].length()-1)
                textindex = linesEditing[textline].length();
            if (textindex > 0)
                textindex--;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (textindex < linesEditing[textline].length() - 1)
                textindex++;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
