/* Names: Nicole Han, Tracy Wang, Roseanna Liang
 * Teacher: Ms. Krasteva
 * Due Date: June 18, 2020
 * Class: Main class
 * 
 * This class is the Main class of the game. It creates the JFrame which
 * the game is displayed on, and starts the game.
 * 
 * Instance Variable Dictionary: N/A
 */

import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;

//https://youtu.be/-3KjiEga-cI
//https://docs.oracle.com/javase/tutorial/uiswing/events/mouselistener.html
//https://stackoverflow.com/questions/12396066/how-to-get-location-of-a-mouse-click-relative-to-a-swing-window
//https://examples.javacodegeeks.com/desktop-java/awt/event/simple-key-press-listener/
//https://stackoverflow.com/questions/6591832/translate-method-in-graphics
//http://www.java2s.com/Tutorials/Java/Graphics_How_to/Font/Load_font_from_ttf_file.htm
//https://gamedev.stackexchange.com/questions/105519/java-game-how-to-change-opacity-of-an-image/105522
//https://stackoverflow.com/questions/5895829/resizing-image-in-java

public class Main {
  public static void main(String[] args) {
    //create new JFrame
    JFrame f = new JFrame("Covid-19 Birthday Bash");
    f.setSize(1000,622);
    f.setResizable(false);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //create an ArrayList of all panels which will be added to the JFrame
    ArrayList<Scene> arr = new ArrayList<>();
    arr.add(new Splashscreen());
    arr.add(new MainMenu());
    arr.add(new LevelSelect());
    arr.add(new Level1());
    arr.add(new BoatRace());
    arr.add(new Level2());
    arr.add(new Basketball());
    arr.add(new NightAtMuseum());
    arr.add(new EndScreen());
    arr.add(new LevelEnd());
    arr.add(new CandyBobbing());
    arr.add(new WhackAMole());
    arr.add(new NightAtMuseum());
    arr.add(new Level3());
    arr.add(new SimonSays());
    for(Scene scene:arr) {
      scene.setArr(arr);
      scene.setFrame(f);
    }
    //start the game with the first scene
    f.add(arr.get(0));
    arr.get(0).start();
    //make JFrame visible
    f.setVisible(true);
  }
}