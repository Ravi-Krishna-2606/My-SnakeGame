import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener{
    private static final int WIDTH=600;
    private static final int HEIGHT=400;
    private static final int UNIT_SIZE=20;
    private static final int GAME_UNITS=(HEIGHT*WIDTH)/(UNIT_SIZE*UNIT_SIZE);
    private static final int DELAY=100;

    private final int[] snakeX=new int[GAME_UNITS];
    private final int[] snakeY=new int[GAME_UNITS];
    private int snakeLength=6;
    private int appleX;
    private int appleY;
    private Direction direction=Direction.RIGHT;
    private boolean isRunning=false;
    private Timer timer;

    public SnakeGame()
    {
        setPreferredSize(new Dimension(WIDTH,HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        setLayout(null);

        addKeyListener(new MyKeyAdapter());

        startGame();

    }

    public void startGame()
    {
        newApple();
        isRunning=true;
        timer=new Timer(DELAY,this);
        timer.start();
        snakeX[0]=0;
        snakeY[0]=0;
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g)
    {
        if(isRunning)
        {
            g.setColor(Color.RED);
            g.fillOval(appleX,appleY,UNIT_SIZE,UNIT_SIZE);

            for(int i=0;i<snakeLength;i++)
            {
                if(i==0)
                {
                    g.setColor(Color.GREEN);
                }
                else
                {
                    g.setColor(Color.GREEN.darker());
                }
                g.fillRect(snakeX[i],snakeY[i],UNIT_SIZE,UNIT_SIZE);
            }
            Toolkit.getDefaultToolkit().sync();
        }
        else
        {
            gameOver(g);
        }
    }

    public void newApple()
    {
        appleX=(int)(Math.random()*(WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY=(int)(Math.random()*(HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }

    public void move()
    {
        for(int i=snakeLength;i>0;i--)
        {
            snakeX[i]=snakeX[i-1];
            snakeY[i]=snakeY[i-1];
        }

        switch(direction)
        {
            case UP:
                snakeY[0]-=UNIT_SIZE;
                break;
            case DOWN:
                snakeY[0]+=UNIT_SIZE;
                break;
            case LEFT:
                snakeX[0]-=UNIT_SIZE;
                break;
            case RIGHT:
                snakeX[0]+=UNIT_SIZE;
                break;
        }
    }

    public void checkCollision()
    {
        for(int i=snakeLength;i>0;i--)
        {
            if(snakeX[0]==snakeX[i] && snakeY[0]==snakeY[i])
            {
                isRunning=false;
                break;
            }
        }

        if(snakeX[0]<0 ||snakeX[0]>=WIDTH || snakeY[0]<0 || snakeY[0]>=HEIGHT)
        {
            isRunning=false;
        }

        if(!isRunning)
        {
            timer.stop();
        }
    }

    public void checkApple()
    {
        if(snakeX[0]==appleX && snakeY[0]==appleY)
        {
            snakeLength++;
            newApple();
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        if(isRunning)
        {
            move();
            checkCollision();
            checkApple();
        }
        repaint();
    }

    public void gameOver(Graphics g)
    {
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free",Font.BOLD,40));
        FontMetrics metrics =getFontMetrics(g.getFont());
        g.drawString("Game Over", (WIDTH-metrics.stringWidth("Game Over"))/2, HEIGHT/2);
    }

    private class MyKeyAdapter extends KeyAdapter{
        public void keyPressed(KeyEvent e)
        {
            switch(e.getKeyCode())
            {
                case KeyEvent.VK_LEFT:
                  if(direction!=Direction.RIGHT)
                     direction=Direction.LEFT;
                     break;

                 case KeyEvent.VK_RIGHT:
                   if(direction!=Direction.LEFT)
                      direction=Direction.RIGHT;
                      break;
                  case KeyEvent.VK_UP:
                      if(direction!=Direction.DOWN)
                         direction=Direction.UP;
                         break;
                  case KeyEvent.VK_DOWN:
                      if(direction!=Direction.UP)
                         direction=Direction.DOWN;
                         break;
            }
        }
    }

    public static void main(String[]args)
    {
        JFrame frame=new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new SnakeGame());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
 
    enum Direction{
        UP,DOWN,LEFT,RIGHT;
    }

}