import java.util.ArrayList;
import java.util.Observable;

@SuppressWarnings("deprecation")
public class Model_Ingame extends Observable {
	// ���� ����
	private Model_Map model_Map;
		
	// ������ ����
	private String[] gameNotice;
	
	// ���� ����: ��������, ���� ����
	private ArrayList<Player> turnList;
	
	Model_Ingame() {
		// ���� ������
		gameNotice = new String[3];
		gameNotice[0] = gameNotice[1] = gameNotice[2] = "";
		newNotice("Game Start");
	}
	
	void newNotice(String str) {
		gameNotice[0] = gameNotice[1];
		gameNotice[1] = gameNotice[2];
		gameNotice[2] = str;
	}
	

	// roll, rest �ϴ� �޼ҵ� - �ݵ�� private
	// move
	
}