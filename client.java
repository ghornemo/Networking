import java.net.*;
import java.io.*;
import java.util.*;

public class client {
	public static void main(String[] args) throws Exception {
		BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));
		Socket clientSocket = new Socket("localhost", 10101);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		new reader(inFromServer).start();

		while(true) {
			try {
			outToServer.writeBytes(inFromUser.readLine()+"\n");
			outToServer.flush();
			}catch(Exception SocketException) { //Server ended the game!
			}
		}
	}
}

class reader extends Thread {
	BufferedReader br;
	public void run() {
		try {
			String s;
			while(true) {
				while( (s = br.readLine()) != null)
					System.out.println(s);
			yield();
			}
		}catch(Exception e) {}
	}
	public reader(BufferedReader read) {
		br = read;
	}
}
