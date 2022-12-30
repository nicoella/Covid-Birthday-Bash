/* Names: Nicole Han, Tracy Wang, Roseanna Liang
 * Teacher: Ms. Krasteva
 * Due Date: June 18, 2020
 * Class: Basketball class
 * 
 * This class is the basketball game class.
 * 
 * Instance Variable Dictionary:
 * type            |name        |purpose
 * ----------------|------------|-------
 * Image           |bg          |stores the background image
 * Image[]         |ball        |stores all the ball images
 * Image           |bar         |stores the image of the bar
 * Image           |net         |stores the net image
 * int             |barY        |stores the current location of the bar
 * boolean         |barUp       |whether or not the bar is up
 * long            |countdown   |the countdown in nanoseconds
 * String          |state       |the state of the game
 * ArrayList<Ball> |balls       |the balls in the game
 * long            |delay       |stores the delay
 * long            |gameTime    |stores the game time
 * int             |score       |stores the score
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;
import java.util.*;

public class Basketball extends Scene {
  private Image bg; 
  private Image[] ball; 
  private Image bar; 
  private Image net; 
  private int barY;  
  private boolean barUp;  
  private long countdown; 
  private String state; 
  private ArrayList<Ball> balls; 
  private long delay;  
  private long gameTime; 
  private int score;  
  
  /* class constructor
   * 
   * Purpose:
   * Reads in all image files, initializes all instance variables.
   * Creates a key listener which has a method keyPressed which
   * is called when a key is pressed.   
   */
  public Basketball() {
    super(); //calls the constructor of the Scene class
    reset(); //reset method initializes variables which need to be reset every time
    ball = new Image[3];
    try { //try-catch block catches errors
      //import all image files
      bg = ImageIO.read(new File("data/backgrounds/minigames/basketball/basketballArcade.png")); 
      for(int i=0; i<3; i++) ball[i] = ImageIO.read(new File("data/backgrounds/minigames/basketball/ball"+i+".png"));
      bar = ImageIO.read(new File("data/backgrounds/minigames/basketball/movingbar.png")); 
      net = ImageIO.read(new File("data/backgrounds/minigames/basketball/net.png"));
    } catch (IOException e) {}
    //adds a listener which will be called each time the user presses a key
    addKeyListener(new KeyAdapter() {
      //keyPressed method is called whenever a key is pressed
      public void keyPressed(KeyEvent e) {
        //user can only throw a ball if the game has started and the 1s delay between ball throws is met
        if(state.equals("game") && System.nanoTime()-delay >= 1000000000 && e.getKeyChar()==' ') { 
          if(barY >= 276 && barY <= 304) { //bar was on the red area
            balls.add(new Ball("center")); //ball has 100% accuracy
          } else if(barY >= 200 && barY <= 380) { //bar was on the green area
            int rand = (int)(Math.random()*100)+1;
            //ball has 25% accuracy
            if(rand<=25) balls.add(new Ball("center")); //ball will go into the net
            else if(rand<=63) balls.add(new Ball("left")); //ball will not go into the net
            else balls.add(new Ball("right")); //ball will not go into the net
          } else { //bar was in the blue area
            //ball has a 5% accuracy
            int rand = (int)(Math.random()*100)+1;
            if(rand<=5) balls.add(new Ball("center")); //ball will go into the net
            else if(rand<=52) balls.add(new Ball("left")); //ball will not go into the net
            else balls.add(new Ball("right")); //ball will not go into the net
          }
          if(balls.get(balls.size()-1).getDirection().equals("center")) score++; //increases user's total score if ball goes in the net
          delay = System.nanoTime(); //resets the 1 second delay between shots
        }
      }
    });
  }
  
  /*reset method
   * 
   * Purpose:
   * resets all the instance variables for the game to begin again
   */
  public void reset() {
    state = "instructions";
    barY = 36;
    barUp = false;
    countdown = -1;
    delay = -1;
    gameTime = -1;
    score = 0;
    balls = new ArrayList<>();
  }
  
  /*getRank method
   * 
   * Purpose:
   * Calculates and returns the user's "rank" out of the 10 children at the party.
   */
  public int getRank() {
    int[] ranks = {35,32,25,23,22,21,16,15,10,5}; //stores the scores of the other children
    int cnt = 1; //stores the user's rank
    for(int i=0; i<ranks.length; i++) {
      if(ranks[i] >= score) cnt++; //decreases the user's rank if the other children scored higher
    }
    return cnt;
  }
  
  /* paintComponent method
   * 
   * purpose:
   * This methods performs the graphics of the game.
   */
  public void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D)g;
    super.paintComponent(g);
    //draw the background
    g2d.drawImage(bg,0,0,null);
    //draw the moving bar
    g2d.drawImage(bar,772,barY,null);
    //animate the moving bar
    if(barUp) barY -= 10;
    else barY += 10;
    if(barY >= 556) barUp = true;
    else if(barY <= 36) barUp = false;
    //draw the ball after a 1 second delay between shots
    if(System.nanoTime()-delay >= 1000000000) g2d.drawImage(ball[0],328, 404, null);
    //draw balls being thrown
    ArrayList<Ball> newList = new ArrayList<>();
    for(Ball i:balls) {
      //gets values of the ball
      int spin = i.getSpin();
      int x = i.getX();
      int y = i.getY();
      String s = i.getState();
      int size = i.getSize();
      int opacity = i.getOpacity();
      //draw balls being thrown
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,Math.max(0,(float)(opacity/255.0))));
      g2d.drawImage(ball[spin].getScaledInstance(size, size, java.awt.Image.SCALE_SMOOTH),x+(100-size)/2,y+(100-size)/2,null);
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1));
      String dir = i.getDirection();
      //calculate new position based on direction and opacity
      if(dir.equals("center")) {
        if(s.equals("forward")) i.setCoords(x,y-10);
        else if(s.equals("down") || s.equals("fade")) i.setCoords(x,y+4);
      } else if(dir.equals("left")) {
        if(s.equals("forward")) i.setCoords(x-2,y-10);
        else if(s.equals("down") || s.equals("fade")) i.setCoords(x-2,y+4);
      } else {
        if(s.equals("forward")) i.setCoords(x+2,y-10);
        else if(s.equals("down") || s.equals("fade")) i.setCoords(x+2,y+4);
      }
      if(opacity>0) newList.add(i);
      if(s.equals("down") || s.equals("fade")) g2d.drawImage(net,316,140,null);
    }
    //updates list after removing balls which have faded out
    balls = newList;
    //calculate time
    long timePassed = gameTime-System.nanoTime();
    long seconds = timePassed/1000000000;
    long minutes = seconds / 60;
    seconds %= 60;
    String secondsStr = seconds+""; //creates the seconds string
    if(secondsStr.length()<=1) secondsStr = "0"+secondsStr;
    String minutesStr = minutes+""; //creates the minutes string
    if(minutesStr.length()<=1) minutesStr = "0"+minutesStr;
    if(gameTime==-1) {
      minutesStr = "01";
      secondsStr = "00";
    }
    //draw timer
    g2d.setFont(getFont().deriveFont(50.0f));
    g2d.setColor(Color.WHITE);
    g2d.drawString(minutesStr+":"+secondsStr,290-g2d.getFontMetrics().stringWidth(minutesStr+":"+secondsStr)/2,30);
    //draw score
    g2d.drawString(score+"",462-g2d.getFontMetrics().stringWidth(score+"")/2,30);
    if(state.equals("instructions")) { //display instructions for the game
      requestFocus();
      //draw text on text box
      String str = "Time to play arcade basketball! Press SPACE to shoot the ball. If the bar is over red, the ball has a 100% chance of getting in. If the bar is over green, the ball has a 25% chance of getting in. If the bar is over blue, the ball has a 5% chance of getting in. Try to score the most balls in 60 seconds!";
      displayText(str,g2d);
      //change state when instructions are finished
      if(getTextFinished()) {
        state = "countdown";
        countdown = System.nanoTime() + 4*1000000000L;
        clearText();
      }
    } else if(state.equals("countdown")) { //display 3 second countdown for the game
      g2d.setFont(getFont().deriveFont(80.0f));
      g2d.setColor(Color.WHITE);
      long secs = (countdown - System.nanoTime())/1000000000;
      //draw the countdown
      g2d.drawString(secs+"",500-g2d.getFontMetrics().stringWidth(secs+"")/2,280);
      //change state when countdown is finished 
      if(secs <= 0) {
        state = "game"; 
        gameTime = System.nanoTime()+61*1000000000L;
      }
    } else if(state.equals("game")) { //when the game is actually occurring
      //change state when the game timer runs out
      if(gameTime - System.nanoTime() <= 0) {
        state = "end";
        gameTime = -1;
        setFadeOutCnt(0);
      }
    }
    //draw scene transitions
    if(getFadeInCnt()>0) {
      fadeIn(g2d);
      reset();
    }
    if(getFadeOutCnt()>=0 && getFadeOutCnt()<255) fadeOut(g2d);
    else if(getFadeOutCnt()!=-1) transition(6,5);
  }
  
  /*actionPerformed method
   * 
   * Purpose:
   * repeats the paintComponent method
   */
  public void actionPerformed(ActionEvent e) {
    repaint();
  }
}