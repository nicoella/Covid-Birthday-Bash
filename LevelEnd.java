/* Names: Nicole Han, Tracy Wang, Roseanna Liang
 * Teacher: Ms. Krasteva
 * Due Date: June 18, 2020
 * Class: Level1 class
 * 
 * This class is the level end scene of the game.
 * 
 * Instance Variable Dictionary:
 * type            |name        |purpose
 * ----------------|------------|-------
 * int             |score       |stores the user's score out of three stars
 * boolean         |buttonHover |stores if the user is hovering over the button
 * Image[]         |bg          |stores the background images
 * Image           |star        |stores the image for the star
 * Image           |starOutline |stores the image for the star outline
 * long            |delay       |stores the delay for drawing the stars
 * int[]           |size        |stores the size of the stars being animated
 * int             |bgIndx      |stores the index of the background to be displayed
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;
import java.util.*;

public class LevelEnd extends Scene {
  private int score;
  private boolean buttonHover;
  private Image[] bg;
  private Image button;
  private Image star;
  private Image starOutline;
  private long delay;
  private int[] size;
  private int bgIndx;
  
  /* class constructor
   * 
   * Purpose:
   * Initializes instance variables and adds a mouse listener
   * which is called when a mouse is clicked.
   */
  public LevelEnd() {
    super(); //calls the Scene class constructor
    buttonHover = false;
    reset(); //reset method initializes variables which need to be reset every time
    bg = new Image[2];
    try { //try-catch block catches errors
      //imports all images
      bg[0] = ImageIO.read(new File("data/backgrounds/levelEnd/levelEnd.png")); 
      bg[1] = ImageIO.read(new File("data/backgrounds/levelEnd/levelFail.png"));
      button = ImageIO.read(new File("data/backgrounds/levelEnd/hover.png"));
      star = ImageIO.read(new File("data/backgrounds/levelEnd/star.png"));
      starOutline = ImageIO.read(new File("data/backgrounds/levelEnd/starOutline.png"));
    } catch (IOException e) {}
    //adds a listener which will be called each time the user presses their mouse
    addMouseListener(new MouseAdapter() {
      //mouseClicked method is called whenever the user clicks their mouse
      public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        //starts screen fade out if the user pressed the main menu button
        if(x >= 356 && x <= 640 && y >= 408 && y <= 464) {
          setFadeOutCnt(0);
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
    delay = -1;
    size = new int[3];
    score = 0;
  }
  
  /*setScore method
   * 
   * Purpose:
   * calculates the user's score out of three stars and saves it to the save file
   */
  public void setScore(int rank, int maxScore, boolean failed) {
    if(!failed) {
      double percent = ((double)rank / maxScore) * 100; //user's percent score
      if(percent <= 25) score = 3; //if the user was in the top 25% they get 3 stars
      else if(percent <= 50) score = 2; //if the user was in the top 50% they get 2 stars
      else score = 1; //otherwise the user gets 1 star
      int lvl = -1; 
      if(maxScore==10) lvl = 0;
      else if(maxScore==20) lvl = 1;
      else lvl = 2;
      String[] scores = new String[3];
      try { //try-catch block catches errors
        BufferedReader in = new BufferedReader(new FileReader("saveFile.txt"));
        //read in from score file
        for(int i=0; i<3; i++) {
          scores[i] = in.readLine();
        }
        //update score if user scored greater
        scores[lvl] = Math.max(Integer.parseInt(scores[lvl]), score)+"";
        PrintWriter out = new PrintWriter(new FileWriter("saveFile.txt"));
        //print to score file
        for(int i=0; i<3; i++) {
          out.println(scores[i]);
        }
        out.close();
      } catch (IOException e) {}
      bgIndx = 0;
    } else {
      bgIndx = 1;
    }
  }
  
  /* paintComponent method
   * 
   * purpose:
   * This methods performs the graphics of the game.
   */
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D)g;
    //draw the background
    g2d.drawImage(bg[bgIndx],0,0,null);
    //update delay time
    if(delay==-1) delay = System.nanoTime();
    //draw the star outline
    if(score > 0) {
      for(int i=0; i<3; i++) g2d.drawImage(starOutline,360+i*96,284,null);
    }
    for(int i=0; i<score; i++) {
      //only start drawing the star after a 1 second delay from the previous star
      if(System.nanoTime()-delay >= (i+1)*1000000000L) {
        if(size[i]+5<=80) size[i]+=5; //only increase star's size if it is under 80 pixels
        //draw the star
        g2d.drawImage(star.getScaledInstance(size[i],size[i], Image.SCALE_DEFAULT),404+i*96-size[i]/2,328-size[i]/2,null);
      }
    }
    //draw the different coloured buttons if hovered over
    if(buttonHover) g2d.drawImage(button,356,408,null);
    //draw scene transitions
    if(getFadeInCnt()>0) fadeIn(g2d);
    if(getFadeOutCnt()>=0 && getFadeOutCnt()<255) fadeOut(g2d);
    else if(getFadeOutCnt()!=-1) {
      transition(9,2);
      if(getCurrLevel() == 3) setMaxLevel(5);
      else setMaxLevel(13);
      reset(); //resets the level end
    }
  }
  
  /*actionPerformed method
   * 
   * Purpose:
   * redraws the screen and calculates if the mouse is hovering over the button
   */
  public void actionPerformed(ActionEvent e) {
    repaint();
    //get coordinates of the mouse
    Point p = this.getMousePosition();
    int x = 0;
    int y = 0;
    if(p!=null) {
      x = p.x;
      y = p.y;
    }
    if(x >= 356 && x <= 640 && y >= 408 && y <= 464) { //the user is hovering over the main menu button
      buttonHover = true;
    } else { //the user is not hovering over the main menu button
      buttonHover = false;
    }
  }
  
}