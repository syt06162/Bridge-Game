import java.util.ArrayList;
import java.util.Observable;

@SuppressWarnings("deprecation")
public class Model_PlayerInfo extends Observable {
	
	// player 정보
	private int numPlayers;
	private Player[] players;
	
	Model_PlayerInfo(int numPlayers) {
		// player 수 만큼 생성
		this.numPlayers = numPlayers;
		players = new Player[numPlayers];
		for (int i = 0 ; i<numPlayers; i++) {
			players[i] = new Player("P"+String.valueOf(i+1));
		}
	}
	
	Player[] getPlayers() {
		return players;
	}
	
	int getNumPlayers() {
		return numPlayers;
	}
}

class Player {

	private String name;
	private int pos_y;
	private int pos_x;
	private boolean isFinished;
	
	// number of cards, rankPoint, totalPoint;
	private int Bcard = 0;
	private int Pcard = 0;
	private int Hcard = 0;
	private int Scard = 0;
	private int rankPoint = 0;
	private int totalPoint = 0;
	
	Player(String name){
		this.name = name;
		isFinished = false;
	}
	
	void setInitialPosition(int iy, int ix) {
		pos_y = iy;
		pos_x = ix;
	}
	
	void move(int py, int px) {
		pos_y = py;
		pos_x = px;
		
	}
	
	// getters
	public String getName() {
		return name;
	}

	public int getPos_y() {
		return pos_y;
	}

	public int getPos_x() {
		return pos_x;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public int getBcard() {
		return Bcard;
	}

	public int getPcard() {
		return Pcard;
	}

	public int getHcard() {
		return Hcard;
	}

	public int getScard() {
		return Scard;
	}

	public int getRankPoint() {
		return rankPoint;
	}

	public int getTotalPoint() {
		return totalPoint;
	}
	
}