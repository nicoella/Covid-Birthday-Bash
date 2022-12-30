import java.awt.*;
import javax.imageio.*;
import java.io.*;
import java.util.*;

public class Player {
  private String name;
  private Image[] left;
  private Image[] right;
  private Image[] front;
  private Image[] back;
  private String state;
  private int walkCnt;
  private long delay;
  private long dirDelay;
  private int x;
  private int y;
  private int walkSpeed;
  
  /* class constructor
   * 
   * Purpose:
   * Initializes instance variables
   */
  public Player(String name, int x, int y) {
    this.name = name;
    this.x = x;
    this.y = y;
    left = new Image[4];
    right = new Image[4];
    front = new Image[4];
    back = new Image[4];
    walkCnt = 0;
    state = "frontStill";
    delay = System.nanoTime();
    dirDelay = System.nanoTime();
    walkSpeed = 15;
    try { //try-catch block catches errors
      //reads all image files
      for(int i=0; i<4; i++) {
        left[i] = ImageIO.read(new File("data/characters/"+name+"/left"+i+".png")); 
        right[i] = ImageIO.read(new File("data/characters/"+name+"/right"+i+".png"));
        front[i] = ImageIO.read(new File("data/characters/"+name+"/front"+i+".png")); 
        back[i] = ImageIO.read(new File("data/characters/"+name+"/back"+i+".png"));  
      }
    } catch (IOException e) {}
  }
  
  /*setState method
   * 
   * Purpose:
   * Sets a new state for the character.
   */
  public void setState(String state) {
    this.state = state;
  }
  
  /*getState method
   * 
   * Purpose:
   * Gets the state of the character.
   */
  public String getState() {
    return state;
  }
  
  /*canWalk method
   * 
   * Purpose:
   * Checks if the character can walk in a certain direction.
   */
  public boolean canWalk(int newX, int newY, Player[] characters, int k) {
    if(k==0) { //coordinates must be shifted for the main character
      newX = (-1)*(newX-443);
      newY = (-1)*(newY-220);
    }
    //coordinates for the decorations in the background
    int[] obsx1 = {0,0,1296,0,340,0,776,1256,0,0,1168,1184};
    int[] obsy1 = {0,0,0,976,0,0,0,0,264,620,292,640};
    int[] obsx2 = {23,1320,1320,1320,559,67,1011,1320,135,131,1300,1300};
    int[] obsy2 = {1000,183,1000,1000,303,239,303,239,535,983,535,887};
    //for loop checks if character will walk into any of the decorations in the background
    for(int i=0; i<obsx1.length; i++) {
      if(((newX+105 >= obsx1[i] && newX+105 <= obsx2[i]) || (newX+5 >= obsx1[i] && newX+5 <= obsx2[i])) &&  (newY+160 >= obsy1[i] && newY+160 <= obsy2[i])) {
        return false;
      }
    }
    //for loop checks if the character will walk into any of the other characters
    for(int j=0; j<characters.length; j++) {
      if(j==k) continue;
      Player i = characters[j];
      int x = i.getX();
      int y = i.getY();
      if(j==0) {
        x = (-1)*(x-443);
        y = (-1)*(y-220);
      }
      //checks distances between the characters
      if((newX+100 >= x+15 && newX+100 <= x+100 && newY+160 >= y+130 && newY+160 <= y+180) || (newX+15 >= x+15 && newX+15 <= x+100 && newY+160 >= y+130 && newY+160 <= y+180)) {
        return false;
      }
    }
    return true;
  }
  
  /*setX method
   * 
   * Purpose:
   * Sets a new x-coordinate for the player.
   */
  public void setX(int x) {
    this.x = x;
  }
  
  /*getX method
   * 
   * Purpose:
   * Gets the x-coordinate of the player.
   */
  public int getX() {
    return x;
  }
  
  /*setY method
   * 
   * Purpose:
   * Sets a new y-coordinate for the player.
   */
  public void setY(int y) {
    this.y = y;
  }
  
  /*getY method
   * 
   * Purpose:
   * Gets the y-coordinate of the player.
   */
  public int getY() {
    return y;
  }
  
  /*getSpeed method
   * 
   * Purpose:
   * Gets the speed of the player.
   */
  public int getSpeed() {
    return walkSpeed;
  }
  
  /*updateWalk method
   * 
   * Purpose:
   * Checks if the character's walk animation should be updated.
   */
  public boolean updateWalk() {
    if((System.nanoTime()-delay)>200000000) { //updates every 0.2 seconds
      delay = System.nanoTime();
      return true;
    }
    return false;
  }
  
  /*updateDir method
   * 
   * Purpose:
   * Updates the direction of the character's walk.
   */
  public void updateDir(boolean noDelay) {
    String[] states = {"leftStill","leftWalk","rightStill","rightWalk","frontStill","frontWalk","backStill","backWalk"}; //possible directions
    if(!noDelay || (System.nanoTime()-dirDelay)>3000000000L) { //only update if no delay is required or delay of 3 seconds met
      int rand = (int)(Math.random()*3); //random number from 0 - 2
      if(rand==0) { //character has a 1/3 chance of updating direction
        dirDelay = System.nanoTime();
        rand = (int)(Math.random()*states.length);
        state = states[rand];
      }
    }
  }
  
  /* drawPlayer method
   * 
   * Purpose:
   * Draws the character.
   */
  public void drawPlayer(Graphics2D g2d) {
    if(state.equals("leftStill")) { //character is facing left
      walkCnt=0;
      g2d.drawImage(left[0],x,y,null);
    } else if(state.equals("leftWalk")) { //character is walking left
      if(updateWalk()) { 
        walkCnt++;
        walkCnt%=4;
      }
      g2d.drawImage(left[walkCnt],x,y,null);
    } else if(state.equals("rightStill")) { //character is facing right
      walkCnt=0;
      g2d.drawImage(right[0],x,y,null);
    } else if(state.equals("rightWalk")) { //character is walking right
      if(updateWalk()) { 
        walkCnt++;
        walkCnt%=4;
      }
      g2d.drawImage(right[walkCnt],x,y,null);
    } else if(state.equals("frontStill")) { //character is facing forward
      walkCnt=0;
      g2d.drawImage(front[0],x,y,null);
    } else if(state.equals("frontWalk")) { //character is walking forward
      if(updateWalk()) { 
        walkCnt++;
        walkCnt%=4;
      }
      g2d.drawImage(front[walkCnt],x,y,null);
    } else if(state.equals("backStill")) { //character is facing back
      walkCnt=0;
      g2d.drawImage(back[0],x,y,null);
    } else if(state.equals("backWalk")) { //character is walking back
      if(updateWalk()) { 
        walkCnt++;
        walkCnt%=4;
      }
      g2d.drawImage(back[walkCnt],x,y,null);
    } 
  }
  
  /*tooClose method
   * 
   * Purpose:
   * Checks if a character is too close to any other character.
   */
  public boolean tooClose(Player other) {
    int x1 = x + 60;
    int y1 = y + 90;
    if(name.equals("main")) {
      x1 = (-1)*(x-443) + 60;
      y1 = (-1)*(y-220) + 90;
    }
    int x2 = other.getX() + 60;
    int y2 = other.getY() + 90;
    double dist = Math.sqrt(Math.pow(x1-x2,2) + Math.pow(y1-y2,2));
    if(dist <= 200) return true;
    return false;
  }
  
  /*drawAll method
   * 
   * Purpose:
   * Draws all characters in their position.
   */
  public static void drawAll(Graphics2D g2d, Player[] characters) {
    Player[] temp = characters.clone();
    Arrays.sort(temp, new Compare()); //sort players
    //draw players from top to bottom
    for(int i=0; i<temp.length; i++) {
      Player j = temp[i];
      j.drawPlayer(g2d);
    }
  }
  
  //compare class is used in sorting players
  static class Compare implements Comparator<Player> {
    public int compare(Player a, Player b) { //sorts players by y-coordinate
      return a.y - b.y;
    }
  }
}