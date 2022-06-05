import java.util.ArrayList;
import java.util.Observable;

@SuppressWarnings("deprecation")
public class Model_PlayerInfo extends Observable {
	
	// player 정보
	private int numPlayers;
	private Player[] players;
	private ArrayList<Player> winner;
	
	Model_PlayerInfo(int numPlayers) {
		// player 수 만큼 생성
		this.numPlayers = numPlayers;
		players = new Player[numPlayers];
		for (int i = 0 ; i<numPlayers; i++) {
			players[i] = new Player("P"+String.valueOf(i+1), this);
		}
	}
	
	Player[] getPlayers() {
		return players;
	}
	
	int getNumPlayers() {
		return numPlayers;
	}
	
	void calcaulateWinner() {
		winner = new ArrayList<Player>();
		int maxPoint = -1;
		Player p;
		for (int i = 0; i<numPlayers; i++) {
			p = players[i];
			if (p.getTotalPoint() > maxPoint)
				maxPoint = p.getTotalPoint();
		}
		for (int i = 0; i<numPlayers; i++) {
			p = players[i];
			if (p.getTotalPoint() == maxPoint)
				winner.add(p);
		}
	}

	public String getWinnerString(){
		String lst = "";
		for (int i = 0; i < winner.size(); i++) {
			lst = lst + winner.get(i).getName() + " ";
		}
		return lst;
	}

	
	void notifyPlayerInfoChanged() {
		setChanged();
		notifyObservers();
	}
}

class Player {
	// to notify observer
	private Model_PlayerInfo model;
	
	private String name;
	private int pos_y;
	private int pos_x;
	
	// number of cards, rankPoint, totalPoint;
	private int Bcard = 0;
	private int Pcard = 0;
	private int Hcard = 0;
	private int Scard = 0;
	private int rank = 0;
	private int totalPoint = 0;
	
	Player(String name, Model_PlayerInfo model){
		this.name = name;
		this.model = model;
	}
	
	void setInitialPosition(int iy, int ix) {
		pos_y = iy;
		pos_x = ix;
	}
	
	// roll or rest result : move or ++/-- card
	
	void move(int py, int px) {
		pos_y = py;
		pos_x = px;
		model.notifyPlayerInfoChanged();
	}
	
	void calculateTotalPoints() {
		totalPoint += Pcard;
		totalPoint += Hcard*2;
		totalPoint += Scard*3;
		System.out.println(rank + "my rank ");
		int rankPoint = 0;
		switch (rank) {
			case 1: {
				rankPoint = 7;
				break;
			}
			case 2: {
				rankPoint = 3;
				break;
			}
			case 3: {
				rankPoint = 1;
				break;
			}
			case 0: {
				// goal 못한 경우
				rankPoint = 0;
				break;
			}
		}
		totalPoint += rankPoint;
	}
	
	// setters;
	void increaseBcard() {
		Bcard++;
	}
	void decreaseBcard() {
		Bcard--;
	}
	void increasePcard() {
		Pcard++;
	}
	void increaseHcard() {
		Hcard++;
	}
	void increaseScard() {
		Scard++;
	}
	void setRank(int rank) {
		this.rank = rank;
	}
	void setTotalPoint(int totalPoint) {
		this.totalPoint = totalPoint;
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

	public int getRank() {
		return rank;
	}

	public int getTotalPoint() {
		return totalPoint;
	}
	
	
}