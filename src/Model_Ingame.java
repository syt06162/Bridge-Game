import java.util.ArrayList;
import java.util.Observable;

@SuppressWarnings("deprecation")
public class Model_Ingame extends Observable {
	// ���� ����
	private Model_Map model_Map;
	private String[][] map;
	private int start_y, start_x;
	private int end_y, end_x;
	
	// �÷��̾� ����
	private Model_PlayerInfo model_PlayerInfo;
	private Player[] players;
	private int numPlayers;
		
	// ������ ����
	private String[] gameNotice;
	
	// ���� ����: ��������, ���� ����, ���� ���°� RR���� IM����
	private ArrayList<Player> turnList;
	private ArrayList<Player> goalInList;
	private int RRorIM; // RR:0, IM:1; (roll rest �ؾ��ϴ��� / input move �ؾ��ϴ��� ����)
	private Boolean canGoBack;
	
	// �̹��� �ֻ��� ����, �̵�����ĭ�� ����
	private int rollResult;
	private int canMove;
	
	// observerUpdateFlag : 0:notice, 1:roll, 2:rest, 3:move, 4:turn end
	private int observerUpdateFlag;

	Model_Ingame(Model_Map model_Map , Model_PlayerInfo model_PlayerInfo) {
		// model ���� ���
		this.model_Map = model_Map;
		this.model_PlayerInfo = model_PlayerInfo;
		map = model_Map.getMap();
		players = model_PlayerInfo.getPlayers();
		numPlayers = model_PlayerInfo.getNumPlayers();
		start_y = model_Map.getStartY();
		start_x = model_Map.getStartX();
		end_y = model_Map.getEndY();
		end_x = model_Map.getEndX();
		
		// �÷��̾� ��ġ ����
		for (int i = 0; i<numPlayers; i++) {
			movePlayer(i, start_y, start_x);
		}
		
		// turn list �ʱ�ȭ, RR mode, cangoback
		turnList = new ArrayList<Player>();
		for (int i = 0; i<numPlayers; i++) {
			turnList.add(players[i]);
		}
		goalInList = new ArrayList<Player>();
		RRorIM = 0;
		canGoBack = true;
		
		// ���� ������
		gameNotice = new String[3];
		gameNotice[0] = gameNotice[1] = gameNotice[2] = "";
		newNotice("==Game Start==");
		Player now = turnList.get(0);
		newNotice(now, "Roll or Rest?");
				
	}
	
	// roll, rest, inputmove
	void roll() {
		Player nowPlayer = turnList.get(0);
		
		if (RRorIM == 1) {
			// ������ Roll �̳� Rest ���ʰ� �ƴ϶�, input move �� ����
			newNotice(nowPlayer, "you should 'input move'");
			return;
		}
			
		rollResult = (int)(Math.random()*6 + 1);
		canMove = rollResult - nowPlayer.getBcard();
		if (canMove<0) {
			canMove = 0;
			newNotice(nowPlayer, "you cannot move. too much Bridge card");
			turnEnd();
		}
		else {
			// �̵��� �� �����Ƿ� input move ���� ����
			newNotice(nowPlayer, "you can move. please input move");
			RRorIM = 1;
		}
		
		observerUpdateFlag = 1;
		setChanged();
		notifyObservers();
	}
	
	void rest() {
		Player nowPlayer = turnList.get(0);
		if (RRorIM == 1) {
			// ������ Roll �̳� Rest ���ʰ� �ƴ϶�, input move �� ����
			newNotice(nowPlayer, "you should 'input move'");
			return;
		}
		
		if (nowPlayer.getBcard() > 0)
			nowPlayer.decreaseBcard();
		
		turnEnd();
		
		// observer
		observerUpdateFlag = 2;
		setChanged();
		notifyObservers();
	}
	
	void inputMove(String input) {
		Player nowPlayer = turnList.get(0);
		if (RRorIM == 0) {
			newNotice(nowPlayer, "you should 'Roll or Rest'");
			return;
		}
		if (input.length() != canMove) {
			newNotice(nowPlayer, "you must input " + String.valueOf(canMove) + " letters");
			return;
		}
		
		// ���� input�� �̵� ������ �Է����� Ȯ�� ����
		// input, player ���� ��ǥ ���
		input = input.toUpperCase();
		int cy = nowPlayer.getPos_y();
		int cx = nowPlayer.getPos_x();
		Boolean flag = true;
		
		// cy,cx: �÷��̾� �ʱ� ��ǥ
		// ty,tx: ��ĭ��ĭ Ȯ���� ��ǥ
		int ty = cy, tx = cx;
		char moveDirection;
		for (int i = 0; i<canMove; i++) {
			moveDirection = input.charAt(i);
			if (canGoBack && (map[ty][tx].charAt(2) != moveDirection) && (map[ty][tx].charAt(4) != moveDirection)) {
				flag = false;
				break;
			}
			else if ((!canGoBack) && (map[ty][tx].charAt(4) != moveDirection)) {
				flag = false;
				break;
			}
			
			// �̹� ���� ������ �� ����
			switch (moveDirection) {
				case 'U': {
					ty -= 1;
					break;
				}
				case 'D': {
					ty += 1;
					break;
				}
				case 'L': {
					tx -= 1;
					break;
				}
				case 'R': {
					tx += 1;
					break;
				}
			}
		}
		
		// flag �� true �� �� �� �ִ°�,-> move!
		// flag �� false�� �� �� ���°� -> ����� return
		if (flag == false) {
			newNotice(nowPlayer, "your input is invalid. input correctly");
			return;
		}
		else {
			nowPlayer.move(ty, tx);
			RRorIM = 0;
			newNotice(nowPlayer, "move success!");
			turnEnd();
		}
		
	}
	
	void turnEnd() {
		Player p = turnList.remove(0);
		turnList.add(p);
		Player now = turnList.get(0);
		newNotice(now, "Roll or Rest?");
		
		// observer
		observerUpdateFlag = 4;
		setChanged();
		notifyObservers();
	}
	
	void turnEndGoalIn() {
		Player p = turnList.remove(0);
	}
	
	
	void movePlayer(int playerNum, int y, int x) {
		players[playerNum].move(y, x);
		
		observerUpdateFlag = 3;
		setChanged();
		notifyObservers();
	}
	
	void newNotice(String str) {
		gameNotice[0] = gameNotice[1];
		gameNotice[1] = gameNotice[2];
		gameNotice[2] = str;
		
		observerUpdateFlag = 0;
		setChanged();
		notifyObservers();
	}
	
	void newNotice(Player p, String str) {
		gameNotice[0] = gameNotice[1];
		gameNotice[1] = gameNotice[2];
		gameNotice[2] = p.getName() + ": " + str;
		
		observerUpdateFlag = 0;
		setChanged();
		notifyObservers();
	}
	
	// getter
	public String[] getGameNotice() {
		return gameNotice;
	}
	
	public int getRollResult() {
		return rollResult;
	}

	public int getCanMove() {
		return canMove;
	}
	
	public int getObserverUpdateFlag() {
		return observerUpdateFlag;
	}

	// roll, rest �ϴ� �޼ҵ� - �ݵ�� private
	// move
	
}