/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import message.ChatMessage;

/**
 *
 * @author Ohjelmistokehitys
 */
public class ChatServer {
    static ArrayList<ServerClientBackEnd> clients = new ArrayList<>();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Start the server to listen port 3010
        try {
            ServerSocket server = new ServerSocket(3011);
            
            // Start to listen and wait connection
            while (true) {
                // Wait here the client
                Socket temp = server.accept();
                ServerClientBackEnd backEnd = new ServerClientBackEnd(temp);
                clients.add(backEnd);
                Thread t = new Thread(backEnd);
                t.setDaemon(true);
                t.start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void broadcastMessage(ChatMessage cm) {
        for(ServerClientBackEnd temp: clients) {
            temp.sendMessage(cm);
        }
    }
    
}
