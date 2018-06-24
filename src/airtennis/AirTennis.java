/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airtennis;

import javax.swing.JFrame;

/**
 * TO DO: change server thread while(over)
 * @author anurag
 */

public class AirTennis{

    public static JFrame jframe;
    public boolean start;
    public static OpenWindow openwindow;
    private int delay = 4;
    
    AirTennis(){
        start = true;
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        
        AirTennis airTennis = new AirTennis();
        airTennis.jframe = new JFrame();
        airTennis.jframe.setSize(600, 600);
        airTennis.jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        airTennis.jframe.setVisible(true);

        openwindow = new OpenWindow();
        airTennis.jframe.add(openwindow);
        airTennis.jframe.setVisible(true);
        
    }

}
