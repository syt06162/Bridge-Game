import java.util.ArrayList;
import java.util.Observable;

@SuppressWarnings("deprecation")
public class Model_Ingame extends Observable {
	// 지도 정보
	private Model_Map model_Map;
	private int start_y, start_x;
	private int end_y, end_x;
	
	// 플레이어 정보
	private Model_PlayerInfo model_PlayerInfo;
	private Player[] players;
	private int numPlayers;
		
	// 글자판 정보
	private String[] gameNotice;
	
	// 게임 정보: 누구의턴, 골인 순서
	private ArrayList<Player> turnList;
	private ArrayList<Player> goalInList;
	
	Model_Ingame(Model_Map model_Map , Model_PlayerInfo model_PlayerInfo) {
		// model 끼리 통신
		this.model_Map = model_Map;
		this.model_PlayerInfo = model_PlayerInfo;
		players = model_PlayerInfo.getPlayers();
		numPlayers = model_PlayerInfo.getNumPlayers();
		start_y = model_Map.getStartY();
		start_x = model_Map.getStartX();
		end_y = model_Map.getEndY();
		end_x = model_Map.getEndX();
		
		// 게임 전광판
		gameNotice = new String[3];
		gameNotice[0] = gameNotice[1] = gameNotice[2] = "";
		newNotice("Game Start");
		
		// 플레이어 위치 지정
		for (int i = 0; i<numPlayers; i++) {
			movePlayer(i, start_y, start_x);
		}
		
		
	}
	
	void movePlayer(int playerNum, int y, int x) {
		players[playerNum].move(y, x);
		
		setChanged();
		notifyObservers();
	}
	
	void newNotice(String str) {
		gameNotice[0] = gameNotice[1];
		gameNotice[1] = gameNotice[2];
		gameNotice[2] = str;
		
		setChanged();
		notifyObservers();
	}
	
	String[] getGameNotice() {
		return gameNotice;
	}

	// roll, rest 하는 메소드 - 반드시 private
	// move
	
}