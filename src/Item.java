import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Item extends Rectangle{

	private static final long serialVersionUID = 1L;
	protected int points;
	protected boolean is_eaten;
	
	/**
	 * Purpose: Constructor
	 * @param x_location Integer Type. Sets the x-location of the Item
	 * @param y_location Integer Type. Sets the y-location of the Item
	 */
	public Item(int x_location, int y_location){
		this.setLocation(x_location, y_location);
		this.setSize(32, 32);
		is_eaten = false;
	}
	
	/**
	 * Purpose: Sets the item status to eaten
	 */
	public void eat(){
		is_eaten = true;
	}
	
	/**
	 * Purpose: To get the point value of the Item
	 * @return Integer type representing the point value of this object
	 */
	public int get_points(){
		return points;
	}

	/**
	 * Purpose: To set the point value of this object
	 * @param points Integer Type. 
	 */
	public void set_points(int points) {
		this.points = points;
		
	}

	public abstract void render(Graphics g); //This must be implemented to render the object
	public abstract void tick(); //Depending upon item this may not be functional

}
