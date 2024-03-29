import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;
import java.util.*;

public class Level2 extends Level {
  private String state;
  private Player[] characters;
    
  /* class constructor
   * 
   * Purpose:
   * Initializes instance variables
   */
  public Level2() {
    super(); //calls the Level class constructor
    reset(); //reset method initializes variables which need to be reset every time
    setCurrLevel(5); //sets the current playing level to 5 (the array index of level2)
  }
  
  /*reset method
   * 
   * Purpose:
   * resets all the instance variables for the game to begin again
   */
  public void reset() {
    super.reset();
    //initializes an array of all characters in the game, with index 0 being the player's character
    characters = new Player[]{new Player("main",-160,-400),new Player("person1",853,740), new Player("person2",956,424), new Player("person3",490,809), new Player("person4",1006,583), new Player("person5",435,626), new Player("person6",749,357), new Player("person7",331,312), new Player("person8",311,469), new Player("person9",352,796)};
    super.setCharacters(characters);
    state = "game1";
  }
  
  /* paintComponent method
   * 
   * purpose:
   * This methods performs the graphics of the game.
   */
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D)g;
    if(state.equals("game1")) { //first social distancing game period
      requestFocus();
      if(getFadeInCnt()<=0 && getGameTime()==-1) setGameTime(System.nanoTime()+20*1000000000L);
      //change state when timer runs out
      if(getGameTime()!=-1 && getGameTime()-System.nanoTime() <= 0) {
        state = "minigame1";
        super.setCharacters(new Player[]{new Player("main",-1000,-1000)});
      }
    } else if(state.equals("minigame1")) { //first minigame
      state = "game2";
      setGameTime(-1);
      transition(5,10);
    } else if(state.equals("game2")) { //second social distancing game period
      if(getGameTime()==-1) {
        requestFocus();
        setGameTime(System.nanoTime()+25*1000000000L);
        super.setCharacters(characters);
      }
      //change state when timer runs out
      if(getGameTime()-System.nanoTime() <= 0) {
        state = "minigame2";
        super.setCharacters(new Player[]{new Player("main",-1000,-1000)});
      }
    } else if(state.equals("minigame2")) { //second minigame
      state = "game3";
      setGameTime(-1);
      transition(5,6);
    } else if(state.equals("game3")) { //third social distancing game period
      if(getGameTime()==-1) {
        requestFocus();
        setGameTime(System.nanoTime()+30*1000000000L);
        super.setCharacters(characters);
      }
      //change state when timer runs out
      if(getGameTime()-System.nanoTime() <= 0) {
        state = "end";
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
      transition(5,9);
      ArrayList arr = getArr();
      //sets score for the level
      ((LevelEnd)arr.get(9)).setScore(((CandyBobbing)arr.get(10)).getRank()+((Basketball)arr.get(6)).getRank(),20,getLevelFailed());
      reset(); //resets level
    }
  }
  

}