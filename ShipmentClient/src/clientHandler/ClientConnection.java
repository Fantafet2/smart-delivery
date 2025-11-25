package clientHandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Serializable;
import java.net.Socket;
import java.io.*;
import java.net.*;

import shared.Driver;
import shared.ServerResponse;
import shared.SharedCustomer;


public class ClientConnection {

    private Socket socket;
    private ObjectOutputStream objOut;
    private ObjectInputStream objIn;
    private boolean success;
    
    String ip = "10.40.1.215";
    
    int port = 9393;

    public ClientConnection() throws Exception {
        socket = new Socket(ip, port);
        objOut = new ObjectOutputStream(socket.getOutputStream());
        objIn = new ObjectInputStream(socket.getInputStream());
    }

    // Send any object (ClientRequest, Driver, etc.)
    public void sendFunciton(Object obj) throws Exception {
        objOut.writeObject(obj);
        objOut.flush();
    }

    // Receive response from server
    public <T> T receive() throws Exception {
        Object response = objIn.readObject();
        return (T) response;  // caller casts to correct type
    }

    // Close socket safely
    public void close() {
        try { objOut.close(); } catch (IOException e) {e.printStackTrace();}
        try { objIn.close(); } catch (IOException e) {e.printStackTrace();}
        try { socket.close(); } catch (IOException e) {e.printStackTrace();}
    }
}
