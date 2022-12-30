/* Names: Nicole Han, Tracy Wang, Roseanna Liang
 * Teacher: Ms. Krasteva
 * Due Date: June 18, 2020
 * Class:levelSelect class
 * 
 * This class is the level select of the game. 
 * 
 * Instance Variable Dictionary:
 * type            |name        |purpose
 * ----------------|------------|-------
 * boolean[]       |buttonHover |stores if a button is being hovered over or not
 * Image           |bg          |stores the background image
 * Image[]         |buttons     |stores the different coloured button hover image
 * Image           |star        |stores the star image
 * int             |choice      |stores which button the user pressed
 * int[]           |scores      |stores the user's score out of three stars for the levels
 * Image           |popup       |stores the image file for the pop up error message
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;

public class LevelSelect extends Scene {
  private boolean[] buttonHover;
  private Image bg;
  private Image[] buttons;
  private Image star;
  private int choice;
  private int[] scores;
  private Image popup;
  
  /* class constructor
   * 
   * Purpose:
   * Initializes instance variables and adds a mouse listener which detects
   * when the user has pressed with their mouse/
   */
  public LevelSelect() {
    super(); //calls the Scene class constructor
    buttons = new Image[4];
    buttonHover = new boolean[4];
    scores = new int[3];
    try { 
      //imports all images
      bg = ImageIO.read(new File("data/backgrounds/levelSelect/levelSelection.png")); 
      for(int i=0; i<4; i++) {
        buttons[i] = ImageIO.read(new File("data/backgrounds/levelSelect/button"+i+".png"));
      }
      star = ImageIO.read(new File("data/backgrounds/levelSelect/star.png"));
      popup = ImageIO.read(new File("data/backgrounds/levelSelect/popup.png"));
    } catch (IOException e) {};
    readFile(); //reads from the score file
    //mouse listener used to detect when the user presses with their mouse
    addMouseListener(new MouseAdapter() {
      //mouseClicked method called when the user clicks their mouse
      public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        int[] choices = {3,5,13,1}; //choices of where the user wants to go
        //loops over 4 buttons to detect if the user has pressed one
        for(int i=0; i<4; i++) {
          //if statement checks if the user has pressed on a button
          if(x>=356 && x<=643 && y>=220+i*88 && y<=279+i*88 && getFadeInCnt() <=0 && getFadeOutCnt()==-1) {
            setFadeOutCnt(0); //begin fade out animation if the user has pressed on a button
            choice = choices[i]; //sets user's button choice
            //displays level locked message if the user has not played previous levels for a level
            if(choice > getMaxLevel()) { //if the user chooses level they cannot access
              setFadeOutCnt(-1);//resets the fade count
              lockLvlMsg();//shows lock message
            }
          }
        }
      }
    });
  }
  
  /* lockLvlMsg method
   * 
   * Purpose:
   * Displays a pop up message when the user clicks on a level they have not unlocked yet.
   */
  public void lockLvlMsg() {
    //initialize variables
    JFrame fr = new JFrame(); //creates new JFrame
    JPanel p = new JPanel(); //creates new JPanel
    p.add(new JLabel(new ImageIcon(popup))); //adds JLabel and picture to JPanel
    fr.add(p); //adds JPanel to JFrame
    fr.setSize(400,280); //sets the size
    fr.setResizable(false); //does not allow window to be resized
    fr.setLocationRelativeTo(getFrame()); //makes popup appear in center
    fr.setVisible(true); //allows JFrame to be seen
  }
  
  /*readFile method
   * 
   * Purpose:
   * Reads the user's scores from the save file.
   */
  public void readFile() {
    try { //try-catch block catches errors
      BufferedReader in = new BufferedReader(new FileReader("saveFile.txt"));
      //read user's score from the file
      for(int i=0; i<3; i++) {
        scores[i] = Integer.parseInt(in.readLine());
      }
      //Sets the maximum level accessible
      setMaxLevel(3);
      setCurrLevel(3); //sets the current level of the game
      if(scores[0] != 0) { //if level 1 passed
        setMaxLevel(5); //move on to level 2
        setCurrLevel(5);
      }
      if(scores[1] != 0 ) { //if level 2 passed
        setMaxLevel(13); //move on to level 3
        setCurrLevel(13);
      }
    } catch (IOException e) {}
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
    g2d.drawImage(bg,0,0,null);
    //draw the buttons being hovered over
    for(int i=0; i<4; i++) {
      if(buttonHover[i]) {
        g2d.drawImage(buttons[i],356,220+i*88,null);
      }
    }
    //draw the stars for levels the user has played
    for(int i=0; i<3; i++) {
      for(int j=0; j<scores[i]; j++) {
        g2d.drawImage(star,592-44*j,264+88*i,null);
      }
    }
    //draw scene transitions
    if(getFadeInCnt()>0) {
      fadeIn(g2d);
      readFile();
    }
    if(getFadeOutCnt()>=0 && getFadeOutCnt()<255) fadeOut(g2d);
    else if(getFadeOutCnt()!=-1) transition(2,choice);
  }
  
  /*actionPerformed method
   * 
   * Purpose:
   * redraws the screen and calculates if the mouse is hovering over a button
   */
  public void actionPerformed(ActionEvent e) {
    repaint();
    Point p = this.getMousePosition(); //get coordinates of the mouse
    int x = 0;
    int y = 0;
    if(p!=null) {
      x = p.x;
      y = p.y;
    }
    for(int i=0; i<4; i++) {
      if(x>=356 && x<=643 && y>=220+i*88 && y<=279+i*88) {  //the user is hovering over a button
        buttonHover[i] = true;
      } else { //the user is not hovering over a button
        buttonHover[i] = false;
      }
    }
  }
}