import java.util.ArrayList;
import java.util.Observable;

@SuppressWarnings("deprecation")
public class Model_Ingame extends Observable {
	// 지도 정보
	private Model_Map model_Map;
		
	// 글자판 정보
	private String[] gameNotice;
	
	// 게임 정보: 누구의턴, 골인 순서
	private ArrayList<Player> turnList;
	
	Model_Ingame() {
		// 게임 전광판
		gameNotice = new String[3];
		gameNotice[0] = gameNotice[1] = gameNotice[2] = "";
		newNotice("Game Start");
	}
	
	void newNotice(String str) {
		gameNotice[0] = gameNotice[1];
		gameNotice[1] = gameNotice[2];
		gameNotice[2] = str;
	}
	

	// roll, rest 하는 메소드 - 반드시 private
	// move
	
}