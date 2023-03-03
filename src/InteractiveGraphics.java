import sun.swing.BakedArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.tools.Tool;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;


public class InteractiveGraphics extends Canvas implements Runnable{
    private BufferStrategy bs;
    private boolean running = false;
    private Thread thread;


    private BufferedImage Horse;
    private int HorseVX = 0;
    private int HorseVY = 0;
    private Rectangle horseHitbox;


    private BufferedImage Background;

    private BufferedImage bitcoincoin;
    private Rectangle bitcoincoinHitbox;


    public InteractiveGraphics() {
        try {
            Horse = ImageIO.read(getClass().getResource("Horse.png"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        horseHitbox = new Rectangle(100, 100, Horse.getWidth()/10, Horse.getHeight()/10);

        try {
            bitcoincoin = ImageIO.read(getClass().getResource("bitcoincoin.png"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bitcoincoinHitbox = new Rectangle( (int)Math.random()*1860, (int)Math.random()*1020, bitcoincoin.getWidth()/8, bitcoincoin.getHeight()/8);

        try {
            Background = ImageIO.read(getClass().getResource("Stonks.png"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        JFrame frame = new JFrame();
        frame.add(this);
        this.addKeyListener(new MyKeyListener());
        requestFocus();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void render() {
        bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        draw(g);

        g.dispose();
        bs.show();
    }

    public void draw(Graphics g) {
        g.clearRect(0,0,getWidth(),getHeight());
        g.drawImage(Background, 0, 0, Background.getWidth(), Background.getHeight(), null);
        g.drawImage(bitcoincoin, bitcoincoinHitbox.x, bitcoincoinHitbox.y, bitcoincoin.getWidth()/8, bitcoincoin.getHeight()/8,null);
        g.drawImage(Horse, horseHitbox.x, horseHitbox.y, Horse.getWidth()/10, Horse.getHeight()/10, null);
    }

    private void update() {
        horseHitbox.x += HorseVX;
        horseHitbox.y += HorseVY;
        if (bitcoincoinHitbox.intersects(horseHitbox)) {
            bitcoincoinHitbox.x = (int) (Math.random()*1860);
            bitcoincoinHitbox.y = (int) (Math.random()*1020);
        }
    }

    public static void main(String[] args) {
        InteractiveGraphics minGrafik = new InteractiveGraphics();
        minGrafik.start();
    }

    public synchronized void start() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        double ns = 1000000000.0 / 100.0;
        double delta = 0;
        long lastTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while(delta >= 1) {
                update();
                render();
                delta--;
            }
        }
        stop();
    }

    public class MyKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyChar() == 'a') {
                HorseVX = -12;
            }
            if (e.getKeyChar() == 'd') {
                HorseVX = 12;
            }
            if (e.getKeyChar() == 'w') {
                HorseVY = -12;
            }
            if (e.getKeyChar() == 's') {
                HorseVY = 12;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyChar() == 'a') {
                HorseVX = 0;
            }
            if (e.getKeyChar() == 'd') {
                HorseVX = 0;
            }
            if (e.getKeyChar() == 'w') {
                HorseVY = 0;
            }
            if (e.getKeyChar() == 's') {
                HorseVY = 0;
            }
        }
    }
}