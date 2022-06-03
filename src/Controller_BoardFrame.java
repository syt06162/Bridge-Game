import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;

// MVC���� - controller ���

public class Controller_BoardFrame extends JFrame {
	Model_Map model;
	
	
	Controller_BoardFrame(String fileName){
		model = new Model_Map(fileName);
		
		setLayout(new BorderLayout());
		setSize(1500,1000);
		setTitle("Bridge Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null); // �߾� ��ġ
		setVisible(true);
		
	}


}
