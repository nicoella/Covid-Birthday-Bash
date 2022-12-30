import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;
import java.util.*;

public class BoatRace extends Scene {
  private Image bg;
  private Image boats[];
  private Image box; 
  private int[] boatX; 
  private double[] boatSpeed; 
  private long[] boatDelay; 
  private long countdown; 
  private int playerX; 
  private boolean[] finished; 
  private String state; 
  
  /* class constructor
   * 
   * Purpose:
   * Reads in all image files, initializes all instance variables.
   * Creates a mouse listener which has a method mouseClicked which
   * is called when the mouse is clicked.   
   */
  public BoatRace() {
    super(); //calls the constructor of the Scene class
    reset(); //reset method initializes variables which need to be reset every time
    boats = new Image[2];
    try { //try-catch block catches errors
      //import all image files
      bg = ImageIO.read(new File("data/backgrounds/minigames/boat/raceScreen.png")); 
      for(int i=0; i<2; i++) boats[i] = ImageIO.read(new File("data/backgrounds/minigames/boat/boat"+i+".png"));
      box = ImageIO.read(new File("data/backgrounds/levels/box.png"));
    } catch (IOException e) {}
    //adds a listener which will be called each time the user releases a key
    addKeyListener(new KeyAdapter() {
      //keyReleased method is called whenever a key is released
      public void keyReleased(KeyEvent e) {
        //user can only move boat forward if the game has started
        if(state.equals("game") && e.getKeyChar()==' ') {
          if(playerX+5 <= 820) playerX += 5; //only moves boat forward if the boat hasn't reached the finish line
          else if(getFadeOutCnt()==-1) setFadeOutCnt(0); //otherwise begin fade out animation
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
    playerX = 0;
    boatX = new int[9];
    boatDelay = new long[9];
    finished = new boolean[9];
    state = "instructions";
    countdown = -1;
    for(int i=0; i<9; i++) boatDelay[i] = -1;
    boatSpeed = new double[]{1.5,0.8,1.9,1.1,2.2,1.0,2.1,1.8,1.4};
  }
  
  /*getRank method
   * 
   * Purpose:
   * Calculates and returns the user's rank out of the 10 children at the party.
   */
  public int getRank() {
    int cnt = 1; //stores the user's rank
    for(int i=0; i<9; i++) {
      if(playerX >= 820 && finished[i]) cnt++; //calculates user rank based on which other boats have finished if the user has already reached the finish line
      else if(playerX <= 820 && boatX[i] >= playerX) cnt++; //calcualtes user rank based on the user's boat position if the user has not reached the finish line
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
    //draw the player's boat
    g2d.drawImage(boats[1],16+playerX,16,null);
    //move the non-player-character boats forward
    for(int i=0; i<9; i++) {
      //only move boats forward if the delay was met
      if(state.equals("game") && playerX < 820 && System.nanoTime()-boatDelay[i] >= boatSpeed[i]*200000000) {
        boatDelay[i] = System.nanoTime(); //updates delay
        if(boatX[i]+5 <= 820) boatX[i] += 5; //moves boat forward if it hasn't reached the finish line yet
        if(boatX[i] > 820) finished[i] = true; //sets the boat's finished status to be true
      }
      //draw the non-player-character's boat
      g2d.drawImage(boats[0],16+boatX[i],16+(i+1)*48,null);
    }
    if(state.equals("instructions")) { //display instructions for the game
      requestFocus();
      //draw text on text box
      String str = "It's origami boat race time! Your boat is the red one at the top. Press SPACE to blow and move your boat forward. Reach the end of the pool to win!";
      displayText(str,g2d);
      //change game state when the text has finished
      if(getTextFinished()) {
        state = "countdown";
        countdown = System.nanoTime() + 4*1000000000L;
        clearText();
      }
    } else if(state.equals("countdown")) { //display the 3 second countdown for the game
      g2d.setFont(getFont().deriveFont(80.0f));
      g2d.setColor(Color.WHITE);
      long seconds = (countdown - System.nanoTime())/1000000000;
      //draw the countdown
      g2d.drawString(seconds+"",500-g2d.getFontMetrics().stringWidth(seconds+"")/2,280);
      //change state when the countdown has finished
      if(seconds <= 0) state = "game"; 
    } else if(state.equals("game")) { //when the game is actually occurring
      //draw player's rank
      g2d.drawImage(box,880,529,null);
      g2d.setFont(getFont());
      g2d.setColor(new Color(184,105,113));
      int rank = getRank(); //gets the user's rank out of 10
      //determines ending for rank
      String rankStr = rank+"";
      if(rank==1) rankStr += "st";
      else if(rank==2) rankStr += "nd";
      else if(rank==3) rankStr += "rd";
      else if(rank<=10) rankStr += "th";
      //display the rank
      g.drawString(rankStr,910,564);
    }
    //draw scene transitions
    if(getFadeInCnt()>0) { 
      fadeIn(g2d); 
      reset(); 
    }
    if(getFadeOutCnt()>=0 && getFadeOutCnt()<255) fadeOut(g2d);
    else if(getFadeOutCnt()!=-1) transition(4,3);
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