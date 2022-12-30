/* Names: Nicole Han, Tracy Wang, Roseanna Liang
 * Teacher: Ms. Krasteva
 * Due Date: June 18, 2020
 * Class: Scene class
 * 
 * This is the Scene class of the game. It extends JPanel,
 * and implements ActionListener. All scenes in the game will
 * extend this class.
 * 
 * Instance Variable Dictionary:
 * type            |name        |purpose
 * ----------------|------------|-------
 * int             |maxLevel    |maximum level allowed
 * int             |currLevel   |current level played in
 * Timer           |tm          |used for animation
 * int             |fadeInCnt   |used to fade in a scene
 * int             |fadeOutCnt  |used to fade out a scene
 * boolean         |run         |stores if the scene is running or not
 * ArrayList       |arr         |stores the list of all scenes
 * JFrame          |f           |stores the frame all graphics are displayed on
 * String[]        |text        |stores the text being displayed on the textbox
 * int             |textIndx    |stores the current index in the text string being displayed
 * int             |textLenCnt  |stores the index in the text array which the current text is being saved to
 * long            |textDelay   |used to delay the animation of text
 * boolean         |textFinished|stores if the text has finished animating or not
 * Image           |textBox     |image of the text box to display text on
 * Font            |font        |stores the game font
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;
import java.util.*;

public abstract class Scene extends JPanel implements ActionListener{
  private static int maxLevel; 
  private static int currLevel;
  private javax.swing.Timer tm;
  private int fadeInCnt;
  private int fadeOutCnt;
  private boolean run;
  private ArrayList<Scene> arr;
  private JFrame f;
  private String[] text;
  private int textIndx;
  private int textLenCnt;
  private long textDelay;
  private boolean textFinished;
  private Image textBox;
  private Font font;
  
  /*class constructor
   * 
   * Purpose:
   * Initializes instance variables.
   */
  public Scene() {
    clearText();
    maxLevel = 3;
    currLevel = 3;
    try { //try-catch block catches errors
      //imports all images
      textBox = ImageIO.read(new File("data/backgrounds/levels/textbox.png"));
    } catch (IOException e) {}
    //import font file
    try { //try-catch block catches errors
      font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("data/fonts/AsepriteFont.ttf")); 
      font = font.deriveFont(50.0f);
    } catch (Exception e) {}
  }
  
  /*getMaxLevel method
   * 
   * Purpose:
   * Returns the maximum level allowed to access
   */
  public int getMaxLevel() {
    return maxLevel;
  }
  
  /*setMaxLevel method
   * 
   * Purpose:
   * Sets the maximum level allowed
   */
  public void setMaxLevel(int lvl) {
    maxLevel = lvl;
  }
  
  /*getMaxLevel method
   * 
   * Purpose:
   * Returns current level
   */
  public int getCurrLevel() {
    return currLevel;
  }
  
  /*getMaxLevel method
   * 
   * Purpose:
   * Sets current level
   */
  public void setCurrLevel(int lvl) {
    currLevel = lvl;
  }
  
  /* transition method
   * 
   * Purpose:
   * Transitions between two scenes.
   */
  public void transition(int a, int b) {
    if(arr.get(b).getParent()==null) f.getContentPane().add(arr.get(b)); //adds the new scene to the JFrame
    arr.get(a).stop(); //stops the current scene
    arr.get(b).start(); //starts the new scene
  }
  
  /* getFont method
   * 
   * Purpose:
   * Gets the game font.
   */
  public Font getFont() {
    return font;
  }
  
  /* clearText method
   * 
   * Purpose:
   * Clears the current text being displayed.
   */
  public void clearText() {
    text = new String[3];
    text[0] = "";
    textIndx = 0;
    textLenCnt = 0;
    textDelay = 0;
    textFinished = false;
  }
  
  /*getTextFinished method
   * 
   * Purpose:
   * Gets if the text has finished animating.
   */
  public boolean getTextFinished() {
    return textFinished;
  }
  
  /* displayText method
   * 
   * Purpose: 
   * Animates text being displayed. 
   */
  public void displayText(String str, Graphics2D g2d) {
    if(textIndx >= str.length() && System.nanoTime() - textDelay >= 2000000000) { //checks if the text has finished animating
      textFinished = true;
      return;
    }
    g2d.setFont(font.deriveFont(35.0f));
    g2d.setColor(new Color(184,105,113));
    if(!textFinished && System.nanoTime()-textDelay >= 50000000 && textIndx < str.length()) { //animate text after delay
      textDelay = System.nanoTime();
      if(str.charAt(textIndx)==' ') {
        //checks if there is enough space for the new word to be added
        String[] words = str.substring(textIndx+1).split(" ");
        if(g2d.getFontMetrics().stringWidth(text[textLenCnt]+" "+words[0]) > 547) { //changes the number of lines if the new word cannot be added
          textLenCnt++;
          if(textLenCnt==3) {
            for(int i=0; i<2; i++) text[i] = text[i+1];
            textLenCnt --;
          }
          text[textLenCnt] = "";
        }
      }
      //adds the next character
      if(str.charAt(textIndx)!=' ' || !text[textLenCnt].equals("")) text[textLenCnt] += str.charAt(textIndx);
      textIndx++;
    }
    //display the text
    int h = 573;
    for(int i=0; i<=textLenCnt; i++) {
      if(h >= 573) {
        h = 514;
        //draw text box
        g2d.drawImage(textBox,206,484,null);
      }
      g2d.drawString(text[i],226,h);
      h += 24;
    }
  }
  
  /* setArr method
   * 
   * Purpose:
   * Sets the ArrayList of all scenes in the game.
   */
  public void setArr(ArrayList<Scene> arr) {
    this.arr = arr;
  }
  
  /* getArr method
   * 
   * Purpose:
   * Gets the ArrayList of all scenes in the game.
   */
  public ArrayList getArr() {
    return arr;
  }
  
  /* getFrame method
   * 
   * Purpose:
   * Gets the JFrame which all graphics are displayed on.
   */
  public JFrame getFrame() {
    return f;
  }
  
  /* setFrame method
   * 
   * Purpose:
   * Sets the JFrame which all graphics are displayed on.
   */
  public void setFrame(JFrame f) {
    this.f = f;
  }
  
  /* start method
   * 
   * Purpose:
   * Starts a scene.
   */
  public void start() {
    tm = new javax.swing.Timer(20,this);
    fadeInCnt = 255; //starts the fade in animation
    fadeOutCnt = -1;
    tm.start();
    run = true;
    requestFocus();
    this.setVisible(true); //makes the scene visible
  }
  
  /* stop method
   * 
   * Purpose:
   * Stops a scene.
   */
  public void stop() {
    run = false;
    this.setVisible(false); //makes the scene not visible
  }
  
  /*fadeIn method
   * 
   * Purpose: 
   * Animates a scene fading in.
   */
  public void fadeIn(Graphics2D g2d) {
    g2d.setColor(new Color(0,0,0,Math.max(0,fadeInCnt)));
    g2d.fillRect(0,0,1000,600);
    fadeInCnt -= 10;
  }
  
  /* getFadeInCnt method
   * 
   * Purpose:
   * Returns the fade in count.
   */
  public int getFadeInCnt() {
    return fadeInCnt;
  }
  
  /*setFadeInCnt method
   * 
   * Purpose:
   * Sets the fade in count.
   */
  public void setFadeInCnt(int n) {
    fadeInCnt = n;
  }
  
  /*fadeOut method
   * 
   * Purpose:
   * Animates a scene fading out.
   */
  public void fadeOut(Graphics2D g2d) {
    g2d.setColor(new Color(0,0,0,Math.min(255,fadeOutCnt)));
    g2d.fillRect(0,0,1000,600);
    fadeOutCnt += 10;
  }
  
  /* getFadeOutCnt method
   * 
   * Purpose:
   * Returns the fade out count.
   */
  public int getFadeOutCnt() {
    return fadeOutCnt;
  }
  
  /*setFadeOutCnt method
   * 
   * Purpose:
   * Gets the fade in count.
   */
  public void setFadeOutCnt(int n) {
    fadeOutCnt = n;
  }
}