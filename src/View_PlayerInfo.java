import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class View_PlayerInfo extends JPanel implements Observer{
	private Controller_BoardFrame controller;
	private Model_Map model_Map;
	private Model_PlayerInfo model_PlayerInfo;
	
	//player info - 갖고있으면 안됨?
	private Player[] players;
	
	//player info JTable : table
	private String[] headers = {"name", "Bridge", "Pdriver", "Hammer", "Saw", "rank", "total"};
	private JTable table;
	private DefaultTableModel dtModel; 
	private JScrollPane scrollPane;
	
	View_PlayerInfo(Model_PlayerInfo model_PlayerInfo) {
		this.model_PlayerInfo = model_PlayerInfo;
		
		setPreferredSize(new Dimension(300,200));
		setLayout(new BorderLayout());
		add(new JLabel("Player Info"), BorderLayout.NORTH);
		
		// player info JTable : table
		dtModel = new DefaultTableModel();
		dtModel.setColumnCount(headers.length);
		dtModel.setColumnIdentifiers(headers);
		dtModel.addRow(headers);
		dtModel.addRow(headers);
		
		table = new JTable(dtModel);
		scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(300,200));
		add(scrollPane, BorderLayout.CENTER);
		
		setVisible(true);
		
	}
	
	void addInitialPlayers() {
		players = model_PlayerInfo.getPlayers();
	}
	
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
}
