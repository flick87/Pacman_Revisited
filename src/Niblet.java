import java.awt.Color;
import java.awt.Graphics;

public class Niblet extends Item {

	private static final long serialVersionUID = 1L;
	private boolean is_eaten;
	private static int green_value, red_value, blue_value = 0;
	
	/**
	 * Purpose: Constructor
	 * @param x Integer type. X-Location
	 * @param y Integer type. Y-Location
	 */
	public Niblet(int x, int y){
		super(x, y);
		super.setBounds(x, y, 1, 1); 
		is_eaten = false;
	}
	
	/**
	 * Purpose: Constructor
	 * @param x_location Integer type. 
	 * @param y_location Integer type. 
	 * @param new_points Integer type.
	 */
	public Niblet(int x_location, int y_location, int new_points){
		super(x_location, y_location);
		super.set_points(new_points);
		is_eaten = false;
		green_value = 155;
	}
	
	/**
	 * Purpose: Set the RGB private member variables that are static across all Niblets. 
	 * 			These variables are static so that all Niblets change color together.
	 * @param red Integer type.
	 * @param green Integer type.
	 * @param blue Integer type.
	 */
	@SuppressWarnings("static-access")
	public void set_RGB(int red, int green, int blue){
		this.red_value = red;
		this.green_value = green;
		this.blue_value = blue;
	}
	
	/**
	 * Purpose: To check and see if a Niblet has been eaten
	 * @return True if the Niblet has been eaten. Otherwise, false.
	 */
	public boolean is_eaten(){
		if(is_eaten){
			return true;
		}
		return false;
	}
	
	/**
	 * Purpose: Set the status of the Niblet to eaten.
	 */
	public void eat(){
		is_eaten = true;
	}
	
	@Override
	public void render(Graphics g) {
		if(!is_eaten){
			g.setColor(new Color(red_value, green_value, blue_value));
			g.fillRect(x + 12, y + 12, 4, 4);
		}
	}

	@Override
	public void tick() {
		
	}
}
