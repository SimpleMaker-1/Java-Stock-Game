package main;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame jframe = new JFrame();

        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setResizable(false);
        jframe.setTitle("Game");


        GamePanel gamepanel = new GamePanel();
        jframe.add(gamepanel);

        jframe.pack();

        jframe.setLocationRelativeTo(null);
        jframe.setVisible(true);

        gamepanel.startGameThread();
    }
}