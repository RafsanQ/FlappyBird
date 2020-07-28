package Flappy_Bird;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class Main implements ActionListener, MouseListener, KeyListener {
    public static Main flappybird;
    public final int width = 1280, height = 720;
    public int ticks, y_motion = 0, score = 0, scr = 0;
    public boolean GameOver = false, initiate = false, initiate2 = false;
    public Rectangle bird;
    public ArrayList<Rectangle> columns;
    public Random rand;
    public Main(){
        JFrame jframe = new JFrame();
        jframe.setTitle("Flap Flap by RafsanQ");
        Timer timer = new Timer(20, this);
        renderer = new Renderer();
        jframe.add(renderer);
        jframe.addMouseListener(this);
        jframe.addKeyListener(this);
        jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jframe.setSize(width, height);
        jframe.setResizable(false);
        jframe.setVisible(true);
        rand = new Random();
        bird = new Rectangle(width/3 - 10, height/2 - 10, 20, 20 );
        columns = new ArrayList<Rectangle>();
        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);
        timer.start();
    }
    public Renderer renderer;

    public void paintColumn(Graphics g, Rectangle column){
        g.setColor(Color.green.darker());
        g.fillRect(column.x, column.y, column.width, column.height);
    }

    public void addColumn(boolean start){
        int space_c = 300;
        int width_c = 100;
        int height_c = 50 + rand.nextInt(300);
        if (start) {
            columns.add(new Rectangle(width + width_c + columns.size() * 200, height - height_c - 120, width_c, height_c));
            columns.add(new Rectangle(width + width_c + (columns.size() - 1) * 200, 0, width_c, height - height_c - space_c));
        }
        else {
            columns.add(new Rectangle(columns.get(columns.size() - 1).x + 400, height - height_c - 120, width_c, height_c));
            columns.add(new Rectangle(columns.get(columns.size() - 2).x + 400, 0, width_c, height - height_c - space_c));
        }
    }
    public void repaint(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillRect(0,0, width, height);
        g.setColor(Color.green);
        g.fillRect(0, height - 130, width, 20);
        g.setColor(Color.orange.darker().darker());
        g.fillRect(0, height - 120, width, height);
        g.setColor(Color.black);
        g.fillRect(bird.x, bird.y, bird.width, bird.height);
        for (Rectangle column : columns) {
            paintColumn(g,column);
        }
        g.setColor(Color.white);
        g.setFont(new Font("Arial", 1, 100));
        if (!initiate && !initiate2) {
            g.drawString("Click to Start", width/4, height/2 - 50);
        }
        if (GameOver) {
            g.setColor(Color.BLACK);
            g.drawString("Game Over !!!", width/4, height/2 - 50);
        }
        g.setColor(Color.white);
        g.drawString("Score: " + score, width - 500, 90);
    }

    public void jump(){
        if (GameOver) {
            bird = new Rectangle(width/2 - 10, height/2 - 10, 20, 20 );
            columns = new ArrayList<Rectangle>();
            y_motion = 0;
            score = 0;
            columns.clear();
            addColumn(true);
            addColumn(true);
            addColumn(true);
            addColumn(true);
            GameOver = false;
        }
        if (!initiate) {
            initiate = true;
            initiate2 = true;
        }
        else if (!GameOver){
            if (y_motion > 0)
                y_motion = 0;
            y_motion -= 10;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        int speed = 10;
        ticks++;
        if (initiate) {
            for (int i = 0; i < columns.size(); i++) {
                Rectangle column = columns.get(i);
                column.x -= speed;
            }
            if (ticks % 2 == 0 && y_motion <= 15) {
                y_motion += 2;
            }
            for (int i = 0; i < columns.size(); i++) {
                Rectangle column = columns.get(i);
                if (column.x + column.width < 0) {
                    columns.remove(column);
                    if (column.y == 0) {
                        addColumn(false);
                    }
                }
            }

            bird.y += y_motion;
            for (Rectangle column : columns) {
                if (bird.x + bird.width/2 > column.x + column.width/2 - 5 && bird.x + bird.width/2 < column.x + column.width/2 + 5){
                    scr++;
                }
                if (column.intersects(bird)) {
                    GameOver = true;
                    initiate = false;
                }
                if (scr > 1) {
                    score++;
                    scr = 0;
                }
            }
            if (bird.y > height - 120 || bird.y < 0) {
                GameOver = true;
                initiate = false;
                bird.y = height - 120 - bird.height;
            }
            renderer.repaint();
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        jump();
    }
    @Override
    public void mousePressed(MouseEvent e) {
    }
    @Override
    public void mouseReleased(MouseEvent e) {
    }
    @Override
    public void mouseEntered(MouseEvent e) {
    }
    @Override
    public void mouseExited(MouseEvent e) {
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
            jump();
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }
    public static void main(String[] args) {
        flappybird = new Main();
    }
}
