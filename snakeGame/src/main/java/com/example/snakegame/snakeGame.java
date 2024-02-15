package com.example.snakegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class snakeGame extends JFrame implements KeyListener {
    private final Rectangle scoreUnit;
    private static int score;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int unitSize = 20;
    private int snakeLength;
    private int[] snakeX, snakeY;
    private int baitX, baitY;
    private char direction;
    private boolean running;

    public snakeGame() {
        setTitle("Snake Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        addKeyListener(this);

        scoreUnit = new Rectangle();
        scoreUnit.add(scoreUnit);

        snakeX = new int[WIDTH * HEIGHT];
        snakeY = new int[WIDTH * HEIGHT];
        direction = 'R';
        running = true;

        startGame();
    }

    public void startGame() {
        snakeLength = 2;
        for (int i = 0; i < snakeLength; i++) {
            snakeX[i] = 60;
            snakeY[i] = 60;
            score = 0;
        }
        spawnBait();

        Thread thread = new Thread(this::gameLoop);
        thread.start();
    }
    public void gameOver() {
        dispose();
    }

    public void spawnBait() {
        baitX = (int) (Math.random() * (WIDTH / unitSize)) * unitSize;
        baitY = (int) (Math.random() * (HEIGHT / unitSize)) * unitSize;
    }

    public void gameLoop() {
        while(running) {
            move();
            checkBait();
            checkCollision();
            repaint();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        gameOver();
    }

    public void move() {
        for (int i = snakeLength; i > 0; i--) {
            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }

        switch (direction) {
            case 'U':
                snakeY[0] -= unitSize;
                break;
            case 'D':
                snakeY[0] += unitSize;
                break;
            case 'L':
                snakeX[0] -= unitSize;
                break;
            case 'R':
                snakeX[0] += unitSize;
                break;
        }
    }

    public void checkBait() {
        if (snakeX[0] == baitX && snakeY[0] == baitY) {
            snakeLength++;
            score++;
            spawnBait();
        }
    }

    public void checkCollision() {
        for (int i = snakeLength; i > 0; i--) {
            if (snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]) {
                running=false;
                System.out.println("Please,do not try to eat yourself :)\n");
                getScore();

            }
        }
        if (snakeX[0] < 0 || snakeX[0] >= WIDTH || snakeY[0] < 0 || snakeY[0] >= HEIGHT) {
            running = false;
            System.out.println("You need to stay away from invisible walls :)\n");
            getScore();
        }
    }

    public void paint(Graphics g) {
        g.clearRect(0, 0, WIDTH, HEIGHT);
        g.setColor(Color.BLACK);
        g.fillRect(baitX, baitY, unitSize,unitSize);
        for (int i = 0; i < snakeLength; i++) {
            g.setColor(Color.ORANGE);
            g.fillRect(snakeX[i], snakeY[i], unitSize,unitSize);
        }
    }

    public static void main(String[] args) {
        new snakeGame().setVisible(true);
    }

    public void getScore(){
        System.out.println("Your score is: "+score);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (direction != 'D')
                    direction = 'U';
                break;
            case KeyEvent.VK_DOWN:
                if (direction != 'U')
                    direction = 'D';
                break;
            case KeyEvent.VK_LEFT:
                if (direction != 'R')
                    direction = 'L';
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != 'L')
                    direction = 'R';
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
