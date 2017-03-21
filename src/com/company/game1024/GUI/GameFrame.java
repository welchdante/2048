package com.company.game1024.GUI;

import com.company.game1024.NumberGame;

import javax.swing.*;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by dante on 3/11/17.
 */

public class GameFrame extends JFrame{

    /*Instance variable for the game panel*/
    private GamePanel panelMain;
    /*Instance variables for heithe and width*/
    private int height, width;
    /*Instance variable for the JFrame*/
    private JFrame frame;
    /*Instance variable for the instance of the game*/
    private NumberGame game;
    /*Instance variable for the menu bar*/
    private JMenuBar menuBar;
    /*Instance variable for the menu*/
    private JMenu gameMenu;
    /*Instance variable for the menu listener*/
    private MenuListener menuListener;
    /*Instance variables for the buttons in the menu*/
    private JMenuItem gameReset, gameChangeWinVal, gameResize;

    /*
     * Constructor that initializes variables
     */

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

        menuListener = new MenuListener();
        panelMain = new GamePanel(4, 4, game);
    }

    /*
     * Method that initializes the game
     */

    public void initialize(){

        gameReset.addActionListener(menuListener);
        gameResize.addActionListener(menuListener);
        gameChangeWinVal.addActionListener(menuListener);

        menuBar.add(gameMenu);
        gameMenu.add(gameReset);
        gameMenu.add(gameResize);
        gameMenu.add(gameChangeWinVal);

        frame.setLayout(new BorderLayout());
        frame.add(panelMain, BorderLayout.CENTER);
        frame.setJMenuBar(menuBar);
        frame.setTitle("1024");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(width, height));
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /*
     * Main method
     */

    public static void main(String[] args){
        GameFrame GUI = new GameFrame();
        GUI.initialize();
    }

    /*
     * Private menulistener class
     */
    private class MenuListener implements ActionListener{

        /*
         * Overridden method that watches for the menu to be pressed
         */
        @Override
        public void actionPerformed(ActionEvent e){
            if(e.getSource() == gameReset){
                game.setWinningValue(1024);
                panelMain.reset();
            }
            else if(e.getSource() == gameResize){
                try {
                    int width = Integer.parseInt(JOptionPane.showInputDialog(
                            null, "Enter width of new board:",
                            "1024", JOptionPane.INFORMATION_MESSAGE));
                    int height = Integer.parseInt(JOptionPane.showInputDialog(
                            null, "Enter height of new board:",
                            "1024", JOptionPane.INFORMATION_MESSAGE));

                    frame.remove(panelMain);
                    panelMain = new GamePanel(width, height, game);
                    frame.add(panelMain);
                    frame.revalidate();
                    frame.repaint();
                } catch(NumberFormatException f){
                    JOptionPane.showMessageDialog(null,
                            "Error making a new board. Enter an integer.",
                            "1024", JOptionPane.OK_OPTION);
                }
            }
            else if(e.getSource() == gameChangeWinVal){
                try{
                    int newVal = Integer.parseInt(JOptionPane.showInputDialog(
                            null, "Enter new winning value " +
                                    "("+ "Power of 2)",
                            "1024", JOptionPane.INFORMATION_MESSAGE));
                    game.setWinningValue(newVal);
                    panelMain.checkStatus();
                } catch(NumberFormatException f){
                    JOptionPane.showMessageDialog(null,
                            "Error making new board. Enter an integer.",
                                     "1024", JOptionPane.OK_OPTION);
                }
            }
        }
    }
}




