package com.example.practicaltest02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

//import ro.pub.cs.systems.pdsd.practicaltest02.general.Constants;
//import ro.pub.cs.systems.pdsd.practicaltest02.general.Utilities;
import android.util.Log;
import android.widget.TextView;

public class Client extends Thread {
	
	private String   word_to_send;
	
	
	private Socket   socket;
	
	public Client(
			String word_to_send
			) {
		this.word_to_send = word_to_send;
		
	}
	
	@Override
	public void run() {
		try {
			String address = "192.168.0.1";
			socket = new Socket(address, 5654);
			
			/*if (socket == null) {
				Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
			}*/
			
			BufferedReader bufferedReader = Utilities.getReader(socket);
			PrintWriter    printWriter    = Utilities.getWriter(socket);
			if (bufferedReader != null && printWriter != null) {
				printWriter.println(word_to_send);
				printWriter.flush();
				//printWriter.println(informationType);
				//printWriter.flush();
				
			} 
			socket.close();
		} catch (IOException ioException) {
			
		}
	}

}