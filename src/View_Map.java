import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

@SuppressWarnings("deprecation")
class View_Map extends JPanel implements Observer{

	private Controller_BoardFrame controller;
	private Model_Map model;
	
	private String[][] map; 
	private Cell[][] cells;
	private int size_y;
	private int size_x;
	
	private ImageIcon ic_Cell, ic_Null, ic_Pdriver, ic_Hammer, ic_Saw, ic_B_start, ic_B_real;
	private ImageIcon[] ic_Start;
	private ImageIcon[] ic_End;
	
	private Image img_Cell, img_Null, img_Pdriver, img_Hammer, img_Saw, img_B_start, img_B_real;;
	private Image[] img_Start;
	private Image[] img_End;
	
	
	View_Map(Model_Map model){
		setPreferredSize(new Dimension(1000,800));
		
		// model
		this.model = model;
		
		// image, icons
		initImageIcons();
		
		// initial map = default.map
		mapInitialized();
		
		
		System.out.println("view intial");
	}
	
	@Override
	public void update(Observable o, Object arg) {
		mapInitialized();
		System.out.println("view update");
	}
	
	void initImageIcons() {
		ic_Cell = new ImageIcon("images/cell_C.png");
		ic_Null = new ImageIcon("images/cell_N.png");
		ic_Pdriver = new ImageIcon("images/cell_P.png");
		ic_Hammer = new ImageIcon("images/cell_H.png");
		ic_Saw = new ImageIcon("images/cell_S.png");
		ic_B_start = new ImageIcon("images/cell_B_start.png"); 
		ic_B_real = new ImageIcon("images/cell_B_real.png");
		
		ic_Start = new ImageIcon[5];
		for (int i = 1; i<5; i++) {
			String tempName = "images/s"+String.valueOf(i)+".png";
			ic_Start[i] = new ImageIcon(tempName);
		}
		ic_End = new ImageIcon[5];
		for (int i = 1; i<5; i++) {
			String tempName = "images/e"+String.valueOf(i)+".png";
			ic_End[i] = new ImageIcon(tempName);
		}
		
		
		img_Cell = ic_Cell.getImage();
		img_Null = ic_Null.getImage();
		img_Pdriver = ic_Pdriver.getImage();
		img_Hammer = ic_Hammer.getImage(); 
		img_Saw = ic_Saw.getImage();
		img_B_start = ic_B_start.getImage(); 
		img_B_real = ic_B_real.getImage();
		
		img_Start = new Image[5];
		for (int i = 1; i<5; i++) {
			img_Start[i] = ic_Start[i].getImage();
		}
		img_End = new Image[5];
		for (int i = 1; i<5; i++) {
			img_End[i] = ic_End[i].getImage();
		}
		
	}

	void mapInitialized() {
		map = model.getMap();
		size_y = model.getSizeY();
		size_x = model.getSizeX();
		
		// 기존 map 지우기
		removeAll();
		
		// 새로운 map 틀
		setLayout(new GridLayout(size_y, size_x));
		
		cells = new Cell[size_y][size_x];
		for (int i = 0; i<size_y ; i++) {
			cells[i] = new Cell[size_x];
		}
		
		for (int i = 0; i<size_y ; i++) {
			for (int j = 0; j<size_x ; j++) {
				Image img = img_Null;
				switch (map[i][j].charAt(0)) {
					case 'S': {
						// start 인지 saw 인지 구분
						int value = map[i][j].charAt(4)-'0';
						if (value>=1 && value<=4) {
							img = img_Start[value];
						}
						else
							img = img_Saw;
						break;
					}
					case 'E': {
						img = img_End[map[i][j].charAt(2)-'0'];
						break;
					}
					case ' ': {
						img = img_Null;
						break;
					}
					case 'P': {
						img = img_Pdriver;
						break;
					}
					case 'H': {
						img = img_Hammer;
						break;
					}
					case 'C': {
						img = img_Cell;
						break;
					}
					case 'B': {
						img = img_B_start;
						break;
					}
					case 'b': {
						img = img_Cell;
						break;
					}
					case '=': {
						img = img_B_real;
						break;
					}
				}
				cells[i][j] = new Cell(img);
				add(cells[i][j]);
			}
		}
		
		// 새로 갱신
		revalidate();
		repaint();
		setVisible(true);
	}
	
	// inner class Cell : one cell in board
	class Cell extends JPanel{
		Image img;
		Cell(Image img){
			setLayout(new FlowLayout());
			this.img = img;
			
			setVisible(true);
		}
		
		public void paintComponent(Graphics g){
	        super.paintComponent(g);
	        g.drawImage(img,0,0,getWidth(),getHeight(),this);
	    }
	}

	
	void setController(Controller_BoardFrame controller) {
		this.controller = controller;
	}
	
	
	
}
