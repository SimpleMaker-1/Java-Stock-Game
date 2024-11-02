package Stocks;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Graph {
    private ArrayList<Integer> array;

    public Graph(int y){
        array = new ArrayList<>();
        array.add(y);
    }

    public BufferedImage addLine(int y){
        BufferedImage img = new BufferedImage(600,300,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setStroke(new BasicStroke(4));


        int next = y + array.get(array.size()-1);
        if(next<0){
            next = 0;
        }
        if(next>300){
            next = 300;
        }
        array.add(next);
        if(array.size() > 21){
            array.remove(array.get(0));
        }
        for(int i = 0 ; i < array.size()-1; i++){ // y.length should be 25
            if(array.get(i) < array.get(i+1)){
                g.setColor(Color.red);
            }
            else{
                g.setColor(Color.green);
            }
            g.drawLine(i * 30 , array.get(i), (i+1) * 30 , array.get(i+1));
        }
        g.dispose();
        return img;
    }
}
