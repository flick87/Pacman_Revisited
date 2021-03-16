
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Ghost extends Character{

	private static final long serialVersionUID = 1L;
	private Point target;
	private BufferedImage ghost;
	private static final int ROWS = 2, COLUMNS = 10;
	private int state; // 0 = dumb, 1 = smart, 2 = looking for new path toward Pacman 
	private final int UP = 0, RIGHT = 1, DOWN = 2, LEFT = 3, DUMB = 0, SMART = 1, HIGHLANDER = 2, NUM_OF_DIR = 4;
	private int direction;
	private int previous_direction;
	private Random genny;
	private int time, switch_time;
	private BufferedImage[][] faces;
	private BufferedImage all_faces, hurt;
	private int face_count;
	private boolean canHurt = false;
	private boolean hurt_picture = false;
	private int timer = 0;

	/**
	 * Purpose: Constructor
	 * @param x Positive integer
	 * @param y Positive integer
	 */
	public Ghost(int x, int y){
		super(x,y);
		genny = new Random();
		genny.setSeed(System.currentTimeMillis());
		state = SMART;
		direction = genny.nextInt(NUM_OF_DIR);
		previous_direction = 0;
		speed = 2;
		time = 0;
		switch_time = 5 * 60; //This will allow ghost to switch algorithms every 3 seconds.
		load_faces("blue");
		face_count = 0;
	}
	
	/**
	 * Purpose: Constructor
	 * @param x Positive integer
	 * @param y Postitive integer
	 * @param color String with the color in all lowercase letters
	 */
	public Ghost(int x, int y, String color) {
		super(x, y);
		genny = new Random();
		genny.setSeed(System.currentTimeMillis());
		time = 0;
		switch_time = 5 * 60; 
		speed = 2;
		state = SMART;
		direction = genny.nextInt(NUM_OF_DIR);
		previous_direction = 0;
		load_faces(color);
		face_count = 0;
	}
	
	/**
	 * Purpose: Constructor
	 * @param p Point location
	 * @param color String with the color in all lowercase letters
	 */
	public Ghost(Point p, String color) {
		super(p.x,p.y);
		genny = new Random();
		time = 0;
		switch_time = 5 * 60; 
		genny.setSeed(System.currentTimeMillis());
		speed = 2;
		direction = genny.nextInt(NUM_OF_DIR);
		previous_direction = 0;
		state = SMART;
		load_faces(color);
		face_count = 0;
	}

	@Override
	public void tick() { //movement
		if(state == 0){
			random_movement();
		}else {
			smart_movement();
		}
		
		if(timer > 0){
			timer--;
		}else if(timer == 0){
			if(canHurt == true){
				canHurt = false;
				hurt_picture = false;
			}else {
				hurt_picture = false;
			}
		}
	}
	
	
	/**
	 * Purpose: Facilitate smart tracking by ghost
	 */
	public void smart_movement(){
		boolean movement_made = false;
		
		if(state == SMART){
			if(this.x < target.x && validate_next_move(this.x + speed, this.y)){
				this.x += speed;
				movement_made = true;
				previous_direction = RIGHT;
			}
			
			if(this.x > target.x && validate_next_move(this.x - speed, this.y)){
				this.x -= speed;
				movement_made = true;
				previous_direction = LEFT;
			}
			
			if(this.y < target.y && validate_next_move(this.x, this.y + speed)){
				this.y += speed;
				movement_made = true;
				previous_direction = DOWN;
			}
			
			if(this.y > target.y && validate_next_move(this.x, this.y - speed)){
				this.y -= speed;
				movement_made = true;
				previous_direction = UP;
			}
			
			if(!movement_made){ 
				state = HIGHLANDER; //THERE CAN ONLY BE ONE...
			}
		}else if (state == HIGHLANDER){
			highlander();
		}
		
		time++;
		if(time >= switch_time * 1.5){ //so they are smart longer
			state = DUMB; //random
			time = 0;
		}
	}
	
	
	/**
	 * Used when the ghost is stuck and needs to find a new way
	 */
	private void highlander(){
		if(previous_direction == RIGHT){
			if(this.y < target.y){  
				if(validate_next_move(this.x, this.y + speed)){
					this.y += speed;
					state = SMART;
				}
			}else {
				if(validate_next_move(this.x, this.y - speed)){
					this.y -= speed;
					state = SMART;
				}
			}
			
			if(validate_next_move(this.x + speed, this.y)){
				this.x += speed;
			}else {
				previous_direction = LEFT;
			}
		}else if (previous_direction == LEFT){
			if(this.y < target.y){ 
				if(validate_next_move(this.x, this.y + speed)){
					this.y += speed;
					state = SMART;
				}
			}else {
				if(validate_next_move(this.x, this.y - speed)){
					this.y -= speed;
					state = SMART;
				}
			}
			
			if(validate_next_move(this.x - speed, this.y)){
				this.x -= speed;
			}else {
				previous_direction = RIGHT;
			}
		}else if(previous_direction == UP){
			if(this.x < target.x){ 
				if(validate_next_move(this.x + speed, this.y)){
					this.x += speed;
					state = SMART;
				}
			}else {
				if(validate_next_move(this.x - speed, this.y)){
					this.x -= speed;
					state = SMART;
				}
			}
			
			if(validate_next_move(this.x, this.y - speed)){
				this.y += speed;
			}else {
				previous_direction = DOWN;
			}
		}else {
			if(this.x < target.x){ 
				if(validate_next_move(this.x + speed, this.y)){
					this.x += speed;
					state = SMART;
				}
			}else {
				if(validate_next_move(this.x - speed, this.y)){
					this.x -= speed;
					state = SMART;
				}
			}
			
			if(validate_next_move(this.x, this.y - speed)){
				this.y -= speed;
			}else {
				previous_direction = UP;
			}
		}
	}
	
	
	/**
	 * Purpose: Randomly move the ghost
	 */
	public void random_movement(){
		switch(direction){
		case UP:
			if(validate_next_move(x, y - speed)){
				y -= speed;
			}else {
				direction = genny.nextInt(NUM_OF_DIR);
			}
			break;
		case DOWN:
			if(validate_next_move(x, y + speed) == true){
				y += speed;
			}else {
				direction = genny.nextInt(NUM_OF_DIR);
			}
			break;
		case RIGHT:
			if(validate_next_move(x + speed, y)){
				x += speed;
			}else {
				direction = genny.nextInt(NUM_OF_DIR);
			}
			break;
		case LEFT:
			if(validate_next_move(x - speed, y)){
				x -= speed;
			}else {
				direction = genny.nextInt(NUM_OF_DIR);
			}
			break;
		default: 
			System.out.println("In default");
		}
		time++;
		if(time >= switch_time){
			state = 1; 
			time = 0;
		}
	}
	
	@Override
	protected boolean validate_next_move(int next_x, int next_y){
		Rectangle next_space = new Rectangle(next_x, next_y, width, height);
		Map temp_map = Game.map;
		
		for(int w = 0; w < temp_map.cells.length; w++){
			for(int h = 0; h < temp_map.cells[0].length; h++){
				if(temp_map.cells[w][h] != null && next_space.intersects(temp_map.cells[w][h])){
					return false;
				}
			}
		}
		return true;
	}
	
	
	@Override
	public void render(Graphics g) {
		if(face_count == 10){
			face_count = 0;
		}
		
		if(hurt_picture){
			ghost = hurt;
		}else {
			switch(direction){
			case UP:
				ghost = faces[0][face_count];
				face_count++;
				break;
			case DOWN:
				ghost = faces[0][face_count];
				face_count++;
				break;
			case LEFT:
				ghost = faces[0][face_count];
				face_count++;
				break;
			case RIGHT:
				ghost = faces[0][face_count];
				face_count++;
				break;
				default:
					ghost = faces[1][face_count];
			}
		}
		g.drawImage(ghost, x, y, 32 , 32, null);
	}

	
	public void set_target(Point p){
		target = p;
	}

	
	/**
	 * Purpose: Load images
	 * @param color String object that is the color in all lowercase letters
	 */
	private void load_faces(String color) {
		faces = new BufferedImage[ROWS][COLUMNS];
		try{
			hurt = ImageIO.read(new File("resources/ghost/blue/hurt.png"));
			all_faces = ImageIO.read(new File("resources/ghost/blue/Ghosttest.png"));
			for(int i = 0; i < ROWS; i++){
				for(int j = 0; j < COLUMNS; j++){
					faces[i][j] = all_faces.getSubimage(j * 200, i * 200, 200, 200);
				}
			}
		}catch (IOException e) {
			System.out.println("Ghost failed to load");
			e.printStackTrace();
		}
	}
	
	
	public void set_hurt(boolean value){
		timer = 300;
		canHurt = value;
	}
	
	public void set_hurt_pic(boolean value){
		hurt_picture = value;
		timer = 300;
	}
	
	
	public boolean get_hurt_value(){
		return canHurt;
	}
}
