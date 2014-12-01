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
    //static ArrayList<String> users = new ArrayList<>();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Start the server to listen port 3010
        try {
            ServerSocket server = new ServerSocket(3010);
            
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
            System.out.println("7");
            ex.printStackTrace();
        }
    }
    
    public static void broadcastMessage(ChatMessage cm) {
        if(cm.isPrivateMessage()) {
            for(ServerClientBackEnd temp: clients) {
                if(temp.getUserName().equals(cm.getPrivateName())) {
                    System.out.printf("Löytyi käyttäjä: %s\n",temp.getUserName());
                    temp.sendMessage(cm);
                }
                else if(temp.getUserName().equals(cm.getUserName())) {
                    temp.sendMessage(cm);
                }
            }
        }
        else {
            for(ServerClientBackEnd temp: clients) {
                temp.sendMessage(cm);
            }
        }
        
    }
     
    public static void updateUserList() {
        ChatMessage cm = new ChatMessage();
        cm.setUserListUpdate(true);
        StringBuilder userList = new StringBuilder();
        for(ServerClientBackEnd a: clients) {
            if(!a.getUserName().isEmpty()) {
                userList.append(a.getUserName()+",");
            }
        }
        cm.setChatMessage(userList.toString());
        broadcastMessage(cm);
    }
    
    public static void removeClient(ServerClientBackEnd s) {
        System.out.printf("poistettiin käyttäjä %s\n",s.getUserName());
        clients.remove(s);
        System.out.printf("clients: %d\n",clients.size());
        updateUserList();
    }
    
}
