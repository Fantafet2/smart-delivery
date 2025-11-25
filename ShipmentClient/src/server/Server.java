package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	public static void main(String[] args) {
		final String SERVER_IP = "localhost";
		final int SERVER_PORT = 9393;
		Socket socket = null;
		
		InputStreamReader in = null;
		OutputStreamWriter out = null;
		BufferedReader  bufferedReader = null;
		BufferedWriter bufferedWriter = null;
		ServerSocket serverSocket = null;
		
		try {
			 serverSocket = new ServerSocket(SERVER_PORT );
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while(true) {
			
			try {
				socket = serverSocket.accept();
				
				in = new InputStreamReader(socket.getInputStream());
				out = new OutputStreamWriter(socket.getOutputStream());
				
				bufferedReader = new BufferedReader(in);
				bufferedWriter = new BufferedWriter(out);
				
				while(true) {
					String msgFromClient = bufferedReader.readLine();
					
					System.out.println("Client: "+ msgFromClient);
					
					bufferedWriter.write("Message Received");
					bufferedWriter.newLine();
					bufferedWriter.flush();
					
					if(msgFromClient.equalsIgnoreCase("bye"))
						break;
				}
				
				socket.close();
				in.close();
				out.close();
				bufferedWriter.close();
				bufferedReader.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
