package checkers;

public class Square {
	public static final int Empty = 0;
	public static final int PlayerOne = 1;
	public static final int PlayerTwo = 2;

	private boolean king = false;
	private int player = Empty;


	public Square() {
		clear();
	}

	public void setPlayer(int player) {
		this.player = player;
	}

	public int getPlayer() {
		return player;
	}

	public boolean isEmpty() {
		return (player == Empty);
	}

	public boolean isKing() {
		return king;
	}

	public void makeKing() {
		king = true;
	}

	public void clear() {
		player = Empty;
		king = false;
	}

	public String toString() {

		String str = "";
		switch (player) {
		case Empty: str +=  "Empty";
		break;
		case PlayerOne: str += "P1";
		break;
		case PlayerTwo: str += "P2";
		}
		if (king) {
			str += "-K";
		}
		return str;
	}
}
