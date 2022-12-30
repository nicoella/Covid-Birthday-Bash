/* Names: Nicole Han, Tracy Wang, Roseanna Liang
 * Teacher: Ms. Krasteva
 * Due Date: June 18, 2020
 * Class: WhackAMole class
 * 
 * Creates the Whack a Mole minigame
 * 
 * Instance Variable Dictionary:
 * type            |name        |purpose
 * ----------------|------------|-------
 * Image           |bg          |stores the background image
 * Image           |front       |front part of background
 * Image           |middle      |middle part of background
 * Image           |back        |back part of background
 * Image           |box         |stores the background for timer and score
 * Image[]         |moles       |moles images
 * long            |countdown   |countdown between rules and game
 * long            |timer       |timer of game in nanoseconds
 * long            |temp        |temporarily holds the count for how long mole has been up
 * int             |num         |the mole that is up
 * boolean[]       |isUp        |whether or not the mole is up
 * boolean         |clicked     |if the user has clicked on the mole
 * int[][]         |locX        |the x location of the moles
 * int[]           |centerX     |the location of the center of the moles' heads. X position
 * int[]           |centerY     |the location of the center of the moles' heads, Y position
 * int             |score       |number of moles hit
 * int             |upY         |y coordinate of mole that is up
 * int             |mouseX      |mouse X coordinate  
 * int             |mouseY      |mouse Y coordinate
 * String          |state       |current state of game - instructions, countdown, game, etc
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;
import java.util.*;

public class WhackAMole extends Scene {
  private Image bg;
  private Image front;
  private Image middle;
  private Image back;
  private Image box;
  private Image[] moles;
  private long countdown;
  private long timer;
  private long temp;
  private int num;
  private boolean[] isUp;
  boolean clicked;
  private int[][] locX;
  final private int[] centerX = {335,665,195,497,792};
  final private int[] centerY = {385,385,120,120,120};
  private int score;
  private int upY;
  private int mouseX;
  private int mouseY;
  private String state;
  
  /* class constructor
   * 
   * Purpose:
   * Reads in all image files, initializes all instance variables.
   * Creates a mouse listener which has a method mouseClicked which
   * is called when the mouse is clicked.   
   */
  public WhackAMole() {
    super(); //calls the constructor of the Scene class
    
    reset(); //reset method initializes variables which need to be reset every time
    try { //try-catch block catches errors
      bg = ImageIO.read(new File("data/backgrounds/minigames/whackamole/background.png")); 
      front = ImageIO.read(new File("data/backgrounds/minigames/whackamole/front.png")); 
      middle = ImageIO.read(new File("data/backgrounds/minigames/whackamole/middle.png")); 
      back = ImageIO.read(new File("data/backgrounds/minigames/whackamole/back.png")); 
      box = ImageIO.read(new File("data/backgrounds/levels/box.png"));
      moles = new Image[5];
      for(int i = 0; i<5; i++) {
        moles[i] = ImageIO.read(new File("data/backgrounds/minigames/whackamole/mole"+(i+1)+".png")); 
      }
    } catch (IOException e) {}
    //adds a listener which will be called each time the user clicks with their mouse
    addMouseListener(new MouseAdapter() {
      //mouseClicked method called whenever the mouse is clicked
      public void mouseClicked( MouseEvent e) {
        if(e.getX() >= locX[num][0] && e.getX() <= locX[num][1] && (timer - System.nanoTime())/1000000000 > 0) {
          if((num < 2 && e.getY() >= 275 && e.getY() <= 480) || (num >=2 && e.getY()>=15 && e.getY() <= 220)) {
            if(e.getY() >=75 || (num<2 && e.getY()>=325)) {
              int dist = (int)Math.sqrt(Math.pow(e.getX()-centerX[num], 2)+Math.pow(e.getY()-centerY[num], 2));
              if(dist <= 105) { //if within radius of head
                score++; //increases score
                clicked = true;
                return;
              }
            } else { //if clicked within rectangle body
              score++;
              clicked = true;
              return;
            }
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
    state = "instructions";
    countdown = -1;
    isUp = new boolean[5];
    for(int i = 0; i<5; i++) {
      isUp[i] = false;
    }
    score = 0;
    temp = -1;
    num = 0;
    upY = 0;
    locX = new int[5][2];
    for(int i = 0; i<5; i++) {
      for(int j = 0; j<2; j++){
        if(i<2) {
          locX[i][j] = 235+i*330+200*j;
        }else {
          locX[i][j] = 95+(i-2)*300+200*j;
        }
      }
    }
    mouseX = 0;
    mouseY = 0;
    clicked = false;
  }
  
  
  /*getRank method
   * 
   * Purpose:
   * Calculates and returns the user's "rank" out of the 10 children at the party.
   */
  public int getRank() {
    int[] ranks = {6, 5, 4, 3, 2, 2, 2, 1, 0}; //stores the scores of the other children
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
    g2d.drawImage(back,0,0,null);
    g2d.drawImage(middle,0,0,null);
    g2d.drawImage(front,0,0,null);
    
    if(state.equals("instructions")) { //instructions for the game
      requestFocus();
      //draw text on text box
      String str = "Welcome to Whack A Mole! Here, you will use your mouse to hit the moles as they pop up. For each mole you hit, your score will increase by 1. You have 15 seconds, go go go!";
      displayText(str,g2d);
      if(getTextFinished()) {
        state = "countdown";
        countdown = System.nanoTime() + 4*1000000000L;
        clearText();
      }
    } else if(state.equals("countdown")) { //3 second countdown
      g2d.setFont(getFont().deriveFont(80.0f));
      g2d.setColor(new Color(184,105,113));
      long seconds = (countdown - System.nanoTime())/1000000000;
      g2d.drawString(seconds+"",500-g2d.getFontMetrics().stringWidth(seconds+"")/2,280);
      if(seconds <= 0) {
        state = "game"; 
        timer = System.nanoTime()+16000000000L;
      }
    } else if(state.equals("game")) { //when the game is running
      long seconds = (timer - System.nanoTime())/1000000000;
      if(seconds == -1 && getFadeOutCnt() ==-1) setFadeOutCnt(0);
      if(seconds<=0) {
        seconds = 0;
      }
      //draw timer
      g2d.drawImage(box,880,12,null);
      g2d.setFont(getFont());
      g2d.setColor(new Color(184,105,113));
      String secondsStr = seconds+"";
      if(secondsStr.length()<=1) secondsStr = "0"+secondsStr;
      g2d.drawString("00:"+secondsStr,900,48);
      
      //while timer still runs
      if(seconds > 0) {
        if(temp-System.nanoTime() <= 0 || clicked) {
          temp = System.nanoTime()+1200000000L; //resets mole timer
          isUp[num] = false;
          clicked = false;
          num = (int)(5*Math.random());
          isUp[num] = true;
          upY = 0;
        }
      }
      
      //drawing the moles
      int i;
      for(i = 0; i<5; i++) {
        if(isUp[i]) {
          g2d.drawImage(moles[i],0,50-upY,null);
          if(i > 1) {
            g2d.drawImage(middle,0,0,null);
          }
          g2d.drawImage(front,0,0,null);
        }
      }
      if (upY <= 45) {//animating movements
        upY+=5;
      }
      
      //draw player's score
      g2d.drawImage(box,880,529,null);
      g2d.setFont(getFont());
      g2d.setColor(new Color(184,105,113));
      String scoreStr = score+"";
      g.drawString(scoreStr,915,564);
    }
    
    //draw scene transitions
    if(getFadeInCnt()>0) {
      fadeIn(g2d);
      reset();
    }
    if(getFadeOutCnt()>=0 && getFadeOutCnt()<255) fadeOut(g2d);
    else if(getFadeOutCnt()!=-1) transition(11,13);
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