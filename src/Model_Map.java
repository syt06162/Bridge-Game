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
	private int size_x, size_y; // size_x = right-left+1
	
	Model_Map(String fileName) {
		buildMap(fileName);
	
	}
	
	void buildMap(String fileName) {
		mapString = new ArrayList<String>();
		
		// ■ 파일 읽어서 mapString에 저장
		try {
	        // 파일, 입력 스트림
	        File file = new File(fileName);
	        BufferedReader br = new BufferedReader(new FileReader(file));
	        
	        // 파일 읽고 저장
	        String temp = br.readLine();
	        for (int i = 0; temp!=null; i++) {
	        	mapString.add(temp);
	        	temp = br.readLine();
	        }
	        br.close();
	        
	    } 
		catch (NullPointerException e){ 
	        e.getStackTrace();
		}
		catch (FileNotFoundException e) {
			e.getStackTrace();
	    } 
		catch (IOException e) {
			e.getStackTrace();
	    }
		
		// ■ 맵 사이즈 계산 (계산 후 상하좌우에 + 1 하여 여백)
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
        
        // ■ 계산 값으로 초기 좌표, 맵 사이즈 지정 및 생성
        start_x = -left_max;
        start_y = -up_max;
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
        
        // ■■ mapString 이용하여 map 채우기
        int cx = start_x, cy = start_y;
        for (int i = 0; i< mapString.size()-1; i++) {
        	map[cy][cx] = mapString.get(i);
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
        map[cy][cx] = mapString.get(mapString.size()-1);
        
        for (int i = 0; i< size_y; i++){
        	for (int j = 0; j< size_x; j++){
            	System.out.print(map[i][j].charAt(0));
            }
        	System.out.println();
        }
		
	}
	
	
	// getter
	public int getStartX() {
		return start_x;
	}
	public int getStartY() {
		return start_y;
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
}
