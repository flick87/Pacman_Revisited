
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Game extends Canvas implements Runnable, KeyListener {

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 640; 
	public static final int HEIGHT = 480;
	public final static String TITLE = "Pacman Alpha";
	private boolean is_running = false;
	private long points; 
	private Pacman player;
	public static Map map;
	private Ghost pinky;
	private Thread thread;
	private int map_number; //Determines which map will be selected when next level is required
	private int lives;
	
	
	/**
	* Purpose: Default Constructor
	*/
	public Game(){
		Dimension dimension = new Dimension(Game.WIDTH, Game.HEIGHT); //encapsulates width and height of a component in an object
		setPreferredSize(dimension);
		setMinimumSize(dimension);
		setMaximumSize(dimension);
		map = new Map("resources/map/map" + map_number + ".png");
		player = new Pacman(map.get_pac_init_location().x, map.get_pac_init_location().y); //middle of screen
		pinky = new Ghost(map.get_ghosts_init_locations().get(0).x, map.get_ghosts_init_locations().get(0).y, "blue"); //"blue" designates image
		pinky.set_target(player.get_location()); 
		addKeyListener(this);
		points = 0;
		map_number = 1;
		lives = 3;
	}
	
	
	/**
	 * Purpose: To add points when Pacman eats an object with value
	 * @param p Integer Type. 
	 */
	public void add_points(int p){
		points += p;
	}
	
	
	/**
	 * @return Long Type. Value of points the player has acquired.
	 */
	public long get_points(){
		return points;
	}
	
	
	/**
	 * Purpose: To start an instance thread of this class if it is not already running
	 */
	public synchronized void start(){
		if(is_running){
			return;
		}
		is_running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	
	/**
	 * Purpose: Stops the instance thread if it is running
	 */
	public synchronized void stop(){
		if(!is_running){
			return;
		}
		is_running = false;
		try{
			thread.join(); 
		}catch (InterruptedException e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Purpose: To check if the instance thread is running
	 * @return True if thread is running. Otherwise, false.
	 */
	public boolean isRunning(){
		if(is_running){
			return true;
		}
		return false;
	}
	
	
	/**
	 * Purpose: updates the movement of the player to screen
	 */
	private void tick(){
		player.tick();
		pinky.set_target(player.get_location());
		pinky.tick();
		
		check_niblets(); //need to change this for one unit collision
		check_pows();
		check_pinky();
		check_power_time(pinky);
		
		//logic below will not work in it's own method and I can't figure out why
		for(int i = 0; i < map.get_pows().size(); i++){
			if(player.intersects(map.get_pows().get(i))){
				points += map.get_pows().get(i).get_points();
				map.get_pows().remove(i);
				player.set_power();
				break;
			}
		}	
		if(map.get_niblets().size() == 0 && map.get_pows().size() == 0){
			map = new Map("resources/map/map" + map_number + ".png");
			map_number++;
			player.setLocation(map.get_pac_init_location());
			pinky.setLocation(map.get_ghosts_init_locations().get(0));
		}
	}
	
	
	/**
	 * Purpose: Checks to see if Pacman has any lives. If not, game will exit. Option will be added later to restart or exit.
	 */
	private void check_lives(){
		if(lives < 1){
			JOptionPane.showMessageDialog(new JFrame(),
				    "You have no more lives...");
			System.exit(1);
		}
	}
	
	
	/**
	 * Purpose: Checks to see if it is time to let Pinky out of isolation. If so, it places Pinky back to starting position. This will
	 * 			be modified later with an added parameter of 
	 */
	private void check_power_time(Ghost ghost){
		if(ghost.getLocation().x == 0 && ghost.getLocation().y == 0 && ghost.get_hurt_value() == false){
			ghost.setLocation(map.get_ghosts_init_locations().get(0));
		}
	}
	
	
	/**
	 * Purpose: Checks if player intersects with Pinky. If so, then a life will be taken and so on.
	 */
	private void check_pinky(){
		if(player.intersects(pinky) && player.get_power() == false){
			player = new Pacman(map.get_pac_init_location().x,map.get_pac_init_location().y);
			pinky.setLocation(map.get_ghosts_init_locations().get(0));
			lives--;
			check_lives();
		}else if(player.intersects(pinky) && player.get_power() == true){
			pinky.set_hurt(true);
			pinky.setLocation(0, 0);
		}
	}
	
	
	/**
	 * 
	 */
	private void check_pows(){
		for(int i = 0; i < map.get_pows().size(); i++){
			if(player.intersects(map.get_pows().get(i))){
				points += map.get_pows().get(i).get_points();
				map.get_pows().remove(i);
				player.set_power();
				pinky.set_hurt_pic(true);
				break;
			}
		}
	}
	
	
	/**
	 * Purpose: To check and see if the player intersects any Niblets.
	 */
	private void check_niblets(){
		for(int i = 0; i < map.get_niblets().size(); i++){ //array is small enough that this should not matter
			if(player.intersects(map.get_niblets().get(i))){
				points += map.get_niblets().get(i).get_points();
				map.get_niblets().remove(i);
				break;
			}
		}
	}
	
	
	/**
	 * Purpose: Renders graphics of the Canvas
	 */
	private void render(){
		BufferStrategy bs = getBufferStrategy(); //This is where the "drawings" are stored for JFrame to use
		if(bs == null){
			createBufferStrategy(3); //reduces tearing, can use 2 as well. Don't go too high
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		map.render(g); 
		g.setColor(Color.YELLOW);
		g.drawString("Score: " + String.valueOf(points), 50, 25 ); 
		g.drawString("Lives: " + lives, Game.WIDTH - 60, 25); 
		
		pinky.render(g);
		player.render(g);
		
		g.setColor(Color.WHITE);
		
		g.dispose();
		bs.show();
	}
	
	
	/**
	 * Purpose: This method provides an entry point for the thread and you will put your complete 
	 * 			business logic inside this method. Following is a simple syntax of the run() method
	 */
	@Override
	public void run() { 
		requestFocus(); //This method makes it to where when you start 
		final int second = 1000; //milliseconds in one second
		@SuppressWarnings("unused")
		int fps = 0; 
		double timer = System.currentTimeMillis();
		long previous_time = System.nanoTime(); //Returns current time in nanoseconds of the JVM time
		double target_fps = 60;
		double change = 0;
		double nanoseconds = 1000000000 / target_fps; //returns time in nanoseconds
		long current_time = 0;
		
		while(is_running){
			current_time = System.nanoTime();
			change += (current_time - previous_time) / nanoseconds;
			previous_time = current_time;
			while(change >= 1){
				tick();
				render(); //if tick and render get dropped below out of loop then this will not
						  //not be synchronized
				fps++;
				change--;
			}
			if(System.currentTimeMillis() - timer >= second){
//				System.out.println("FPS: " + fps);
				fps = 0;
				timer += second;
			}
		}
		stop();
	}
	

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@SuppressWarnings("static-access")
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT){ 
			if((int)player.getX() > WIDTH){
				player.setLocation(0, player.y);
			}
			player.move_right = true;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT){
			if((int)player.getX() < 0){
				player.setLocation(WIDTH, player.y);
			}
			player.move_left = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_UP){
			if((int)player.getY() < 0){
				player.setLocation(player.x, HEIGHT);
			}
			player.move_up = true;
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN){
			if((int)player.getY() > HEIGHT){
				player.setLocation(player.x, 0);
			}
			player.move_down = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_T){
			map.get_niblets().clear();
			map.get_pows().clear();
		}else if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
			System.exit(1);
		}else if(e.getKeyChar() == KeyEvent.VK_0){
			map.get_niblets().clear();
			map.get_pows().clear();
		}else if(e.KEY_PRESSED == KeyEvent.VK_0 && e.KEY_PRESSED == KeyEvent.VK_2 && e.KEY_PRESSED == KeyEvent.VK_4 ){
			map.get_niblets().clear();
			map.get_pows().clear();
		}
	}


	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT){
			player.move_right = false;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT){
			player.move_left = false;
		}else if(e.getKeyCode() == KeyEvent.VK_UP){
			player.move_up = false;
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN){
			player.move_down = false;
		}
	}
	
	
	/**
	 * Purpose: Return the location of the player object 
	 * @return Point Object
	 */
	public Point get_player_location(){
		return player.getLocation();
	}
	
	
	public static void main(String[] args) {
		
		Game game = new Game();
		JFrame frame = new JFrame();
		frame.setTitle(Game.TITLE);
		frame.add(game);
		frame.setResizable(false);
		frame.pack(); //sets the size of the frame so that all contents are >= to preferred sizes
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null); //sets frame to center of screen
		frame.setVisible(true); 
		game.start();
	}
	
//Not ready for implementation.
//	private void check_completed_level(){
//		if(map.get_niblets().size() == 0 && map.get_pows().size() == 0){
//			map = new Map("resources/map/map" + map_number + ".png");
//			map_number++;
//			player.setLocation(map.get_pac_init_location());
//			pinky.setLocation(map.get_ghosts_init_locations().get(0));
//		}
//	}

}
