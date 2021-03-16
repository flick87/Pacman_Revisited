
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Map {

	private int width;
	private int height;
	private boolean initialized = false;
	private ArrayList<Point> ghost_locations;
	private Point pac_location;
	private ArrayList<PowerPellet> pows;
	private int red_value, green_value, blue_value;
	private ArrayList<Niblet> niblets;
	public Cell[][] cells; 
	
	/**
	 * Constructor
	 * @param file_path String object specifying location of map file
	 */
	public Map(String file_path){
		ghost_locations = new ArrayList<Point>();
		niblets = new ArrayList<Niblet>();
		pows = new ArrayList<PowerPellet>();
		red_value = 255;
		blue_value = 1;
		green_value = 0;
		
		try {
			BufferedImage map = ImageIO.read(new File(file_path));
			this.height = map.getHeight();
			this.width = map.getWidth();
			int[] pixels = new int[this.height * this.width];
			cells = new Cell[this.width][this.height];
			map.getRGB(0, 0, this.width, this.height, pixels, 0, this.width); 
			for(int w = 0; w < this.width; w++){
				for(int h = 0; h < this.height; h++){
					int color = pixels[w + (h * this.width)];
					if(color == 0xFF000000){ //This is black. This is wall.
						
						cells[w][h] = new Cell(w * 32 , h * 32);
					}else if(color == 0xFF2414FF){
						pac_location = new Point(w * 32, h * 32);
					}else if(color == 0xFFFF1928){ 
						ghost_locations.add(new Point(w * 32, h * 32));
					}else if(color == 0xFFB6FF00){ //This is the corner
						cells[w][h] = new Cell(w * 32 , h * 32, 32, 32);
					}else if(color == 0xFFFFFF00){ 
						pows.add(new PowerPellet(w * 32, h * 32));
					}else {
						niblets.add(new Niblet(w * 32, h * 32, 100));
					}
				}
			}
		} 
		
		catch(IllegalArgumentException r){
			System.out.println("This is the path: " + file_path);
			r.printStackTrace();
		}
		
		catch (IOException e) {
			System.out.println("This is the path: " + file_path);
			e.printStackTrace();
		}
	}
	
	/**
	 * Purpose: To retrieve the array of power pellets called pows
	 * @return ArrayList that contains all the PowerPellets
	 */
	public ArrayList<PowerPellet> get_pows(){
		return pows;
	}
	
	/**
	 * Purpose: To retrieve the array of Niblets called niblets
	 * @return  ArrayList that contains all the Niblets.
	 */
	public ArrayList<Niblet> get_niblets(){
		return niblets;
	}
	
	
	/**
	 * Purpose: Gets the starting locations of all the ghosts 
	 * @return ArrayList containing Point objects that hold ghost starting locations on the map
	 */
	public ArrayList<Point> get_ghosts_init_locations(){
		return ghost_locations;
	}
	
	/**
	 * Purpose: Retrieve the starting location of Pacman
	 * @return Point object
	 */
	public Point get_pac_init_location(){
		return pac_location;
	}
	
	
	/**
	 * Purpose: To draw the graphic
	 * @param g Graphics object
	 */
	public void render(Graphics g){
		int size_nible = niblets.size();
		int size_pows = pows.size();
		if(!initialized){
			for(int i = 0; i < height; i++){
				for(int j = 0; j < width; j++){
					if(cells[j][i] != null){
						cells[j][i].render(g); 
					}
				}
			}
		}
		
		this.alternate_RGB();
		if(niblets.isEmpty() == false){
			niblets.get(0).set_RGB(red_value, green_value, blue_value);
		}

		if(size_pows != 0){
			pows.get(0).set_RGB(red_value, green_value, blue_value);
		}
		
		//below is for dots
		for(int i = 0; i < size_nible; i++){
			niblets.get(i).render(g);
		}
		for(int i = 0; i < size_pows; i++){
			pows.get(i).render(g);
		}
	}
	
	/**
	 * Purpose: To alternate RGB values as a function of time. This alternates between each color moving around the color wheel.
	 */
	private void alternate_RGB(){
			if(blue_value == 0 && red_value == 255){
				green_value++;
				if(green_value == 255){
					red_value--;
				}
			}else if(green_value == 255 && blue_value == 0){
				red_value--;
				if(red_value == 0){
					blue_value++;
				}
			}else if(green_value == 255 && red_value == 0){
				blue_value++;
				if(blue_value == 255){
					green_value--;
				}
			}else if(red_value == 0 && blue_value == 255){
				green_value--;
				if(green_value == 0){
					red_value++;
				}
			}else if(green_value == 0 && blue_value == 255){
				red_value++;
				if(red_value == 255){
					blue_value--;
				}
			}else if(green_value == 0 && red_value == 255){
				blue_value--;
				if(blue_value == 0){
					green_value++;
				}
			}
	}
	
}
