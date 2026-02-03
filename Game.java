import java.awt.*;
import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;

public class Game extends JFrame {
  JPanel jPanel = new JPanel();
  JLabel startButton = new JLabel();
  JLabel logo = new JLabel();
  JLabel car = new JLabel();
  JLabel gameOver = new JLabel();
  JLabel displayScore = new JLabel();
  int carPos = 330;
  boolean isGameOn = false;
  int score = 0;
  int highScore = 0;
  KeyListener arrowKeyListener;
  ActionListener moveLeftListener;
  ActionListener moveRightListener;
  
  public Timer moveLeft;
  public Timer moveRight;
  ObstaclesCreation createObstacle;

  public JLabel gameBg = new JLabel();
  
  // main game
  public Game() {
    jPanel.setLayout(null);
    java.net.URL url = ClassLoader.getSystemResource("images/icon.png");
    Toolkit kit = Toolkit.getDefaultToolkit();
    Image img = kit.createImage(url);
    this.setIconImage(img);
    readHighScore();

    // right or left movement
    moveRightListener = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if(carPos < 680) {
          carPos += 7;
        }
        car.setBounds(carPos, 700, 100, 168);
      }
    };
    moveLeftListener = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if(carPos > 30) {
          carPos -= 7;
        }
        car.setBounds(carPos, 700, 100, 168);
      }
    };

    moveLeft = new Timer(10, moveLeftListener);
    moveRight = new Timer(10, moveRightListener);
    
    // button
    ImageIcon startIcon = new ImageIcon(new ImageIcon("images/startbutton.png").getImage().getScaledInstance(369, 169, Image.SCALE_SMOOTH));
    startButton.setIcon(startIcon);
    startButton.setBounds(200, 620, 369, 169);

    // logo
    ImageIcon logoIcon = new ImageIcon(new ImageIcon("images/logo.png").getImage().getScaledInstance(500, 230, Image.SCALE_SMOOTH));
    logo.setIcon(logoIcon);
    logo.setBounds(150, 250, 500, 230);

    // car graphic
    ImageIcon carIcon = new ImageIcon(new ImageIcon("images/mcqueen.png").getImage().getScaledInstance(86, 168, Image.SCALE_SMOOTH));
    car.setIcon(carIcon);
    car.setVisible(false);
    car.setBounds(carPos, 700, 120, 168);

    // game over
    ImageIcon gameOverIcon = new ImageIcon(new ImageIcon("images/gameOver.png").getImage().getScaledInstance(500, 250, Image.SCALE_SMOOTH));
    gameOver.setIcon(gameOverIcon);
    gameOver.setBounds(150, 300, 500, 250);
    gameOver.setVisible(false);
  
    
    // JPanel bounds
    jPanel.setBounds(0, 0, 800, 1000);

    displayScore.setBounds(0, 0, 800, 50);
    displayScore.setBackground(Color.white);
    displayScore.setOpaque(true);

    displayScore.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
    Font scoreFont = new Font("Calibri", Font.PLAIN, 32);
    displayScore.setFont(scoreFont);

    addBackground();
    gameBg.add(startButton);
    gameBg.add(logo);
    gameBg.add(car);
    gameBg.add(displayScore);
    gameBg.add(gameOver);
    
    add(jPanel);

    // JFrame properties
    setSize(800, 1000);
    // setBackground(Color.GRAY);
    setTitle("Asphalt 0");
    setLocationRelativeTo(null);
    setFocusable(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);

    // detect start and arrow keys pressed
    arrowKeyListener = new KeyListener(){
      @Override
         public void keyPressed(KeyEvent e) {
             if (!isGameOn){
               if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) {
                 startGame();
                 logo.setVisible(false);
                 isGameOn = true;
               }
             }
             if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D){
                 moveRight.start();
             } 
             else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A){
                 moveLeft.start();
             }
         }
         @Override
         public void keyTyped(KeyEvent e) {
              //
         }
         @Override
         public void keyReleased(KeyEvent e) {
             if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D){
                 moveRight.stop();
             } 
             else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A){
                 moveLeft.stop();
             }
         }
       };
    addKeyListener(arrowKeyListener);
  }

  // starts the game
  private void startGame() {
    startButton.setVisible(false);
    car.setVisible(true);
    createObstacle = new ObstaclesCreation(gameBg, car, this);
    createObstacle.start();
  }
  
  // detects collisions
  public void onCollision() {
    System.out.println("GAME OVER!!!");
    createObstacle.stop();
    ImageIcon carIcon = new ImageIcon(new ImageIcon("images/explosion.png").getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH));
    car.setIcon(carIcon);
    car.setBounds(carPos, 700, 150, 150);

    gameOver.setVisible(true);

    removeKeyListener(arrowKeyListener);
    moveLeft.removeActionListener(moveLeftListener);
    moveRight.removeActionListener(moveRightListener);

    updateHighScore();
  }

  // reads high score
  private void readHighScore() {
    try {
      File myObj = new File("score.txt");
      Scanner scoreReader = new Scanner(myObj);
      highScore = Integer.parseInt(scoreReader.nextLine());
      scoreReader.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred while reading high score.");
      e.printStackTrace();
    }
  }

  // updates high score
  private void updateHighScore() {
    if(getScore() >= highScore) {
      try {
        FileWriter scoreWriter = new FileWriter("score.txt");
        scoreWriter.write(String.valueOf(getScore()));
        scoreWriter.close();
      } catch (IOException e) {
        System.out.println("An error occurred while updating highest score.");
        e.printStackTrace();
      }
    }
  }

  // updates score
  public void updateScore() {
    score += 10;

    if (score > highScore) {
      highScore = score;
    }

    displayScore.setText("    Score: " + String.valueOf(score) + "    High Score: " + String.valueOf(highScore));
  }

  // reads score
  public int getScore() {
    return score;
  }


  // ends game
  public void gameOver() {
    System.out.println(score);
  }

  // adds background
  public void addBackground() {
    ImageIcon background = new ImageIcon(new ImageIcon("images/roadFrames/roadFrame1.png").getImage().getScaledInstance(800, 1000, Image.SCALE_SMOOTH));
    gameBg.setIcon(background);
    gameBg.setBounds(-10, 0, 800, 1000);
    add(gameBg);
  }

  // changes background road frames
  public void updateBackground(int num) {
    ImageIcon background2 = new ImageIcon(new ImageIcon("images/roadFrames/roadFrame" + num + ".png").getImage().getScaledInstance(800, 1000, Image.SCALE_SMOOTH));
    gameBg.setIcon(background2);
  }
}
