/* Names: Nicole Han, Tracy Wang, Roseanna Liang
 * Teacher: Ms. Krasteva
 * Due Date: June 18, 2020
 * Class: CandyBobbing class
 *
 * This is the candy bobbing game class.
 *
 * Instance Variable Dictionary:
 * type            |name        |purpose
 * ----------------|------------|-------
 * Image           |bg          |stores the background image
 * Image           |box         |stores the box to display the timer
 * Image           |water       |stores the image of the water
 * Image[]         |candies     |stores the images of the candies
 * Point[]         |candiesPos  |stores the center coordinates of the candies
 * boolean[]       |candiesFound|stores which candies have been found
 * long            |countdown   |stores the nanotime for the countdown
 * long            |timer       |stores the nanotime for the game timer
 * int             |numFound    |stores which candies have been found
 * String          |state       |stores the current state of the game
 * boolean         |hasReset    |stores if the game board has been reset at the beginning of the game or not
 *
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;
import java.util.*;

public class CandyBobbing extends Scene {
  private Image bg;
  private Image box;
  private Image water;
  private Image candies[];
  private Point candiesPos[];
  private boolean candiesFound[];
  private long countdown;
  private long timer;
  private int numFound;
  private String state;
  private boolean hasReset;

  /* class constructor
   *
   * Purpose:
   * Reads in all image files, initializes all instance variables.
   * Creates a mouse listener which has a method mouseClicked which
   * is called when the mouse is clicked.
   */
  public CandyBobbing() {
    super(); //calls the constructor of the Scene class
    reset(); //reset method initializes variables which need to be reset every time
    hasReset = false;
    candies = new Image[8];
    try {
      //imports all images
      bg = ImageIO.read(new File("data/backgrounds/minigames/candy/candyBg.png"));
      water = ImageIO.read(new File("data/backgrounds/minigames/candy/water.png"));
      for(int i = 0; i < 8; i++){
        candies[i] = ImageIO.read(new File("data/backgrounds/minigames/candy/candy" + i + ".png"));
      }
      box = ImageIO.read(new File("data/backgrounds/levels/box.png"));
    } catch (IOException e) {}
    //adds a listener which will be called each time the user presses or releases a key
    addMouseListener(new MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        //only allows candies to be found when the game has started
        if(state.equals("game")){
          //checks if the area pressed is on a candy
          for(int i = 0; i < candiesPos.length; i++) {
            //if statement checks if candy has been found and was not found before
            if(candiesFound[i] == false && e.getPoint().distance(candiesPos[i]) <= 30){
              numFound++; //increases the number of candies found
              candiesFound[i] = true; //sets the current candy found to be true
            }
          }
          //all candies have been found, start fade out animation
          if(numFound == candies.length && getFadeOutCnt()==-1){
            setFadeOutCnt(0);
          }
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
    candiesPos = new Point[8];
    candiesFound = new boolean[8];
    countdown = -1;
    timer = -1;
    numFound = 0;
    state = "instructions";
    //calculate candy coordinates
    for(int i = 0; i < candiesPos.length; i++){
      candiesPos[i] = new Point((int) (Math.random() * (852-144+1)) + 144, (int) (Math.random() * (304-144+1)) + 144);
      for(int j = 0; j < i; j++){
        while(candiesPos[i].distance(candiesPos[j]) <= 100){
          candiesPos[i] = new Point((int) (Math.random() * (852-144+1)) + 144, (int) (Math.random() * (304-144+1)) + 144);
        }
      }
    }
  }

  /*getRank method
   *
   * Purpose:
   * Calculates and returns the user's "rank" out of the 10 children at the party.
   */
  public int getRank() {
    int[] ranks = {6,5,4,3,2,2,2,2,1}; //stores the scores of the other children
    int cnt = 1; //stores the user's rank
    for(int i=0; i<ranks.length; i++) {
      if(ranks[i] >= numFound) cnt++; //decreases the user's rank if other children scored higher
    }
    return cnt;
  }

  /* paintComponent method
   *
   * purpose:
   * This methods performs the graphics of the game.
   */
  public void paintComponent(Graphics g) {
    //reset the instance variables if they haven't been reset
    if(!hasReset) {
      reset();
      hasReset = true;
    }
    Graphics2D g2d = (Graphics2D)g;
    super.paintComponent(g);

    //draw the background
    g2d.drawImage(bg,0,0,null);

    //draw timer
    g2d.drawImage(box,880,12,null);
    g2d.setFont(getFont());
    g2d.setColor(new Color(184,105,113));
    g2d.drawString("00:00",900,48);

    //draw number of candies found
    g2d.drawImage(box,880,529,null);
    g2d.setFont(getFont());
    g2d.setColor(new Color(184,105,113));
    g.drawString(""+numFound+"/8",910,564);

    //draw the candies
    for(int i = 0; i < candies.length; i++){
      g2d.drawImage(candies[i], (int)candiesPos[i].getX()-20, (int)candiesPos[i].getY()-20, null);
    }
    if(state.equals("instructions")) { //display the game instructions
      //draw text on text box
      String str = "It's time to make like a duck and bob for candies! Look closely at the candies in the bin right now because, in a moment, the tub will be filled with water! Your goal is to bob out all the candies by clicking on the screen. The box in the top right corner displays how much time you have left and the box in the bottom right corner displays how many candies you've found. Try to bob out all or as many candies as you can before the time runs out! Good luck and have fun, duckeroo!";
      displayText(str,g2d);
      //change state if the text has finished displaying
      if(getTextFinished()) {
        state = "countdown";
        countdown = System.nanoTime() + 4*1000000000L;
        clearText();
      }
    } else if(state.equals("countdown")) { //display the countdown for the game
      g2d.setFont(getFont().deriveFont(80.0f));
      g2d.setColor(Color.WHITE);
      long seconds = (countdown - System.nanoTime())/1000000000;
      //draw the countdown
      g2d.drawString(seconds+"",500-g2d.getFontMetrics().stringWidth(seconds+"")/2,280);
      //change state if the countdown has finished
      if(seconds <= 0){
        state = "game";
        timer = System.nanoTime() + 31*1000000000L;
      }
    } else if(state.equals("game")) { //when the game is running
      //calculate time
      long seconds = (timer - System.nanoTime())/1000000000;
      //draw water
      g2d.drawImage(water,0,0,null);
      //draw timer
      g2d.drawImage(box,880,12,null);
      g2d.setFont(getFont());
      g2d.setColor(new Color(184,105,113));
      String secondsStr = seconds+"";
      if(secondsStr.length()<=1) secondsStr = "0"+secondsStr;
      g2d.drawString("00:"+secondsStr,900,48);
      //draw number of candies found
      g2d.drawImage(box,880,529,null);
      g2d.setFont(getFont());
      g2d.setColor(new Color(184,105,113));
      g.drawString(""+numFound+"/8",910,564);
      //draw candies
      if(numFound > 0){
        for(int i = 0; i < candiesFound.length; i++){
          if(candiesFound[i] == true){
            g2d.drawImage(candies[i], (int)candiesPos[i].getX()-20, (int)candiesPos[i].getY()-20, null);
          }
        }
      }
      //change state if the game timer has run out
      if(timer - System.nanoTime() <= 0){
        state = "end";
        setFadeOutCnt(0);
      }
    }

    //draw scene transitions
    if(getFadeInCnt()>0) {
      fadeIn(g2d);
    }
    if(getFadeOutCnt()>=0 && getFadeOutCnt()<255) fadeOut(g2d);
    else if(getFadeOutCnt()!=-1) {
      transition(10,5);
      hasReset = false;
    }
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
