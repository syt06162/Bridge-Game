import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class View_Ingame extends JPanel implements Observer{
	private Controller_BoardFrame controller;
	private Model_Map model_Map;
	private Model_PlayerInfo model_PlayerInfo;
	
	//player info - 갖고있으면 안됨?
	private Player[] players;
	
	// panel 구성
	private final int NOTICE_CNT = 3;
	private JPanel noticePanel;
	private JLabel[] noticeList;
	
	private JPanel rollResultPanel;
	private String rollResultText = "Roll Result : 0";
	private String canMoveText = "Can Move : 0";
	private JLabel rollResultLabel;
	private JLabel canMoveLabel;
		
		
	public View_Ingame(Model_PlayerInfo model_PlayerInfo) {
		this.model_PlayerInfo = model_PlayerInfo;
		
		setLayout(new GridLayout(1,2));
		
		// left = noticePanel
		noticePanel = new JPanel(new GridLayout(NOTICE_CNT,1));
		noticeList = new JLabel[NOTICE_CNT];
		for (int i = 0; i<NOTICE_CNT; i++) {
			noticeList[i] = new JLabel("hi");
			noticePanel.add(noticeList[i]);
		}
		add(noticePanel);
		
		// right = rollResultPanel
		rollResultPanel = new JPanel(new GridLayout(3,1));
		rollResultLabel = new JLabel(rollResultText);
		canMoveLabel = new JLabel(canMoveText);
		rollResultPanel.add(rollResultLabel);
		rollResultPanel.add(canMoveLabel);
		
		add(rollResultPanel);

	}
	
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
}
