/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoeapplication;

import java.util.ArrayList;
import java.util.Random;

//"Bot" is basically an unintelligent AI that returns a random spot
public class bot {
    public static int pickSpot(TicTacToe game){
        
        //create list of available spots
        ArrayList<Integer> choices = new ArrayList<>();
        for (int i = 0; i < 9; i++){
            if (game.board[i] == '-'){
                choices.add(i+1);
            }
        }
        
        //return a random spot from list of available spots
        Random rand = new Random();
        return choices.get(Math.abs(rand.nextInt() % choices.size()));
    }
}