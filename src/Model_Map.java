import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;

@SuppressWarnings("deprecation")
public class Model_Map extends Observable {
	
	private String[][] map;
	private ArrayList<String> mapString;
	private int start_x, start_y;
	private int end_x, end_y;
	private int size_x, size_y; // size_x = right-left+1
	
	private int successLoad = 0; // flag
	
	Model_Map(String fileName) {
		buildMap(fileName);
	}
	
	void buildMap(String fileName) {
		mapString = new ArrayList<String>();
		
		// �� ���� �о mapString�� ����
		try {
	        // ����, �Է� ��Ʈ��
	        File file = new File(fileName);
	        BufferedReader br = new BufferedReader(new FileReader(file));
	        
	        // ���� �а� ����
	        String temp = br.readLine();
	        for (int i = 0; temp!=null; i++) {
	        	mapString.add(temp);
	        	temp = br.readLine();
	        }
	        br.close();
	        
	    } 
		catch (NullPointerException e){ 
	        e.getStackTrace();
	        successLoad = -1;
	        setChanged();
	        notifyObservers();
	        return;
		}
		catch (FileNotFoundException e) {
			e.getStackTrace();
			successLoad = -1;
	        setChanged();
	        notifyObservers();
	        return;
	    } 
		catch (IOException e) {
			e.getStackTrace();
			successLoad = -1;
	        setChanged();
	        notifyObservers();
	        return;
	    }
		
		// �� �� ������ ��� (��� �� �����¿쿡 + 1 �Ͽ� ����)
        int left_max = 0, right_max = 0, up_max = 0, down_max = 0;
        int currentX = 0, currentY = 0;
        
        for (int i = 0; i<mapString.size() - 1; i++) {
        	switch (mapString.get(i).charAt( mapString.get(i).length()-1 )) {
				case 'U': {
					currentY -= 1;
					if (currentY < up_max)
						up_max = currentY;
					break;
				}
				case 'D': {
					currentY += 1;
					if (currentY > down_max)
						down_max = currentY;
					break;
				}
				case 'L': {
					currentX -= 1;
					if (currentX < left_max)
						left_max = currentX;
					break;
				}
				case 'R': {
					currentX += 1;
					if (currentX > right_max)
						right_max = currentX;
					break;
				}
	        }
        }
        left_max--;
        right_max++;
        up_max--;
        down_max++;
        
        // �� ��� ������ �ʱ� ��ǥ, �� ������ ���� �� ����
        start_x = -left_max;
        start_y = -up_max;
        end_x = currentX + start_x;
        end_y = currentY + start_y;
        size_x = right_max - left_max + 1;
        size_y = down_max - up_max + 1;
        
        map = new String[size_y][size_x];
        for (int i = 0; i<map.length; i++) {
        	map[i] = new String[size_x];
        }
        for (int i = 0; i< size_y; i++){
        	for (int j = 0; j< size_x; j++){
            	map[i][j] = " ";
            }
        }
        
        // ��� mapString �̿��Ͽ� map ä���
        int cx = start_x, cy = start_y;
        for (int i = 0; i< mapString.size()-1; i++) {
        	map[cy][cx] = mapString.get(i);
        	if (map[cy][cx].charAt(0)=='B')
        		map[cy][cx+1] = "= L R";
        	switch (mapString.get(i).charAt( mapString.get(i).length()-1 )) {
				case 'U': {
					cy -= 1;
					break;
				}
				case 'D': {
					cy += 1;
					break;
				}
				case 'L': {
					cx -= 1;
					break;
				}
				case 'R': {
					cx += 1;
					break;
				}
        	}
        }
        // last end cell
        map[cy][cx] = mapString.get(mapString.size()-1);
        
        // start, end 4ĭ���� �����
        map[start_y-1][start_x-1] = "S X 1";
        map[start_y-1][start_x] = "S X 2";
        map[start_y][start_x-1] = "S X 3";
        map[start_y][start_x] = "S " + mapString.get(0).charAt(2) 
        							+ " " + mapString.get(0).charAt(2);
        
        map[end_y-1][end_x] = "E 1";
        map[end_y-1][end_x+1] = "E 2";
        map[end_y][end_x] = "E 3";
        map[end_y][end_x+1] = "E 4";
        
        // debug
        for (int i = 0; i< size_y; i++){
        	for (int j = 0; j< size_x; j++){
            	System.out.print(map[i][j].charAt(0));
            }
        	System.out.println();
        }
        
        // observer �˷��ֱ�
        successLoad = 0;
        setChanged();
        notifyObservers();
	}
	
	
	// getter
	public int getStartX() {
		return start_x;
	}
	public int getStartY() {
		return start_y;
	}
	public int getEndX() {
		return end_x;
	}
	public int getEndY() {
		return end_y;
	}
	public int getSizeX() {
		return size_x;
	}
	public int getSizeY() {
		return size_y;
	}
	public String[][] getMap(){
		return map;
	}
	public int getSuccessLoad() {
		return successLoad;
	}
	
}
