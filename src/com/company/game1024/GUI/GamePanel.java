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
    /*Instance variables for width, height, size, x, y*/
    private int width, height, size, x, y;
    /*Instance variables for wins, losses, time left*/
    private int wins, losses, timeLeft;
    /*Instance variable for the number game*/
    private NumberGame game;
    /*Instance variable for the button listener*/
    private ButtonListener btnListener;
    /*Instance variables for the button panel, the container, and info panels*/
    private JPanel buttonPanel, buttonContainer, rightInfoPanel, leftInfoPanel;
    /*Instance variables for slide buttons*/
    private JButton slideUp, slideDown, slideRight, slideLeft;
    /*Instance variable for undo*/
    private JButton undo;
    /*Instance variable for the tiles*/
    private NumberTile[][] tiles;
    /*Instance variables for the JLabels*/
    private JLabel score, scoreText, timerLabel, highScore,
            highScoreText, winsText, lossesText;
    /*Instance variable for the timer listener*/
    private TimerListener timerListener;
    /*Instance variable for the timer*/
    private Timer timer;

    /*
     * Constructor that instantiates our variables
     * @param width width of the game board
     * @param height height of the game board
     * @param game state of the game
     */
    public GamePanel(int width, int height, NumberGame game){

        this.width = width;
        this.height = height;
        this.game = game;
        this.size = 96;
        this.wins = 0;
        this.losses = 0;
        this.timeLeft = 200;

        /* Null layout to manually position our elements */
        setLayout(null);
        resizeBoard(width, height);

        /* Initialize all of the variables */
        buttonPanel = new JPanel();
        buttonContainer = new JPanel();
        rightInfoPanel = new JPanel();
        leftInfoPanel = new JPanel();
        buttonPanel = new JPanel();
        btnListener = new ButtonListener();
        score = new JLabel();
        scoreText = new JLabel();
        timerLabel = new JLabel();
        highScore = new JLabel();
        highScoreText = new JLabel();
        winsText = new JLabel();
        lossesText = new JLabel();
        timerListener = new TimerListener();

        /* Create a new timer*/
        timer = new Timer(1000, timerListener);

        /* Initializing the buttons */
        slideUp = new JButton("Slide Up");
        slideDown = new JButton("Slide Down");
        slideLeft = new JButton("Slide Left");
        slideRight = new JButton("Slide Right");
        undo = new JButton("Undo");

        /* Adding action listeners */
        slideLeft.addActionListener(btnListener);
        slideUp.addActionListener(btnListener);
        slideDown.addActionListener(btnListener);
        slideRight.addActionListener(btnListener);
        undo.addActionListener(btnListener);

        /* Add key listener to our game*/
        addKeyListener(this);

        /* Add the buttons to the button panel */
        setLayout(new BorderLayout());
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(slideLeft, BorderLayout.WEST);
        buttonPanel.add(slideUp, BorderLayout.NORTH);
        buttonPanel.add(slideDown, BorderLayout.SOUTH);
        buttonPanel.add(slideRight, BorderLayout.EAST);
        buttonPanel.add(undo, BorderLayout.CENTER);
        buttonPanel.setBackground(Color.GRAY);
        buttonContainer.setBackground(Color.LIGHT_GRAY);

        /* Add the left and right info panels*/
        rightInfoPanel.setLayout(new BorderLayout());
        rightInfoPanel.add(score, BorderLayout.CENTER);
        rightInfoPanel.add(scoreText, BorderLayout.NORTH);
        rightInfoPanel.add(lossesText, BorderLayout.SOUTH);
        rightInfoPanel.setBackground(Color.LIGHT_GRAY);
        leftInfoPanel.setLayout(new BorderLayout());
        leftInfoPanel.add(highScore, BorderLayout.CENTER);
        leftInfoPanel.add(highScoreText, BorderLayout.NORTH);
        leftInfoPanel.add(winsText, BorderLayout.SOUTH);
        leftInfoPanel.setBackground(Color.LIGHT_GRAY);

        /* Setting the layout */
        buttonContainer.add(buttonPanel, BorderLayout.NORTH);
        add(buttonContainer, BorderLayout.SOUTH);
        add(rightInfoPanel, BorderLayout.EAST);
        add(leftInfoPanel, BorderLayout.WEST);
        add(timerLabel, BorderLayout.NORTH);

        /* Add a key listener to the button container*/
        buttonContainer.addKeyListener(this);

        /* Add the scores*/
        score.setForeground(Color.WHITE);
        score.setFont (new Font("Clear Sans" , Font.BOLD, 24));
        score.setVerticalAlignment(SwingConstants.CENTER);
        score.setHorizontalAlignment(SwingConstants.CENTER);
        scoreText.setForeground(Color.WHITE);
        scoreText.setFont(new Font("Clear Sans", Font.BOLD, 18));
        scoreText.setText("    SCORE    ");
        scoreText.setHorizontalAlignment(SwingConstants.CENTER);
        scoreText.setVerticalAlignment(SwingConstants.BOTTOM);
        highScore.setForeground(Color.WHITE);
        highScore.setFont (new Font("Clear Sans" , Font.BOLD, 24));
        highScore.setVerticalAlignment(SwingConstants.CENTER);
        highScore.setHorizontalAlignment(SwingConstants.CENTER);
        highScoreText.setForeground(Color.WHITE);
        highScoreText.setFont(new Font("Clear Sans", Font.BOLD, 18));
        highScoreText.setText("    BEST    ");
        highScoreText.setHorizontalAlignment(SwingConstants.CENTER);
        highScoreText.setVerticalAlignment(SwingConstants.BOTTOM);

        /* Wins and losses stats*/
        winsText.setForeground(Color.WHITE);
        winsText.setFont(new Font("Clear Sans", Font.BOLD, 18));
        winsText.setText("    WINS    ");
        winsText.setHorizontalAlignment(SwingConstants.CENTER);
        winsText.setVerticalAlignment(SwingConstants.CENTER);
        lossesText.setForeground(Color.WHITE);
        lossesText.setFont(new Font("Clear Sans", Font.BOLD, 18));
        lossesText.setText("    LOSSES    ");
        lossesText.setHorizontalAlignment(SwingConstants.CENTER);
        lossesText.setVerticalAlignment(SwingConstants.CENTER);

        /* Add the timer*/
        timerLabel.setForeground(Color.GRAY);
        timerLabel.setFont(new Font("Clear Sane", Font.BOLD, 24));
        timerLabel.setText("Time Left: " + timeLeft);
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timerLabel.setVerticalAlignment(SwingConstants.CENTER);

        /* Create the board*/
        renderBoard();
        setBackground(Color.WHITE);
        setFocusable(true);
        requestFocusInWindow();
        timer.start();
    }

    /* Overridden method that listens for a key pressed
    * @param e the key pressed
    * */

    @Override
    public void keyTyped(KeyEvent e) {
    }

    /* Overridden method that determines which key is pressed
     * and then makes some action happen
     * @param e the key pressed
     * */

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

    /* Overridden method that finished the key press
     * @param e the key pressed
     * */

    @Override
    public void keyReleased(KeyEvent e) {
    }

    /*
     * Renders the board and changes the value to the screen
     */

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

        score.setText("" + game.getScore());
        highScore.setText("" +game.getHighScore());
        checkStatus();
    }

    /*
     * Resizes and repaints the board
     * @param width the width of the board
     * @param height the height of the board
     */

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

        x = (800/2) - ((size*width)/2) - 20;
        y = (600/2) - ((size*height)/2) - 70;

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

    /*
     * Private button listener class
     */

    private class ButtonListener implements ActionListener {

        /* Overridden method that looks for the button press
         * @param e the button pressed
         * */
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

    /*
     * Checks the status of the game and displays
     * a given message to the screen based on the situation
     */

    public void checkStatus(){
        if(game.getStatus() != GameStatus.IN_PROGRESS){
            String screenMessage = "";

            if(game.getStatus() == GameStatus.USER_WON){
                screenMessage = "You won! Play again?";
                wins++;
            }

            if(game.getStatus() == GameStatus.USER_LOST){
                screenMessage = "You lost. Play again?";
                losses++;
            }

            winsText.setText("Wins: " +wins);
            lossesText.setText("Losses: " +losses);

            int option = JOptionPane.showConfirmDialog(
                    this, screenMessage,
                    "1024", JOptionPane.YES_NO_OPTION);

            if(option == 1){
                game.saveHighScore();
                System.exit(0);
            }
            else{
                reset();
            }
        }
    }

    /*
     * Resets the game
     */

    public void reset(){
        game.reset();
        game.setWinningValue(1024);
        timeLeft = 200;
        renderBoard();
    }

    /*
     * Private timer class
     */

    private class TimerListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e){
            if(timeLeft <= 0 && game.getStatus() !=
                    GameStatus.IN_PROGRESS){
                timerLabel.setText("Time Remaining: " +timeLeft);

                int option = JOptionPane.showConfirmDialog(null,
                        "You lost. Play again?", "1024",
                        JOptionPane.YES_NO_OPTION);

                if(option == 1){
                    game.saveHighScore();
                    System.exit(0);
                }
                else{
                    reset();
                }
            }
            else{
                if(game.getStatus() == GameStatus.IN_PROGRESS){
                    timerLabel.setText("Time Left: " +timeLeft);
                    timeLeft--;
                }
            }
        }
    }

}
