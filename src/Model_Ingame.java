import java.util.ArrayList;
import java.util.Observable;

@SuppressWarnings("deprecation")
public class Model_Ingame extends Observable {
	// 지도 정보
	private Model_Map model_Map;
	private String[][] map;
	private int start_y, start_x;
	private int end_y, end_x;
	
	// 플레이어 정보
	private Model_PlayerInfo model_PlayerInfo;
	private Player[] players;
	private int numPlayers;
		
	// 글자판 정보
	private String[] gameNotice;
	
	// 게임 정보: 누구의턴, 골인 순서, 현재 상태가 RR인지 IM인지
	private ArrayList<Player> turnList;
	private ArrayList<Player> goalInList;
	private int RRorIM; // RR:0, IM:1; (roll rest 해야하는지 / input move 해야하는지 상태)
	private Boolean canGoBack;
	
	// 이번턴 주사위 정보, 이동가능칸수 정보
	private int rollResult;
	private int canMove;
	
	// observerUpdateFlag : 0:notice, 1:roll, 2:rest, 3:move, 4:turn end
	private int observerUpdateFlag;

	Model_Ingame(Model_Map model_Map , Model_PlayerInfo model_PlayerInfo) {
		// model 끼리 통신
		this.model_Map = model_Map;
		this.model_PlayerInfo = model_PlayerInfo;
		map = model_Map.getMap();
		players = model_PlayerInfo.getPlayers();
		numPlayers = model_PlayerInfo.getNumPlayers();
		start_y = model_Map.getStartY();
		start_x = model_Map.getStartX();
		end_y = model_Map.getEndY();
		end_x = model_Map.getEndX();
		
		// 플레이어 위치 지정
		for (int i = 0; i<numPlayers; i++) {
			movePlayer(i, start_y, start_x);
		}
		
		// turn list 초기화, RR mode, cangoback
		turnList = new ArrayList<Player>();
		for (int i = 0; i<numPlayers; i++) {
			turnList.add(players[i]);
		}
		goalInList = new ArrayList<Player>();
		RRorIM = 0;
		canGoBack = true;
		
		// 게임 전광판
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
			// 지금은 Roll 이나 Rest 차례가 아니라, input move 할 차례
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
			// 이동할 수 있으므로 input move 모드로 진입
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
			// 지금은 Roll 이나 Rest 차례가 아니라, input move 할 차례
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
		
		// 이제 input이 이동 가능한 입력인지 확인 절차
		// input, player 시작 과표 기억
		input = input.toUpperCase();
		int cy = nowPlayer.getPos_y();
		int cx = nowPlayer.getPos_x();
		Boolean flag = true;
		
		// cy,cx: 플레이어 초기 좌표
		// ty,tx: 한칸한칸 확인할 좌표
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
			
			// 이번 것은 움직일 수 있음
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
		
		// flag 가 true 면 갈 수 있는것,-> move!
		// flag 가 false면 갈 수 없는것 -> 경고후 return
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

	// roll, rest 하는 메소드 - 반드시 private
	// move
	
}