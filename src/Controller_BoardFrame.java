import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

// MVC패턴 - controller 기능

public class Controller_BoardFrame extends JFrame {
	private Model_Map model_Map;
	private View_Map view_Map;
	
	private Model_PlayerInfo model_PlayerInfo;
	private View_PlayerInfo view_PlayerInfo;
	
	private JPanel startPanel;
	private JPanel ingamePanel;
	
	Controller_BoardFrame(String fileName){
		// view, model
		model_Map = new Model_Map(fileName);
		view_Map = new View_Map(model_Map);
		model_Map.addObserver(view_Map);
		view_Map.setController(this);
		
		// this JFrame
		setLayout(new BorderLayout());
		setSize(1000,1000);
		setTitle("Bridge Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null); // 중앙 배치
		
		// add map(view) and button(panels)
		startPanel = new StartPanel(model_Map, this);
		add(view_Map, BorderLayout.CENTER);
		add(startPanel, BorderLayout.SOUTH);
		
		setVisible(true);
	}
	
	void gameStart(int numPlayers) {
		remove(startPanel);
		
		IngamePanel ingamePanel = new IngamePanel(model_PlayerInfo, this); 
		add(ingamePanel, BorderLayout.SOUTH);
		
		revalidate();
		repaint();
	}

}

class StartPanel extends JPanel {
	private Model_Map model_Map;
	private Controller_BoardFrame controller;
	
	// left - new map components
	private JLabel newMapLabel = new JLabel("input new map file name (xxx.map) :");
	private JButton newMapButton = new JButton("load new map");
	private JTextField newMapTextField = new JTextField("default.map", 10);
	
	// right - play start components
	private JLabel gamePeopleLabel = new JLabel("input playing people number (2-4) :");
	private JButton gameStartButton = new JButton("game start");
	private JTextField gamePeopleTextField = new JTextField("2", 2);
	
	StartPanel(Model_Map model_Map, Controller_BoardFrame controller) {
		this.model_Map = model_Map;
		this.controller = controller;
		
		setPreferredSize(new Dimension(1000,200));
		setLayout(new GridLayout(1,2));
		
		// left - new map 
		JPanel left = new JPanel(new FlowLayout());
		left.add(newMapLabel);
		left.add(newMapTextField);
		newMapButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					model_Map.buildMap(newMapTextField.getText());
				}
				catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		left.add(newMapButton);
		left.setVisible(true);
		add(left);
		
		// right - play start
		JPanel right = new JPanel(new FlowLayout());
		right.add(gamePeopleLabel);
		right.add(gamePeopleTextField);
		gameStartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int numPlayers = Integer.parseInt(gamePeopleTextField.getText());
				if ((numPlayers<2) || (numPlayers>4)) {
					JOptionPane.showMessageDialog(
							null, "player number should be 2~4", "Game Start Error", JOptionPane.WARNING_MESSAGE);
				}
				else {
					controller.gameStart(numPlayers);
				}
			}
		});
		right.add(gameStartButton);
		right.setVisible(true);
		add(right);
	}
}

class IngamePanel extends JPanel {
	private Model_Map model_map;
	private Model_PlayerInfo model_PlayerInfo;
	private Controller_BoardFrame controller;
	
	private View_PlayerInfo view_PlayerInfo;
	
	IngamePanel(Model_PlayerInfo model_PlayerInfo, Controller_BoardFrame controller) {
		this.model_PlayerInfo = model_PlayerInfo;
		this.controller = controller;
		setPreferredSize(new Dimension(1000,200));
		setLayout(new GridLayout(1,1));
		
		// 일단 임시
		view_PlayerInfo = new View_PlayerInfo(model_PlayerInfo);
		add(view_PlayerInfo);
		
		setVisible(true);
	}
	
}
