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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;

/**
 *
 * @author anurag
 * 
 * 
 * e
 */

// receive message from the client
// make chages to client pad
class ReceiveThread extends Thread{
    private Socket client;
    
    ReceiveThread(Socket client){
        this.client = client;
    }
    
    // get the inputStream of the client
    // receice event 
    // update pad
    // pad2(bottom pad)
    public void run() {
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
        // receive data from the client.
        while(!st.equals("over")){
            if(st.equals("right")){
                
                OpenWindow.crt.moveRight1();
            }
            if(st.equals("left")){
                    OpenWindow.crt.moveLeft1();
            }
            if(st.equals("enter")){
                OpenWindow.crt.play = true;
            }    
                
                
            // try catch
            try {
                st = bf.readLine();
            } catch (IOException ex) {
                Logger.getLogger(ReceiveThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // System.out.println("server thread has ended\n");
        try {
            //client.close();
            ServerWorker.ss.close();
        } catch (IOException ex) {
            Logger.getLogger(ReceiveThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("server is here");
        AirTennis.jframe.remove(OpenWindow.crt);
        AirTennis.jframe.add(AirTennis.openwindow);
        AirTennis.openwindow.repaint();
        AirTennis.jframe.setVisible(true);
        
        
    }    
}

// this thread is for making changes on server pad
class SendThread extends Thread {
    
    private Socket client;
    
    SendThread(Socket client){
        this.client = client;
    }
    
    // send the key command to the client 
    public void run(){
            //Socket clientSocket = new Socket(IP, port);
            
            String st = "hello from server\r";
            OutputStreamWriter os = null;
        try {
            os = new OutputStreamWriter(client.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(SendThread.class.getName()).log(Level.SEVERE, null, ex);
        }
            PrintWriter out = new PrintWriter(os);
            
            //send on key note
            
            out.write(st);
            out.flush();
            System.out.println("message is sent\n");
        
    }
}


public class ServerWorker extends SwingWorker<Void, Void> implements KeyListener{

    private Socket client;
    public static ServerSocket ss;
    private OutputStreamWriter os1;
    private PrintWriter out;
    static Court crt;
    public static boolean isServer = false;
    
    @Override
    protected Void doInBackground() throws Exception {
        
        isServer = true;
        // wait for the client 
        // when connected send ack and load the court
        // game started and send key values to the client
        
        boolean start = true;
        System.out.println("wait button is clicked");
        int port = 9999;
        try {
            System.out.println("server has started");
            ss = new ServerSocket(port);
            
            System.out.println("server is waiting");
            client = ss.accept();
            
            System.out.println("client is connected");
            os1 = new OutputStreamWriter(client.getOutputStream());
            out = new PrintWriter(os1);
            
            sendMessage("ack\r");
            
            //remove the window of waiting add court
            AirTennis.jframe.remove(ConnectionWindow.clientWait);
            System.out.print("message send");
            
            if(start)
                publish(start);
           
            
            System.out.println("thread has started");
            //------------thread of client
            ReceiveThread rt = new ReceiveThread(client);
            rt.start();
            //---------------this thread handle client
            
            
            /*
            // thread for server
            SendThread sendthread = new SendThread(client);
            sendthread.start();
            */
            
            //outputstream
            

        } catch (IOException ex) {
            Logger.getLogger(ConnectionWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void sendMessage(String st){
        out.write(st);
        out.flush();
    }
    
    protected void publish(Boolean start){
        //AirTennis.jframe.remove(OpenWindow.cw);
                OpenWindow.crt = new Court(true);
                AirTennis.jframe.add(OpenWindow.crt);
                AirTennis.jframe.setVisible(true);
                OpenWindow.crt.requestFocus(true);
                OpenWindow.crt.addKeyListener(this);
                start = false;
                System.out.println("court is ready for server");
            
    }

    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == e.VK_LEFT){
            //OpenWindow.crt.moveLeft1();
            System.out.println("server is here\r");
            //send signal from socket
            
        }
        if(e.getKeyCode() == e.VK_RIGHT){
            //OpenWindow.crt.moveRight1();
        }
        
        if(e.getKeyCode() == e.VK_UP && OpenWindow.crt.play == true){
            OpenWindow.crt.moveLeft2();
            sendMessage("left\r");
        }
        
        if(e.getKeyCode() == e.VK_DOWN && OpenWindow.crt.play == true){
            OpenWindow.crt.moveRight2();
            sendMessage("right\r");
        }
        
        if(e.getKeyCode() == e.VK_ENTER){
            System.out.println("enter key is clicked");
            //OpenWindow.crt.play = true;
            
        }
        if(e.getKeyCode() == e.VK_BACK_SPACE){
            AirTennis.jframe.remove(OpenWindow.crt);
            AirTennis.jframe.add(AirTennis.openwindow);
            AirTennis.openwindow.repaint();
            AirTennis.jframe.setVisible(true);
        
            sendMessage("over\r");
            try {
                ss.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerWorker.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }
        
    
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    
}
