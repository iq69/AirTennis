/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airtennis;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;

/**
 *
 * @author anurag
 */

class ReceiveThreadClient extends Thread{
    private Socket client;
    
    
    ReceiveThreadClient(Socket client){
        this.client = client;
    }
    
    // get the inputStream of the client
    // receice event 
    // update pad
    // pad2(bottom pad)
    @Override
    public void run(){
        System.out.println("thread has started\n");
        
        BufferedReader bf = null;
        
        try {
            bf = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(ReceiveThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // try catch
        try {
            bf.ready();
        } catch (IOException ex) {
            Logger.getLogger(ReceiveThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        // try catch
        
        String st = "";
        try {
            st = bf.readLine();
        } catch (IOException ex) {
            Logger.getLogger(ReceiveThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // while loop
        while(!st.equals("over")){
            if(st.equals("right")){
                    OpenWindow.crt.moveRight2();
            }
            if(st.equals("left")){
                    OpenWindow.crt.moveLeft2();
            }
            if(st.equals("enter")){
                OpenWindow.crt.play = true;
            }    
                
                
            // read from the server
            try {
                st = bf.readLine();
            } catch (IOException ex) {
                Logger.getLogger(ReceiveThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("server thread has ended\n");
        
    }    
}




public class clientWorket extends SwingWorker<Integer, Boolean> implements KeyListener { 

    private BufferedReader bf;
    static Court crt;  
    private OutputStreamWriter os;
    private PrintWriter out;
    private String IP;
    
    @Override
    protected Integer doInBackground() throws Exception {
        // connect to the server
        // wait for ack from server
        // load the game window
        //----LOOP
        // update the game window
        // send key value to the server it will also update the court
        //-------
        // game end back to first disconnect close the socket and back to W1
        
       
        // connect to the server
        //StringIP = IPTextField.getText();
        boolean start = true;
        //IP = "localhost";
        System.out.println(IP);
        int port = 9999;
        
        try {
            
            // connect to ip and port
            InetAddress inetAddress = InetAddress.getByName(IP);
            Socket clientSocket = new Socket(inetAddress, port);
            String st;
            
            // receive ack from the server
            bf = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("connected to server");
            st = receiveMessage(clientSocket);
            
            if(st.equals("ack")){
                // load the court 
                AirTennis.jframe.remove(ConnectionWindow.wait);
                
            }
            // add the court
            if(start){
                publish(start);
            }
            
            // send enter to the server and start the court
            
            os = new OutputStreamWriter(clientSocket.getOutputStream());
            out = new PrintWriter(os);
            //out.write(st);
            //out.flush();
            ReceiveThreadClient rt = new ReceiveThreadClient(clientSocket);
            rt.start();
            
            
        }catch (IOException ex) {
            Logger.getLogger(ConnectionWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("message is sent\n");
        
        return 2;
    }

    public void sendMessage(String st){
        out.write(st);
        out.flush();
    }
    
    public String receiveMessage(Socket client) throws IOException{
        
        bf.ready();
        String st;
        
        st = bf.readLine();
        return st;
    }
    
    protected void publish(Boolean start){
        AirTennis.jframe.remove(OpenWindow.cw);
                OpenWindow.crt = new Court();
                AirTennis.jframe.add(OpenWindow.crt);
                AirTennis.jframe.setVisible(true);
                OpenWindow.crt.requestFocus(true);
                OpenWindow.crt.addKeyListener(this);
                start = false;
                
    }
            
    clientWorket(String st){
        this.IP = st;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == e.VK_LEFT){
            OpenWindow.crt.moveLeft1();
            sendMessage("left\r");
            //send signal from socket
        }
        if(e.getKeyCode() == e.VK_RIGHT){
            OpenWindow.crt.moveRight1();
            sendMessage("right\r");
        }
        
        if(e.getKeyCode() == e.VK_UP){
            //OpenWindow.crt.moveLeft2();
        }
        
        if(e.getKeyCode() == e.VK_DOWN){
            //OpenWindow.crt.moveRight2();
        }
        
        if(e.getKeyCode() == e.VK_ENTER){
            OpenWindow.crt.play = true;
            sendMessage("enter\r");
        }
        if(e.getKeyCode() == e.VK_BACK_SPACE){
            AirTennis.jframe.remove(OpenWindow.crt);
            AirTennis.jframe.add(AirTennis.openwindow);
            AirTennis.openwindow.repaint();
            AirTennis.jframe.setVisible(true);
        }
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    
}
