package checkers;

/**
 *
 * This is the Model - CheckerBoard.java

 * The model (CheckerBoard), the view (CheckerboardCanvas, and the controller
 * (NetCheckers) are in separate files.  When the model changes, the view is
 * notified and updated automatically.
 */

import java.util.Observable;

public class CheckerBoard extends Observable { 

	private Square board[][];						// grid representing the board
	static final public int BOARD_SIZE = 8;			// this is fixed and public; part of Checkers


	/**
	 *	Creates a checkerboard with all the pieces in their starting places.
	 */
	
	public CheckerBoard() {
		board = new Square[BOARD_SIZE][BOARD_SIZE];

		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				board[row][col] = new Square();	
			}
		}
		setPlayers();
	}

	public void newGame() {
		clearBoard();
		setPlayers();

		setChanged();
		notifyObservers();	
	}

	/**
	 * Sets all the board locations to empty
	 */
	private void clearBoard() {
		for (int r = 0; r < BOARD_SIZE; r++)
			for (int c = 0; c < BOARD_SIZE; c++)
				board[r][c].clear();
	}

	/**
	 *	Places the check board pieces in their starting locations
	 */
	private void setPlayers() {
		int startingPos;

		// Top player pieces in first three rows
		for (int row = 0; row < 3; row++) {		
			startingPos = (row % 2 == 0) ? 1 : 0; // 'even' rows have checkers in the odd squares, and vice-versa

			for (int col = startingPos; col < BOARD_SIZE; col = col + 2) {	// every other square
				board[row][col].setPlayer(Square.PlayerOne); 
			}
		}

		// Bottom player pieces in bottom three rows; same idea as above
		for (int row = BOARD_SIZE - 3; row < BOARD_SIZE; row++) {
			startingPos = (row % 2 == 0) ? 1 : 0; 

			for (int col = startingPos; col < BOARD_SIZE; col = col + 2) {
				board[row][col].setPlayer(Square.PlayerTwo); 
			}
		}
	}

	/**
	 * Is a grid position valid?
	 * @param row row location of checkerboard
	 *@param ccol column location of checkerboard
	 * @return returns true if row & col fall within the dimensions of the checkerboard
	 */
	
	public boolean validLocation(int row, int col) {
		if (row < 0 || row >= BOARD_SIZE || 
				col < 0 || col >= BOARD_SIZE) {
			return false; 
		} else {
			return true;
		}
	}

	/**
	 * Black squares can be found in
	 *	Even Row & Odd Column
	 *	Odd Row & Even Column
	 * @param r row location of checkerboard
	 * @param c column location of checkerboard
	 * @return returns true if location (r,c) is a black square on the checkerboard
	 */
	
	public boolean squareIsBlack(int r, int c) {
		if (!validLocation(r, c))
			return false;
		if (r % 2 == 0)
			return (c % 2 != 0);
		return (c % 2 == 0);
	}

	/**
	 * @param r row location of checkerboard
	 * @param c column location of checkerboard
	 * @return returns true is there is a checker piece on the grid location (r,c)
	 *        otherwise returns false
	 */
	public boolean squareIsOccupied(int r, int c) {
		if (!validLocation(r,c))
			return false;
		return !(board[r][c].isEmpty());
	}

	/**
	 * Are two positions occupied by different players?
	@param myRow row location of checkerboard
	@param myCol column location of checkerboard
	@param oppRow row location of checkerboard
	@param oppCol column location of checkerboard
	@precondition: Both positions are valid (within grid and on black)
	@return Returns true if positions occupy different players
	 */
	public boolean isOpponent(int myRow, int myCol, int oppRow, int oppCol) {
		if (!squareIsOccupied(myRow, myCol) || !squareIsOccupied(oppRow, oppCol))
			return false;

		return (board[myRow][myCol].getPlayer() != board[oppRow][oppCol].getPlayer());
	}

	/**
	returns true if the location is occupied by a King
	@param r row location of checkerboard
	@param c column location of checkerboard
	@return returns true if location is occupied by a King
	 */
	public boolean squareHoldsKing(int r, int c)
	{
		if (!validLocation(r,c) || !squareIsBlack(r,c) || !squareIsOccupied(r,c))
			return false;
		return (board[r][c].isKing());
	}


	/**
	 *	Checking to see if (fromRow,fromCol) to (toRwo,toCol)
	 *	is a valid non-jumping move.
	@param fromRow row location of checkerboard
	@param fromCol column location of checkerboard
	@param toRow row location of checkerboard
	@param toCol column location of checkerboard
	Precondition:  Current location is valid
	@return returns true if it is a valid move
	 */
	public boolean moveIsValid(int fromRow, int fromCol, int toRow, int toCol)
	{
		// Must move within the board
		if (!validLocation(toRow, toCol))
			return false;

		// Must move to a black square that is not occupied
		if (!squareIsBlack(toRow, toCol) && !squareIsOccupied(toRow, toCol))
			return false;
		
		// The column distance must be one
		if (Math.abs(fromCol - toCol) != 1)
			return false;


		/* If not a King must move 'forward' one row:
		 * Player one goes from the top to the bottom
		 * Player two goes from the bottom to the top
		 */

		if (!squareHoldsKing(fromRow,fromCol)) {
			if (board[fromRow][fromCol].getPlayer() == Square.PlayerOne) {	// PLAYER_ONE moves 'down' the board			
				if (fromRow + 1 != toRow)
					return false;
			}
			else if (board[fromRow][fromCol].getPlayer() == Square.PlayerTwo) {	// PLAYER_ONE moves 'up' the board
				if (fromRow - 1 != toRow)
					return false;
			}
		}
		else { // Kings can move 'forward' or 'backwards' one row
			if (Math.abs(fromRow - toRow) != 1)
				return false;
		}

		return true;
	}

	/**
	 *	Checking to see if (fromRow,fromCol) to (toRwo,toCol)
	 *	is a valid jump move.
	@param fromRow row location of checkerboard
	@param fromCol column location of checkerboard
	@param toRow row location of checkerboard
	@param toCol column location of checkerboard
	Precondition:  Current location is valid
	@return returns true if it is a valid jump move
	 */
	public boolean jumpIsValid(int fromRow, int fromCol, int toRow, int toCol)
	{
		// Must move within the board
		if (!validLocation(toRow, toCol))
			return false;

		// Must move to a black square that is not occupied
		if (!squareIsBlack(toRow, toCol) || squareIsOccupied(toRow, toCol))
			return false;

		// It not a King must move forward
		// Player one goes from the top to the bottom
		// Player two goes from the bottom to the top
		// Check moving two spaces forward
		if (!squareHoldsKing(fromRow, fromCol)) {

			if (board[fromRow][fromCol].getPlayer() == Square.PlayerOne) {
				if (fromRow + 2 != toRow) {	// Must move exactly two rows forward on a jump (down board)
					return false;
				}
				if (isOpponent(fromRow, fromCol, fromRow+1, (fromCol+toCol)/2)) { // Make sure there is an opponent in-between
					return true;
				}
				return false;
			}
			else { // Must be PlayerTwo
				
				if (fromRow - 2 != toRow)	// Must move exactly two rows forward on a jump (up board)
					return false;

				if (isOpponent(fromRow, fromCol, fromRow-1, (fromCol+toCol)/2)) // Make sure there is an opponent  in-between
					return true;
				return false;
			}
		}
		else	// Check for valid King move
		{

			if (fromRow - 2 != toRow && fromRow + 2 != toRow)	// Must move exactly two rows either forward or backwards
				return false;

			if (isOpponent(fromRow, fromCol, (fromRow+toRow)/2, (fromCol+toCol)/2)) // Make sure there is an opponent in between
				return true;
			return false;
		}
	}

	/**
	Attempt to move a checker piece
	@param fromRow row location of checkerboard
	@param fromCol column location of checkerboard
	@param toRow row location of checkerboard
	@param toCol column location of checkerboard
	@return returns true if a move has been made
	 */
	public boolean move(int fromRow, int fromCol, int toRow, int toCol) {

		boolean jumped = jumpIsValid(fromRow, fromCol, toRow, toCol);

		if (jumped || moveIsValid(fromRow, fromCol, toRow, toCol)) { // if this is a legal move, then execute it
			
			Square from = board[fromRow][fromCol];

			board[toRow][toCol].setPlayer(from.getPlayer()); // 'Copy' the player to its new position
			if (from.isKing()) {
				board[toRow][toCol].makeKing(); // this is kludgy
			}


			if (from.getPlayer() == Square.PlayerOne && toRow == BOARD_SIZE-1) {// Check to see if the player is now a king
				board[toRow][toCol].makeKing();
				
			}
			else if (from.getPlayer() == Square.PlayerTwo && toRow == 0) {
				board[toRow][toCol].makeKing();
				
			}
			// Remove the jumped player
			if (jumped)
				board[(fromRow+toRow)/2][(fromCol+toCol)/2].clear();

			board[fromRow][fromCol].clear(); // Remove player from old location

			setChanged();
			notifyObservers();

			return true;
		} else {
			return false;
		}

	}


	/**
	 *	Determines who is at location (r,c)
	@param r is the row in the grid to check
	@param c is column in the gird to check
	@return returns true if "player one" is at location [r][c],
	        otherwise returns false
	 */
	public boolean isPlayerOne(int row, int col)
	{
		if (!validLocation(row,col))
			return false;
		return (board[row][col].getPlayer() == Square.PlayerOne);

	}

	/**
	 *	Determines if there is a winner.
	Count the number of pieces left for each player.  If there are not any
	pieces left for a player, they lose, and the other player is the winner.
	@return returns true if there is a winner - all of one color's pieces are gone
	 */
	public boolean gameOver()
	{
		int playerOneScore = 0, playerTwoScore = 0;
		for (int r = 0; r < BOARD_SIZE; r++)
			for (int c = 0; c < BOARD_SIZE; c++)
				if (board[r][c].getPlayer() == Square.PlayerOne)
					playerOneScore++;
				else if (board[r][c].getPlayer() == Square.PlayerTwo)
					playerTwoScore++;
		return ((playerOneScore == 0) || (playerTwoScore == 0));
	}

	// DEBUG method
	public String toString()
	{
		String b = "";
		for (int r = 0; r < BOARD_SIZE; r++)
		{
			for (int c = 0; c < BOARD_SIZE-1; c++)
				b += board[r][c] + "\t";
			b += board [r][BOARD_SIZE-1] + "\n";
		}
		return b;
	}

	public boolean canMoveFrom(int currentPlayer, int fromRow, int fromCol) {
		return (board[fromRow][fromCol].getPlayer() == currentPlayer);

	}
}
