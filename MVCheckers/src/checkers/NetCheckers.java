package checkers;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

public class NetCheckers extends JFrame implements MouseListener {

	private CheckerboardCanvas cbCanvas; // the 'view' of the checkerboard
	private CheckerBoard model;			// The model - which is the checker board

	private int canvasTopInset;			// distance Canvas is placed from the top

	private int fromRow, fromCol;		// Where the checker is moving from

	private int currentPlayer;	

	public NetCheckers() {

		super("NetCheckers");
		setSize(450,450);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);        

		model = new CheckerBoard();			// Create the model

		cbCanvas = new CheckerboardCanvas();
		add(cbCanvas);					// Add the view to this frame

		model.addObserver(cbCanvas);		// Associate the viewer with the model
		model.newGame();					// Display the view of the model

		addMouseListener(this);				// Have this program listen for mouse events

		currentPlayer=Square.PlayerOne;
		setTitle("Player One's Turn");

		setVisible(true);
		canvasTopInset = getInsets().top;

	}

	/**
	Gets the pixel location of the mouse click and turns it into the
	row and column in the grid of the checker location.
	@param e the mouse event that just occurred
	 */
	public void mousePressed(MouseEvent e)
	{
		fromRow = cbCanvas.getRow(e.getY()-canvasTopInset);
		fromCol = cbCanvas.getCol(e.getX());

		System.err.println("Mouse down: " + fromRow + "-" + fromCol);
	}

	/**

	Attempts to move piece from mouse-press location to mouse-release location. If the move is valid, 
	advances the game.

	@param e the mouse event that just occurred
	 */

	public void mouseReleased(MouseEvent e) {

		int toRow = cbCanvas.getRow(e.getY()-canvasTopInset); // adjust coordinates to account for the checkerboard's location
		int toCol = cbCanvas.getCol(e.getX());

		if (model.canMoveFrom(currentPlayer,fromRow,fromCol)) {
			System.err.println("Trying to move.");

			if (model.move(fromRow, fromCol, toRow, toCol)) {		// if the move was successful 
				currentPlayer = (currentPlayer == Square.PlayerOne) ? Square.PlayerTwo : Square.PlayerOne;

				if (model.gameOver()) {
					setTitle("Game over");
				} else {
					if (currentPlayer == Square.PlayerOne)
						setTitle("Player One's move");
					else
						setTitle("Player Two's move");
				}
			}
		}
	}


	// not used
	public void mouseDragged(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {System.out.print("");}


	public static void main(String[] args) {

		EventQueue.invokeLater(
				new Runnable() {
					@Override
					public void run() {
						NetCheckers game = new NetCheckers();

					}
				});
	}
}