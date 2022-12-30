/* Names: Nicole Han, Tracy Wang, Roseanna Liang
 * Teacher: Ms. Krasteva
 * Due Date: June 18, 2020
 * Class: Ball class
 * 
 * This class is the basketball that is used during the basketball minigame.
 * 
 * Instance Variable Dictionary:
 * type      |name        |purpose
 * ----------|------------|-------
 * String    |direction   |stores the direction of the ball
 * int       |x           |stores the x coordinate
 * int       |y           |stores the y coordinate
 * String    |state       |stores the state of the ball (such as "forward")
 * int       |spin        |stores which spin stage the ball is in
 * long[]    |delay       |stores the delay
 * int       |size        |stores the size of the ball
 * int       |opacity     |stores the opacity of the ball
 */

public class Ball {
  private String direction; 
  private int x;  
  private int y;  
  private String state;  
  private int spin;  
  private long[] delay;  
  private int size; 
  private int opacity;  

  /* class constructor
   * 
   * Purpose:
   * Reads in all image files, initializes all instance variables. 
   */
  public Ball(String direction) {
    this.direction = direction;
    state = "forward";
    x = 328;
    y = 404;
    size = 100;
    spin = 0;
    opacity = 255;
    delay = new long[3];
    for(int i=0; i<3; i++) delay[i] = System.nanoTime();
  }
  
  /*getOpacity method
   * 
   * Purpose:
   * Returns the current opacity of the ball
   */
  public int getOpacity() {
    if(System.nanoTime()-delay[0] >= 15000000) {//changes opacity based on system time
      delay[0] = System.nanoTime();
      if(state.equals("fade")) opacity -= 60;
    }
    return opacity;
  }
  
  /*getSize method
   * 
   * Purpose:
   * Returns the size of the ball
   */
  public int getSize() {
    if(System.nanoTime()-delay[1] >= 50000000) {//changes size based on system time
      delay[1] = System.nanoTime();
      size -= 2;
    }
    return size;
  }
  
  /*getSpin method
   * 
   * Purpose:
   * Returns the spin position of the ball
   */
  public int getSpin() {
    if(System.nanoTime()-delay[2] >= 200000000) {//changes spin based on system time
      delay[2] = System.nanoTime();
      spin++;
      spin %= 3;
    }
    return spin;
  }
  
  /*getDirection method
   * 
   * Purpose:
   * Returns the direction
   */
  public String getDirection() {
    return direction;
  }
  
  /*setState method
   * 
   * Purpose:
   * Sets the state
   */
  public void setState(String state) {
    this.state = state;
  }
  
  /*getState method
   * 
   * Purpose:
   * Returns the current state of the ball
   */
  public String getState() {
    return state;
  }
  
  /*setCoords method
   * 
   * Purpose:
   * Set the ball coordinates
   */
  public void setCoords(int x, int y) {
    this.x = x;
    this.y = y;
    if(this.y <= 40) state = "down";
    else if(state.equals("down") && this.y >= 180) state = "fade"; //sets to fade if ball beneath certain height
  }
  
  /*getX method
   * 
   * Purpose:
   * Returns the x coordinate
   */
  public int getX() {
    return x;
  }
  
  /*getY method
   * 
   * Purpose:
   * Returns the y coordinate
   */
  public int getY() {
    return y;
  }
}