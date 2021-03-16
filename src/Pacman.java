
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Pacman extends Character {

	private static final long serialVersionUID = 1L;
	private final int ARY_ROW = 6, ARY_COL = 10;
	private final int POWER_TIME = 300;
	private final int DEFAULT_SPEED = 3;
	private final int SUB_IMAGE_PIXEL_SIZE = 51;
	private final int UP = 4, DOWN = 3, LEFT = 1, RIGHT = 0;
	private final int IMAGE_SIZE = 32;
	private boolean power_gain;
	private BufferedImage current_face;
	private BufferedImage[][] pac_ary;
	private String direction = "east", previous_direction = "east";
	public boolean move_right = false, move_left = false, move_up = false, move_down = false;
	private int face_count;
	private double previous_x, previous_y;
	private static int power_timer;
	
	/**
	 * Constructor
	 * @param x Integer type used to specify the x-coordinate of the top left point of the cell
	 * @param y Integer type used to specify the y-coordinate of the top left point of the cell
	 */
	public Pacman(int x, int y) {
		super(x, y);
		load_pacs();
		previous_x = this.x;
		previous_y = this.y;
		speed = DEFAULT_SPEED;
		face_count = 0; 
		power_timer = 0;
		power_gain = false;
	}
	
	
	/**
	 * Purpose: To get the power status of Pacman. This variable is affected by the PowerPellets
	 * @return power_gain variable which is a Boolean Data Type. If he has power from a PowerPellet it will return true. Otherwise, false.
	 */
	public boolean get_power(){
		return power_gain;
	}
	
	
	/**
	 * Purpose: Sets power_gain to true which means Pacman ate a PowerPellet
	 */
	public void set_power(){
		this.power_gain = true;
		power_timer = POWER_TIME;
	}

	
	/**
	 * Purpose: Updates Pacman's movement
	 */
	@Override
	public void tick() {
		if(move_right && validate_next_move(speed + x, y)){
			x += speed;
			direction = "east";
		}else if(move_left && validate_next_move(x - speed, y)){
			x -= speed;
			direction = "west";
		} 
		if(move_up && validate_next_move(x, y - speed)){
			y -= speed;
			direction = "north";
		}else if(move_down && validate_next_move(x, y + speed)){
			y += speed;
			direction = "south";
		}
		
		if(power_timer > 0){
			power_timer--;
		}else if(power_timer == 0){
			power_gain = false;
		}
	}
	
	
	/**
	 * Purpose: To load Pacman's images to pac_ary which is an array of BufferedImages
	 */
	private void load_pacs(){
		pac_ary = new BufferedImage[ARY_ROW][ARY_COL];
		try {
			BufferedImage temp = ImageIO.read(new File("resources/pacman/pactest.png"));
			for(int i = 0; i < ARY_ROW; i++){
				for(int j = 0; j < ARY_COL; j++){
					pac_ary[i][j] =  temp.getSubimage(j * SUB_IMAGE_PIXEL_SIZE, i * SUB_IMAGE_PIXEL_SIZE, 
							SUB_IMAGE_PIXEL_SIZE, SUB_IMAGE_PIXEL_SIZE);
				}
			}
		} catch (IOException e) {
			System.out.println("Failed to load pacs in load_pacs()...");
			e.printStackTrace();
		}
	}

	
	/**
	 * Purpose: To print location of Pacman
	 */
	public void print_loc(){
		System.out.println(this.getLocation());
	}
	
	
	/**
	 * Returns the location of Pacman 
	 * @return Point Object
	 */
	public Point get_location(){
		return this.getLocation();
	}

	
	@Override
	protected boolean validate_next_move(int next_x, int next_y){
		Rectangle next_space = new Rectangle(next_x, next_y, this.width, this.height);
		Map temp_map = Game.map; 
		int y_difference = this.y - next_y;
		int x_difference = this.x - next_x;
		int original_x = this.x;
		int original_y = this.y;
		
		//This is changing later on. There is an issue placing this logic in functions. 
		for(int w = 0; w < temp_map.cells.length; w++){
			for(int h = 0; h < temp_map.cells[0].length; h++){
				if(temp_map.cells[w][h] != null && next_space.intersects(temp_map.cells[w][h])){ //If this is the case then it must be part of the map
					if(x_difference > 0){
						x_difference *= -1;
						for(int i = x_difference; i <= 0; i++){
							next_space.setLocation(original_x + i, original_y);
							if(!next_space.intersects(temp_map.cells[w][h])){
								this.setLocation(original_x + i, original_y);
								return false;
							}
						}
					}else if(x_difference < 0){
						for(int i = Math.abs(x_difference); i >= 0; i--){
							next_space.setLocation(original_x + i, original_y);
							if(!next_space.intersects(temp_map.cells[w][h])){
								this.setLocation(original_x + i, original_y);
								return false;
							}
						}
					}else if(y_difference > 0){
						y_difference *= -1;
						for(int i = y_difference; i <= 0; i++){
							next_space.setLocation(original_x,original_y + i);
							if(!next_space.intersects(temp_map.cells[w][h])){
								this.setLocation(original_x,original_y + i);
								return false;
							}
						}
					}else if(y_difference < 0){
						for(int i = Math.abs(y_difference); i >= 0; i--){
							next_space.setLocation(original_x,original_y + i);
							if(!next_space.intersects(temp_map.cells[w][h])){
								this.setLocation(original_x,original_y + i);
								return false;
							}
						}
					}else {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	
	@Override
	public void render(Graphics g) {		
		if(previous_x == this.x && previous_y == this.y){
			current_face = pac_ary[RIGHT][RIGHT];
			g.drawImage(current_face, x, y, IMAGE_SIZE, IMAGE_SIZE, null);
		}else {
			switch(direction){
			case "north":
				if(check_rotation()){
					current_face = pac_ary[UP][face_count];
					face_count++;
				}else if(!check_rotation()){
					face_count = 0;
					current_face = pac_ary[UP][face_count];
					face_count++;
				}
				break;
			case "south":
				if(check_rotation()){
					current_face = pac_ary[DOWN][face_count];
					face_count++;
				}else if(!check_rotation()){
					face_count = 0;
					current_face = pac_ary[DOWN][face_count];
					face_count++;
				}
				break;
			case "east":
				if(check_rotation()){
					current_face = pac_ary[RIGHT][face_count];
					face_count++;
				}else if(!check_rotation()){
					face_count = 0;
					current_face = pac_ary[RIGHT][face_count];
					face_count++;
				}
				break;
			case "west":
				if(check_rotation()){
					current_face = pac_ary[LEFT][face_count];
					face_count++;
				}else if(!check_rotation()){
					face_count = 0;
					current_face = pac_ary[LEFT][face_count];
					face_count++;
				}
				break;
			default:
				current_face = pac_ary[RIGHT][RIGHT];
			}
			previous_direction = direction;
			previous_x = this.x;
			previous_y = this.y;
			g.drawImage(current_face, x, y, IMAGE_SIZE , IMAGE_SIZE, null);
		}
	}
	
	
	/**
	 * Purpose: To check the direction pacman is moving and was moving as well as which image pacman is on
	 * 			in the array of pacman images called pac_ary
	 * @return True if the previous direction is the same as the current and face_count isn't at 10. Otherwise, returns false.
	 */
	private boolean check_rotation(){
		if(previous_direction.equals(direction) && face_count != pac_ary.length){
			return true;
		}
		return false;
	}
}
