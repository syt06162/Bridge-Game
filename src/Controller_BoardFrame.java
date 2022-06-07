import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
	
	private Model_Ingame model_Ingame;
	
	private StartPanel startPanel;
	private IngamePanel ingamePanel;
	
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
		
		// view, model
		model_PlayerInfo = new Model_PlayerInfo(numPlayers);
		model_Ingame = new Model_Ingame(model_Map ,model_PlayerInfo);
		view_Map.startGame(model_PlayerInfo);
		ingamePanel = new IngamePanel(model_Map, model_PlayerInfo, model_Ingame, this, view_Map); 
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
	
	// controller
	private Controller_BoardFrame controller;
	
	// model_map
	private Model_Map model_map;
	
	// playerInfo : Model, View
	private Model_PlayerInfo model_PlayerInfo;
	private View_PlayerInfo view_PlayerInfo;
	
	//  Ingame: Model, View (has playerInfo panel as component)
	private Model_Ingame model_Ingame;
	private View_Ingame view_Ingame;
	
	// ---UI--
	// 우측 패널 (Ingame(공지, 주사위값), rrPanel, inputPanel
	private JPanel rightPanel;
	
	//  우측 하단 Button들
	private JPanel rightDownPanel;
	
	private JPanel rrPanel;
	private JButton restButton;
	private JButton rollButton;
	
	private JPanel inputMovePanel;
	private JTextField inputMoveTextField;
	private JButton inputMoveButton;

	
	IngamePanel(Model_Map model_map, Model_PlayerInfo model_PlayerInfo, Model_Ingame model_Ingame,  Controller_BoardFrame controller, View_Map view_Map) {
		this.model_PlayerInfo = model_PlayerInfo;
		this.model_Ingame = model_Ingame;
		this.model_map = model_map;
		this.controller = controller;
		setPreferredSize(new Dimension(1000,200));
		setLayout(new GridLayout(1,2));
		
		// 좌측 - player info
		view_PlayerInfo = new View_PlayerInfo(model_PlayerInfo);
		model_PlayerInfo.addObserver(view_PlayerInfo);
		model_PlayerInfo.addObserver(view_Map);
		add(view_PlayerInfo);
		
		// 우측 패널 (2,1) grid
		rightPanel = new JPanel(new GridLayout(2,1));
		rightPanel.setPreferredSize(new Dimension(700,200));
		rightPanel.setVisible(true);
		add(rightPanel);
		
		//  우측 상단 - ingame view
		view_Ingame = new View_Ingame(model_map, model_PlayerInfo, model_Ingame);
		rightPanel.add(view_Ingame);
		model_Ingame.addObserver(view_Ingame);
		model_Ingame.addObserver(view_PlayerInfo);
		
		//  우측 하단 - (rest roll) 버튼, 이동입력창
		rightDownPanel = new JPanel(new GridLayout(1,2, 10, 10));
		rightDownPanel.setPreferredSize(new Dimension(700, 80));
		rightDownPanel.setVisible(true);
		
		rrPanel = new JPanel(new GridLayout(1,2,10,10));
		rrPanel.setVisible(true);
		restButton = new JButton("REST");
		restButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model_Ingame.rest();
			}
		});
		rollButton = new JButton("ROLL");
		rollButton.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				model_Ingame.roll();
			}
		});
		rrPanel.add(restButton);
		rrPanel.add(rollButton);
		
		inputMovePanel = new JPanel(new GridLayout(2,1,10,10));
		inputMovePanel.setVisible(true);
		inputMoveTextField = new JTextField(5);
		inputMoveButton = new JButton("move");
		inputMoveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model_Ingame.inputMove(inputMoveTextField.getText());
			}
		});
		inputMovePanel.add(inputMoveTextField);
		inputMovePanel.add(inputMoveButton);
		
		rightDownPanel.add(rrPanel);
		rightDownPanel.add(inputMovePanel);
		rightPanel.add(rightDownPanel);
		
		setVisible(true);
	}
	

}
