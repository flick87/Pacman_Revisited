import java.awt.Color;
import java.awt.Graphics;

public class PowerPellet extends Item {

	private static final long serialVersionUID = 1L;
	private boolean is_eaten;
	private static int red, green, blue;

	
	/**
	 * Purpose: Constructor
	 * @param x_location Float type
	 * @param y_location Float type
	 */
	public PowerPellet(int x_location, int y_location) {
		super(x_location, y_location);
		points = 1000;
		red = 0;
		green = 0;
		blue = 0;
	}
	
	
	/**
	 * Purpose: Constructor
	 * @param x_location Float type
	 * @param y_location Float type
	 * @param new_points Integer type. This parameter sets the value of the PowerPellet object in terms of points
	 */
	public PowerPellet(int x_location, int y_location, int new_points){
		super(x_location, y_location);
		super.set_points(new_points);
		is_eaten = false;
		green = 0;
	}
	
	
	/**
	 * Purpose: Returns points from object as an integer
	 */
	public int get_points(){
		return points;
	}
	
	
	/**
	 * Purpose: Sets the RGB values of the PowerPellet
	 * @param red Integer type red value
	 * @param green Integer type green value
	 * @param blue Integer type blue value
	 */
	@SuppressWarnings("static-access")
	public void set_RGB(int red, int green, int blue){
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
	
	
	/**
	 * Purpose: Returns the state of the object
	 * @return True if the object has been eaten. Otherwise, false.
	 */
	public boolean is_eaten(){
		if(is_eaten){
			return true;
		}
		return false;
	}
	
	
	/**
	 * Purpose: "Eats" the PowerPellet object by setting it's boolean value for is_eaten to true
	 */
	public void eat(){
		is_eaten = true;
	}

	
	/**
	 * Purpose: Render graphic of object
	 */
	@Override
	public void render(Graphics g) {
		g.setColor(new Color(green, blue, red)); //reversed where colors should be for cool effect
		g.fillOval(x + 5, y + 5, 20, 20);
	}

	
	@Override
	public void tick() {
		
		
	}

}
