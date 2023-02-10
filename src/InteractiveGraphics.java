import sun.security.ssl.HandshakeOutStream;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class InteractiveGraphics extends Canvas implements Runnable{
    private BufferStrategy bs;

    private boolean running = false;
    private Thread thread;

    private BufferedImage Horse;

    private int HorseX = 100;
    private int HorseY = 100;
    private int HorseVX = 0;
    private int HorseVY = 0;

    private Rectangle horseHitbox;
   // private int x = 0;
   // private int y = 0;
   // private Rectangle hitbox = new Rectangle(x,y,30,30);


    private BufferedImage bitcoincoin;
    private int bitcoincoinX = (int) (Math.random()*560);
    private int bitcoincoinY = (int) (Math.random()*360);
    private Rectangle bitcoincoinHitbox = new Rectangle(bitcoincoinX, bitcoincoinY, bitcoincoin.getWidth(), bitcoincoin.getHeight());
    //private Rectangle target = new Rectangle(targetX, targetY, 40,40);

    public InteractiveGraphics() {
        try {
            Horse = ImageIO.read(new File("Horse.png"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        horseHitbox = new Rectangle(HorseX, HorseY, Horse.getWidth(), Horse.getHeight());
        try {
            bitcoincoin = ImageIO.read(new File("bitcoincoin.png"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setSize(600,400);
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

        // Rita ut den nya bilden
        draw(g);

        g.dispose();
        bs.show();
    }

    public void draw(Graphics g) {
        g.clearRect(0,0,getWidth(),getHeight());
        //g.setColor(targetColor);
        //g.fillRect(target.x, target.y, target.width, target.height);
        //g.setColor(new Color(0xBE5817));
        //g.fillOval(hitbox.x,hitbox.y,hitbox.width,hitbox.height);
        g.fillRect(horseHitbox.x, horseHitbox.y, horseHitbox.width, horseHitbox.height);
        g.fillRect(bitcoincoinHitbox.x, bitcoincoinHitbox.y, bitcoincoinHitbox.width, bitcoincoinHitbox.height);

        g.drawImage(bitcoincoin, bitcoincoinX, bitcoincoinY, bitcoincoin.getWidth()/8, bitcoincoin.getHeight()/8,null);
        g.drawImage(Horse, HorseX, HorseY, Horse.getWidth()/10, Horse.getHeight()/10, null);
    }

    private void update() {
        HorseX += HorseVX;
        HorseY += HorseVY;
        // if (target.intersects(hitbox)) {
            //target.x = (int) (Math.random()*560);
            //target.y = (int) (Math.random()*360);
       // }
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
        double ns = 1000000000.0 / 25.0;
        double delta = 0;
        long lastTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while(delta >= 1) {
                // Uppdatera koordinaterna
                update();
                // Rita ut bilden med updaterad data
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
                HorseVX = -5;
            }
            if (e.getKeyChar() == 'd') {
                HorseVX = 5;
            }
            if (e.getKeyChar() == 'w') {
                HorseVY = -5;
            }
            if (e.getKeyChar() == 's') {
                HorseVY = 5;
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