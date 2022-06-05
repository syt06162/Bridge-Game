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
	
	// observerUpdateFlag : 0:notice, 1:roll, 2:rest, 3:move, 4:turn end  -1: game end
	// notice observer �̸�: 0,1,4: ingame   / 2,3: playerInfo      /  4:map / -1:ingame
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
		if (turnList.size() == 0) {
			newNotice("game is already ended");
			return;
		}
		
		Player nowPlayer = turnList.get(0);
		
		if (RRorIM == 1) {
			// ������ Roll �̳� Rest ���ʰ� �ƴ϶�, input move �� ����
			newNotice(nowPlayer, "you should 'input move'");
			return;
		}
			
		rollResult = (int)(Math.random()*6 + 1);
		canMove = rollResult - nowPlayer.getBcard();
		if (canMove<=0) {
			canMove = 0;
			newNotice(nowPlayer, "you cannot move. ");
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
		if (turnList.size() == 0) {
			newNotice("game is already ended");
			return;
		}
		
		Player nowPlayer = turnList.get(0);
		if (RRorIM == 1) {
			// ������ Roll �̳� Rest ���ʰ� �ƴ϶�, input move �� ����
			newNotice(nowPlayer, "you should 'input move'");
			return;
		}
		
		if (nowPlayer.getBcard() > 0) {
			nowPlayer.decreaseBcard();
			newNotice(nowPlayer, "Bridge card - 1");
		}
		
		turnEnd();
		
		// observer
		observerUpdateFlag = 2;
		setChanged();
		notifyObservers();
	}
	
	void inputMove(String input) {
		if (turnList.size() == 0) {
			newNotice("game is already ended");
			return;
		}
		
		Player nowPlayer = turnList.get(0);
		
		if (RRorIM == 0) {
			newNotice(nowPlayer, "you should 'Roll or Rest'");
			return;
		}
		if (input.length() != canMove) {
			newNotice(nowPlayer, "you must input " + String.valueOf(canMove) + " letters");
			return;
		}
		
		// �� ���� input�� �̵� ������ �Է����� Ȯ�� ����
		// input, player ���� ��ǥ ���
		input = input.toUpperCase();
		int cy = nowPlayer.getPos_y();
		int cx = nowPlayer.getPos_x();
		Boolean flag = true;
		
		// cy,cx: �÷��̾� �ʱ� ��ǥ
		// ty,tx: ��ĭ��ĭ Ȯ���� ��ǥ
		int ty = cy, tx = cx;
		char moveDirection;
		Boolean isEnd = false;
		int bridgeCnt = 0;
		
		// �� ��ĭ ��ĭ Ȯ�� ����
		for (int i = 0; i<canMove; i++) {
			moveDirection = input.charAt(i);
			
			// �긴�� - �ǳʱ� 
			if ((map[ty][tx].charAt(0) == 'B') && (moveDirection == 'R')) {
				moveDirection = 'B';
			}
			// �긴�� - �ǵ��ư���
			else if ((canGoBack) && (map[ty][tx].charAt(0) == 'b') && (moveDirection == 'L')) {
				moveDirection = 'b';
			}
			// �������� ����
			else if (canGoBack && (map[ty][tx].charAt(2) != moveDirection) && (map[ty][tx].charAt(4) != moveDirection)) {
				flag = false;
				break;
			}
			// �ڵ��ư��� ���µ� �ڵ��ư����� �ϴ� ���
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
				case 'B': {
					bridgeCnt++;
					tx += 2;
					break;
				}
				case 'b': {
					bridgeCnt++;
					tx -= 2;
					break;
				}
			}
			
			// ���� ��ġ�� ���, ���� ���� �ùٸ� ��� end
			if (map[ty][tx].charAt(0) == 'E') {
				Boolean isValid = true;
				for (int j = i+1; j<canMove; j++) {
					char next = input.charAt(j);
					if ((next == 'R') || (next == 'L') || (next == 'U') || (next == 'D')) 
						continue; 
					else {
						isValid = false;
						break;
					}
				}
				
				if (isValid) {
					flag = true;
					isEnd = true;
					break;
				}
				else {
					flag = false;
					break;
				}
			}
		}
		
		// �� flag �� true �� �� �� �ִ°�,-> move!
		// �� flag �� false�� �� �� ���°� -> ����� return
		if (flag == false) {
			newNotice(nowPlayer, "your input is invalid. input correctly");
			return;
		}
		else {
			// �̵� , �ٸ� �ǳͼ���ŭ ī�� get
			nowPlayer.move(ty, tx);
			while (bridgeCnt > 0) {
				nowPlayer.increaseBcard();
				bridgeCnt--;
			}
			
			// ī�� ��� ��ġ�̸� ī�� ���
			char nowCellType = map[nowPlayer.getPos_y()][nowPlayer.getPos_x()].charAt(0);
			switch (nowCellType) {
				case 'P': {
					nowPlayer.increasePcard();
					newNotice(nowPlayer, "get 1 Pdriver !");
					break;
				}
				case 'H': {
					nowPlayer.increaseHcard();
					newNotice(nowPlayer, "get 1 Hammer !");
					break;
				}
				case 'S': {
					nowPlayer.increaseScard();
					newNotice(nowPlayer, "get 1 Saw !");
					break;
				}
				default : {
					break;
				}
			}
			
			RRorIM = 0;
			newNotice(nowPlayer, "move success!");
			
			// End ����������! ����!
			if (isEnd) {
				turnEndGoalIn();
			}
			else {
				turnEnd();
			}
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
		canGoBack = false;
		Player p = turnList.remove(0);
		goalInList.add(p);
		int rank = goalInList.size() ;
		p.setRank(rank);
		p.calculateTotalPoints();
		
		// observer
		observerUpdateFlag = 4;
		setChanged();
		notifyObservers();
		
		// ���� �������� Ȯ�� (1�� ������ ����)
		if (turnList.size() == 1) {
			endGame();
		}
		else {
			Player now = turnList.get(0);
			newNotice(now, "Roll or Rest?");
		}
	}
	
	
	void endGame() {
		Player p = turnList.remove(0);
		p.calculateTotalPoints();
		
		// observer
		observerUpdateFlag = 4;
		setChanged();
		notifyObservers();
		
		newNotice("==Game End==");
		model_PlayerInfo.calcaulateWinner();
		newNotice("winner : " + model_PlayerInfo.getWinnerString());
		
		// game end observer
		observerUpdateFlag = -1;
		setChanged();
		notifyObservers();
		
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