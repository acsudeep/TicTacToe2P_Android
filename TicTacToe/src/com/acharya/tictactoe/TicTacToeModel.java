package com.acharya.tictactoe;

public class TicTacToeModel {
	public enum Piece {X, O, _ };
	private final int GRID_WIDTH = 3;
	private final int GRID_HEIGHT = 3;
	
	private Piece[][] board = new Piece[GRID_WIDTH][GRID_HEIGHT];
	private Piece currentPlayer = Piece.X;
	
	public TicTacToeModel() {
		reset();
	}
	
	public void reset() {
		for(int i=0; i< GRID_WIDTH; i++)
			for(int j = 0; j < GRID_HEIGHT; j++)
				board[i][j] = Piece._;
	}
	
	private void togglePlayer() {
		if(currentPlayer == Piece.X)
			currentPlayer = Piece.O;
		else
			currentPlayer = Piece.X;
	}
	
	public Piece getCurrentPlayer() {
		return currentPlayer;
	}
	
	public void setValue(int row, int col, Piece p) {
		board[row][col] = p;
		togglePlayer();
	}
	
	public Piece getValue(int row, int col) {
		return board[row][col];
	}
	
	public Piece checkWinner() {
		Piece winner = Piece._;
		
		// Check for Rows that win
		for(int i=0; i<GRID_HEIGHT; i++)
			if(winner == Piece._ && board[i][0] == board[i][1] && board[i][1] == board[i][2])
				winner = board[i][0];
		
//		System.out.println("1" + winner);
		
		// Check for Columns that win
		for(int i=0; i<GRID_WIDTH; i++)
			if(winner == Piece._ && board[0][i] == board[1][i] && board[1][i] == board[2][i])
				winner = board[0][i]; 
		
//		System.out.println("2" + winner);
		
		// Check for cross that wins
		if(winner == Piece._ && board[0][0] == board[1][1] && board[1][1] == board[2][2])
			winner = board[0][0];
		
//		System.out.println("3" + winner);
		
		if(winner == Piece._ && board[0][2] == board[1][1] && board[1][1] == board[2][0])
			winner = board[0][2]; 
		
//		System.out.println("4" + winner);
		
		return winner;
	
	}
}
