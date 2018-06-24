/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airtennis;

import static airtennis.AirTennis.jframe;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author anurag
 */
public class OpenWindow extends JPanel implements ActionListener{
    
    final private JButton jbutton1,jbutton2;
    static Court crt;
    static ConnectionWindow cw;
    
    OpenWindow(){
        //----------- lower window with buttons-----------//
        jbutton1 = new JButton("Play with computer");
        jbutton2 = new JButton("Play with opponent");
        jbutton1.setActionCommand("compButton");
        jbutton2.setActionCommand("oppoButton");
        jbutton1.addActionListener(this);
        jbutton2.addActionListener(this);
        
        repaint();   
        
        setLayout(new GridLayout(4,4,4,4));
       
        add(jbutton1);
        add(jbutton2);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("oppoButton")){
            // start window disapper and game window open
            jframe.remove(AirTennis.openwindow);
            // add the court window
            
            cw = new ConnectionWindow();
            jframe.add(cw);
            jframe.setVisible(true);
            
            //  
        }
        else if(e.getActionCommand().equals("compButton")){
            //start = false;
            System.out.println("computer button is clicked");
        }
    }
    
    @Override
    public void paintComponent(Graphics g){
        
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, 600, 400);
        
        g.setColor(Color.yellow);
        g.fillRect(0, 400, 600, 200);
    }
}

