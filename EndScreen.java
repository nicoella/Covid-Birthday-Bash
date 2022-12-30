import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;
import java.util.*;

public class EndScreen extends Scene { 
  private Image bg; //stores the image file for the background
  
  /* class constructor
   * 
   * Purpose:
   * Reads in all image files, initializes all instance variables.
   * Creates a mouse listener which has a method mouseClicked which
   * is called when the mouse is clicked.   
   */
  public EndScreen() {
    super(); //calls the constructor of the Scene class
    try { //try-catch block catches errors
      //imports all images
      bg = ImageIO.read(new File("data/backgrounds/endScreen/endScreenBg.png")); 
    } catch (IOException e) {}
    //adds a listener which will be called each time the user releases a key
    addKeyListener(new KeyAdapter() {
      //keyReleased method is called when user releases a key
      public void keyReleased(KeyEvent e) {
        //starts fade out animation
        setFadeOutCnt(0);
      }
    });
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
    requestFocus();
    //draw scene transitions
    if(getFadeInCnt()>0) fadeIn(g2d);
    if(getFadeOutCnt()>=0 && getFadeOutCnt()<255) fadeOut(g2d);
    else if(getFadeOutCnt()!=-1) System.exit(0);
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