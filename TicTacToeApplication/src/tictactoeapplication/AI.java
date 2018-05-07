/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoeapplication;

import java.util.ArrayList;

/**
 *
 * @author bchen
 */
public class AI {
    
    //MDP value function
    public static float value(TicTacToe tempGame){
        //if AI won, return positive reward (1)
        if (tempGame.isThereAWinner()&& tempGame.winner == tempGame.aiMarker) {
            return 1;
        }
        //if draw or return no reward
        if (tempGame.isBoardFilled()){
            return 0; //fail
        }
        //if lost, return negative reward (I'm not sure if this actually works in MDP)
        if (tempGame.isThereAWinner() && tempGame.winner == tempGame.userMarker){
            return -1; //lose
        }
        
        //find all possible successive states
        ArrayList<Integer> choices = new ArrayList<>();
        for (int i = 0; i < 9; i++){
            if (tempGame.board[i] == '-'){
                choices.add(i+1);
            }
        }
        
        float val = 0f; //init value at 0 (we add to this later)
        
        // get value of possible successive states
        for (int i = 0; i < choices.size(); i++){ 
            // create alternate game where opponent picks one of the open spots at random
            TicTacToe newTempGame = new TicTacToe(tempGame);
            newTempGame.tryOpponentTurn(choices.get(i)); 

            /*
             for each alternate game, create another alternate game where
                AI picks one of the remaining spots at random */
            for (int j = i+1; j < choices.size(); j++){
                TicTacToe newTempGameTwo = new TicTacToe(newTempGame);
                newTempGameTwo.tryAITurn(choices.get(j));
                // increase value based on normalized value of alternate searches
                val+=value(newTempGameTwo);
            }
            
            // else if board filled, call value one last time to get the value
            if (choices.size() == 1){ //if only one turn still need a return value
                val+=value(newTempGame);
            }
        }
        return val/choices.size(); // normalize the value based on number of choices
    }
    
    public static int pickSpot(TicTacToe game){
        
        // find available spots
        ArrayList<Integer> choices = new ArrayList<>();
        for (int i = 0; i < 9; i++){
            if (game.board[i] == '-'){
                choices.add(i+1);
            }
        }
        
        /*
        if AI is about to win - place winning marker, 
        or if the opposite is true, block opponent
        check() will return 10 if no final action is necessary
        */
        if (game.check() < 10){
            return game.check();
        }
        
        //init max value at some extreme minimum
        float maxValue = -999f;
        //max choice means choice corresponding to max value
        int maxChoice = choices.get(0);
        
        /*
        Markov Decision Process Policy:
        for each available spots, compare values of each alternate game 
          based on each available spot to determine which spot to take for next turn 
        */
        for (int i = 0; i < choices.size(); i++){
            TicTacToe tempGame = new TicTacToe(game);
            int aiSpot = choices.get(i);
            tempGame.tryAITurn(aiSpot);
            float value = value(tempGame);
            if (value > maxValue){
                maxValue = value;
                maxChoice = choices.get(i);
            }
        }

        return maxChoice;
    }
}
