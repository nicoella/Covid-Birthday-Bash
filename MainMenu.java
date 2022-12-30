/* Names: Nicole Han, Tracy Wang, Roseanna Liang
 * Teacher: Ms. Krasteva
 * Due Date: June 18, 2020
 * Class: MainMenu class
 * 
 * This class is the Main Menu of the game. It provides two options to the user, 
 * to exit the game or to view the levels.
 * 
 * Instance Variable Dictionary:
 * type      |name        |purpose
 * ----------|------------|-------
 * boolean[] |buttonHover |stores if a button is being hovered over or not
 * Image     |bg          |stores the image file for the background
 * Image[]   |buttons     |stores the image files for the buttons
 * int       |choice      |stores which choice the user has made (which button was pressed)
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;
import java.util.*;

public class MainMenu extends Scene {
  private boolean[] buttonHover;  
  private Image bg; 
  private Image[] buttons; 
  private int choice; 
  
  /* class constructor
   * 
   * Purpose:
   * Reads in all image files, initializes all instance variables.
   * Creates a mouse listener which has a method mouseClicked which
   * is called when the mouse is clicked.   
   */
  public MainMenu() {
    super(); //calls the constructor of the Scene class
    //imports all images
    buttons = new Image[2];
    buttonHover = new boolean[2];
    try { //try-catch block catches errors
      bg = ImageIO.read(new File("data/backgrounds/mainMenu/mainMenu.png")); 
      for(int i=0; i<2; i++) {
        buttons[i] = ImageIO.read(new File("data/backgrounds/mainMenu/button"+i+".png"));
      }
    } catch (IOException e) {}
    //adds a listener which will be called each time the user presses their mouse
    addMouseListener(new MouseAdapter() {
      //mouseClicked method is called when the user clicks with their mouse
      public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        int[] choices = {2,8}; //choices for where the user will go
        for(int i=0; i<2; i++) { //checks if a user clicked on a button
          //detect if the user clicked on the current button
          if(x>=312+i*208 && x<=476+i*208 && y>=416 && y<=472 && getFadeInCnt()<=0 && getFadeOutCnt()==-1) {
            setFadeOutCnt(0); //start fade out animation
            choice = choices[i]; //set choice to the choice corresponding to the button they pressed
          }
        }
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
    //draw the different coloured buttons if hovered over
    for(int i=0; i<2; i++) {
      if(buttonHover[i]) {
        g2d.drawImage(buttons[i],312+i*208,416,null);
      }
    }
    //draw scene transitions
    if(getFadeInCnt()>0) fadeIn(g2d);
    if(getFadeOutCnt()>=0 && getFadeOutCnt()<255) fadeOut(g2d);
    else if(getFadeOutCnt()!=-1) transition(1,choice); 
  }
  
  /*actionPerformed method
   * 
   * Purpose:
   * redraws the screen and calculates if the mouse is hovering over a button
   */
  public void actionPerformed(ActionEvent e) {
    repaint();
    //gets x and y coordinates of the mouse
    Point p = this.getMousePosition();
    int x = 0;
    int y = 0;
    if(p!=null) {
      x = p.x;
      y = p.y;
    }
    //for loop checks if mouse is hovering over a button
    for(int i=0; i<2; i++) { 
      if(x>=312+i*208 && x<=479+i*208 && y>=416 && y<=475) { //mouse is hovering over the button
        buttonHover[i] = true;
      } else { //mouse is not hovering over the button
        buttonHover[i] = false;
      }
    }
  }
}