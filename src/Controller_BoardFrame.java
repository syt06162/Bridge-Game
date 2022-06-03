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
import javax.swing.JPanel;
import javax.swing.JTextField;

// MVC패턴 - controller 기능

public class Controller_BoardFrame extends JFrame {
	private Model_Map model;
	private View_Map view;
	
	private JPanel startPanel;
	private JPanel gamePanel;
	
	
	Controller_BoardFrame(String fileName){
		model = new Model_Map(fileName);
		view = new View_Map(model);
		startPanel = new StartPanel(model);
		
		setLayout(new BorderLayout());
		setSize(1000,1000);
		setTitle("Bridge Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null); // 중앙 배치
		setVisible(true);
		
		add(view, BorderLayout.CENTER);
		add(startPanel, BorderLayout.SOUTH);
		// button 에 리스너 달거 있으면 달기
		
		model.addObserver(view);
		view.setController(this);
		
	}

}

class StartPanel extends JPanel {
	private Model_Map model;
	
	private JLabel newMapLabel = new JLabel("input new map file name (xxx.map) :");
	private JButton newMapButton = new JButton("load new map");
	private JTextField newMapTextField = new JTextField("default.map", 10);
	
	private JLabel gamePeopleLabel = new JLabel("input playing people number(2-4) :");
	private JButton gameStartButton = new JButton("game start");
	private JTextField gamePeopleTextField = new JTextField("2", 2);
	
	StartPanel(Model_Map model) {
		this.model = model;
		setPreferredSize(new Dimension(1000,200));
		setLayout(new GridLayout(1,2));
		
		// left - new map 
		JPanel left = new JPanel(new FlowLayout());
		left.add(newMapLabel);
		left.add(newMapTextField);
		newMapButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.buildMap(newMapTextField.getText());
			}
		});
		left.add(newMapButton);
		left.setVisible(true);
		add(left);
		
		// right - play start
		JPanel right = new JPanel(new FlowLayout());
		right.add(gamePeopleLabel);
		right.add(gamePeopleTextField);
		right.add(gameStartButton);
		right.setVisible(true);
		add(right);
		
	}
}
