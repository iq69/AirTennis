/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airtennis;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author anurag
 */
public class Court extends JPanel implements ActionListener{
        
        public static boolean play;
        final private int crtwidth = 600;final private int crtHeight = 600;
        final private int padwidth = 150;final private int padheight = 20;
        private int pad1X = 10;final private int pad1Y = 20;
        private int pad2X = 10;final private int pad2Y = 520;
        private int ballX = 100;private int ballY = 100;
        private int dirX = -1;private int dirY = -1;
        final private int delay = 4;
        final private Timer timer;
        private boolean isServer;
        
        
        Court(boolean bt){
            isServer = bt;
            play = false;
            timer = new Timer(delay, this);
            timer.start();
        }
        
        public void paint(Graphics g){
            
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, crtwidth, crtHeight);
            
            if(play == false){
                g.setColor(Color.MAGENTA);
                if(!isServer){
                    g.drawString("Player 1", 200, 170);
                    g.drawString("Press Enter to start", 200, 200);
                    g.drawString("Press BackSpace to go to previous Menu", 200, 230);
                }
                else{
                    g.drawString("Player 2", 200, 170);
                    g.drawString("Opponent will start the Game", 200, 200);
                }
            }
            
            g.setColor(Color.red);
            g.fillRect(pad1X, pad1Y, padwidth, padheight);// client pad
            g.setColor(Color.white);
            g.drawString("Player 1", pad1X+padwidth/2, pad1Y+padheight/2);
            
            
            g.setColor(Color.GREEN);
            g.fillRect(pad2X, pad2Y, padwidth, padheight);// server pad
            g.setColor(Color.white);
            g.drawString("Player 2", pad2X+padwidth/2, pad2Y + padheight/2);
            
            
            
            g.setColor(Color.YELLOW);
            g.fillOval(ballX, ballY, 20, 20);
            
            if(new Rectangle(ballX, ballY, 20, 20).intersects(new Rectangle(pad1X, pad1Y, padwidth , padheight))){
                dirY = -dirY;
            }
            if(new Rectangle(ballX, ballY, 20, 20).intersects(new Rectangle(pad2X, pad2Y, padwidth , padheight))){
                dirY = -dirY;
            }
            
        }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(play){
            ballX += dirX;ballY += dirY;
            if(ballX <= 0 || ballX >= 570){
                dirX = -dirX;
            }
            if(ballY <= 0 || ballY >= pad2Y){
                play = false;
            }
        }
        else{
            ballX = 100;ballY = 100;
        }
        repaint();
    }

    public void moveLeft1(){
        if(pad1X - 10 > 0){
            pad1X -= 10;
        }
    }
    
    public void moveRight1(){
        if(pad1X + 10+padwidth < 600){
            pad1X += 10;
        }
    }
    
    public void moveLeft2(){
        if(pad2X - 10 > 0){
            pad2X -= 10;
        }
    }
    
    public void moveRight2(){
        if(pad2X + 10+padwidth < 600){
            pad2X += 10;
        }
    }

}
