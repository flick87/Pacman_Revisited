
import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Character extends Rectangle{
	
	private static final long serialVersionUID = 1L;
	protected int speed = 5, width = 29, height = 29;
	
	
	/**
	 * Purpose: Check the next move of the Character to make sure the move is valid.
	 * @param next_x Integer Type. Must be positive.
	 * @param next_yInteger Type. Must be positive.
	 * @return Boolean Value. If next move is valid return true. Otherwise, false.
	 */
	protected abstract boolean validate_next_move(int next_x, int next_y);
	
	/**
	 * Constructor
	 * @param x Integer type used to specify the x-coordinate of the top left point of the cell
	 * @param y Integer type used to specify the y-coordinate of the top left point of the cell
	 */
	public Character(int x, int y){
		setBounds(x,y,width,height); //x,y == bounds and 32 is width and height
	}
	
	public abstract void tick(); //Update method for the Character's subclasses
	public abstract void render(Graphics g); //Drawing method 
}
