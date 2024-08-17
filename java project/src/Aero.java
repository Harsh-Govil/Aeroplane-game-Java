import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Aero extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 760;
    int boardHeight = 640;

    //images
    Image backgroundImg;
    Image aeroImg;
    Image topPipeImg;
    Image bottomPipeImg;

    //bird class
    int aeroX = boardWidth / 8;
    int aeroY = boardWidth / 2;
    int aeroWidth = 34;
    int aeroHeight = 24;

    class AeroBird {
        int x = aeroX;
        int y = aeroY;
        int width = aeroWidth;
        int height = aeroHeight;
        Image img;

        AeroBird(Image img) {
            this.img = img;
        }
    }

    //pipe class
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    class Pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;

        Pipe(Image img) {
            this.img = img;
        }
    }

    //game logic
    AeroBird aero;
    int velocityX = -4;
    int velocityY = 0;
    int gravity = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameLoop;
    Timer placePipeTimer;
    boolean gameOver = false;
    double score = 0;
    
    JButton playAgainButton;

    public Aero() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);

        //load images
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        aeroImg = new ImageIcon(getClass().getResource("./aero.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./hello.jpg")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./hello.jpg")).getImage();

        //bird
        aero = new AeroBird(aeroImg);
        pipes = new ArrayList<>();

        //place pipes timer
        placePipeTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        placePipeTimer.start();

        //game timer
        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();

        //play again button
        playAgainButton = new JButton("Play Again");
        playAgainButton.setFont(new Font("Arial", Font.PLAIN, 20));
        playAgainButton.setBounds(boardWidth / 2 - 75, boardHeight / 2, 150, 50);
        playAgainButton.setVisible(false);
        playAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });
        add(playAgainButton);
        setLayout(null);
    }

    void placePipes() {
        int randomPipeY = (int) (pipeY - pipeHeight / 4 - Math.random() * (pipeHeight / 2));
        int openingSpace = boardHeight / 4;

        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        //background
        g.drawImage(backgroundImg, 0, 0, this.boardWidth, this.boardHeight, null);

        //bird
        g.drawImage(aero.img, aero.x, aero.y, aero.width, aero.height, null);

        //pipes
        for (Pipe pipe : pipes) {
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        //score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameOver) {
            g.drawString("Game Over!", 10, 35);
            g.drawString("Your Score: " + (int) score, 10, 70);
            playAgainButton.setVisible(true);
        } else {
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    public void move() {
        //bird
        velocityY += gravity;
        aero.y += velocityY;
        aero.y = Math.max(aero.y, 0);

        //pipes
        for (Pipe pipe : pipes) {
            pipe.x += velocityX;

            if (!pipe.passed && aero.x > pipe.x + pipe.width) {
                score += 0.5;
                pipe.passed = true;
            }

            if (collision(aero, pipe)) {
                gameOver = true;
            }
        }

        if (aero.y > boardHeight) {
            gameOver = true;
        }
    }

    boolean collision(AeroBird a, Pipe b) {
        return a.x < b.x + b.width &&
               a.x + a.width > b.x &&
               a.y < b.y + b.height &&
               a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            placePipeTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -9;

            if (gameOver) {
                restartGame();
            }
        }
    }

    private void restartGame() {
        // Reset the game state
        aero.y = aeroY;
        velocityY = 0;
        pipes.clear();
        gameOver = false;
        score = 0;
        gameLoop.start();
        placePipeTimer.start();
        playAgainButton.setVisible(false);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
