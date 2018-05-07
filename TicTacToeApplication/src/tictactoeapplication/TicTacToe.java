/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoeapplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TicTacToe {
    // Gameplay
    // INIT:
    //   - | - | -
    // ------------
    //   - | - | -
    // ------------
    //   - | - | -

    //game variables
    protected char[] board;
    protected char userMarker;
    protected char aiMarker;
    protected char winner;
    protected char currentMarker;
    
    public TicTacToe(char playerToken, char aiToken){
        this.userMarker = playerToken;
        this.aiMarker = aiToken;
        this.winner = '-';
        this.board = setBoard();
        this.currentMarker = userMarker;
    }
    
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~AI FUNCTIONS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    ////////////////////////////////////////////////////////////////////////////
    
    //constructor for cloning game (used for recursion/depth first search)
    public TicTacToe(TicTacToe another) {
        this.userMarker = another.userMarker;
        this.aiMarker = another.aiMarker;
        this.winner = another.winner;
        this.board = another.board.clone(); // you can access  
        this.currentMarker = another.currentMarker;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~AI FUNCTIONS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    
    
    //initializes board to all dashes
    public char[] setBoard() {
        char[] board = new char[9];
        for (int i = 0; i < 9; i++){
            board[i] = '-';
        }
        return board;
    }
    
    //called to place a marker appropriately for each player's turn
    public boolean playTurn(int spot){
        boolean isValid = withinRange(spot) && !isSpotTaken(spot);
        if (isValid){
            this.board[spot-1] = currentMarker;
            currentMarker = (currentMarker == userMarker)?aiMarker:userMarker;
        }
        return isValid;
    }
    
    
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~AI FUNCTIONS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
    ////////////////////////////////////////////////////////////////////////////
    
    /*
    DFS functions to explore breadth
    both essentially perform the same function:
        tryTurn places marker for AI
        tryOpponentTurn places marker for user
    The two separate function names are for making the code easier to read
    */
    
    //try placing spot with current marker and return opposite marker
    /*
    public char tryTurn(int spot, char currMarker){
        boolean isValid = withinRange(spot) && !isSpotTaken(spot);
        if (isValid){
            this.board[spot-1] = currMarker;
        }
        return (currMarker = (currMarker == userMarker)?aiMarker:userMarker);
    }*/
    
    public boolean tryAITurn(int spot){
        boolean isValid = withinRange(spot) && !isSpotTaken(spot);
        if (isValid){
            this.board[spot-1] = aiMarker;
        }
        return isValid;
    }
    
    public boolean tryOpponentTurn(int spot){
        boolean isValid = withinRange(spot) && !isSpotTaken(spot);
        if (isValid){
            this.board[spot-1] = userMarker;
        }
        return isValid;
    }
    
    /*check rows/cols/diagonals for if opponent is about to win. 
        return relevant position to block, or return 10 if not true */
    public int check(){
        //We create a list of Maps, which map the indices to their board values
        ArrayList<Map> winList = new ArrayList<>();
        Map<Integer,Character> r1 = new HashMap<>();
        r1.put(0, board[0]);
        r1.put(1, board[1]);
        r1.put(2, board[2]);        
        
        Map<Integer,Character> r2 = new HashMap<>();
        r2.put(3, board[3]);
        r2.put(4, board[4]);
        r2.put(5, board[5]);
        
        Map<Integer,Character> r3 = new HashMap<>();
        r3.put(6, board[6]);
        r3.put(7, board[7]);
        r3.put(8, board[8]);
        
        Map<Integer,Character> c1 = new HashMap<>();
        c1.put(0, board[0]);
        c1.put(3, board[3]);
        c1.put(6, board[6]);
        
        Map<Integer,Character> c2 = new HashMap<>();
        c2.put(1, board[1]);
        c2.put(4, board[4]);
        c2.put(7, board[7]);
        
        Map<Integer,Character> c3 = new HashMap<>();
        c3.put(2, board[2]);
        c3.put(5, board[5]);
        c3.put(8, board[8]);
        
        Map<Integer,Character> d1 = new HashMap<>();
        d1.put(0, board[0]);
        d1.put(4, board[4]);
        d1.put(8, board[8]);
        
        Map<Integer,Character> d2 = new HashMap<>();
        d2.put(2, board[2]);
        d2.put(4, board[4]);
        d2.put(6, board[6]);
        
        winList.add(r1);
        winList.add(r2);
        winList.add(r3);
        winList.add(c1);
        winList.add(c2);
        winList.add(c3);
        winList.add(d1);
        winList.add(d2);
        
        //iterate through possible wins for AI to snatch a win
        for (int i = 0; i < winList.size(); i++){
            //Arraylist to hold opponent markers in a specified row/col/diag
            ArrayList<Integer> AIMarkers = new ArrayList<>(); 
            Map<Integer,Character> tempMap = winList.get(i);
            
            //iterate through keys in each map (row/col/diag)
            for (Integer key : tempMap.keySet()){
                if (tempMap.get(key)==this.aiMarker ) {
                    AIMarkers.add(key);
                }
                //if AI is about to win
                if (AIMarkers.size() == 2){
                   for (Integer k : tempMap.keySet()){
                        //find the empty space left in that row/col/diag and return it
                        if (!AIMarkers.contains(k) && tempMap.get(k)=='-') {
                            return k+1; //remember +1 because indices are shifted by 1
                        }
                    } 
                }

            }
            
        }
        
        //iterate through possible wins for opponent to block
        for (int i = 0; i < winList.size(); i++){
            //Arraylist to hold opponent markers in a specified row/col/diag
            ArrayList<Integer> opponentMarkers = new ArrayList<>(); 
            Map<Integer,Character> tempMap = winList.get(i);
            //iterate through keys in each map (row/col/diag)
            for (Integer key : tempMap.keySet()){
                if (tempMap.get(key)==this.userMarker ) {
                    opponentMarkers.add(key);
                }
                
                //if opponent is about to win (2 in a row/col/diag)
                if (opponentMarkers.size() == 2){
                    for (Integer k : tempMap.keySet()){
                        //find the empty space left in that row/col/diag and return it
                        if (!opponentMarkers.contains(k) && tempMap.get(k)=='-') {
                            return k+1; //remember +1 because indices are shifted by 1
                        }
                    }
                }
            }            
        }      
        return 10; //no action required
    }
    
    ////////////////////////////////////////////////////////////////////////////
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~AI FUNCTIONS~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//
   
    //called by playTurn to check if spot is valid
    public boolean withinRange(int spot){
        return (spot>0 && spot<board.length + 1);
    }
    
    //called by playTurn to check if spot is valid
    public boolean isSpotTaken(int spot){
        return (board[spot-1] != '-');
    }
    
    //print out the current board
    public void printBoard(){
        System.out.println();
        boolean newLine = true;
        for (int i = 0; i < board.length; i++){
            if (i%3 == 0 && i != 0){
                System.out.println();
                System.out.println(" ---------");
                newLine = true;
            }
            if (newLine){
                System.out.print(" " + board[i]);
                newLine = false;
            }
            else {
                System.out.print(" | " + board[i]);
            }
        }
        System.out.println();
    }
    
    //print current board with indices to instruct player how to select spots
    public static void printIndexBoard(){
        System.out.println();
        boolean newLine = true;
        for (int i = 0; i < 9; i++){
            if (i%3 == 0 && i != 0){
                System.out.println();
                System.out.println(" ---------");
                newLine = true;
            }
            if (newLine){
                System.out.print(" " + (i+1));
                newLine = false;
            }
            else {
                System.out.print(" | " + (i+1));
            }
        }
        System.out.println();
    }
    
    //called to check if there is a winner and if so, declare the winner
    public boolean isThereAWinner() {
        boolean diagonalsAndMiddles = (rightDi() || leftDi() || middleRow() || secondCol()) && this.board[4] != '-';
        boolean topAndFirst = (topRow() || firstCol()) && this.board[0] != '-';
        boolean bottomAndThird = (bottomRow() || thirdCol()) && this.board[8] != '-';
        if (diagonalsAndMiddles){
            this.winner = this.board[4];
        } else if (topAndFirst){
            this.winner = this.board[0];
        } else if (bottomAndThird) {
            this.winner = this.board[8];     
        }
        return diagonalsAndMiddles || topAndFirst || bottomAndThird;
    }
    
    //subsequent functions are for determining winner
    public boolean topRow() {
        return board[0] == board[1] && board[1] == board[2];
    }
    
    public boolean middleRow(){
        return board[3] == board[4] && board[4] == board[5];
    }
    
    public boolean bottomRow(){
        return board[6] == board[7] && board[7] == board[8];
    }
    
    public boolean firstCol() {
        return board[0] == board[3] && board[3] == board[6];
    }
    
    public boolean secondCol() {
        return board[1] == board[4] && board[4] == board[7];
    }
    
    public boolean thirdCol() {
        return board[2] == board[5] && board[5] == board[8];
    }
    
    public boolean rightDi() {
        return board[0] == board[4] && board[4] == board[8];
    }
    
    public boolean leftDi() {
        return board[2] == board[4] && board[4] == board[6];
    }
    
    //function to determine draws
    public boolean isBoardFilled() {
        for (int i = 0; i < board.length; i++){
            if (board[i] == '-'){ 
                return false;
            }
        }
        return true;
    }
    
    //check if the game is over and return the outcome
    public String gameOver() {
        if (isThereAWinner()) {
            return "We have a winner! The winner is " + this.winner + "'s";
        } else if (isBoardFilled()) {
            return "draw";
        } else {
            return "notOver";
        }
    }
}
