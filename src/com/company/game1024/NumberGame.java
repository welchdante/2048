package com.company.game1024;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

/**************************************************
 * Author: Dante Welch
 * Date: February 28, 2017
 * Project: 2048 Backend
 * Description: Backend for the popular game 2048
 *************************************************/
public class NumberGame implements NumberSlider{

    /*Stores the state of game board  */
    private int[][] gameBoard;
    /*Width of the game board */
    private int width;
    /*Height of the game board */
    private int height;
    /*Stores the value for winning the game */
    private int winningValue;
    /*Stores the score of the game */
    private int score;
    /*Tells if the game is over or still going */
    private GameStatus currentStatus;
    /*Tells if a tile has merged on a given slide */
    private boolean[][] hasMerged;
    /*Stack used to save board after slide */
    private Stack<int[][]> savedBoards;
    /*Stack to store scores */
    private Stack<Integer> savedScores;
    /*Stores location and value of a cell */
    private ArrayList<Cell> cells;
    /*Stores high score*/
    private int highScore;

    /**************************************************
    * Changes size and winning v2alue of the game
    * @param height
    * @param width
    * @param winningValue
    * ************************************************/

    public void resizeBoard (int height, int width, int winningValue){

        this.height = height;
        this.width = width;
        this.winningValue = winningValue;
        this.gameBoard = new int[width][height];
        this.hasMerged = new boolean[height][width];
        this.savedBoards = new Stack<>();
        this.savedScores = new Stack<>();
        this.score = 0;
        this.highScore = 0;

        /*Checks if winning value is power of 2 */
        if((winningValue > 0) && ((winningValue &
                (winningValue - 1)) == 0) == false ||
                winningValue < 0){
            throw new IllegalArgumentException();
        }

        if(height > 0 && width > 0){
            gameBoard = new int[width][height];
        }
        else{
            throw new IllegalArgumentException();
        }

    }

     /*************************************************
     * Resets the game
     *************************************************/

    public void reset(){

        this.gameBoard = new int[width][height];
        this.savedBoards.clear();
        this.savedScores.clear();
        this.score = 0;

        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                gameBoard[i][j] = 0;
                hasMerged[i][j] = false;
            }
        }

        this.currentStatus = GameStatus.IN_PROGRESS;
        placeRandomValue();
        placeRandomValue();
    }

    /*************************************************
    * Generates a rancom number
    * @param min minimum value in the range
    * @param max maximum value in the range
    * @return randomNum a random number in that range
    *************************************************/

    public int randomNumber(int min, int max){
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    /*************************************************
    * Generates a random power of two
    * @return value random power of two
    **************************************************/

    public int randomPowerOfTwo(){
        int randPower = randomNumber(1,10);
        int value = (int) Math.pow(2,randPower);
        return value;
    }

    /**************************************************
    * Places two random values on the board
    * @param blankBoard a blank board
    **************************************************/

    public void placeTwoRandoms(int[][] blankBoard){
        int randPow = randomPowerOfTwo();
        int randCol = randomNumber(0,width);
        int randRow = randomNumber(0,height);

        int randPow2 = randomPowerOfTwo();
        int randCol2 = randomNumber(0,width);
        int randRow2 = randomNumber(0,height);

        while(randCol == randCol2 && randRow == randRow2){
            randCol = randomNumber(0,width);
            randRow = randomNumber(0,height);

            randCol2 = randomNumber(0,width);
            randRow2 = randomNumber(0,height);
        }

        gameBoard[randCol][randRow] = randPow;
        gameBoard[randCol2][randRow2] = randPow2;
    }

    /**************************************************
    * Replaces the current board values
    * @param ref 2d array to set board valus to
    **************************************************/
    @Override
    public void setValues(final int[][] ref){
        width = ref.length;
        height = ref[0].length;

        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                gameBoard[i][j] = ref[i][j];
            }
        }
    }

    /**************************************************
    * Place a random value on the board
    * @return Cell where to place the random value
    **************************************************/

    public Cell placeRandomValue(){

        Random random = new Random();

        Cell randomCell = getEmptyTiles().get(random.nextInt(getEmptyTiles().size()));

        int randRow = randomCell.row;
        int randCol = randomCell.column;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (randRow == i && randCol == j) {
                    int val = (((random.nextInt(9) + 1) / 2) == 1 ? 2 : 4);
                    gameBoard[randRow][randCol] = val;

                    return new Cell(randRow, randCol, val);
                }
            }
        }
        return null;
    }

    /**************************************************
    * Slides the board in a given direction
    * @param dir number that indicates if firection is up or down
    * @return didSlide true if it slid, false otherwise
    **************************************************/

    public boolean slide (SlideDirection dir){

        boolean didSlide = false;

        if(savedBoards.size() == 0){
            saveBoard();
        }

        if(dir == SlideDirection.DOWN){
            if(moveVertically(1)){
                didSlide = true;
            }

        }

        else if(dir == SlideDirection.UP){
            if(moveVertically(-1)){
                didSlide = true;
            }
        }

        else if(dir == SlideDirection.RIGHT){

            transposeMatrix(gameBoard);

            if(moveVertically(1)){
                didSlide = true;
            }

            transposeMatrix(gameBoard);
        }

        else if(dir == SlideDirection.LEFT){

            transposeMatrix(gameBoard);

            if(moveVertically(-1)){
                didSlide = true;
            }

            transposeMatrix(gameBoard);
        }

        if(didSlide){
            placeRandomValue();
            saveBoard();
        }

        resetMerge();

        return didSlide;

    }

    /**************************************************
    * Moves each part of the board either up or down
    * @param dir indicates either up or down
    * @return didSlide true if slide, otherwise false
    **************************************************/

    public boolean moveVertically(int dir){
        /*i and j values to start at */
        int iStart;
        int jStart;

        boolean didSlide = false;

        /*Will start at bottom right if dir == 1, otherwise start top left */
        if(dir == 1){
            iStart = height - 1;
            jStart = width - 1;
        }
        else{
            iStart = 0;
            jStart = 0;
        }

        for (int i = iStart; dir == 1 ? i >= 0 : i < height; i-=dir) {
            for (int j = jStart; dir == 1 ? j >= 0 : j < width; j -= dir) {
                /*Checking if the tile is empty */
                if(gameBoard[i][j] != 0){

                    /*Reassign so manipulation is possible */
                    int y = i;
                    /*checks the tile either above or below it and makes
                     * sure it is able to be moved/merged */
                    while(dir == -1 ? y > 0 : y < height -1){
                        if(canMerge(y, j, y + dir, j)){
                            merge(y, j, y + dir, j);
                            didSlide = true;
                        }

                        if(canMove(y, j, y + dir, j)){
                            move(y, j, y + dir, j);
                            didSlide = true;
                        }

                        y += dir;
                    }
                }
            }
        }
        return didSlide;
    }

    /**************************************************
    * Reset the hasMerged variable for each element
    **************************************************/

    public void resetMerge(){
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                hasMerged[i][j] = false;
            }
        }
    }

    /**************************************************
    * Checks to see if any two given tiles are capable of merging
    * @param i1 i coordinate 1
    * @param j1 j coordinate 1
    * @param i2 i coordinate 2
    * @param j2 j coordinate 2
    * @return sameValue tells if the two tiles are the same
    * @return didTilesMerge tells if the tile has already merged
    * @return notZero tells if the tiles are non-zero values
    **************************************************/

    public boolean canMerge(int i1, int j1,
                            int i2, int j2){

        boolean notZero = gameBoard[i1][j1] != 0 &&
                          gameBoard[i2][j2] != 0;

        boolean sameValue = gameBoard[i1][j1] ==
                            gameBoard[i2][j2];

        boolean didTilesMerge = hasMerged[i1][j1] ||
                                hasMerged[i2][j2];

        return sameValue && !didTilesMerge && notZero;

    }

    /**************************************************
    * Merges the two tiles together
    * @param i1 i coordinate 1
    * @param j1 j coordinate 1
    * @param i2 i coordinate 2
    * @param j2 j coordinate 2
    **************************************************/

    private void merge(int i1, int j1,
                       int i2, int j2){

        gameBoard[i2][j2] = gameBoard[i1][j1] * 2;
        gameBoard[i1][j1] = 0;

        hasMerged[i2][j2] = true;
        if(gameBoard[i2][j2] > score){
            score = gameBoard[i2][j2];
        }

        if(score > highScore){
            this.highScore = score;
        }
    }

    /**************************************************
    * Checks to see if the tile can move to the tile next to it
    * @param i1 i coordinate 1
    * @param j1 j coordinate 1
    * @param i2 i coordinate 2
    * @param j2 j coordinate 2
    * @return gameBoard one tile must be non-zero and the other must be zero
     *********************************************** */

    private boolean canMove(int i1, int j1,
                            int i2, int j2){

        return gameBoard[i1][j1] !=
                0 && gameBoard[i2][j2] == 0;
    }

    /**************************************************
    * Moves one tile into a zero tile
    * @param i1 i coordinate 1
    * @param j1 j coordinate 1
    * @param i2 i coordinate 2
    * @param j2 j coordinate 2
    **************************************************/

    private void move(int i1, int j1,
                      int i2, int j2){

        gameBoard[i2][j2] = gameBoard[i1][j2];
        gameBoard[i1][j1] = 0;
    }

    /**************************************************
    * Transposes a matrix
    * @param gameBoard current game board
    * @return gameBoard transosed game board
    **************************************************/

    public int[][] transposeMatrix(int[][] gameBoard) {

        int[][] temp = new int[gameBoard[0].length][gameBoard.length];

            for (int i = 0; i < gameBoard.length; i++) {
                for (int j = 0; j < gameBoard[0].length; j++) {
                    temp[j][i] = gameBoard[i][j];
                }
            }

            for (int i = 0; i < gameBoard.length; i++) {
                for (int j = 0; j < gameBoard[0].length; j++) {
                    gameBoard[i][j] = temp[i][j];
                }
            }

        return gameBoard;
        }

    /**************************************************
    * Overridden method that checks for the non-empty tiles in the gameboard
    * @return temp ArrayList of type cell of tiles that re not empty
    **************************************************/

    @Override
    public ArrayList<Cell> getNonEmptyTiles(){

        ArrayList<Cell> temp = new ArrayList<>();

        for(int i=0; i<height; i++){
            for(int j=0; j<width;j++){
                if(gameBoard[i][j] != 0){
                    temp.add(new Cell(i,j, gameBoard[i][j]));
                }
            }
        }
        return temp;
    }

    /**************************************************
    * Method that checks for the empty tiles
    * @return temp ArrayList of type Cell of tiles that are empty
    **************************************************/

    public ArrayList<Cell> getEmptyTiles(){

        ArrayList<Cell> temp = new ArrayList<>();

        for(int i=0; i<height; i++){
            for(int j=0; j<width;j++){
                if(gameBoard[i][j] == 0){
                    temp.add(new Cell(i,j, gameBoard[i][j]));
                }
            }
        }
        return temp;
    }

    /**************************************************
    * Finds the current status of the game
    * @return currentStatus status of the game
    **************************************************/

    public GameStatus getStatus() {

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (gameBoard[i][j] == winningValue) {
                    currentStatus = GameStatus.USER_WON;
                    return currentStatus;
                }
            }
        }

        if (getNonEmptyTiles().size() == width * height) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (i > 0) {

                        if (canMerge(i, j, i - 1, j)) {
                            currentStatus = GameStatus.IN_PROGRESS;
                            return currentStatus;
                        }

                        if (i < height - 1) {
                            if (canMerge(i, j, i + 1, j)) {
                                currentStatus = GameStatus.IN_PROGRESS;
                                return currentStatus;
                            }
                        }
                    }
                    if (j > 0) {

                        if (canMerge(i, j, i, j - 1)) {
                            currentStatus = GameStatus.IN_PROGRESS;
                            return currentStatus;
                        }

                        if (j < width - 1) {

                            if (canMerge(i, j, i, j + 1)) {
                                currentStatus = GameStatus.IN_PROGRESS;
                                return currentStatus;
                            }
                        }
                    }
                }
            }
            currentStatus = GameStatus.USER_LOST;
            return currentStatus;
        }
        else{
            currentStatus = GameStatus.IN_PROGRESS;
            return currentStatus;
        }
    }

    /**************************************************
    * Saves the current board into a stack
    **************************************************/

    private void saveBoard() {

        int[][] temp = new int[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                temp[i][j] = gameBoard[i][j];
            }
        }

        /* Pushing the board and score to respective Stacks. */
        savedBoards.push(temp);
        savedScores.push(score);
    }

    /**************************************************
    * Method that will undo the last player move
     * ***********************************************/

    public void undo(){

        if(savedBoards.size() > 1){

            savedBoards.pop();
            savedScores.pop();

            int[][] previousBoard = savedBoards.pop();

            int previousScore = savedScores.pop();

            setValues(previousBoard);
            score = previousScore;

        }
        else{
            throw new IllegalStateException();
        }
    }

    public int getScore(){
        return score;
    }

    public int getHighScore(){
        return highScore;
    }

    public void setWinningValue(int winningValue){
        this.winningValue = winningValue;
        getStatus();
    }

    public void saveHighScore(){
        PrintWriter out = null;

        try{
            out = new PrintWriter(new BufferedWriter(
                    new FileWriter("highscore.txt")));
            out.println(this.highScore);
            out.close();
        } catch(IOException e){

        }
    }
}
