import java.awt.*;
import javax.swing.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Obstacles {
    String[] obsArray = {"obstacle1.png", "obstacle2.png", "obstacle3.png", "obstacle4.png"};
    int[] obsWidth = {150, 150, 80, 110};
    int[] obsHeight = {150, 150, 141, 97};

    Random randX = new Random();
    int obsPos = randX.nextInt(600) + 50;

    Random randObs = new Random();
    int obsInd = randObs.nextInt(obsArray.length);

    JLabel obsItem = new JLabel();

    int totalHeight = 1000;
    int initYPos = 0;

    Timer timeVal = new Timer();

    int obsIndex = 0;

    JLabel gameBgRef;
    JLabel carRef;
    Game gameRef;

    // creates single obstacle
    public Obstacles(JLabel gameBg, JLabel car, Game game) {
        gameBgRef = gameBg;
        carRef = car;
        gameRef = game;
    }

    // moves the obstacles
    public void moveObstacle() {

        TimerTask task;

        task = new TimerTask() {
            @Override
            public void run() {
                initYPos += 3;
                obsItem.setBounds(obsPos, initYPos, obsWidth[obsInd], obsHeight[obsInd]);
                Rectangle carBounds = carRef.getBounds();
                Rectangle obsItemBounds = obsItem.getBounds();
                if(obsItemBounds.intersects(carBounds)) {
                    System.out.println("collision detected");
                    stopTimer();
                    gameRef.onCollision();
                    
                }
                if(initYPos > 1200) {
                    stopTimer();
                    gameBgRef.remove(5);   // assuming the oldest obstacle on screen will be at index 5
                }
            }
        };
        timeVal.schedule(task, 10, 10);
    }

    // stops obstacles from moving upon game over
    public void stopTimer() {
        timeVal.cancel();
    }

    // add random images to obstacles
    public JLabel getObstacles() {
        ImageIcon obsIcon = new ImageIcon(new ImageIcon("images/" + obsArray[obsInd]).getImage().getScaledInstance(obsWidth[obsInd], obsHeight[obsInd], Image.SCALE_SMOOTH));
        obsItem.setIcon(obsIcon);
        obsItem.setBounds(obsPos, initYPos, obsWidth[obsInd], obsWidth[obsInd]);
        moveObstacle();
        return obsItem;
    }

    // gives index
    public void setIndex(int index) {
        obsIndex = index;
    }

    // return index
    public int getIndex() {
        return obsIndex;
    }

    
}
