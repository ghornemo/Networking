	/******

		A program to demonstrate the effectiveness of threads.

	*******/
import java.util.*;
import java.net.*;
import java.io.*;

public class program {
	public static void main(String[] args) throws Exception {
		ServerSocket server = new ServerSocket(10101); //Server on port 10101.
		while(true) {
			try {
				Socket connection = server.accept();
				Socket s = connection;
				Thread p = new Player(s);
				p.start();
			} catch (Exception ex) {
			}
		}
	}
}

class logger {

	//Get name from logger file.
	public static String getName(String IP) {
		try {
			Scanner scanner = new Scanner(new File("names.txt"));
			while(scanner.hasNext()) {
				String s = scanner.nextLine();
				String[] token = s.split(",");
				if(token[0].equals(IP)) //Searching for IP
					return token[1]; //returning the name
			}
		}catch(Exception E) {System.out.println("oops, failed to get name");}
		return null;
	}

	public static void log(String IP, String name) {
		try {
			FileWriter writer = new FileWriter(new File("names.txt")); 
			writer.write(IP+","+name);
			writer.close();
		}catch(Exception e) {}
	}

}

class Player extends Thread {

	public String IP, name;
	public int wins, loses;
	public Socket socket;
	public OutputStreamWriter out;
	public BufferedReader in;

	public void run() {
		//start game here..
		try {
		Random random = new Random();
		int solution = random.nextInt(100);
		int guess = -1;
		int attempts = 0;
		write("now we will begin.");
		write("I'm thinking of a number between 0 and 100. What is it?");
		while(guess != solution) {
			String s = read();
			guess = Integer.decode(s);
			attempts++;
			if(guess == solution) {
				write("Impressive, the number was in fact "+guess+"!");
				write("You guessed the answer in "+attempts+" attempts.");
			}else if(guess > solution) {
				write("The answer is less than "+guess+".");
			}else{
				write("The answer is more than "+guess+".");
			}
		}
		write("Thanks for playing the guessing game!");
		socket.close();
		}catch(Exception e) {System.out.println("oops exception!");
					System.out.println(e);}
	}

	public void load() {
		try {
			out = new OutputStreamWriter(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			InetAddress address = socket.getInetAddress();
			String userName = logger.getName(address.toString());
			if(userName == null) {
				out.write("It seems you are a new user. Please give a name. \n");
				out.flush();
				name = in.readLine();
				logger.log(address.toString(), name);
				System.out.println("Successfully requested a name.");
				write(name+", Thank you for providing a name!");
			}else
			name = userName;
			out.write("Hello, "+name+"! Welcome to the guessing game!");
			out.flush();
		}catch(Exception e) {
		System.out.println("Oops! Problem loading player.");
		System.out.println(e);
		}
	}

	public String read() {
		try {
			return in.readLine();
		}catch(Exception e) {}
		return null;
	}

	public void write(String s) {
		try {
			out.write(s+"\n");
			out.flush();
		}catch(Exception e) {System.out.println("LOL");
					System.out.println(e);}
	}

	public Player(Socket s) {
		socket = s;
		load();
	}

}
