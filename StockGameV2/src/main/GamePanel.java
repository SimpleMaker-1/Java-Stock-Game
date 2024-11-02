package main;

import Inputs.KeyInputs;
import Stocks.Graph;
import Stocks.Player;
import background.BackGround;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable{

    //images
    public BufferedImage graph;
    public BufferedImage backGroundImg;

    // game classes
    public Thread thread;
    public KeyInputs keys;
    public Graph stocks;
    public BackGround backGround;
    public Player player;

    //logic variables
    public Random rand;
    public int updateTimer , updateAt = 180; // time in frames, 120 in a second
    public int backGroundTimer , backGroundUpdateAt = 3; // time in frames, 120 in a second
    public double lastPrice = 50.0;
    public double price = 50;
    public double percent;
    private final double BUY_SELL_SPEED = 0.2; // dollars per frame you can buy / sell
    private final double GOAL = 1000.0;
    private boolean won = false;

    public GamePanel(){
        instantiate(); // easily instantiates all major classes and values
        this.setPreferredSize(new Dimension(1280, 720));
        this.setDoubleBuffered(true);
        this.addKeyListener(keys);
        this.setFocusable(true);
    }

    private void instantiate() {

        keys = new KeyInputs();
        player = new Player(100); // sets initial player fund, less money = harder
        rand = new Random();
        stocks = new Graph(150);
        backGround = new BackGround(1);
        backGroundImg = backGround.nextImg();

    }

    public String formattedNum(double num){
        String input = Double.toString(num);
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        Number parsedNumber = null;
        try {
            parsedNumber = numberFormat.parse(input.replaceAll(",", ""));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        double roundedNumber = Math.round(parsedNumber.doubleValue() * 100.0) / 100.0;
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        String formattedNumber = decimalFormat.format(roundedNumber);

        return formattedNumber;
    }

    @Override
    public void run() { // calls update then paint every frame, 120 frames per second
        double drawInterval = (double) 1000000000 / 120;

        double delta = 0;
        int frames = 0; // fps counter
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;

        while (thread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime)/drawInterval;
            timer += (currentTime-lastTime);
            lastTime = currentTime;

            if(delta >=1){
                update();

                repaint();
                delta--;
                frames++;
            }

            if(timer >=1000000000){
                System.out.println("fps: "+ frames);
                frames = 0;
                timer = 0;
            }
        }
    }

    private void update() {
        if(!won){
            updateTimer++;
            if(updateTimer>=updateAt){
                int temp = rand.nextInt(101)-50;

                price -= (double) temp /3;
                if(price > 100){
                    price = 100;
                }
                if(price < 10){
                    price = 10;
                }
                percent = price / lastPrice;
                adjustInvested();
                lastPrice = price;
                graph = stocks.addLine(temp);
                updateTimer = 0;
            }

            backGroundTimer++;
            if(backGroundTimer>=backGroundUpdateAt){
                backGroundImg = backGround.nextImg();
                backGroundTimer = 0;
            }

            if(keys.up && !keys.down){
                buy();
            }
            if(keys.down && !keys.up){
                sell();
            }

            if(player.getFund() + player.getInvestments() >= GOAL){
                won = true;
            }
        }
        else{
            
        }
    }

    public void adjustInvested(){
        player.setInvestments(player.getInvestments() * percent);
    }

    public void buy(){
        double temp;
        if( BUY_SELL_SPEED > player.getFund()){
            temp = player.getFund();
        }
        else{
            temp = BUY_SELL_SPEED;
        }
        player.setInvestments(player.getInvestments() + temp );
        player.setFund(player.getFund() - temp);
    }
    public void sell(){
        double temp = 0;
        if( BUY_SELL_SPEED > player.getInvestments()){
            temp = player.getInvestments();
        }
        else{
            temp = BUY_SELL_SPEED;
        }
        player.setInvestments(player.getInvestments() - temp );
        player.setFund(player.getFund() + temp);

    }

    public void drawText(Graphics g){
        String bank = "Bank: $" + formattedNum(player.getFund());
        String invested = "Invested: $" + formattedNum(player.getInvestments());
        String stockPrice = "Stock Price: $" + formattedNum(price);
        String goal = "Goal: $10,000.00";
        String stockBase = "Baseline Price: $50.00";

        g.setFont(new Font("Courier New", Font.BOLD,20));
        g.setColor(Color.white);
        FontMetrics font = g.getFontMetrics();


        g.drawString(bank, 1260 - font.stringWidth( bank ),80);
        g.drawString(invested,1260 - font.stringWidth( invested ),120);
        g.drawString(goal,1260 - font.stringWidth( goal ),40);
        g.drawString(stockPrice,1260 - font.stringWidth( stockPrice ),160);
        g.drawString(stockBase,1260 - font.stringWidth( stockBase ),200);

    }


    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(backGroundImg , 0,0,null);
        g.drawImage(graph , 340,210,null);
        drawText(g);
    }

    public void startGameThread() {
        thread = new Thread(this);
        thread.start();
    }
}

