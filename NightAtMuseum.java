/* Names: Nicole Han, Tracy Wang, Roseanna Liang
 * Teacher: Ms. Krasteva
 * Due Date: June 18, 2020
 * Class: NightAtMuseum class
 * 
 * This class is the Night at the Museuem minigame.
 * 
 * Instance Variable Dictionary:
 * type      |name        |purpose
 * ----------|------------|-------]
 * Image     |bg          |stores the image file for the background
 * Image     |police1     |stores the front of the police
 * Image     |polie2      |stores the left of the police
 * Image     |police0     |stores the right if the police
 * String    |state       |the state of the game
 * long      |delay       |stores the delay
 * long      |countdown   |stores the countdown
 * boolean   |moving      |whether or not the player is moving
 * boolean[] |charMove    |whether or not the characters are moving
 * boolean[] |charFinish  |whether or not the character is finished
 * boolean   |finished    |whether or not the player is finished
 * int       |playerX     |the player position
 * int[]     |charX       |the character positions
 * int[]     |chances     |the chance of the character stopping
 * boolean   |done        |whether or not the chances have been calculated
 * Player    |main        |the main character
 * Player[]  |otherChar   |the other characters
 * Image     |box         |stores the image of the rank box
 * 
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;
import java.util.*;

public class NightAtMuseum extends Scene {
  private Image bg;
  private Image police1;
  private Image police2;
  private Image police0;
  private String state;
  private long delay;
  private long countdown;
  private boolean moving;
  private boolean[] charMove;
  private boolean[] charFinish;
  private boolean finished;
  private int playerX;
  private int[] charX;
  private int[] chances;
  private boolean done;
  private Player main;
  private Player[] otherChar;
  private Image box;
  
  /* class constructor
   * 
   * Purpose:
   * Reads in all image files, initializes all instance variables.
   * Creates a key listener which has a method keyPressed and keyReleased which
   * is called when the keys are pressed and released.   
   */
  public NightAtMuseum() {
    super(); //calls the constructor of the Scene class
    reset(); //reset method initializes variables which need to be reset every time
    try { //try-catch block catches errors
      //imports all images
      bg = ImageIO.read(new File("data/backgrounds/minigames/museum/museumbg.png")); 
      police0 = ImageIO.read(new File("data/characters/person6/right0.png"));
      police1 = ImageIO.read(new File("data/characters/person6/front0.png"));
      police2 = ImageIO.read(new File("data/characters/person6/left0.png"));
      box = ImageIO.read(new File("data/backgrounds/levels/box.png"));
    } catch (IOException e) {}
    //adds a listener which will be called each time the user presses or releases a key
    addKeyListener(new KeyAdapter() {
      //keyPressed method is called whenever a key is pressed
      public void keyPressed(KeyEvent e) {
        //if the user presses SPACE, stop the character
        if(state.equals("game") && e.getKeyChar() == ' ') {
          moving = false;
        }
      }
      //keyReleased method is called whenever a key is released
      public void keyReleased(KeyEvent e) {
        //if the user releases the SPACE key, character begins to move again
        if(state.equals("game") && e.getKeyChar() == ' ' && !finished) {
          moving = true;
        }
      }
    });
  }
  
  /*reset method
   * 
   * purpose:
   * Resets all variables for next use
   */
  public void reset() {
    state = "instructions";
    delay = 5*(10^9);
    countdown = -1;
    moving = true;
    playerX = 80;
    main = new Player("main",playerX,100);
    charX = new int[4];
    for(int i = 0; i<4; i++) {
      charX[i] = 80;
    }
    otherChar = new Player[4];
    for(int i = 0; i<4; i++) {
      otherChar[i] = new Player("person"+(i+1), charX[i],180+i*80);
    }
    charFinish = new boolean[4];
    for(int i = 0; i<4; i++) {
      charFinish[i] = false;
    }
    chances = new int[4];
    chances[0] = 100;
    chances[1] = 60;
    chances[2] = 70;
    chances[3] = 40;
    charMove = new boolean[4];
    for(int i = 0; i<4; i++) {
      charMove[i] = true;
    }
    done = false;
  }
  
  /*getRank method
   * 
   * Purpose:
   * Calculates and returns the user's rank out of the characters.
   */
  public int getRank() {
    int cnt = 1; //stores the user's rank
    for(int i=0; i<4; i++) {
      if(playerX >= 700 && finished) cnt++; //user's rank is decreased
      else if(playerX <= 700 && charX[i] > playerX) cnt++; //user's rank is decreased
    }
    return cnt;
  }
  
  /*getResult method
   * 
   * Purpose:
   * Returns whether or not the specific character is moving
   */
  public boolean getResult(int chance) {
    int num = (int)(100*Math.random())+1; //gets random number
    if(num>=chance) {
      return false;
    }
    return true; //true if succeed
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
    //draw the characters
    main.drawPlayer(g2d);
    for(int i = 0; i<4; i++) {
      otherChar[i].drawPlayer(g2d);
    }
    g2d.drawImage(police0,800,200,null);
    
    if(state.equals("instructions")) { //display game instructions
      requestFocus();
      //draw text on text box
      String str = "It's time for Night at the Museum! Your goal is to get to the policeman without getting seen moving. Your character will move forward automatically, you must press SPACE when the policeman turns, or you'll get sent to the start again! The game ends when you reach the policeman!";
      displayText(str,g2d);
      //change game state when instructions are finished
      if(getTextFinished()) {
        state = "countdown";
        countdown = System.nanoTime() + 4*1000000000L;
        clearText();
      }
    } else if(state.equals("countdown")) { //display 3 second countdown
      g2d.setFont(getFont().deriveFont(80.0f));
      g2d.setColor(new Color(184,105,113));
      long seconds = (countdown - System.nanoTime())/1000000000;
      g2d.drawString(seconds+"",500-g2d.getFontMetrics().stringWidth(seconds+"")/2,280);
      //change game state when countdown ends
      if(seconds <= 0) { 
        state = "game";
        delay = System.nanoTime()+5*1000000000L;
      } 
    } else if(state.equals("game")) { //when the game is running
      if(moving) {
        playerX++;
        main.setX(playerX);
        main.setState("rightWalk");
      }else {
        main.setState("rightStill");
      }
      
      //loop to check every character and set whether or not they are moving
      for(int i = 0; i<4; i++) {
        if(!charFinish[i]) {
          if(charMove[i]) {
            charX[i]++;
            otherChar[i].setX(charX[i]);
            otherChar[i].setState("rightWalk");
          }else {
            otherChar[i].setState("rightStill");
          }
        }else {
          otherChar[i].setState("rightStill");
        }
      }
      
      //creates timer
      long seconds = (long)Math.ceil((delay-System.nanoTime())/1000000000.0);
      
      if(seconds == 2 || seconds == 0) { //if the police is facing to the side
        //face side
        g2d.drawImage(police1,800,200,null);
        if(seconds==0) {
          for(int i = 0; i<4; i++) {
            charMove[i] = true;
          }
          done = false;
        }else if(!done) {
          for(int i = 0; i<4; i++) {
            if(getResult(chances[i])) {
              charMove[i] = false;
            }
          }
          done = true;
        }
      }else if(seconds == 1) {
        //turn
        g2d.drawImage(police2,800,200,null);
        if(moving) {
          playerX = 100;
          main.setState("rightStill");
        }
        
        for(int i = 0; i<4; i++) {
          if(charMove[i]) {
            charX[i] = 100;
            otherChar[i].setState("rightStill");
          }
        }
        
      }else if(seconds == -1){
        delay = System.nanoTime() + ((int)(Math.random()*3)+4)*1000000000L;
      }else {
        //face away
        g2d.drawImage(police0,800,200,null);
      }
      if(playerX >= 700) {//checks location of user
        finished = true;
      }
      for(int i = 0; i<4; i++) {//checks location of NPCs
        if(charX[i]>=870) {
          charFinish[i] = true;
        }
      }
      if(finished && getFadeOutCnt() == -1) { //prepares for transition
        setFadeOutCnt(0);
      }
      
      //draws the rank
      g2d.drawImage(box,880,529,null);
      g2d.setFont(getFont());
      g2d.setColor(new Color(184,105,113));
      int rank = getRank();
      String rankStr = rank+"";
      if(rank==1) rankStr += "st";
      else if(rank==2) rankStr += "nd";
      else if(rank==3) rankStr += "rd";
      else if(rank<=10) rankStr += "th";
      g.drawString(rankStr,910,564);
    }
    //draw scene transitions
    if(getFadeInCnt()>0) fadeIn(g2d);
    if(getFadeOutCnt()>=0 && getFadeOutCnt()<255) fadeOut(g2d);
    else if(getFadeOutCnt()!=-1) {
      transition(12,13);
      reset();
    }
  }
  
  /* paintComponent method
   * 
   * purpose:
   * This methods performs the graphics of the game.
   */
  public void actionPerformed(ActionEvent e) {
    repaint();
  }
}