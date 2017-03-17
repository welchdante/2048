package com.company.game1024.Project_2_Starter_Files;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.TreeMap;

/*****************************************************************
 NumberTile JLabel class. Originally when initialized, this class
 would throw a NullPointerException. I have edited it (comments
 where edits occur) so that it works properly.
 @author Hans Dulimarta
 @version 1.0
 *****************************************************************/
public class NumberTile extends JLabel implements ActionListener {
    private static Map<String,Color> bgColorMap;
    private Timer tick;
    private Color current, target;
    private int dRed, dGreen, dBlue, step;
    private static final int NSTEPS = 6;

    static {
        bgColorMap = new TreeMap<String,Color>();
        bgColorMap.put("1", new Color(0xD8, 0xE2, 0xDC));
        bgColorMap.put("2", new Color(0xFF, 0xE5, 0xD9));
        bgColorMap.put("4", new Color(0xFF, 0xCA, 0xD4));
        bgColorMap.put("8", new Color(0xFF, 176, 172));
        bgColorMap.put("16", new Color(0xFF, 193, 137));
        bgColorMap.put("32", new Color(194, 123, 75));
        bgColorMap.put("64", new Color(209, 60, 20));
        bgColorMap.put("128", new Color(209, 176, 26));
        bgColorMap.put("256", new Color(198, 209, 22));
        bgColorMap.put("512", new Color(161, 209, 93));
        bgColorMap.put("1024", new Color(131, 203, 209));

    }
    public NumberTile(String text, int horizontalAlignment) {
        super(text, horizontalAlignment);

        /** Originally there were variable initializations here,
         but they all threw a NPE. I've removed them and placed
         them elsewhere. */
        setOpaque(true);
    }

    private void animateColorTo (Color next) {

        /** Here I check if the background is null, and set it to a
         default color, then initialize the timer properly. */
        if (getBackground() == null) {
            setBackground(Color.LIGHT_GRAY);
            tick = new Timer(50, this);
        }

        /** Simply enough, the following code now runs successfully. */
        target = next;
        step = 0;
        current = getBackground();
        dRed = (next.getRed() - current.getRed()) / NSTEPS;
        dGreen = (next.getGreen() - current.getGreen()) / NSTEPS;
        dBlue = (next.getBlue() - current.getBlue()) / NSTEPS;
        tick.start();
    }

    @Override
    public void setText(String text) {
        super.setText(text);
        if (text.length() == 0)
            animateColorTo(Color.lightGray);
        Color c = bgColorMap.get(text);
        if (c != null)
            animateColorTo(c);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (step < NSTEPS) {
            step++;
            setBackground(new Color(
                    current.getRed() + step * dRed,
                    current.getGreen() + step * dGreen,
                    current.getBlue() + step * dBlue));
        }
        else {
            tick.stop();
            setBackground(target);
        }
    }
}
