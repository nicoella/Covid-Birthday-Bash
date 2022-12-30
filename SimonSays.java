/* Names: Nicole Han, Tracy Wang, Roseanna Liang
 * Teacher: Ms. Krasteva
 * Due Date: June 18, 2020
 * Class: Basketball class
 * 
 * This class is the simon-says game class.
 * 
 * Instance Variable Dictionary:
 * type            |name        |purpose
 * ----------------|------------|-------
 * Image           |bg          |stores the background image
 * Image           |box         |stores the background for timer and # of games won 
 * Image           |simonBox    |stores the textbox for what Simon/simun says
 * long            |timer       |stores the amount of time players have to click the correct buttons
 * long            |countdown   |stores the length of the countdown before game starts
 * int             |gamesWon    |stores how many games the user has won
 * boolean         |simonPrefix |stores whether or not to add "Simon says" or "Simun says" to the beginning of simonStr
 * char            |currButton  |stores character of button simon or simun says should be pressed (A,B,C, or D - e.g. "Simon says press B", "Press D")
 * String          |simonStr    |stores what simon/simun says ("Simon/simon says press A/B/C/D")
 * String          |state       |current state of game - instructions, countdown, game, etc
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;
import java.util.*;

public class SimonSays extends Scene{
  private Image bg;
  private Image box;
  private Image simonBox;
  private long timer;
  private long countdown;
  private int gamesWon;
  private boolean simonPrefix;
  private char currButton;
  private String simonStr;
  private String state;
  private char lastButton;
  
  /* class constructor
   * 
   * Purpose:
   * Reads in all image files, initializes all instance variables.
   * Creates a key listener which has a method keyReleased. This is called whenever a key is released.   
   */
  public SimonSays(){
    super();
    reset(); //reset method initializes instance variables which need to be reset each time the level is played
    try{
      //reads in images for background and display/text boxes
      bg = ImageIO.read(new File("data/backgrounds/minigames/simon/simonSays.png"));
      box = ImageIO.read(new File("data/backgrounds/levels/box.png"));
      simonBox = ImageIO.read(new File("data/backgrounds/minigames/simon/simonBox.png"));
    } catch(IOException e){}
    
    addKeyListener(new KeyAdapter(){
      public void keyReleased(KeyEvent e) {
        if(state.equals("game")){
          //verifies that user has pressed the right key, corresponding to the simonStr (press A/B/C/D if Simon tells you to, press SPACE if Simun tells you to)
          if(simonPrefix == true && (e.getKeyChar() == currButton || e.getKeyChar() == (char)currButton+' ')|| simonPrefix == false && e.getKeyChar() == ' '){
            //updates number of games won
            gamesWon++;
            //randomly generates components of simonStr
            simonPrefix = ((int)(Math.random()*2)+1) % 2 == 0;
            while(currButton == lastButton) currButton = (char) (char) ((int)(Math.random()*3)+1 + 'A');
            lastButton = currButton;
            timer = 10*1000000000L - gamesWon*1000000000L + System.nanoTime();
          } else {
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
  public void reset(){
    timer = -1;
    countdown = -1;
    gamesWon = 0;
    simonPrefix = ((int)(Math.random()*2)+1) % 2 == 0;
    currButton = (char) ((int)(Math.random()*3)+1 + 'A');
    lastButton = ' ';
    simonStr = "";
    state = "instructions";
  }
  
  /*getRank method
   * 
   * Purpose:
   * Calculates and returns the user's "rank" out of the 10 children at the party.
   */
  public int getRank() {
    int[] ranks = {6,5,4,3,2,2,2,2,1}; //stores scores of the non-player-characters
    int cnt = 1; //stores user's rank
    for(int i=0; i<ranks.length; i++) {
       if(ranks[i] >= gamesWon) cnt++; //user's rank goes down if it is lower than other kid's score
    }
    return cnt;
  }
  
  /* paintComponent method
   * 
   * purpose:
   * This methods performs the graphics of the game.
   */
  public void paintComponent(Graphics g){
    requestFocus();
    Graphics2D g2d = (Graphics2D)g;
    super.paintComponent(g);
    
    //draw the background
    g2d.drawImage(bg,0,0,null);
    
    //draw timer
    g2d.drawImage(box,880,12,null);
    g2d.setFont(getFont());
    g2d.setColor(new Color(184,105,113));
    g2d.drawString("00:00",900,48);
    
    //draw games won
    g2d.drawImage(box,880,78,null);
    g2d.drawString(""+gamesWon,900,114);
    
    if(state.equals("instructions")) { //display the instructions for the game
      String str = "Get ready because it's time to play Simon-says, COVID-19 edition. Because of the COVID virus you're not allowed to touch your face. Instead, you'll be playing Simon-says using a panel of buttons. You must click the button Simon tells you to, by pressing that letter on your keyboard. Beware though! Simon's friend, Simun, is out to trick you. When Simun tells you to press a certain button, you must press the SPACE bar instead. The box in the top right corner tells you how much time you have to press the right key. After each game, the timer decreases by 1 second. The game ends either when you press the wrong key or when the timer hits zero. Good luck out there!";
      displayText(str,g2d);
      //change game state when instructions are done
      if(getTextFinished()) {
        state = "countdown";
        countdown = System.nanoTime() + 4*1000000000L;
        clearText();
      }
    } else if(state.equals("countdown")){ //display the three second countdown
      g2d.setFont(getFont().deriveFont(80.0f));
      g2d.setColor(Color.WHITE);
      long seconds = (countdown - System.nanoTime())/1000000000;
      //display the countdown
      g2d.drawString(seconds+"",500-g2d.getFontMetrics().stringWidth(seconds+"")/2,250);
      //change game state when the countdown is done
      if(seconds <= 0){
        state = "game";
        timer = System.nanoTime() + 10*1000000000L;
      }
    } else if (state.equals("game")){ //when the game is running
      //calculate time
      long seconds = (timer - System.nanoTime())/1000000000;
      
      //draw timer
      g2d.drawImage(box,880,12,null);
      g2d.setFont(getFont());
      g2d.setColor(new Color(184,105,113));
      String secondsStr = seconds+"";
      if(secondsStr.length()<=1) secondsStr = "0"+secondsStr;
      g2d.drawString("00:"+secondsStr,900,48);
      
      //draw games won
      g2d.drawImage(box,880,78,null);
      g2d.drawString(""+gamesWon,900,114);
      
      //draw simon textbox
      g2d.drawImage(simonBox,372,20,null);
      g2d.setFont(getFont().deriveFont(45.0f));
      if(simonPrefix==true)simonStr = "Simon says press " + currButton;
      else simonStr = "Simun says press " + currButton;
      g2d.setColor(Color.WHITE);
      g2d.drawString(simonStr,392,51);
      
      //change game state when the timer is done
      if(timer - System.nanoTime() <= 0){
        state = "end";
        setFadeOutCnt(0);
      }
    }
    
    //draw scene transitions
    if(getFadeInCnt()>0) fadeIn(g2d);
    if(getFadeOutCnt()>=0 && getFadeOutCnt()<255) fadeOut(g2d);
    else if(getFadeOutCnt()!=-1) {
      transition(14,13);
      reset();
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