package com.company.game1024.GUI;

import com.company.game1024.NumberGame;

import javax.swing.*;
import javax.swing.event.MenuListener;
import java.awt.*;

/**
 * Created by dante on 3/11/17.
 */

/*
 * Constructor that initializes variables
 *
 */
public class GameFrame extends JFrame{

    private JPanel panelMain;

    private int height, width;

    private JFrame frame;

    private NumberGame game;

    private JMenuBar menuBar;

    private JMenu gameMenu;

    private MenuListener menuListener;

    private JMenuItem gameReset, gameChangeWinVal, gameResize;

    public GameFrame(){
        width = 800;
        height = 600;
        frame = new JFrame();

        menuBar = new JMenuBar();
        gameMenu = new JMenu("Game");
        gameReset = new JMenuItem("Reset");
        gameResize = new JMenuItem("Resize");
        gameChangeWinVal = new JMenuItem("Change Winning Value");

        game = new NumberGame();

        //menuListener = new MenuListener();
        panelMain = new GamePanel(4, 4, game);
    }

    public void initialize(){
        frame.setLayout(new BorderLayout());
        frame.add(panelMain, BorderLayout.CENTER);
        frame.setTitle("1024");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(width, height));
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        //gameReset.addActionListener(menuListener);
        //gameResize.addActionListener(menuListener);
        //gameChangeWinVal.addActionListener(menuListener);
    }

    public static void main(String[] args){
        GameFrame GUI = new GameFrame();
        GUI.initialize();

    }
}




