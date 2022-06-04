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
	
	// number of cards, rankPoint, totalPoint;
	private int Bcard = 0;
	private int Pcard = 0;
	private int Hcard = 0;
	private int Scard = 0;
	private int rankPoint = 0;
	private int totalPoint = 0;
	
	Player(String name){
		this.name = name;
	}
	
	void move(int py, int px) {
		pos_y += py;
		pos_x += px;
	}
}