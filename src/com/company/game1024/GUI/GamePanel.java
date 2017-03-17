package com.company.game1024.GUI;

import com.company.game1024.Cell;
import com.company.game1024.GameStatus;
import com.company.game1024.NumberGame;
import com.company.game1024.Project_2_Starter_Files.NumberTile;
import com.company.game1024.SlideDirection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 * Created by dante on 3/11/17.
 */

public class GamePanel extends JPanel implements KeyListener{

    private int width, height, size, x, y;
    private NumberGame game;
    //private KeyboardListener keyListener;
    private ButtonListener btnListener;
    private JPanel buttonPanel, buttonContainer, rightInfoPanel, leftInfoPanel;
    private JButton slideUp, slideDown, slideRight, slideLeft;
    private JButton undo;
    private NumberTile[][] tiles;
    private JLabel score;



    public GamePanel(int width, int height, NumberGame game){
        this.width = width;
        this.height = height;
        this.game = game;
        this.size = 96;

        /*Null layout to manually position our elements */
        setLayout(null);
        resizeBoard(width, height);

        //keyListener = new KeyboardListener();
        buttonPanel = new JPanel();
        buttonContainer = new JPanel();
        rightInfoPanel = new JPanel();
        leftInfoPanel = new JPanel();
        buttonPanel = new JPanel();
        btnListener = new ButtonListener();

        slideUp = new JButton("Slide Up");
        slideDown = new JButton("Slide Down");
        slideLeft = new JButton("Slide Left");
        slideRight = new JButton("Slide Right");
        undo = new JButton("Undo");

        slideLeft.addActionListener(btnListener);
        slideUp.addActionListener(btnListener);
        slideDown.addActionListener(btnListener);
        slideRight.addActionListener(btnListener);
        undo.addActionListener(btnListener);

        addKeyListener(this);

        setLayout(new BorderLayout());
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(slideLeft, BorderLayout.WEST);
        buttonPanel.add(slideUp, BorderLayout.NORTH);
        buttonPanel.add(slideDown, BorderLayout.SOUTH);
        buttonPanel.add(slideRight, BorderLayout.EAST);
        buttonPanel.add(undo, BorderLayout.CENTER);
        buttonPanel.setBackground(Color.BLACK);
        buttonContainer.setBackground(Color.WHITE);


        buttonContainer.add(buttonPanel, BorderLayout.SOUTH);

        add(buttonContainer);
        buttonContainer.addKeyListener(this);

        rightInfoPanel.setLayout(new BorderLayout());
        renderBoard();
        setFocusable(true);

    }

    /*Overridden method that listens for a key pressed */

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println("Hello");
    }

    /*Overridden method that determines which key is pressed
     * and then makes some action happen */

    @Override
    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_UP){
            game.slide(SlideDirection.UP);
            renderBoard();
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            game.slide(SlideDirection.DOWN);
            renderBoard();
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            game.slide(SlideDirection.LEFT);
            renderBoard();
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            game.slide(SlideDirection.RIGHT);
            renderBoard();
        }
        else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            try {
                game.undo();
                renderBoard();
            } catch(IllegalStateException ise) {
                JOptionPane.showMessageDialog(
                        null, "Can't undo that far!",
                        "1024", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    /* Overridden method that finished the key press */
    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("Hello");
    }

    public void renderBoard(){
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                tiles[i][j].setText("");
            }
        }

        for(Cell cell : game.getNonEmptyTiles()){
            if(!tiles[cell.row][cell.column].getText().contains
                    (cell.value + "")){
                tiles[cell.row][cell.column].setText(cell.value + "");
            }
        }

        //score.setText("" + game.getScore());
        //update score
        checkStatus();
    }

    private void resizeBoard(int width, int height){
        this.height = height;
        this.width = width;

        int boardWidth = (size*width) + (width*5);
        int boardHeight = (size*height) + (height*5);

        while(boardWidth > 414 || boardHeight > 414){
            size--;
            boardWidth = (size*width) + (width*5);
            boardHeight = (size*height) + (height*5);
        }

        x = (800/2) - ((size*width)/2)-150;
        y = (600/2) - ((size*height)/2) +10;
        
        tiles = new NumberTile[height][width];

        game.resizeBoard(height, width, 1024);
        game.placeRandomValue();
        game.placeRandomValue();

        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){

                tiles[i][j] = new NumberTile("", JLabel.CENTER);
                tiles[i][j].setFont(new Font("Helvetica", Font.PLAIN, 32));

                tiles[i][j].setBounds(new Rectangle(
                        (x - ((j*5)/2)) + (size*j) + (j*5),
                        y + (size*i) + (i*5), size, size));
                add(tiles[i][j]);
            }
        }

        setBackground(Color.WHITE);
    }

    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e){
            if(e.getSource() == slideUp){
                System.out.println("Hello");
                game.slide(SlideDirection.UP);
                renderBoard();
            }
            else if (e.getSource() == slideDown) {
                game.slide(SlideDirection.DOWN);
                renderBoard();
            }
            else if (e.getSource() == slideLeft) {
                game.slide(SlideDirection.LEFT);
                renderBoard();
            }
            else if (e.getSource() == slideRight) {
                game.slide(SlideDirection.RIGHT);
                renderBoard();
            }
            else if (e.getSource() == undo) {
                try {
                    game.undo();
                    renderBoard();
                } catch(IllegalStateException ise) {
                    JOptionPane.showMessageDialog(
                            null, "Can't undo that far!",
                            "1024", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }

    public void checkStatus(){
        if(game.getStatus() != GameStatus.IN_PROGRESS){
            String screenMessage = "";

            if(game.getStatus() == GameStatus.USER_WON){
                screenMessage = "You won!";
            }

            if(game.getStatus() == GameStatus.USER_LOST){
                screenMessage = "You lost";
            }
        }
    }

}
