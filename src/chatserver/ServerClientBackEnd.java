/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.ChatMessage;

/**
 *
 * @author Ohjelmistokehitys
 */
public class ServerClientBackEnd implements Runnable {
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    
    public ServerClientBackEnd(Socket s) {
        socket = s;
    }
    
    @Override
    public void run() {
        
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            
            while (true) {
                ChatMessage cm = (ChatMessage)input.readObject();
                if(cm.isNameUpdate()) {
                    ChatServer.addUserToArray(cm.getUserName());
                }
                else if (cm.isIsPrivate()) {
                    System.out.println("Private-viesti");
                    ChatServer.sendPrivateMessage(this, cm);
                }
                else {
                    ChatServer.broadcastMessage(cm);
                }
                
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("5");
            ex.printStackTrace();
        }
        finally {
            ChatServer.removeClient(this);
        }
    }
    
    public void sendMessage(ChatMessage cm) {
        
        try {
            output.writeObject(cm);
            output.flush();
        } catch (IOException ex) {
            System.out.println("6");
            ex.printStackTrace();
        }
    }
    
}
