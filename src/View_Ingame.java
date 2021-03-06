import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class View_Ingame extends JPanel implements Observer{
	private Controller_BoardFrame controller;
	private Model_PlayerInfo model_PlayerInfo;
	private Model_Ingame model_Ingame;
	
	// panel ????
	private final int NOTICE_CNT = 3;
	private JPanel noticePanel;
	private JLabel[] noticeList;
	
	private JPanel rollResultPanel;
	private String rollResultText = "Roll Result : ";
	private String canMoveText = "Can Move : ";
	private JLabel rollResultLabel;
	private JLabel canMoveLabel;
	private JLabel pleaseInput;
		
		
	public View_Ingame(Model_Map model_Map, Model_PlayerInfo model_PlayerInfo, Model_Ingame model_Ingame) {
		this.model_PlayerInfo = model_PlayerInfo;
		this.model_Ingame = model_Ingame;
		
		// UI setting
		setLayout(new GridLayout(1,2));
		
		// left = noticePanel
		noticePanel = new JPanel(new GridLayout(NOTICE_CNT,1));
		noticePanel.setBackground(Color.YELLOW);
		noticePanel.setPreferredSize(new Dimension(400,120));
		noticePanel.setVisible(true);
		noticeList = new JLabel[NOTICE_CNT];
		for (int i = 0; i<NOTICE_CNT; i++) {
			noticeList[i] = new JLabel();
			noticePanel.add(noticeList[i]);
		}
		add(noticePanel);
		
		// right = rollResultPanel
		rollResultPanel = new JPanel(new GridLayout(3,1));
		rollResultPanel.setPreferredSize(new Dimension(300,120));
		rollResultPanel.setVisible(true);
		
		rollResultLabel = new JLabel(rollResultText);
		canMoveLabel = new JLabel(canMoveText);
		rollResultPanel.add(rollResultLabel);
		rollResultPanel.add(canMoveLabel);
		pleaseInput = new JLabel("input move (ex. ddrru):");
		pleaseInput.setHorizontalAlignment(JLabel.CENTER);
		rollResultPanel.add(pleaseInput);
		add(rollResultPanel);
		
		setVisible(true);
		
		updateNotice(); // notice
	}
	
	private void updateNotice() {
		String[] notice = model_Ingame.getGameNotice();
		for (int i = 0; i<3; i++) {
			noticeList[i].setText(notice[i]);
		}
	}
	
	private void updateRollResult() {
		rollResultLabel.setText(rollResultText + String.valueOf( model_Ingame.getRollResult() ));
		canMoveLabel.setText(canMoveText + String.valueOf( model_Ingame.getCanMove() ));
	}
	
	private void updateTurnEnd() {
		rollResultLabel.setText(rollResultText);
		canMoveLabel.setText(canMoveText);
	}
	
	private void gameEnd() {
		JOptionPane.showMessageDialog(
				null, "winner: " + model_PlayerInfo.getWinnerString(), "Game End", JOptionPane.DEFAULT_OPTION);
	}
	
	
	@Override
	public void update(Observable o, Object arg) {
		int flag = model_Ingame.getObserverUpdateFlag();
		
		if (flag == 0)
			updateNotice(); // notice
		else if (flag == 1)
			updateRollResult();
		else if (flag == 4)
			updateTurnEnd();
		else if (flag == -1)
			gameEnd();
	}
	
//	void setController(Controller_BoardFrame controller) {
//		this.controller = controller;
//	}
}
