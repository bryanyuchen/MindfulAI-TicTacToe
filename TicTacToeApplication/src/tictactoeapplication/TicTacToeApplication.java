/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoeapplication;

import java.util.Scanner;

/**
 *
 * @author bchen
 */
public class TicTacToeApplication {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        boolean test = false; //for testing AI vs bot
        Scanner sc = new Scanner(System.in);
        
        //for testing
        int aiWins = 0;
        int draws = 0;
        int n = 1000;
        
        
        //game variables
        int itr = (test)?n:1; //if testing, set game iterations to n, otherwise 1
        char playerToken;
        char opponentToken;
        
        

        while (itr-->0){
            
            //if not a test, include prompt for player
            if (!test){
                System.out.println("Welcome to TicTacToe \nEnter a character for yourself");
                playerToken = sc.next().charAt(0);
                System.out.println("Enter a character for your opponent");
                opponentToken = sc.next().charAt(0);      
            }
            
            //if is a test, don't worry about prompt and scanner input
            else {
                playerToken = 'x';
                opponentToken = 'o';
            }
            
            //game init
            TicTacToe game = new TicTacToe(playerToken, opponentToken);
            if(!test){
                System.out.println("\n Enter a number to place your token on the designated spot");
                TicTacToe.printIndexBoard();
                System.out.println();
            }

            while (game.gameOver().equals("notOver")){
                /*user turn (although who starts first is determined by 
                  assignment of userMarker in the constructor*/
                if (game.currentMarker == game.userMarker){
                    int spot=0; //some initialization
                    
                    //if not testing, ask user for spot
                    if (!test){
                        System.out.println("your turn - pick a spot!");
                        try {spot = sc.nextInt();}
                        catch (java.util.InputMismatchException e){ sc.nextLine();}
                    }
                    
                    //if testing, ignore scanner prompt
                    else if (test){
                        spot = bot.pickSpot(game);
                    }
                    
                    while (!game.playTurn(spot)) {
                        System.out.println("try another spot please");
                        try {spot = sc.nextInt();}
                        catch (java.util.InputMismatchException e){ sc.nextLine();}
                    }
                    
                    if (!test) {System.out.println("You picked " + spot);}
                    
                //AI turn
                } else {
                    int aiSpot = AI.pickSpot(game);
                    game.playTurn(aiSpot);
                    if (!test) {
                        System.out.println("I picked " + aiSpot);
                    }
                }
                if (!test){
                    game.printBoard();
                }
            }
            if (!test){
                System.out.println(game.gameOver());
                System.out.println();
            }
            
            else if (test) {
                if (game.winner == game.aiMarker){
                    aiWins++;
                }
                if (game.winner == game.userMarker){
                    game.printBoard();
                    for (int i = 0; i < 100; i++){
                        System.out.println();
                    }
                }
                if ("draw".equals(game.gameOver())) {
                    draws++;
                }
            }
        }
        
        //testing printout of results
        if (test) {
            System.out.println("---AI RESULTS---");
            System.out.println("Wins: " + aiWins);
            System.out.println("Draws: " + draws);
            System.out.println("Losses: " + (n-aiWins-draws));
        }
    }
    
}
