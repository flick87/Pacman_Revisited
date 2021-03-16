import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Cell extends Rectangle {

	private static final long serialVersionUID = 1L;
	private int size = 32;

	/**
	 * Constructor
	 * @param x Integer type used to specify the x-coordinate of the top left point of the cell
	 * @param y Integer type used to specify the y-coordinate of the top left point of the cell
	 */
	public Cell(int x, int y){
		setBounds(x,y,size,size);
	}
	
	/**
	 * Purpose: Constructor
	 * @param x Integer type used to specify the x-coordinate of the top left point of the cell
	 * @param y Integer type used to specify the y-coordinate of the top left point of the cell
	 * @param width Positive integer that sets the width of the cell
	 * @param height Positive integer that sets the height of the cell
	 */
	public Cell(int x, int y, int width, int height){
		setBounds(x,y,width,height);
	}
	
	/**
	 * Constructor
	 * @param other Cell type. Bounds will be copied over.
	 */
	public Cell(Cell other){
		this.setBounds(other.x, other.y, other.width, other.height);
	}
	
	/**
	 * Purpose: To draw the Cell to the canvas or frame
	 * @param g Graphics object
	 */
	public void render(Graphics g){
		g.setColor(Color.BLUE);
		g.fillRect(x, y, width, height);
	}
	
	/**
	 * Purpose: Set the size member variable of the rectangle 
	 * @param size positive integer
	 */
	public void set_size(int size){
		this.size = size;
	}

}
