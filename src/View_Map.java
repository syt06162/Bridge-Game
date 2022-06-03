import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;


@SuppressWarnings("serial")
class View_Map extends JPanel implements Observer{

	private Controller_BoardFrame controller;
	private Model_Map model;
	
	View_Map(Model_Map model){
		// model
		this.model = model;
		
		// JPanel
		setLayout(new GridLayout(model.getSizeY(), model.getSizeX());
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
	
}
