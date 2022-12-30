import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;
import java.util.*;

public class Level1 extends Level {
  private String state;
  private Player[] characters;
  private boolean[] keyPressed;

  /* class constructor
   * 
   * Purpose:
   * Initializes all instance variables.
   * Creates a key listener which has a method keyPressed which
   * is called when a key is pressed.   
   */
  public Level1() {
    super(); //calls the Level class constructor
    reset(); //reset method initializes variables which need to be reset every time
    //keylistener which is called when a key is pressed
    addKeyListener(new KeyAdapter() {
      //keyPressed method is called when a key is pressed
      public void keyPressed(KeyEvent e) {
        if(e.getKeyChar()=='d' || e.getKeyChar()=='D') { //user is pressing D
          keyPressed[0] = true;
        } else if(e.getKeyChar()=='a' || e.getKeyChar()=='A') { //user is pressing A
          keyPressed[1] = true;
        } else if(e.getKeyChar()=='w' || e.getKeyChar()=='W') { //user is pressing W
          keyPressed[2] = true;
        } else if(e.getKeyChar()=='s' || e.getKeyChar()=='S') { //user is pressing S
          keyPressed[3] = true;
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
    super.reset();
    //initializes an array of all characters in the game, with index 0 being the player's character
    characters = new Player[]{new Player("main",-160,-400)};
    super.setCharacters(characters);
    characters = new Player[]{new Player("main",-160,-400),new Player("person1",853,740), new Player("person2",956,424), new Player("person3",490,809), new Player("person4",1006,583), new Player("person5",435,626), new Player("person6",749,357), new Player("person7",331,312), new Player("person8",311,469), new Player("person9",352,796)};
    state = "keyboard";
    keyPressed = new boolean[4];
  }
  
  /* paintComponent method
   * 
   * purpose:
   * This methods performs the graphics of the game.
   */
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D)g;
    if(state.equals("keyboard")) { //introduction to keyboard movement
      requestFocus();
      //draw text on text box
      String str = "You're turning 5, and it's your birthday party! Try moving around using the keys WASD.";
      if(getFadeInCnt()<=0) displayText(str,g2d); //only display text when screen has fully faded in
      //change scenes when text has finished displaying and user has tried moving in all four direction
      if(getTextFinished() && getFadeInCnt()<=0 && keyPressed[0] && keyPressed[1] && keyPressed[2] && keyPressed[3]) {
        state = "timer";
        clearText();
      }
    } else if(state.equals("timer")) { //introduction to the game timer
      //draw text on text box
      String str = "Great! Take a look at the game timer, in the top right corner. It shows when the next event will start! Take a look around the room until the next event starts.";
      displayText(str,g2d);
      if(getGameTime()==-1 || !getTextFinished()) setGameTime(System.nanoTime()+26*1000000000L); //set the game timer to 25 seconds
      //change scenes when the game timer has finished
      if(getGameTime()-System.nanoTime() <= 0) {
        state = "people";
        clearText();
        setGameTime(System.nanoTime()+31*1000000000L);
        super.setCharacters(characters);
      }
    } else if(state.equals("people")) { //introduction to social distancing
      //draw text on text box
      String str = "The other kids are arriving soon! However, you must social distance, because of the covid-19 virus. The bar in the top right corner is your health. It decreases if you are too close to the other kids! If the bar runs out, the game is over. Try to social distance until the next event starts.";
      displayText(str,g2d);
      //change scenes when the timer has finished
      if(getGameTime()-System.nanoTime() <= 0) {
        state = "minigame";
        clearText();
        super.setCharacters(new Player[]{new Player("main",-1000,-1000)});
      }
    } else if(state.equals("minigame")) { //first minigame
      state = "socialdistance";
      setGameTime(-1);
      transition(3,4);
    } else if(state.equals("socialdistance")) { //social distancing period
      if(getGameTime()==-1) {
        requestFocus();
        setGameTime(System.nanoTime()+26*1000000000L);
        super.setCharacters(characters);
      }
      //change scenes when the timer has finished
      if(getGameTime()-System.nanoTime() <= 0) {
        state = "end";
        clearText();
        setFadeOutCnt(0);
      }
    }
    
    //check if level has been failed
    if(getLevelFailed() && getFadeOutCnt()==-1) {
      setFadeOutCnt(0);
    }
    //draw scene transitions
    if(getFadeInCnt()>0) fadeIn(g2d); //fade in
    if(getFadeOutCnt()>=0 && getFadeOutCnt()<255) fadeOut(g2d);
    else if(getFadeOutCnt()!=-1) {
      transition(3,9);
      ArrayList arr = getArr();
      //sets score for the level
      ((LevelEnd)arr.get(9)).setScore(((BoatRace)arr.get(4)).getRank(),10,getLevelFailed());
      reset(); //resets level
    }
  }
}