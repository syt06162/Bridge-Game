import java.util.ArrayList;
import java.util.Observable;

@SuppressWarnings("deprecation")
public class Model_Ingame extends Observable {
	// ���� ����
	private Model_Map model_Map;
	private int start_y, start_x;
	private int end_y, end_x;
	
	// �÷��̾� ����
	private Model_PlayerInfo model_PlayerInfo;
	private Player[] players;
	private int numPlayers;
		
	// ������ ����
	private String[] gameNotice;
	
	// ���� ����: ��������, ���� ����
	private ArrayList<Player> turnList;
	private ArrayList<Player> goalInList;
	
	Model_Ingame(Model_Map model_Map , Model_PlayerInfo model_PlayerInfo) {
		// model ���� ���
		this.model_Map = model_Map;
		this.model_PlayerInfo = model_PlayerInfo;
		players = model_PlayerInfo.getPlayers();
		numPlayers = model_PlayerInfo.getNumPlayers();
		start_y = model_Map.getStartY();
		start_x = model_Map.getStartX();
		end_y = model_Map.getEndY();
		end_x = model_Map.getEndX();
		
		// ���� ������
		gameNotice = new String[3];
		gameNotice[0] = gameNotice[1] = gameNotice[2] = "";
		newNotice("Game Start");
		
		// �÷��̾� ��ġ ����
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

	// roll, rest �ϴ� �޼ҵ� - �ݵ�� private
	// move
	
}