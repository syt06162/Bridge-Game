import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("deprecation")
public class View_PlayerInfo extends JPanel implements Observer{
	private Controller_BoardFrame controller;
	private Model_PlayerInfo model_PlayerInfo;
	
	//player info
	private Player[] players;
	private int numPlayers;
	
	//player info JTable : table
	private String[] headers = {"name", "Bridge", "Pdriver", "Hammer", "Saw", "rank", "total"};
	private JTable table;
	private DefaultTableModel dtModel; 
	private JScrollPane scrollPane;
	
	View_PlayerInfo(Model_PlayerInfo model_PlayerInfo) {
		// model 초기 연동
		this.model_PlayerInfo = model_PlayerInfo;
		players = model_PlayerInfo.getPlayers();
		numPlayers = model_PlayerInfo.getNumPlayers();
		
		// UI
		setPreferredSize(new Dimension(300,200));
		setLayout(new BorderLayout());
		add(new JLabel("Player Info"), BorderLayout.NORTH);
		
		//  player info JTable : table
		dtModel = new DefaultTableModel();
		dtModel.setColumnCount(headers.length);
		dtModel.setColumnIdentifiers(headers);
		
		table = new JTable(dtModel){
	        public boolean editCellAt(int row, int column, java.util.EventObject e) {
	        	return false;
	        }
	        public boolean isCellEditable(int rowIndex, int mColIndex) {
	        	return false;
	        }
	        public boolean isCellSelected(int row, int column) {
	        	return false;
	        }
	    };
	    table.getTableHeader().setReorderingAllowed(false); // 이동 불가
	    table.getTableHeader().setResizingAllowed(false); // 크기 조절 불가
	 
		scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(300,200));
		add(scrollPane, BorderLayout.CENTER);
		
		
		showPlayersInTable();
		
		setVisible(true);
		
	}
	
	private void showPlayersInTable() {
		String[] temp = new String[7];
		
		// 기존 table 삭제
		int rowCnt = dtModel.getRowCount();
		while (rowCnt != 0) {
			dtModel.removeRow(0);
			rowCnt--;
		}
		
		for (int i = 0; i<numPlayers; i++) {
			temp[0] = players[i].getName();
			temp[1] = String.valueOf( players[i].getBcard());
			temp[2] = String.valueOf( players[i].getPcard());
			temp[3] = String.valueOf( players[i].getHcard());
			temp[4] = String.valueOf( players[i].getScard());
			temp[5] = String.valueOf( players[i].getRank());
			temp[6] = String.valueOf( players[i].getTotalPoint());
			dtModel.addRow(temp);
		}
		
		dtModel.fireTableDataChanged();
	}
	
	
	@Override
	public void update(Observable o, Object arg) {
		showPlayersInTable();
		
	}
}
